<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.DateUtil"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
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
	
	<script src="/<%=Global.CACHE_FILE %>SiteColumn_used.js"></script>
	<script src="/<%=Global.CACHE_FILE %>SiteColumn_type.js"></script>
<body>


<form id="form" method="post" class="layui-form" style="padding:20px; padding-top:35px; margin-bottom: 10px;">
	<input type="hidden" value="${siteColumn.id }" name="id">
	<input type="hidden" value="${site.id }" name="siteid">
	
	<div class="layui-form-item">
		<label class="layui-form-label">栏目名称</label>
		<div class="layui-input-block">
			<input type="text" name="name" lay-verify="name" autocomplete="off" placeholder="限8个汉字以内" class="layui-input" value="${siteColumn.name }">
		</div>
	</div>

	<div class="layui-form-item">
		<label class="layui-form-label" id="columnType">栏目类型</label>
		<div class="layui-input-block">
			<select name="type" id="type" lay-filter="filtertype" onblur="selectTypeChange();" lay-verify="required">
				<script type="text/javascript">writeSelectAllOptionFortype_('${siteColumn['type'] }','请选择');</script>
			</select>
		</div>
	</div>
	
	<!-- layui图片保存不上，待以后查看 -->
	<div class="layui-form-item" style="display:none">
		<label class="layui-form-label">栏目图标</label>
		<div class="layui-input-inline">
			<input type="file" name="iconFile" class="layui-upload-file">
		</div>
		<div class="layui-form-mid layui-word-aux" style="padding:0px; padding-left:15px;">
				<img src="${icon }" style="height:38px;" />
			</div>
	</div>
	
	<div class="layui-form-item" id="xnx3_url">
		<label class="layui-form-label">链接网址</label>
		<div class="layui-input-block">
			<input type="text" name="url" autocomplete="off" placeholder="请输入目标网页链接地址" class="layui-input" value="${siteColumn.url }">
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">是否显示</label>
		<div class="layui-input-block">
			<select name="used" id="used" lay-verify="required">
				<script type="text/javascript">writeSelectAllOptionForused_('${siteColumn.used }','请选择');</script>
			</select>
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
      if(value.length == 0 || value.length > 10){
        return '请输入10个字以内的栏目名称';
      }
    }
  });
  	//当类型发生变动改变
  	form.on('select(filtertype)', function (data) {
		selectTypeChange();
	});
  
  //监听提交
  form.on('submit(demo1)', function(data){
	  msg.loading('栏目保存中');
		var d=$("form").serialize();
        $.post("/column/savePopupColumnGaoJiUpdate.do", d, function (result) {
			msg.close();
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


//当类型改变后，相应的自定义网址也会显示或者隐藏
function selectTypeChange(){
	if(document.getElementById("type").options[4].selected){
		document.getElementById("xnx3_url").style.display="";
	}else{
		document.getElementById("xnx3_url").style.display="none";
	}
}
selectTypeChange();

</script>
</body>
</html>