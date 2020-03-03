<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="修改网站基本信息"/>
</jsp:include>

<form id="form" action="saveSite.do" method="post" class="layui-form" style="padding:20px; padding-top:35px;">
  <div class="layui-form-item">
    <label class="layui-form-label">联系人姓名</label>
    <div class="layui-input-block">
      <input type="text" name="username" lay-verify="username" autocomplete="off" placeholder="限5个汉字以内" class="layui-input" value="${site.username }">
    </div>
  </div>
    <div class="layui-form-item">
    <label class="layui-form-label">联系电话</label>
    <div class="layui-input-block">
      <input type="text" name="phone" lay-verify="phone" autocomplete="off" placeholder="限14个汉字以内" class="layui-input" value="${site.phone }">
    </div>
  </div>
    <div class="layui-form-item">
    <label class="layui-form-label">联系QQ</label>
    <div class="layui-input-block">
      <input type="text" name="qq" lay-verify="qq" autocomplete="off" placeholder="请输入您的QQ号，可不填" class="layui-input" value="${site.qq }">
    </div>
  </div>
    <div class="layui-form-item">
    <label class="layui-form-label">公司名</label>
    <div class="layui-input-block">
      <input type="text" name="companyName" lay-verify="companyName" autocomplete="off" placeholder="请输入您的公司或团队名字，限30字以内" class="layui-input" value="${site.companyName }">
    </div>
  </div>
    <div class="layui-form-item">
    <label class="layui-form-label">办公地址</label>
    <div class="layui-input-block">
    	<textarea name="address" required lay-verify="address" placeholder="请输入您的公司或团队名字" class="layui-textarea">${site.address }</textarea>
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
      if(value.length > 14){
        return '请输入14个字符以内的联系电话';
      }
    },
    qq: function(value){
      if(value.length > 14){
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
        		parent.msg.success(obj.info);
        	}else{
        		parent.msg.success(result);
        	}
        	parent.layer.close(index);
         }, "text");
    return false;
  });
  
});
</script>

</body>
</html>