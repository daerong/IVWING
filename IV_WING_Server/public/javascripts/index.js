jQuery(function($){
  function funLoad(){
    var var_height = $(window).height();
    $('body').css({'height': var_height+'px'});
  }
  window.onload = funLoad;
  window.onresize = funLoad;

  $('#keep_log_label').click(function(){
    $('#keep_log_checkbox').prop('checked', !$('#keep_log_checkbox').prop('checked'));
  });

  $("#userEmail").keyup(function(key){
    if(key.keyCode == 13){
      if($("#userEmail").val() == ""){
        alert("이메일은 필수 입력사항입니다.");
        return;
      }else if($("#userPwd").val() == ""){
        alert("휴대폰 번호는 필수 입력사항입니다.");
        return;
      }
      var userEmail = $("#userEmail").val();
      var userPwd = $("#userPwd").val();

      var reqJSON = {
        "user_email" : userEmail,
        "user_pwd" : userPwd
      };

      var strJSON = JSON.stringify(reqJSON);

      var replaceText = "";

      $.ajax({
        type : "POST",
        url : "/",
        data : 
        { json : strJSON },
        dataType : 'json',
        success : function(result){
          if(result.status == "Success"){ 
            window.location.href='./monitor';
          } else{
            alert(result.msg);
          }
        },
        error : function(error){
          if(error.status == 400){
            alert("비밀번호 확인바랍니다.");
          }else if(error.status == 401){
            alert("[관리자]계정 확인바랍니다.");
          }else if(error.status == 500){
            alert("서버에 문제가 있습니다.");
          }else{
            alert("Error : \n" + JSON.stringify(error.responseJSON.msg));
          }
        }
      });// end ajax
    }
  });

  $("#userPwd").keyup(function(key){
    if(key.keyCode == 13){
      if($("#userEmail").val() == ""){
        alert("이메일은 필수 입력사항입니다.");
        return;
      }else if($("#userPwd").val() == ""){
        alert("휴대폰 번호는 필수 입력사항입니다.");
        return;
      }
      var userEmail = $("#userEmail").val();
      var userPwd = $("#userPwd").val();

      var reqJSON = {
        "user_email" : userEmail,
        "user_pwd" : userPwd
      };

      var strJSON = JSON.stringify(reqJSON);

      var replaceText = "";

      $.ajax({
        type : "POST",
        url : "/",
        data : 
        { json : strJSON },
        dataType : 'json',
        success : function(result){
          if(result.status == "Success"){ 
            window.location.href='./monitor';
          } else{
            alert(result.msg);
          }
        },
        error : function(error){
          if(error.status == 400){
            alert("비밀번호 확인바랍니다.");
          }else if(error.status == 401){
            alert("[관리자]계정 확인바랍니다.");
          }else if(error.status == 500){
            alert("서버에 문제가 있습니다.");
          }else{
            alert("Error : \n" + JSON.stringify(error.responseJSON.msg));
          }
        }
      });// end ajax
    }
  });

  $("#login_btn").on("click", function(){
    if($("#userEmail").val() == ""){
  		alert("이메일은 필수 입력사항입니다.");
  		return;
    }else if($("#userPwd").val() == ""){
  		alert("휴대폰 번호는 필수 입력사항입니다.");
  		return;
  	}
    var userEmail = $("#userEmail").val();
    var userPwd = $("#userPwd").val();

    var reqJSON = {
      "user_email" : userEmail,
      "user_pwd" : userPwd
    };

    var strJSON = JSON.stringify(reqJSON);

    var replaceText = "";

    $.ajax({
  		type : "POST",
  		url : "/",
  		data : 
  		{ json : strJSON },
  		dataType : 'json',
  		success : function(result){
      	if(result.status == "Success"){	
        	window.location.href='./monitor';
        } else{
        	alert(result.msg);
        }
    	},
    	error : function(error){
        if(error.status == 400){
          alert("비밀번호 확인바랍니다.");
        }else if(error.status == 401){
          alert("[관리자]계정 확인바랍니다.");
        }else if(error.status == 500){
          alert("서버에 문제가 있습니다.");
        }else{
          alert("Error : \n" + JSON.stringify(error.responseJSON.msg));
        }
      }
    });// end ajax
  });// end on
});// end jQuery