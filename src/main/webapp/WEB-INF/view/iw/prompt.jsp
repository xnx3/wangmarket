<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
String redirectUrl = request.getAttribute("redirectUrl").toString();
if(redirectUrl.indexOf("http")>-1){
}else if(redirectUrl.indexOf("avascript")>-1){
}else{
	redirectUrl = "/"+redirectUrl;
}

int state = (int)request.getAttribute("state");

Object infoObj = request.getAttribute("info");
String info = "";
if(infoObj != null){
	info = infoObj.toString();
}

String tishiIco = "&#xe605;"; //默认对号
if(state == Global.PROMPT_STATE_SUCCESS){
	tishiIco = "&#xe605;";
	if(info.equals("")){
		info = "执行成功";
	}
}else{
	tishiIco = "&#x1006;";
	if(info.equals("")){
		info = "执行失败";
	}
}

%>
<!DOCTYPE html>
<html>
<head>
  	<meta charset="utf-8">
    <title>提示</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
	<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}module/layui/css/layui.css">
	<style>
		.myForm{
			margin: 0 auto;
		    width: 495px;
		    height: auto;
		    border-width: 1px 1px 1px 1px;
		    padding: 0px;
		    overflow: hidden;
		    -webkit-box-shadow: 0 0 10px #e2e2e2;
		    -moz-box-shadow: 0 0 10px #e2e2e2;
		    box-shadow: 0 0 3px #e2e2e2;
		    position: absolute;
		    left: 50%;
		    top: 50%;
		    margin-left: -248px;
		    margin-top: -181px;
		    text-align: center;
 		}
 		.tishitubiao{
			float:left;
			margin-left:70px;
			padding-bottom:20px;
			padding-right: 25px;
		}
		
		@media screen and (max-width:600px){
			.myForm{
				margin: 0 auto;
			    width: 100%;
			    height: 100%;
			    border-width: 0px;
			    padding: 0px;
			    border-radius: 0px;
			    overflow: auto;
			    -webkit-box-shadow: 0 0 0px #e2e2e2;
			    -moz-box-shadow: 0 0 0px #e2e2e2;
			    box-shadow: 0 0 0px #e2e2e2;
			    position: static;
			    left: 0px;
			    top: 0px;
			    margin-left: 0px;
			    margin-top: 0px;
			}
			.tishitubiao{
				width:100%;
				margin-left:0px;
				padding-right: 0px;
			}
		}
	</style>
</head>	
<body>

<div class="myForm">
  <div style="padding-bottom: 60px; padding-top: 60px;">
  		<div class="tishitubiao">
  			<i class="layui-icon" style="font-size: 120px; color: #5FB878;"><%=tishiIco %></i>  
  		</div>
  		<div style="padding: 30px 50px 40px 50px; font-size: 22px;color: black;opacity: 0.9; min-height: 70px;">
		  		<%=info %>
		  </div>
		  <div style="font-size: 20px;opacity: 0.6;">
		  		<span id="time">3</span>秒后自动&nbsp;<b><a href="<%=redirectUrl %>" style="text-decoration: underline;">跳转</a></b>&nbsp;...
		  </div>
  		
  </div>
</div>

<script type="text/javascript">
function run(){
    var s = document.getElementById("time");
    if(s.innerHTML == 1){
        window.location.href='<%=redirectUrl %>';
        return false;
    }
    s.innerHTML = s.innerHTML * 1 - 1;
}
window.setInterval("run();", 1000);
</script>
</body>
</html>