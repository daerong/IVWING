module.exports.verifyToken = function(token, secret) {
	const jsonWebToken = require('jsonwebtoken');
	
	let returnObject = {
		userId : null,
		userName : null,
		userEmail : null,
		userPhone : null,
		userAge : null,
		userGender : null,
		data : null
	};

	jsonWebToken.verify(token, secret, (verifyErr, verifyData) => {
	    if (verifyErr) {
	        if (verifyErr.message === 'jwt expired') {
	        	returnObject.data = "expired token";
	        } else if (verifyErr.message === 'invalid token') {
	        	returnObject.data = "Error : invalid token";
	        } else if (verifyErr.message === "jwt must be provided"){
				returnObject.data = "jwt must be provided";
	        } else {
		        console.log(verifyErr); 
	        	returnObject.data = "JWT fatal error";
	        }
	    } else {
	        returnObject.userId = verifyData.user_id;
	        returnObject.userName = verifyData.user_name;
	        returnObject.userEmail = verifyData.user_email;
	        returnObject.userPhone = verifyData.user_phone;
	        returnObject.userAge = verifyData.user_age;
	        returnObject.userGender = verifyData.user_gender;
	        returnObject.userStat = verifyData.user_stat;
	        returnObject.data = "Success";
	    }
	});

	return returnObject;
}