<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="编辑角色属性"/>
</jsp:include>


<form id="form" class="layui-form" action="" style="padding:20px; padding-top:35px; margin-bottom: 10px;">
	<input type="hidden" value="${role.id }" name="id">
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_columnName">角色名称</label>
		<div class="layui-input-block">
			<input type="text" name="name" required autocomplete="off" placeholder="15个汉字以内" class="layui-input" value="${role.name }">
		</div>
	</div>
	
	<div class="layui-form-item layui-form-text">
	    <label class="layui-form-label">角色描述</label>
	    <div class="layui-input-block">
	      <textarea name=description placeholder="备注描述，方便日后快速理解此角色的作用，无实际意义" class="layui-textarea">${role.description }</textarea>
	    </div>
	</div>
	
	<div class="layui-form-item">
	    <div class="layui-input-block">
			<button class="layui-btn" lay-submit lay-filter="formSubmit">立即保存</button>
			<button type="reset" class="layui-btn layui-btn-primary">重置</button>
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
	form.on('submit(formSubmit)', function(data){
		$.showLoading('保存中...');
		var d=$("form").serialize();
        $.post("saveRole.do", d, function (result) {
        	$.hideLoading();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.layer.msg('操作成功', {shade: 0.3});
        		parent.location.reload();	//刷新父窗口
        		parent.layer.close(index);
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