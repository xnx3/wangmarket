<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="文章内容"/>
</jsp:include>
<script type="text/javascript" src="${STATIC_RESOURCE_PATH}js/fun.js"></script>

<script type="text/javascript">
/**
 * 编辑页面的内容代码， 同文章列表页的一样，复制于此
 * 页面类型为独立页面
 * @param name TemplatePage.name要编辑的模版页面名字
 */
function editText(name){
	if(parent.currentMode == 2){
		//要将其切换回智能模式
		parent.window.htmledit_mode();
	}

	parent.document.getElementById("currentTemplatePageName").value = name;
	parent.loadIframe();
	
	try{
		parent.document.getElementById("currentTemplateType").innerHTML = '详情页模版';
		parent.document.getElementById("tongyong").style.display = '';
		parent.document.getElementById("lanmu").style.display = '';
		parent.document.getElementById("fenye").style.display = 'none';
		parent.document.getElementById("wenzhang").style.display = '';
		parent.document.getElementById("dongtailanmu").style.display = '';
		parent.document.getElementById("xiangqingduyou").style.display = '';
		parent.document.getElementById("liebiaoduyou").style.display = 'none';
	}catch(err){}
	
	//parent.document.getElementById('iframe').src='http://localhost:8080/selfSite/template/getTemplatePageText.do?pageName='+name;
	parent.layer.close(index);
}

//判断文章所属的栏目，所属类型，若是独立页面，同时还是模板式编辑，那么直接进入模板编辑
if('${siteColumn.type}' == 3 && '${siteColumn.editMode}' == 1){
	editText('${siteColumn.templatePageViewName}');
}
</script>

<style>
/*input输入框下面的文字说明*/
.explain{
	font-size: 12px;
    color: gray;
    padding-top: 3px;
}
</style>


<form id="form" method="post" class="layui-form" enctype="multipart/form-data" style="padding-top:35px; margin-bottom: 10px; padding-right:35px;">
	<input type="hidden" name="id" value="${news.id }" />
	<input type="hidden" name="cid" value="${news.cid }" />
	<input type="hidden" name="type" value="${news.type }" />
	
	${inputModelText }
    
	<div class="layui-form-item" style="padding-top:15px; text-align:center;">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit="" lay-filter="demo1" style="width:150px; font-size: 16px; height: 45px;line-height: 45px;">保存</button>
		</div>
	</div>
</form>

<script>
try{
	if('${siteColumn.editUseTitlepic}' == '1'){
		document.getElementById('sitecolumn_editUseTitlepic').style.display='';
	}
}catch(e){ }
try{
	if('${siteColumn.editUseExtendPhotos}' == '1'){
		document.getElementById('sitecolumn_editUseExtendPhotos').style.display='';
	}
}catch(e){ }
try{
	if('${siteColumn.editUseIntro}' == '1'){
		document.getElementById('sitecolumn_editUseIntro').style.display='';
	}
}catch(e){ }
try{
	if('${siteColumn.editUseText}' == '1'){
		document.getElementById('sitecolumn_editUseText').style.display='';
	}
}catch(e){ }

var uploadExtendPhotos = {
		elem: '.uploadImagesButton' //绑定元素
		,url: parent.masterSiteUrl+'sites/uploadImage.do' //上传接口
		,field: 'image'
		,accept: 'file'
		,size: ${maxFileSizeKB}
		,exts:'${ossFileUploadImageSuffixList }'	//可上传的文件后缀
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

var upload;
layui.use('upload', function(){
	upload = layui.upload;
	//上传图片,封面图
	//upload.render(uploadPic);
	upload.render({
		elem: "#uploadImagesButton" //绑定元素
		,url: parent.masterSiteUrl+'sites/uploadImage.do' //上传接口
		,field: 'image'
		,accept: 'file'
		,size: ${maxFileSizeKB}
		,exts:'${ossFileUploadImageSuffixList }'	//可上传的文件后缀
		,done: function(res){
			//上传完毕回调
			parent.msg.close();
			if(res.result == 1){
				try{
					document.getElementById("titlePicInput").value = res.url;
					document.getElementById("titlePicA").href = res.url;
					document.getElementById("titlePicImg").src = res.url;
					document.getElementById("titlePicImg").style.display='';	//避免新增加的文章，其titlepicImg是隐藏的
				}catch(err){}
				parent.msg.success("上传成功");
			}else{
				parent.msg.failure(res.info);
			}
		}
		,error: function(index, upload){
			//请求异常回调
			parent.msg.close();
			parent.msg.failure();
		}
		,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
			parent.msg.loading('上传中..');
		}
	});
	
	//上传图片,图集，v4.6扩展
	upload.render(uploadExtendPhotos);
});
</script>

<script type="text/javascript">
layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form;
  //监听提交
	form.on('submit(demo1)', function(data){
	
		//判断是否有 save() 方法，若有，则先执行 save()  这个方法是用户在输入模型中自己定义的
		if(typeof(save) == 'function'){
			try{
				var s_return = save();
				if(typeof(s_return) == 'boolean' && s_return == false){
					return false;
				}
			}catch(err){}
		}
	
		parent.msg.loading("保存中");    //显示“操作中”的等待提示
		//创建FormData对象，获取到form表单的相关数据
		var formobj =  document.getElementById("form");
		var data = new FormData(formobj);
	
	    //为FormData对象添加上传图片的数据，这里由3.11版本更新后，图片单独上传，此项废弃
	    //$.each($('#titlePicFile')[0].files, function(i, file) {
	    //    data.append('titlePicFile', file);
	    //});
		$.ajax({
			url:'/news/saveNews.do',
	        type:'POST',
	        data:data,
	        cache: false,
	        contentType: false,    //不可缺
	        processData: false,    //不可缺
	        success:function(data){
	        	parent.msg.close();    //关闭“操作中”的等待提示
	        	if(data.result == '1'){
			        //上传成功
			        
	        		//判断是否有 saveSuccess() 方法，若有，则先执行 saveSuccess()  这个方法是用户在输入模型中自己定义的
	        		if(typeof(saveSuccess) == 'function'){
	        			try{
	        				var success_return = saveSuccess();
	        				if(typeof(success_return) == 'boolean' && success_return == false){
	        					return false;
	        				}
	        			}catch(err){ console.log(err); }
	        		}
			        
	            	parent.msg.success('保存成功');
	            	window.location.href='listForTemplate.do?cid=${news.cid }';
			    }else if(data.result == '0'){
			        parent.msg.failure(data.info);
			    }else{
			        parent.msg.failure('操作失败');
			    }
	        },
	        error:function(){
	        	parent.msg.close();    //关闭“操作中”的等待提示
	        	parent.msg.failure('出错');
	        }
		});
    return false;
  });
  
});

//载入素材库
function loadSuCai(){
	//加载素材的支持库
	dynamicLoading.js(resBasePath+"ueditor/template/load.js");
}
</script>

</body>
</html>