<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="开通网站"/>
</jsp:include>

<jsp:include page="./addSite.jsp"></jsp:include>

<!-- 隐藏语种切换按钮 -->
<style> #translate{display:none;} </style>
<jsp:include page="/wm/common/foot.jsp"></jsp:include> 