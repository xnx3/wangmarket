<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.shiro.ShiroFunc"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="欢迎使用"/>
</jsp:include>


<style>
.iw_table tbody tr .iw_table_td_view_name{
	width:50%;
	padding-left:25%;
}
body{
	padding:40px;
}
</style>



<div class="layui-tab" id="gonggao" style="display:none;">
  <ul class="layui-tab-title">
    <li class="layui-this">公告信息</li>
    <li>联系</li>
  </ul>
  <div class="layui-tab-content" style="font-size:14px;">
    <div class="layui-tab-item layui-show" id="parentAgencyNotice">${parentAgencyNotice }</div>
    <div class="layui-tab-item">
    	名称：${parentAgency.name }<br/>
    	电话：${parentAgency.phone }<br/>
    	QQ：${parentAgency.qq }<br/>
    	地址：${parentAgency.address }<br/>
    	
    </div>
  </div>
</div>
<script>
//注意：选项卡 依赖 element 模块，否则无法进行功能性操作
layui.use('element', function(){
  var element = layui.element;
});
try{
	document.getElementById('parentAgencyNotice').innerHTML = document.getElementById('parentAgencyNotice').innerHTML.replace(/\n/g,"<br/>");
}catch(e){}
try{
	if(document.getElementById('parentAgencyNotice').innerHTML.length > 1){
		document.getElementById('gonggao').style.display='';
	}
}catch(e){}
</script>


<table class="layui-table iw_table" lay-even lay-skin="nob" style="width:100%; margin-top: 20px;">
	<tbody>
		<tr>
			<td class="iw_table_td_view_name">用户名</td>
			<td>${user.username }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">到期时间</td>
			<td>
				<x:time linuxTime="${site.expiretime }" format="yyyy-MM-dd"></x:time>
				<a href="javascript:parent.jumpParentAgency();" id="yanchangriqi" class="layui-btn layui-btn-primary" style="height: 30px;line-height: 30px;padding: 0 10px;font-size: 12px;margin-right: 10px;">延长</a>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">上次登陆时间</td>
			<td><x:time linuxTime="${user.lasttime }" format="yyyy-MM-dd"></x:time></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">上次登陆ip</td>
			<td>${user.lastip }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">自动分配域名</td>
			<td><script>document.write('<a href="http://${site.domain }.'+parent.autoAssignDomain+'" target="_black">${site.domain }.'+parent.autoAssignDomain+'</a>');</script></td>
		</tr>
    </tbody>
</table>


<h2 style="padding-top:50px;">入门功能，快捷导航</h2>
<hr>
<div>
<button onclick="parent.helpVideo();" class="layui-btn layui-btn-primary">入门视频教程</button>
<button onclick="parent.openBindDomain();" class="layui-btn layui-btn-primary">绑定域名</button>
<button onclick="parent.openTemplatePageList();" class="layui-btn layui-btn-primary">模版页面</button>


</div>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>  