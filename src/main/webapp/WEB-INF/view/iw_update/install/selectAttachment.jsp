<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
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

<div style="padding:30px; padding-bottom:10px; text-align:center; font-size:20px;">
	第一步：选择文件存储方式
	<div style="font-size: 14px;">
		存储上传的图片、附件、网站的缓存页面(.html)、以及页面请求的静态资源
	</div>
</div>

<div class="select">
	
	<div class="selectItem" onclick="window.location.href='setAttachmentMode.do?mode=${AttachmentFile_MODE_LOCAL_FILE}';">
		<div class="title">可选一：服务器本身存储</div>
		<div class="intro">
			<br/>
			使用运行程序的服务器本身作为存储对象，会在程序根目录下创建site文件夹存储文件，传统文件存储方式。将生成的html页面、上传的图片，都存储在代码所在的服务器上。
			<br/>优点：简单，免费！可快速使用，无需再掌握其他知识。
			<br/>缺点：无法分布式部署。建议开通的网站个数在一万个以内
		</div>
	</div>
	
	<br/>
	
	<div class="selectItem" id="yun" onmouseenter="qiehuanshouquan();" onmouseleave="qiehuanshouquanyuanshi();" style="color: gray;" onclick="window.open('http://www.wang.market/5541.html')">
		<div class="title">可选二：云存储（需授权）</div>
		<!-- setAttachmentMode.do?mode=${AttachmentFile_MODE_ALIYUN_OSS} -->
	</div>
	<div style="display:none;" id="weishouquanyun">
		<div class="title">可选二：云存储（需购买授权）</div>
		<div class="intro">
			云端存储，速度更快、数据更安全！
			<br/>优点：
					<ul class="lilist"> 
						<li>去我方版权标识及链接，相当于你自己的产品，自由开展你的业务。</li>
						<li>可分布式部署，无网站个数限制</li>
						<li>使用CDN加速，让网站极速访问</li>
						<li>增加统计功能</li>
						<li>增可用手机号自助开通网站</li>
						<li>可配置多个域名解析绑定服务器，如一台香港的可绑定不需要备案的域名，一台国内的绑定已备案的域名</li>
						<li>包安装</li>
						<li>提供技术支持、咨询</li>
					</ul>
		</div>
	</div>
	<div style="display:none;" id="shouquanxinxi">
		<div class="title">可选二：云存储（需授权）</div>
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