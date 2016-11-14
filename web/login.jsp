<%@ page language="java"  
import="java.util.*,
com.sun.org.apache.xerces.internal.impl.dv.util.Base64,
data.framework.utility.EncryptHelper" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <link rel="shortcut icon" href="theme/default/icon/logintitle.ico" /> -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="theme/default/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/ui.common.js"></script>
<title>欢迎登录系统</title>
<script type="text/javascript" src="js/page.login.js"></script>
<script type="text/javascript">
  function enterHomePage(){
             if(event.keyCode==13){
               $("#login_form").trigger("submit");
              }
  };
</script>
</head>
<body>
<%
String password = null;
String loginAccount = null;
String autoLogin = null;
Cookie c[]=request.getCookies();
if(c!=null){
	for(int x=0;x<c.length;x++){
		if(c[x].getName().equals("zz_oa")){
			String cookieValue = c[x].getValue();
			String cookieValueAfterDecode = new String(Base64.decode(cookieValue),"utf-8");
			String cookieValues[] = cookieValueAfterDecode.split(":");
			loginAccount = cookieValues[0];
			password = cookieValues[1];
			autoLogin = cookieValues[2];
			request.setAttribute("loginAccount", loginAccount);
			request.setAttribute("password", password);
			request.setAttribute("autoLogin", autoLogin);
		}
	} 
}
%>

    <input type="hidden" name="loginAccount" id="loginAccount" value="${loginAccount}"/>
    <input type="hidden" name="password" id="password" value="${password}"/>
    <input type="hidden" name="autoLogin" id="autoLogin" value="${autoLogin}"/>
	<div class="bigbox" style="width: 950px;">
		<form action="#" id="login_form" method="post" autocomplete="off">

			<div class="titlebox">
				<!-- <span><a href="javascript:;">联系我们</a> | <a href="javascript:;">网站地图</a> | <a href="javascript:;">版权声明</a> | <a href="javascript:;">法律顾问</a></span> -->
				<a href="javascript:;"><img
					src="theme/default/images/login_logo.jpg" /></a>
			</div>
			<div class="picbox">
				<img src="theme/default/images/login_bj.jpg" />
			</div>
			<div class="loginbox">
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td colspan="2" class="ind"><img
							src="theme/default/images/lh_logo.jpg" /></td>
					</tr>
					<tr>
						<td class="l01">登录名：</td>
						<td class="r01">
							<input type="text" value="" class="textinp" placeholder="用户名" id="j_username" name="j_username"/>
						</td>
					</tr>
					<tr>
						<td class="l01">密&nbsp;&nbsp;码：</td>
						<td class="r01">
							<input onkeypress="enterHomePage();" type="password" value="" class="textinp" placeholder="口令" id="j_password" name="j_password"/>
						</td>
					</tr>
					<tr>
						<td class="l01 tcen">&nbsp;</td>
						<td class="r01">
							<input type="checkbox" name="j_remeberMe" id="j_remeberMe" /> 记住密码&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="checkbox" name="j_autoLogin" id="j_autoLogin" /> 自动登录
						</td>
					</tr>
					<tr>
						<td class="l01 tcen">&nbsp;</td>
						<td class="r01">
							<a style="cursor: pointer;" onclick='$("#login_form").trigger("submit");'>
								<img src="theme/default/images/login_tb.jpg" />
								<button style="display: none;" type="submit"></button>
							</a>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</div>
</body>
</html>