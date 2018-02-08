<%@page import="com.xnx3.admin.G"%>
<%@page import="com.xnx3.j2ee.shiro.ShiroFunc"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="api"/>
</jsp:include>


<style>
.iw_table tbody tr .iw_table_td_view_name{
	width:100px;
	padding-left:5%;
}
</style>

<div style="text-align:center; font-size:29px; padding-top:35px; padding-bottom: 10px;">
	Api 接口
</div>
<div style="padding:40px;">
key:
<textarea style="width:100%; height:50px; padding:10px;">${key }</textarea>
</div>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>  