<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@page import="com.xnx3.j2ee.shiro.ShiroFunc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<script src="/<%=Global.CACHE_FILE %>Site_client.js"></script>
<style>
.xnx3_input{
}
</style>
<form class="layui-form" action="" style="padding-top:3%; padding-left:15%; padding-right:15%; padding-bottom: 3%;">
	<input type="hidden" value="<%=ShiroFunc.getUser().getId() %>" name="inviteid" />
	<input type="hidden" value="3" name="client" />
	
	<div class="layui-form-item"  id="pc_autoCreateColumn" style="display:nonee;">
		<label class="layui-form-label">自动创建</label>
		<div class="layui-input-block">
			<input type="checkbox" name="like[write]" title="关于我们" checked disabled>
			<input type="checkbox" name="like[read]" title="新闻咨询" checked disabled>
			<input type="checkbox" name="like[dai]" title="产品展示" checked disabled>
			<input type="checkbox" name="like[dai]" title="联系我们" checked disabled>
    	</div>
	</div>
	<div class="layui-form-item"  id="wap_autoCreateColumn" style="display:nonee;">
		<label class="layui-form-label">自动创建</label>
		<div class="layui-input-block">
			<input type="checkbox" name="like[write]" title="关于我们" checked disabled>
    	</div>
	</div>
	
	
	<div class="layui-form-item">
		<label class="layui-form-label">网站名称</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="siteName" required  lay-verify="required" placeholder="请输入网站名" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item elseZL" id="contactUsername_id">
		<label class="layui-form-label">联系人姓名</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="contactUsername" placeholder="负责管理网站的人，或者该网站所属的老板的姓名" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item elseZL">
		<label class="layui-form-label">联系人手机</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="sitePhone" placeholder="请填写负责管理网站的人的手机" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item elseZL">
		<label class="layui-form-label">联系人QQ</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="siteQQ" placeholder="对方的QQ号(可不填)" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item elseZL" id="companyName_id">
		<label class="layui-form-label">公司名称</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="companyName" placeholder="限20个字以内，可填写公司名、个体工商户名，若都没可填写个人名字" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item elseZL" id="address_id">
		<label class="layui-form-label">公司地址</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="address" placeholder="限60个字以内，公司或者办公地点、工作的地址" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item elseZL" id="email_id">
		<label class="layui-form-label">电子邮箱</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="email" placeholder="对方的电子邮箱（可不填）" autocomplete="off" class="layui-input">
		</div>
	</div>
	
	<hr/>
	<div class="layui-form-item">
		<label class="layui-form-label">登陆账号</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="username" placeholder="限20个英文或汉字，开通网站后，用户用此账号登陆" required  lay-verify="required" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">登陆密码</label>
		<div class="layui-input-block xnx3_input">
			<input type="text" name="password" placeholder="限20个英文或汉字，开通网站的用户登录网站管理后台所使用的密码" required  lay-verify="required" autocomplete="off" class="layui-input">
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
document.getElementById("wap_autoCreateColumn").style.display="none";
document.getElementById("pc_autoCreateColumn").style.display="none";
$(".elseZL").css("display","none");

//form组件，开启select
layui.use(['form'], function(){

	var form = layui.form;
	//当网站类型发生变动改变
	form.on('select(client)', function (data) {
		selectClientChange();
	});

	//监听提交
	form.on('submit(formDemo)', function(data){
		parent.msg.loading('开通中');
		var d=$("form").serialize();
        $.post("addSubmit.do", d, function (result) { 
        	parent.msg.close();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.msg.success('开通成功');
				window.location.href="userList.do?orderBy=id_DESC"; 
        	}else if(obj.result == '0'){
        		parent.msg.failure(obj.info);
        	}else{
        		parent.msg.failure(result);
        	}
         }, "text");
		
		return false;
	});
});

</script>