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
	<div class="layui-form-item">
		<label class="layui-form-label">网站类型</label>
		<div class="layui-input-inline xnx3_input">
			<script type="text/javascript">writeSelectAllOptionForclient_('','请选择', true);</script>
		</div>
		<div id="help_client" class="layui-form-mid layui-word-aux" style="cursor: pointer;"><i class="layui-icon" style="font-size:18px;">&#xe607;</i></div>
	</div>
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
			<input type="text" name="username" placeholder="限20个英文或汉字，开通网站后，用户用此账号登陆wang.market，进入网站管理后台" required  lay-verify="required" autocomplete="off" class="layui-input">
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
//当网站类型发生改变
function selectClientChange(){
	if(document.getElementById("client").options[1].selected){
		//client＝pc
		document.getElementById("pc_autoCreateColumn").style.display="";
		$(".elseZL").css("display","");
		
		document.getElementById("wap_autoCreateColumn").style.display="none";
	}else if(document.getElementById("client").options[3].selected){
		//cms
		document.getElementById("wap_autoCreateColumn").style.display="none";
		document.getElementById("pc_autoCreateColumn").style.display="none";
		$(".elseZL").css("display","none");
	}else{
		//client=wap
		$(".elseZL").css("display","");
		document.getElementById("wap_autoCreateColumn").style.display="";
		
		document.getElementById("email_id").style.display="none";
		document.getElementById("address_id").style.display="none";
		document.getElementById("companyName_id").style.display="none";
		document.getElementById("contactUsername_id").style.display="none";
		document.getElementById("pc_autoCreateColumn").style.display="none";
	}
}

//form组件，开启select
layui.use(['form'], function(){

	var form = layui.form;
	//当网站类型发生变动改变
	form.on('select(client)', function (data) {
		selectClientChange();
	});

	//监听提交
	form.on('submit(formDemo)', function(data){
		$.showLoading('开通中...');
		var d=$("form").serialize();
        $.post("addSubmit.do", d, function (result) { 
        	$.hideLoading();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		$.toast("开通成功", function() {
					window.location.href="userList.do?orderBy=id_DESC"; 
				});
        	}else if(obj.result == '0'){
        		layer.msg(obj.info, {shade: 0.3})
        	}else{
        		layer.msg(result, {shade: 0.3})
        	}
         }, "text");
		
		return false;
	});
	
	//如果进入时，类型没有选则，那么默认选择CMS模式
	if(document.getElementById("client").options[0].selected){
		document.getElementById('client')[3].selected = true;
		layui.form.render();
		selectClientChange();
	}
});


//鼠标跟随提示
$(function(){
	//网站类型
	var help_client_text = '要创建何种类型的网站。创建后不可更改！'+
					'<br/><b>1.&nbsp;CMS</b>&nbsp;（推荐使用此种）全能模式！且百分百可自定义！类似于现有的织梦CMS、帝国CMS，懂HTML可以进行定制开发自己的网站，也可以根据导入模版一键创建出成品网站，只需要修改一下栏目名字、文字及图片就可达到使用级别！但是一旦导入模版使用后，就不可以再导入其他模版。此种类型因自由度极高，被广大建站爱好者青睐，其可以根据自己的意愿作出任意形式的网站。建议稍微懂点建站的，都可以试试这种模式！'+
					'<br/><b>2.&nbsp;电脑端</b>&nbsp; 适合不太懂互联网的小白用户。最适合做SEO优化，但网站模版稍显单一。不过此种类型的网站有几套模版可以在手机或电脑中都可使用，且支持随意切换模版。推荐做SEO的网站首选。如果将网站作为一个赠送品送人，此种模式的网站是不错的选择，开通即用，没有使用难度'+
					'<br/><b>3.&nbsp;手机端</b>&nbsp; 适合不太懂互联网的小白用户。专为手机设计的网站，仅有手机页面，但手机体验也是最好的。支持随意切换的手机模版。如果将网站作为一个赠送品送人，此种模式的网站是不错的选择，开通即用，没有使用难度';
					
	var help_client_index = 0;
	$("#help_client").hover(function(){
		help_client_index = layer.tips(help_client_text, '#help_client', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(help_client_index);
	})
	
});
</script>