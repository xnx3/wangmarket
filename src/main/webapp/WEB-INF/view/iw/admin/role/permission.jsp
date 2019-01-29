<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="编辑资源"/>
</jsp:include>

<form id="form" class="layui-form" action="" style="padding:20px; padding-top:35px; margin-bottom: 10px;">
	<input type="hidden" value="${permission.id }" name="id">
	<input type="hidden" class="form-control" value="${permission.parentId }" name="parentId">
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_columnName">所属上级</label>
		<div class="layui-input-block">
			<input type="text" class="layui-input" value="${parentPermissionDescription }" disabled>
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_columnName">资源名称</label>
		<div class="layui-input-block">
			<input type="text" name="name" required autocomplete="off" placeholder="给这个资源(权限)起个名吧，限8个汉字以内" class="layui-input" value="${permission.name }">
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_columnName">percode</label>
		<div class="layui-input-block">
			<input type="text" name="percode" required autocomplete="off" placeholder="资源代码，可以代表此资源的唯一编码，用于程序中标注此资源作用于哪些方法" class="layui-input" value="${permission.percode }">
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_columnName">URL</label>
		<div class="layui-input-block">
			<input type="text" name="url" autocomplete="off" placeholder="该资源的访问URL，可以用于自定义动态显示相关权限菜单使用" class="layui-input" value="${permission.url }">
		</div>
	</div>
	
	<div class="layui-form-item layui-form-text">
	    <label class="layui-form-label">备注描述</label>
	    <div class="layui-input-block">
	      <textarea name=description placeholder="备注，方便日后快速理解此资源的作用，无实际意义" class="layui-textarea">${permission.description }</textarea>
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
		parent.iw.loading("保存中");
		var d=$("form").serialize();
        $.post("savePermission.do", d, function (result) {
        	parent.iw.loadClose();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.parent.iw.msgSuccess("操作成功");
        		parent.layer.close(index);	//关闭当前窗口
        		parent.location.reload();	//刷新父窗口列表
        	}else if(obj.result == '0'){
        		parent.iw.msgFailure(obj.info);
        	}else{
        		parent.iw.msgFailure(result);
        	}
         }, "text");
		return false;
	});
});
</script>

<jsp:include page="../../common/foot.jsp"></jsp:include>