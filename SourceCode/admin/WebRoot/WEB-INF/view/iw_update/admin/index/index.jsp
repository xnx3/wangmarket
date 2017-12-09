<%@page import="com.xnx3.admin.G"%>
<%@page import="com.xnx3.j2ee.entity.User"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../../../iw/common/head.jsp">
	<jsp:param name="title" value="管理后台"/>
</jsp:include>
<script src="http://res.weiunity.com/js/fun.js"></script>
<script>
var masterSiteUrl = '<%=G.masterSiteUrl %>'; 
</script>
<script src="http://res.weiunity.com/js/commonedit.js?v=<%=G.VERSION %>"></script>

<style>
body{margin: 0;padding: 0px;height: 100%;overflow: hidden;}
#editPanel{
	position: absolute;
    top: 0px;
    width:150px;
}
#editPanel span{
	width:100%;
}

.menu{
	width:150px;
	height:100%;
	background-color: #393D49;
	position: absolute;
}
.menu ul li{
	cursor: pointer;
}

/*左侧的一级菜单的图标*/
.firstMenuIcon{
	font-size:16px;
	padding-right:8px;
	font-weight: 700;
}
/*左侧的一级菜单的文字描述*/
.firstMenuFont{
	
}

/* 二级菜单 */
.menu .layui-nav-item .layui-nav-child .subMenuItem{
	padding-left:48px;
}
</style>

<div id="leftMenu" class="layui-nav layui-nav-tree layui-nav-side menu">
	<ul class="">
		<shiro:hasPermission name="adminUser"> 
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('<%=basePath %>admin/user/list.do');">
					<i class="layui-icon firstMenuIcon">&#xe612;</i>
					<span class="firstMenuFont">用户管理</span>
				</a>
			</li>
		</shiro:hasPermission>
		<shiro:hasPermission name="adminSite">
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('<%=basePath %>admin/site/list.do');">
					<i class="layui-icon firstMenuIcon">&#xe857;</i>
					<span class="firstMenuFont">网站管理</span>
				</a>
			</li>
		</shiro:hasPermission>
		<shiro:hasPermission name="adminSite">
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('<%=basePath %>admin/news/list.do');">
					<i class="layui-icon firstMenuIcon">&#xe632;</i>
					<span class="firstMenuFont">文章管理</span>
				</a>
			</li>
		</shiro:hasPermission>
		<shiro:hasPermission name="adminMessage"> 
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('<%=basePath %>admin/message/list.do');">
					<i class="layui-icon firstMenuIcon">&#xe63a;</i>
					<span class="firstMenuFont">站内信息</span>
				</a>
			</li>
		</shiro:hasPermission>
			
		<shiro:hasPermission name="adminBbs"> 
			<li class="layui-nav-item">
				<a href="javascript:;">
					<i class="layui-icon firstMenuIcon">&#xe606;</i>
					<span class="firstMenuFont">论坛管理</span>
				</a>
				<dl class="layui-nav-child">
					<shiro:hasPermission name="adminBbsClassList">
						<dd><a class="subMenuItem" href="javascript:loadUrl('<%=basePath %>/admin/bbs/classList.do');">分类板块</a></dd>
					</shiro:hasPermission>
					<shiro:hasPermission name="adminBbsPostList"> 
						<dd><a class="subMenuItem" href="javascript:loadUrl('<%=basePath %>/admin/bbs/postList.do');">帖子管理</a></dd>
					</shiro:hasPermission>
					<shiro:hasPermission name="adminBbsPostCommentList">
						<dd><a class="subMenuItem" href="javascript:loadUrl('<%=basePath %>/admin/bbs/commentList.do');">回帖管理</a></dd>
					</shiro:hasPermission>
				</dl>
			</li>
		</shiro:hasPermission>
			
		<shiro:hasPermission name="adminLog"> 
			<li class="layui-nav-item">
				<a href="javascript:;">
					<i class="layui-icon firstMenuIcon">&#xe62c;</i>
					<span class="firstMenuFont">日志统计</span>
				</a>
				<dl class="layui-nav-child">
					<shiro:hasPermission name="adminLogList">
						<dd><a class="subMenuItem" href="javascript:loadUrl('<%=basePath %>admin/log/list.do');">用户动作</a></dd>
					</shiro:hasPermission>
					<shiro:hasPermission name="adminLog">
						<dd><a class="subMenuItem" href="javascript:loadUrl('<%=basePath %>admin/requestLog/fangwenList.do');">访问记录</a></dd>
					</shiro:hasPermission>
					<shiro:hasPermission name="adminSmsLog"> 
						<dd><a class="subMenuItem" href="javascript:loadUrl('<%=basePath %>admin/smslog/list.do');">手机验证</a></dd>
					</shiro:hasPermission>
					<shiro:hasPermission name="adminPayLog"> 
						<dd><a class="subMenuItem" href="javascript:loadUrl('<%=basePath %>admin/payLog/list.do');">在线支付</a></dd>
					</shiro:hasPermission>
					<shiro:hasPermission name="adminLogCartogram">
						<dd><a class="subMenuItem" href="javascript:loadUrl('<%=basePath %>admin/log/cartogram.do');">动作统计</a></dd>
					</shiro:hasPermission>
					<shiro:hasPermission name="adminLog">
						<dd><a class="subMenuItem" href="javascript:loadUrl('<%=basePath %>admin/requestLog/fangwentongji.do');">访问统计</a></dd>
					</shiro:hasPermission>
				</dl>
			</li>
		</shiro:hasPermission>
		
		<shiro:hasPermission name="adminRole"> 
			<li class="layui-nav-item">
				<a href="javascript:;">
					<i class="layui-icon firstMenuIcon">&#xe628;</i>
					<span class="firstMenuFont">权限管理</span>
				</a>
				<dl class="layui-nav-child">
					<shiro:hasPermission name="adminRoleRoleList">
						<dd><a class="subMenuItem" href="javascript:loadUrl('<%=basePath %>admin/role/roleList.do');">角色管理</a></dd>
					</shiro:hasPermission>
					<shiro:hasPermission name="adminRolePermissionList"> 
						<dd><a class="subMenuItem" href="javascript:loadUrl('<%=basePath %>admin/role/permissionList.do');">资源管理</a></dd>
					</shiro:hasPermission>
				</dl>
			</li>
		</shiro:hasPermission>
		
		<shiro:hasPermission name="adminSystem"> 
			<li class="layui-nav-item">
				<a href="javascript:;">
					<i class="layui-icon firstMenuIcon">&#xe614;</i>
					<span class="firstMenuFont">系统管理</span>
				</a>
				<dl class="layui-nav-child">
					<shiro:hasPermission name="adminOnlineUserList">
						<dd><a class="subMenuItem" href="javascript:loadUrl('<%=basePath %>admin/user/onlineUserList.do');">在线会员</a></dd>
					</shiro:hasPermission>
					<shiro:hasPermission name="adminSystemVariable"> 
						<dd><a class="subMenuItem" href="javascript:loadUrl('<%=basePath %>admin/system/variableList.do?orderBy=lasttime_ASC');">系统变量</a></dd>
					</shiro:hasPermission>
				</dl>
			</li>
		</shiro:hasPermission>
		
		<!-- agency start -->
		<shiro:hasPermission name="agencyIndex">
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('<%=basePath %>agency/userList.do');">
					<i class="layui-icon firstMenuIcon">&#xe857;</i>
					<span class="firstMenuFont">网站管理</span>
				</a>
			</li>
		</shiro:hasPermission>
		<shiro:hasPermission name="agencyUserList">
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('<%=basePath %>agency/subAgencyList.do?orderBy=expiretime_ASC');">
					<i class="layui-icon firstMenuIcon">&#xe612;</i>
					<span class="firstMenuFont">下级代理</span>
				</a>
			</li>
		</shiro:hasPermission>
		<shiro:hasPermission name="AgencyNormalAdd">
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('<%=basePath %>agency/actionLogList.do');">
					<i class="layui-icon firstMenuIcon">&#xe62a;</i>
					<span class="firstMenuFont">操作日志</span>
				</a>
			</li>
		</shiro:hasPermission>
		<shiro:hasPermission name="AgencyNormalAdd">
			<li class="layui-nav-item">
				<a href="javascript:loadUrl('<%=basePath %>agency/siteSizeLogList.do');">
					<i class="layui-icon firstMenuIcon">&#xe62a;</i>
					<span class="firstMenuFont">站币日志</span>
				</a>
			</li>
		</shiro:hasPermission>
		<!-- agency end -->
		
		<li class="layui-nav-item">
			<a href="javascript:;">
				<i class="layui-icon firstMenuIcon">&#xe63a;</i>
				<span class="firstMenuFont">客服管理</span>
			</a>
			<dl class="layui-nav-child">
				<dd><a id="im_menu" class="subMenuItem" href="javascript:openKefuSet();">基本设置</a></dd>
				<dd><a id="im_hostory" class="subMenuItem" href="javascript:loadUrl('<%=basePath %>im/hostoryChatList.do');">历史咨询</a></dd>
			</dl>
		</li>
		
		<li class="layui-nav-item">
			<a href="javascript:updatePassword();" id="xiugaimima">
				<i class="layui-icon firstMenuIcon">&#xe642;</i>
				<span class="firstMenuFont">更改密码</span>
			</a>
		</li>
		<li class="layui-nav-item">
			<a href="<%=basePath %>user/logout.do">
				<i class="layui-icon firstMenuIcon">&#xe633;</i>
				<span class="firstMenuFont">退出登陆</span>
			</a>
		</li>
		
		<li class="layui-nav-item" style="position: absolute;bottom: 0px; text-align:center;">
			<a href="http://github.com/xnx3/iw" target="_black">
				<span class="firstMenuFont">power by iw</span>
			</a>
		</li>
	</ul>
</div>


<div id="content" style="width: 100%;height:100%;position: absolute;left: 150px;word-wrap: break-word;border-right: 150px;border-right-style: dotted;box-sizing: border-box;">
	<iframe name="iframe" id="iframe" frameborder="0" style="width:100%;height:100%;box-sizing: border-box;"></iframe>
</div>

<script>
layui.use('element', function(){
  var element = layui.element;
});

/**
 * 在主体内容区域iframe中加载制定的页面
 * url 要加载的页面的url
 */
function loadUrl(url){
	document.getElementById("iframe").src=url;
}

//加载登录后的默认页面
loadUrl('<%=basePath %>${indexUrl}');
</script>

<!-- IM start -->
<script src="http://res.weiunity.com/layui217/layui.js"></script>
<script>
var id = ${user.id};	//用户的id，用户唯一
var password = "${password }";	//加密后密码
var username = "${user.nickname }";	//用户昵称，用户在聊天框显示的名字
var sign = '';	//当前用户签名
var socketUrl = '<%=G.websocketUrl %>'; //socket的url请求地址
</script>
<script src="http://res.weiunity.com/js/im/admin.js"></script>
<!-- IM end -->

</body>
</html>
