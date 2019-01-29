<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="编辑系统参数"/>
</jsp:include>

<form class="layui-form" style="padding-top:35px; margin-bottom: 10px; padding-right:35px;">
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_columnName">变量名</label>
		<div class="layui-input-block">
			<input type="text" name="name"  required lay-verify="required" autocomplete="off" placeholder="请输入英文或数字" class="layui-input" value="${system.name }">
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_columnName">值</label>
		<div class="layui-input-block">
			<input type="text" name="value"  required lay-verify="required" autocomplete="off" placeholder="限1000字符以内" class="layui-input" value="${system.value }">
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_columnName">描述</label>
		<div class="layui-input-block">
			<textarea name="description" placeholder="只是起个备注作用，以便自己认识。无其他意义。这里支持HTML标签" class="layui-textarea">${system.description }</textarea>
		</div>
	</div>
    
   	<div class="layui-form-item" style="padding-top:15px;">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit="" lay-filter="demo1">保存修改</button>
		</div>
	</div>
</form>

<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form;
  
  //监听提交
  form.on('submit(demo1)', function(data){
  	  parent.iw.loading("保存中");
		var d=$("form").serialize();
        $.post("/admin/system/variableSave.do", d, function (result) { 
        	parent.iw.loadClose();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.parent.iw.msgSuccess("操作成功")
        		parent.layer.close(index);
        		parent.location.reload();	//刷新父窗口
        	}else if(obj.result == '0'){
        		parent.layer.msg(obj.info, {shade: 0.3})
        	}else{
        		parent.layer.msg(result, {shade: 0.3})
        	}
         }, "text");
		
    return false;
  });
  
});  
</script>

<jsp:include page="../../common/foot.jsp"></jsp:include> 