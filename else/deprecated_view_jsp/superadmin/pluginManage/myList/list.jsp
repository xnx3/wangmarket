<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x"%>
<jsp:include page="../../../iw/common/head.jsp">
	<jsp:param name="title" value="插件管理" />
</jsp:include>

<table class="aui-table-responsive layui-table iw_table"
	style="color: black; font-size: 14px; margin-top:10px;">
	<thead>
		<tr>
			<th style="text-align: center;">插件ID</th>
			<th style="text-align: center;">插件名称</th>
			<th style="text-align: center;width: 100px;">操作</th>
		</tr>
	</thead>
	<tbody id="tbody">
		<c:forEach var="plugin" items="${list }">
			<tr>
				<td style="text-align: center;" id = "id">${plugin.id }</td>
				<td style="text-align: center;" >${plugin.menuTitle }</td>
				<td style="text-align: center;">
					<a class="layui-btn layui-btn-sm" onclick="exportPlugin('${plugin.id }', '${plugin.menuTitle }')">导出</a>
				</td>
			</tr>
		</c:forEach>

	</tbody>
</table>
<div style="height: 100%;text-align: right;padding-right:40px; font-size: 16px;">
	<span style="color: red;"></span><a href = "http://help.wang.market/16664.html" style="color: gray;" target = "_blank">&nbsp;点击查看插件压缩包规范文档&nbsp;</a>
</div>	
<!-- 通用分页跳转 -->
<%-- <jsp:include page="../../../iw/common/page.jsp"></jsp:include> --%>

<script type="text/javascript">
// 导出插件
function exportPlugin(plugin_id, plugin_name) {
	var dtp_confirm = layer.confirm('确定要导出插件“' + plugin_name + '”？', {
		  btn: ['导出','取消'] //按钮
		}, function(){
			layer.close(dtp_confirm);
			parent.msg.loading("导出中");    //显示“操作中”的等待提示
			$.post('/plugin/pluginManage/exportPlugin.do?plugin_id=' + plugin_id, function(data){
			    parent.msg.close();    //关闭“操作中”的等待提示
			    if(data.result == 1){
			        parent.msg.success('导出成功');
			        //设置下载文件的返回路径
			        window.location.href=data.info;
			     }else if(data.result == 0){
			         parent.msg.failure(data.info);
			     }else{
			         parent.msg.failure();
			     }
			});
		}, function(){
	});		
}
</script>

<jsp:include page="../../../iw/common/foot.jsp"></jsp:include>