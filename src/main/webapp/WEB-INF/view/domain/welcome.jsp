<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="欢迎使用网市场云建站系统"/>
</jsp:include>

  	<style>
  		.content{
  			width: 600px;
  			min-height:80%;
		    margin: 0 auto;
		    box-shadow: rgba(0, 0, 0, 0.06) 0px 1px 10px 2px;
		    padding: 30px;
		    margin-top: 90px;
  		}
  		.title{
  			border-bottom: 1px solid #eee;
		    padding-top: 20px;
		    padding-left: 10px;
		    padding-bottom: 20px;
		    font-size: 28px;
		    margin-bottom: 20px;
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
  		.info h2{
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
  	</style>
  	
    <div class="content">
    	<div class="title">
    		欢迎使用 网市场云建站系统 v<%=G.VERSION %>
    	</div>
    	
    	<div style="padding:15%; text-align:center;">
			<a href="<%=basePath %>install/selectAttachment.do" class="layui-btn layui-btn-primary" style="line-height: 0px;padding: 28px;font-size: 20px;">点击此处开始安装本系统</a>
			<br/>
			<div style="font-size:14px; padding-top:5px; color: gray;">
				<a href="https://v.qq.com/x/page/c053533596l.html" style="">查看视频演示</a>
				|
				<a href="javascript:updateSystemConfig();">安装好后再次修改配置方式点此查看</a>
			</div>
			
		</div>

    	<div class="info">
			本系统您完全可以自行部署到你们自己的服务器上进行商用，这一切，都是免费的！如果您感觉效果不错，欢迎捐献支援我(<a href="http://www.wang.market/5541.html" target="_black">购买授权</a>)
    		
    		<h2>使用帮助</h2>
    		<ul>
	    		<li>本系统共分为三种后台，其中，开通网站是在 <a href="http://help.wscso.com/5717.html" target="_black">代理后台</a> 进行开通！</li>
	    		<li>如果您要正式使用时，请一定记得自行修改 admin、 agency 的默认密码</li>
	    	</ul>
    		
    		<h2>联系交流</h2>
    		官网：<a href="http://www.wang.market" target="_black">www.wang.market</a><br/>
    		交流QQ群：472328584<br/>
    		作者：管雷鸣<br/>
    		作者QQ：921153866<br/>
    		作者微信：xnx3com<br/>
    		微信公众号：wangmarket<br/>
    	</div>
    	
    	
    	
    </div>
    
    <script>
    //修改系统相关配置，也就是安装的系统参数
    function updateSystemConfig(){
    	layer.open({
   		  title: '修改方式'
   		  ,content: '当您安装成功后，可使用账号 admin  密码 admin 进行登录，登录成功后，在 系统设置 - 系统变量 中，可进行修改相关配置参数。<br/>本次安装，便是针对系统变量的一些重要参数进行简单的引导设定。<br/>注意：看不懂的建议别随便改'
   		});
    }
    
    
    </script>
    
  </body>
</html>
