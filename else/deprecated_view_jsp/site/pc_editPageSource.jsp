<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="修改页面"/>
</jsp:include>

<form id="form" class="layui-form layui-form-pane" action="savePageSource.do" method="post" onsubmit="return validate();" style="padding:5px;">
  <div class="layui-form-item" style="">
    <label class="layui-form-label">文件名</label>
    <div class="layui-input-inline">
      <input type="text" name="fileName" lay-verify="fileName" placeholder="请输入文件名" autocomplete="off" class="layui-input" value="${fileName }">
    </div>
    <div class="layui-form-mid">.html</div>
  </div>

  <div class="layui-form-item layui-form-text" style="height: 80%;">
    <label class="layui-form-label">页面 html 代码</label>
    <div class="layui-input-block">
      <textarea name="html" lay-verify="html" placeholder="请输入html代码" class="layui-textarea" style="height: 95%;">${html }</textarea>
    </div>
  </div>
  <div class="layui-form-item" style="text-align:center;">
  	<button class="layui-btn" lay-submit="" lay-filter="demo1">保存修改</button>
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
    fileName: function(value){
      if(value.length == 0){
        return '请输入.html文件的名字';
      }
      if(value.indexOf('.html') > 0){
      	return '文件的名字末尾请不要带.html，会自动加上.html后缀';
      }
    }
  });
  
  //监听提交
  form.on('submit(demo1)', function(data){
    msg.loading('数据保存中');
		var d=$("form").serialize();
        $.post("sitePc/savePageSource.do", d, function (result) {
          msg.close();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.layer.msg('操作成功', {shade: 0.3});
        	}else if(obj.result == '0'){
        		parent.layer.msg(obj.info, {shade: 0.3})
        	}else{
        		parent.layer.msg(result, {shade: 0.3})
        	}
        	parent.layer.close(index);
         }, "text");
		
    return false;
  });
  
});
</script>

</body>
</html>