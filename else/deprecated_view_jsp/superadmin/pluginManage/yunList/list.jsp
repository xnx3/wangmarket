<%@page import="com.xnx3.j2ee.Global"%>
<%@page import="com.xnx3.wangmarket.Authorization"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <a class="layui-btn iw_list_search_submit" onclick="titleQuery()"  >搜索</a>
    
</form>
<div style="height: 10px;"></div>
<script type="text/javascript">

//判断表格显示是或者否
function yesOrNo(code) {
	if(code == 1){
		document.write('<i class="layui-icon" style = "color:green;">&#x1005;</i>');
	}else {
		document.write('<i class="layui-icon" style = "color:#EE4926;">&#x1007;</i>');
	}
}

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
		document.write(one + '.' + two);
	}else {
		document.write(one + '.' + two + '.' + three);
	}
}
</script>
<table class="aui-table-responsive layui-table iw_table" style="color: black;font-size: 14px;">
  <thead>
    <tr>
		<th style="text-align: center;">插件ID</th>
        <th style="text-align: center;">插件名称</th>
        <th style="text-align: center;">作者名称</th>
        <th style="text-align: center;">支持Mysql</th>
        <th style="text-align: center;">支持Sqlite</th>
        <th style="text-align: center;">插件版本</th>
        <th style="text-align: center;width: 150px;">最低网市场版本</th>
        <th style="text-align: center;width: 100px;">操作</th>
    </tr>
  </thead>
  <tbody id="tbody">
  		<c:forEach var="plugin" items="${list }">
    	<tr>
			<td style="text-align: center;cursor:pointer;" onclick="pluginView('${plugin.id }')">${plugin.id }</td>
	        <td style="text-align: center;cursor:pointer;" onclick="pluginView('${plugin.id }')">${plugin.menuTitle }</td>
	        <td style="text-align: center;cursor:pointer;" onclick="pluginView('${plugin.id }')">${plugin.authorName }</td>
	        <td style="text-align: center;cursor:pointer;" onclick="pluginView('${plugin.id }')"><script>yesOrNo('${plugin.supportMysql}')</script></td>
	        <td style="text-align: center;cursor:pointer;" onclick="pluginView('${plugin.id }')"><script>yesOrNo('${plugin.supportSqlite}')</script></td>
	        <td style="text-align: center;cursor:pointer;" onclick="pluginView('${plugin.id }')"><script>versionFormat('${plugin.version }')</script></td>
	        <td style="text-align: center;cursor:pointer;" onclick="pluginView('${plugin.id }')"><script>versionFormat('${plugin.wangmarketVersionMin }')</script></td>
	        <td style="text-align: center;width: 100px;">
	        <c:choose>
	        	<c:when test="${fn:contains(unSupportPluginId, plugin.id) == true }">
	        		<botton class="layui-btn layui-btn-disabled" onclick="showUnSupport()" style="width: 80px;font-size:14px;line-height: 30px;height: 30px;text-align: center;padding:0 10px 0 10px;">不支持</botton>
	        		
	        	</c:when>
	        	<c:otherwise>
	        		<!-- 已授权用户 -->
		        	<c:if test="${isUnAuth == false }">
		        		<c:choose>
				   			<c:when test="${plugin.supportAuthorizeVersion == 0 }">
				   				<botton class="layui-btn layui-btn-disabled" onclick="showUnAuth(true)" style="width: 80px;font-size:14px;line-height: 30px;height: 30px;text-align: center;padding:0 10px 0 10px;">需未授权</botton>
				   			</c:when>
				   			<c:otherwise>
				   				<c:if test="${fn:contains(pluginIds, plugin.id) == false }">
				   					<botton class="layui-btn layui-btn-sm" onclick="installPlugin('${plugin.id }', '${plugin.menuTitle }')" style="margin-left: 3px;width: 80px;"><i class="layui-icon">&#xe61f;安装</i></botton>
				   				</c:if>
				   				<c:if test="${fn:contains(pluginIds, plugin.id) == true }">
				   					<botton class="layui-btn layui-btn-disabled" onclick="javascript:;" style="width: 80px;line-height: 30px;height: 30px;"><i class="layui-icon" style="font-size:15px;text-align: center;">已安装</i>
				   				</c:if>
				   			</c:otherwise>
			   			</c:choose>
		        	</c:if>
			    	<!-- 判断检查是否已经安装 -->
			    
				    <!-- 未授权用户 -->
				    <c:if test="${isUnAuth ==true }">
				   		<!-- 判断未授权用户是否可以使用 -->
				   		<c:choose>
				   			<c:when test="${plugin.supportFreeVersion == 0 }">
				   				<botton class="layui-btn layui-btn-disabled" onclick="showUnAuth()" style="width: 80px;font-size:15px;line-height: 30px;height: 30px;text-align: center;">需授权</botton>
				   			</c:when>
				   			<c:otherwise>
				   				<c:if test="${fn:contains(pluginIds, plugin.id) == false}">
				   					<botton class="layui-btn layui-btn-sm" onclick="installPlugin('${plugin.id }', '${plugin.menuTitle }')" style="margin-left: 3px;width: 80px;"><i class="layui-icon">&#xe61f;安装</i></botton>
				   				</c:if>
				   				<c:if test="${fn:contains(pluginIds, plugin.id) == true }">
				   					<botton class="layui-btn layui-btn-primary layui-btn-disabled" onclick="javascript:;" style="width: 80px;line-height: 30px;height: 30px;width: 80px"><i class="layui-icon"  style="font-size:15px;text-align: center;">已安装</i>
				   				</c:if>
				   			</c:otherwise>
				   		</c:choose>
			    	</c:if>
	        	</c:otherwise>
	        </c:choose>
	        </td>
	    </tr>
    </c:forEach>
  </tbody>
</table>

<script type="text/javascript">

//安装插件
function installPlugin(pluginId, pluginName) {
	var dtp_confirm = layer.confirm('确定要安装' + pluginName+  '？', {
		  btn: ['安装','取消'] //按钮
		}, function(){
			layer.close(dtp_confirm);
			parent.msg.loading("安装中");    //显示“操作中”的等待提示
			$.ajax({
				url:'/plugin/pluginManage/installYunPlugin.do?plugin_id=' + pluginId,
				type:'POST',
				cache: false,
				contentType: false,
				processData: false,
				success:function(data){
					parent.msg.close();    //关闭“操作中”的等待提示
					if(data.result == 1){
						if(data.info == 'restart') {
							var aler = layer.alert('安装成功。该插件需要重新启动当前服务，请稍后重试。<span style="color:red;">注：windows系统tomcat环境下需要手动启动tomcat。</span>', {
						  skin: 'layui-layer-molv' //样式类名
						  ,closeBtn: 0
						}, function(){
							// 关闭弹窗
							layer.close(aler);
							// 重启服务器
							parent.msg.loading("请稍候,此过程大约1分钟左右");
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
						}else {
							parent.msg.success('安装成功');
							parent.parent.loadUrl('/plugin/pluginManage/index.do')
						}
					}else if(data.result == 0){
					     parent.msg.failure(data.info);
					}else{
					     parent.msg.failure();
					}
 				},
 				error:function(){
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

/*
 * 按插件名称进行搜索
 */
function titleQuery(){
	var menuTitle = $("#menuTitle").val();
	window.location.href = '/plugin/pluginManage/yunList.do?menu_title=' + menuTitle;
}

//提示禁止安装信息
function showUnAuth(auth) {
	if(auth == true) {
		msg.failure("此插件仅未授权用户可用");
	}else{
		window.location.href = 'http://www.leimingyun.com/price.html';	
	}
	
}

function showUnSupport() {
	msg.failure("您当前环境不支持该插件");
}

//查看插件详情信息
function pluginView(plugin_id){
	layer.open({
		type: 2, 
		title:'查看插件信息', 
		area: ['460px', '630px'],
		shadeClose: true, //开启遮罩关闭
		content: '/plugin/pluginManage/queryYunPluginById.do?plugin_id=' + plugin_id
	});
}
</script>

<jsp:include page="../../../iw/common/foot.jsp"></jsp:include>