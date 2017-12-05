<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.selfSite.G"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="文章内容"/>
</jsp:include>
<script type="text/javascript" src="http://res.weiunity.com/js/fun.js"></script>

<script type="text/javascript">
/**
 * 编辑页面的内容代码， 同文章列表页的一样，复制于此
 * 页面类型为独立页面
 * @param name TemplatePage.name要编辑的模版页面名字
 */
function editText(name){
	if(parent.currentMode == 2){
		//要将其切换回智能模式
		parent.window.htmledit_mode();
	}

	parent.document.getElementById("currentTemplatePageName").value = name;
	parent.loadIframe();
	
	try{
		parent.document.getElementById("currentTemplateType").innerHTML = '详情页模版';
		parent.document.getElementById("tongyong").style.display = '';
		parent.document.getElementById("lanmu").style.display = '';
		parent.document.getElementById("fenye").style.display = 'none';
		parent.document.getElementById("wenzhang").style.display = '';
		parent.document.getElementById("dongtailanmu").style.display = '';
		parent.document.getElementById("xiangqingduyou").style.display = '';
		parent.document.getElementById("liebiaoduyou").style.display = 'none';
	}catch(err){}
	
	//parent.document.getElementById('iframe').src='http://localhost:8080/selfSite/template/getTemplatePageText.do?pageName='+name;
	parent.layer.close(index);
}

//判断文章所属的栏目，所属类型，若是独立页面，同时还是模板式编辑，那么直接进入模板编辑
if('${siteColumn.type}' == 3 && '${siteColumn.editMode}' == 1){
	editText('${siteColumn.templatePageViewName}');
}
</script>

<!-- 配置文件 -->
<script type="text/javascript" src="<%=basePath %>/module/ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="<%=basePath %>/module/ueditor/ueditor.all.js"></script>

<form action="saveNews.do" enctype="multipart/form-data" class="weui_cells weui_cells_form" method="post" onsubmit="return validate();">
	<input type="hidden" name="id" value="${news.id }" />
	<input type="hidden" name="cid" value="${news.cid }" />
	<input type="hidden" name="type" value="${news.type }" />
	
	
	${inputModelText }
    
    
	<div class="weui_btn_area">
        <input class="weui_btn weui_btn_primary" type="submit" value="保存">
    </div>
</form>


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

//载入素材库
function loadSuCai(){
	//加载素材的支持库
	dynamicLoading.js(resBasePath+"ueditor/template/load.js");
}
</script>

</body>
</html>