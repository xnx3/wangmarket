<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../../iw/common/head.jsp">
	<jsp:param name="title" value="用户列表"/>
</jsp:include>

<script type="text/javascript">
	
</script>

<form id="form" class="layui-form" onsubmit="javascript:;" style="padding:20px; padding-top:35px; margin-bottom: 10px;">
	<div class="layui-form-item">
		<label class="layui-form-label">插件id</label>
		<div class="layui-input-block">
			<c:choose>
				<c:when test="${plugin != null }">
					<input type="text"  readonly="readonly" name="id" id="pluginId"  class="layui-input" value="${plugin.id }">
				</c:when>
				<c:otherwise>
					<input type="text" name="id" id="pluginId"  class="layui-input" value="${plugin.id }">
				</c:otherwise>
			</c:choose>
			
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">插件名称</label>
		<div class="layui-input-block">
			<input type="text" name="menuTitle" id="menuTitle" class="layui-input" value="${plugin.menuTitle }">
		</div>
	</div>
	
	<div class="layui-form-item">
	    <div class="layui-input-block">
			<button class="layui-btn" lay-submit lay-filter = "formSubmit">立即保存</button>
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
		//表单信息验证d
		//如果是删除操作的话不需要验证消息
		
		//检查进行的操作是不是删除操作，删除的话没必要进行信息校验，直接修改学校删除状态即可
		if($("#pluginId").val() == ''){
			iw.msgFailure("请输入插件id");
			return false;
		}
		if($("#menuTitle").val() == ''){
			iw.msgFailure("请输入插件名称");
			return false;
		}
		//表单序列化
		var d = $("#form").serialize();
		parent.iw.loading("保存中");
        $.post("/plugin/pluginManage/addSubmit.do", d, function (result) {
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
<jsp:include page="../../../iw/common/foot.jsp"></jsp:include>