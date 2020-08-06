<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="选择栏目"/>
</jsp:include>

<style>
.body{
	margin: 0;padding: 0px;height: 100%;overflow: hidden;
}
.menu{
	width:160px;
	height:100%;
	background-color: #EAEDF1;
	position: absolute;
}

/* 左侧栏目列表的文字 */
.layui-nav-tree .layui-nav-item a{
	color:#333;
}

/*鼠标移动到某项后的样式*/
.layui-nav-tree .layui-nav-item a:hover:HOVER{
	background-color: #f4f6f8;
	color:#222;
}

/*子栏目*/
.layui-nav-tree .layui-nav-item dl dd a{
	padding-left:35px;
}

.dltitle{
	background-color: #EAEDF1;
}

.layui-nav-itemed>a, .layui-nav-tree .layui-nav-title a, .layui-nav-tree .layui-nav-title a:hover {
    background-color: #EAEDF1!important;
    color: #222!important;
}

.table>thead>tr>th, .table>tbody>tr>th, .table>tfoot>tr>th{
	line-height: 2.2;
	text-align:center;
}

.layui-nav-child dd{
	background-color: #EAEDF1;
}

.layui-nav .layui-nav-item .layui-nav-more{
	border-top-color: rgba(64, 34, 34, 0.7);
}
/*有二级栏目的，二级栏目伸缩的时候，右侧的小尖头的颜色*/
.layui-nav .layui-nav-itemed .layui-nav-more{
	border-top-color: rgba(64, 34, 34, 0);
	border-color: transparent transparent #a42828;
}
    
</style>

<table class="layui-table" style="margin-top:-1px;">
	<tbody>
		${columnTreeNav }
	</tbody>
</table>
<div style="padding: 10px; font-size:14px; color:gray;">
	<b>提示</b>：灰色的代表不能移入。
	<br/>文章只能移动到栏目类型为图片列表或者新闻列表的，列表栏目中去
</div>

<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form;
});

//选择指定的栏目进行移动文章 targetColumnId 要移动到哪个栏目的id
function selectColumn(targetColumnId){
	msg.loading("转移中");
	$.post("/news/newsChangeColumnForSelectColumnSubmit.do?newsid=${newsid}&targetColumnId="+targetColumnId, function(data){
		msg.close();
		if(data.result == '1'){
			parent.msg.success("转移成功");
       		parent.location.reload();	//刷新父窗口
       		parent.layer.close(index);
	 	}else if(data.result == '0'){
	 		parent.msg.failure(data.info);
	 	}else{
	 		parent.msg.failure('操作失败');
	 	}
	});
	
}
</script>
</body>
</html>