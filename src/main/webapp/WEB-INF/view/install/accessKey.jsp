<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="/wm/common/head.jsp">
	<jsp:param name="title" value="设置阿里云AccessKey参数"/>
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
	第三步：设置阿里云的Access Key参数，以使用云功能模块
	<br/>
	Access Key ID、Secret，可从此网址获取 <a href="https://ak-console.aliyun.com" target="_black">https://ak-console.aliyun.com</a> 
	| <a href="https://promotion.aliyun.com/ntms/act/ambassador/sharetouser.html?userCode=hlr7sxuh&utm_source=hlr7sxuh" target="_black">领优惠卷</a>
</div>
<form class="layui-form" style="padding-top:35px; margin-bottom: 10px; padding-right:35px;">
	<div class="layui-form-item">
		<label class="layui-form-label">Access Key ID</label>
		<div class="layui-input-block">
			<input type="text" name="id"  required lay-verify="required" autocomplete="off"  class="layui-input" value="">
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">Access Key Secret</label>
		<div class="layui-input-block">
			<input type="text" name="secret"  required lay-verify="required" autocomplete="off" class="layui-input" value="">
		</div>
	</div>
    
   	<div class="layui-form-item" style="padding-top:15px;">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit="" lay-filter="demo1">保存</button>
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
        $.post("/install/accessKeySave.do", d, function (result) {
			msg.close();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
				msg.success();
        		window.location.href='systemSet.do'; 
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
.three{background-color: #5FB878;}
</style>
<jsp:include page="common.jsp"></jsp:include>


<jsp:include page="/wm/common/foot.jsp"></jsp:include> 