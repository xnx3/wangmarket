<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../../../../iw/common/head.jsp">
	<jsp:param name="title" value="编辑分类板块"/>
</jsp:include>

<form class="layui-form" method="post" style="padding-top:35px; margin-bottom: 10px; padding-right:35px;">
	<input type="hidden" value="${postClass.id }" name="id" />
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_columnName">板块名称</label>
		<div class="layui-input-block">
			<input type="text" name="name"  required lay-verify="required" autocomplete="off" placeholder="起个板块分类的名字吧" class="layui-input" value="${postClass.name }">
		</div>
	</div>
    
   	<div class="layui-form-item" style="padding-top:15px;">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit="" lay-filter="demo1">保存</button>
		</div>
	</div>
</form>
<div>&nbsp;</div>

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
        $.post("saveClass.do", d, function (result) { 
        	parent.iw.loadClose();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.iw.msgSuccess('保存成功');
        		parent.layer.close(index);
        		parent.location.reload();	//刷新父窗口
        	}else if(obj.result == '0'){
        		iw.msgFailure(data.info)
        	}else{
        		iw.msgFailure('500');
        	}
         }, "text");
		
    return false;
  });
  
});  
</script>

<jsp:include page="../../../../iw/common/foot.jsp"></jsp:include> 