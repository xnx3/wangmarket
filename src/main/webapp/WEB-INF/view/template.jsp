<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="iw/common/head.jsp">
	<jsp:param name="title" value="模版列表"/>
</jsp:include>
<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}css/layerGlobal.css" media="all">

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
	font-size: 14px;
}

#template_type>a{
	border:0px;
	margin-left:10px;
	font-size:16px;
}
#template_type>a:before{
	content: "|";
    color: #946c6c;
    padding-right: 12px;
    font-weight: 400;
}
.terminal{
	font-size: 13px;
    color: gray;
}
.terminal span{
	padding:3px;
}
</style>
<body>
<div style="width: 100%; padding-left: 20px; padding-right: 20px; box-sizing: border-box; padding:30px;" id="template_type">
	加载中...
</div>
<div class="list" id="cloudList">
	
</div>

<script type="text/javascript">
layui.use('element', function(){
	var element = layui.element;
});

var typeArray = '广告设计|学校培训|五金制造|门窗卫浴|IT互联网|化工环保|建筑能源|智能科技|房产物业|金融理财|工商法律|人力产权|生活家政|服装饰品|医疗保健|装修装饰|摄影婚庆|家具数码|茶酒果蔬|组织政府|餐饮酒店|旅游服务|汽车汽配|畜牧种植|体育健身|儿童玩具|个人博客|文档资料'.split("|");
//初始化模版列表的type
function initType(){
	var html = '<a href="javascript:typeClick(-1);" class="templateType" id="type_-1">全部模版</a>';
	for(var i = 0; i < typeArray.length; i++){
		html = html+'<a href="javascript:typeClick('+i+');" class="templateType" id="type_'+i+'">'+typeArray[i]+'</a>';
	}
	document.getElementById('template_type').innerHTML = html;
}
//type点击触发
function typeClick(type){
	//将所有type还原会原本样子
	$(".templateType").css("font-size","16px");
	$(".templateType").css("color","black");
	
	//将选中的这个设置颜色
	document.getElementById('type_'+type).style.fontSize='19px';
	document.getElementById('type_'+type).style.color='red';

	document.getElementById("cloudList").innerHTML = '<div style="font-size: 30px;padding-top: 10%;color: lightgrey; text-align:center;width: 100%;box-sizing: border-box;">加载中...</div>';
	//若使用云端模版库，则可以将下面请求地址换为 http://wang.market/cloudTemplateList.do
	//使用当前配置文件的模版，则为：/cloudTemplateList.do
	$.getJSON('/template/getTemplateList.do?type='+type,function(obj){
		var html = '';	//云端模版的列表
			if(obj.result == '1'){
				var divArray = new Array();	//共分四列，也就是下标为0～3
				for(var i=0; i<obj.list.length; i++){
					var xiabiao = i%4;	//取余，得数组下表
					var to = obj.list[i];
					var temp = '<div>'+
								'<img src="'+((to.previewPic != null && to.previewPic.length > 8)? to.previewPic:'${AttachmentFileUrl}websiteTemplate/'+to.name+'/preview.jpg') +'" class="previewImg" onclick="previewUrl(\''+to.previewUrl+'\');" />'+
								((to.previewUrl != null && to.previewUrl.length > 8)? '<div class="previewButton"><a href="javascript:window.open(\''+to.previewUrl+'\');" target="_black">点此预览</a></div>':'')+
								'<div class="templateName" onclick="useCloudTemplate(\''+to.name+'\');">模版编码：'+to.name+'</div>'+
								'<div class="terminal">访问支持：'+
									(to.terminalPc? '<span>电脑端</span>':'')+
									(to.terminalMobile? '<span>手机端</span>':'')+
									(to.terminalIpad? '<span>平板</span>':'')+
									(to.terminalDisplay? '<span>展示机</span>':'')+
								'</div>'+
								'<div class="info">'+to.remark+'</div>'+
								'<div class="info">提供方：'+to.username+'&nbsp;&nbsp;('+(to.companyname.length > 0 ? (to.companyname == '潍坊雷鸣云网络科技有限公司'? '官方':to.companyname):'')+')</div>'+
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
initType();
typeClick(-1);	//默认加载中所有模版

//预览网站示例。 url 要预览的网站
function previewUrl(url){
	if(url != null && url.length > 8){
		window.open(url);
	}else{
		iw.msgFailure('抱歉，该模版暂无预览体验的网站示例');
	}
}

</script>

</body>
</html>