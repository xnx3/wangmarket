<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="/wm/common/head.jsp">
	<jsp:param name="title" value="设置系统参数"/>
</jsp:include>

<style>
.layui-form-label{
	width:180px;
}
.layui-input-block{
	margin-left: 210px;
}
</style>

<div style="padding:30px; padding-bottom:10px; text-align:center; font-size:20px;">
	域名设置
</div>
<form class="layui-form" style="padding-top:35px; margin-bottom: 10px; padding-right:35px;">
	
	<div class="layui-form-item">
		<div class="layui-input-block">
			<div style="color:#d2d2d2; padding-top:5px;">
				本系统需要独占一个域名！ 
				比如你准备的域名为 leimingyun.com 
				
				第一步：你需要fan jie
			</div>
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">访问域名</label>
		<div class="layui-input-block">
			<input type="text" name="MASTER_SITE_URL" placeholder="格式如 http://wang.market/" autocomplete="off"  class="layui-input" value="${fangwenyuming }">
			<div style="color:#d2d2d2; padding-top:5px;">
				设置当前建站系统的域名。如建站系统的登录地址为 http://wang.market/login.do ，那么就将 http://wang.market/  填写到此处。
				<br/>注意，前面的 &nbsp; http:// &nbsp;以及最后的&nbsp; / &nbsp;不要拉下
				<div style="color: #a2a2a2; font-weight: bold;">注意：若不是很懂此处，使用默认自动填写的即可</div>
			</div>
		</div>
	</div>
	
	<br/>
	
	<div class="layui-form-item">
		<label class="layui-form-label">附件域名</label>
		<div class="layui-input-block">
			<input type="text" name="ATTACHMENT_FILE_URL" placeholder="格式如 http://wang.market/" autocomplete="off"  class="layui-input" value="${fujianyuming}">
			<div style="color:#d2d2d2; padding-top:5px;">
				设置当前建站系统中，上传的图片、附件的访问域名。若后续想要将附件转到云上存储、或开通CDN加速，可平滑上云使用。
				<br/>
				如，此处您填写为&nbsp; http://cdn.weiunity.com/ &nbsp;那么您在站点里面上传了一张图片，则图片的访问地址为&nbsp; http://cdn.weiunity.com/site/123/news/tupianmingming.jpg &nbsp;
				<br/>注意，前面的 &nbsp; http:// &nbsp;以及最后的&nbsp; / &nbsp;不要拉下
				<div style="color: #a2a2a2; font-weight: bold;">注意：若不是很懂此处，填写的跟上面的 “访问域名” 相同即可</div>
			</div>
		</div>
	</div>
	<br/>
	
	<div class="layui-form-item">
		<label class="layui-form-label">泛解析域名</label>
		<div class="layui-input-block">
			<input type="text" name="AUTO_ASSIGN_DOMAIN" autocomplete="off"  class="layui-input" value="<%=Global.get("AUTO_ASSIGN_DOMAIN") %>" placeholder="格式如 wscso.com">
			<div style="color:#d2d2d2; padding-top:5px;">
				如果您只是想快速体验本程序，此项可忽略
			</div>
			<div style="color:#d2d2d2; padding-top:5px;">
				每当创建网站，会自动分配给网站一个二级域名。这里便是自动分配出二级域名的，要泛解析的主域名。同时，您需要将此域名进行泛解析到此IP（如果您只是再本地进行体验，可以将要泛解析的主域名解析到127.0.0.1）
				<br/>
				如，您想创建网站后，自动分配一个二级域名如 1234.wang.market &nbsp; ，那么你需要将 wang.market 进行泛解析，指向当前服务器的ip
			</div>
		</div>
	</div>
	
	<br/>
	
	<div class="layui-form-item">
		<label class="layui-form-label">管理员邮箱</label>
		<div class="layui-input-block">
			<input type="text" name="SERVICE_MAIL" autocomplete="off"  class="layui-input" value="<%=Global.get("SERVICE_MAIL") %>">
			<div style="color:#d2d2d2; padding-top:5px;">
				当系统出现什么问题，或者有什么提醒时，会自动向此处的管理员邮箱发送提示信息通知
			</div>
		</div>
	</div>
    
   	<div class="layui-form-item" style="padding-top:15px;">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit="" lay-filter="demo1">保存设置</button>
		</div>
	</div>
</form>

<script>
layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form;
  
  //监听提交
  form.on('submit(demo1)', function(data){
	  msg.loading('保存中...');
		var d=$("form").serialize();
        $.post("/install/systemSetSave.do", d, function (result) {
			msg.close();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
				msg.success();
        		if('<%=Global.get("ATTACHMENT_FILE_MODE") %>' == 'aliyunOSS'){
        			//使用的阿里云oss进行附件存储，那么需要配置阿里云相关参数
        			window.location.href='accessKey.do'; 
        		}else{
        			//本地服务器存储。那么到此完成配置
        			window.location.href='installSuccess.do'; 
        		}
        	}else if(obj.result == '0'){
        		layer.alert(obj.info);
        	}else{
        		layer.msg(result, {shade: 0.3})
        	}
         }, "text");
		
    return false;
  });
  
});  

</script>

<style>
.two{
	background-color: #5fb87852;
    padding: 2px;
    padding-left: 10px;
    border-radius: 5px;
}
</style>
<jsp:include page="common.jsp"></jsp:include>

<jsp:include page="/wm/common/foot.jsp"></jsp:include> 