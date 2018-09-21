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
	<jsp:param name="title" value="欢迎登陆"/>
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

<div style="text-align:center; font-size:39px; padding-top:35px; padding-bottom: 10px;">
	欢迎使用 <%=Global.get("SITE_NAME") %>云建站系统
</div>
<div style="padding: 20px; font-size: 25px; border: 1px solid #eee;">
	您好，我们改版了，原本登陆成功后这个位置显示的编辑首页，你可以在左侧“模版管理” - “模版页面” 中，找到首页，进行编辑即可。<a href="javascript:template();">点击此处快速进入模版管理</a>

</div>
<table class="layui-table iw_table" lay-even lay-skin="nob" style="width:100%; padding-top:20px;">
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
<button onclick="helpVideo();" class="layui-btn layui-btn-primary">入门视频教程</button>
<button onclick="bindDomain();" class="layui-btn layui-btn-primary">绑定域名</button>
<button onclick="template();" class="layui-btn layui-btn-primary">模版页面</button>


</div>

<script>
//绑定域名
function bindDomain(){
	//收起所有一级栏目
	parent.backAllFirstColumn();
	//展开某个一级栏目
	parent.unfoldFirstColumn('li_system');
	//选中某个菜单
	parent.selectedLeftMenu('dd_wangzhanshuxing');
	
	//调出绑定域名
	parent.updateBindDomain();
}

//模版管理
function template(){
	//收起所有一级栏目
	parent.backAllFirstColumn();
	//展开某个一级栏目
	parent.unfoldFirstColumn('li_template');
	//选中某个菜单
	parent.selectedLeftMenu('dd_mobanyemian');
	
	//打开模版管理
	parent.loadIframeByUrl('templatePageList.do');
}

//入门视频
function helpVideo(){
	//收起所有一级栏目
	parent.backAllFirstColumn();
	//展开某个一级栏目
	parent.unfoldFirstColumn('li_help');
	//选中某个菜单
	parent.selectedLeftMenu('dd_help_basicUse');
	
	//打开模版管理
	parent.loadIframeByUrl('http://www.wscso.com/site_basicUse.html');
}
</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>  