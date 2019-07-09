<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="插件管理"/>
</jsp:include>
<link href="/css/site_two_subMenu.css" rel="stylesheet">

<div style="width:100%;height:100%; background-color: #fff; overflow-x: hidden;">
		
	<div class="layui-nav layui-nav-tree layui-nav-side menu">
		<div style="height: 65px;text-align: left;line-height: 65px;font-size: 16px;font-weight: 700;color: black;padding-left: 18px;">
			插件管理
		</div>
		<ul class="">
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('allList.do');" >所有插件</a>
			</li>
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('myList.do');" >当前使用</a>
			</li>
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('keyongList.do');" >可安装插件</a>
			</li>
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('keyongList.do');" >可导出插件</a>
			</li>
			
			<!-- 
				<li class="layui-nav-item" id="super2">
					<a href="javascript:;" class="dltitle">新闻咨询</a>
					<dl class="layui-nav-child" style="background-color: #EAEDF1;">
				  	<dd>
				  		<a href="#"  style="background-color: #fff; color:#222;" >公司新闻</a>
				  		<script>document.getElementById("super2").className+=" layui-nav-itemed";</script>
				  	</dd>
				  	<dd>
				  		<a href="#" >行业动态</a>
				  	</dd>
				  	</dl>
			  	</li>
		  	 -->
		  	 
		</ul>
	</div>
	
	<div style="width: 100%;height:100%;position: absolute;left: 170px;word-wrap: break-word;border-right: 170px;box-sizing: border-box; padding-right: 10px; overflow-y: auto;overflow-x: hidden; border-right: 170px solid transparent;">
		<iframe src="myList.do" id="iframe" frameborder="0" style="width:100%; height:100%;"></iframe>
	</div>
</div>

<script>
layui.use('element', function(){
  var element = layui.element;
});

//右侧主区域，加载url
function loadUrl(url){
	document.getElementById("iframe").src=url;
}
</script>

</body>
</html>