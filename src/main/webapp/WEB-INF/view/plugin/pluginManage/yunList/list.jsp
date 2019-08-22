<%@page import="com.xnx3.j2ee.Global"%>
<%@page import="com.xnx3.wangmarket.Authorization"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../../iw/common/head.jsp">
	<jsp:param name="title" value="插件管理"/>
</jsp:include>

<div style="height: 10px;"></div>
<form>
	<label class="layui-form-label">插件名称</label>
	<div class="layui-input-inline" style="width: 100px; float:left;">
		<input style="100px" id = "menuTitle" type="text" name="menu_title" autocomplete="off" class="layui-input">
	</div>
    <a class="layui-btn iw_list_search_submit" onclick="pageQuery(1, 'menu')"  >搜索</a>
    
    <div style="float: right; " class="layui-form">
		<script type="text/javascript"> orderBy('id_DESC=编号'); </script>
	</div>
</form>
<div style="height: 10px;"></div>

<table class="aui-table-responsive layui-table iw_table" style="color: black;font-size: 14px;">
  <thead>
    <tr>
		<th style="text-align: center;">插件ID</th>
        <th style="text-align: center;">插件名称</th>
        <th style="text-align: center;">作者名称</th>
        <th style="text-align: center;">授权专用</th>
        <th style="text-align: center;">支持Mysql</th>
        <th style="text-align: center;">支持Sqlite</th>
        <th style="text-align: center;">插件版本</th>
        <th style="text-align: center;width: 100px;">操作</th>
    </tr> 
  </thead>
  <tbody id="tbody">
  		
  </tbody>
</table>
<!-- 通用分页跳转 -->
<style>
	#xnx3_page{
		width:100%;
		padding-top:15px;
		padding-bottom: 20px;
	}
	#xnx3_page ul{
		width: 100%;
		text-align: center;
	}
	#xnx3_page ul li{
		display: inline-block;
	    vertical-align: middle;
	    border: 1px solid #e2e2e2;
	    background-color: #fff;
	    padding-right: 1px;
    	padding-left: 1px;
	}
	#xnx3_page ul .xnx3_page_currentPage, #xnx3_page ul .xnx3_page_currentPage a{
		background-color: #009688;
		color:#fff;
	}
	#xnx3_page ul li a{
	 	padding: 0 15px;
	 	height: 30px;
	    line-height: 30px;
	    background-color: #fff;
	    color: #333;
	 }
</style>
<div id="xnx3_page">
	
</div>

<script type="text/javascript">

//判断表格显示是或者否
function yesOrNo(code) {
	if(code == 1){
		return '是';
	}else {
		return '否';
	}
}

//格式化版本号将10010010转换为1.1.1格式
function versionFormat(version){
	//对版本中的0进行替换
	version = version.toString().replace(/00/g, '.').replace(/0/g, '.');
	//如果是以.为结尾的话去掉最后的点
	if(version.lastIndexOf('.') == version.length - 1){
		version = version.substring(0,version.length - 1);
	}
	return version;
}
//页面加载完成之后访问接口获取插件列表
$(function(){
	// 默认查询第一页
	pageQuery(1, '');
});

// 分页查询
function pageQuery(currentPage,menuTitle) {
	// 获取插件搜索条件
	if(menuTitle == 'menu') {
		menuTitle = $("#menuTitle").val();
	}
	var freeIdList = "";
	//未授权用户，请尊重作者劳动成果，保留我方版权标示及链接！授权参见：http://www.wang.market/price.html
	
	// 获取未经授权用户可以使用的插件id列表
	$.post("//plugin.wangmarket.leimingyun.com/application/getFreePluginName.do",function(data, status){
		freeIdList = data;
	});
		
	
	$.post("//plugin.wangmarket.leimingyun.com/application/list.do",{"currentPage" : currentPage, "menu_title" : menuTitle},function(data){
		//如果返回结果为成功
		if(data.result == 1){
			/*
			* 插件列表
			*/
			//得到插件的list信息
			var pluginIds = '${pluginIds}';
			var list = data.list;
			var html = '';
			//循环遍历插件list，拼装需要显示的信息
			$.each(list,function(index , plugin){
			html += '<tr>';
		    html += '	<td style="text-align:center;cursor: pointer;" onclick="pluginView(\'' + plugin.id + '\');">' + plugin.id + '</td>';
		    html += '    <td style="text-align:center;cursor: pointer;" onclick="pluginView(\'' + plugin.id + '\');">' + plugin.menuTitle + '</td>';
		    html += '    <td style="text-align:center;cursor: pointer;" onclick="pluginView(\'' + plugin.id + '\');">' + plugin.authorName + '</td>';
		    html += '    <td style="text-align:center;cursor: pointer;" onclick="pluginView(\'' + plugin.id + '\');">' + yesOrNo(plugin.supportAuthorizeVersion) + '</td>';
		    html += '    <td style="text-align:center;" onclick="pluginView(\'' + plugin.id + '\');">' + yesOrNo(plugin.supportMysql) + '</td>';
		    html += '    <td style="text-align:center;" onclick="pluginView(\'' + plugin.id + '\');">' + yesOrNo(plugin.supportSqlite) + '</td>';
		    html += '    <td style="text-align:center;" onclick="pluginView(\'' + plugin.id + '\');">' + versionFormat(plugin.version) + '</td>';
		    html += '    <td style="text-align:center;">';
		   
		    // 已授权用户
		    <% if(!Authorization.copyright){ %>
		    	 // 判断检查是否已经安装
			    if(pluginIds.indexOf(plugin.id) == -1){
				    html += '<botton class="layui-btn layui-btn-sm" onclick="installPlugin(\'' + plugin.id + '\', \'' + plugin.menuTitle + '\', \'' + plugin.downUrl + '\')" style="margin-left: 3px;"><i class="layui-icon">&#xe61f;安装</i></botton>';
			    }else{
				    html += '<botton class="layui-btn layui-btn-sm" onclick="javascript:;" style="margin-left: 3px;"><i class="layui-icon">&#x1005;已安装</i></botton>';
			    }
		    <% } %>
		    
		    // 未授权用户
		    <% if(Authorization.copyright){ %>
		   		// 判断为授权用户是否可以使用
			    if(freeIdList.indexOf(plugin.id) == -1) {
			    	 html += '<botton class="layui-btn layui-btn-sm" onclick="showUnAyth()" style="margin-left: 3px;"><i class="layui-icon" style = "background : gary;">&#x1006;禁用</i></botton>';
			    }else {
			    	 // 判断检查是否已经安装
			    	if(pluginIds.indexOf(plugin.id) == -1){
					    html += '<botton class="layui-btn layui-btn-sm" onclick="installPlugin(\'' + plugin.id + '\', \'' + plugin.menuTitle + '\', \'' + plugin.downUrl + '\')" style="margin-left: 3px;"><i class="layui-icon">&#xe61f;安装</i></botton>';
				    }else{
					    html += '<botton class="layui-btn layui-btn-sm" onclick="javascript:;" style="margin-left: 3px;"><i class="layui-icon">&#x1005;已安装</i></botton>';
				    }
			    }
	    	<% } %>
		    
		    
		    html += '    </td>';
		    html += '</tr>';
			});
			//将拼装的字符新婚添加到指定区域
			$("#tbody").html(html);
			
			/*
			* 插件列表分页
			*/
			var page = data.page;
			var pageHtml = '';
			pageHtml += '<ul>';
			// 判断当前页面是否是列表页第一页，若不是第一页，那会显示首页、上一页的按钮
			if(page.currentFirstPage == false) {
				pageHtml += '	<li><a href="javascript:pageQuery(1);">首页</a></li>';
				// 截取上一页的页数
				pageHtml += '	<li><a href="javascript:pageQuery(' + page.upPageNumber + ', \'' + menuTitle + '\');">上一页</a></li>';
			}
			
			//输出上几页的连接按钮
			$.each(page.upList, function(i, a) {
				// 截取当前的代表的页数
				/* var num = a.href.charAt(page.upPage.length - 1); */
				pageHtml += '<li><a href="javascript:pageQuery(' + (i + 1) + ', \'' + menuTitle + '\');">' + a.title + '</a></li>';
			});
			// 当前页面，当前第几页 
			pageHtml += '<li class="xnx3_page_currentPage"><a href="#">' + page.currentPageNumber + '</a></li>';
			pageHtml += '';
			
			//输出下几页的连接按钮 
			$.each(page.nextList, function(i, a) {
				// 截取当前的代表的耶稣
				var num = a.href.charAt(page.upPage.length - 1);
				pageHtml += '<li><a href="javascript:pageQuery(' + num + ', \'' + menuTitle + '\');">' + a.title + '</a></li>';
			});
			
			pageHtml += '';
			// 判断当前页面是否是列表页最后一页，若不是最后一页，那会显示下一页、尾页的按钮
			if(page.currentLastPage == false) {
				// 截取下一页的页数
				pageHtml += '<li><a href="javascript:pageQuery(' + page.nextPageNumber + ', \'' + menuTitle + '\');">下一页</a></li>';
				pageHtml += '<li><a href="javascript:pageQuery(' + page.allRecordNumber + ', \'' + menuTitle + '\');">尾页</a></li>';
			}
			pageHtml += '<li style="margin-right:30px;border:0px; padding-top:5px;">共' + page.allRecordNumber + '条，' + page.lastPageNumber + '页</li>';
			pageHtml += '</ul>';
			
			// 将拼装的字符加入到div中
			$("#xnx3_page").html(pageHtml);
		}else{
			iw.msgFailure("获取插件列表失败");
		}
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
			$.post('/plugin/pluginManage/installYunPlugin.do', {"plugin_id" : pluginId}, function(data){
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

//提示禁止安装信息
function showUnAyth() {
	iw.msgFailure("此插件仅授权用户可用");
}

//查看用户详情信息
function pluginView(plugin_id){
	layer.open({
		type: 2, 
		title:'查看插件信息', 
		area: ['460px', '630px'],
		shadeClose: true, //开启遮罩关闭
		content: '//plugin.wangmarket.leimingyun.com/application/queryById.do?plugin_id=' + plugin_id
	});
}
</script>

<jsp:include page="../../../iw/common/foot.jsp"></jsp:include>