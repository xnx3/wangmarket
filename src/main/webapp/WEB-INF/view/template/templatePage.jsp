<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="编辑模版页面"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>TemplatePage_type.js"></script>
<script src="/<%=Global.CACHE_FILE %>TemplatePage_editMode.js"></script>

<form id="form" method="post" class="layui-form" style="padding:20px; padding-top:35px; margin-bottom: 0px; padding-bottom:0px;">
	<input type="hidden" name="id" value="${templatePage.id}" />
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_type">所属类型</label>
		<div class="layui-input-block">
			<script type="text/javascript">writeSelectAllOptionFortype_('${templatePage.type}', '请选择', true);</script>
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_name">模版页名称</label>
		<div class="layui-input-block">
			<input type="text" name="name" lay-verify="name" autocomplete="off" placeholder="限20个字符以内" class="layui-input" value="${templatePage.name }">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_editMode">编辑方式</label>
		<div class="layui-input-block">
			<script type="text/javascript">writeSelectAllOptionForeditMode_('${templatePage.editMode}', '请选择', true);</script>
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_remark">模版页备注</label>
		<div class="layui-input-block">
			<input type="text" name="remark" lay-verify="remark" autocomplete="off" placeholder="限30个字符以内" class="layui-input" value="${templatePage.remark }">
		</div>
	</div>
	
	<div class="layui-form-item" style="padding-top:15px;">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit="" lay-filter="demo1">保存</button>
			<button type="reset" class="layui-btn layui-btn-primary">重置</button>
		</div>
	</div>
	<div style="padding:10px; color:#e2e2e2; padding-bottom: 15px;">
		提示：鼠标放到左侧描述,可显示当前说明
	</div>
</form>


<script>
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form;
 
  //自定义验证规则
  form.verify({
    name: function(value){
      if(value.length == 0){
        return '请输入页面的名字';
      }
      if(value.length > 20){
      	return '请输入20个字以内的变量名字';
      }
    },
    type: function(value){
      if(value.length == 0){
        return '请选择当前模版页类型';
      }
    },
    editMode: function(value){
      if(value.length == 0){
        return '请选择模版页面的编辑方式';
      }
    },
    remark: function(value){
      if(value.length > 30){
      	return '请输入30个字以内的对当前模版页的备注';
      }
    },
  });
  
  //监听提交
  form.on('submit(demo1)', function(data){
		parent.msg.loading('保存中');
		var d=$("form").serialize();
        $.post("/template/saveTemplatePage.do", d, function (result) { 
        	parent.msg.close();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.parent.msg.success("保存成功");
       			parent.location.reload();	//刷新父窗口
       			parent.layer.close(index);
        	}else if(obj.result == '0'){
        		parent.parent.msg.failure(obj.info);
        	}else{
        		parent.parent.msg.failure(result);
        	}
         }, "text");
		
    return false;
  });
  
});


//鼠标跟随提示
$(function(){
	//类型
	var label_type_index = 0;
	$("#label_type").hover(function(){
		label_type_index = layer.tips('首页模版只能存在一个！<br/>多个就不起作用了！', '#label_type', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['230px' , 'auto']
		});
	},function(){
		layer.close(label_type_index);
	})
	
	//名字
	var label_name_index = 0;
	$("#label_name").hover(function(){
		label_name_index = layer.tips('<ol style="list-style-type:demical"><li style="list-style-position:outside;">模版页面的名字，请用用英文、数字、"_" 来命名！</li><li style="list-style-position:outside;"><b>添加后最好不要对其进行修改了！</b>栏目绑定的模版、备份还原模版页时，都是依据此处进行操作。</li></ol>', '#label_name', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['230px' , 'auto']
		});
	},function(){
		layer.close(label_name_index);
	})
	
	//备注
	var label_remark_index = 0;
	$("#label_remark").hover(function(){
		label_remark_index = layer.tips('给网站后台操作人员备注提示说明，便于区分模版页面，仅此而已，无实际作用', '#label_remark', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['230px' , 'auto']
		});
	},function(){
		layer.close(label_remark_index);
	})
	
	//编辑方式
	var label_editMode_index = 0;
	$("#label_editMode").hover(function(){
		label_editMode_index = layer.tips('模版页面的内容编辑方式：<br/><b>可视化编辑</b>：也就是智能模式，想改图片，右键修改-上传；想改文字，鼠标点击直接输入。<br/><b>纯代码编辑</b>：纯代码编辑，同传统的帝国CMS、织梦CMS的textarea文本框编辑。如果您之前使用过帝国、织梦，您可先用此种方式。另外，如果网站js效果很多，建议使用代码模式。', '#label_editMode', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['230px' , 'auto']
		});
	},function(){
		layer.close(label_editMode_index);
	})
	
});	


</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>