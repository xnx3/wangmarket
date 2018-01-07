<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.DateUtil"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@page import="com.xnx3.selfSite.G"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%><jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="修改二维码"/>
</jsp:include>

  <div class="layui-form-item">
    <div class="layui-input-block">
		<img src="<%=OSSUtil.url %>site/${site.id }/images/qr.jpg?v=<%=DateUtil.timeForUnix10() %>" style="width:119px;" />
		<br/>
		<button type="button" class="layui-btn" id="qrImage">
		  <i class="layui-icon">&#xe67c;</i>上传图片
		</button>
    </div>
    
  </div>

<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

layui.use('upload', function(){
	layui.upload.render({
		elem: '#qrImage', //绑定元素
		url: parent.masterSiteUrl+'site/popupQrImageUpdateSubmit.do',
		exts: 'jpg|gif|png|jpeg|bmp', //那么，就只会支持这三种格式的上传。注意是用|分割。
		field:'qrImageFile',
		before: function(input){
			//返回的参数item，即为当前的input DOM对象
			$.showLoading('图片上传中');
		},
		done: function(res, input){
			$.hideLoading();
			parent.layer.msg('上传成功', {shade: 0.2});
			parent.layer.close(index);
		},
		error: function(res, input){
			$.hideLoading();
			parent.layer.msg('上传失败！', {shade: 0.2});
			parent.layer.close(index);
		}
	});
});
</script>

</body>
</html>