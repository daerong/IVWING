const mysql = require('mysql');
const dbHostConfig = require('../config/dbConfig');

const dbSetting = {
	host : dbHostConfig.dbConfig.host,
	port : dbHostConfig.dbConfig.port,
	user : dbHostConfig.dbConfig.user, 
	password : dbHostConfig.dbConfig.password,
	database : dbHostConfig.dbConfig.database,
	connectionLimit : dbHostConfig.dbConfig.connectionLimit
};

module.exports = mysql.createPool(dbSetting);