var messageCue = function(Info){
	 var judge_bool = true;
		for(var i in Info){
			var $tag=$(i),content=Info[i],reg="";
			var format_str = "<span class='cue_info' style='color:red;'> 格式有误</span>" ;
			var isnull = $tag.val()==null||$tag.val()==""||$tag.val().replace(/\s/g, "")=="";
			for(var k in content){
				if(content[k]==false || content[k]==undefined || k=="title" ){
					continue;
				}
			switch (k){
				case "notNull":
					if(isnull){
						window.message({
					         text: "<b style='color:#0099cc;'>"+content.title+"</b>不为空",
					         title: "提醒",
					         buttons: {
					             "确认": function () {
					            	 window.top.$(this).dialog("close");
					             }
					         }
					     });
						return false;
					}
					break;
				case "email":
					reg= /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
					if(!isnull&&!reg.test($tag.val())){
						formatError("邮箱");
						return false;
					}
					break;
				case "phone":
					reg=/^(((0\d{2,3}-)?\d{7,8})|(1\d{10}))$/;
					if(!isnull&&!reg.test($tag.val())){
						$tag.after(format_str);
						return false;
					}
					break;
				case "SFZ":
					reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/; 
					if(!isnull&&!reg.test($tag.val())){
						formatError("身份证号码");
						return false;
					}
					break;
				case "postcode":
					reg = /^[1-9]\d{5}$/; 
					if(!isnull&&!reg.test($tag.val())){
						formatError("邮政编码");
						return false;
					}
					break;
				case "int":
					reg =/^([0-9]*)$/;
					if(!isnull && !reg.test($tag.val()) ){
						window.message({
					         text: "<b style='color:#0099cc;'>"+(content.title!=undefined?content.title:"")+"</b>请填写整数",
					         title: "提醒",
					         buttons: {
					             "确认": function () {
					            	 window.top.$(this).dialog("close");
					             }
					         }
					     });
						return false;
					}
					break;
				case "number":
					reg =/^[0-9]+([.]{1}[0-9]+){0,1}$/;
					if(!isnull && !reg.test($tag.val()) ){
						if(content.title==undefined){
							formatError("数字");
						}else{
							formatError(content.title);
						}
						return false;
					}
					break;
				case "dateOrder":
					var arrDate = i.split(",");
					if(!isnull && arrDate.length==2){
						reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
						var start = $(arrDate[0]).val(),end=$(arrDate[1]).val();
						if( start!=""&&end!=""&&(!reg.test(start) || !reg.test(end)) ){
							formatError("比较大小的日期");//两个日期格式有误
							return false;
						}else if( convert(start)>convert(end) ){//两个日期先后顺序有误
							if(content.title!=undefined){
								var t = content.title.split(",");
								 window.message({
								       text: "<b style='color:#0099cc;'>"+t[0]+"</b>不能大于<b style='color:#0099cc;'>"+t[1]+"</b>",
								       title: "提醒",
								       buttons: {
								           "确认": function () {
								          	 window.top.$(this).dialog("close");
								           }
								       }
								   });
								 return false;
							}else{
								 window.message({
								       text: "<b style='color:#0099cc;'>开始时间</b>不能大于<b style='color:#0099cc;'>结束时间</b>",
								       title: "提醒",
								       buttons: {
								           "确认": function () {
								          	 window.top.$(this).dialog("close");
								           }
								       }
								   });
								 return false;
							}
						}
					}
					break;
				case "date":
					reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
					
					if(!isnull && !reg.test($tag.val()) ){
						if(content.title==undefined){
							formatError("日期");
						}else{
							formatError(content.title);
						}
						return false;
					}
					break;
				case "maxLength":
					if(!isnull&&$tag.val().length>parseInt(content[k])){
						window.message({
					         text: "<b style='color:#0099cc;'>"+content.title+"</b>不超过<b style='color:#0099cc;'>"+content.maxLength+"</b>个字符",
					         title: "提醒",
					         buttons: {
					             "确认": function () {
					            	 window.top.$(this).dialog("close");
					             }
					         }
					     });
						return false;
					}
					break;
				default:return false;
			}
		}
	}
		return true;
}


var formatError = function(title){
	 window.message({
       text: "<b style='color:#0099cc;'>"+title+"</b>格式有误",
       title: "提醒",
       buttons: {
           "确认": function () {
          	 window.top.$(this).dialog("close");
           }
       }
   });
}

var convert=function(curDate){
    var dateAry=curDate.split("-");
       var result="";
        for(var i=0;i<dateAry.length;i++ ){
         result+=dateAry[i];
        }
      return parseInt(result);
  };

