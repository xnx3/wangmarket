<%@page import="com.xnx3.j2ee.Global"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="浏览模版插件"/>
</jsp:include>
<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}css/layerGlobal.css" media="all">

<!-- author:管雷鸣 -->
<style>
.list{
	padding-left: 50px;
}
.list>div{
	float:left;
	width:48%;
	padding:0.5%;
	min-width: 500px;
    max-width: 900px;
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
	padding: 10px;
	padding-bottom: 0px;
}

.leftImage{
	float:left;
	width: 50%;
}

/* 使用说明 */
.usedoc{
	padding-left: 10px;
    text-align: right;
    padding-right: 10px;
}
.usedoc a{
	color: blue;
}

</style>
<body>

<div class="site-title">
	<fieldset><legend><a name="fieldset">云端插件列表</a></legend></fieldset>
</div>

<div class="list" id="cloudList">
	加载中...

	
</div>

<script type="text/javascript">
layui.use('element', function(){
	var element = layui.element;
});

//使用某个云端模版，传入其模版名、编号 templateName
function useTemplatePlugin(pluginName){
	var dtp_confirm = layer.confirm('确定要导入模版插件：'+pluginName+'?', {
	  btn: ['立即导入','取消'] //按钮
	}, function(){
		layer.close(dtp_confirm);
		msg.loading("获取中");
		$.post('/template/restoreTemplatePluginByRemote.do?pluginName='+pluginName, function(data){
			msg.close();
			if(data.result == '1'){
				templateComparePreview();
		 	}else if(data.result == '0'){
		 		parent.msg.failure(data.info);
		 	}else{
		 		parent.msg.failure('获取失败');
		 	}
		});
		
	}, function(){
	});
}

//弹出模版比对窗口，选择要还原的项
//url:  相对路径，比如 template/restoreTemplateByRemote.do
function templateComparePreview(){
	layer.open({
		type: 2, 
		title:'导入模版插件', 
		area: ['800px', '560px'],
		shadeClose: true, //开启遮罩关闭
		content: '/template/restoreTemplateComparePreview.do'
	});
}

//加载云端的模版列表
function loadCloudTemplateList(){
	//若使用云端模版库，则可以将下面请求地址换为 http://wang.market/cloudTemplateList.do
	$.getJSON('//res.weiunity.com/cloudControl/templatePlugin.json',function(obj){
		var html = '';	//云端模版的列表
			if(obj.result == '1'){
				var divArray = new Array();	//共分2列，也就是下标为0～1
				for(var i=0; i<obj.list.length; i++){
					var xiabiao = i%2;	//取余，得数组下表
					var to = obj.list[i];
					var temp = '<div><div class="leftImage"><img src="//res.weiunity.com/template_plugin/'+to.name+'/preview.jpg" class="previewImg" onclick="useTemplatePlugin(\''+to.name+'\');"><div class="previewButton"><a href="'+to.demoUrl+'" target="_black">点此预览</a></div></div><div class="leftImage"><div class="templateName" onclick="useTemplatePlugin(\''+to.name+'\');">'+to.title+'</div><div class="info">'+to.info+'</div><div class="usedoc"><a href="'+to.docUrl+'?MASTER_SITE_URL=<%=Global.get("MASTER_SITE_URL") %>" target="_black">使用说明</a></div></div></div>';
					/* var temp = '<div>'+
								'<img src="//res.weiunity.com/template/'+to.name+'/preview.jpg" class="previewImg" onclick="useCloudTemplate(\''+to.name+'\');" />'+
								'<div class="previewButton"><a href="http://'+to.name+'.wscso.com" target="_black">点此预览</a></div>'+
								'<div class="templateName" onclick="useCloudTemplate(\''+to.name+'\');">模版编号：'+to.name+'</div>'+
								'<div class="info">'+to.intro+'</div>'+
					 	 		'</div>';
					*/ 		
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
	     		msg.failure(obj.info);
	     	}else{
	     		msg.failure(obj);
	     	}
		});
}
loadCloudTemplateList();
</script>

</body>
</html>