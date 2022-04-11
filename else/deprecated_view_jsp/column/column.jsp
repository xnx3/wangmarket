<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="栏目管理"/>
</jsp:include>

<script src="/<%=Global.CACHE_FILE %>SiteColumn_used.js"></script>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_type.js"></script>

<form id="form" method="post" enctype="multipart/form-data" action="" class="layui-form" style="padding:20px; padding-top:35px;">
	<input type="hidden" name="id" value="${siteColumn.id }" />
  <div class="layui-form-item">
    <label class="layui-form-label" id="label_columnName">栏目名称</label>
    <div class="layui-input-block">
      <input type="text" name="name" required autocomplete="off" placeholder="(20个汉字之内，必填)" class="layui-input" value="${siteColumn.name }">
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label" id="columnIconFile">图标</label>
    <div class="layui-input-inline">
		<input type="file" id="iconFile" name="iconFile" class="layui-input" style="padding-top: 6px;" />
    </div>
    <style>
    	.oldImage{
    		margin-top: 0px;
    	}
    	@media screen and (max-width: 450px) {
		    .oldImage{
	    		margin-top: -43px;
	    		float: right;
	    		position: relative;
	    		margin-right: 9px;
    		}
		}
    </style>
    <div style="" class="oldImage">${iconImage }</div>
  </div>
    <div class="layui-form-item">
    <label class="layui-form-label" id="columnType">栏目类型</label>
    <div class="layui-input-block">
    	<script type="text/javascript">writeSelectAllOptionFortype_('${siteColumn['type'] }','请选择', 1);</script>
    </div>
  </div>
    <div class="layui-form-item" id="xnx3_url">
    <label class="layui-form-label">网址</label>
    <div class="layui-input-block">
      <input type="text" name="url" autocomplete="off" placeholder="请输入目标网页链接地址" class="layui-input" value="${siteColumn.url }">
    </div>
  </div>
    <div class="layui-form-item">
    <label class="layui-form-label" id="xianshi_label">是否显示</label>
    <div class="layui-input-block">
    	<script type="text/javascript">writeSelectAllOptionForused_('${siteColumn.used }','请选择', 1);</script>
    </div>
  </div>
  
  <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn" lay-submit="" lay-filter="demo1">保存修改</button>
      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
    </div>
  </div>
</form>

<script type="text/javascript">
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

//当select type发生变化
function selectTypeChange(){
	if(document.getElementById("type").options[4].selected){
		document.getElementById("xnx3_url").style.display="";
	}else{
		document.getElementById("xnx3_url").style.display="none";
	}
}
selectTypeChange();

layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form;
  
	//当类型发生变动改变
	form.on('select(type)', function (data) {
		selectTypeChange();
	});
	
	form.on('submit(demo1)', function(data){
		parent.msg.loading('保存中');
	
		//创建FormData对象，获取到form表单的相关数据
		var formobj =  document.getElementById("form");
		var data = new FormData(formobj);
	
	    //为FormData对象添加上传图片的数据
	    $.each($('#iconFile')[0].files, function(i, file) {
	        data.append('image', file);
	    });
		$.ajax({
			url:'/column/saveColumn.do',
	        type:'POST',
	        data:data,
	        cache: false,
	        contentType: false,    //不可缺
	        processData: false,    //不可缺
	        success:function(data){
	        	parent.msg.close();
	            if(data.result=='0'){
	            	alert(data.info);
	            }else{
	            	//上传成功
	            	parent.msg.success('上传成功');
	            	parent.location.reload();	//刷新父窗口
        			parent.layer.close(index);
	            }
	        },
	        error:function(){
	        	alert('上传出错！');
	        }
		});
		
		return false;
	});
});


//鼠标跟随提示
$(function(){
	//栏目名称
	var label_columnName_index = 0;
	$("#label_columnName").hover(function(){
		label_columnName_index = layer.tips('给当前栏目起个名字吧。这里的名字可以直接在网站里调出显示，也可以在内容管理中显示栏目的名字。', '#label_columnName', {
			tips: [4, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['278px' , 'auto']
		});
	},function(){
		layer.close(label_columnName_index);
	})

	
	//栏目类型
	var columnType_index = 0;
	$("#columnType").hover(function(){
		columnType_index = layer.tips('这里通常使用的有这么几种：<br/><b>新闻信息</b>：像是新闻列表、动态资讯，这种纯文字性质的列表，并点击某项后可进入查看详情<br/><b>图文信息</b>：像是产品展示、案例展示等，图片＋文字形式的列表，并点击项后进入查看详情<br/><b>独立页面</b>：像是公司简介、联系我们、招商加盟这种单独的页面<br/><b>超链接</b>：点击后会跳转到其他网站，比如点击此栏目后会跳转到您的淘宝店铺上', '#columnType', {
			tips: [4, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['278px' , 'auto']
		});
	},function(){
		layer.close(columnType_index);
	})
	
	//显示
	var xianshi_index = 0;
	$("#xianshi_label").hover(function(){
		xianshi_index = layer.tips('此处选择显示即可！若选择隐藏，则不会在网站上显示。相当于没有此栏目', '#xianshi_label', {
			tips: [4, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['278px' , 'auto']
		});
	},function(){
		layer.close(xianshi_index);
	})
	
	//图标
	var columnIconFile_index = 0;
	$("#columnIconFile").hover(function(){
		columnIconFile_index = layer.tips('此栏目在首页上显示时，栏目名字上面的图片，便是在这里进行上传。建议上传正方形的图片', '#columnIconFile', {
			tips: [4, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['278px' , 'auto']
		});
	},function(){
		layer.close(columnIconFile_index);
	})
	
	
});	

</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>