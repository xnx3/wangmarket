<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="编辑全局变量"/>
</jsp:include>

<form id="form" class="layui-form layui-form-pane" action="save.do" method="post" style="padding:5px;">
   <div class="layui-form-item" style="">
    <label class="layui-form-label">变量名</label>
    <div class="layui-input-inline">
      <input type="text" name="name" lay-verify="name" placeholder="请输入变量名" autocomplete="off" class="layui-input" value="${json.codeName }">
    </div>
    <div class="layui-form-mid" style="color: gray; font-size: 14px; padding-top:0px;">同一个网站中的变量名必须是唯一的,限英文、数字。<br/>在模板制作时，可以用 {var.xxx} 进行调取</div>
  </div>
  
  
  <div class="layui-form-item" style="">
    <label class="layui-form-label">备注说明</label>
    <div class="layui-input-inline">
      <input type="text" name="description" lay-verify="description" placeholder="请输入备注" autocomplete="off" class="layui-input" value="${json.description }">
    </div>
    <div class="layui-form-mid" style="color: gray; font-size: 14px;">仅为提示备注,无其他任何作用</div>
  </div>
  
  <div class="layui-form-item" style="">
    <label class="layui-form-label">变量值</label>
    <div class="layui-input-inline">
      <input type="text" name="value" lay-verify="value" placeholder="请输入变量值" autocomplete="off" class="layui-input" value="${json.value }">
    </div>
    <div class="layui-form-mid" style="color: gray; font-size: 14px; padding-top:0px;">变量的值</div>
  </div>
  
  
  <div class="layui-form-item" style="text-align:center;">
  	<button class="layui-btn" lay-submit="" lay-filter="demo1">保存</button>
  </div>
</form>

<script>

layui.use(['element', 'form', 'layedit', 'laydate'], function(){
  var form = layui.form;
  var element = layui.element;
  
  //自定义验证规则
  form.verify({
    name: function(value){
      if(value.length < 1){
      	return '请输入变量名';
      }
    },
  });
  
  //监听提交
  form.on('submit(demo1)', function(data){
		parent.msg.loading('保存中');
		var d=$("form").serialize();
        $.post("/siteVar/save.do", d, function (result) { 
        	parent.msg.close();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.msg.success("保存成功");
        		parent.location.reload();
        	}else if(obj.result == '0'){
        		parent.msg.failure(obj.info);
        	}else{
        		parent.msg.failure(result);
        	}
         }, "text");
		
    return false;
  });
  
});

</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>