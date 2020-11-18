const express = require('express');
const router = express.Router();

const pool = require('../../../private_module/dbPool');
const async = require('async');  

const crypto = require('crypto');


router.post('/', (req, res) => {
   let signinTaskArray = [
      (callback) => {
         pool.getConnection((connectingError, connectingResult) => {
            if(connectingError) {
               callback("DB connection has failed : " + connectingError);

               res.status(500).send({
                  status : "Fail",
                  msg : "DB connection has failed"
               })
            } else {
               callback(null, connectingResult);
            }
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
                callback(null, connection, queryResult);
            }
         });
      },
      (connection, userData, callback) => {
         if (userData[0] !== undefined) {
            connection.release();
            callback("This email already exist.");

            res.status(201).send({
               status: "Fail",
               msg: "This email already exist."
            });
         } else {
            callback(null, connection);
         }
      },
      (connection, callback) => {
        crypto.randomBytes(32, (saltingError, saltingResult) => {
            if(saltingError) {
               connection.release();
           	   callback("Salting has failed : " + saltingError);

            	res.status(500).send({
                	status : "Fail",
               	msg : "Salting has failed"
            	});
            }
            else callback(null, connection, saltingResult.toString('base64'));
         });
      },
      // password가 salt와 함께 넘어와서 hashing됨
      (connection, salt, callback) => {
      	console.log(req.body);
         crypto.pbkdf2(req.body.user_pwd, salt, 100000, 64, 'SHA512', (hashingError, hashingResult) => {
            if(hashingError) {
                  connection.release();
               	callback("Hashing has failed : " + hashingError);

               	res.status(500).send({
                  	status : "Fail",
                  	msg : "Hashing has failed"
               	});
            } else {
               callback(null, connection, salt, hashingResult.toString('base64'));
            }
         });
      },
      (connection, salt, hashedPwd, callback) => {
         let insertQuery = "insert into user values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            connection.query(insertQuery, [null, req.body.user_name, req.body.user_email, hashedPwd, salt, req.body.user_phone, req.body.user_age, req.body.user_gender, null, null, null, null], (queryError) => {
               if(queryError) {
                  connection.release();
                  callback("Query has sentance error : " + queryError);

                  res.status(500).send({
                     status : "Fail",
                     msg : "Query has sentance error" 
                  });
               } else {
                  connection.release();
                  callback(null, "Successful join");

                  res.status(201).send({
                     status : "Success",
                     msg : "Successful join!"
                  });
               }
            });
          }
   ];
      async.waterfall(signinTaskArray, (asyncError, asyncResult) => {
      if(asyncError) console.log("Async has error : " + asyncError);
      else console.log("Async has success : " + asyncResult);
   });
});

module.exports = router;