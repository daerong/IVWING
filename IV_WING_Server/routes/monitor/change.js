const express = require('express');
const router = express.Router();
const async = require('async');

const pool = require('../../private_module/dbPool');
const jsonVerify = require('../../private_module/jwtVerify');

const jwtSecret = require('../../config/jwtSecret');

router.post('/', function(req, res, next) {
	let getTargetTaskArray = [
		(callback) => {
			var targetData = JSON.parse(req.body.json);

		    var jwtToken = req.cookies.token;		
			let verifyToken = jsonVerify.verifyToken(jwtToken, jwtSecret.jwt_secret);

			console.log("verifyToken =" + JSON.stringify(verifyToken));		

			if(verifyToken.message == "login in first"){
				res.writeHead(302, {'Location': '/'});
				res.end();
			}else if (verifyToken.adminId === null){
				res.status(403).send({
					status : "Fail",
					msg : verifyToken.message
				});
			} else callback(null, verifyToken.adminId, targetData);
		},
		(adminId, targetData, callback) => {
			pool.getConnection((connectingError, connectingResult) => {
		        if (connectingError) {
		          	res.status(500).send({
		            	status: "Fail",
		            	msg: "DB connection has failed"
		          	});
		          	callback("DB connection error has occured : " + connectingError);
		        } else callback(null, connectingResult, targetData);
	    	});
		},
		(connection, targetData, callback) => {
			let loadTableQuery = "select * from iv where iv_id = ?";

 			connection.query(loadTableQuery, [targetData.ivId], (loadQueryError, loadQueryResult) => {
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
		(ivData, callback) => {
            if (ivData[0] == undefined) {
                res.status(201).send({
                    status: "Fail",
                    msg: "data empty"
                });

                callback("data empty");
            } else {
				res.status(201).send({
					status: "Success",
					ivData: ivData[0],
					msg: "Successful delete target reservation"
				});

                callback(null, "Success");
            }
        }
	];

	async.waterfall(getTargetTaskArray, (asyncError, asyncResult) => {
		if(asyncError) console.log("Async has error : " + asyncError);
		else console.log("Async has success : " + asyncError);
	});
});

module.exports = router;