<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="编辑输入模型"/>
</jsp:include>
<!-- 代码编辑模式所需资源 -->
<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}module/editor/css/editormd.css" />
<script src="${STATIC_RESOURCE_PATH}module/editor/editormd.js"></script>

<form id="form" class="layui-form layui-form-pane" action="save.do" method="post" style="padding:5px;">
  <input type="hidden" name="id" value="${inputModel.id }" />
  
      <div class="layui-form-item" style="">
    <label class="layui-form-label">模型代码</label>
    <div class="layui-input-inline">
      <input type="text" name="codeName" lay-verify="codeName" placeholder="请输入模型代码" autocomplete="off" class="layui-input" value="${inputModel.codeName }">
    </div>
    <div class="layui-form-mid" style="color: gray; font-size: 14px; padding-top:0px;">同一个网站中的模型代码必须是唯一的,限30字以内。<b>强烈建议添加后就不要改动了！</b><br/>备份还原、栏目绑定模型都是使用此模型代码</div>
  </div>
  
  
  <div class="layui-form-item" style="">
    <label class="layui-form-label">备注说明</label>
    <div class="layui-input-inline">
      <input type="text" name="remark" lay-verify="remark" placeholder="请输入备注" autocomplete="off" class="layui-input" value="${inputModel.remark }">
    </div>
    <div class="layui-form-mid" style="color: gray; font-size: 14px;">仅为提示备注,无其他任何作用,限制30字以内</div>
  </div>
  

  <div class="layui-form-item layui-form-text" style="height: 80%;">
    <label class="layui-form-label">模型内容(2万字以内)</label>
    <div class="layui-input-block">
    	<div id="htmlMode" style="width:100%;height:auto; ">

			<div id="editormd" style="width:100%; height:auto; min-height:400px;">
				<textarea name="text" id="html_textarea" style="height:600px;" lay-verify="text" placeholder="请输入输入模型的提交表单的html代码" class="layui-textarea" style="height: 95%;"></textarea>
			</div>
			
        </div>
    </div>
  </div>
  
	<!-- 说明区域 -->
	<div class="layui-collapse" style="margin-top:-17px;">
	  <div class="layui-colla-item">
	    <h2 class="layui-colla-title">动态调取参数说明(若是修改数据，可用此参数调取原本数据的信息)</h2>
	    <div class="layui-colla-content" style="font-size:12px;">
	    	{news.title} 标题<br/>
	    	{news.intro} 简介<br/>
	    	{news.extend.???} 【推荐】预留字段，可根据你的意愿来自由使用，自由扩展任意多的字段。 <a href="http://tag.wscso.com/8318.html" target="_black">查看详细说明</a><br/>
	    	{news.reserve1} 【已不推荐！推荐使用 {news.extend.???}】预留字段，系统中未使用，可根据你的意愿来自由使用<br/>
	    	{news.reserve2} 【已不推荐! 推荐使用 {news.extend.???}】预留字段，系统中未使用，可根据你的意愿来自由使用<br/>
	    	{titlepicImage} 或 {news.titlepic} 标题图/列表图，用于列表展示的图片，若有，会以<img ...>标签显示出来<br/>
	    	{siteColumn.type} 当前操作的内容所属栏目的类型编码<br/>
	    	{text} 内容，正文<br/>
	    </div>
	  </div>
	  <div class="layui-colla-item">
	    <h2 class="layui-colla-title">表单提交保存数据的input标签的name说明</h2>
	    <div class="layui-colla-content" style="font-size:12px;">
	    	name="title" 标题(最大可输入30字，必填项，不可省去，必须存在此项)<br/>
	    	name="intro" 简介(最大可输入160字，选填，若不需要可不再表单中体现).<br/>
	    	&nbsp;&nbsp;&nbsp;&nbsp;当提交的简介为空，或者没有简介这个字段时，会自动从正文text中截取前160个字作为简介<br/>
	    	&nbsp;&nbsp;&nbsp;&nbsp;当提交的简介有内容时，保存简介的内容<br/>
	    	name="titlePicFile" 标题图，用于存储信息列表展示的图片，如产品展示栏目所需要的产品列表图，注意，此处input标签的type类型需要为file！(选填，若不需要可不再表单中体现)
	    	name="text" 内容，正文，最大可保存五十万字。几乎可忽略字数限制。<br/>
	    	name="extend.???"【推荐】预留字段，可根据你的意愿来自由使用,自由扩展任意多的字段。 <a href="http://tag.wscso.com/8318.html" target="_black">查看详细说明</a><br/>
	    	name="reserve1" 【已不推荐！推荐使用 news.extend.???】预留字段，系统中未使用，可根据你的意愿来自由使用。长度限制10个文字字符以内！<br/>
	    	name="reserve2" 【已不推荐！推荐使用 news.extend.???】预留字段，系统中未使用，可根据你的意愿来自由使用。长度限制10个文字字符以内！<br/>
	    </div>
	  </div>
	  <div class="layui-colla-item" style="font-size:12px;">
	    <h2 class="layui-colla-title">其他说明</h2>
	    <div class="layui-colla-content">
	    	1. 模型内容最大可存储两万字。
    		<br/>2. 已引入JQuery、Layer等js框架，可直接再其中使用
    		<br/>3. 您可自己在其中添加js方法 function save(){} 在点击保存时，会先执行save()，再执行提交。 另外，如果save() 方法 return false，那么会终止保存功能的执行，即不会进行保存
	    </div>
	  </div>
	</div>


  <br/>
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
    remark: function(value){
      if(value.length > 30){
      	return '请输入30个字以内的输入模型的名称';
      }
    },
    codeName: function(value){
        if(value.length == 0){
          return '请输入输入模型代码';
        }
        if(value.length > 30){
        	return '请输入30个字以内的输入模型代码';
        }
      }
  });
  
  //监听提交
  form.on('submit(demo1)', function(data){
		parent.msg.loading('保存中');
		var d=$("form").serialize();
        $.post("/inputModel/save.do", d, function (result) { 
        	parent.msg.close();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.msg.success("保存成功");
        		window.location.href="list.do";
        	}else if(obj.result == '0'){
        		msg.failure(obj.info);
        	}else{
        		msg.failure(result);
        	}
         }, "text");
		
    return false;
  });
  
});

//在加载完输入模型内容后，进行加载编辑器
function loadEditor(){
	//代码编辑器
	testEditor = editormd("editormd", {
	    width            : "100%",
	    height            : "650px",
	    watch            : false,
	    toolbar          : false,
	    codeFold         : true,
	    searchReplace    : true,
	    placeholder      : "请输入模版变量的代码",
	    value            : document.getElementById("html_textarea").value,
	    theme            : "default",
	    mode             : "text/html",
	    path             : '${STATIC_RESOURCE_PATH}module/editor/lib/'
	});
}

//加载输入模型的主要数据
function load(){
	parent.msg.loading("加载中");
	$.post("/inputModel/getInputModelTextById.do?id=${inputModel.id }", function(data){
		parent.msg.close();
		if(data.result == '1'){
			//编辑模式，获取模型主要内容成功，加载到textarea
			document.getElementById("html_textarea").innerHTML = data.info;
			loadEditor();
	 	}else if(data.result == '3'){
	 		//新增模式，获取默认的输入模型内容，赋予textarea
			document.getElementById("html_textarea").innerHTML = data.info;
			layer.msg("自动赋予系统默认输入模型，可以在此基础上进行修改，以创建自己的输入模型！", {shade: 0.3})
			loadEditor();
	 	}else{
	 		parent.msg.failure(data.info);
	 	}
	});

}
load();


</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>