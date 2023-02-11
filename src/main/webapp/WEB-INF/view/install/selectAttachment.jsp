<%@page import="com.xnx3.j2ee.util.ApplicationPropertiesUtil"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/wm/common/head.jsp">
	<jsp:param name="title" value="选择图片等附件的存储方式"/>
</jsp:include>

<%
Object selectStorage = ApplicationPropertiesUtil.getProperty("wangmarket.install.selectStorage");
if(selectStorage != null && selectStorage.toString().trim().equalsIgnoreCase("false")){
	//不使用这个页面，那么直接跳转到下个域名设置页面
	response.sendRedirect("/install/domainSet.do");
}
%>
	<style>
		
		.content{
			width: 600px;
			min-height:80%;
			margin: 0 auto;
			box-shadow: rgba(0, 0, 0, 0.06) 0px 1px 10px 2px;
			padding: 30px;
			margin-top: 50px;
		}
		.title{
			border-bottom: 1px solid #eee;
			padding-top: 20px;
			padding-left: 10px;
			padding-bottom: 20px;
			font-size: 28px;
			margin-bottom: 20px;
			text-align:center;
		}
		.content ul{
			padding-left: 20px;
		}
		.content ul li{
			list-style-type: decimal;
			padding-left:10px;
			padding-bottom:4px;
		}
		.content ul li img{
			max-width:250px;
			padding:4px;
			padding-left:40px;
		}
		.info{
			font-size:14px;
		}
		.info h2,h3,h4,h5{
			border-bottom: 1px solid #eee;
			padding-top: 15px;
			margin-bottom: 5px;
		}
		
		@media only screen and (max-width: 700px) {
			.content{
				width:auto;
				margin-top: 0px;
				box-shadow: rgba(0, 0, 0, 0.06) 0px 0px 0px 0px;
			}
			
		}
		
		a{
			color: blue;
		}
	</style>



	<div class="content">
	
		<div class="title">
			网站中所上传的图片等附件资源，如何存储？
		</div>
	
		<div class="select">
			<!-- 这里的 onmouseenter 是为了避免下面的收费鼠标移开后，不变回来，所以这里又加了一个，如果下面的收费的霉变过来，鼠标移动过来后，收费的恢复回来 -->
			<!-- setAttachmentMode.do?mode=${AttachmentFile_MODE_LOCAL_FILE} -->
			<div class="selectItem" onclick="mianfeiban();">
				<div class="title">默认-服务器本身存储</div>
				<div class="intro">
					使用服务器本身来存储网站中所上传的图片等附件资源。
					<br/>
					<b>如果你不是非常懂，想快速体验功能、或想快速完成安装使用，你选这个默认的就对了！</b>
				</div>
			</div>
			
			
			<div class="selectItem" id="weishouquanyun" onclick="selectObs();">
				<div class="title">扩展-华为云OBS存储</div>
				<div class="intro">
					使用华为云OBS对象存储来存储网站中所上传的图片等附件资源。搭配CDN，效果最优。
					<br/>
					降低服务器带宽成本、网站图片加载速度更快、更易扩展维护。但只限我们战略合作伙伴会此安装方式。
					<div style="font-size:11px; color:gray; text-align:right;padding-top:10px;">获取您当前城市战略合作伙伴联系方式 | 想成为战略合作伙伴？  -- 微信 xnx3com</div>
				</div>
			</div>
			
		</div>
		
	</div>
	
	
	<div style="width: 100%;text-align: center; padding-top: 50px;">
		<a href="http://www.leimingyun.com" target="_black" style="color: gray;font-size: 8px;">欢迎云厂商、各地IDC商家、以及提供存储服务等厂商开展各种合作，微信：xnx3com</a>
	</div>



<style>
.select{
	padding:20px;
}
.select>div{
	padding: 30px;
	border: 1px;
	border-style: solid;
	border-color: #d2d2d236;
}
.select div .title{
	font-size: 20px;
}
.select div .intro{
	font-size: 14px;
}


.lilist{
	padding-left: 40px;
}
.lilist>li{
	list-style: decimal;
}

.selectItem{
	cursor: pointer;
}
.selectItem:HOVER{
	border-color: #000000;
	background-color: #FAFAFA;
}
</style>

<script>

function mianfeiban(){
	if(window.location.hostname.indexOf('localhost') > -1 || window.location.hostname.indexOf('127.0.0.1') > -1){
		
		layer.open({
			type: 1
			,title: false
			,closeBtn: false
			,area: '590px;'
			,shade: 0.8
			,id: 'versionUpdateTipsss'
			,resize: false
			,btn: ['本地快速安装','完整安装']
			,btnAlign: 'c'
			,moveType: 1
			,content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300; text-align:left;">'
				+'<div style="width:100%; text-align:center; font-size:22px;"> <span style="font-size:28px; padding-right:15px;"></span>友情提示</div><br>'
				+'<style> .xnx3_gxc li{list-style-type: decimal;} </style>'
				+'<ul style="list-style-type: decimal;" class="xnx3_gxc">'
				+'<div>监测到您访问域名是本地的，如果是本地体验功能、或者做模版，您可以点下面的 本地快速安装 。 </div>'
				+'</ul></div>'
				
			,yes: function(index, layero){
				window.location.href="setLocalDomain.do?host="+window.location.host;
			}
			,btn2: function(index, layero){
				window.location.href='domainSet.do';
			}
		});
		
		
	}else{
		window.location.href='domainSet.do';
	}
}

//选择使用obs存储
function selectObs(){
	/* msg.popups({
		text:'此种方式只有战略合作伙伴才会安装配置',
		width:'200px'
	}); */
	
	msg.loading('正在判断是否存在华为云OBS配置引导插件');
	post('/plugin/huaWeiYunServiceCreate/isExist.json', {}, function(result){
		msg.close();
		if(result.result == '1'){
			//成功，有这个插件，跳转进入
			window.location.href='/plugin/huaWeiYunServiceCreate/index.do';
		}
	});
	
	//3秒要是还没获取到响应，那肯定就是没这个插件
	setTimeout(function () {
		msg.close();
		msg.alert('当前系统未安装华为云OBS自动安装引导插件，请联系我们战略合作伙伴进行安装');
	}, 3000);
	
	/* msg.confirm('使用华为云OBS存储？此种方式只有战略合作伙伴才会安装配置',function(){
		msg.confirm({
			text:'您可在安装完成后，登录总管理后台（账号密码都是admin），找到 【功能插件】 下的 华为云 插件即可完成配置',
			buttons:{
				确定:function(){
					window.location.href='domainSet.do';
				}
			} 
		});
		
	}); */
}
</script>

<jsp:include page="/wm/common/foot.jsp"></jsp:include> 