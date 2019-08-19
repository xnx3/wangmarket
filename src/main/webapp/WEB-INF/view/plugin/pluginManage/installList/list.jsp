<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../../../iw/common/head.jsp">
	<jsp:param name="title" value="插件管理"/>
</jsp:include>

<div style="height: 10px;"></div>
<form action="/plugin/pluginManage/installList.do">
	<label class="layui-form-label">插件名称</label>
	<div class="layui-input-inline" style="width: 100px; float:left;">
		<input style="100px" id = "menuTitle" type="text" name="menu_title" autocomplete="off" class="layui-input">
	</div>
    <button class="layui-btn iw_list_search_submit" type="submit">搜索</button>
    
    <div style="float: right; " class="layui-form">
		<script type="text/javascript"> orderBy('id_DESC=编号'); </script>
	</div>
</form>
<div style="height: 10px;"></div>


<table class="aui-table-responsive layui-table iw_table" style="color: black;font-size: 14px;">
  <thead>
    <tr>
		<th style="text-align:center;">插件ID</th>
        <th style="text-align:center;">插件名称</th>
        <th style="text-align:center;">简介</th>
        <th style="text-align:center;">操作</th>
    </tr> 
  </thead>
  <tbody id="tbody">
  	<c:forEach var = "plugin" items="${pluginList }">
  		<tr>
	     	<td style="text-align:center;cursor: pointer;" onclick="pluginView('${plugin.id }');">${plugin.id }</td>
	         <td style="text-align:center;cursor: pointer;width:150px;" onclick="pluginView('${plugin.id }');">${plugin.menuTitle }</td>
	         <td style="text-align:center;cursor: pointer;" onclick="pluginView('${plugin.id }');">${plugin.intro }</td>
	         <td style="text-align:center; width:200px;">
         		<a class="layui-btn layui-btn-sm" onclick="unIntallPlugin('${plugin.id }', '${plugin.menuTitle }')" style="margin-left: 3px;"><i class="layui-icon">&#xe640;卸载</i></a>
				<!-- 如果是自己开发的插件可以导出 -->
				<!-- 云端模板库的只能升级了 -->
         		<c:choose>
         			<c:when test="${fn:contains(ids, plugin.id) }">
         				<a class="layui-btn layui-btn-sm" onclick="exportPlugin('${plugin.id }', '${plugin.menuTitle }')" style="margin-left: 3px;"><i class="layui-icon">&#xe601;导出</i></a>
         			</c:when>
         			<c:otherwise>
         				<a class="layui-btn layui-btn-sm" onclick="upgradePlugin('${plugin.id }', '${plugin.version}', '${plugin.menuTitle }')" style="margin-left: 3px;"><i class="layui-icon">&#xe857;升级</i></a>
         			</c:otherwise>
         		</c:choose>
	         </td>
	     </tr>
  	</c:forEach>
  		 
  </tbody>
</table>
<a id = "downPlugin" href = ""></a>
<script type="text/javascript">
	
	// 升级插件
	function upgradePlugin(pluginId, version, pluginName) {
		var dtp_confirm = layer.confirm('确定要升级插件' + pluginName + '？', {
			  btn: ['确认','取消'] //按钮
			}, function(){
				layer.close(dtp_confirm);
				parent.iw.loading("升级中");    //显示“操作中”的等待提示
				$.post('/plugin/pluginManage/upgradePlugin.do',{"plugin_id" : pluginId, "version" : version }, function(data){
				    parent.iw.loadClose();    //关闭“操作中”的等待提示
				    if(data.result == '1'){
				        parent.iw.msgSuccess('升级成功');
				        window.location.reload();	//刷新当前页
				     }else if(data.result == '0'){
				         parent.iw.msgFailure(data.info);
				     }else{
				         parent.iw.msgFailure();
				     }
				});
			}, function(){
		});		
	}

	// 卸载插件
	function unIntallPlugin(pluginId, pluginName){
		var dtp_confirm = layer.confirm('确定要卸载插件' + pluginName + '？', {
			  btn: ['确认','取消'] //按钮
			}, function(){
				layer.close(dtp_confirm);
				parent.iw.loading("卸载中");    //显示“操作中”的等待提示
				$.post('/plugin/pluginManage/unIstallPlugin.do?plugin_id=' + pluginId, function(data){
				    parent.iw.loadClose();    //关闭“操作中”的等待提示
				    if(data.result == '1'){
				        parent.iw.msgSuccess('卸载成功');
				        window.location.reload();	//刷新当前页
				     }else if(data.result == '0'){
				         parent.iw.msgFailure(data.info);
				     }else{
				         parent.iw.msgFailure();
				     }
				});
			}, function(){
		});		
	}
	// 导出插件
	function exportPlugin(plugin_id, plugin_name) {
		var dtp_confirm = layer.confirm('确定要导出插件“' + plugin_name + '”？', {
			  btn: ['导出','取消'] //按钮
			}, function(){
				layer.close(dtp_confirm);
				parent.iw.loading("导出中");    //显示“操作中”的等待提示
				$.post('/plugin/pluginManage/exportPlugin.do?plugin_id=' + plugin_id, function(data){
				    parent.iw.loadClose();    //关闭“操作中”的等待提示
				    if(data.result == 1){
				        parent.iw.msgSuccess('导出成功');
				        //设置下载文件的返回路径
				        $("#downPlugin").attr("href" , data.info);
				        //下载文件
				        $("#downPlugin")[0].click();
				     }else if(data.result == 0){
				         parent.iw.msgFailure(data.info);
				     }else{
				         parent.iw.msgFailure();
				     }
				});
			}, function(){
		});		
	}
</script>

<jsp:include page="../../../iw/common/foot.jsp"></jsp:include>
