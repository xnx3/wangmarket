<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.xnx3.wangmarket.Authorization"%>
<%@page import="com.xnx3.j2ee.util.SafetyUtil"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	//登陆用户名
	String username = SafetyUtil.filter(request.getParameter("username"));
	if(username == null || (username != null && username.equals("null"))){
		username = "";
	}
	
	//登陆密码
	String password = SafetyUtil.filter(request.getParameter("password"));
	if(password == null || (password != null && password.equals("null"))){
		password = "";
	}
 %>
<jsp:include page="/wm/common/head.jsp">
	<jsp:param name="title" value="登录"/>
</jsp:include>
    <!-- Meta tag Keywords -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <!-- <link href="${STATIC_RESOURCE_PATH}plugin/login/css/Noto-Sans_JP.css" rel="stylesheet">  -->
    <link rel="stylesheet" href="${STATIC_RESOURCE_PATH}plugin/login/css/style.css" type="text/css" media="all" />
    <link rel="stylesheet" href="${STATIC_RESOURCE_PATH}plugin/login/css/font-awesome.min.css" type="text/css" media="all">

    <style type="text/css">
        .content-wthree input{
            background: #FFFFFF;
        }
        .codeBox{
            background-color: #FFFFFF;
            overflow: hidden;
            display: flex;
            align-items: center;
        }
    </style>

    <div id="block" class="w3lvide-content" data-vide-bg="${STATIC_RESOURCE_PATH}plugin/login/images/freelan" data-vide-options="position: 0% 50%">
            <!-- /form -->
            <div class="workinghny-form-grid" style="width: 100%">
                <div class="main-hotair">
                    <div class="content-wthree">
                        <h1><span style="
    font-family: 'Pacifico', cursive;
    font-size: 50px;
    color: #fff;
    letter-spacing: 5px;
    text-align: center;
"><%=Global.get("SITE_NAME") %></span></h1>
                        <form>
                            <input type="text" class="text" name="username" value="<%=username %>" placeholder="请输入 用户名/邮箱" required  lay-verify="required" autocomplete="off">
                            <input type="password" id="password" class="password" name="password" value="<%=password %>" placeholder="请输入密码"  required  lay-verify="required" autocomplete="off">
                            <div style="position: relative">
                                <input type="text" class="text" name="code" placeholder="请输入右边验证码"  required  lay-verify="required" autocomplete="off" style="display: block">
                                <div class="layui-word-aux codeBox"><img id="code" src="captcha.do" onclick="reloadCode();" style="cursor: pointer;" /></div>
                            </div>

                            <div class="layui-form-item" style="display:none">
                                <label class="layui-form-label">记住密码</label>
                                    <div class="layui-input-block">
                                        <input type="checkbox" name="switch" lay-skin="switch">
                                    </div>
                            </div>
                            
                            <button class="btn" lay-submit lay-filter="formDemo">立即登陆</button>
<%--                            <button type="reset" class="btn" style="margin-top:10px">重置</button>--%>
                        </form>

                    </div>
                  
                </div>
           
     
        <!-- 未授权用户，请尊重作者劳动成果，保留我方版权标示及链接！否则将追究法律责任。授权参见：http://www.wang.market/price.html -->
		<% if(Authorization.copyright){ 
				Date d = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				String nowYear = df.format(d);
		%>
			 <!-- copyright-->
	        <div class="copyright text-center" style="padding-top:1rem;">
	            <p class="copy-footer-29" style="font-size: 12px; color: gray;"><a href="http://cms.zvo.cn" target="_black" style="color: gray;">Copyright © 2016-<%=nowYear %>,  power by wangmarket CMS </a></p>
	        </div>
		<% } %>
		
		        
        
        
       </div>
        <!-- //copyright-->
    </div>
    <!-- //form section start -->
      <!-- js -->
  <script src="/plugin/login/js/jquery.min.js"></script>
  <!-- //js -->
  <script>
  	var STATIC_RESOURCE_PATH = '${STATIC_RESOURCE_PATH}';
  	//STATIC_RESOURCE_PATH = '/';
  </script>
  <script src="/plugin/login/js/jquery.vide.js"></script>
<script>
//Demo
layui.use('form', function(){
	var form = layui.form;

	//监听提交
	form.on('submit(formDemo)', function(data){
		msg.loading("登陆中");
		var d=$("form").serialize();
		$.post("wangmarketLoginSubmit.do", d, function (result) {
			msg.close();
			var obj = JSON.parse(result);
			try{
				console.log(obj);
			}catch(e){}
			if(obj.result == '1'){
				localStorage.setItem('token',obj.token);
				msg.success("登陆成功", function(){
					window.location.href=obj.info;
				});
			}else if(obj.result == '0'){
				//登陆失败
				msg.failure(obj.info);
				reloadCode();
			}else if(obj.result == '11'){
				//网站已过期。弹出提示
				reloadCode();
				layer.open({
					title: '到期提示'
					,content: obj.info
				});	 
			}else{
				reloadCode();
				msg.failure(result);
			}
		}, "text");
		return false;
	});
});

//重新加载验证码
function reloadCode(){
var code=document.getElementById('code');
code.setAttribute('src','captcha.do?'+Math.random());
//这里必须加入随机数不然地址相同我发重新加载
}

/* 
//检测浏览器，若不是Chrome浏览器，弹出提示
if(navigator.userAgent.indexOf('Chrome') == -1){
	layer.open({
		type: 1
		,title:'提示'
		,offset: 'rb' //具体配置参考：offset参数项
		,content: '<div style="padding: 18px; line-height:30px;">建议使用<a href="https://www.baidu.com/s?wd=Chrome" target="_black" style="text-decoration:underline">Chrome(谷歌)</a>浏览器<br/>其他浏览器登录可能无法正常操作！</div>'
		,btn: ['下载Chrome','忽略']
		,btnAlign: 'c' //按钮居中
		,shade: 0 //不显示遮罩
		,yes: function(){
			layer.closeAll();
			window.open("https://www.baidu.com/s?wd=Chrome");
		}
		,btn2: function(){
			layer.closeAll();
		}
	});
} */
/*
$(document).ready(function () {
	$("#block").vide("111video/ocean"); // Non declarative initialization
    var instance = $("#block").data("vide"); // Get instance
   	var video = instance.getVideoObject(); // Get video object
    instance.destroy(); // Destroy instance
});
*/
</script>

<script>
    $(function() {
        $('#password').val('');
    });
</script>

<jsp:include page="/wm/common/foot.jsp"></jsp:include> 
<style> /* 显示多语种切换 */ .translateSelectLanguage{ display:block;background-color: rgba(255,255,255,.1);color: #ff9800 } </style>