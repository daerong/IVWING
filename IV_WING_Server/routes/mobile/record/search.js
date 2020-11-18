const express = require('express');
const router = express.Router();

const pool = require('../../../private_module/dbPool');
const async = require('async');  
const jsonWebToken = require('jsonwebtoken');

const jsonVerify = require('../../../private_module/jwtVerify');

router.post('/', function(req, res, next) {
    let memberVerifyTaskArray = [
        (callback) => {
            pool.getConnection((connectingError, connectingResult) => {
                if (connectingError) {
                    res.status(500).send({
                        status: "Fail",
                        msg: "DB connection has failed"
                    });
                    callback("DB connection error has occured : " + connectingError);
                } else callback(null, connectingResult);
            });
        },
        (connection, callback) => {
            let loadTableQuery = "select record_id, record_gtt, record_stat, iv_id, iv_name, iv_max, iv_now, iv_stat, ROUND((iv_now/iv_max)*100, 0) AS iv_percent, ROUND((iv_now/record_gtt)*15, 0) AS iv_time \
            from (select * from record where record_user = ? AND record_stat = 'O') records left join iv on records.record_iv = iv.iv_id";
           
            connection.query(loadTableQuery, [req.body.user_id], (loadQueryError, loadQueryResult) => {
                if (loadQueryError) {
                    connection.release();
                    res.status(500).send({
                        status: "Fail",
                        msg: "Can't load table"
                    });

                    callback("error : " + loadQueryError);
                } else {
                    connection.release();
                    callback(null, loadQueryResult);
                }
            });
        },
        (recordData, callback) => {
            if (recordData[0] == undefined) {
                res.status(201).send({
                    status: "Success",
                    msg: "Empty data"
                });

                callback("data empty");
            } else {
                console.log(recordData);
                res.status(201).send({
                    status : "Success",
                    data: recordData,
                    msg : 'Successfully return recordArray'
                });
                callback(null, "Success");
            }
        }
    ];

    async.waterfall(memberVerifyTaskArray, (asyncError, asyncResult) => {
        if(asyncError) console.log("Async has error : " + asyncError);
        else console.log("Async has success : " + asyncResult);
    });
});

module.exports = router;