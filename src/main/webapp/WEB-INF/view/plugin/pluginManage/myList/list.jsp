<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x"%>
<jsp:include page="../../../iw/common/head.jsp">
	<jsp:param name="title" value="插件管理" />
</jsp:include>

<jsp:include page="../../../iw/common/list/formSearch_formStart.jsp"></jsp:include>
<jsp:include page="../../../iw/common/list/formSearch_input.jsp">
	<jsp:param name="iw_label" value="插件名称" />
	<jsp:param name="iw_name" value="menu_title" />
</jsp:include>

<input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />

<a class="layui-btn layui-btn-normal"
	href="/plugin/pluginManage/add.do">添加插件</a>

<div style="float: right;" class="layui-form">
	<script type="text/javascript"> orderBy('id_DESC=编号,lasttime_DESC=最后登陆时间,money_DESC=账户余额,currency=<%=Global.get("CURRENCY_NAME")%>'); </script>
</div>
</form>
<script type="text/javascript">

//判断表格显示是或者否
function yesOrNo(code) {
	if(code == 1){
		document.write('是');
	}else {
		document.write('否');
	}
}

//格式化版本号将10010010转换为1.1.1格式
function versionFormat(version){
	//对版本中的0进行替换
	version = version.toString().replace(/00/g, '.').replace(/0/g, '.');
	//如果是以.为结尾的话去掉最后的点
	if(/.$/.test(version)){
		version = version.substring(0,version.length - 1);
	}
	document.write(version);
}

</script>
<table class="aui-table-responsive layui-table iw_table"
	style="color: black; font-size: 14px;">
	<thead>
		<tr>
			<th style="text-align: center;">插件ID</th>
			<th style="text-align: center;">插件名称</th>
			<th style="text-align: center;">作者名称</th>
			<th style="text-align: center;">授权专用</th>
			<th style="text-align: center;">支持Mysql</th>
			<th style="text-align: center;">支持Sqlite</th>
			<th style="text-align: center;">最低支持版本</th>
			<th style="text-align: center;">安装状态</th>
			<th style="text-align: center;width: 360px;">操作</th>
		</tr>
	</thead>
	<tbody id="tbody">
		<c:forEach var="plugin" items="${list }">
			<tr>
				<td style="text-align: center;" id = "id"
					onclick="pluginView('${plugin.id }');">${plugin.id }</td>
				<td style="text-align: center;"
					onclick="pluginView('${plugin.id }');">${plugin.menuTitle }</td>
				<td style="text-align: center;"
					onclick="pluginView('${plugin.id }');">${plugin.authorName }</td>
				<td style="text-align: center;"
					onclick="pluginView('${plugin.id }');"><script>yesOrNo(${plugin.supportAuthorizeVersion })</script></td>
				<td style="text-align: center;"
					onclick="pluginView('${plugin.id }');"><script>yesOrNo(${plugin.supportMysql })</script></td>
				<td style="text-align: center;"
					onclick="pluginView('${plugin.id }');"><script>yesOrNo(${plugin.supportSqlite })</script></td>
				<td style="text-align: center;"
					onclick="pluginView('${plugin.id }');"><script>versionFormat(${plugin.version })</script></td>
				<td style="text-align: center;"
					onclick="pluginView('${plugin.id }');"><script>yesOrNo(${plugin.installState })</script></td>
				<td style="text-align: center;">
					<botton
						class="layui-btn layui-btn-sm"
						onclick="editPlugin('${plugin.id }')" style="margin-left: 3px;">
					<i class="layui-icon">&#xe642;编辑</i></botton>
					 <botton
						class="layui-btn layui-btn-sm"
						onclick="deletePlugin('${plugin.id }','${plugin.menuTitle }')" style="margin-left: 3px;">
					<i class="layui-icon">&#xe640;删除</i>
					</botton> <c:if
						test="${plugin.installState == 0 }">
						<botton class="layui-btn layui-btn-sm"
							onclick="installPlugin('${plugin.id }', '${plugin.menuTitle }', '${plugin.downUrl }')"
							style="margin-left: 3px;">
						<i class="layui-icon">&#xe61f;安装</i></botton>
					</c:if>
					</botton> 
					<c:choose>
						<c:when test="${plugin.downUrl == null || plugin.downUrl eq '' }">
							<botton class="layui-btn layui-btn-sm"
								onclick="upload('${plugin.id }')"
								style="margin-left: 3px;">
							<i class="layui-icon">&#xe681;上传</i></botton>
						</c:when>
						<c:otherwise>
							<botton class="layui-btn layui-btn-sm" 
								onclick="upload('${plugin.id }')"
								style="margin-left: 3px;">
							<i class="layui-icon">&#xe681;更新</i></botton>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:forEach>

	</tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../../../iw/common/page.jsp"></jsp:include>

<script type="text/javascript">

// 上传插件压缩文件
function upload(pluginId){
	layer.open({
		type: 2, 
		title:'上传插件文件', 
		area: ['260px', '330px'],
		shadeClose: true, //开启遮罩关闭
		content: '/plugin/pluginManage/upload.do?plugin_id=' + pluginId
	});
}

//安装插件
function installPlugin(pluginId, pluginName, downUrl) {
	if(typeof(downUrl) == 'undefined' || downUrl == '') {
		iw.msgFailure('请先上传插件的压缩文件');
		return false;
	}
	
	var dtp_confirm = layer.confirm('确定要安装' + pluginName+  '？', {
		  btn: ['安装','取消'] //按钮
		}, function(){
			layer.close(dtp_confirm);
			parent.iw.loading("安装中");    //显示“操作中”的等待提示
			$.post('/plugin/pluginManage/installPlugin.do', {"plugin_id" : pluginId, "down_url" : downUrl}, function(data){
			    parent.iw.loadClose();    //关闭“操作中”的等待提示
			    if(data.result == 1){
			    	if(data.info == 'restart') {
			    		var aler = layer.alert('安装成功。该插件需要重新启动当前服务，请稍后重试。<span style="color:red;">注：windows系统tomcat环境下需要手动启动tomcat。</span>', {
			    			  skin: 'layui-layer-molv' //样式类名
			    			  ,closeBtn: 0
			    			}, function(){
			    				// 关闭弹窗
			    				layer.close(aler);
			    				// 重启服务器
			    				window.location.href = '/plugin/pluginManage/restart.do';
			    			});
			    	}else {
			    		parent.iw.msgSuccess('安装成功');
				        window.location.reload();	//刷新当前页
			    	}
			     }else if(data.result == 0){
			         parent.iw.msgFailure(data.info);
			     }else{
			         parent.iw.msgFailure();
			     }
			});
		}, function(){
	});
}


//根据id删除插件
function deletePlugin(plugin_id,name){
	var dtp_confirm = layer.confirm('确定要删除插件“'+name+'”？删除后不可恢复！', {
	  btn: ['删除','取消'] //按钮
	}, function(){
		layer.close(dtp_confirm);
		
		parent.iw.loading("删除中");    //显示“操作中”的等待提示
		$.post('/plugin/pluginManage/deletePlugin.do',{"plugin_id" : plugin_id}, function(data){
		    parent.iw.loadClose();    //关闭“操作中”的等待提示
		    if(data.result == '1'){
		        parent.iw.msgSuccess('操作成功');
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

//查看插件详情信息
function pluginView(plugin_id){
	layer.open({
		type: 2, 
		title:'查看插件信息', 
		area: ['460px', '630px'],
		shadeClose: true, //开启遮罩关闭
		content: '/plugin/pluginManage/queryViewById.do?plugin_id=' + plugin_id
	});
}

//修改插件信息
function editPlugin(plugin_id, pluginname){
	window.location.href = '/plugin/pluginManage/add.do?plugin_id=' + plugin_id;
}

</script>

<jsp:include page="../../../iw/common/foot.jsp"></jsp:include>