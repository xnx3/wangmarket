<%@page import="com.xnx3.j2ee.func.ActionLogCache"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Exception e = (Exception) request.getAttribute("exception");
e.printStackTrace();
//记录日志
ActionLogCache.insert(request, "500", e.getMessage());
%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>500_内部发生错误</title>
</head>
<body>
	<div style="text-align: center; padding-top:20%;">
		<span style="color: red; font-weight: bold; font-size:34px;">500</span>
		&nbsp;&nbsp;
		<span style="font-size:31px;">Sorry,内部发生错误</span>
		<div style="color:#e2e2e2; padding-top:12px;"><%=e.getMessage() %></div>
	</div>
</body>
</html>