<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="编辑全局变量"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>SiteVar_type.js"></script>

<form id="form" class="layui-form" action="save.do" method="post" style="padding-top:20px; padding-right:20px;">
	<input type="hidden" name="updateName" value="${siteVar.name }">
	<input type="hidden" name="name" value="${siteVar.name }">
	<input type="hidden" name="title" value="${siteVar.title }">
	<input type="hidden" id="type" name="type" value="${siteVar.type }">
	<textarea name="valueItems" id="valueItems" style="display:none;">${siteVar.valueItems }</textarea>
	<textarea name="description" style="display:none;">${siteVar.description }</textarea>
	
	<div class="layui-form-item" id="var_value_layuiItem">
		<label class="layui-form-label" id="keywords_label" style=" height: 18px;">${siteVar.title}</label>
		<div class="layui-input-block" id="var_value_div">
			<textarea name="value" class="layui-textarea">${siteVar.value }</textarea>
		</div>
		<div class="layui-form-mid" style="margin-left: 110px;line-height: 14px; color: gray; font-size: 12px; padding-top:0px;">${siteVar.description }</div>
	</div>
  	
  	
  	<div class="layui-form-item" id="sitecolumn_editUseExtendPhotos" style="display:none;">
	<div id="photosDefaultValue" style="display:none;">${siteVar.value}</div><!-- 这里放置图集原本的值 -->
	<script>
		try{
			//var pdv = document.getElementById('photosDefaultValue').innerHTML.trim().replace(/{templatePath}/g,'${templatePath}');
			var pdv = document.getElementById('photosDefaultValue').innerHTML.trim();
			if(pdv.length > 0 && pdv.indexOf(',') > 0){
				var pdvs = pdv.split(',');
				var pdvjson = '[';
				for(var p = 0; p<pdvs.length; p++){
					if(pdvjson.length > 2){
						pdvjson = pdvjson + ',';
					}
					pdvjson = pdvjson + '"'+pdvs[p]+'"';
				}
				pdvjson = pdvjson + ']';
				console.log(pdvjson);
				document.getElementById('photosDefaultValue').innerHTML = pdvjson;
			}
		}catch(e){ console.log(e); }
	</script>
	<input type="hidden" value="0" id="photos_i" style="display:none;" /><!-- 这里放循环输出input的i，也就是extend.photos数组下标。也就是图集中有多少个input输入框。从0开始。此处由 appendPhotosInput 自动管理，不可吧此删除掉。 -->
	<label class="layui-form-label" id="label_columnName">${siteVar.title}</label>
	<div class="layui-input-block" id="photoInputList" style="min-height: 0px;">
		<!-- 同样，这个 photoInputList 里面的也算是每一个item的模版。item模版开始 -->
		<div id="photos_input_item_{i}" style="padding-top:5px;">
			<input name="imageGroupName" id="titlePicInput{i}" type="text" autocomplete="off" placeholder="点击右侧添加" class="layui-input" value="{value}" style="padding-right: 174px;">
			<button type="button" name="{i}" class="layui-btn uploadImagesButton" id="uploadImagesButton{i}" style="float: right;margin-top: -38px;">
				<i class="layui-icon layui-icon-upload"></i>
			</button>
			<a href="{value}" id="titlePicA{i}" style="float: right;margin-top: -38px;margin-right: 116px;" title="预览原始图片" target="_black">
				<img id="titlePicImg{i}" class="previewImg" src="{value}?x-oss-process=image/resize,h_38" onerror="this.style.display='none';" style="height: 36px;max-width: 57px; padding-top: 1px;" alt="预览原始图片">
			</a><input class="layui-upload-file" type="file" name="fileName">
			<a href="javascript:deletePhotosInput('{i}');" class="layui-btn" style="float: right;margin-top: -38px;margin-right: 58px;" title="删除" >
				<i class="layui-icon layui-icon-delete"></i>
			</a>
		</div>
		<!-- item模版结束 -->
	</div>
	<div style="padding-top:5px; padding-left:110px;">
		<a href="javascript:appendPhotosInput('');" class="layui-btn layui-btn-sm layui-btn-primary layui-btn-radius" style="float:left;">添加一个图片输入框</a>
		<div class="layui-form-mid" style="line-height: 14px; color: gray; font-size: 12px; padding-top:0px;">${siteVar.description }</div>
	</div>
</div>
  <!--  图集，让文章可以拥有多张图片上传的功能。若是使用，可以在 栏目管理 中，编辑栏目时，有个 信息录入的选项卡，找到文章图集，点击 使用 即可。若是自己添加的输入模型，请保留 id="editUseExtendPhotos" ,不然栏目设置中的是否使用图集功能将会失效！ -->
<script type="text/javascript" src="/js/admin/cms/news_extend_photos.js"></script>
  
  
  
	  <div class="layui-form-item" style="text-align:center;">
	  	<button class="layui-btn" lay-submit="" lay-filter="demo1">保存</button>
	  </div>
</form>

<div id="varValue" style="display:none;">${siteVar.value }</div>
<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

layui.use(['element', 'form', 'layedit', 'laydate'], function(){
  var form = layui.form;
  var element = layui.element;
  
  //监听提交
  form.on('submit(demo1)', function(data){
  		//判断是否是多图片图集方式
  		if(document.getElementById('type').value == 'imagegroup'){
  			//使用的图集，要将图集内容赋予 value 中
  			document.getElementsByName("value")[0].value = getImageGroupValue();
  		}
  		
		parent.parent.msg.loading('保存中');
		var d=$("form").serialize();
        $.post("/siteVar/save.do", d, function (result) { 
        	parent.msg.close();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.parent.msg.success("保存成功");
        		parent.location.reload();
        	}else if(obj.result == '0'){
        		parent.parent.msg.failure(obj.info);
        	}else{
        		parent.parent.msg.failure(result);
        	}
         }, "text");
		
    return false;
  });
  
});

var exts = '${ossFileUploadImageSuffixList }';
var size = ${maxFileSizeKB};
</script>
<script type="text/javascript" src="/js/admin/cms/siteVarEdit.js"></script>
<script>
showVarValue('${siteVar.type}');


parent.layer.iframeAuto(index);
</script>
<script>
try{
	//图片预览, 将 {templatePath} 转化为绝对url路径，来显示出图片
	var templatePath = '${templatePath}';
	var previreImgList = document.getElementsByClassName('previewImg');
	for(var i = 0; i < previreImgList.length; i++){ 
		previreImgList[i].src = previreImgList[i].attributes.src.value.replace(/{templatePath}/g,templatePath);
		previreImgList[i].parentNode.href=previreImgList[i].src;
	}
}catch(e){
	console.log(e);
}
</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>