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
            var date_start = new Date();
            var date_end = new Date();
            date_start.setHours(0, 0, 0, 0);
            date_end.setHours(23, 59, 59, 0);

            let loadTableQuery = " select * from step where step_user = ? AND DATE(step_date) BETWEEN ? AND ? order by step_date";
           
            connection.query(loadTableQuery, [req.body.step_user, date_start, date_end], (loadQueryError, loadQueryResult) => {
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
        (stepData, callback) => {
            if (stepData[0] == undefined) {
                 res.status(201).send({
                    status: "Success",
                    msg: "Empty data"
                });

                callback("data empty");
            } else {
                console.log(stepData);
                res.status(201).send({
                    status : "Success",
                    data: stepData,
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