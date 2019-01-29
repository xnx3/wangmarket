<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.shiro.ShiroFunc"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%
String path = request.getContextPath();
String port = (request.getServerPort() != 80) ? (":"+request.getServerPort()):"";
String basePath = "http://"+request.getServerName()+port+path+"/";
%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="自助创建网站"/>
</jsp:include>
<script src="<%=Global.get("ATTACHMENT_FILE_URL") %>js/fun.js"></script>

<style>
.iw_table tbody tr .iw_table_td_view_name{
	width:50%;
	padding-left:25%;
}
</style>




<div style=" font-size:16px; padding:35px;">
	
	<div>
		你可以将此链接分享到微博、QQ群、……各大平台。其他人点击此链接后，便可自行注册网站开通使用。期间，完全没你什么事，群发出去，坐等代理平台里呼呼得涨用户，增加网站。
		<br/>
		<style>
		.zhuyi{
			padding-left: 40px;
			padding-bottom: 30px;
		}
		.zhuyi li{
			list-style-type: decimal;
		}
		</style>
		<ul class="zhuyi">
			<li>通过此链接，自助创建的网站，可以免费使用一年。如果用户第二年想继续用，就会联系你交钱了。至于你要收多少，你自己说了算！</li>
			<li>通过此链接创建网站，不会扣除你代理平台的<%=Global.get("CURRENCY_NAME") %>，也就是说，通过此链接创建的网站，是完全免费的！</li>
		</ul>
		
	</div>
	
	<textarea style="width:100%; height:26px; padding:15px; font-size:20px;"><%=basePath %>regByPhone.do?inviteid=${user.id }</textarea>
</div>


<jsp:include page="../iw/common/foot.jsp"></jsp:include>  