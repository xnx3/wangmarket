<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="修改邮箱"/>
</jsp:include>

<form id="form" action="saveSite.do" method="post" class="layui-form" style="padding:20px; padding-top:35px;">
  <div class="layui-form-item">
    <label class="layui-form-label">电子邮箱：</label>
    <div class="layui-input-block">
      <input type="text" name="email" id="email" lay-verify="username" autocomplete="off" placeholder="可用于接收信息提醒" class="layui-input" value="${site.username }">
    </div>
  </div>
   
  <div class="layui-form-item">
    <label class="layui-form-label">验证码</label>
    <div class="layui-input-inline">
      <input type="text" name="code" required lay-verify="required" placeholder="右侧获取邮箱验证码" autocomplete="off" class="layui-input">
    </div>
    <div class="layui-form-mid layui-word-aux" style="padding-top: 3px;padding-bottom: 0px; padding-left:5px;">
		<span class="layui-btn layui-btn-sm layui-btn-primary" onclick="getPhoneCode();">获取验证码</span>
	</div>
  </div>
  
  <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn" lay-submit="" lay-filter="demo1">保存修改</button>
      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
    </div>
  </div>
</form>

<script>
try{
	//自适应弹出层大小
	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	parent.layer.iframeAuto(index);
}catch(err){}

layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form;
 
  //自定义验证规则
  form.verify({
    username: function(value){
      if(value.length > 5){
        return '联系人姓名不能超过5个字！';
      }
    },
    phone: function(value){
      if(value.length == 0 || value.length > 14){
        return '请输入14个数字以内的联系电话';
      }
    },
    qq: function(value){
      if(value.length == 0 || value.length > 14){
        return '请输入12个数字或以内的QQ号';
      }
    },
    companyName: function(value){
      if(value.length > 30){
        return '请输入30个字以内的公司名字';
      }
    },
    address: function(value){
      if(value.length > 80){
        return '请输入80个字以内的公司或办公所在地址';
      }
    }
  });
  
  //监听提交
  form.on('submit(demo1)', function(data){
		msg.loading('保存中');
		var d=$("form").serialize();
        $.post("/sites/savePopupSiteUpdate.do", d, function (result) { 
        	msg.close();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.msg.success('保存成功');
        	}else if(obj.result == '0'){
        		parent.msg.failure(obj.info);
        	}else{
        		parent.msg.failure(result);
        	}
        	parent.layer.close(index);
         }, "text");
		
    return false;
  });
  
});


//获取手机验证码
function getEmailCode(){
	//判断邮箱是否准确
	var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); //正则表达式
	var email = document.getElementById("email").value; //要验证的对象
　　if(email.length < 3){ //输入不能为空
		layer.tips('请输入正确的邮箱地址', '#email', {
		  tips: [1, '#3595CC'],
		  time: 3000
		});
　　　　return false;
　　}else if(!reg.test(obj.value)){ //正则验证不通过，格式不对
　　　　layer.tips('请输入正确的邮箱地址', '#email', {
		  tips: [1, '#3595CC'],
		  time: 3000
		});
　　　　return false;
　　}
	//通过，继续执行	
	
	msg.loading("发送中");
	$.post("sendPhoneRegCodeByAliyun.do?&phone="+document.getElementById('phone').value, function(data){
		msg.close();
		if(data.result == '1'){
			layer.closeAll();
	    	msg.success("验证码已发送");
	 	}else if(data.result == '0'){
	 		msg.failure(data.info);
	 	}else{
	 		msg.failure('发送失败');
	 	}
	});
	
	if(true){
		return;
	}
}
</script>

</body>
</html>