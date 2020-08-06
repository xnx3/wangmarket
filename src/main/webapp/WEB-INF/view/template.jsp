<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="iw/common/head.jsp">
	<jsp:param name="title" value="模版列表"/>
</jsp:include>

<style>
/*列表页头部form搜索框*/
.toubu_xnx3_search_form{
	padding-top:10px;
	padding-bottom: 10px;
}
/*列表页头部搜索，form里面的搜索按钮*/
.iw_list_search_submit{
	margin-left:22px;
}
/* 列表页，数据列表的table */
.iw_table{
	margin:0px;
}
/* 详情页，table列表详情展示，这里是描述，名字的td */
.iw_table_td_view_name{
	width:150px;
}
</style>
</head>
<body>

<style>
*{box-sizing: border-box;}
body{margin: 0;background-color: rgba(240,240,240,1.00);}
ul,li{padding: 0;margin: 0;list-style: none;display: block;}
a{display: block;color: #333;text-decoration: none;}
a:hover{transition: all .3s ease-in-out;-webkit-transition: all .3s ease-in-out;}
.clear_left{clear: left;}
.zong{width: 1340px;margin: 0 auto;margin-top: 120px;}
.yinying{-moz-box-shadow: 2px 4px 8px 1px rgba(4, 0, 0, 0.16);-webkit-box-shadow: 2px 4px 8px 1px rgba(4, 0, 0, 0.16);box-shadow: 2px 4px 8px 1px rgba(4, 0, 0, 0.16);}
.nav{width: 140px;float: left;background-color: #fff;margin-bottom: 300px;-moz-box-shadow: 2px 4px 8px 1px rgba(4, 0, 0, 0.16);-webkit-box-shadow: 2px 4px 8px 1px rgba(4, 0, 0, 0.16);box-shadow: 2px 4px 8px 1px rgba(4, 0, 0, 0.16);border-radius: 10px;}
.nav ul{width: 100%;padding: 14px 0;box-sizing: border-box;}
.nav ul li{height: 40px;transition: all .3s ease-in-out;-webkit-transition: all .3s ease-in-out;}
.nav ul li:last-child{display: none;}
.nav ul li:hover{background-color: rgba(240,240,240,1.00);}
.nav ul li a{text-align: center;width: calc(100% - 20px);font-size: 18px;line-height: 38px;border-bottom: solid 1px #F2F2F2;margin: 0 10px;}
.nav ul li a:hover{background-color: rgba(240,240,240,1.00);border-bottom: solid 1px #666;}
.guanbi{width: 40px;height: 40px;float: right;margin-right: 40px;padding-top: 18px;position: absolute;z-index: 10000;right: 0;display: none;}
.template{float: left;width: calc(100% - 140px);}
.template ul{width: 100%;padding-left: 40px;}
.template ul li{position: relative;}
.template ul li a{position: relative;}
.template .templateliimg{height: 320px;overflow: hidden;position: relative;}
.template .templateliimg img{display: block;width: 100%;position: absolute;top: 0;}
/*.template ul li a div:nth-child(1) img:hover{display: block;width: 100%;position: absolute;top: 0;animation: hehe 5s infinite alternate}*/
.biaot{display: block;}
.dangq{display: none;}
.gengduo{display: none;}
@keyframes hehe {
	0%{top: 0;bottom: auto;}
	50%{top: -100px;bottom: auto;}
	100%{top: auto;bottom: 0;}
}
.template_li{width: 280px;float: left;margin: 0 5px 20px 5px;background-color: #fff;padding: 20px;-moz-box-shadow: 2px 4px 8px 1px rgba(4, 0, 0, 0.16);-webkit-box-shadow: 2px 4px 8px 1px rgba(4, 0, 0, 0.16);box-shadow: 2px 4px 8px 1px rgba(4, 0, 0, 0.16);border-radius: 10px;position: relative;height: 520px;}
.template_li a{height: auto;line-height: 20px;}
.template_li h2{text-align: center;line-height: 30px;font-size: 15px;margin: 6px 0;font-weight: 400;color: #333}

 img{display: block;width: 100%;}
.tl1{font-size: 13px;color: #808080;}
.tl1 span{padding: 3px;color: #808080;}
.tl2{font-size: 14px;color: #c2c2c2;margin-top: 5px;}
.tl3{font-size: 14px;color: #c2c2c2;margin-top: 5px;}
.tl0{height: 150px;position: relative;}

.zong1{width: 100%;background-color: #fff;border-bottom: solid 1px #ccc;position: fixed;top: 0;left: auto;z-index: 1000;}
.head{height: 80px;width: 1340px;margin: 0 auto;}
.head ul{float: right;width: auto;height: 80px;padding-right: 100px;}
.head ul li{float: right;height: 80px;width: 120px;	text-align: center;}
.head ul li a{line-height: 80px;}
.logo{top: 0;display: inline-block;width: auto;height: 80px;padding: 10px 0;}
.logo a{width: 100%;height: 100%;position: relative; font-size:35px;}
.donghua{background-color: rgba(240,240,240,1.00)!important;}
@media screen and (max-width: 1340px){
	.head{width: auto;}
	.head ul{padding-right: 40px;}
	.zong{width: 100%;}
	.nav{width: 15%;margin-left: 5%;}
	.template{width: 80%;}
	.template ul{width: 940px;margin: 0 auto;}
	.template ul li{margin: 0 10px 20px 10px;}
	.nav ul{margin: 0 auto;}
	.nav ul li a{font-size: 16px;}
	.nav ul li:last-child{display: block;}
}
@media screen and (max-width: 1180px){
	.template{width: 80%;}
	.template ul{width: 620px;margin: 0 auto;}
	.template ul li{margin: 0 5px 20px 5px;}
}
@media screen and (max-width: 800px){
	.template{width: 100%;}
	.template ul{width: 100%;}
	.biaot{display: block;}
	.zong{margin-top: 70px;padding-top: 10px;}
	.nav{/*display: none;*/}
	.template{float: none;margin-top: 80px;}
	.head ul li{width: 70px;font-size: 15px;}
	.logo{width: 100px;background-size: 100%;margin-left: 10px;}
	.fenlei{float: right;}
	.dangq{float: left;}
	.biaot{background-color: #fff;height: 40px;line-height: 40px;padding: 0 40px;}
	.template ul{padding: 0;}
	.template ul li{width: 48%;margin: 0 1% 20px 1%;}
	.nav{width: 100%;border-radius: 0px;margin: 0 0 15px;}
	.nav ul li{float: left;width: 25%;}
	#div1{display: block;padding: 0;}
.nav{position: relative;}
#fenlei{position: absolute;;text-align: right;width: 100%;background-color: #fff;z-index: 1000}
#div1{position: relative;z-index: 100;background-color: #fff;width: 100%;transition: all .3s ease-in-out;-webkit-transition: all .3s ease-in-out;height: 0;overflow: hidden;}
.biaot{position: fixed;top: 80px;width: 100%;padding: 0px;}
.gengduo{float: right;padding-right: 40px;}
.dangq{padding-left: 40px;}
.active:hover #div1{transition: all .3s ease-in-out;-webkit-transition: all .3s ease-in-out;display: block;height: 320px;}
.active:hover .gengduo{display: block;}
.dangq{display: block;}
.gengduo{display: block;}
}
@media screen and (max-width: 610px){
	.template ul{width: 360px;margin: 0 auto;}
	.template ul li{width: 96%;margin: 0 2% 20px 2%;}
	.head ul{padding-right: 10px;}
	.dangq{padding-left: 10px;}
	.gengduo{padding-right: 10px;}
.zong .nav ul .donghua a{background-color: rgba(240,240,240,1.00);border-bottom: solid 1px #666;}
.fenlei:active .nav{display: block!important;}
}
@media screen and (max-width: 370px){
	.template ul{width: 100%;margin: 0 auto;}
	.template ul li{width: 96%;margin: 0 2% 20px 2%;}
	.head ul{padding-right: 10px;}
	.dangq{padding-left: 10px;}
	.gengduo{padding-right: 10px;}
.zong .nav ul .donghua a{background-color: rgba(240,240,240,1.00);border-bottom: solid 1px #666;}
.fenlei:active .nav{display: block!important;}
}
.guanbi span{width: 100%;border-bottom: 2px solid #000;display: block;}
.qiehuan{display: none}
.active{display: block;}



.preview1{position: absolute;right: 20px;top: 270px;}
.preview_img,.preview_url{display: block;line-height: 20px!important;height: 24px;color: #fff;background-color: #FF5722;padding: 0 4px 0 10px;border-radius: 12px 0 0 12px;font-size: 14px;}
.preview_img:hover,.preview_url:hover{color: #fbff00;}
.preview_img{margin-bottom: 8px;}

#goToTop{position: fixed;bottom: 100px;right: 40px;display: none;}
#goToTop a{background: url("data:img/png;base64,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") no-repeat;width: 50px;height: 50px;background-size: 60%;background-position: center;}
@media screen and (max-width: 1180px){
	#goToTop{position: fixed;bottom: 10px;right: 6px;display: none;}
}
</style>
<body>
<div style="width: 100%; box-sizing: border-box;" id="template_type">
	加载中...

<div class="list" id="cloudList">
	
</div>
</div>
<script type="text/javascript">
layui.use('element', function(){
	var element = layui.element;
});

var typeArray = '广告设计|学校培训|五金制造|门窗卫浴|IT互联网|化工环保|建筑能源|智能科技|房产物业|金融理财|工商法律|人力产权|生活家政|服装饰品|医疗保健|装修装饰|摄影婚庆|家具数码|茶酒果蔬|组织政府|餐饮酒店|旅游服务|汽车汽配|畜牧种植|体育健身|儿童玩具|个人博客|文档资料'.split("|");
//初始化模版列表的type
function initType(){
	var html = '<div class="zong1"><div id="goToTop"><a href="javascript:;"></a></div><div class="head"><div class="logo"><a href="/login.do" target="_black"><%=Global.get("SITE_NAME") %></a></div><ul><li><a href="/login.do" target="_blank">进入管理后台</a></li><div class="clear_left"></div></ul><div class="clear_left"></div></div></div>'+'<div class="zong" id="zong"><div class="guanbi" id="div2"><span style="transform: rotate(45deg);position: relative;left: 1px;top: 2px;"></span><span style="transform: rotate(-45deg);"></span></div><div class="nav" ><div id="fenlei" style="text-indent：28px;"><div class="biaot" id="div00"><div class="dangq" id = "typeNow">当前分类：</div><div class="gengduo" id="">更多分类</div><div class="clear_left"></div><ul class="" id="div1"><li class="li_biaoti donghua" id="div1_1li"><a href="javascript:typeClick(-1);" class="templateType" id="type_-1">全部模版</a></li>';
	for(var i = 0; i < typeArray.length; i++){
		html = html+'<li id="li_type_'+i+'" class="li_biaoti"><a href="javascript:typeClick('+i+');" class="templateType" id="type_'+i+'">'+typeArray[i]+'</a></li>';
	}
	document.getElementById('template_type').innerHTML = html + '<div class="clear_left"></div></ul></div></div></div><div class="template"><ul id="zong_muban"></ul>';//清除浮动。（未知，不清楚能不能用）
}
//type点击触发
function typeClick(type){
	//将所有type还原会原本样子
//	$(".templateType").css("font-size","16px");
//	$(".templateType").css("color","black");
	//$("#div1 li").removeClass("donghua");
	//$('#li_type_'+type).removeClass("donghua")

	
	//将选中的这个设置颜色
	//$('li_type_'+type).addClass("donghua");
	//document.getElementById('type_'+type).style.color='red';
//	/$('#li_type_'+type).addClass("donghua")
	var tempid = 'type_' + type;


	if(typeof($('#li_type_'+type).html()) == 'undefined'){
		$("#typeNow").html("当前分类：全部模板");
	}else{
		$("#typeNow").html("当前分类：" + $('#type_'+type).html());
	}
	
	

//
//
//javascript:window.open(\''+to.previewUrl+'\');          //预览线上示例网站链接
//((to.previewPic != null && to.previewPic.length > 8)? to.previewPic:'//cdn.weiunity.com/websiteTemplate/'+to.name+'/preview.jpg')      //图片链接

	document.getElementById("zong_muban").innerHTML = '<div style="font-size: 30px;padding-top: 10%;color: lightgrey; text-align:center;width: 100%;box-sizing: border-box;">加载中...</div>';
	//若使用云端模版库，则可以将下面请求地址换为 http://wang.market/cloudTemplateList.do
	//使用当前配置文件的模版，则为：/cloudTemplateList.do
	$.getJSON('/template/getTemplateList.do?type='+type,function(obj){
		var html = '';	//云端模版的列表
			if(obj.result == '1'){
				var divArray = new Array();	//共分四列，也就是下标为0～3
				for(var i=0; i<obj.list.length; i++){
					var xiabiao = i%4;	//取余，得数组下表
					var to = obj.list[i];
					try{
						//避免信息有异常导致所有模版都不显示
						var temp = '<li class="template_li"><a href="javascript:void(0);">'+
								'<div class="templateliimg"><img src="'+((to.previewPic != null && to.previewPic.length > 8)? to.previewPic+'?x-oss-process=image/resize,m_lfit,w_345':'//cdn.weiunity.com/websiteTemplate/'+to.name+'/preview.jpg?x-oss-process=image/resize,m_lfit,w_345') +'" class="previewImg" onclick="previewUrl(\''+to.previewUrl+'\');" /></div>'+((to.previewUrl != null && to.previewUrl.length > 8)? '':'')+
								'<h2 class="templateName" onclick="useCloudTemplate(\''+to.name+'\');">模版编码：'+to.name+'</h2><div class="preview1"><a class="preview_img" target="_blank" href="'+((to.previewPic != null && to.previewPic.length > 8)? to.previewPic:'//cdn.weiunity.com/websiteTemplate/'+to.name+'/preview.jpg') +'">点此预览大图</a><a class="preview_url" onclick="previewUrl(\''+to.previewUrl+'\');" href="javascript:void(0);">点此预览模版</a></div>'+
								'<div class="previewButton tl0"><div class="terminal tl1">访问支持：'+
									(to.terminalPc? '<span>电脑端</span>':'')+
									(to.terminalMobile? '<span>手机端</span>':'')+
									(to.terminalIpad? '<span>平板</span>':'')+
									(to.terminalDisplay? '<span>展示机</span>':'')+
								'</div>'+
								'<div class="info tl2">简介：'+to.remark+'</div>'+
								'<div class="info tl3">提供方：'+to.username+'&nbsp;&nbsp;('+(to.companyname.length > 0 ? (to.companyname == '潍坊雷鸣云网络科技有限公司'? '官方':to.companyname):'')+')</div></a>'+
						 		'</li>';
						if(divArray[xiabiao] == null){
							divArray[xiabiao] = '';
						}	 
						divArray[xiabiao] = divArray[xiabiao] + temp;
					}catch(e){
						console.log(e);
					}
				}
				
				//将四个div分别遍历，组合成显示的html   
				for(var i=0; i<divArray.length; i++){
					//html = html + '<ul class="template">' + divArray[i] +'<div class="clear_left"></div></ul><div class="clear_left"></div>';
					html = html + divArray[i];
				}
				
				document.getElementById("zong_muban").innerHTML = html;
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
		msg.failure('抱歉，该模版暂无预览体验的网站示例');
		
	}
}


//返回顶部
$(function(){
	$(window).scroll(function(){
		// console.log($(this).scrollTop());
		//当window的scrolltop距离大于1时，go to 
		if($(this).scrollTop() > 100){
			$('#goToTop').fadeIn();
		}else{
			$('#goToTop').fadeOut();
		}
	});
	$('#goToTop a').click(function(){
		$('html ,body').animate({scrollTop: 0}, 300);
		return false;
	});
});


$(".li_biaoti").click(function(){
		$(".li_biaoti").removeClass('donghua');
		$(this).addClass('donghua');
	}
)
$('#div00').click(function(){$('#div00').toggleClass('active');})
</script>

</body>
</html>