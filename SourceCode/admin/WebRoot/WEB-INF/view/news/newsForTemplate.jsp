<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.admin.G"%>
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

<form id="form" method="post" class="layui-form" enctype="multipart/form-data" style="padding-top:35px; margin-bottom: 10px; padding-right:35px;">
	<input type="hidden" name="id" value="${news.id }" />
	<input type="hidden" name="cid" value="${news.cid }" />
	<input type="hidden" name="type" value="${news.type }" />
	
	${inputModelText }
    
	<div class="layui-form-item" style="padding-top:15px; text-align:center;">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit="" lay-filter="demo1" style="width:150px; font-size: 16px; height: 45px;line-height: 45px;">保存</button>
		</div>
	</div>
</form>


<script type="text/javascript">

layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form;
  //监听提交
	form.on('submit(demo1)', function(data){
		$.showLoading('保存中...');
		//创建FormData对象，获取到form表单的相关数据
		var formobj =  document.getElementById("form");
		var data = new FormData(formobj);
	
	    //为FormData对象添加上传图片的数据
	    $.each($('#titlePicFile')[0].files, function(i, file) {
	        data.append('titlePicFile', file);
	    });
		$.ajax({
			url:'<%=basePath %>news/saveNews.do',
	        type:'POST',
	        data:data,
	        cache: false,
	        contentType: false,    //不可缺
	        processData: false,    //不可缺
	        success:function(data){
	        	$.hideLoading();
	            if(data.result=='0'){
	            	alert(data.info);
	            }else{
	            	//上传成功
	            	parent.layer.msg('保存成功');
	            	window.location.href='listForTemplate.do?cid=${news.cid }';
	            }
	        },
	        error:function(){
	        	alert('上传出错！');
	        }
		});
    return false;
  });
  
});

//载入素材库
function loadSuCai(){
	//加载素材的支持库
	dynamicLoading.js(resBasePath+"ueditor/template/load.js");
}
</script>

</body>
</html>