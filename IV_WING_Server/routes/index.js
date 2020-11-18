var express = require('express');
var router = express.Router();
const async = require('async');
const crypto = require('crypto');
const jsonWebToken = require('jsonwebtoken');
const pool = require('../private_module/dbPool');

const mobileDirectory = require('./mobile/index');
const monitorDirectory = require('./monitor/index');

router.use('/m', mobileDirectory);
router.use('/monitor', monitorDirectory);

router.post('/', (req, res) => {
    let loginTaskArray = [
        (callback) => {
            var parsedJSON = JSON.parse(req.body.json);
            console.log(parsedJSON);
            pool.getConnection((connectingError, connectingResult) => {
                if (connectingError) {
                    callback("DB connection error has occured : " + connectingError);

                    res.status(500).send({
                        status: "Fail",
                        msg: "DB connection has failed"
                    });
                } else callback(null, connectingResult, parsedJSON);
            });
        },
        (connection, parsedJSON, callback) => {
	        let selectQuery = "select * from user where user_email=?";
	        connection.query(selectQuery, [parsedJSON.user_email], (queryError, queryResult) => {
	            if (queryError) {
	                connection.release();
	                callback("Query has sentance error : " + queryError);

	                res.status(500).send({
	                    status: "Fail",
	                    msg: "Query has sentance error"
	                });
	            } else {
	                connection.release();
	                callback(null, queryResult, parsedJSON.user_pwd);
	            }
	        });
        },
        (userArr, userPwd, callback) => {
            if (userArr[0] === undefined) {
                callback("This user has not recognized");

                res.status(401).send({
                    status: "Fail",
                    msg: "You're not our member"
                });
            } else {
                crypto.pbkdf2(userPwd, userArr[0].user_salt, 100000, 64, 'SHA512', (hashingError, hashingResult) => {
                    if(hashingError) {
                        callback("Hashing has failed : " + hashingError);

                        res.status(500).send({
                            status : "Fail",
                            msg : "Hashing has failed"
                        });
                    } else if(hashingResult.toString('base64') === userArr[0].user_pwd) {
                        callback(null, userArr[0]);
                    } else {
                        callback("Passowrd does not match");

                        res.status(400).send({
                            status : "Fail",
                            msg : "Passowrd does not match"
                        });
                    }
                });
            }
        },
        (userData, callback) => {
            console.log(userData);
            if(userData.user_stat == 'A'){
                callback(null, userData.user_id);
            }
            else{
                callback("Need to permission for admin");

                res.status(401).send({
                    status: "Fail",
                    msg: "Need to permission for admin"
                });
            }
        },
        (userId, callback) => {
            let jwtSecret = req.app.get('jwt-secret');
            let option = {
                algorithm: "HS512",
                expiresIn: 3600 * 24 * 3 // 초 * 시간 * 일
            };

            let payload = {
                user_id : userId
            }
            //저기서 const 로 발급받은 key 로 key.secret 으로 사용하는거야 
            //저기 안에 파일 들어가서 secret 받아오는 형태! 
            jsonWebToken.sign(payload, jwtSecret, option, (err, token) => {
                if (err) {
                    callback('token error' + err);

                    res.status(500).send({
                       	status : "Fail",
                        msg: 'token error'
                    });
                } else {
                    callback(null, "token ok");

                    res.cookie('token', token, { expires: new Date(Date.now() + 60 * 60 * 1000), httpOnly: true });

                    res.status(201).send({
                        status : "Success",
                        data: token, //정보 담긴 token 발급!
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

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('login', { title: 'Express' });
});
module.exports = router;
