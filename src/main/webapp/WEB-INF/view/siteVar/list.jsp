<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="网站全局变量"/>
</jsp:include>


<div style="width:100%;height:100%; background-color: #fff; overflow-x: hidden;">
	<table class="layui-table iw_table">
		<thead>
			<tr>
				<th>变量名</th>
	            <th>说明</th>
	            <th>值</th>
	            <th>操作</th>
			</tr> 
		</thead>
		<tbody>
			<c:forEach items="${list}" var="item">
	            <tr>
	                <td style="width:55px;">${item['name'] }</td>
	                <td>${item['description'] }</td>
	                <td>${item['value'] }</td>
	                <td style="text-align: center; width:140px;">
	                	<botton class="layui-btn layui-btn-sm" onclick="edit('${item['name'] }');" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
	                	<botton class="layui-btn layui-btn-sm" onclick="deleteVar('${item['name'] }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
	                </td>
	            </tr>
	        </c:forEach>
		</tbody>
	</table>

	<div style="padding:15px;">
		<button class="layui-btn" onclick="edit('');" style="margin-left: 10px;margin-bottom: 20px;"><i class="layui-icon" style="padding-right:8px; font-size: 22px;">&#xe608;</i>添加变量</button>
	</div>

	<div style="width: 100%;height:100%;position: fixed;left: 170px;word-wrap: break-word;border-right: 170px;box-sizing: border-box; padding-right: 10px; overflow-y: auto;overflow-x: hidden; border-right: 170px solid transparent;">
		<div style="padding: 20px;color: gray;">
			<div>操作按钮提示:</div>
			<div><i class="layui-icon">&#xe642;</i> &nbsp;：对某篇文章进行编辑操作</div>
			<div><i class="layui-icon">&#xe640;</i> &nbsp;：删除某篇文章，栏目类型为新闻信息、图文信息的栏目才会有此按钮</div>
		</div>
	</div>
</div>

<script>
layui.use('element', function(){
  var element = layui.element;
});
var laydate;
layui.use('laydate', function(){
  laydate = layui.laydate;
});


/**
 * 删除变量，传入要删除的变量的name
 */
function deleteVar(name){
	var dtv_confirm = layer.confirm('删除后不可恢复，您确定要删除此变量吗？', {
	  btn: ['删除','取消'] //按钮
	}, function(){
		layer.close(dtv_confirm);
		parent.msg.loading("删除中");
		$.post("/siteVar/deleteVar.do?name="+name, function(data){
			parent.msg.close();
			if(data.result == '1'){
				parent.msg.success("删除成功");
				location.reload();
	     	}else if(data.result == '0'){
	     		parent.msg.failure(data.info);
	     	}else{
	     		parent.msg.failure('操作失败');
	     	}
		});
    }, function(){
	});
}

/**
 * 新增，编辑变量
 * name 修改的变量，如果是新增，传入 '' 空字符串
 */
function edit(name){
	var url = '/siteVar/edit.do?name='+name;
	layer.open({
		type: 2, 
		//title:'添加栏目', 
		area: ['490px', '600px'],
		shadeClose: true, //开启遮罩关闭
		content: url,
		title:false, 
		closeBtn: 1
	});
}
</script>


</body>
</html>