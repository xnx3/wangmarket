<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="编辑全局变量"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>SiteVar_type.js"></script>

<form id="form" class="layui-form" action="save.do" method="post" style="padding-top:20px; padding-right:20px;">
	<input type="hidden" name="updateName" value="${siteVar.name }">
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnCode">变量名</label>
		<div class="layui-input-block">
			<input type="text" name="name" lay-verify="name" autocomplete="off" placeholder="请输入变量名" class="layui-input" value="${siteVar.name }">
		</div>
		<div class="layui-form-mid" style="margin-left: 110px;line-height: 14px; color: gray; font-size: 12px; padding-top:0px;">同一个网站中，变量名必须是唯一的,限英文、数字、下划线_<br/>在制作模板时，也就是在模板变量跟模板页面中，可以用 {var.${siteVar.name }} 来调取变量值</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnCode">标题</label>
		<div class="layui-input-block">
			<input type="text" name="title" class="layui-input" value="${siteVar.title}">
		</div>
		<div class="layui-form-mid" style="margin-left: 110px;line-height: 14px; color: gray; font-size: 12px; padding-top:0px;">实际给客户使用时，会隐藏变量名，这个标题就是显示给用户看的录入项的标题。这个标题就只是提供观看而已。</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnCode">录入方式</label>
		<div class="layui-input-block">
			<script type="text/javascript">writeSelectAllOptionFortype_('${siteVar.type}','', true);</script>
		</div>
		<div class="layui-form-mid" style="margin-left: 110px;line-height: 14px; color: gray; font-size: 12px; padding-top:0px;">用户填写此变量的方式</div>
	</div>
	<div class="layui-form-item" id="valueItemsFormItem">
		<label class="layui-form-label" id="columnCode">下拉数值</label>
		<div class="layui-input-block">
			<textarea name="valueItems" onchange="showVarValue('select');" id="valueItems" autocomplete="off" class="layui-textarea">${siteVar.valueItems }</textarea>
		</div>
		<div class="layui-form-mid" style="margin-left: 110px;line-height: 14px; color: gray; font-size: 12px; padding-top:0px;">下拉选择框的可选项，每个选项一行，填写如：<br/>
			<div>
				0:关闭<br/>
				1:开启
			</div> 
			每行格式的含义为  变量的值:用户看到的文字说明
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label" id="description_label">备注说明</label>
		<div class="layui-input-block">
			<textarea name="description" lay-verify="description" autocomplete="off" placeholder="限200个字符以内" class="layui-textarea">${siteVar.description }</textarea>
		</div>
		<div class="layui-form-mid" style="margin-left: 110px;line-height: 14px; color: gray; font-size: 12px; padding-top:0px;">只是备注而已，没有什么其他作用。修改的时候看到这个，能直到这是干嘛的</div>
	</div>
	<div class="layui-form-item" id="var_value_layuiItem">
		<label class="layui-form-label" id="keywords_label">变量值</label>
		<div class="layui-input-block" id="var_value_div">
			<textarea name="value" class="layui-textarea">${siteVar.value }</textarea>
		</div>
	</div>
	
	
	<div class="layui-form-item" id="sitecolumn_editUseExtendPhotos" style="display:none;">
	<div id="photosDefaultValue" style="display:none;">${siteVar.value}</div><!-- 这里放置图集原本的值 -->
	<script>
		try{
			//var pdv = document.getElementById('photosDefaultValue').innerHTML.trim().replace(/{templatePath}/g,'${templatePath}');;
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
	<label class="layui-form-label" id="label_columnName">文章图集</label>
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
		<!-- <div class="explain" style=" float: left; padding-left: 15px; padding-top: 6px; float:left;">这里显示图集的上传指引，比如：建议图片比例为4:3</div> -->
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

layui.use(['element', 'form', 'layedit', 'laydate'], function(){
  var form = layui.form;
  var element = layui.element;
  
  //自定义验证规则
  form.verify({
    name: function(value){
      if(value.length < 1){
      	return '请输入变量名';
      }
		if(/^[a-zA-Z0-9_]*$/g.test(value)){
			//success
		}else{
			return '变量名只限英文、数字、下划线_';
		}
    },
  });
  
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
  
  	//当类型发生变动改变
	form.on('select(type)', function (data) {
		showVarValue(document.getElementById("type").value);
	});
  
});

var exts = '${ossFileUploadImageSuffixList }';
var size = ${maxFileSizeKB};
</script>
<script type="text/javascript" src="/js/admin/cms/siteVarEdit.js"></script>
<script>
showVarValue('${siteVar.type}');
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