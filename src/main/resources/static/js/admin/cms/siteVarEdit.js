/* 模板变量里面的多图片上传 */

try{

	//图集方面
	var uploadExtendPhotos = {
		elem: '.uploadImagesButton' //绑定元素
		,url: '/sites/uploadImage.do' //上传接口
		,field: 'image'
		,accept: 'file'
		,size: size
		,exts:exts	//可上传的文件后缀
		,done: function(res){
			//上传完毕回调
			parent.msg.close();
			
			var key = this.item[0].name;	//拿到传递参数的key，也就是 extend.photos 中，数组某项的下表
			
			if(res.result == 1){
				try{
					document.getElementById("titlePicInput"+key).value = res.url;
					document.getElementById("titlePicA"+key).href = res.url;
					document.getElementById("titlePicImg"+key).src = res.url;
					document.getElementById("titlePicImg"+key).style.display='';	//避免新增加的文章，其titlepicImg是隐藏的
				}catch(err){}
				parent.msg.success("上传成功");
			}else{
				parent.msg.failure(res.info);
			}
		}
		,error: function(index, upload){
			//请求异常回调
			parent.msg.close();
			parent.msg.failure('操作失败');
		}
		,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
			parent.msg.loading('上传中');
		}
	};
	layui.use('upload', function(){
		upload = layui.upload;
		//上传图片,图集
		upload.render(uploadExtendPhotos);
	});

	
}catch(e){
	console.log(e);
}

/* 获取图集的值。每个图片用,分割 */
function getImageGroupValue(){
	var v = '';
	try{
		var nodes = document.getElementsByName("imageGroupName");
		for (var i = 0; i < nodes.length; i++) {
			var array_element =nodes[i];
			if(array_element.value.length > 0){
				if(v.length > 0){
					v = v + ',';
				}
				v = v + array_element.value;
			}
		}
	}catch(e){
		console.log(e);
	}
	return v;
}

//获取当前select的可选值
function getValueItems(){
	var v = document.getElementById('valueItems').value;
	return v.split(/[\s\n]/);
}

//显示变量值的方式
function showVarValue(ctype){
	var currentValue = document.getElementById('varValue').innerHTML;
	if(document.getElementById('valueItemsFormItem') != null){
		document.getElementById('valueItemsFormItem').style.display = 'none';	//默认隐藏
	}
	
	if(ctype == 'image'){
		//单图片上传
		layui.use('upload', function(){
			var upload = layui.upload;
			//上传图片,封面图
			upload.render({
				elem: "#uploadImagesButton" //绑定元素
				,url: '/sites/uploadImage.do' //上传接口
				,field: 'image'
				,accept: 'file'
				,size: size
				,exts:exts	//可上传的文件后缀
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
		
		var text = '<input name="value" id="titlePicInput" type="text" autocomplete="off" placeholder="点击右侧添加" class="layui-input" value="'+currentValue+'" style="padding-right: 120px;"><button type="button" class="layui-btn" id="uploadImagesButton" style="float: right;margin-top: -38px;">	<i class="layui-icon layui-icon-upload"></i></button><a href="'+currentValue+'" id="titlePicA" style="float: right;margin-top: -38px;margin-right: 60px;" title="预览原始图片" target="_black">	<img id="titlePicImg" src="'+currentValue+'?x-oss-process=image/resize,h_38" class="previewImg" onerror="this.style.display=\'none\';" style="height: 36px;max-width: 57px; padding-top: 1px;" alt="预览原始图片"></a><input class="layui-upload-file" type="file" name="fileName">';	
		document.getElementById('var_value_div').innerHTML = text;

		document.getElementById('var_value_layuiItem').style.display = '';	//显示变量值这一行
		document.getElementById('sitecolumn_editUseExtendPhotos').style.display = 'none';	//隐藏图集录入
	}else if(ctype == 'select'){
		//下拉选择
		
		if(document.getElementById('valueItemsFormItem') != null){
			document.getElementById('valueItemsFormItem').style.display = '';	//显示编辑项
		}
		
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
		
		document.getElementById('var_value_layuiItem').style.display = '';	//显示变量值这一行
		document.getElementById('sitecolumn_editUseExtendPhotos').style.display = 'none';	//隐藏图集录入
	}else if(ctype == 'number'){
		//number整数输入方式
		document.getElementById('var_value_div').innerHTML = '<input type="number" name="value" value="'+currentValue+'" class="layui-input" />';
		
		document.getElementById('var_value_layuiItem').style.display = '';	//显示变量值这一行
		document.getElementById('sitecolumn_editUseExtendPhotos').style.display = 'none';	//隐藏图集录入
	}else if(ctype == 'imagegroup'){
		//多图片方式
		document.getElementById('sitecolumn_editUseExtendPhotos').style.display = '';	//显示图集录入
		document.getElementById('var_value_layuiItem').style.display = 'none';	//隐藏变量值这一行
	}else{
		//text方式
		document.getElementById('var_value_div').innerHTML = '<textarea name="value" lay-verify="value" autocomplete="off" class="layui-textarea">'+currentValue+'</textarea>';
		
		document.getElementById('var_value_layuiItem').style.display = '';	//显示变量值这一行
		document.getElementById('sitecolumn_editUseExtendPhotos').style.display = 'none';	//隐藏图集录入
	}
	
	layui.use('form', function(){
		layui.form.render();;
	});
}