<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="插件管理"/>
</jsp:include>


当前系统中已经安装的插件列表....正在开发中...待完善

<hr/>
<c:forEach items="${pluginList}" var="item">
	插件名字：${item.menuTitle }<br/>
	插件简介：${item.intro }<br/>
	插件id：${item.id }<br/>
	<hr/>	
</c:forEach>


<jsp:include page="../../iw/common/foot.jsp"></jsp:include>