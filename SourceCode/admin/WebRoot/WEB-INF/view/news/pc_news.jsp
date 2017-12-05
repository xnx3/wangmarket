<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
${headHtml}
<body>
<!-- 网站顶部标题栏 -->
${topHtml}
<script type="text/javascript">
//form提交验证
function validate(){
	if(document.getElementById('input_title').value.length == 0 || document.getElementById('input_title').value.length > 30){
		$.alert("请输入30个字以内的标题", "提示！");
		document.getElementById('input_title').focus();
		return false;
	}
	$.showLoading('正在保存中');
	return true;
}

try{
	resBasePath.length > 0;
}catch(err){
	var resBasePath = "http://res.weiunity.com/";
}

</script>

<form action="saveNews.do" class="weui_cells weui_cells_form" method="post" onsubmit="return validate();">
	<input type="hidden" name="id" value="${news.id }" />
	<input type="hidden" name="cid" value="${siteColumn.id }" />
	
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
      <div class="weui_cell" style="text-align: left;">
        <div class="weui_cell_bd weui_cell_primary">
          <textarea class="weui_textarea" id="ueditorText" name="text" placeholder="请输入新闻内容" rows="3">${text }</textarea>
        </div>
      </div>
    </div>
	<div class="weui_btn_area">
        <input class="weui_btn weui_btn_primary" type="submit" value="保存">
    </div>
</form>
				
<!-- 配置文件 -->
<script type="text/javascript" src="<%=basePath %>/module/ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="<%=basePath %>/module/ueditor/ueditor.all.js"></script>
<!-- 实例化编辑器 -->
<script type="text/javascript">
    var ue = UE.getEditor('ueditorText',{
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