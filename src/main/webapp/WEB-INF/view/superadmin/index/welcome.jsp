<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.entity.User"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/wm/common/head.jsp">
	<jsp:param name="title" value="管理后台"/>
</jsp:include>
<script src="${STATIC_RESOURCE_PATH}js/fun.js"></script>
<script>
var masterSiteUrl = '<%=Global.get("MASTER_SITE_URL") %>'; 
</script>
<script src="/js/admin/commonedit.js?v=${version }"></script>


<style>
.iw_table tbody tr .iw_table_td_view_name{
	width:50%;
	padding-left:25%;
}
</style>

<div style="text-align:center; font-size:29px; padding-top:7%; padding-bottom: 10px;">
	欢迎使用网市场云建站系统  
</div>


<table class="layui-table iw_table" lay-even lay-skin="nob" style="margin:3%; width:94%;">
	<tbody>
		<tr>
			<td class="iw_table_td_view_name">当前版本</td>
			<td>
				<span style="font-size:18px; padding-right:15px;" class="ignore">v${version }</span>
				<!--  <span id="versionTishi" style="font-size:14px;">(最新版本检测中...)</span>  -->
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">作者</td>
			<td class="ignore">管雷鸣</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">作者微信号</td>
			<td class="ignore">xnx3com</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">交流QQ群</td>
			<td class="ignore">472328584</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">文档</td>
			<td class="ignore"><a href="http://cms.zvo.cn" target="_black">cms.zvo.cn</a></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">官网</td>
			<td class="ignore"><a href="http://www.wang.market" target="_black">www.wang.market</a></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">微信公众号</td>
			<td><img src="${STATIC_RESOURCE_PATH}image/weixin_gzh.png" style="height:100px;" /></td>
		</tr>
		
	</tbody>
</table>

<script>
//检测最新版本
/* v6.5 去掉
function checkVersion(){
	$.getJSON("../../getNewVersion.do",function(result){
		if(result.findNewVersion){
			document.getElementById('versionTishi').innerHTML = '<span style="color:red;">发现新版本&nbsp;v'+result.newVersion+' <a href="'+result.previewUrl+'" style="color:red;" target="_black">点此查看</a></span>';
		}else{
			document.getElementById('versionTishi').innerHTML = '（已是最新版本）';
		}
	});
}
checkVersion();
*/
//借助于upgrade插件实现在线升级的提示
function upgradePlugin(){
	wm.post("/plugin/upgrade/superadmin/getNextVersion.json",{},function(result){
		if(result.result == '1'){
			//有返回值，那么说明自动升级插件存在，可以进行自动升级操作
			var html = '发现可升级版本：<b>'+result.info+'</b>'+
			   '<br/>是否需要进行在线升级？';
			msg.confirm({
			    text:html,
			    buttons:{
			        进入升级页面:function(){
			        	parent.loadUrl('/plugin/upgrade/superadmin/index.do');
			        },
			        取消:function(){
			            
			        }
			    } 
			});
		}	
	});
}
try{
	upgradePlugin();
}catch(e){ 
	console.log('upgrade plugin check...');
	console.log(e);
}

</script>

<jsp:include page="/wm/common/foot.jsp"></jsp:include> 
<style> /* 显示多语种切换 */ .translateSelectLanguage{ display:block; } </style>