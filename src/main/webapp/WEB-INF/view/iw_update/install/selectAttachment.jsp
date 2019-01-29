<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="设置选择文件存储方式"/>
</jsp:include>

<style>
.layui-form-label{
	width:180px;
}
.layui-input-block{
	margin-left: 210px;
}
</style>

<div style="padding:30px; padding-bottom:10px; text-align:center; font-size:30px;">
	选择使用方式
</div>

<div class="select">
	
	<div class="selectItem" onclick="window.location.href='setAttachmentMode.do?mode=${AttachmentFile_MODE_LOCAL_FILE}';">
		<div class="title">免费版</div>
		<div class="intro">
			<br/>服务器存储
			<ul class="lilist"> 
				<li>安装方便，一键运行</b></li>
				<li>使用 Sqlite 数据库</li>
				<li>可用于商业用途</li>
				<li>可开通数千网站</li>
			</ul>
			使用运行程序的服务器本身作为存储对象，会在程序根目录下创建site文件夹存储文件，传统文件存储方式。将生成的html页面、上传的图片，都存储在代码所在的服务器上。
			<br/>优点：简单，免费！可快速使用，无需再掌握其他知识。
			<br/>缺点：无法分布式部署。网站个数建议在500个以内。另外使用请看帮助说明 <a href="http://help.wscso.com" target="_black">help.wscso.com</a> 或者加入QQ群472328584讨论交流，免费版我们不提供技术客服，请各位谅解。如果您想获得更好的服务，以及更好用它来服务客户，想更好的赚钱，欢迎联系我们购买授权版本。
		</div>
	</div>
	
	<br/>
	
	<div class="selectItem" id="yun" onmouseenter="qiehuanshouquan();" onmouseleave="qiehuanshouquanyuanshi();" onclick="window.open('http://www.wang.market/price.html')">
		<div class="title">收费版</div>
		<!-- setAttachmentMode.do?mode=${AttachmentFile_MODE_ALIYUN_OSS} -->
	</div>
	<div style="display:none;" id="weishouquanyun">
		<div class="title">收费版</div>
		<div class="intro">
			云端存储，无限扩展、速度更快、数据更安全！
			<br/>优点：
					<ul class="lilist"> 
						<li>我们帮你安装调试好，你只管用！</li>
						<li>使用Mysql数据库</li>
						<li>我方可帮你选择阿里云服务器规格及购买。仅此项，就可以让你<b>省不止500元！</b></li>
						<li>少走弯路，技术指导，有不懂的地方可以向我们咨询，帮你快速掌握使用技巧。</li>
						<li>正版授权，去我方版权标识</li>
						<li>可分布式部署，无网站个数限制</li>
						<li>使用CDN加速，网站极速访问</li>
						<li>增加访问统计、操作日志等功能</li>
						<li>可配置多个域名解析绑定服务器，如一台香港的可绑定不需要备案的域名，一台国内的绑定已备案的域名</li>
					</ul>
		</div>
	</div>
	<div style="display:none;" id="shouquanxinxi">
		<div class="title">收费版</div>
		<div style="font-size: 38px;color: red;">
			需我方授权！
			<br/>
			授权费用 ：500元/年、 或5000元永久
			<br/>
			永久使用。
		</div>
	</div>
	
</div>
<br/>

<style>
.first{background-color: #5FB878;}
.select{
	padding:20px;
}
.select>div{
	padding: 30px;
    border: 1px;
    border-style: solid;
    border-color: #d2d2d2;
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
<jsp:include page="common.jsp"></jsp:include>

<script>
function qiehuanshouquan(){
	document.getElementById('yun').innerHTML = document.getElementById('shouquanxinxi').innerHTML; 
}
//用户鼠标移开，显示云版本的功能
function qiehuanshouquanyuanshi(){
	document.getElementById('yun').innerHTML = document.getElementById('weishouquanyun').innerHTML;
}
qiehuanshouquanyuanshi();
</script>

<jsp:include page="../../iw/common/foot.jsp"></jsp:include> 