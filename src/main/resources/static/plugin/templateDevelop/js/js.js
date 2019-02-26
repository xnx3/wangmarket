layui.use('element', function(){
	var element = layui.element;
});
layui.use('upload', function(){
	
	layui.upload.render({
	  url: 'uploadPreview.do'
	  ,method :'post'
	  ,elem : '#preview_index_button'
	  ,exts: 'jpg'
	  ,field: 'image'
	  ,title :'上传网站首页预览图'
	  ,size: '1000'	//1000KB
	  ,before: function(res){
		parent.iw.loading("导入中");
	  }
	  ,done: function(res, index, upload){
	  	parent.iw.loadClose();
	    //上传成功返回值，必须为json格式
	    if(res.result == '1'){
	    	parent.iw.msgSuccess("导入成功");
	    	window.location.reload();	//刷新当前页
	    }else{
	    	alert(res.info);
	    }
	  }
	}); 
});



//保存模版名字
function saveTemplateName(){
	var templateName = document.getElementById('templateName').value.trim();
	if(templateName.length == 0){
		parent.iw.msgFailure("请输入模版编码");
		return;
	}
	
	parent.iw.loading("设置中");    //显示“操作中”的等待提示
	$.post("saveTemplateName.do?templateName="+templateName, function(data){
		parent.iw.loadClose();    //关闭“操作中”的等待提示
	    if(data.result == '1'){
	    	parent.iw.msgSuccess('设置成功');
	    	//刷新当前页面
	    	window.location.reload();
	     }else if(data.result == '0'){
	    	 parent.iw.msgFailure(data.info);
	     }else{
	    	 parent.iw.msgFailure();
	     }
	});
	
}

//模版分类数组
var typeArray = '广告设计|学校培训|五金制造|门窗卫浴|IT互联网|化工环保|建筑能源|智能科技|房产物业|金融理财|工商法律|人力产权|生活家政|服装饰品|医疗保健|装修装饰|摄影婚庆|家具数码|茶酒果蔬|组织政府|餐饮酒店|旅游服务|汽车汽配|畜牧种植|体育健身|儿童玩具|个人博客|文档资料'.split("|");

//显示第五部的选择框
function showStop5CheckboxItem(){
	var html = '';	//显示出来的选择狂
	var localType = localStorage.getItem("wangmarket_templateDevelop_type");
	if(localType == null || localType.length == 0){
		localType = -1;
	}
	
	for(var i = 0; i < typeArray.length; i++){
		html = html + '<div style="padding:10px; float:left;"><input type="radio" style="padding-left:100px;" name="type" value="'+i+'" title="'+typeArray[i]+'" lay-filter="typeRadio" '+(localType - i == 0 ? 'checked':'')+' /></div>';
	}
	document.getElementById('stop_5_checkbox_item').innerHTML = html;
}
showStop5CheckboxItem();

//显示第六步的终端适配。 传入如 pc\mobile等
function stop6SwickUse(zhongduan){
	var zd = localStorage.getItem("wangmarket_templateDevelop_terminal_"+zhongduan);
	if(zd == null || zd.length == 0){
		console.log(0);
		return 0;
	}
	return zd;
}
function showStop6Swick(){
	document.getElementById('stop6switch_mobile').innerHTML = '<input type="radio" name="terminalMobile" value="1" title="支持" lay-filter="terminalMobile" '+(stop6SwickUse('mobile') == 1 ? 'checked':'')+' >';
	document.getElementById('stop6switch_mobile').innerHTML = document.getElementById('stop6switch_mobile').innerHTML + '<input type="radio" name="terminalMobile" value="0" title="不支持" lay-filter="terminalMobile" '+(stop6SwickUse('mobile') == 0 ? 'checked':'')+' >'; 
	
	document.getElementById('stop6switch_pc').innerHTML = '<input type="radio" name="terminalPc" value="1" title="支持" lay-filter="terminalPc" '+(stop6SwickUse('pc') == 1 ? "checked":"")+' />';
	document.getElementById('stop6switch_pc').innerHTML = document.getElementById('stop6switch_pc').innerHTML + '<input type="radio" name="terminalPc" value="0" title="不支持" lay-filter="terminalPc" '+(stop6SwickUse('pc') == 0 ? "checked":"")+' />';

	document.getElementById('stop6switch_ipad').innerHTML = '<input type="radio" name="terminalIpad" value="1" title="支持" lay-filter="terminalIpad" '+(stop6SwickUse('ipad') == 1 ? "checked":"")+' />';
	document.getElementById('stop6switch_ipad').innerHTML = document.getElementById('stop6switch_ipad').innerHTML + '<input type="radio" name="terminalIpad" value="0" title="不支持" lay-filter="terminalIpad" '+(stop6SwickUse('ipad') == 0 ? "checked":"")+' />';

	document.getElementById('stop6switch_display').innerHTML = '<input type="radio" name="terminalDisplay" value="1" title="支持" lay-filter="terminalDisplay" '+(stop6SwickUse('display') == 1 ? "checked":"")+' />';
	document.getElementById('stop6switch_display').innerHTML = document.getElementById('stop6switch_display').innerHTML	+ '<input type="radio" name="terminalDisplay" value="0" title="不支持" lay-filter="terminalDisplay" '+(stop6SwickUse('display') == 0 ? "checked":"")+' />';
	
}
showStop6Swick();

/**
 * 针对第七部的开发者信息，自动填充存储的值
 */
function stop7autoInitValue(){
	var companyname = localStorage.getItem("wangmarket_templateDevelop_stop7info_companyname");
	if(companyname == null){
		companyname = '';
	}
	document.getElementById('companyname').value = companyname;
	
	var username = localStorage.getItem("wangmarket_templateDevelop_stop7info_username");
	if(username == null){
		username = '';
	}
	document.getElementById('username').value = username;
	
	var siteurl = localStorage.getItem("wangmarket_templateDevelop_stop7info_siteurl");
	if(siteurl == null){
		siteurl = '';
	}
	document.getElementById('siteurl').value = siteurl;
	
	var remark = localStorage.getItem("wangmarket_templateDevelop_stop7info_remark");
	if(remark == null){
		remark = '';
	}
	document.getElementById('remark').value = remark;
}
stop7autoInitValue();

/*
 * 第七步，设置开发者的信息
 * name 表单名字，如 companyname  username
 * value 值
 */
function stop7InputOnchange(name, value){
	console.log(value);
	localStorage.setItem("wangmarket_templateDevelop_stop7info_"+name, value);
}

layui.use('form', function(){
  var form = layui.form;
  
  form.on('radio(typeRadio)', function (data) {
	    //将选中的，存入 h5 存储
		localStorage.setItem("wangmarket_templateDevelop_type", data.value);
		console.log(data.value);
  });
  

  form.on('radio(terminalMobile)', function (data) {
	    //将选中的，存入 h5 存储
	  	console.log(data.value);
		localStorage.setItem("wangmarket_templateDevelop_terminal_mobile", data.value);
  });
  form.on('radio(terminalPc)', function (data) {
	    //将选中的，存入 h5 存储
	  	console.log(data.value);
		localStorage.setItem("wangmarket_templateDevelop_terminal_pc", data.value);
  });
  form.on('radio(terminalIpad)', function (data) {
	    //将选中的，存入 h5 存储
	  	console.log(data.value);
		localStorage.setItem("wangmarket_templateDevelop_terminal_ipad", data.value);
  });
  form.on('radio(terminalDisplay)', function (data) {
	    //将选中的，存入 h5 存储
	  	console.log(data.value);
		localStorage.setItem("wangmarket_templateDevelop_terminal_display", data.value);
  });
  
  
  
});




//导出模版
function exportTemplate(){
	var templateName = document.getElementById('templateName').value;	//第一步设置的模版名字
	if(templateName == null || templateName.length == 0){
		iw.msgFailure("请设置【第一步，给要新建立的模版起一个编码】");
		return;
	}
	
	//第四步，预览图
	try{
		console.log(document.getElementById('stop4_img').src.length);
	}catch(e){
		iw.msgFailure("请设置【第四步，模版首页预览图】");
		return;
	}
	
	
	var type = localStorage.getItem("wangmarket_templateDevelop_type");
	console.log('type-'+type);
	if(type == null || type.length < 0){
		iw.msgFailure("请设置【第五步，模版所属分类】");
		return;
	}
	var terminalMobile = stop6SwickUse('mobile');
	var terminalPc = stop6SwickUse('pc');
	var terminalIpad = stop6SwickUse('ipad');
	var terminalDisplay = stop6SwickUse('display');
	if(terminalMobile == 0 && terminalPc == 0 && terminalIpad == 0 && terminalDisplay == 0){
		//如果都是0，那么就是没选择
		iw.msgFailure("请设置【第六步，模版支持的终端设备】");
		return;
	}
	
	//previewUrl
	
	parent.iw.loading("打包中...");
	$.post("exportTemplate.do", 
		{ 
			"name": templateName,
			"companyname":document.getElementById('companyname').value ,
			"username":document.getElementById('username').value ,
			"siteurl":document.getElementById('siteurl').value ,
			"remark":document.getElementById('remark').value ,
			"terminalMobile":terminalMobile ,
			"terminalPc":terminalPc ,
			"terminalIpad":terminalIpad ,
			"terminalDisplay":terminalDisplay,
			"type":type
		}, 
	   function(data){
			parent.iw.loadClose();
			if(data.result != '1'){
				iw.msgFailure(data.info);
				return;
			}else{
				parent.iw.msgSuccess("打包成功！");
				window.location.href=data.info;
			}
	   }, "json");
}




$(function(){
	
	//导入模版
	var daorutemplate_tipindex = 0;
	$("#daorutemplate").hover(function(){
		daorutemplate_tipindex = layer.tips('1.&nbsp;&nbsp;当网站刚创建，但尚未有模版时，此处可从云端模版库、或者本地自有模版中导入现有的模版，快速创建模版页面、模版变量、以及栏目，实现极速建站<br/>2.&nbsp;&nbsp;当模版已经存在，使用了模版后，可以通过此处进行还原操作。还原回模版某一时刻的样子', '#daorutemplate', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(daorutemplate_tipindex);
	})
	
	
	//导出模版
	var daochutemplate_tipindex = 0;
	$("#daochutemplate").hover(function(){
		daochutemplate_tipindex = layer.tips('1.&nbsp;&nbsp;将当前网站的模版导出，可以在新网站中将导出的模版文件导入，实现快速复制网站。<br/>2.&nbsp;&nbsp;也会起到备份模版的作用，网站如果操作失误删除东西了，可以通过此处进行还原回来', '#daochutemplate', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(daochutemplate_tipindex);
	})
});
	
