<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../common/head.jsp">
	<jsp:param name="title" value="设置阿里云AccessKey参数"/>
</jsp:include>

<% if(Global.get("IW_AUTO_INSTALL_USE") == null){ %>
<div style="background-color: black; color:red; font-size:30px; padding:30px;">
	未导入数据库！请先连接数据库后再操作
</div>
<% } %>

<% if(!(Global.get("IW_AUTO_INSTALL_USE") != null && Global.get("IW_AUTO_INSTALL_USE").equals("true"))){ %>
<div style="background-color: black; color:red; font-size:30px; padding:30px;">
	注意！！系统检测到您已通过此完成安装，强烈建议将此安装文件删除掉！
	&nbsp; 
	<a href="delete.do" target="_black" style="color:blue; font-weight: bold; font-size:35px;">点此立即删除</a>
</div>
<% } %>
<div style="padding:30px; padding-bottom:10px; text-align:center; font-size:20px;">
	通过此安装，可以让你快速上手使用！
	
	<br/>
	<br/>
	<br/>
	<a href="accessKey.do" class="layui-btn layui-btn-primary">开始安装</a>
	<div style="padding-top:30px; font-size:14px; color:gray;">
		如果使用后感觉还不错，那么建议将 /src 下的配置文件中，其他功能也配置上。
	</div>
</div>


<jsp:include page="../common/foot.jsp"></jsp:include> 