const express = require('express');
const router = express.Router();

const loginLocation = require('./login');
const signupLocation = require('./signup');

router.use('/login', loginLocation);
router.use('/signup', signupLocation);

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'User' });
});


module.exports = router;