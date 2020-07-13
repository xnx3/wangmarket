<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.DateUtil"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@page import="com.xnx3.wangmarket.admin.G"%><html>
<head>
	<meta charset="utf-8">
	<title>修改栏目</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
	<script src="${STATIC_RESOURCE_PATH}js/jquery-2.1.4.js"></script>
	<!-- 模版的加载都是动态加载，在下面js里 -->
	<script src="${STATIC_RESOURCE_PATH}module/layui/layui.js" type="text/javascript"></script>
	<link href="${STATIC_RESOURCE_PATH}module/layui/css/layui.css" rel="stylesheet" type="text/css">	
	
	<script src="${STATIC_RESOURCE_PATH}js/jquery-weui.js" type="text/javascript"></script>
	<link href="${STATIC_RESOURCE_PATH}css/weui.min.css" rel="stylesheet" type="text/css">	
	<link href="${STATIC_RESOURCE_PATH}css/jquery-weui.css" rel="stylesheet" type="text/css">	
<body>


<form id="form" method="post" class="layui-form" style="padding:20px; padding-top:35px;">
	<input type="hidden" name="id" value="${siteColumn.id }" />
	
	<div class="layui-form-item">
		<label class="layui-form-label">栏目名称</label>
		<div class="layui-input-block">
			<input type="text" name="name" lay-verify="name" autocomplete="off" placeholder="限8个汉字以内" class="layui-input" value="${siteColumn.name }">
		</div>
	</div>

	<div class="layui-form-item" style="padding-top:15px;">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit="" lay-filter="demo1">保存修改</button>
			<button type="reset" class="layui-btn layui-btn-primary">重置</button>
		</div>
	</div>
</form>

<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form();
 
  //自定义验证规则
  form.verify({
    name: function(value){
      if(value.length == 0 || value.length > 20){
        return '请输入20个字以内的栏目名称';
      }
    }
  });
  
  //监听提交
  form.on('submit(demo1)', function(data){
  	  parent.msg.loading('保存中');
		var d=$("form").serialize();
        $.post("/column/popupColumnUpdateSubmit.do", d, function (result) { 
        	parent.msg.close();
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
</script>

</body>
</html>