<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="基本信息"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Site_mShowBanner.js"></script>
<script src="/<%=Global.CACHE_FILE %>WeUI_mShowBanner.js"></script>
<script src="/<%=Global.CACHE_FILE %>Site_client.js"></script>
<body>
<!-- author:管雷鸣 -->
<style>
.myForm{
	margin: 0 auto;
	margin-top:25px;
	margin-bottom:25px;
    width: 495px;
    height: auto;
    border-width: 1px 1px 1px 1px;
    padding: 0px;
    border-radius: 20px;
    overflow: hidden;
    -webkit-box-shadow: 0 0 10px #e2e2e2;
    -moz-box-shadow: 0 0 10px #e2e2e2;
    box-shadow: 0 0 10px #e2e2e2;
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

<script>
/*页面上输出选择框的所有option，显示到页面上*/ 
function selectOptionClient(firstTitle){ 
	var content = ""; 
	content = content + '<option value="" selected="selected">'+firstTitle+'</option>'; 
	for(var p in client){ 
		if(p != '3'){ 
			content = content+'<option value="'+p+'">'+client[p]+'</option>'; 
		} 
	} 
	document.write(content); 
}
</script>

<form class="layui-form layui-elem-quote layui-quote-nm myForm" action="/userCreateSite.do">
	<input type="hidden" name=keywords placeholder="关键词" value="">
	<input type="hidden" name=mShowBanner placeholder="是否显示轮播图" value="1">
	
  <div class="layui-form-item" style="    height: 70px;
    background-color: #eeeeee;
    line-height: 70px;
    text-align: center;
    font-size: 25px;
    color: #3F4056;">
    恭喜您，注册成功！
  </div>
  <div style="font-size: 22px;text-align: center; padding-top: 15px;">填入以下信息，免费在线创建网站吧</div>
	<div style="padding: 30px 50px 40px 0px;">
		<div class="layui-form-item">
			<label class="layui-form-label">网站名称</label>
			<div class="layui-input-block">
				<input type="text" name="name" required  lay-verify="required" placeholder="限20个汉字之内，必填" autocomplete="off" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">网站类型</label>
			<div class="layui-input-block">
				<select name="client" id="client" lay-filter="filtertype" lay-verify="required">
					<script type="text/javascript">selectOptionClient('请选择');</script>
				</select>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">联系人</label>
			<div class="layui-input-block">
				<input type="text" name="username" lay-verify="required" placeholder="限5个汉字以内" autocomplete="off" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">联系电话</label>
			<div class="layui-input-block">
				<input type="text" name="phone" lay-verify="required" placeholder="限5个汉字以内" autocomplete="off" class="layui-input" value="${user.phone }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">联系QQ</label>
			<div class="layui-input-block">
				<input type="text" name="qq" lay-verify="required" placeholder="请输入您的QQ号，可不填" autocomplete="off" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">公司名</label>
			<div class="layui-input-block">
				<input type="text" name="companyName" lay-verify="required" placeholder="请输入您的公司或团队名字，限30字以内" autocomplete="off" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">办公地址</label>
			<div class="layui-input-block">
				<input type="text" name="address" lay-verify="required" placeholder="请输入您的办公或常用地址" autocomplete="off" class="layui-input">
			</div>
		</div>
	
		<div class="layui-form-item">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit lay-filter="formDemo">免费创建网站</button>
		</div>
	</div>
  </div>
</form>

<div id="createSuccessExplain" style="display:none;">
	<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;">
		<div style="font-size: 22px;"><i class="layui-icon" style="font-size: 30px;color: #FF5722;">&#xe618;</i>&nbsp;&nbsp;请稍等半分钟</div>
		
		<br><br/>我们所坚持的：
		<br/>以高精尖技术压缩成本，
		<br/>以超低价甚至免费让用户享受高端体验！
		<br/>让云上技术惠及千万家！
		<br><br>当前您的网站已创建成功，云智能机器人正在努力帮你构建中，预计2分钟之内就可以帮您将网站建设好！
		<br/>您只需要上去修改一下文字、将您自己的图片换上就可以了！
	</div>
</div>

<script>
//Demo
layui.use('form', function(){
  var form = layui.form;
  
  //监听提交
  form.on('submit(formDemo)', function(data){
  	var loadVar = layer.msg('网站创建中...', {
	  icon: 16
	  ,shade: 0.01
	  ,time: 0 //不自动关闭
	});
    var d=$("form").serialize();
	$.post("/sites/userCreateSite.do", d, function (result) { 
		layer.close(loadVar);
       	var obj = JSON.parse(result);
       	if(obj.result == '1'){
       		layer.msg('创建成功', {shade: 0.3});
       		
       		var divCode=document.getElementById('createSuccessExplain').innerHTML;
			divCode = divCode.replace(/Domain/, obj.site.domain);
			divCode = divCode.replace(/Domain/, obj.site.domain);
       		layer.open({
		        type: 1
		        ,title: false //不显示标题栏
		        ,closeBtn: false
		        ,area: '320px;'
		        ,shade: 0.8
		        ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
		        ,btn: ['我知道了，进入管理后台']
		        ,moveType: 1 //拖拽模式，0或者1
		        ,content: divCode
		        ,success: function(layero){
		        	//页面打开
		        }
		        ,yes: function(){
		        	//点击底部按钮
		        	window.location.href='/sites/editPcIndex.do?siteid='+obj.site.id;
		        }
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

	var divCode=document.getElementById('divCode').innerHTML;
	divCode = divCode.replace(/thisIdIsimgCode/, "imgCode");
	divCode = divCode.replace(/thisIdIsimgCode/, "imgCode");
	layer.open({
        type: 1
        ,title: false //不显示标题栏
        ,closeBtn: false
        ,area: '340px;'
        ,shade: 0.8
        ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
        ,btn: ['验证并发送短信', '取消']
        ,moveType: 1 //拖拽模式，0或者1
        ,content: divCode
        ,success: function(layero){
        	reloadCode();	//刷新验证码
        }
        ,yes: function(){
        	$.getJSON("sendPhoneRegCodeByAliyun.do?code="+document.getElementById('imgCode').value+"&phone="+document.getElementById('phone').value,function(result){
			    if(result.result){
			    	layer.closeAll();
			    	layer.msg("验证码已发送至您的手机<br/>预计1分钟内就可收到，请将收到的验证码填入");
			    }else{
			    	layer.msg(result.info);
			    	reloadCode();
			    }
			});
        }
      });
}

</script>


</body>
</html>
