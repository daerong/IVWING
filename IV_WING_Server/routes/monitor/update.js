const express = require('express');
const router = express.Router();
const async = require('async');

const pool = require('../../private_module/dbPool');
const jsonVerify = require('../../private_module/jwtVerify');

const jwtSecret = require('../../config/jwtSecret');

router.post('/user', function(req, res, next) {
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
			let updateTableQuery;
			let updateTableVar;
			if(targetData.user_name == null){
				updateTableQuery = "update user set user_room = ? where user_id = ?";
				updateTableVar = [targetData.user_room, targetData.user_id];
			}else if(targetData.user_room == null){
				updateTableQuery = "update user set user_name = ? where user_id = ?";
				updateTableVar = [targetData.user_name, targetData.user_id];
			}else{
				updateTableQuery = "update user set user_name = ?, user_room = ? where user_id = ?";
				updateTableVar = [targetData.user_name, targetData.user_room, targetData.user_id];
			}

 			connection.query(updateTableQuery, updateTableVar, (updateQueryError, updateQueryResult) => {
                if (updateQueryError) {
                    connection.release();
                    res.status(500).send({
                        status: "Fail",
                        msg: "Can't update table"
                    });

                    callback("error : " + updateQueryError);
                } else {
                    connection.release();

					res.status(201).send({
						status: "Success",
						msg: "Successful update user"
					});

	                callback(null, "Success");
                }
            });
		}
	];

	async.waterfall(getTargetTaskArray, (asyncError, asyncResult) => {
		if(asyncError) console.log("Async has error : " + asyncError);
		else console.log("Async has success : " + asyncError);
	});
});

router.post('/iv', function(req, res, next) {
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
			if(targetData.iv_old == null){
				console.log("targetData.iv_old : " + targetData.iv_old);
				callback(null, connection, targetData);
			}else{
				updateTableQuery = "update iv set iv_stat = ? where iv_id = ?";

	 			connection.query(updateTableQuery, ['N', targetData.iv_old], (updateQueryError, updateQueryResult) => {
	                if (updateQueryError) {
	                    connection.release();
	                    res.status(500).send({
	                        status: "Fail",
	                        msg: "Can't update table"
	                    });

	                    callback("error : " + updateQueryError);
	                } else callback(null, connection, targetData);
	            });
			}
			
		},
		(connection, targetData, callback) => {
			updateTableQuery = "update iv set iv_stat = ? where iv_id = ?";

 			connection.query(updateTableQuery, ['U', targetData.iv_new], (updateQueryError, updateQueryResult) => {
                if (updateQueryError) {
                    connection.release();
                    res.status(500).send({
                        status: "Fail",
                        msg: "Can't update table"
                    });

                    callback("error : " + updateQueryError);
                } else callback(null, connection, targetData);
            });
		},
		(connection, targetData, callback) => {
			updateTableQuery = "update record set record_iv = ? where record_user = ?";

 			connection.query(updateTableQuery, [targetData.iv_new, targetData.user_id], (updateQueryError, updateQueryResult) => {
                if (updateQueryError) {
                    connection.release();
                    res.status(500).send({
                        status: "Fail",
                        msg: "Can't update table"
                    });

                    callback("error : " + updateQueryError);
                } else {
                    connection.release();

					res.status(201).send({
						status: "Success",
						msg: "Successful update record and iv"
					});

	                callback(null, "Success");
                }
            });
		}
	];

	async.waterfall(getTargetTaskArray, (asyncError, asyncResult) => {
		if(asyncError) console.log("Async has error : " + asyncError);
		else console.log("Async has success : " + asyncError);
	});
});


module.exports = router;