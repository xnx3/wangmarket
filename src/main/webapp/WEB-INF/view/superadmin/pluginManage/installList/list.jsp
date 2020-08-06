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
    
</form>
<div style="height: 10px;"></div>


<table class="aui-table-responsive layui-table iw_table" style="color: black;font-size: 14px;">
  <thead>
    <tr>
		<th style="text-align:center;">插件ID</th>
        <th style="text-align:center;">插件名称</th>
        <th style="text-align:center;">简介</th>
        <th style="text-align:center;">当前版本</th>
        <th style="text-align:center;">操作</th>
    </tr> 
  </thead>
  <tbody id="tbody">
  	<c:forEach var = "plugin" items="${pluginList }">
  		<tr>
	     	<td style="text-align:center;cursor: pointer;">${plugin.pluginRegisterBean.id }</td>
	         <td style="text-align:center;cursor: pointer;width:150px;">${plugin.pluginRegisterBean.menuTitle }</td>
	         <td style="text-align:center;cursor: pointer; text-align:left;">${plugin.pluginRegisterBean.intro }</td>
	         <td style="text-align:center;cursor: pointer; text-align:left; width:80px;">${plugin.pluginRegisterBean.version }</td>
	         <td style="text-align:center; width:160px;">
         		<a class="layui-btn layui-btn-sm" onclick="unIntallPlugin('${plugin.pluginRegisterBean.id }', '${plugin.pluginRegisterBean.menuTitle }')" style="margin-left: 3px;"><i class="layui-icon">&#xe640;卸载</i></a>
				<!-- 如果是自己开发的插件可以导出 -->
				<!-- 云端模板库的只能升级了 -->
         		<c:choose>
         			<c:when test="${plugin.findNewVersion == false }">
         				 
         			</c:when>
         			<c:otherwise>
         				<a class="layui-btn layui-btn-sm" onclick="upgradePlugin('${plugin.pluginRegisterBean.id }', '${plugin.pluginRegisterBean.version}', '${plugin.pluginRegisterBean.menuTitle }')" style="margin-left: 3px;"><i class="layui-icon">&#xe857;升级</i></a>
         			</c:otherwise>
         		</c:choose>
	         </td>
	     </tr>
  	</c:forEach>
  </tbody>
</table>
<a id = "downPlugin" href = ""></a>
<script type="text/javascript">
	//格式化版本号将10010010转换为1.1.1格式
	function versionFormat(version){
		version = version + '';
		var add = 9 - version.length;
		for(var i = 0 ; i < add; i ++) {
			version = "0" + version;
		}
		var one = version.substring(0,3).replace(/\b(0+)/gi, "");
		var two = version.substring(3,6).replace(/\b(0+)/gi, "");
		var three = version.substring(6,9).replace(/\b(0+)/gi, "");
		if(one == ''){
			one = '0';
		}
		if(two == ''){
			two = '0';
		}
		if(three == ''){
			three = '0';
		}
		return one + '.' + two + '.' + three;
	}
	// 升级插件
	function upgradePlugin(pluginId, version, pluginName) {
		var dtp_confirm = layer.confirm('确定要升级插件' + pluginName + '？', {
			  btn: ['确认','取消'] //按钮
			}, function(){
				layer.close(dtp_confirm);
				parent.msg.loading("升级中");    //显示“操作中”的等待提示
				$.post('/plugin/pluginManage/upgradePlugin.do',{"plugin_id" : pluginId, "version" : version }, function(data){
				    parent.msg.close();    //关闭“操作中”的等待提示
				    if(data.result == 1){
				        parent.msg.success('升级成功');
				        
				        var aler = layer.alert('升级成功。重新Tomcat服务后生效，请稍后重试。<span style="color:red;">注：windows系统tomcat环境下需要手动启动tomcat。</span>', {
						  skin: 'layui-layer-molv' //样式类名
						  ,closeBtn: 0
						}, function(){
							// 关闭弹窗
							layer.close(aler);
							// 重启服务器
							parent.msg.loading("重启中,此过程大约1分钟左右");
							$.ajax({
								url:'/plugin/pluginManage/restart.do',
						        type:'POST',
						        cache: false,
						        contentType: false,
						        processData: false,
						        success:function(data){
						        	if(data.result == '1'){
						        		//定时器 持续访问后台 直到服务器重启完毕
						        		window.setInterval("revisit()", 3000);
								    }
						        },
						        error:function(){
						        }
							});
						});
				        
				        //window.location.reload();	//刷新当前页
				     }else if(data.result == '0'){
				         parent.msg.failure(data.info);
				     }else{
				         parent.msg.failure();
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
				parent.msg.loading("卸载中");    //显示“操作中”的等待提示
				$.post('/plugin/pluginManage/unIstallPlugin.do?plugin_id=' + pluginId, function(data){
				    parent.msg.close();    //关闭“操作中”的等待提示
				    if(data.result == '1'){
				        parent.msg.success('卸载成功');
				        parent.parent.loadUrl('/plugin/pluginManage/index.do')
				     }else if(data.result == '0'){
				         parent.msg.failure(data.info);
				     }else{
				         parent.msg.failure();
				     }
				});
			}, function(){
		});		
	}
	
	
//服务器重启后重新访问后台
function revisit(){
	$.ajax({
		url:'/login.do',
        type:'POST',
        contentType: false,    //不可缺
        processData: false,    //不可缺
        success:function(data){
        	if(data != null && data != '' && data != undefined){
        		parent.msg.close();
        		parent.parent.window.location.reload();
		    }
        },
        error:function(){
        }
	});
}
</script>

<jsp:include page="../../../iw/common/foot.jsp"></jsp:include>
