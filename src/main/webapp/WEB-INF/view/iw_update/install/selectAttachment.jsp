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
			<b>进行安装体验本程序，或者对云不是很了解，建议使用此种方式</b>
			<br/>
			使用运行程序的服务器本身作为存储对象，会在程序根目录下创建site文件夹存储文件。同帝国CMS、织梦CMS的传统文件存储方式。
			<br/>优点：简单，可快速使用，无需再掌握其他知识
			<br/>缺点：无法分布式部署。建议开通的网站个数在1000个以内
		</div>
	</div>
	
	<br/>
	<div class="selectItem" onclick="window.location.href='setAttachmentMode.do?mode=${AttachmentFile_MODE_ALIYUN_OSS}';">
		<div class="title">可选二：阿里云OSS存储</div>
		<div class="intro">
			云端存储，速度更快、数据更安全！
			<br/>优点：
					<ul class="lilist"> 
						<li>可分布式部署，无网站个数限制</li>
						<li>使用CDN加速，让网站极速访问</li>
						<li>可配置多个域名解析绑定服务器，如一台香港的可绑定不需要备案的域名，一台国内的绑定已备案的域名</li>
					</ul>
			<div>缺点：需了解相关云知识(OSS、CDN等)，稍微有一点点门槛</div>
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


<jsp:include page="../../iw/common/foot.jsp"></jsp:include> 