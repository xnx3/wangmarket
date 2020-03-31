<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="编辑全局变量"/>
</jsp:include>

<form id="form" class="layui-form" action="save.do" method="post" style="padding-top:20px; padding-right:20px;">
	<input type="hidden" name="updateName" value="${siteVar.name }">
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnCode">变量名</label>
		<div class="layui-input-block">
			<input type="text" name="name" lay-verify="name" autocomplete="off" placeholder="请输入变量名" class="layui-input" value="${siteVar.name }">
		</div>
		<div class="layui-form-mid" style="margin-left: 110px;line-height: 14px; color: gray; font-size: 12px; padding-top:0px;">同一个网站中，变量名必须是唯一的,限英文、数字、下划线_<br/>在制作模板时，也就是在模板变量跟模板页面中，可以用 {var.${siteVar.name }} 来调取变量值</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label" id="description_label">备注说明</label>
		<div class="layui-input-block">
			<textarea name="description" lay-verify="description" autocomplete="off" placeholder="限200个字符以内" class="layui-textarea">${siteVar.description }</textarea>
		</div>
		<div class="layui-form-mid" style="margin-left: 110px;line-height: 14px; color: gray; font-size: 12px; padding-top:0px;">只是备注而已，没有什么其他作用。修改的时候看到这个，能直到这是干嘛的</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label" id="keywords_label">变量值</label>
		<div class="layui-input-block">
			<textarea name="value" lay-verify="value" autocomplete="off" class="layui-textarea">${siteVar.value }</textarea>
		</div>
	</div>
  
	  <div class="layui-form-item" style="text-align:center;">
	  	<button class="layui-btn" lay-submit="" lay-filter="demo1">保存</button>
	  </div>
</form>

<script>

layui.use(['element', 'form', 'layedit', 'laydate'], function(){
  var form = layui.form;
  var element = layui.element;
  
  //自定义验证规则
  form.verify({
    name: function(value){
      if(value.length < 1){
      	return '请输入变量名';
      }
		if(/^[a-zA-Z0-9_]*$/g.test(value)){
			//success
		}else{
			return '变量名只限英文、数字、下划线_';
		}
    },
  });
  
  //监听提交
  form.on('submit(demo1)', function(data){
		parent.msg.loading('保存中');
		var d=$("form").serialize();
        $.post("/siteVar/save.do", d, function (result) { 
        	parent.msg.close();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.msg.success("保存成功");
        		parent.location.reload();
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

<jsp:include page="../iw/common/foot.jsp"></jsp:include>