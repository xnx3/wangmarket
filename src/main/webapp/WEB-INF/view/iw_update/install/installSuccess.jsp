<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="设置阿里云AccessKey参数"/>
</jsp:include>

<div style="padding:30px; padding-bottom:10px; text-align:center; font-size:20px;">
	恭喜您，系统安装配置成功！网市场云建站系统，共分为三种管理后台，如下所示，请查看！
	<a href="delete.do" target="_black" style="font-size:12px;">删除安装文件</a>
	<br/>
	
	
	<style>
	.sanhoutai{
		text-align:left;
	}
	.sanhoutai>div{
		float:left;
		width:30%;
		padding:1.5%;
	}
	.title{
		border-bottom-style: inset;
	    padding: 5px;
	    margin-bottom: 10px;
	}
	.login{
		font-size:12px;
	}
	.intro{
		font-size:13px;
	}
	.xiangqing{
		padding-top:30px;
		text-align:center;
	}
	</style>
	<div style="padding-top:50px; " class="sanhoutai">
		
		<div>
			<div class="title">网站管理后台</div>
			<div class="login">
				<div>
					登陆地址：
					<a href="../login.do" target="_black"><%=Global.get("MASTER_SITE_URL") %>login.do</a>
				</div>
				<div>登陆账号： <b>wangzhan</b></div>
				<div>登陆密码： <b>wangzhan</b></div>
			</div>
			<div class="intro">
				网站管理后台，是专门针对网站进行修改、操作的后台。<br/>
				客户可以通过开通的账号跟密码，自己登陆网站管理后台，进行管理网站。比如发布新闻信息、产品信息、查看网站访问统计等。
			</div>
			<div class="xiangqing">
				<a href="http://help.wscso.com/5732.html" class="layui-btn layui-btn-primary" target="_black">查看详细说明</a>
			</div>
		</div>
		
		<div>
			<div class="title">代理后台</div>
			<div class="login">
				<div>
					登陆地址：
					<a href="../login.do" target="_black"><%=Global.get("MASTER_SITE_URL") %>login.do</a>
				</div>
				<div>登陆账号： <b>agency</b></div>
				<div>登陆密码： <b>agency</b></div>
			</div>
			<div class="intro">
				开通网站、开通下级代理，都是通过代理后台来开通的。
			</div>
			<div class="xiangqing">
				<a href="http://help.wscso.com/5717.html" class="layui-btn layui-btn-primary" target="_black">查看详细说明</a>
			</div>
		</div>
		
		
		<div>
			<div class="title">总管理后台</div>
			<div class="login">
				<div>
					登陆地址：
					<a href="../login.do" target="_black"><%=Global.get("MASTER_SITE_URL") %>login.do</a>
				</div>
				<div>登陆账号： <b>admin</b></div>
				<div>登陆密码： <b>admin</b></div>
			</div>
			<div class="intro">
				总管理后台，只是提供查看审核的作用。如查看统计信息、审核所有网站内有没有发布违法违规的文章等。
			</div>
			<div class="xiangqing">
				<a href="http://help.wscso.com/5716.html" class="layui-btn layui-btn-primary" target="_black">查看详细说明</a>
			</div>
		</div>
	</div>
	
	
	<div style=" position: absolute; bottom: 20px; text-align: center;width: 100%;">
		提示：网站做好后，请去自行将这三个密码改掉！
	</div>
	
</div>

<jsp:include page="../../iw/common/foot.jsp"></jsp:include> 