<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@page import="com.xnx3.DateUtil"%><%@page import="com.xnx3.j2ee.Global"%><%@page import="com.xnx3.admin.G"%><%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../iw/common/head.jsp">
    <jsp:param name="title" value="编辑栏目"/>
</jsp:include>

<script src="<%=basePath+Global.CACHE_FILE %>SiteColumn_used.js"></script>
<script src="<%=basePath+Global.CACHE_FILE %>SiteColumn_type.js"></script>
<script src="<%=basePath+Global.CACHE_FILE %>SiteColumn_editMode.js"></script>


<form id="form" method="post" class="layui-form" style="padding:20px; padding-top:35px; margin-bottom: 10px;">
	<input type="hidden" value="${siteColumn.id }" name="id">
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_columnName">栏目名称</label>
		<div class="layui-input-block">
			<input type="text" name="name" lay-verify="name" autocomplete="off" placeholder="限8个汉字以内" class="layui-input" value="${siteColumn.name }">
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
			<script type="text/javascript">writeSelectAllOptionFortype_('${siteColumn.type }','请选择', true);</script>
		</div>
	</div>
	
	<!-- 内容编辑方式，当独立页面时才会有效，才会显示。选择是使用内容富文本编辑框编辑，还是使用模板的方式编辑 -->
	<div class="layui-form-item" id="xnx3_editMode">
		<label class="layui-form-label" id="columnEditMode">编辑方式</label>
		<div class="layui-input-block">
			<script type="text/javascript">writeSelectAllOptionForeditMode_('${siteColumn.editMode }','', true);</script>
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

	<div class="layui-form-item" id="xnx3_url">
		<label class="layui-form-label">链接网址</label>
		<div class="layui-input-block">
			<input type="text" name="url" autocomplete="off" placeholder="请输入目标网页链接地址" class="layui-input" value="${siteColumn.url }">
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
		
	<div class="layui-form-item" id="inputModel">
		<label class="layui-form-label" id="inputModel_label">输入模型</label>
		<div class="layui-input-block">
			<select name="inputModelCodeName" id="inputModelCodeName">
				${inputModelOptions }
			</select>
		</div>
	</div>
	
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="xianshi_label">是否显示</label>
		<div class="layui-input-block">
			<script type="text/javascript">writeSelectAllOptionForused_('${siteColumn.used }','请选择', true);</script>
		</div>
	</div>
	
	
	<div class="layui-form-item" style="padding-top:15px;">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit="" lay-filter="demo1">保存修改</button>
			<button type="reset" class="layui-btn layui-btn-primary">重置</button>
		</div>
	</div>
	
	<div style="padding:10px;color:#e2e2e2; padding-bottom: 2px; width:100%; text-align:center;">
		提示：鼠标放到左侧描述,可显示当前说明
	</div>
</form>

<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form;

  //自定义验证规则
  form.verify({
    name: function(value){
      if(value.length == 0 || value.length > 20){
        return '请输入20个字以内的栏目名称';
      }
    }
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
  		parent.iw.loading('保存中');
		var d=$("form").serialize();
        $.post("<%=basePath %>column/savePopupColumnGaoJiUpdate.do", d, function (result) { 
        	parent.iw.loadClose();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.parent.iw.msgSuccess("操作成功");
        		parent.location.reload();	//刷新父窗口
        		parent.layer.close(index);
        	}else if(obj.result == '0'){
        		parent.layer.msg(obj.info, {shade: 0.3})
        	}else{
        		parent.layer.msg(result, {shade: 0.3})
        	}
         }, "text");
		
    return false;
  });
  
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
		columnEditMode_index = layer.tips('编辑方式，设定其填充内容数据的编辑方式。<br/>1.&nbsp;图文编辑框，系统默认的内容编辑，比如添加新闻、图文时，都可以使用这个。若对此详不是太了解，一律选择此项即可。<br/>2.&nbsp;模版式编辑，直接调取编辑模版页面的方式进行编辑，同样，编辑的工具是模版页，也就是直接对模版页面进行编辑。这种编辑方式，通常其模版页面是只使用了一次的，栏目类型为独立页面', '#columnEditMode', {
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
		columnType_index = layer.tips('这里通常使用的有这么三种：<br/><b>新闻信息</b>：像是新闻列表、动态资讯，这种纯文字性质的列表，并点击某项后可进入查看详情<br/><b>图文信息</b>：像是产品展示、案例展示等，图片＋文字形式的列表，并点击项后进入查看详情<br/><b>独立页面</b>：像是公司简介、联系我们、招商加盟这种单独的页面', '#columnType', {
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
		inputModel_index = layer.tips('本栏目建立好后，在<b>内容管理</b>中操作本栏目的具体数据时，数据录入的样式。<br/>可在<b>模板管理</b>-<b>输入模型</b>中进行修改<b><br/>注意，不懂此处的建议不要随意修改</b>', '#inputModel_label', {
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
		xianshi_index = layer.tips('此处选择显示即可！', '#xianshi_label', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(xianshi_index);
	})
	
});	

//当类型改变后，相应的自定义网址也会显示或者隐藏、模版也会相应显示或者隐藏
function selectTypeChange(){
	document.getElementById("xnx3_viewTemplate").style.display="none";	//内容模版
	document.getElementById("xnx3_listTemplate").style.display="none";	//列表模版
	document.getElementById("xnx3_url").style.display="none";			//网址
	document.getElementById("listnum").style.display="none";				//列表页面每页显示多少条
	
	if(document.getElementById("type").options[1].selected || document.getElementById("type").options[2].selected){
		//新闻、图文
		document.getElementById("xnx3_viewTemplate").style.display="";
		document.getElementById("xnx3_listTemplate").style.display="";
		document.getElementById("listnum").style.display="";
		document.getElementById("xnx3_editMode").style.display="none";
	}else if(document.getElementById("type").options[3].selected){
		//独立页面
		document.getElementById("xnx3_viewTemplate").style.display="";
		document.getElementById("xnx3_editMode").style.display="";
	}else{
		//url
		document.getElementById("xnx3_url").style.display="";
		document.getElementById("xnx3_editMode").style.display="none";
	}
}
selectTypeChange();

//当编辑方式放生改变
function selectEditMode(){
	if(document.getElementById("editMode").options[2].selected){
		//模板式编辑，将不显示输入模型
		document.getElementById("inputModel").style.display="none";
	}else{
		document.getElementById("inputModel").style.display="";
	}
}
selectEditMode();

</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>