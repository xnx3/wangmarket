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
	<input type="hidden" name="type" value="${siteVar.type }">
	<textarea name="valueItems" id="valueItems" style="display:none;">${siteVar.valueItems }</textarea>
	<textarea name="description" style="display:none;">${siteVar.description }</textarea>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="keywords_label">${siteVar.title}</label>
		<div class="layui-input-block" id="var_value_div">
			<textarea name="value" lay-verify="value" autocomplete="off" class="layui-textarea">${siteVar.value }</textarea>
		</div>
		<div class="layui-form-mid" style="margin-left: 110px;line-height: 14px; color: gray; font-size: 12px; padding-top:0px;">${siteVar.description }</div>
	</div>
  
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

//获取当前select的可选值
function getValueItems(){
	var v = document.getElementById('valueItems').value;
	return v.split(/[\s\n]/);
}

//显示变量值的方式
function showVarValue(ctype){
	var currentValue = document.getElementById('varValue').innerHTML;
	
	if(ctype == 'image'){
		
		layui.use('upload', function(){
			var upload = layui.upload;
			//上传图片,封面图
			upload.render({
				elem: "#uploadImagesButton" //绑定元素
				,url: '/sites/uploadImage.do' //上传接口
				,field: 'image'
				,accept: 'file'
				,size: ${maxFileSizeKB}
				,exts:'${ossFileUploadImageSuffixList }'	//可上传的文件后缀
				,done: function(res){
					//上传完毕回调
					parent.parent.msg.close();
					if(res.result == 1){
						try{
							document.getElementById("titlePicInput").value = res.url;
							document.getElementById("titlePicA").href = res.url;
							document.getElementById("titlePicImg").src = res.url;
							document.getElementById("titlePicImg").style.display='';	//避免新增加的文章，其titlepicImg是隐藏的
						}catch(err){}
						parent.parent.msg.success("上传成功");
					}else{
						parent.parent.msg.failure(res.info);
					}
				}
				,error: function(index, upload){
					//请求异常回调
					parent.parent.msg.close();
					parent.parent.msg.failure('操作异常');
				}
				,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
					parent.parent.msg.loading('上传中');
				}
			});
			
			//上传图片,图集，v4.6扩展
			//upload.render(uploadExtendPhotos);
		});
		
		var text = '<input name="value" id="titlePicInput" type="text" autocomplete="off" placeholder="点击右侧添加" class="layui-input" value="'+currentValue+'" style="padding-right: 120px;"><button type="button" class="layui-btn" id="uploadImagesButton" style="float: right;margin-top: -38px;">	<i class="layui-icon layui-icon-upload"></i></button><a href="'+currentValue+'" id="titlePicA" style="float: right;margin-top: -38px;margin-right: 60px;" title="预览原始图片" target="_black">	<img id="titlePicImg" src="'+currentValue+'?x-oss-process=image/resize,h_38" onerror="this.style.display=\'none\';" style="height: 36px;max-width: 57px; padding-top: 1px;" alt="预览原始图片"></a><input class="layui-upload-file" type="file" name="fileName">';	
		document.getElementById('var_value_div').innerHTML = text;
	}else if(ctype == 'select'){
		try{
			var optionList = '';
			var s = getValueItems();
			for(var i = 0; i< s.length; i++){
				var vs = s[i].split(":");
				var sel = '';
				if(currentValue==vs[0]){
					sel = ' selected="selected"';
				}
				optionList = optionList + '<option value="'+vs[0]+'"' +sel+'>'+vs[1]+'</option>'; 
			}
			document.getElementById('var_value_div').innerHTML = '<select name="value">'+optionList+'</select>';
		}catch(e){
			console.log(e);
			document.getElementById('var_value_div').innerHTML = '下拉数值格式填写错误，解析失败';
		}
	}else if(ctype == 'number'){
		//number输入方式
		document.getElementById('var_value_div').innerHTML = '<input type="number" name="value" value="'+currentValue+'" class="layui-input" />';
	}else{
		//text方式
		document.getElementById('var_value_div').innerHTML = '<textarea name="value" lay-verify="value" autocomplete="off" class="layui-textarea">'+currentValue+'</textarea>';
	}
	
	layui.use('form', function(){
		layui.form.render();;
	});
}
showVarValue('${siteVar.type}');
</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>