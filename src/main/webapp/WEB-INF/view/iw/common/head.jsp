<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
//标题
String title = request.getParameter("title");
if(title == null ){
	title = Global.get("SITE_NAME");
}else{
	title=title+"_"+Global.get("SITE_NAME");
}

//关键字
String keywords = request.getParameter("keywords");
if(keywords == null ){
	keywords = Global.get("SITE_KEYWORDS");
}

//描述
String description = request.getParameter("description");
if(description == null ){
	description = Global.get("SITE_DESCRIPTION");
}
%><!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title><%=title %></title>
<meta name="keywords" content="<%=keywords %>" />
<meta name="description" content="<%=description %>" />

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="author" content="管雷鸣">

<!-- layer 、 layui -->
<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}module/layui/css/layui.css">
<script src="${STATIC_RESOURCE_PATH}module/layui/layui.js"></script>
<script>
//加载 layer 模块
layui.use('layer', function(){
  layer = layui.layer;
});
</script>

<script src="${STATIC_RESOURCE_PATH}js/jquery-2.1.4.js"></script>

<!-- weui，一个UI框架，这个包含weui，依赖Jquery -->
<script src="${STATIC_RESOURCE_PATH}js/jquery-weui.js"></script>
<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}css/jquery-weui.css">
<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}css/weui.min.css">

<!-- order by 列表的排序 -->
<script src="${STATIC_RESOURCE_PATH}js/iw.js"></script>

<style>
/*列表页头部form搜索框*/
.toubu_xnx3_search_form{
	padding-top:10px;
	padding-bottom: 10px;
}
/*列表页头部搜索，form里面的搜索按钮*/
.iw_list_search_submit{
	margin-left:22px;
}
/* 列表页，数据列表的table */
.iw_table{
	margin:0px;
}
/* 详情页，table列表详情展示，这里是描述，名字的td */
.iw_table_td_view_name{
	width:150px;
}
</style>
</head>
<body>