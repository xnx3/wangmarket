<%@page import="com.xnx3.j2ee.util.Page"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- 这是日志列表页面底部的分页显示 -->
<style>
	#xnx3_page{
		width:100%;
		padding-top:15px;
		padding-bottom: 20px;
	}
	#xnx3_page ul{
		width: 100%;
		text-align: center;
	}
	#xnx3_page ul li{
		display: inline-block;
	    vertical-align: middle;
	    border: 1px solid #e2e2e2;
	    background-color: #fff;
	    padding-right: 1px;
    	padding-left: 1px;
	}
	#xnx3_page ul .xnx3_page_currentPage, #xnx3_page ul .xnx3_page_currentPage a{
		background-color: #009688;
		color:#fff;
	}
	#xnx3_page ul li a{
	 	padding: 0 15px;
	 	height: 30px;
	    line-height: 30px;
	    background-color: #fff;
	    color: #333;
	 }
</style>
<div id="xnx3_page">
	<ul>
		<!-- 判断当前页面是否是列表页第一页，若不是第一页，那会显示首页、上一页的按钮 -->
		<c:if test="${!page.currentFirstPage}">
			<li><a href="${page.firstPage }">首页</a></li>
			<li><a href="${page.upPage }">上一页</a></li>
		</c:if>
		
		<!-- 输出上几页的连接按钮 -->
		<c:forEach items="${page.upList}" var="a">
			<li><a href="${a.href }">${a.title }</a></li>
		</c:forEach>
		
		<!-- 当前页面，当前第几页 -->
		<li class="xnx3_page_currentPage"><a href="#">${page.currentPageNumber }</a></li>
		
		<!-- 输出下几页的连接按钮 -->
		<c:forEach items="${page.nextList}" var="a">
			<li><a href="${a.href }">${a.title }</a></li>
		</c:forEach>
		
		<!-- 判断当前页面是否是列表页最后一页，若不是最后一页，那会显示下一页、尾页的按钮 -->
		<c:if test="${!page.currentLastPage}">
			<li><a href="${page.nextPage }">下一页</a></li>
			<li><a href="${page.lastPage }">尾页</a></li>
		</c:if>
		
		<li style="margin-right:30px;border:0px; padding-top:5px;">共${page.allRecordNumber }条，${page.currentPageNumber }/${page.lastPageNumber }页</li>
	</ul>
</div>