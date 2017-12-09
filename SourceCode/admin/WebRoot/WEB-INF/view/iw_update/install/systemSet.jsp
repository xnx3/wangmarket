<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../../iw/common/head.jsp">
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


<form class="layui-form" style="padding-top:35px; margin-bottom: 10px; padding-right:35px;">
	<div class="layui-form-item">
		<label class="layui-form-label">泛解析域名</label>
		<div class="layui-input-block">
			<input type="text" name="AUTO_ASSIGN_DOMAIN" autocomplete="off"  class="layui-input" value="<%=Global.get("AUTO_ASSIGN_DOMAIN") %>">
			<div style="color:#d2d2d2; padding-top:5px;">
				每当创建网站，会自动分配给网站一个二级域名。这里便是自动分配出二级域名的，要泛解析的主域名。同时，您需要将此域名进行泛解析到此IP
			</div>
		</div>
	</div>
	
	<br/>
	
	<div class="layui-form-item">
		<label class="layui-form-label">您的邮箱</label>
		<div class="layui-input-block">
			<input type="text" name="SERVICE_MAIL" autocomplete="off"  class="layui-input" value="<%=Global.get("SERVICE_MAIL") %>">
			<div style="color:#d2d2d2; padding-top:5px;">
				当系统出现什么问题，或者有什么提醒时，会自动向此处的管理员邮箱发送提示信息通知
			</div>
		</div>
	</div>
    
   	<div class="layui-form-item" style="padding-top:15px;">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit="" lay-filter="demo1">保存</button>
		</div>
	</div>
</form>

<style>
.two{background-color: #5FB878;}
</style>
<jsp:include page="common.jsp"></jsp:include>

<script>
layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form;
  
  //监听提交
  form.on('submit(demo1)', function(data){
	iw.loading('保存中...');
		var d=$("form").serialize();
        $.post("<%=basePath %>install/systemSetSave.do", d, function (result) { 
        	iw.loadClose();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		iw.msgSuccess();
        		window.location.href='installSuccess.do'; 
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

<jsp:include page="../../iw/common/foot.jsp"></jsp:include> 