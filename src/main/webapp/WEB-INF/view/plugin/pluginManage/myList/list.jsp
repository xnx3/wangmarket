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

<a class="layui-btn layui-btn-normal" id = "uploadPluginZip"
	href="javascript:;">添加更新插件</a>

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
			<th style="text-align: center;">安装状态</th>
			<th style="text-align: center;width: 240px;">操作</th>
		</tr>
	</thead>
	<tbody id="tbody">
		<c:forEach var="plugin" items="${list }">
			<tr>
				<td style="text-align: center;" id = "id">${plugin.id }</td>
				<td style="text-align: center;" >${plugin.menuTitle }</td>
				<td style="text-align: center;"><script>yesOrNo(${plugin.installState })</script></td>
				<td style="text-align: center;">
					 <botton
						class="layui-btn layui-btn-sm"
						onclick="deletePlugin('${plugin.id }','${plugin.menuTitle }')" style="margin-left: 3px;">
					<i class="layui-icon" title="删除">&#xe640;</i>
					</botton> <c:if
						test="${plugin.installState == 0 }">
						<botton class="layui-btn layui-btn-sm"
							onclick="installPlugin('${plugin.id }', '${plugin.menuTitle }')"
							style="margin-left: 3px;">
						<i class="layui-icon" title="安装">&#xe61f;</i></botton>
					</c:if>
					</botton> 
					<botton class="layui-btn layui-btn-sm"
						onclick="upload('${plugin.id }')"
						style="margin-left: 3px;">
					<i class="layui-icon" title="上传、更新">&#xe681;</i>
					</botton>
					<a class="layui-btn layui-btn-sm"
					  onclick="exportPlugin('${plugin.id }', '${plugin.menuTitle }')"
					  style="margin-left: 3px;">
					  <i title = "导出" class="layui-icon">&#xe601;</i>
				    </a>
				</td>
			</tr>
		</c:forEach>

	</tbody>
</table>
<div style="width:100%;padding:10px 30px 2px 0;text-align: right;font-size: 16px;">
	<span style="color:red;font-weight: bold;">注：</span>
	<i style="padding-right: 15px;" class="layui-icon">&#xe640;删除</i>
	<i style="padding-right: 15px;" class="layui-icon">&#xe61f;安装</i>
	<i style="padding-right: 15px;" class="layui-icon">&#xe681;更新</i>
	<i style="padding-right: 15px;" class="layui-icon">&#xe601;导出</i>
	&nbsp;&nbsp;&nbsp;&nbsp;
</div>
<a id = "downPlugin" href = ""></a>
<!-- 通用分页跳转 -->
<jsp:include page="../../../iw/common/page.jsp"></jsp:include>

<script type="text/javascript">

// 更新插件压缩文件
function upload(pluginId){
	layer.open({
		type: 2, 
		title:'更新插件文件', 
		area: ['260px', '330px'],
		shadeClose: true, //开启遮罩关闭
		content: '/plugin/pluginManage/upload.do?plugin_id=' + pluginId
	});
}

//安装插件
function installPlugin(pluginId, pluginName) {
	var dtp_confirm = layer.confirm('确定要安装' + pluginName+  '？', {
		  btn: ['安装','取消'] //按钮
		}, function(){
			layer.close(dtp_confirm);
			parent.iw.loading("安装中");    //显示“操作中”的等待提示
			$.post('/plugin/pluginManage/installPlugin.do', {"plugin_id" : pluginId}, function(data){
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
			    		parent.parent.window.location.reload();	//刷新当前页
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

//导出插件
function exportPlugin(plugin_id, plugin_name) {
	var dtp_confirm = layer.confirm('确定要导出插件“' + plugin_name + '”？', {
		  btn: ['导出','取消'] //按钮
		}, function(){
			layer.close(dtp_confirm);
	        //设置下载文件的返回路径
	        $("#downPlugin").attr("href" , "/myPlugin/" + plugin_id + ".zip");
	        //下载文件
	        $("#downPlugin")[0].click();
	        parent.iw.msgSuccess('导出成功');
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

// 通过上传插件压缩包进行添加插件
layui.use('upload', function(){
	layui.upload.render({
	  url: '/plugin/pluginManage/addByZip.do'
	  ,data: {}
	  ,method :'post'
	  ,elem : '#uploadPluginZip'
	  ,exts: 'zip'
	  ,field: 'file'
	  ,title :'上传插件'
	  ,size: '${maxFileSizeKB}'	//50MB ，这里单位是KB
      , before: function (obj) {
          parent.iw.loading("上传中");
      }
	  ,done: function(res, index, upload){
	  	parent.iw.loadClose();
	    //上传成功返回值，必须为json格式
	    if(res.result == 1){
	    	parent.iw.msgSuccess("上传成功！");
	    	parent.location.reload();	//刷新父窗口列表
	    }else{
	    	parent.iw.msgFailure(res.info);
	    }
	  }
	}); 
});

</script>

<jsp:include page="../../../iw/common/foot.jsp"></jsp:include>