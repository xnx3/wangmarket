<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../common/head.jsp">
	<jsp:param name="title" value="关注列表"/>
</jsp:include>


<div><h1>添加关注</h1></div>

<form action="addSubmit.do" method="get">
	要关注的用户id：<input type="text" name="othersid"/>
	<input type="submit" value="关注" />
</form>

<jsp:include page="../common/foot.jsp"></jsp:include> 