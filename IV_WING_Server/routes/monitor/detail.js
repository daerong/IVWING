const express = require('express');
const router = express.Router();
const async = require('async');

const pool = require('../../private_module/dbPool');
const jsonVerify = require('../../private_module/jwtVerify');

const jwtSecret = require('../../config/jwtSecret');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/:id', function(req, res, next) {
	let getDetailTaskArray = [
		(callback) => {
			var parsedId = parseInt(req.params.id, 10);
			console.log(parsedId);

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
			} else callback(null, parsedId);
		},
		(userId, callback) => {
			pool.getConnection((connectingError, connectingResult) => {
		        if (connectingError) {
		          	res.status(500).send({
		            	status: "Fail",
		            	msg: "DB connection has failed"
		          	});
		          	callback("DB connection error has occured : " + connectingError);
		        } else callback(null, connectingResult, userId);
	    	});
		},
		(connection, userId, callback) => {
			let loadTableQuery = "SELECT \
            record_id, record_gtt, record_stat, \
            iv_id, iv_name, iv_max, iv_now, iv_stat, ROUND((iv_now/iv_max)*100, 1) AS iv_percent, ROUND((iv_now/record_gtt)*15, 0) AS iv_time, \
            user_id, user_name, user_age, user_gender, user_room, \
            linker_id, linker_battery\
            FROM (SELECT * \
	            FROM (SELECT * \
	            	FROM (SELECT * FROM record WHERE record_stat = 'O' AND record_user = ?) records \
	            	LEFT JOIN iv ON records.record_iv = iv.iv_id) non_user \
	            left join user on non_user.record_user = user.user_id) non_linker \
	        left join linker on non_linker.user_linker = linker.linker_id";

 			connection.query(loadTableQuery, [userId], (loadQueryError, loadQueryResult) => {
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
		(userData, callback) => {
            if (userData[0] == undefined) {
                res.status(201).send({
                    status: "Fail",
                    msg: "data empty"
                });

                callback("data empty");
            } else {
				res.status(201).send({
					status: "Success",
					userData: userData,
					msg: "Successful delete target reservation"
				});

                callback(null, "Success");
            }
        }
	];

	async.waterfall(getDetailTaskArray, (asyncError, asyncResult) => {
		if(asyncError) console.log("Async has error : " + asyncError);
		else console.log("Async has success : " + asyncError);
	});
});

module.exports = router;