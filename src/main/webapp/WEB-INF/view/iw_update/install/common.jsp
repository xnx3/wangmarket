<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
.foot{
	font-size:20px; 
	background-color: #F0F0F0;
	padding:20px;
	text-align:center;
	
}
.foot>li{
	padding-right:10px;
	display: inline-block;
}
</style>
<ul class="foot" style="display:none;">
	<li class="first">
		<a href="selectAttachment.do">
			第一步.选择使用方式
		</a>
	</li>
	<li>
		>
	</li>
	
	<li class="two">
		<a href="systemSet.do">
			第二步.设置系统参数
		</a>
	</li>
	<li class="three">
		>
	</li>
	
	<li class="three">
		<a href="accessKey.do">
			第三步.设置阿里云 AccessKey
		</a>
	</li>
	
</ul>
<script>
	if('<%=Global.get("ATTACHMENT_FILE_MODE") %>' != 'aliyunOSS'){
		//不是阿里云OSS， 那么需要将第三部设置阿里云参数去掉
		$('.three').css("display","none");
	}
</script>