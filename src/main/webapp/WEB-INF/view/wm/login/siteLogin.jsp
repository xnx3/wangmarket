<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	//登陆用户名
	String username = request.getParameter("username");
	if(username == null || (username != null && username.equals("null"))){
		username = "";
	}
	
	//登陆密码
	String password = request.getParameter("password");
	if(password == null || (password != null && password.equals("null"))){
		password = "";
	}
 %>
<jsp:include page="/wm/common/head.jsp">
	<jsp:param name="title" value="登录"/>
</jsp:include>

<style>
.myForm{
	margin: 0 auto;
    width: 495px;
    height: 360px;
    border-width: 1px 1px 1px 1px;
    padding: 0px;
    border-radius: 20px;
    overflow: hidden;
    -webkit-box-shadow: 0 0 10px #e2e2e2;
    -moz-box-shadow: 0 0 10px #e2e2e2;
    box-shadow: 0 0 10px #e2e2e2;
    position: absolute;
    left: 50%;
    top: 50%;
    margin-left: -248px;
    margin-top: -181px;
}

@media screen and (max-width:600px){
	.myForm{
		margin: 0 auto;
	    width: 100%;
	    height: 100%;
	    border-width: 0px;
	    padding: 0px;
	    border-radius: 0px;
	    overflow: auto;
	    -webkit-box-shadow: 0 0 0px #e2e2e2;
	    -moz-box-shadow: 0 0 0px #e2e2e2;
	    box-shadow: 0 0 0px #e2e2e2;
	    position: static;
	    left: 0px;
	    top: 0px;
	    margin-left: 0px;
	    margin-top: 0px;
	}
}

.touming{
	background: rgba(0,190,150,0.1) none repeat scroll !important;
}
.baisetouming{
	background: rgba(250,250,250,0.1) none repeat scroll !important;
}
</style>

<!-- 背景 -->

<form class="layui-form layui-elem-quote layui-quote-nm myForm">
  <div class="layui-form-item touming" style="height: 70px;background-color: #eeeeee;line-height: 70px;text-align: center;font-size: 25px;color: #3F4056;">
    <%=Global.get("SITE_NAME") %> 平台登陆
  </div>
  <div style="padding: 30px 50px 40px 0px;">
  	<div class="layui-form-item">
	    <label class="layui-form-label">用户名</label>
	    <div class="layui-input-block">
	      <input type="text" name="username" required  lay-verify="required" value="<%=username %>" placeholder="请输入 用户名/邮箱" autocomplete="off" class="layui-input baisetouming">
	    </div>
	  </div>
	  <div class="layui-form-item">
	    <label class="layui-form-label"> 密 码&nbsp;&nbsp; </label>
	    <div class="layui-input-block">
	      <input type="password" name="password" required lay-verify="required" value="<%=password %>" placeholder="请输入密码" autocomplete="off" class="layui-input baisetouming">
	    </div>
	  </div>
	  <div class="layui-form-item">
	    <label class="layui-form-label"> 验证码 </label>
	    <div class="layui-input-inline">
	      <input type="text" name="code" required lay-verify="required" placeholder="请输入右侧验证码" autocomplete="off" class="layui-input baisetouming">
	    </div>
	    <div class="layui-form-mid layui-word-aux" style="padding-top: 3px;padding-bottom: 0px;"><img id="code" src="captcha.do" onclick="reloadCode();" style="height: 29px;width: 110px; cursor: pointer;" /></div>
	  </div>
	  <div class="layui-form-item" style="display:none">
	    <label class="layui-form-label">记住密码</label>
	    <div class="layui-input-block">
	      <input type="checkbox" name="switch" lay-skin="switch">
	    </div>
	  </div>
	  <div class="layui-form-item">
	    <div class="layui-input-block">
	      <button class="layui-btn" lay-submit lay-filter="formDemo" style="opacity:0.6; margin-right: 50px;">立即登陆</button>
	      <button type="reset" class="layui-btn layui-btn-primary baisetouming" style="width: 90px;">重置</button>
	    </div>
	  </div>
  </div>
</form>
 
<!--[if IE]>
	<div style="position: absolute;bottom: 0px;padding: 10px;text-align: center;width: 100%;background-color: yellow;">建议使用<a href="https://www.baidu.com/s?wd=Chrome" target="_black" style="text-decoration:underline">Chrome(谷歌)</a>、<a href="https://www.baidu.com/s?wd=Firefox" target="_black" style="text-decoration:underline">Firefox(火狐)</a>浏览器，IE浏览器会无法操作！！！</div>
<![endif]-->

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

</script>


</body>
</html>