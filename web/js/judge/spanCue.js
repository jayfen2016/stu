
var spanCue = function(Info){
	 var judge_bool = true;
		for(var i in Info){
			var $tag=$(i),content=Info[i],reg="";
			var format_str = "<span class='cue_info' style='color:red;'> 格式有误</span>" ;
			var isnull = $tag.val()==null||$tag.val()==""||$tag.val().replace(/\s/g, "")=="";
			for(var k in content){
				if(content[k]==false || content[k]==undefined || k=="message" ){
					continue;
				}
				switch (k){
				case "notNull":
					if(isnull){
						$tag.after("<span class='cue_info' style='color:red;'> 必填</span>");
						judge_bool=false;
					}
					break;
				case "email":
					reg= /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
					if(!isnull&&!reg.test($tag.val())){
						$tag.after(format_str);
						judge_bool=false;
					}
					break;
				case "phone":
					reg=/^(((0\d{2,3}-)?\d{7,8})|(1\d{10}))$/;
					if(!isnull&&!reg.test($tag.val())){
						$tag.after(format_str);
						judge_bool=false;
					}
					break;
				case "SFZ":
					reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/; 
					if(!isnull&&!reg.test($tag.val())){
						$tag.after(format_str);
						judge_bool=false;
					}
					break;
				case "postcode":
					reg = /^[1-9]\d{5}$/; 
					if(!isnull&&!reg.test($tag.val())){
						$tag.after(format_str);
						judge_bool=false;
					}
					break;
				case "int":
					reg =/^([0-9]*)$/;
					if(!isnull && !reg.test($tag.val()) ){
						$tag.after("<span class='cue_info' style='color:red;'>请填写整数</span>");
						judge_bool=false;
					}
					break;
				case "number":
					reg =/^[0-9]+([.]{1}[0-9]+){0,1}$/;
					if(!isnull && !reg.test($tag.val()) ){
						$tag.after(format_str);
						judge_bool=false;
					}
					break;
				case "date":
					reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
					if(!isnull && !reg.test($tag.val()) ){
						$tag.after(format_str);
						judge_bool=false;
					}
					break;
				case "maxLength":
					if(!isnull&&$tag.val().length>parseInt(content[k])){
						$tag.after("<span class='cue_info' style='color:red;'> 不超过"+content[k]+"字</span>");
						judge_bool=false;
					}
					break;
				default:break;
			}
		}
	}
	return judge_bool;
}


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
					         text: "<b style='color:#0099cc;'>"+content[k].title+"</b>不为空",
					         title: "提醒",
					         buttons: {
					             "确认": function () {
					            	 window.top.$(this).dialog("close");
					             }
					         }
					     });
						return judge_bool=false;
					}
					break;
				case "email":
					reg= /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
					if(!isnull&&!reg.test($tag.val())){
						formatError("邮箱");
						return judge_bool=false;
					}
					break;
				case "phone":
					reg=/^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/;
					if(!isnull&&!reg.test($tag.val())){
						$tag.after(format_str);
						return judge_bool=false;
					}
					break;
				case "SFZ":
					reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/; 
					if(!isnull&&!reg.test($tag.val())){
						formatError("身份证号码");
						return judge_bool=false;
					}
					break;
				case "postcode":
					reg = /^[1-9]\d{5}$/; 
					if(!isnull&&!reg.test($tag.val())){
						formatError("邮政编码");
						return judge_bool=false;
					}
					break;
				case "int":
					reg =/^([0-9]*)$/;
					if(!isnull && !reg.test($tag.val()) ){
						window.message({
					         text: "<b style='color:#0099cc;'>"+content.title+"</b>请填写整数",
					         title: "提醒",
					         buttons: {
					             "确认": function () {
					            	 window.top.$(this).dialog("close");
					             }
					         }
					     });
						return judge_bool=false;
					}
					break;
				case "number":
					reg =/^[0-9]+([.]{1}[0-9]+){0,1}$/;
					if(!isnull && !reg.test($tag.val()) ){
						$tag.after(format_str);
						return judge_bool=false;
					}
					break;
				case "date":
					reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
					if(!isnull && !reg.test($tag.val()) ){
						$tag.after(format_str);
						return judge_bool=false;
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
						return judge_bool=false;
					}
					break;
				default:return judge_bool=false;
			}
		}
	}
		return judge_bool;
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
