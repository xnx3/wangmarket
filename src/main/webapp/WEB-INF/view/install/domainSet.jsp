<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/wm/common/head.jsp">
	<jsp:param name="title" value="域名设置"/>
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
    		域名设置
    	</div>
    	
    	<div class="info">
    		本系统需要独占一个域名！<br/> 
			比如你准备的域名为 leimingyun.com <br/> 
			那么你需要进入你的域名管理后台，做个泛解析 *.leimingyun.com 指向到你这个服务器 ip
    	</div>
    	
    	<div class="info">
    		<h2>第一步：设置域名</h2>
    		
    		<div class="layui-form-item" style="padding-top: 20px;">
				<label class="layui-form-label">你的域名：</label>
				<div class="layui-input-block" style="width: 200px; float: left;margin-left: 10px;">
					<input type="text" id="autoAssignDomain" onkeypress="domainChange();" onchange="domainChange();" name="autoAssignDomain" placeholder="请输入您要配置的域名" class="layui-input" value="${autoAssignDomain}" style="width: 200px;">
				</div>
				<div style="color:#d2d2d2; font-size: 13px;">
					&nbsp;&nbsp;填入格式如  leimingiyun.com  
					<br/>&nbsp;&nbsp;注意，域名不带www，这个是要做泛解析的
				</div>
			</div>
    	</div>
    	
    	<div class="info">
    		<h2>第二步：进行域名泛解析</h2>
    		登陆你的域名管理后台，添加一条泛解析 <span style="color: rebeccapurple; font-size: 16px; padding-left: 10px; padding-right: 10px;">*.<span id="jiexidomain"></span></span> 指向到本服务器的ip
    		<br/>
    		<span style="color:gray; font-size:8px;">泛解析不会的，可以自行百度搜索。或者<a href="https://www.leimingyun.com/pay.html" target="_black">请求我们有偿帮助</a></span>
    	</div>
    	
    	
    	<div style="padding-top:100px;; padding-bottom:200px; text-align:center;">
			<button onclick="saveDomain();" class="layui-btn layui-btn-primary" style="line-height: 0px;padding: 24px;font-size: 17px; border-radius: 5px;">保存设置，完成安装</button>
			<button onclick="quickTest();" class="layui-btn " style="line-height: 0px;padding: 24px;font-size: 17px; border-radius: 5px;">跳过设置，只是体验</button>
		</div>
    	
    	<!-- <div style="width: 100%;text-align: center;">
    		<a href="http://www.leimingyun.com" target="_black" style="color: gray;font-size: 8px;">网市场云建站系统 - 潍坊雷鸣云网络科技有限公司</a>
    	</div> -->
    </div>
    
    <script>
	//域名改变,也就是input输入框的值改变触发
	function domainChange(){
		document.getElementById('jiexidomain').innerHTML = document.getElementById('autoAssignDomain').value;
	}
	domainChange();
    
    //修改系统相关配置，也就是安装的系统参数
    function updateSystemConfig(){
    	layer.open({
   		  title: '修改方式'
   		  ,content: '当您安装成功后，可使用账号 admin  密码 admin 进行登录，登录成功后，在 系统设置 - 系统变量 中，可进行修改相关配置参数。<br/>本次安装，便是针对系统变量的一些重要参数进行简单的引导设定。<br/>注意：看不懂的建议别随便改'
   		});
    }
    
    //点击了保存更改按钮
    function saveDomain(){
    	var domain = document.getElementById('autoAssignDomain').value;
    	if(domain.length < 3){
			msg.failure("请先设置域名");
    		return;
    	}

		msg.loading("更改中");    //显示“更改中”的等待提示
		$.post(
		    "domainSetSave.do", 
		    { "autoAssignDomain": domain }, 
		        function(result){
					msg.close();    //关闭“更改中”的等待提示
		        if(result.result != '1'){
					msg.failure(result.info);
		        }else{
					//安装成功
					window.location.href="installSuccess.do";
		        }
		    }, 
		"json");
    }
    
    //快速体验，自动设置域名
    function quickTest(){
    	layer.confirm('确定只是体验系统功能吗？体验完后，如果你想线上正式使用，请重新安装一次。', {icon: 7, title:'提示'}, function(index){
		  window.location.href="setLocalDomain.do";
		  layer.close(index);
		});
    }
    
    </script>
    
  </body>
</html>
