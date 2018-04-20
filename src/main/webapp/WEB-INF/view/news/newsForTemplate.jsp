<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
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

<script>
layui.use('upload', function(){
	var upload = layui.upload;
	//上传图片
	upload.render({
		elem: '#uploadImagesButton' //绑定元素
		,url: parent.masterSiteUrl+'site/uploadImage.do' //上传接口
		,field: 'image'
		,done: function(res){
			//上传完毕回调
			loadClose();
			if(res.result == 1){
				document.getElementById("titlePicInput").value = res.url;
				document.getElementById("titlePicA").href = res.url;
				document.getElementById("titlePicImg").src = res.url;
				document.getElementById("titlePicImg").style.display='';	//避免新增加的文章，其titlepicImg是隐藏的
				parent.iw.msgSuccess("上传成功");
			}else{
				parent.iw.msgFailure(res.info);
			}
		}
		,error: function(){
			//请求异常回调
			parent.iw.loadClose();
			parent.iw.msgFailure();
		}
		,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
			parent.iw.loading('上传中..');
		}
	});
});
</script>

<script type="text/javascript">
layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form;
  //监听提交
	form.on('submit(demo1)', function(data){
		parent.iw.loading("保存中");    //显示“操作中”的等待提示
		//创建FormData对象，获取到form表单的相关数据
		var formobj =  document.getElementById("form");
		var data = new FormData(formobj);
	
	    //为FormData对象添加上传图片的数据，这里由3.11版本更新后，图片单独上传，此项废弃
	    //$.each($('#titlePicFile')[0].files, function(i, file) {
	    //    data.append('titlePicFile', file);
	    //});
		$.ajax({
			url:'<%=basePath %>news/saveNews.do',
	        type:'POST',
	        data:data,
	        cache: false,
	        contentType: false,    //不可缺
	        processData: false,    //不可缺
	        success:function(data){
	        	parent.iw.loadClose();    //关闭“操作中”的等待提示
	        	if(data.result == '1'){
			        //上传成功
	            	parent.iw.msgSuccess('保存成功');
	            	window.location.href='listForTemplate.do?cid=${news.cid }';
			    }else if(data.result == '0'){
			        parent.iw.msgFailure(data.info);
			    }else{
			        parent.iw.msgFailure();
			    }
	        },
	        error:function(){
	        	parent.iw.loadClose();    //关闭“操作中”的等待提示
	        	parent.iw.msgFailure('出错');
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