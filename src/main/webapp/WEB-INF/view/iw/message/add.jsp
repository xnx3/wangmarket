<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../common/head.jsp">
	<jsp:param name="title" value="发送站内信息"/>
</jsp:include>

<div style="padding:30px; text-align:center; font-size:30px;">
	发送站内信息
</div>

<form method="post" action="send.do">
	对方id（接收消息方的user.id）：<input type="text" name="recipientid" />
	<br/>
	信息内容：<textarea name="content"></textarea>
	<br/>
	<input type="submit" value="发送" />
</form>