$("body").ready(function () {
	if ($("#autoLogin").val()=="autoLogin"){
    	var array = new Array() ;
    	array.push( "j_username=" ) ;
    	array.push( $("#loginAccount").val()) ;
    	array.push( "&j_password=" ) ;
    	array.push( $("#password").val()) ;
    	array.push( "&captcha=NONE" ) ;
    	array.push( "&ifCookie=true" ) ;
        $.ajax({
            url: "j_spring_security_check",
            type: "POST",
            data: array.join(""),
            dataType: "JSON",
            success: function (data, xhr) {
            	if( data.status ){
            		window.location.replace("homePage_.do");
            	}
                else
                {
                	alert(data.message);
                	var strCookie = document.cookie;
                	var arrCookie=strCookie.split(";"); 
                	for(var i=0;i<arrCookie.length;i++){ 
	                	var arr=arrCookie[i].split("="); 
	                	if("zz_oa"==arr[0]){ 
	                		document.cookie = "zz_oa=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/ZZ_OA/";
	                	}
                	}
                }
            }
        }); 
        
		return;
	}
	
    var $bgImg = $("#bg_img"),
           ih = 0,
           iw = 0;
    var resizeWin = function () {
        if (!$bgImg.data("isload")) { return; }

        var winW = $(document).width(),
            winH = $(document).height(),
              ws = (winW / winH),
              is = (iw / ih),
              nw = 0,
              nh = 0;

        var cssobj = {};
        if (ws > is) {
            nw = winW;
            nh = ih * (winW / iw);
            cssobj = { "margin-top": (winH - nh) / 2, "margin-left": 0 };
        } else {
            nh = winH;
            nw = iw * (winH / ih);
            cssobj = { "margin-top": 0, "margin-left": (winW - nw) / 2 };
        }
        $bgImg.height(nh).width(nw).css(cssobj);
    };
    $(window).resize(resizeWin);
    $("#j_username").focus();
    $bgImg.attr("onload", function () {
        setTimeout(function () {
            $bgImg.data("isload", true);
            ih = $bgImg[0].height,
            iw = $bgImg[0].width;
            resizeWin();
            $bgImg.css("visibility", "visible").animate({ "opacity": 1 }, 500);
        }, 200);
    });

    $("#j_username").val($("#loginAccount").val());
    $("#j_password").val($("#password").val());
    if (null!=$("#loginAccount").val() && $("#password").val()){
    	$("#j_remeberMe").attr("checked", true);
    }
    
    $("#login_form").on("submit", function () {
        var $vthis = $(this),
            $u = $vthis.find("#j_username"),
            $p = $vthis.find("#j_password"),
            un = $u.val(),
            pw = $p.val();
        if ($vthis.data("ismask")) { return false; }


        if ($.trim(un) === "") {
            $u.focus();
        } else {
            if (pw === "") {
                $p.focus();
            } else {
                $vthis.formMask(function () {
                	var submitArray = new Array() ;
                    submitArray.push( "j_username=" ) ;
                    submitArray.push( un ) ;
                    submitArray.push( "&j_password=" ) ;
                    submitArray.push( pw ) ;
                    submitArray.push( "&captcha=NONE" ) ;
                    if ($("#j_remeberMe").is(':checked')){
                        submitArray.push( "&j_save_username=" ) ;
                        submitArray.push( un ) ;
                        submitArray.push( "&j_save_password=" ) ;
                        submitArray.push( pw ) ;
                    }
                    
                    if ($("#j_autoLogin").is(':checked')){
                        submitArray.push( "&autoLogin=" ) ;
                        submitArray.push( "autoLogin") ;
                        submitArray.push( "&j_save_username=" ) ;
                        submitArray.push( un ) ;
                        submitArray.push( "&j_save_password=" ) ;
                        submitArray.push( pw ) ;
                    }
                    $.ajax({
                        url: "j_spring_security_check",
                        type: "POST",
                        data: submitArray.join(""),
                        dataType: "JSON",
                        success: function (data, xhr) {
                        	$vthis.unformMask(function () {
                        		if( data.status )
                                {
                            		window.location.replace("homePage_.do");
                                }
                                else
                                {
                                	alert(data.message);
                                }
                            });
                        }
                    });                    
                });
            }
        }
        return false;
    });
});

// //clear auto fill
// $(window).on("load", function () {
// setTimeout(function () {
// var $waary = $("#login_form input:-webkit-autofill");
// if ($waary.length === 0) { return; }
// var lfNV = [];
// $waary.each(function (index, item) {
// var $item = $(item),
// text = $item.val();
// lfNV.push({ "oldvalue": text, el: $item });
// });
// $("#login_form")[0].reset();
// for (var i in lfNV) {
// lfNV[i].el.val(lfNV[i].oldvalue);
// }
// }, 300);
// });
