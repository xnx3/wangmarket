<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.DateUtil"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="修改关于我们的图片"/>
</jsp:include>

	<div style="text-align: center;">
		<div style="padding:8px;">建议大小：242*150</div>
		<div><img src="${news.titlepic }" style="width:219px;" /></div>
		<div>&nbsp;</div>
		<button type="button" class="layui-btn" id="titlePicFile">
		  <i class="layui-icon">&#xe67c;</i>上传图片
		</button>
	</div>

<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

layui.use('upload', function(){
	var upload = layui.upload;
	upload.render({
		elem: '#titlePicFile', //绑定元素
		url: '/sites/popupAboutUsImageUpdateSubmit.do',
		field:'titlePicFile',
		exts: 'jpg|gif|png|jpeg|bmp', //那么，就只会支持这三种格式的上传。注意是用|分割。
		before: function(input){
			//返回的参数item，即为当前的input DOM对象
			msg.loading('图片上传中');
		},
		done: function(res, input){
			msg.close();
			parent.layer.msg('上传成功', {shade: 0.2});
			parent.location.reload();
		},
		error: function(res, input){
			msg.close();
			parent.layer.msg('上传失败！', {shade: 0.2});
			parent.layer.close(index);
		}
	});
});

</script>

</body>
</html>