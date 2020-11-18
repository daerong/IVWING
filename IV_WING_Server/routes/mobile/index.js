var express = require('express');
var router = express.Router();

const userDirectory = require('./user/index');
const planDirectory = require('./plan/index');
const stepDirectory = require('./step/index');
const recordDirectory = require('./record/index');

router.use('/user', userDirectory);
router.use('/plan', planDirectory);
router.use('/step', stepDirectory);
router.use('/record', recordDirectory)

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

module.exports = router;
