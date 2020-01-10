<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
//标题
String title = request.getParameter("title");
%>
<jsp:include page="../head.jsp">
	<jsp:param name="title" value=""/>
</jsp:include>
<link href="/css/site_two_subMenu.css" rel="stylesheet">
<style>
/* 避免出现左右滚动条 */
body{
    width: 100%;
    overflow-x: hidden;
}
.adminSubMenu_title{
	height: 65px;
	text-align: left;
	line-height: 65px;
	font-size: 16px;
	font-weight: 700;
	color: black;
	padding-left: 18px;
	display:<%=(title != null && title.length() > 0)? "":"none" %>;
}
</style>

<div style="width:100%;height:100%; background-color: #fff; overflow-x: hidden;">
	
	<div class="layui-nav layui-nav-tree layui-nav-side menu">
		<div class="adminSubMenu_title">
			<%=title %>
		</div>
		<ul>
			