const express = require('express');
const router = express.Router();

const searchLocation = require('./search');

router.use('/search', searchLocation);

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Plan' });
});

module.exports = router;