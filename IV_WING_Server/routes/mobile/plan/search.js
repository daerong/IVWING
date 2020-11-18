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
            date_start = new Date(req.body.year, req.body.mon, req.body.day, req.body.hour, req.body.min, req.body.sec, 0);
            date_end = new Date(req.body.year, req.body.mon, req.body.day, 23, 59, 59, 0);

            let loadTableQuery = "select plan_id, plan_date, plan_type, plan_user, plan_dept, plan_doctor, dept_name, doctor_name, doctor_type from (select * from (select * from plan where plan_user = ? AND DATE(plan_date) BETWEEN ? AND ? order by plan_date) plans left join dept on plans.plan_dept = dept.dept_id) subQuery left join doctor on subQuery.plan_doctor = doctor.doctor_id";
           
            connection.query(loadTableQuery, [req.body.user_id, date_start, date_end], (loadQueryError, loadQueryResult) => {
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
        (planData, callback) => {
            if (planData[0] == undefined) {
                res.status(201).send({
                    status: "Success",
                    msg: "Empty data"
                });

                callback("data empty");
            } else {
                console.log(planData);
                res.status(201).send({
                    status : "Success",
                    data: planData,
                    msg : 'Successfully return planArray'
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

        // (callback) => {
        //     const secret = req.app.get('jwt-secret')
        //     var jwtToken = req.headers.token; 
        //     let verifyToken = jsonVerify.verifyToken(jwtToken, secret);
        //     console.log("verifyToken = " + JSON.stringify(verifyToken));  
        //     console.log("token = " + jwtToken);
   

        //     if (verifyToken.data === "expired token") {
        //         callback("Expired token");

        //         res.status(400).send({
        //             status: "Fail",
        //             msg: "Expired token"
        //         });
        //     } else if (verifyToken.data === "invalid token") {
        //         callback("Invalid token");

        //         res.status(400).send({
        //             status: "Fail",
        //             msg: "Invalid token"
        //         });
        //     }else if (verifyToken.data === "jwt must be provided") {
        //         callback("jwt must be provided");

        //         res.status(400).send({
        //             status: "Fail",
        //             msg: "login in first"
        //         });
        //     } else if (verifyToken.data === "JWT fatal error") {
        //         callback("JWT fatal error");

        //         res.status(500).send({
        //             status: "Fail",
        //             msg: "JWT fatal error"
        //         });
        //     } else {
        //         callback(null, verifyToken.userId);
        //     }
        // },
        // (userId, callback) => {
        //     pool.getConnection((connectingError, connectingResult) => {
        //         if (connectingError) {
        //             res.status(500).send({
        //                 status: "Fail",
        //                 msg: "DB connection has failed"
        //             });
        //             callback("DB connection error has occured : " + connectingError);
        //         } else callback(null, connectingResult, userId);
        //     });
        // },