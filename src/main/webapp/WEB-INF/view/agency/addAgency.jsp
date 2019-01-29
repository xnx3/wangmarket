<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@page import="com.xnx3.j2ee.shiro.ShiroFunc"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="开通下级代理"/>
</jsp:include>

<form class="layui-form" action="" style="padding-top:3%; padding-left:15%; padding-right:15%; padding-bottom: 3%;">
	<input type="hidden" value="<%=ShiroFunc.getUser().getId() %>" name="inviteid" />

	<div class="layui-form-item">
		<label class="layui-form-label">联系人姓名</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="contactUsername" placeholder="请输入联系人的姓名" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">联系人手机</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="phone" placeholder="可以填写手机、座机、400电话等，限14个字符以内" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">联系人QQ</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="qq" placeholder="对方的QQ号(可不填)" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">公司名称</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="companyName" placeholder="限30个字以内，可填写公司名、个体工商户名，若都没可填写个人名字" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">公司地址</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="address" placeholder="限60个字以内，公司或者办公地点、工作的地址" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">电子邮箱</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="email" placeholder="对方的电子邮箱（可不填）" autocomplete="off" class="layui-input">
		</div>
	</div>
	
	<hr/>
	<div class="layui-form-item">
		<label class="layui-form-label">登陆账号</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="username" placeholder="限20个英文或汉字，开通代理后，用户用此账号登陆wang.market管理" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">登陆密码</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="password" placeholder="限20个英文或汉字，用户进入代理后台登陆的密码" autocomplete="off" class="layui-input">
		</div>
	</div>
	
	<div class="layui-form-item">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit lay-filter="formDemo">确认开通</button>
			<button type="reset" class="layui-btn layui-btn-primary">重置</button>
		</div>
	</div>
</form>

<script type="text/javascript">
//form组件，开启select
layui.use(['form'], function(){
	var form = layui.form;
	
	//监听提交
	form.on('submit(formDemo)', function(data){
		$.showLoading('开通中...');
		var d=$("form").serialize();
        $.post("addAgencySave.do", d, function (result) { 
        	$.hideLoading();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		$.toast("开通成功", function() {
					window.location.href="subAgencyList.do?orderBy=addtime_DESC"; 
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
</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>