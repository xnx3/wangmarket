<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="选择模版"/>
</jsp:include>
<link rel="stylesheet" href="http://res.weiunity.com/css/layerGlobal.css" media="all">

<!-- author:管雷鸣 -->
<style>
.list{
	padding-left: 50px;
}
.list>div{
	float:left;
	width:24%;
	padding:0.5%;
}
.list>div>div{
	padding-bottom: 35px;
}

/*预览图*/
.previewImg{
	width:100%;
	cursor: pointer;
}
/*预览按钮*/
.previewButton{
	margin-top: -30px;
	text-align: right;
	overflow-x: hidden;
}
.previewButton a{
    background-color: #FF5722;
    border-radius: 15px;
    padding: 3px;
    padding-left: 12px;
    padding-right: 22px;
    cursor: pointer;
    color: #eeeeee;
    font-size: 18px;
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
<div style="padding: 12px;font-weight: 700;padding-left: 32px;color: red; font-size: 20px;">
	注意，方案二、三，当您使用了一个模版后，将无法再使用其他模版。
</div>

<div class="site-title">
	<fieldset><legend><a name="fieldset">方案一.不使用模版，自己从头开始做一套模版</a></legend></fieldset>
</div>
<div style="padding-left: 50px;">
	<span style="padding-left:10px; color:#c2c2c2;">注意，此种方式需要您简单掌握HTML的相关知识才可！入门非常简单，<a href="http://res.weiunity.com/html/templateTag/index.html" target="_black" style="color: blue;text-decoration: underline;">点此进入耐心看一看</a>，一小时内学会自己做模版，成大神！</span>
</div>


<div style="height:20px; width:100%;">&nbsp;</div>
<div class="site-title">
	<fieldset><legend><a name="fieldset">方案二.使用自己本地模版</a></legend></fieldset>
</div>
<div style="padding-left: 50px;">
	<button type="button" class="layui-btn layui-btn-primary" id="loadLocalTemplateFile">
		<i class="layui-icon">&#xe67c;</i>上传本地模版
	</button>
	<span style="padding-left:10px; color:#c2c2c2;">如果您有自己的模版，可以在此上传您自己的模版文件(导出模版功能导出的文件)，将其应用于当前的网站。</span>
</div>

<div style="height:20px; width:100%;">&nbsp;</div>
<div class="site-title">
	<fieldset><legend><a name="fieldset">方案三.使用云端官方模版</a></legend></fieldset>
</div>

<div class="list" id="cloudList">
	加载中...

</div>

<script type="text/javascript">
layui.use('element', function(){
	var element = layui.element;
});
layui.use('upload', function(){
	layui.upload.render({
	  url: '<%=basePath %>template/uploadImportTemplate.do'
	  ,method :'post'
	  ,elem : '#loadLocalTemplateFile'
	  ,exts: 'wscso|xnx3'
	  ,field: 'templateFile'
	  ,title :'加载本地模版'
	  ,size: '1000'
	  ,before: function(res){
		$.showLoading('上传中');
	  }
	  ,done: function(res, index, upload){
	    //上传成功返回值，必须为json格式
	    if(res.result == '1'){
	    	$.toast("模版加载成功", function() {
				parent.window.location.reload();	//刷新当前页
			});
	    }else{
	    	alert(res.info);
	    }
	  }
	}); 
});

//使用某个云端模版，传入其模版名、编号 templateName
function useCloudTemplate(templateName){
	var dtp_confirm = layer.confirm('确定要使用编号为“'+templateName+'”的模版？', {
	  btn: ['立即使用','取消'] //按钮
	}, function(){
		layer.close(dtp_confirm);
		
		iw.loading("加载中");
		$.post('<%=basePath %>template/remoteImportTemplate.do?templateName='+templateName, function(data){
			iw.loadClose();
			if(data.result == '1'){
				parent.iw.msgSuccess("加载成功");
				parent.window.location.reload();	//刷新当前页
		 	}else if(data.result == '0'){
		 		parent.iw.msgFailure(data.info);
		 	}else{
		 		parent.iw.msgFailure();
		 	}
		});
		
	}, function(){
	});
}

//加载云端的模版列表
function loadCloudTemplateList(){
	//若使用云端模版库，则可以将下面请求地址换为 http://wang.market/cloudTemplateList.do
	//使用当前配置文件的模版，则为：<%=basePath %>cloudTemplateList.do
	$.getJSON('http://res.weiunity.com/cloudControl/cmsTemplate.json',function(obj){
		var html = '';	//云端模版的列表
			if(obj.result == '1'){
				var divArray = new Array();	//共分四列，也就是下标为0～3
				for(var i=0; i<obj.list.length; i++){
					var xiabiao = i%4;	//取余，得数组下表
					var to = obj.list[i];
					var temp = '<div>'+
								'<img src="http://res.weiunity.com/template/'+to.name+'/preview.jpg" class="previewImg" onclick="useCloudTemplate(\''+to.name+'\');" />'+
								'<div class="previewButton"><a href="http://'+to.name+'.wscso.com" target="_black">点此预览</a></div>'+
								'<div class="templateName" onclick="useCloudTemplate(\''+to.name+'\');">模版编号：'+to.name+'</div>'+
								'<div class="info">'+to.intro+'</div>'+
						 		'</div>';
					if(divArray[xiabiao] == null){
						divArray[xiabiao] = '';
					}	 		
					divArray[xiabiao] = divArray[xiabiao] + temp;
				}
				
				//将四个div分别遍历，组合成显示的html
				for(var i=0; i<divArray.length; i++){
					html = html + '<div>' + divArray[i] +'</div>';
				}
				
				document.getElementById("cloudList").innerHTML = html;
	     	}else if(obj.result == '0'){
	     		 $.toast(obj.info, "cancel", function(toast) {});
	     	}else{
	     		alert(obj.result);
	     	}
		});
}
loadCloudTemplateList();
</script>

</body>
</html>