<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>

<link rel="stylesheet" type="text/css" href="http://apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="http://cdn.bootcss.com/font-awesome/4.6.0/css/font-awesome.min.css">
<jsp:include page="../../../iw/common/head.jsp">
	<jsp:param name="title" value="上传文件"/>
</jsp:include>
<style type="text/css">

.hovereffect {
overflow:hidden;
position:relative;
text-align:center;
cursor:default;
border: 1px solid #ddd;
float: left;
padding: 10px;
margin: 5px;
background: #FFF;
}

.hovereffect .overlay {
width:100%;
height:100%;
position:absolute;
overflow:hidden;
top:0;
left:0;
opacity:0;
background-color:rgba(0,0,0,0.5);
-webkit-transition:all .4s ease-in-out;
transition:all .4s ease-in-out
}

.hovereffect img {
display:block;
position:relative;
-webkit-transition:all .4s linear;
transition:all .4s linear;
}


.hovereffect a.info {
text-decoration:none;
display:inline-block;
text-transform:uppercase;
color:#fff;
border:1px solid #fff;
background-color:transparent;
opacity:0;
filter:alpha(opacity=0);
-webkit-transition:all .2s ease-in-out;
transition:all .2s ease-in-out;
margin:50px 0 0;
padding:7px 14px;
}

.hovereffect a.info:hover {
box-shadow:0 0 5px #fff;
}

.hovereffect:hover img {
-ms-transform:scale(1.2);
-webkit-transform:scale(1.2);
transform:scale(1.2);
}

.hovereffect:hover .overlay {
opacity:1;
filter:alpha(opacity=100);
}

.hovereffect:hover a.info {
opacity:1;
filter:alpha(opacity=100);
-ms-transform:translatey(0);
-webkit-transform:translatey(0);
transform:translatey(0);
}

.hovereffect:hover a.info {
-webkit-transition-delay:.2s;
transition-delay:.2s;
}

.col-lg-3, .col-md-4, .col-sm-6, .col-xs-12{
padding: 0px;
margin: 0px;
width: auto;
}

/* 找不到图片时去除默认边框 */
/* img[src=""],img:not([src]) {
opacity: 0;
} */

</style>
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
 	<div style="padding:10px">
	 	<p style="width: 100%;text-align: center;">
		<button class="layui-btn" id="uploadFile" lay-submit lay-filter="formSubmit">上传插件</button>
		</p>
	</div>
<script>


//自适应弹出层大小
/* var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);
layui.use('element', function(){
	var element = layui.element;
}); */

//上传学校头像的点击事件 
layui.use('upload', function(){
	layui.upload.render({
	  url: '/plugin/pluginManage/uploadZip.do'
	  ,data: {"plugin_id" : '${plugin_id }'}
	  ,method :'post'
	  ,elem : '#uploadFile'
	  ,exts: 'zip'
	  ,field: 'file'
	  ,title :'上传插件'
	  ,size: '${maxFileSizeKB}'	//50MB ，这里单位是KB
      , before: function (obj) {
          parent.msg.loading("上传中");
      }
	  ,done: function(res, index, upload){
	  	parent.msg.close();
	    //上传成功返回值，必须为json格式
	    if(res.result == '1'){
	    	parent.msg.success("上传成功！");
	    	parent.location.reload();	//刷新父窗口列表
	    }else{
	    	parent.msg.failure(data.info);
	    }
	  }
	}); 
});

</script>

<jsp:include page="../../../iw/common/foot.jsp"></jsp:include>