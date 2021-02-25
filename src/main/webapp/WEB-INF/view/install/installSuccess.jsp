<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/wm/common/head.jsp">
	<jsp:param name="title" value="安装成功"/>
</jsp:include>

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
  			line-height: 22px;
  		}
  		.info h2,h3,h4,h5{
 			border-bottom: 1px solid #eee;
		    padding-top: 23px;
		    margin-bottom: 10px;
		    padding-bottom: 5px;
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
    		恭喜您，系统安装成功！
    	</div>
    	
    	<div class="info">
    		网市场云建站系统，共有三种管理后台，有专门管理网站的网站管理后台，有专门开通、暂停网站的代理后台
    	</div>
    	
    	<div class="info">
    		<h2>后台登陆网址</h2>
    		<script>
    			document.write('<a href="'+window.location.origin+'/login.do" target="_black">'+window.location.origin+'/login.do</a>');
    		</script>
    		<br/>
    		三种后台，网站管理后台、代理后台、总管理后台，登陆的地址都是这一个。只是不同账号拥有不同权限，登陆成功后根据账号所拥有的权限，自动跳转到相应的后台。
    	</div>
    	
    	<div class="info">
    		<h2>网站管理后台</h2>
    		更改网站页面、添加新闻、更改公司简介等，建立、维护、管理具体某个网站的<br/>
			客户可以通过开通的账号跟密码，自己登陆网站管理后台，进行管理网站。比如发布新闻信息、产品信息、查看网站访问统计等。
			<br/>这里有一个用于体验的网站，默认的登陆账号跟密码都是 <b>wangzhan</b>
			<br/>你也可以通过下面的代理后台，开通多个网站
			<br/>
			<a href="http://help.wscso.com/5732.html" target="_black">查看详细说明</a>
    	</div>
    	
    	<div class="info">
    		<h2>代理后台</h2>
    		可开通网站、开通下级代理、为某个网站延长使用时间、冻结、解冻某个网站。
    		<br/>默认的登陆账号跟密码都是 <b>agency</b> &nbsp;&nbsp; 真正使用时，记得自己更改密码
    		<br/><a href="http://help.wscso.com/5717.html" target="_black">查看详细说明</a>
    	</div>
    	<div class="info">
    		<h2>总管理后台</h2>
    		总管理后台，主要只是提供查看的作用。如查看所有网站、用户等。实际使用的过程中，忽略掉这个后台即可。主要是给开发、维护人员使用的。
    		<br/>默认的登陆账号跟密码都是 <b>admin</b> &nbsp;&nbsp; 真正使用时，记得自己更改密码
    		<br/><a href="http://help.wscso.com/5716.html"  target="_black">查看详细说明</a>
    	</div>
    	
    	<div style="padding-top:100px;; padding-bottom:200px; text-align:center;">
			<a href="/login.do" class="layui-btn layui-btn-primary" style="line-height: 0px;padding: 24px;font-size: 17px; border-radius: 5px;">登陆后台体验一下吧</a>
		</div>
    	
    	<div style="width: 100%;text-align: center;">
    		<a href="http://www.leimingyun.com" target="_black" style="color: gray;font-size: 8px; padding-left:40px; padding-right:40px;">潍坊雷鸣云网络科技有限公司</a>
    	</div>
    </div>
    
  </body>
</html>