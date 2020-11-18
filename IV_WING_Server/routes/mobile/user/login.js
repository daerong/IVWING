const express = require('express');
const router = express.Router();

const pool = require('../../../private_module/dbPool');
const async = require('async');
const jsonWebToken = require('jsonwebtoken');

const crypto = require('crypto');


router.post('/', (req, res) => {
    let loginTaskArray = [
        (callback) => {
            pool.getConnection((connectingError, connectingResult) => {
                if (connectingError) {
                    callback("DB connection error has occured : " + connectingError);

                    res.status(500).send({
                        status: "Fail",
                        msg: "DB connection has failed"
                    });
                } else callback(null, connectingResult);
            });
        },
        (connection, callback) => {
	        let selectQuery = "select * from user where user_email=?";
	        connection.query(selectQuery, [req.body.user_email], (queryError, queryResult) => {
	            if (queryError) {
	                connection.release();
	                callback("Query has sentance error : " + queryError);

	                res.status(500).send({
	                    status: "Fail",
	                    msg: "Query has sentance error"
	                });
	            } else {
	                connection.release();
	                callback(null, queryResult);
	            }
	        });
        },
        (userData, callback) => {
            if (userData[0] === undefined) {
                callback("This user has not recognized");

                res.status(500).send({
                    status: "Fail",
                    msg: "You're not our member"
                });
            } else {
                crypto.pbkdf2(req.body.user_pwd, userData[0].user_salt, 100000, 64, 'SHA512', (hashingError, hashingResult) => {
                    if(hashingError) {
                        callback("Hashing has failed : " + hashingError);

                        res.status(500).send({
                            status : "Fail",
                            msg : "Hashing has failed"
                        });
                    } else if(hashingResult.toString('base64') === userData[0].user_pwd) {
                        callback(null, userData[0]);
                    } else {
                        callback("Passowrd does not match");

                        res.status(201).send({
                            status : "Fail",
                            msg : "Passowrd does not match"
                        });
                    }
                });
            }
        },
        (userData, callback) => {
            const secret = req.app.get('jwt-secret')
            let option = {
                algorithm: "HS512",
                expiresIn: 3600 * 1 * 1 // 초 * 시간 * 일
            };

            let privateData = {
                user_id: userData.user_id, 
                user_name: userData.user_name, 
                user_email: userData.user_email, 
                user_phone: userData.user_phone, 
                user_age: userData.user_age, 
                user_gender: userData.user_gender, 
                user_stat: userData.user_stat, 
                user_room: userData.user_room, 
                user_linker: userData.user_linker
            }

            //저기서 const 로 발급받은 key 로 key.secret 으로 사용하는거야 
            //저기 안에 파일 들어가서 secret 받아오는 형태! 

            jsonWebToken.sign(privateData, secret, option, (err, token) => {
                if (err) {
                    callback('token err' + err);

                    res.status(500).send({
                        status : "Fail",
                        msg: 'token err'
                    });
                } else {
                    callback(null, "token ok");

                    res.status(200).send({
                        status : "Success",
                        token: token, //메일과 닉네임 정보 담긴 token 발급!
                        data: privateData,
                        msg : 'Successfully issue Json Web Token(JWT)'
                    });
                }
            });
        }
    ];

    async.waterfall(loginTaskArray, (asyncError, asyncResult) => {
        if (asyncError) console.log("Async has error : " + asyncError);
        else console.log("Async has success : " + asyncResult);
    });
});

module.exports = router;