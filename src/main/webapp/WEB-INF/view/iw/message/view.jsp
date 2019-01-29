<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../common/head.jsp">
	<jsp:param name="title" value="阅读站内信"/>
</jsp:include>

		<hr/>
		发信者：${messageVO.senderUser.nickname }
		<br/>
		收信者：${messageVO.recipientUser.nickname }
		<br/>
		信息发送时间：<x:time linuxTime="${messageVO.message.time }" format="yy-MM-dd HH:mm"></x:time>
		<br/>
		信息内容：${messageVO.content}
		<br/>

<jsp:include page="../common/foot.jsp"></jsp:include> 