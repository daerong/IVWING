var express = require('express');
var router = express.Router();

const newLocation = require('./new');
const roomLocation = require('./room');
const leftLocation = require('./left');
const batteryLocation = require('./battery');
// const detailLocation = require('./detail');
const loadLocation = require('./load');
const changeLocation = require('./change');
const updateLocation = require('./update');

router.use('/new', newLocation);
router.use('/room', roomLocation);
router.use('/left', leftLocation);
router.use('/battery', batteryLocation);
// router.use('/detail', detailLocation);
router.use('/load', loadLocation);
router.use('/change', changeLocation);
router.use('/update', updateLocation);

/* GET home page. */
router.get('/', (req, res) => {
	res.writeHead(302, {'Location': '/monitor/new'});
	res.end();
});

module.exports = router;
