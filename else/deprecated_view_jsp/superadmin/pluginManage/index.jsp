<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../../iw/common/adminSubMenu/head.jsp">
	<jsp:param name="title" value="插件管理"/>
</jsp:include>

			<li class="layui-nav-item">
				<a href="javascript:loadUrl('myList.do');" >我开发的插件</a>
			</li>
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('installList.do');" >已安装的插件</a>
			</li>
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('yunList.do');" >插件云市场</a>
			</li>
			
			<!-- 
				<li class="layui-nav-item" id="super2">
					<a href="javascript:;" class="dltitle">新闻咨询</a>
					<dl class="layui-nav-child" style="background-color: #EAEDF1;">
				  	<dd>
				  		<a href="#"  style="background-color: #fff; color:#222;" >公司新闻</a>
				  		<script>document.getElementById("super2").className+=" layui-nav-itemed";</script>
				  	</dd>
				  	<dd>
				  		<a href="#" >行业动态</a>
				  	</dd>
				  	</dl>
			  	</li>
		  	 -->

<jsp:include page="../../iw/common/adminSubMenu/foot.jsp" />
<script>loadUrl('yunList.do');</script>