const express = require('express');
const router = express.Router();

const searchLocation = require('./search');
const updateLocation = require('./update');
const todyLocation = require('./today');


router.use('/search', searchLocation);
router.use('/update', updateLocation);
router.use('/today', todyLocation);

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Step' });
});

module.exports = router;