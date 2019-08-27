<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
		    margin-top: 50px;
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
    		欢迎使用 网市场云建站系统 v<%=G.VERSION %>
    	</div>
    	
    	
    	<div style="padding-top:100px;; padding-bottom:110px; text-align:center;">
			<a href="/install/selectAttachment.do" class="layui-btn layui-btn-primary" style="line-height: 0px;padding: 30px;font-size: 20px;">点击此处开始安装本系统</a>
			<br/>
			<div style="font-size:12px; padding-top:8px; ">
				<span style="color: gray;">交流QQ群：472328584</span> &nbsp;&nbsp; <span style="color: gray;">作者微信：xnx3com</span>
				<!-- <a href="javascript:updateSystemConfig();" style="color: gray;">安装好后再次修改配置方式</a> -->
			</div>
			
		</div>
    	
    	<div class="info">
    		<h3>服务器选配</h3>
    		最低配置要求：1核1G <a href="http://help.wscso.com/16329.html" target="_black">详细配置点此查看，最低一元成本！</a>
    	</div>
    	
    	<div class="info">
    		<h3>数据库</h3>
    		系统默认内置 Sqlite 数据库，如果你只是建几个网站，那用这个就足够，无需任何其他配置<br/>
    		如果你是建站公司，建议配置 Mysql 数据库。<a href="http://help.wscso.com/10240.html" target="_black">点此查看Mysql配置说明</a>
    	</div>
    	
    	
    	
    	<div style="width: 100%;text-align: center; padding-top: 50px;">
    		<a href="http://www.leimingyun.com" target="_black" style="color: gray;font-size: 8px;">潍坊雷鸣云网络科技有限公司</a>
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
