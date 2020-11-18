const express = require('express');
const router = express.Router();
const async = require('async');

const pool = require('../../private_module/dbPool');
const jsonVerify = require('../../private_module/jwtVerify');

const jwtSecret = require('../../config/jwtSecret');

/* GET home page. */
router.get('/', function(req, res, next) {
    let memberVerifyTaskArray = [
        (callback) => {
            var jwtToken = req.cookies.token;       
            let verifyToken = jsonVerify.verifyToken(jwtToken, jwtSecret.jwt_secret);

            console.log("verifyToken =" + JSON.stringify(verifyToken));     

            if(verifyToken.message == "login in first"){
                res.writeHead(302, {'Location': '../../'});
                res.end();
            }else if (verifyToken.userId === null){
                res.writeHead(302, {'Location': '../../'});
                res.end();
            } else callback(null, verifyToken.userId);
        },
        (adminId, callback) => {
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
            let loadTableQuery = "select \
            record_id, record_gtt, record_stat, \
            iv_id, iv_name, iv_max, iv_now, iv_stat, ROUND((iv_now/iv_max)*100, 1) AS iv_percent, ROUND((iv_now/record_gtt)*15, 0) AS iv_time, \
            user_id, user_name, SUBSTRING(user_phone, 8, 4) as user_phone, user_age, user_gender, user_room, \
            linker_id, linker_battery\
            from (select * \
                from (select * \
                    from (select * from record where record_stat = 'O') records \
                    left join iv on records.record_iv = iv.iv_id) non_user \
                left join user on non_user.record_user = user.user_id) non_linker \
            left join linker on non_linker.user_linker = linker.linker_id where non_linker.user_name is null order by user_room, user_name";

            connection.query(loadTableQuery, (loadQueryError, loadQueryResult) => {
                if (loadQueryError) {
                    connection.release();
                    res.status(500).send({
                        status: "Fail",
                        msg: "Can't load table"
                    });

                    callback("error : " + loadQueryError);
                } else {
                    callback(null, connection, loadQueryResult);
                }
            });
        },
        (connection, emptyData, callback) => {
            let loadTableQuery = "SELECT user_room, count(record_id) as user_cnt \
            FROM (SELECT * \
                FROM record \
                LEFT JOIN user \
                ON record.record_user = user.user_id) all_tb \
            WHERE user_room IS NOT NULL AND user_linker IS NOT NULL \
            GROUP BY user_room \
            ORDER BY user_room";

            connection.query(loadTableQuery, (loadQueryError, loadQueryResult) => {
                if (loadQueryError) {
                    connection.release();
                    res.status(500).send({
                        status: "Fail",
                        msg: "Can't load table"
                    });

                    callback("error : " + loadQueryError);
                } else {
                    callback(null, connection, emptyData, loadQueryResult);
                }
            });
        },
        (connection, emptyData, roomGroupArr ,callback) => {
            let loadTableQuery = "select \
            record_id, record_gtt, record_stat, \
            iv_id, iv_name, iv_max, iv_now, iv_stat, ROUND((iv_now/iv_max)*100, 1) AS iv_percent, ROUND((iv_now/record_gtt)*15, 0) AS iv_time, \
            user_id, user_name, SUBSTRING(user_phone, 8, 4) as user_phone, user_age, user_gender, user_room, \
            linker_id, linker_battery\
            from (select * \
                from (select * \
                    from (select * from record where record_stat = 'O') records \
                    left join iv on records.record_iv = iv.iv_id) non_user \
                left join user on non_user.record_user = user.user_id) non_linker \
            left join linker on non_linker.user_linker = linker.linker_id where non_linker.user_name is not null order by user_room, user_name";

            connection.query(loadTableQuery, (loadQueryError, loadQueryResult) => {
                if (loadQueryError) {
                    connection.release();
                    res.status(500).send({
                        status: "Fail",
                        msg: "Can't load table"
                    });

                    callback("error : " + loadQueryError);
                } else {
                    connection.release();
                    callback(null, emptyData, roomGroupArr, loadQueryResult);
                }
            });
        },
        (emptyData, roomGroupArr, recordArr, callback) => {
            if (roomGroupArr[0] == undefined || recordArr[0] == undefined) {
                callback("data empty");

                res.status(201).send({
                    status: "Fail",
                    msg: "data empty"
                });
            } else {
                var roomArr = new Array(roomGroupArr.length);
                for (var index = 0; index < roomGroupArr.length; index++) {
                    roomArr[index] = {
                        room_index : roomGroupArr[index].user_room,
                        room_cnt : roomGroupArr[index].user_cnt,
                        components : []
                    }
                }

                var pastRoom = 0;
                var roomIndex = -1;
                for (var index = 0; index < recordArr.length; index++) {
                    if(pastRoom == 0 || pastRoom != recordArr[index].user_room){
                        roomIndex++;
                    }
                    roomArr[roomIndex].components.push(recordArr[index]);
                    pastRoom = recordArr[index].user_room;
                }

                console.log(roomArr);

                res.render('sort_room', {
                    status: "Success",
                    emptyData: emptyData,
                    recordData: roomArr,
                    msg: "Successful toss result array"
                });

                callback(null, "Success");
            }
        }
    ];

    async.waterfall(memberVerifyTaskArray, (asyncError, asyncResult) => {
        if(asyncError) console.log("Async has error : " + asyncError);
        else console.log("Async has success : " + asyncError);
    });
});

module.exports = router;