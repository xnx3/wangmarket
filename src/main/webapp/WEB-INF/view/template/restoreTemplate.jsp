<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="还原模版"/>
</jsp:include>
<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}css/layerGlobal.css" media="all">

<!-- author:管雷鸣 -->
<style>
.list{
	padding-left: 50px;
}


/*预览图*/
.previewImg{
	width:100%;
}
/*预览按钮*/
.previewButton{
	margin-top: -30px;
	text-align: right;
	overflow-x: hidden;
}
.previewButton a{
    background-color: #AAA752;
    border-radius: 15px;
    padding: 3px;
    padding-left: 12px;
    padding-right: 22px;
    cursor: pointer;
    color: #eeeeee;
    font-size: 14px;
    margin-right: -12px;
}

/*模版名字，编号*/
.templateName{
	width:100%;
	text-align:center;
	color:#2F4056;
	padding-top: 8px;
	cursor: pointer;
}
/*模版简介说明*/
.info{
	color:#c2c2c2;
}
</style>
<body>
<div style="padding: 12px; padding-left:25px; font-weight: 700;padding-left: 32px;color: red; font-size: 20px;">
	<b>还原模板</b>
</div>

<div style="height:300px;">
	<div style="width:47%; padding-right:3%; float:left;">
		<div class="site-title">
			<fieldset><legend><a name="fieldset">方案一.从云端恢复</a></legend></fieldset>
		</div>
		<div class="list" id="cloudList">
			<c:choose>
			    <c:when test="${usedYunTemplate}">
			        <div style="float:left; padding-right: 25px;">
			        	<img src="" class="previewImg" style="height:150px; width:auto;" id="previewPic"/>
						<div class="previewButton"><a href="#" id="previewUrl">预览模版</a></div>
						<div class="templateName" style="text-align: left;padding-left: 10px;">编号:${template.name }</div>
						<script>
							//图片
			        		var previewPic = '${template.previewPic}';
			        		if(previewPic.indexOf("websiteTemplate") == 0){
								//这种情况是使用Eclipse开发状态，且没有设置主域名的情况下
								previewPic = "/"+previewPic;
							}
							document.getElementById('previewPic').src = previewPic;
							
							//预览模版
							var previewUrl = '${template.previewUrl }';
							if(previewUrl == null || previewUrl.length < 5){
								previewUrl = 'javascript:alert("当前模版还没有可供预览的网站");';
							}else{
								document.getElementById('previewUrl').target = "_black";
							}
							document.getElementById('previewUrl').href = previewUrl;
			        	</script>
					</div>
					<div style="padding-top:10px;">
						<div>
							<button onclick="getCloudTemplate();" class="layui-btn layui-btn-primary" style="font-size: 16px;">云端初始模版还原</button>
						</div>
						<div style="padding-top:20px; color:#c2c2c2; ">
							您现在的模板是使用的云端模版，可以直接使用云端模版进行还原操作，还原回最初导入云端模版的样子
						</div>
						
					</div>
			        
			    </c:when>
			    <c:otherwise>
			        您当前使用的模板，非云端模板，无法使用云端恢复功能。
			    </c:otherwise>
			</c:choose>
		</div>
	</div>
	<div style="width:47%; padding-right:3%; float:right;">
		<div class="site-title">
			<fieldset><legend><a name="fieldset">方案二.从自己本地导入模板恢复</a></legend></fieldset>
		</div>
		<div style="padding-left: 50px;">
			<button id="localhostTemplateFileId" class="layui-btn layui-btn-primary" style="font-size: 16px;">导入本地模版</button>
			<span style="padding-top:20px; color:#c2c2c2; float: left;">
				如果您现在的模板，之前有过以下其中一项：
					<br/>&nbsp;&nbsp;1.&nbsp;&nbsp;备份(使用"导出/备份"功能导出过模板备份)
					<br/>&nbsp;&nbsp;2.&nbsp;&nbsp;使用本地模板创建的网站
				<br/>可以在此上传您电脑上的模版文件，将其应用(还原)到当前的网站。
			</span>
		</div>
	</div>
</div>

<div>&nbsp;</div>

<blockquote class="layui-elem-quote layui-quote-nm" style="margin:3px;">
	如果您的网站模板被您改错了，想还原回来，可以使用此功能，将您的网站的
	<ul style="padding-left:30px;">
		<li style="list-style-type: decimal;">某个模板</li>
		<li style="list-style-type: decimal;">某个模板变量</li>
		<li style="list-style-type: decimal;">某个输入模型</li>
		<li style="list-style-type: decimal;">某个栏目</li>
		<li style="list-style-type: decimal;">任意组合，甚至可以全部还原</li>
	</ul>
	还原成
	<ul style="padding-left:30px;">
		<li style="list-style-type: decimal;">使用云端模板还原，还原为最初的样子</li>
		<li style="list-style-type: decimal;">使用本地导入("导出/备份"导出的模板备份)进行还原，还原回你备份那一刻的样子</li>
	</ul>
</blockquote>

<script src="${STATIC_RESOURCE_PATH}module/layui/layui.all.js"></script>
<script type="text/javascript">
layui.use('element', function(){
	var element = layui.element;
});

layui.upload.render({
	elem: '#localhostTemplateFileId'
	,url: '/template/restoreTemplateByLocalhostFile.do'
	,field: 'templateFile'
	,method :'post'
	,exts: 'wscso|xnx3'
	,title :'加载本地模版'
	,before: function(input){
		//$.showLoading('');
		parent.msg.loading('载入中');
	}
	,done: function(res){
		parent.msg.close();
	    //上传成功返回值，必须为json格式
	    if(res.result == '1'){
	    	templateComparePreview();
	    }else{
	    	layer.msg(res.info, {shade: 2});
	    }
	}
});
 


//加载云端模版，触发选中云端模版还原
function getCloudTemplate(){
	parent.msg.loading("获取中");
	$.post('/template/restoreTemplateByRemote.do', function(data){
		parent.msg.close();
		if(data.result == '1'){
			templateComparePreview();
	 	}else if(data.result == '0'){
	 		parent.msg.failure(data.info);
	 	}else{
	 		parent.msg.failure('操作失败');
	 	}
	});
}

//弹出模版比对窗口，选择要还原的项
//url:  相对路径，比如 template/restoreTemplateByRemote.do
function templateComparePreview(){
	layer.open({
		type: 2, 
		title:'还原模版，进行比对', 
		area: ['800px', '560px'],
		shadeClose: true, //开启遮罩关闭
		content: '/template/restoreTemplateComparePreview.do'
	});
}
</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>