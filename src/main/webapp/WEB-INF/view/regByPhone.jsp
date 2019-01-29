<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="iw/common/head.jsp">
	<jsp:param name="title" value="免费开通网站"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Site_client.js"></script>
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

<form class="layui-form layui-elem-quote layui-quote-nm myForm" action="/regSubmit.do">
  <div class="layui-form-item" style="    height: 70px;
    background-color: #eeeeee;
    line-height: 70px;
    text-align: center;
    font-size: 25px;
    color: #3F4056;
    white-space:nowrap;
    ">
    <%=Global.get("SITE_NAME") %> 云建站平台 免费开通网站
  </div>
  <div style="padding: 30px 50px 40px 0px;">
  	<div class="layui-form-item">
		<label class="layui-form-label">网站类型</label>
		<div class="layui-input-block">
			<script type="text/javascript">writeSelectAllOptionForclient_('3','请选择', true);</script>
		</div>
		<div id="help_client" class="layui-form-mid layui-word-aux" style="cursor: pointer;float: right;margin-top: -38px;margin-right: -28px;"><i class="layui-icon" style="font-size:18px;">&#xe607;</i></div>
	</div>
  
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
	  <div class="layui-form-item">
	    <label class="layui-form-label">手机号</label>
	    <div class="layui-input-block">
	      <input type="text" name="phone" id="phone" required  lay-verify="required" placeholder="请输入您的手机号" autocomplete="off" class="layui-input">
	    </div>
	  </div>
	  <div class="layui-form-item">
	    <label class="layui-form-label">验证码</label>
	    <div class="layui-input-inline">
	      <input type="text" name="code" required lay-verify="required" placeholder="右侧获取手机验证码" autocomplete="off" class="layui-input">
	    </div>
	    <div class="layui-form-mid layui-word-aux" style="padding-top: 3px;padding-bottom: 0px; padding-left:5px;">
			<span class="layui-btn layui-btn-sm layui-btn-primary" onclick="getPhoneCode();">获取验证码</span>
		</div>
	  </div>
	  <div class="layui-form-item" style="display:none">
	    <label class="layui-form-label">记住密码</label>
	    <div class="layui-input-block">
	      <input type="checkbox" name="switch" lay-skin="switch">
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
	$.showLoading('开通中...');
    var d=$("form").serialize();
	$.post("/userCreateSite.do", d, function (result) { 
		$.hideLoading();
       	var obj = JSON.parse(result);
       	if(obj.result == '1'){
       		layer.alert('创建成功！', function(index){
				window.location.href='login.do';
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

//鼠标跟随提示
$(function(){
	//网站类型
	var help_client_text = '若看不懂，选择“CMS”即可'+
					'<br/><b>1.&nbsp;电脑端</b>&nbsp;最适合做SEO优化，但网站模版稍显单一。不过此种类型的网站有几套模版可以在手机或电脑中都可使用，且支持随意切换模版。推荐做SEO的网站首选。'+
					'<br/><b>2.&nbsp;手机端</b>&nbsp;专为手机设计的网站，仅有手机页面，但手机体验也是最好的。支持随意切换的手机模版。'+
					'<br/><b>3.&nbsp;CMS</b>&nbsp;全能模式！且百分百可自定义！类似于现有的织梦CMS、帝国CMS，懂HTML可以进行定制开发自己的网站，也可以根据导入模版一键创建出成品网站，只需要修改一下栏目名字、文字及图片就可达到使用级别！但是一旦导入模版使用后，就不可以再导入其他模版。此种类型因自由度极高，被广大建站爱好者青睐，其可以根据自己的意愿作出任意形式的网站。建议稍微懂点建站的，都可以试试这种模式！';
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

//右侧弹出提示
function rightTip(){
	layer.open({
	  title: '临时弹窗之问题反馈',offset: 'rb', shadeClose:true, shade:0
	  ,content: '您有什么不懂的，或者自助开通时，遇到什么问题导致您无法操作、不知如何进行，或者无法开通，任何问题都可反馈给我们。也或者直接关注我们微信公众号"wangmarket"进行在线沟通咨询。<br/>我的QQ:921153866<br/>微信：xnx3com'
	});
}

//未授权用户，请尊重作者劳动成果，保留我方版权标示及链接！授权参见：http://www.wang.market/price.html
<% if(G.copyright){ %>
	setTimeout("rightTip()",1000);
<% } %>
</script>


<jsp:include page="iw/common/foot.jsp"></jsp:include>