function wrapWindowByMask(){
    var maskHeight = $(window).height();
    var maskWidth = $(document).width();

    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
    $('#wrap_mask').css({'width':maskWidth,'height':maskHeight});
    // 플로팅 효과
    $('#wrap_mask').fadeIn(500); //시간 딜레이 1000=1s
    $('#wrap_mask').fadeTo("slow",0.5); //띄우는 시간, 어둡기 투명도 조절

    //윈도우 같은 거 띄운다.
    $('#popup_window').show();
}

function changeHTML(target, code) {
    target.replaceWith(code);
}

jQuery(function($){
    $(document).ready(function(){

        //검은 막 띄우기
        $('.monitor_component').click(function(e){
            // 여기서 해야할 일, popup_window의 값을 업데이트
            e.preventDefault();

            var target = $(this).children('.component_name_volume').children('.component_name');

            var userId = parseInt($(this).children('.hidden_userId').text());
            var userName = target.children('.visible_userName').text();
            var userGender = target.children('.visible_userGender').text();
            var userRoom = parseInt($(this).children('.hidden_userRoom').text());
            var linkerId = parseInt($(this).children('.hidden_linkerId').text());
            var linkerBattery = parseInt($(this).children('.hidden_linkerBattery').text());

            if(userRoom == 0) userRoom = "미지정";

            var replaceText = "\
<div class=\"popup_window_body\">\
    <div class=\"window_body_container\">\
        <p class = \"hidden_userId\">" + userId + "</p>\
        <p class = \"hidden_linkerId\">" + linkerId + "</p>\
        <div class=\"each_box_json\">\
            <div class=\"attribute\">환자이름:</div>\
            <input type=\"text\" id=\"sick_name\" placeholder=\"" + userName + "\"/>\
        </div>\
        <div class=\"each_box_json\" style=\"margin-bottom: 20px;\">\
            <div class=\"attribute\">병실위치:</div>\
            <input type=\"text\" id=\"sick_room\" placeholder=\"" + userRoom + "\"/>\
        </div>\
        <div id=\"escape_box\"></div>\
        <div id=\"repeat_area\">";


            $('.monitor_component > .hidden_userId').each(function(index){
                if(parseInt($(this).text()) == userId){
                    if(index != 0) {
                        replaceText += "\
            <div class=\"horizon\"></div>";
                    }

                    var ivId = parseInt($(this).siblings('.hidden_ivId').text());
                    var ivName = $(this).siblings('.component_type').text();
                    var ivPercent = parseInt($(this).siblings('.hidden_ivPercent').text());
                    var ivNow = parseInt($(this).siblings('.hidden_ivNow').text());
                    var ivMax = parseInt($(this).siblings('.hidden_ivMax').text());
                    var recordGtt = parseInt($(this).siblings('.hidden_recordGtt').text());
                    var recordTime = parseInt($(this).siblings('.hidden_recordTime').text());

                    replaceText += "\
            <p class = \"hidden_ivId\">" + $(this).siblings('.hidden_ivId').text() + "</p>\
            <div class=\"each_box_json\">\
                <div class=\"attribute\">수액명칭:</div>\
                <div class = \"toggle_component\">\
                    <div class = \"toggle_main\">\
                        <div class = \"left_area\"><p class= \"target_ivId\">" + ivId + "</p><span>" + ivName + "</span></div>\
                        <div class = \"right_area\"><p>▼</p></div>\
                    </div>\
                    <div class=\"toggle_box\">\
                    </div>\
                </div>\
            </div>\
            <div class=\"each_box_graph\">\
                <div class=\"attribute\">남은수액:</div>\
                <div class=\"graph_border\">\
                    <div class=\"graph_gauge\">\
                        <div class=\"graph_text_gray\"><span>" + ivNow + "/" + ivMax + "(mL)</span></div>\
                        <div class=\"graph_gauge_bar\" style=\"width:" + ivPercent + "%;\">\
                           <div class=\"graph_text\"><span>" + ivNow + "/" + ivMax + "(mL)</span></div>\
                        </div>\
                    </div>\
                </div>\
            </div>\
            <div class=\"each_box_time\">\
                <div class=\"attribute\">남은시간:</div>\
                <div class=\"value\">" + (recordTime - (recordTime%60))/60 + "시간 " + recordTime%60 + "분</div>\
                <div class=\"boundary\">/</div>\
                <div class=\"attribute\">투여속도:</div>\
                <div class=\"value\">" + recordGtt + "gtt</div>\
            </div>";

                }

            });

            replaceText += "\
        </div>\
        <div class=\"each_box_json\">\
            <div class=\"attribute\">배터리:</div>\
            <div id=\"battery_text\" class=\"value\"><span>" + linkerBattery + "%</span></div>";

            if(linkerBattery <= 0){
                replaceText += "<img id=\"battery_big\" src=\"../images/battery-big_0.png\"/>";
            }else if(linkerBattery <= 10){
                replaceText += "<img id=\"battery_big\" src=\"../images/battery-big_1.png\"/>";
            }else if(linkerBattery <= 30){
                replaceText += "<img id=\"battery_big\" src=\"../images/battery-big_2.png\"/>";
            }else if(linkerBattery <= 50){
                replaceText += "<img id=\"battery_big\" src=\"../images/battery-big_3.png\"/>";
            }else if(linkerBattery <= 70){
                replaceText += "<img id=\"battery_big\" src=\"../images/battery-big_4.png\"/>";
            }else if(linkerBattery <= 100){
                replaceText += "<img id=\"battery_big\" src=\"../images/battery-big_5.png\"/>";        
            }

            replaceText += "\
        </div>\
        <div class=\"each_box_special\">\
            <div class=\"attribute\">특이사항</div>\
            <textarea id=\"remark\" placeholder=\"\"></textarea>\
        </div>\
        <div class = \"button_area\">\
            <input type=\"button\" class=\"white_btn\" href=\"#\" id=\"close\" value=\"창닫기\"/>\
            <input type=\"button\" class=\"blue_btn\" href=\"#\" id=\"save\" value=\"저장하기\"/>\
        </div>\
    </div>\
</div>";

        $(".popup_window_body").replaceWith(replaceText);
            wrapWindowByMask();
        });

        // //검은 막을 눌렀을 때
        // $('#wrap_mask').click(function () {
        //     $(this).hide();
        //     $('#popup_window').hide();
        // });
    });

    $(document).on("click", "#popup_window #close", function(){
        // e.preventDefault();
        $('#wrap_mask, #popup_window').hide();
    });

    $(document).on("click", ".toggle_component", function(){
        var changeTarget = $(this);

        var windowWidth = $('#popup_window').width();
        var windowHeight = $('#popup_window').height();

        $('#escape_box').css("display", "block");
        $('#escape_box').css({'width':windowWidth,'height':windowHeight}); 

        $.ajax({
            type : "POST",
            url : "./load",
            dataType : 'json',
            success : function(result){
                if(result.status == "Success"){             
                    var replaceText = "<div class=\"toggle_box_show\">";
                    
                    for(var i = 0; i < result.ivData.length; i++){
                        replaceText += "<div class=\"toggle_content\">\
                                            <p class=\"toggle_hidden_now\">" + result.ivData[i].iv_now + "</p>\
                                            <p class=\"toggle_hidden_max\">" + result.ivData[i].iv_max + "</p>\
                                            <p class=\"toggle_hidden_id\">" + result.ivData[i].iv_id + "</p>\
                                            <span>" + result.ivData[i].iv_name + "</span>\
                                        </div>";
                    }
                        
                    replaceText += "</div>";

                    changeTarget.children('.toggle_box').replaceWith(replaceText);

                } else {
                    alert("Server error");
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
        });//end ajax
    });

    $(document).on("click", ".toggle_content", function(e){
        e.stopPropagation();

        var targetId = parseInt($(this).children('.toggle_hidden_id').text());
        var targetNow = parseInt($(this).children('.toggle_hidden_now').text());
        var targetMax = parseInt($(this).children('.toggle_hidden_max').text());
        var targetName = $(this).children('span').text();

        var strPercent = targetNow + "/" + targetMax + "(mL)";

        var targetParent = $(this).parents('.toggle_box_show').siblings('.toggle_main').children('.left_area');
        targetParent.children('.target_ivId').text(targetId);
        targetParent.children('span').text(targetName);

        var targetGrandParent = $(this).parent('.toggle_box_show').parent('.toggle_component').parent('.each_box_json').siblings('.each_box_graph').children('.graph_border').children('.graph_gauge');
        
        // alert(targetGrandParent.children('.graph_text_gray').children('span').text());
        targetGrandParent.children('.graph_text_gray').children('span').text(strPercent);
        targetGrandParent.children('.graph_gauge_bar').css('width', (targetNow/targetMax*100)+"%");
        targetGrandParent.children('.graph_gauge_bar').children('.graph_text').children('span').text(strPercent);

        var targetTwins = $(this).parent('.toggle_box_show').parent('.toggle_component').parent('.each_box_json').siblings('.each_box_time').children('.value');
        targetTwins.each(function(index){
            $(this).text("미지정");
        });

        $("#escape_box").css("display", "none");
        $(".toggle_box_show").attr('class', 'toggle_box');
    });

    $(document).on("click", "#escape_box", function(e){
        $("#escape_box").css("display", "none");
        $(".toggle_box_show").attr('class', 'toggle_box');
    });

    $(window).resize(function() {
        //창크기 변화 감지
        var maskHeight = $(window).height();
        var maskWidth = $(document).width();

        //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
        $('#wrap_mask').css({'width':maskWidth,'height':maskHeight});
    });

    $(document).on("click", "#save", function(){
        var changeStat = 0;

        var nameBool = 0;
        var roomBool = 0;
        var ivTarget = -1;

        var userId = parseInt($('.window_body_container > .hidden_userId').text());
        var userName = $("#sick_name").val();
        var userRoom = $("#sick_room").val();

        var recordId = parseInt($('.window_body_container > .hidden_recordId').text());
        var catchIv = $("#repeat_area").children(".hidden_ivId").text();
        var catchNew = $(".left_area").children(".target_ivId").text();

        if(userName != "" && userName != $("#sick_name").attr('placeholder')){nameBool = 1;}
        if(userRoom != "" && userRoom != $("#sick_room").attr('placeholder')){roomBool = 1;}

        if(nameBool||roomBool){
            changeStat = 1;
            var reqJSON = {
                user_id : userId,
                user_name : null,
                user_room : null
            }

            if(nameBool){reqJSON.user_name = userName;}
            if(roomBool){reqJSON.user_room = userRoom;}

            var strJSON = JSON.stringify(reqJSON);

            $.ajax({
                type : "POST",
                url : "./update/user",
                data : 
                { json : strJSON },
                dataType : 'json',
                success : function(result){                
                    if(result.status == "Success"){
                        // alert("Successful Update");
                    } else {
                        alert("Server error");
                        location.reload();
                    }
                },
                error : function(error){
                    alert("Error : \n" + JSON.stringify(error.responseJSON.msg));
                }
            });
        }

        if(catchIv != catchNew){
            changeStat = 1;
            var reqJSON = {
                user_id : userId,
                iv_old : parseInt(catchIv),
                iv_new : parseInt(catchNew)
            }

            var strJSON = JSON.stringify(reqJSON);

            $.ajax({
                type : "POST",
                url : "./update/iv",
                data : 
                { json : strJSON },
                dataType : 'json',
                success : function(result){                
                    if(result.status == "Success"){
                        // alert("Successful Update");
                    } else {
                        alert("Server error");
                        location.reload();
                    }
                },
                error : function(error){
                    alert("Error : \n" + JSON.stringify(error.responseJSON.msg));
                }
            });
        }    
        setTimeout(function(){
            location.href = location.href;
        }, 100);

    });

});

