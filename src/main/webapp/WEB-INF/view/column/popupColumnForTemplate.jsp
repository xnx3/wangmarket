<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.DateUtil"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../iw/common/head.jsp">
    <jsp:param name="title" value="编辑栏目"/>
</jsp:include>

<script src="/<%=Global.CACHE_FILE %>SiteColumn_used.js"></script>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_type.js"></script>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_editMode.js"></script>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_listRank.js"></script>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_useGenerateView.js"></script>

<script src="/<%=Global.CACHE_FILE %>SiteColumn_templateCodeColumnUsed.js"></script>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_adminNewsUsed.js"></script>
<style>
.explain{
	font-size: 12px;
    color: gray;
    padding-top: 4px;
}
</style>


<form id="form" method="post" class="layui-form" style="padding:0px;margin-bottom: 10px; margin-top:0px;">
	<input type="hidden" value="${siteColumn.id }" name="id">
	
	
<div class="layui-tab layui-tab-card" style="border-style: none; box-shadow: 0 0px 0px 0 rgba(0,0,0,.1); margin-top: 0px;">
  <ul class="layui-tab-title">
    <li class="layui-this">基本设置</li>
    <li>信息录入</li>
    <li>显示</li>
    <li>SEO</li>
    <li>高级设置</li>
  </ul>
  <div class="layui-tab-content" style="padding-right: 35px;">
    <div class="layui-tab-item layui-show">
    	<div class="layui-form-item">
			<label class="layui-form-label" id="label_columnName">栏目名称</label>
			<div class="layui-input-block">
				<input type="text" name="name" lay-verify="name" autocomplete="off" placeholder="限40个字符以内" class="layui-input" value="${siteColumn.name }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" id="parentColumnCode_label">所属栏目</label>
			<div class="layui-input-block">
				<select name="parentCodeName" id="parentCodeName">
					${parentColumnOption}
				</select>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" id="columnType">栏目类型</label>
			<div class="layui-input-block">
				<select name="type" required="type" lay-verify="type" lay-filter="type" id="type">
					<option value="">请选择</option>
					<option value="7" <c:if test="${siteColumn.type=='7'}"> selected="selected"</c:if>>信息列表</option>
					<option value="8" <c:if test="${siteColumn.type=='8'}"> selected="selected"</c:if>>独立页面</option>
				</select>
			</div>
		</div>
		<div class="layui-form-item" id="xnx3_listTemplate">
			<label class="layui-form-label" id="listTemplate">列表模版</label>
			<div class="layui-input-block">
				<select name="templatePageListName" lay-verify="listTemplateVerify" id="templatePageListName">
					${tpl_list_option}
				</select>
			</div>
		</div>
		<div class="layui-form-item" id="xnx3_viewTemplate">
			<label class="layui-form-label" id="viewTemplate">内容模版</label>
			<div class="layui-input-block">
				<select name="templatePageViewName" id="templatePageViewName">
					${tpl_view_option}
				</select>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" id="columnCode">栏目代码</label>
			<div class="layui-input-block">
				<input type="text" name="codeName" lay-verify="required" autocomplete="off" placeholder="限20个字符以内" class="layui-input" value="${siteColumn.codeName }">
			</div>
		</div>
		<div class="layui-form-item" id="listnum">
			<label class="layui-form-label" id="listnum_label">列表条数</label>
			<div class="layui-input-block">
				<input type="number" name="listNum" autocomplete="off" placeholder="列表页面每页显示的条数" class="layui-input" value="${siteColumn.listNum }">
			</div>
		</div>
		
		
		
    </div>
    <div class="layui-tab-item">
    	<!-- 信息录入 -->
    	
    	<!-- 内容编辑方式，当独立页面时才会有效，才会显示。选择是使用内容富文本编辑框编辑，还是使用模板的方式编辑 -->
		<div class="layui-form-item" id="xnx3_editMode">
			<label class="layui-form-label" id="columnEditMode">编辑方式</label>
			<div class="layui-input-block">
				<script type="text/javascript">writeSelectAllOptionForeditMode_('${siteColumn.editMode }','', true);</script>
			</div>
		</div>
    	
    	<div class="layui-form-item neirongguanli_shuru" id="inputModel">
			<label class="layui-form-label" id="inputModel_label">输入模型</label>
			<div class="layui-input-block">
				<select name="inputModelCodeName" id="inputModelCodeName">
					${inputModelOptions }
				</select>
			</div>
		</div>
		
		
		<div style="padding-left: 30px; font-size: 12px; color: gray;padding-bottom: 10px;" class="neirongguanli_shuru">
			<hr>
			以下是当你进入 “内容管理” 中进行编辑文章时，是否显示这几个录入项
			
		</div>
		
    	
<style>
.layui-form-switch em {
	font-size: 15px;
}
</style>    	
    	<div class="layui-form-item neirongguanli_shuru">
			<label class="layui-form-label" id="label_editUseTitlepic">标题图片</label>
			<div class="layui-input-block">
				<input type="checkbox" name="editUseTitlepic" lay-skin="switch" lay-text="显示|隐藏" value="1" <c:if test="${siteColumn.editUseTitlepic=='1'}"> checked</c:if>>
			</div>
		</div>
		<div class="layui-form-item neirongguanli_shuru">
			<label class="layui-form-label" id="label_editUseExtendPhotos">文章图集</label>
			<div class="layui-input-block">
				<input type="checkbox" name="editUseExtendPhotos" lay-skin="switch" lay-text="显示|隐藏" value="1" <c:if test="${siteColumn.editUseExtendPhotos=='1'}"> checked</c:if>>
			</div>
		</div>
		<div class="layui-form-item neirongguanli_shuru">
			<label class="layui-form-label" id="label_editUseIntro">内容简介</label>
			<div class="layui-input-block">
				<input type="checkbox" name="editUseIntro" lay-skin="switch" lay-text="显示|隐藏" value="1" <c:if test="${siteColumn.editUseIntro=='1'}"> checked</c:if>>
			</div>
		</div>
		<div class="layui-form-item neirongguanli_shuru">
			<label class="layui-form-label" id="label_editUseText">内容正文</label>
			<div class="layui-input-block">
				<input type="checkbox" name="editUseText" lay-skin="switch" lay-text="显示|隐藏" value="1" <c:if test="${siteColumn.editUseText=='1'}"> checked</c:if>>
			</div>
		</div>
		
    </div>
    
    <div class="layui-tab-item">
    	<!-- 显示 -->
    	
		<fieldset class="layui-elem-field layui-field-title site-title" style="margin-bottom: 10px;">
	      <legend><a name="compatibility" style="font-size: 14px;">该栏目是否可在模版中用动态栏目代码调取出来</a></legend>
	    </fieldset>
    	<div class="layui-form-item">
			<label class="layui-form-label" id="templateCodeColumnUsed_label" >子栏目列表</label>
			<div class="layui-input-block">
				<script type="text/javascript">writeSelectAllOptionFortemplateCodeColumnUsed_('${siteColumn.templateCodeColumnUsed }','请选择', true);</script>
			</div>
		</div>
		
		
		<fieldset class="layui-elem-field layui-field-title site-title" style="margin-bottom: 10px;">
	      <legend><a name="compatibility" style="font-size: 14px;">是否在内容管理中显示这个栏目</a></legend>
	    </fieldset>
    	<div class="layui-form-item">
			<label class="layui-form-label" id="adminNewsUsed_label" >内容管理中</label>
			<div class="layui-input-block">
				<script type="text/javascript">writeSelectAllOptionForadminNewsUsed_('${siteColumn.adminNewsUsed }','请选择', true);</script>
			</div>
		</div>
		
    	
    	<fieldset class="layui-elem-field layui-field-title site-title" style="margin-bottom: 10px;">
	      <legend><a name="compatibility" style="font-size: 14px;">下面这个已废弃</a></legend>
	    </fieldset>
	    <div class="layui-form-item">
			<label class="layui-form-label" id="xianshi_label"><s>是否显示</s></label>
			<div class="layui-input-block">
				<script type="text/javascript">writeSelectAllOptionForused_('${siteColumn.used }','请选择', true);</script>
				<div class="explain">注意，这个已经废弃！您可使用上面几个来控制</div>
			</div>
		</div>
    </div>
    
    <!-- SEO -->
    <div class="layui-tab-item">
    	<div class="layui-form-item">
			<label class="layui-form-label" id="keywords_label">Keywords</label>
			<div class="layui-input-block">
				<textarea name="keywords" lay-verify="keywords" autocomplete="off" placeholder="限50个字符以内，多个用,分割" class="layui-textarea">${siteColumn.keywords }</textarea>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" id="description_label">Description</label>
			<div class="layui-input-block">
				<textarea name="description" lay-verify="description" autocomplete="off" placeholder="限200个字符以内" class="layui-textarea">${siteColumn.description }</textarea>
			</div>
		</div>
    </div>
    
    <div class="layui-tab-item">
    	<!-- 高级方式 -->
		<div class="layui-form-item" id="listRank">
			<label class="layui-form-label" id="listRank_label">信息排序</label>
			<div class="layui-input-block">
				<script type="text/javascript">writeSelectAllOptionForlistRank_('${siteColumn.listRank }','请选择', false);</script>
			</div>
		</div>
		<div class="layui-form-item" id="useGenerateView_div">
			<label class="layui-form-label" id="useGenerateView_label">生成内容页</label>
			<div class="layui-input-block">
				<script type="text/javascript">writeSelectAllOptionForuseGenerateView_('${siteColumn.useGenerateView }','请选择', true);</script>
			</div>
		</div>
    	
		<!-- 标题图片、封面图片。若是使用，可以在 栏目管理 中，编辑栏目时，有个 信息录入的选项卡，找到 标题图片，点击 使用 即可。若是自己添加的输入模型，请保留 id="sitecolumn_editUseTitlepic" ,不然栏目设置中的是否使用图集功能将会失效！ -->
		<div class="layui-form-item" id="icon_div">
			<label class="layui-form-label" id="label_columnName">栏目图片</label>
			<div class="layui-input-block">
				<input name="icon" id="titlePicInput" type="text" autocomplete="off" placeholder="点击右侧添加" class="layui-input" value="${siteColumn.icon }" style="padding-right: 120px;">
				<button type="button" class="layui-btn" id="uploadImagesButton" style="float: right;margin-top: -38px;">
					<i class="layui-icon layui-icon-upload"></i>
				</button>
				<a href="${icon }" id="titlePicA" style="float: right;margin-top: -38px;margin-right: 60px;" title="预览原始图片" target="_black">
					<img id="titlePicImg" src="${icon }?x-oss-process=image/resize,h_38" onerror="this.style.display='none';" style="height: 36px;max-width: 57px; padding-top: 1px;" alt="预览原始图片">
				</a><input class="layui-upload-file" type="file" name="fileName">
			</div>
		</div>
    </div>
    
  </div>
</div>

	
	<div class="layui-form-item" style="padding-top:15px;">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit="" lay-filter="demo1">保存修改</button>
			<button type="reset" class="layui-btn layui-btn-primary">重置</button>
		</div>
	</div>
	
	<div style="padding:10px;color:#e2e2e2; padding-bottom: 2px; width:100%; text-align:center; box-sizing: border-box;">
		提示：鼠标放到左侧描述,可显示当前说明
	</div>
</form>

<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

layui.use(['form', 'layedit', 'laydate', 'element'], function(){
  var form = layui.form;

  //自定义验证规则
  form.verify({
    name: function(value){
      if(value.length == 0 || value.length > 40){
        return '请输入40个字以内的栏目名称';
      }
    },
    type: function(value){
      if(value=='' || value.length ==0){
        return '请选择栏目类型';
      }
    },
    keywords: function(value){
      if(value.length > 50){
        return 'SEO 下的 keywords限制不可超过50字符';
      }
    },
    description: function(value){
      if(value.length > 200){
        return 'SEO 下的 description限制不可超过200字符';
      }
    },
  });
  
	//当类型发生变动改变
	form.on('select(type)', function (data) {
		selectTypeChange();
	});
	
	//当编辑方式发生变动改变
	form.on('select(editMode)', function (data) {
		selectEditMode();
	});
  
  //监听提交
  form.on('submit(demo1)', function(data){
  		parent.parent.msg.loading('保存中');
		var d=$("form").serialize();
        $.post("/column/savePopupColumnGaoJiUpdate.do", d, function (result) { 
        	parent.parent.msg.close();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.parent.msg.success("操作成功");
        		parent.location.reload();	//刷新父窗口
        		parent.layer.close(index);
        	}else if(obj.result == '0'){
        		msg.failure(obj.info);
        	}else{
        		msg.failure(result);
        	}
         }, "text");
		
    return false;
  });
  
});


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



//鼠标跟随提示
$(function(){
	//栏目名称
	var label_columnName_index = 0;
	$("#label_columnName").hover(function(){
		label_columnName_index = layer.tips('给当前栏目起个名字吧。这里的名字可以直接在网站里调出显示，也可以在内容管理中显示栏目的名字。', '#label_columnName', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(label_columnName_index);
	})

	//列表页模版
	var listTemplate_index = 0;
	$("#listTemplate").hover(function(){
		listTemplate_index = layer.tips('当前栏目信息展示给用户看时，的列表页面模版<br/>比如，新闻列表、产品列表、成功案例列表等此类信息列表。<br/>若这里为空，那肯定就是还没有创建过列表页模版了，先去<b>模版页面</b>去创建一个列表页模版吧', '#listTemplate', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(listTemplate_index);
	})
	
	//编辑方式
	var columnEditMode_index = 0;
	$("#columnEditMode").hover(function(){
		columnEditMode_index = layer.tips('编辑方式，设定其填充/编辑内容信息的编辑方式。<br/>1.&nbsp;<b>在内容管理中编辑</b>，系统默认的内容编辑，比如添加新闻、图文时，都可以使用这个。若对此详不是太了解，一律选择此项即可。<br/>2.&nbsp;<b>在模版页面中编辑</b>，直接编辑模版页面的方式进行编辑，同样，编辑的工具是模版页，也就是直接对模版页面进行编辑。<br/><b style="font-size:16px; padding:6px;">注意，若是不理解，请勿改动本项。</b>', '#columnEditMode', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(columnEditMode_index);
	})
	
	//内容页模版
	var viewTemplate_index = 0;
	$("#viewTemplate").hover(function(){
		viewTemplate_index = layer.tips('当前栏目信息展示给用户看时，的内容页面模版<br/>比如，新闻详情、产品详情、公司介绍、联系我们等页面，都是单独的一个内容介绍页面<br/>若这里为空，那肯定就是还没有创建过内容页模版了，先去<b>模版页面</b>去创建一个内容页模版吧', '#viewTemplate', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(viewTemplate_index);
	})
	
	//栏目类型
	var columnType_index = 0;
	$("#columnType").hover(function(){
		//这里通常使用的有这么三种：<br/><b>新闻信息</b>：像是新闻列表、动态资讯，这种纯文字性质的列表，并点击某项后可进入查看详情<br/><b>图文信息</b>：像是产品展示、案例展示等，图片＋文字形式的列表，并点击项后进入查看详情<br/><b>独立页面</b>：像是公司简介、联系我们、招商加盟这种单独的页面
		columnType_index = layer.tips('<b>信息列表</b>：像是新闻列表、动态资讯、产品展示等，这种信息的列表。这个栏目内的文章条数不固定，用户可以随意添加多条信息。<br/><b>独立页面</b>：像是公司简介、联系我们这种的，一个栏目就只有一个页面的', '#columnType', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(columnType_index);
	})
	
	//栏目代码
	var columnCode_index = 0;
	$("#columnCode").hover(function(){
		columnCode_index = layer.tips('1.&nbsp;网站中，每个栏目都有一个唯一的栏目代码，在模版中动态调用某个栏目、或某个栏目下的动态数据时，就是根据这个栏目的栏目代码来进行调取的。<br/>2.&nbsp;此处强烈建议使用英文或拼音，禁止特殊字符<br/>3.&nbsp;<b>提示：生成的页面就是以这里命名的。填的栏目代码.html</b>。例如，你的栏目代码设置为aboutus，那么你这个页面的地址便是你的域名/aboutus.html<br/>4.&nbsp;栏目代码设置好后最好不要再改动！模版页面、模版栏目中动态调用栏目就是使用的这个；还有栏目的备份还原也是使用的栏目代码！', '#columnCode', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(columnCode_index);
	})
	
	
	//上级栏目
	var parentColumnCode_index = 0;
	$("#parentColumnCode_label").hover(function(){
		parentColumnCode_index = layer.tips('您当前操作的栏目，是属于哪个栏目下的。<br/>若选顶级栏目，则此栏目不属于任何栏目，是顶级栏目<br/><b>注意，模版中动态调取数据只支持一级子栏目</b>', '#parentColumnCode_label', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(parentColumnCode_index);
	})
	
	//输入模型
	var inputModel_index = 0;
	$("#inputModel_label").hover(function(){
		inputModel_index = layer.tips('本栏目建立好后，在<b>内容管理</b>中操作本栏目的具体数据时，数据录入的样式。<br/>可在<b>模板管理</b>-<b>输入模型</b>中进行修改<b><br/><b style="font-size:16px; padding:6px;">注意，若是不理解，请勿改动本项。</b>', '#inputModel_label', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(inputModel_index);
	})
	
	//列表条数，列表页面的显示条数
	var listnum_index = 0;
	$("#listnum_label").hover(function(){
		listnum_index = layer.tips('当前列表页面，每个列表页显示多少条数据。<br/>如新闻列表，再生成后，用户查看新闻列表时，每页显示多少条新闻', '#listnum_label', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(listnum_index);
	})
	
	//显示
	var xianshi_index = 0;
	$("#xianshi_label").hover(function(){
		xianshi_index = layer.tips('绝大多数时候，都是显示即可，显示即正常使用。<br/>若是隐藏，在 内容管理 中不会显示此栏目、另外在模版页面中使用动态栏目代码调取子栏目列表时，也不会调取到', '#xianshi_label', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(xianshi_index);
	})
	
	//listRank ，信息排序
	var listRank_index = 0;
	$("#listRank_label").hover(function(){
		listRank_index = layer.tips('当前栏目内，内容信息排序方式。有以下两种：<br>1.&nbsp;<b>发布时间倒序</b>：发布时间越晚，排序越靠前。<br/>2.&nbsp;<b>发布时间正序</b>：发布时间越早，排序越靠前', '#listRank_label', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(listRank_index);
	})
	
	
	//内容管理中，是否使用标题图片
	var label_editUseTitlepic_index = 0;
	$("#label_editUseTitlepic").hover(function(){
		label_editUseTitlepic_index = layer.tips('标题图片，也就是文章的封面图，让每篇文章都有自己的一个封面图。比如，这个封面图可以是网站中，产品列表页面的每个产品的图片。', '#label_editUseTitlepic', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(label_editUseTitlepic_index);
	})
	
	
	//内容管理中，是否使用图集
	var label_editUseExtendPhotos_index = 0;
	$("#label_editUseExtendPhotos").hover(function(){
		label_editUseExtendPhotos_index = layer.tips('文章图集，也就是让每篇文章都有自己的图集，一篇文章里面，可以上传不固定数量的多张图片，可以在网站中，做出轮播图等效果', '#label_editUseExtendPhotos', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(label_editUseExtendPhotos_index);
	})
	
	//内容管理中，是否使用内容简介
	var label_editUseIntro_index = 0;
	$("#label_editUseIntro").hover(function(){
		label_editUseIntro_index = layer.tips('内容简介，也就是让本栏目中的每篇文章都可以编辑自己的内容简介。这些简介文字，可以在网站中，进行调取出来，比如在新闻列表中，作为新闻的简介说明。<br/>如果设置为不显示，内容简介也会有信息，会自动从内容正文中截取前120个字符作为内容简介的信息。', '#label_editUseIntro', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(label_editUseIntro_index);
	})
	
	//内容管理中，是否使用内容正文
	var label_editUseText_index = 0;
	$("#label_editUseText").hover(function(){
		label_editUseText_index = layer.tips('内容正文，也就是文章的正文、详情。也就是富文本编辑器UEditor编辑的区域，可以通过这里进行随意布局、上传图片、附件、表情、以及插入地图等！', '#label_editUseText', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(label_editUseText_index);
	})
	
	//是否生成内容详情页面
	var useGenerateView_label_index = 0;
	$("#useGenerateView_label").hover(function(){
		useGenerateView_label_index = layer.tips('是否生成内容详情页面？<br/>如果您这个栏目只是要做一个列表，不做点击进入的详情页面的话，即无内容详情页，也就是此处可以设置为不生成内容页面，可以提高生成整站的速度。<br/><b>注意，若是不懂，请勿改动此处！</b>', '#useGenerateView_label', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(useGenerateView_label_index);
	})
	
	//是否在模版栏目列表中显示栏目
	var templateCodeColumnUsed_label_index = 0;
	$("#templateCodeColumnUsed_label").hover(function(){
		templateCodeColumnUsed_label_index = layer.tips('是否在模版调用中显示（调取子栏目列表）。在模板中，使用动态栏目调用代码调取栏目列表时，是否会调取到此栏目。<br/>例如顶级栏目名为 "手机" ，其下有三个子栏目，分别为小米、魅族、中兴，如果这个栏目是“魅族”，那么设置此处为隐藏后，调取“手机”这个栏目下的所有子栏目列表时，就只有小米、中兴<br/><b>注意，若是不懂，请勿改动此处！</b>', '#templateCodeColumnUsed_label', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(templateCodeColumnUsed_label_index);
	})
	
	var adminNewsUsed_label_index = 0;
	$("#adminNewsUsed_label").hover(function(){
		adminNewsUsed_label_index = layer.tips('是否在内容管理中显示此栏目。<br/><b>注意，若是不懂，请勿改动此处！</b>', '#adminNewsUsed_label', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(adminNewsUsed_label_index);
	})
	
	var keywords_label_index = 0;
	$("#keywords_label").hover(function(){
		keywords_label_index = layer.tips('SEO 的 keywords ，多个中间用,分割<br/>在模板中可以用栏目标签 {siteColumn.keywords} 调取。<br/><b>注意，最大限制50个字符</b>', '#keywords_label', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(keywords_label_index);
	})
	
	var description_label_index = 0;
	$("#description_label").hover(function(){
		description_label_index = layer.tips('SEO 的 description <br/>在模板中可以用栏目标签 {siteColumn.description} 调取。<br/><b>注意，最大限制200个字符</b>', '#description_label', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(description_label_index);
	})
	
	
	//是否在模版的文章列表中显示此栏目的文章
	//var templateCodeNewsUsed_label_index = 0;
	//$("#templateCodeNewsUsed_label").hover(function(){
	//	templateCodeNewsUsed_label_index = layer.tips('是否在模版调用中显示（调取文章列表）。在模板中，使用动态栏目调用代码调取某个父栏目下的所有文章列表时（调取父栏目下所有子栏目的文章列表），这个栏目(当这个栏目是一个子栏目时)的文章列表是否要一并调取出来。<br/>例如顶级栏目名为 "手机" ，其下有三个子栏目，分别为小米、魅族、中兴，如果这个栏目是“魅族”，那么设置此处为隐藏后，调取“手机”这个栏目下的所有文章列表时，就只有小米、中兴这两个栏目的文章<br/><b>注意，若是不懂，请勿改动此处！</b>', '#templateCodeNewsUsed_label', {
	//		tips: [2, '#0FA6A8'], //还可配置颜色
	//		time:0,
	//		tipsMore: true,
	//		area : ['310px' , 'auto']
	//	});
	//},function(){
	//	layer.close(templateCodeNewsUsed_label_index);
	//})
	
	
	

});	

//当类型改变后，相应的自定义网址也会显示或者隐藏、模版也会相应显示或者隐藏
function selectTypeChange(){
	document.getElementById("xnx3_viewTemplate").style.display="none";	//内容模版
	document.getElementById("xnx3_listTemplate").style.display="none";	//列表模版
	document.getElementById("listnum").style.display="none";				//列表页面每页显示多少条
	
	if(document.getElementById("type").options[1].selected){
		//列表页面（新闻、图文等）
		document.getElementById("xnx3_viewTemplate").style.display="";
		document.getElementById("xnx3_listTemplate").style.display="";
		document.getElementById("listnum").style.display="";
		document.getElementById("xnx3_editMode").style.display="none";
		document.getElementById("listRank").style.display="";
		document.getElementById("useGenerateView_div").style.display="";
	}else if(document.getElementById("type").options[2].selected){
		//独立页面
		document.getElementById("xnx3_viewTemplate").style.display="";
		document.getElementById("xnx3_editMode").style.display="";
		document.getElementById("listRank").style.display="none";
		document.getElementById("useGenerateView_div").style.display="none";
	}
}
selectTypeChange();

//当编辑方式放生改变
function selectEditMode(){
	if(document.getElementById("editMode").options[2].selected){
		//模板式编辑，将不显示输入模型、以及图集、内容等输入选项
		$(".neirongguanli_shuru").hide();
	}else{
		$(".neirongguanli_shuru").show();
	}
}
selectEditMode();

</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>