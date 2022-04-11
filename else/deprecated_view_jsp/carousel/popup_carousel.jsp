<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.DateUtil"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<html>
<head>
	<meta charset="utf-8">
	<title>修改Banner</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
	<script src="${STATIC_RESOURCE_PATH}js/jquery-2.1.4.js"></script>
	<!-- 模版的加载都是动态加载，在下面js里 -->
	<script src="//res.weiunity.com/layui/layui.js" type="text/javascript"></script>
	<link href="//res.weiunity.com/layui/css/layui.css" rel="stylesheet" type="text/css">	
	
	<script src="//res.weiunity.com/js/jquery-weui.js" type="text/javascript"></script>
	<link href="//res.weiunity.com/css/weui.min.css" rel="stylesheet" type="text/css">	
	<link href="//res.weiunity.com/css/jquery-weui.css" rel="stylesheet" type="text/css">	
<body>


<form id="form" target="layui-upload-iframe" key="set-mine" method="post" enctype="multipart/form-data" class="layui-form" style="padding:30px; padding-top:50px;">
	<div style="text-align: center;">
		<div><img src="${image }" style="width:219px;" /></div>
		<div>&nbsp;</div>
		<div><input type="file" name="imageFile" class="layui-upload-file"></form></div>
	</div>

</form>

<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

layui.use('upload', function(){
	layui.upload({
		url: '/carousel/popupCarouselUpdateSubmit.do?id=${carousel.id }',
		title:'点击此处上传Banner图',
		ext: 'jpg|png|gif', //那么，就只会支持这三种格式的上传。注意是用|分割。
		before: function(input){
			//返回的参数item，即为当前的input DOM对象
			msg.loading('图片上传中');
		},
		success: function(res, input){
			msg.close();
			parent.layer.msg('上传成功', {shade: 0.2});
			parent.layer.close(index);
		}
	});
});

</script>

</body>
</html>