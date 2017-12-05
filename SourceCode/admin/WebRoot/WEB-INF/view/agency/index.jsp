<%@page import="com.xnx3.admin.G"%>
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
	<jsp:param name="title" value="代理首页"/>
</jsp:include>
<script src="<%=basePath+Global.CACHE_FILE %>Role_role.js"></script>
<script src="http://res.weiunity.com/js/fun.js"></script>
<script src="http://res.weiunity.com/js/commonedit.js?v=<%=G.VERSION %>" type="text/javascript"></script>

<script type="text/javascript">
//得到当前版本号，用于版本更新后提醒更新内容
versionUpdateRemind('<%=G.VERSION %>');
</script>
<!-- 版本提示结束 -->

<style>
.iw_table tbody tr .iw_table_td_view_name{
	width:50%;
	padding-left:25%;
}
</style>

<div style="text-align:center; font-size:29px; padding-top:35px; padding-bottom: 10px;">
	欢迎登录 网市场云建站系统
</div>
<table class="layui-table iw_table" lay-even lay-skin="nob" style="margin:3%; width:94%;">
	<tbody>
		<tr>
			<td class="iw_table_td_view_name">公司名称</td>
			<td>${agency.name }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">到期时间</td>
			<td><x:time linuxTime="${agency.expiretime }"></x:time></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">权限</td>
			<td><script type="text/javascript">writeName('${user.authority }');</script></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">最后登陆</td>
			<td><x:time linuxTime="${user.lasttime }"></x:time></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">最后登陆ip</td>
			<td>${user.lastip }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">账户余额</td>
			<td>${agency.siteSize }站币
            	<div style="margin-top: -23px;margin-left: 80px;">
            		1站币 = 开通一个网站/年<br/>
            		1站币 = 续费一个网站/年<br/>
            		<%=G.agencyAddSubAgency_siteSize %>站币 = 开通一个下级代理/年<br/>
            		<%=G.agencyAddSubAgency_siteSize %>站币 = 续费一个下级代理/年<br/>
            	</div>
            </td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">网站限制</td>
			<td>
            	为保障网站不被非法利用，做如下限制：使用1站币开通或续费的网站：
            	<br/>1.限制存储空间&nbsp;<b>1GB</b>（一般正常使用大约占用不足 百MB）
            	<div style="padding-left:30px; padding-bottom:10px;">用满可续费，再原本的基础上增加，价格为&nbsp;1站币/1GB/年</div>
            	2.限制月流量&nbsp;<b>10GB/月</b>（一般正常使用每月大约消耗 5MB~200MB）
            	<div style="padding-left:30px; padding-bottom:10px;">不做非法用途，不用担心用超！</div>
            	3.限制文章条数&nbsp;<b>1000条</b>（一般正常使用，企业网站也就用个几十条，做SEO优化一周四篇文章，也足够用五年！）
            	<div style="padding-left:30px; padding-bottom:10px;">用满可续费，在原本的基础上增加，价格为&nbsp;1站币/500条/年</div>
            </td>
		</tr>
    </tbody>
</table>


<script type="text/javascript">
//代理开通15日内，登录会弹出网站快速开通的视频说明
try {
	var currentTime = Date.parse( new Date() ).toString();
	currentTime = currentTime.substr(0,10);
	if(currentTime - ${user.regtime } < 1296000){
		//多窗口模式，层叠置顶
		layer.open({
		  type: 2 //此处以iframe举例
		  ,title: '90秒学会，快速开通网站视频教程'
		  ,area: ['390px', '100%']
		  ,shade: 0
		  ,offset: 'rb'
		  ,maxmin: true
		  ,content: 'http://www.wscso.com/jianzhanDemo.html'
		  ,zIndex: layer.zIndex //重点1
		});
	}
} catch(error) {}
</script>



<jsp:include page="../iw/common/foot.jsp"></jsp:include>  