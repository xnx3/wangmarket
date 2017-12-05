<%@page import="com.xnx3.j2ee.func.ActionLogCache"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//记录日志
ActionLogCache.insert(request, "403");
%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>403_无权访问</title>
</head>
<body>
	<div style="text-align: center; padding-top:20%;">
		<span style="font-weight: bold; font-size:34px;">403</span>
		&nbsp;&nbsp;
		<span style="font-size:31px;">Sorry,您无权访问此页面</span>
	</div>
</body>
</html>