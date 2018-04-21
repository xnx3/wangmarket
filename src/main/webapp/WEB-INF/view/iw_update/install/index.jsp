<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="设置阿里云AccessKey参数"/>
</jsp:include>

<% if(Global.get("IW_AUTO_INSTALL_USE") == null){ %>
<div style="background-color: black; color:red; font-size:30px; padding:30px;">
	未导入数据库！请先连接数据库后再操作
</div>
<% } %>

<% if(!(Global.get("IW_AUTO_INSTALL_USE") != null && Global.get("IW_AUTO_INSTALL_USE").equals("true"))){ %>
<div style="background-color: black; color:red; font-size:30px; padding:30px;">
	注意！！系统检测到您已通过此完成安装。如果您需要重新更改，请登录总管理后台，找到左侧菜单中的“系统管理”下的“系统变量”，将变量名为“IW_AUTO_INSTALL_USE”的值改为“true”
	&nbsp; 
	<a href="delete.do" target="_black" style="color:blue; font-weight: bold; font-size:15px;">点此删除安装文件</a>
</div>
<% } %>
<div style="padding-top:25%; text-align:center; font-size:20px;">
	<a href="selectAttachment.do" class="layui-btn layui-btn-primary">开始安装</a>
</div>

<div style="font-size:14px; color:gray; position: absolute; bottom: 50px; text-align: center; width: 100%;">
	通过此安装，可以让你快速配置相关参数
	<br/>
	您也可以&nbsp;<a href="https://v.qq.com/x/page/c053533596l.html" target="_black"><u>点此查看安装视频</u></a>&nbsp;演示，以作参考
</div>

<jsp:include page="../../iw/common/foot.jsp"></jsp:include> 