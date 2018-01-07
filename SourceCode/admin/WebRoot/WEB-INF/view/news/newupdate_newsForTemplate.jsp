<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en"  style="margin: 0;padding: 0px;height: 100%;overflow: hidden;">
<head>
	<jsp:include page="../publicPage/popup/top.jsp">
	   	<jsp:param name="title" value="编辑内容"/>
	</jsp:include>
    
</head>
<body style="margin: 0;padding: 0px;height: 100%;overflow: hidden;">

<style>
.menu{
	width:160px;
	height:100%;
	background-color: #393D49;
	position: absolute;
}
</style>

<div style="width:100%;height:100%;">



	<div class="layui-nav layui-nav-tree layui-nav-side menu">
		<ul class="">
		  <li class="layui-nav-item layui-nav-itemed" style="margin-bottom:2px; cursor: default; font-size: 16px;">
		    <a href="javascript:;" style="Color:gray; font-weight: bold; cursor: default;">栏目列表</a>
		  </li>
		  ${columnTreeNav }
		  <li class="layui-nav-item layui-nav-itemed" style="margin-bottom:2px; cursor: default;">
		    <span style="height:45px;">&nbsp;</span>
		  </li>
		  <li class="layui-nav-item layui-nav-itemed" style="margin-bottom:2px;bottom: 0px; position: fixed;width: 160px;">
		    <a href="<%=basePath %>template/index.do" style="Color:gray;font-weight: bold;font-size: 17px;">
			<i class="layui-icon">&#xe603;</i>返回主界面</a>
		  </li>
		</ul>
	</div>
	
	
	<div style="width: 100%;height:100%;position: absolute;left: 170px;word-wrap: break-word;border-right: 170px;box-sizing: border-box; padding-right: 10px;">
		<form action="saveNews.do" enctype="multipart/form-data" class="weui_cells weui_cells_form" method="post" onsubmit="return validate();">
		<input type="hidden" name="id" value="${news.id }" />
		<input type="hidden" name="cid" value="${news.cid }" />
		<input type="hidden" name="type" value="${news.type }" />
		
		<div class="weui_cell">
			<div class="weui_cell_hd"><label class="weui_label">标题：</label></div>
			<div class="weui_cell_bd weui_cell_primary">
				<input class="weui_input" type="text" id="input_title" name="title" placeholder="(30个字之内，必填)" value="${news.title }">
			</div>
		</div>
		
	<c:choose>
	    <c:when test="${siteColumn.type == 2 }">
	    	<!-- 图文栏目才会显示标题图编辑功能 -->
	        <div class="weui_cell">
		        <div class="weui_cell_hd"><label class="weui_label">标题图片：</label></div>
		        <div class="weui_cell_bd weui_cell_primary">
		        	<input type="file" class="weui_input" value="" name="titlePicFile" />
		        </div>
		        <div class="weui_cell_ft">
		        	${titlepicImage }
		        	(如果是图文列表页面，图片将会在列表显示)
		        </div>
		     </div>
	    </c:when>
	    <c:otherwise>
	        <input type="hidden" class="weui_input" value="" name="titlePicFile" />
	    </c:otherwise>
	</c:choose>
		
		<div class="weui_cells weui_cells_form">
			<div class="edit-content fl" id="sucai" style="padding-top:10px; padding-left: 5px;">
			    <!-- 素材异步加载到此处区域 -->
			    <div style="font-size: 20px;text-align: center;width: 100%;cursor: pointer;background-color: aquamarine;" onclick="openSuCai();" id="useSuCai">使用素材模块，轻松美化页面。点此加载云端素材库</div>
			</div>
	
	      <div class="weui_cell">
	        <div class="weui_cell_bd weui_cell_primary" id="asd">
	         	<textarea class="weui_textarea" style="" id="myEditor" placeholder="请输入新闻内容" name="text" rows="3">${text }</textarea>
	        </div>
	        
	      </div>
	    </div>
		<div class="weui_btn_area">
	        <input class="weui_btn weui_btn_primary" type="submit" value="保存">
	    </div>
	</form>
</div>

<!-- 配置文件 -->
<script type="text/javascript" src="<%=basePath %>/module/ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="<%=basePath %>/module/ueditor/ueditor.all.js"></script>
<!-- 实例化编辑器 -->
<script type="text/javascript">
layui.use('element', function(){
  var element = layui.element();
});

var ueditorText = document.getElementById('myEditor').innerHTML;
var ue = UE.getEditor('myEditor',{
	autoHeightEnabled: true,
	autoFloatEnabled: true
});
//对编辑器的操作最好在编辑器ready之后再做
ue.ready(function() {
	
});

//打开素材区域
function openSuCai(){
	document.getElementById('useSuCai').innerHTML='素材库加载中...';
	//加载素材的支持库
	dynamicLoading.js(resBasePath+"ueditor/template/load.js");
}
</script>

</body>
</html>