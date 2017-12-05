<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="栏目管理"/>
</jsp:include>

<script src="<%=basePath+Global.CACHE_FILE %>SiteColumn_used.js"></script>
<script src="<%=basePath+Global.CACHE_FILE %>SiteColumn_type.js"></script>

<form id="form" method="post" enctype="multipart/form-data" action="<%=basePath %>column/saveColumn.do" class="layui-form" style="padding:20px; padding-top:35px;">
	<input type="hidden" name="id" value="${siteColumn.id }" />
  <div class="layui-form-item">
    <label class="layui-form-label">栏目名称</label>
    <div class="layui-input-block">
      <input type="text" name="name" required autocomplete="off" placeholder="(20个汉字之内，必填)" class="layui-input" value="${siteColumn.name }">
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">图标</label>
    <div class="layui-input-inline">
		<input type="file" id="iconFile" name="iconFile" class="layui-input" style="padding-top: 6px;" />
    </div>
    <style>
    	.oldImage{
    		margin-top: 0px;
    	}
    	@media screen and (max-width: 450px) {
		    .oldImage{
	    		margin-top: -43px;
	    		float: right;
	    		position: relative;
	    		margin-right: 9px;
    		}
		}
    </style>
    <div style="" class="oldImage">${iconImage }</div>
  </div>
    <div class="layui-form-item">
    <label class="layui-form-label">栏目类型</label>
    <div class="layui-input-block">
    	<script type="text/javascript">writeSelectAllOptionFortype_('${siteColumn['type'] }','请选择', 1);</script>
    </div>
  </div>
    <div class="layui-form-item" id="xnx3_url">
    <label class="layui-form-label">网址</label>
    <div class="layui-input-block">
      <input type="text" name="url" autocomplete="off" placeholder="请输入目标网页链接地址" class="layui-input" value="${siteColumn.url }">
    </div>
  </div>
    <div class="layui-form-item">
    <label class="layui-form-label">是否显示</label>
    <div class="layui-input-block">
    	<script type="text/javascript">writeSelectAllOptionForused_('${siteColumn.used }','请选择', 1);</script>
    </div>
  </div>
  
  <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn" onclick="formSubmit();">保存修改</button>
      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
    </div>
  </div>
</form>

<script type="text/javascript">
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

//当select type发生变化
function selectTypeChange(){
	if(document.getElementById("type").options[4].selected){
		document.getElementById("xnx3_url").style.display="";
	}else{
		document.getElementById("xnx3_url").style.display="none";
	}
}
selectTypeChange();

layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form;
  
	//当类型发生变动改变
	form.on('select(type)', function (data) {
		selectTypeChange();
	});
});

//提交
function formSubmit(){
	$.showLoading('保存中...');
	document.getElementById("form").submit();
}
</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>