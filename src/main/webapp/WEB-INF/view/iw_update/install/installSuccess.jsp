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

<div style="padding:30px; padding-bottom:10px; text-align:center; font-size:20px;">
	恭喜您，系统参数配置成功！您可以正常使用开展您的业务了！
	<a href="delete.do" target="_black" style="font-size:12px;">删除安装文件</a>
	<br/>
	
	
	
	<div style="padding-top:30px; text-align: left;">
		系统登录地址：<a href="../login.do" target="_black"><%=Global.get("MASTER_SITE_URL") %>login.do</a>
		<br/>
		<br/>
		<div>
			总管理后台登录的账号密码都是 admin
			<div style="font-size:14px; color:gray;">
				所有开通网站的用户、所有网站、所有网站的文章、整个系统的权限管理、即系统的设置，都是在总管理后台进行操作
				<br/><b>需要上去自行将密码改掉！</b>
			</div>
		</div>
		<br/>
		<div>
			代理后台(可开通网站跟下级代理)登录的账号密码都是 agency
			<div style="font-size:14px; color:gray;">
				开通网站，以及开通代理平台
			</div>
		</div>
		<br/>
		<div>
			提供快速体验的账号密码都是 wangzhan
			<div style="font-size:14px; color:gray;">
				体验一下网站管理后台。开通网站后，将网站的账号密码给客户，客户可以自己上传新闻、产品，自行维护
			</div>
		</div>		
	</div>
</div>

<jsp:include page="../../iw/common/foot.jsp"></jsp:include> 