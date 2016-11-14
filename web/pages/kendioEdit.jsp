<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<link href="../js/themes/default/default.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/util.js"></script>
<script type="text/javascript" src="../js/kindeditor-all.js"></script>
<script type="text/javascript" src="../js/lang/zh_CN.js"></script>
</head>
<script type="text/javascript">
  	var editor,flag=getUrlParam("flag"),id=getUrlParam("id"),url="../announcement/announcementList.do?command=load";
  		$(document).ready(function(){
  			
			setKindEdit(setdata);
  		});
  		
  		var setKindEdit=function(callback){
  			
	  		KindEditor.ready(function(K) {
					editor = K.create('textarea[name="content"]', {
						resizeType : 1,
						allowPreviewEmoticons : false,
						allowImageUpload : false,
						items : [
							'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
							'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
							'insertunorderedlist', '|', 'emoticons', 'image', 'link']
					});
				});
			
				if($.trim(flag) =="update"){
					callback();
				}
  		};
  		
  		var setdata=function(){
  		$.post(url+"&id="+id,null,function(data){
  		         var obj=$.parseJSON(data);
				 editor.insertHtml(obj.context);	
		});
  		};
  		
		function getValue(callback){
		 var gethtml= editor.html();
			callback(gethtml);
		}
	
</script>
<body>
<form id="kenditedit">
	<textarea name="content" style="width:100%;height:400px;"></textarea>
</form>
</body>
</html>