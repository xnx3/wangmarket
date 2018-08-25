<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="免费注册"/>
</jsp:include>
<script src="<%=basePath+Global.CACHE_FILE %>Site_client.js"></script>
<script src="<%=Global.get("ATTACHMENT_FILE_URL") %>js/admin/commonedit.js?v=<%=G.VERSION %>"></script>
<style>
.myForm{
	margin: 0 auto;
    width: 495px;
    height: auto;
    border-width: 1px 1px 1px 1px;
    padding: 0px;
    border-radius: 20px;
    overflow: hidden;
    -webkit-box-shadow: 0 0 10px #e2e2e2;
    -moz-box-shadow: 0 0 10px #e2e2e2;
    box-shadow: 0 0 10px #e2e2e2;
    position: absolute;
    left: 50%;
    top: 45%;
    margin-left: -248px;
    margin-top: -230px;
}
#divCode{
	padding:20px;
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
</style>

<form class="layui-form layui-elem-quote layui-quote-nm myForm" action="<%=basePath %>regSubmit.do">
  <div class="layui-form-item" style="    height: 70px;
    background-color: #eeeeee;
    line-height: 70px;
    text-align: center;
    font-size: 25px;
    color: #3F4056;
    white-space:nowrap;
    ">
    <%=Global.get("SITE_NAME") %> 注册
  </div>
  <div style="padding: 30px 50px 40px 0px;">
  	
  	<div class="layui-form-item">
	    <label class="layui-form-label">用户名</label>
	    <div class="layui-input-block">
	      <input type="text" name="username" required  lay-verify="required" placeholder="登录使用。只限英文或数字" autocomplete="off" class="layui-input">
	    </div>
	  </div>
	  <div class="layui-form-item">
	    <label class="layui-form-label"> 邮  箱&nbsp;&nbsp; </label>
	    <div class="layui-input-block">
	      <input type="text" name="email" placeholder="可用于接收提醒" autocomplete="off" class="layui-input">
	    </div>
	  </div>
	  <div class="layui-form-item">
	    <label class="layui-form-label"> 密 码&nbsp;&nbsp; </label>
	    <div class="layui-input-block">
	      <input type="password" name="password" required lay-verify="required" placeholder="请输入密码" autocomplete="off" class="layui-input">
	    </div>
	  </div>
	  <div class="layui-form-item" style="display:none;">
	    <label class="layui-form-label">手机号</label>
	    <div class="layui-input-block">
	      <input type="text" name="phone" id="phone" placeholder="请输入您的手机号" autocomplete="off" class="layui-input">
	    </div>
	  </div>
	  <div class="layui-form-item" style="display:none;">
	    <label class="layui-form-label">验证码</label>
	    <div class="layui-input-inline">
	      <input type="text" name="code"  placeholder="右侧获取手机验证码" autocomplete="off" class="layui-input">
	    </div>
	    <div class="layui-form-mid layui-word-aux" style="padding-top: 3px;padding-bottom: 0px; padding-left:5px;">
			<span class="layui-btn layui-btn-sm layui-btn-primary" onclick="getPhoneCode();">获取验证码</span>
		</div>
	  </div>
	  
	  <div class="layui-form-item">
	    <div class="layui-input-block">
	      <button class="layui-btn" lay-submit lay-filter="formDemo">免费开通</button>
	      <a href="login.do" class="layui-btn layui-btn-primary">去登陆</a>
	    </div>
	  </div>
  </div>
</form>
<div id="divCode" style="display:none;">
	<div class="layui-form-item" style="padding-left:13px; padding-top:16px;">
	    <div class="layui-input-inline">
	    	<input type="text" name="imgCode" placeholder="请输入右侧验证码" class="layui-input" id="thisIdIsimgCode" />
	    </div>
	    <div class="layui-form-mid layui-word-aux" style="padding-top: 3px;padding-bottom: 0px;">
	    	<img id="thisIdIsimgCodeImage" src="captcha.do" onclick="reloadCode();" style="height: 29px;width: 110px; cursor: pointer;" />
	    </div>
	</div>
</div>
<!--[if IE]>
	<div style="position: absolute;bottom: 0px;padding: 10px;text-align: center;width: 100%;background-color: yellow;">建议使用<a href="https://www.baidu.com/s?wd=Chrome" target="_black" style="text-decoration:underline">Chrome(谷歌)</a>、<a href="https://www.baidu.com/s?wd=Firefox" target="_black" style="text-decoration:underline">Firefox(火狐)</a>浏览器，IE浏览器可能无法操作！</div>
<![endif]-->

<script>
//Demo
layui.use('form', function(){
  var form = layui.form;
  
  //监听提交
  form.on('submit(formDemo)', function(data){
	$.showLoading('注册中...');
    var d=$("form").serialize();
	$.post("regSubmit.do", d, function (result) { 
		$.hideLoading();
       	var obj = JSON.parse(result);
       	if(obj.result == '1'){
       		layer.alert('注册成功！', function(index){
				window.location.href='list.do';
			});
       	}else if(obj.result == '0'){
       		layer.msg(obj.info, {shade: 0.3})
       	}else{
       		layer.msg(result, {shade: 0.3})
       	}
	}, "text");
    return false;
  });
});

//重新加载验证码
function reloadCode(){
var code=document.getElementById('imgCodeImage');
//这里必须加入随机数不然地址相同我发重新加载
code.setAttribute('src','captcha.do?'+Math.random());
}

//获取手机验证码
function getPhoneCode(){
	//判断手机号是否是11位
	if(document.getElementById('phone').value.length!=11){
		layer.tips('请输入您的11位手机号', '#phone', {
		  tips: [1, '#3595CC'],
		  time: 3000
		});
		return;
	}

	iw.loading("发送中");
	$.post("sendPhoneRegCodeByAliyun.do?&phone="+document.getElementById('phone').value, function(data){
		iw.loadClose();
		if(data.result == '1'){
			layer.closeAll();
	    	iw.msgSuccess("验证码已发送至您的手机");
	 	}else if(data.result == '0'){
	 		iw.msgFailure(data.info);
	 	}else{
	 		iw.msgFailure();
	 	}
	});
	
	if(true){
		return;
	}
	
}

</script>


<jsp:include page="../../iw/common/foot.jsp"></jsp:include>