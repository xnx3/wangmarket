<%@page import="com.xnx3.net.MailUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.domain.G"%>
<%@page import="com.xnx3.wangmarket.admin.entity.Site"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="网站基本设置"/>
</jsp:include>
<!-- weui，一个UI框架，这个包含weui，依赖Jquery -->
<script src="${STATIC_RESOURCE_PATH}js/jquery-weui.js"></script>
<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}css/jquery-weui.css">
<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}css/weui.min.css">

<body style="text-align:left; min-width:10px;">
<div class="weui_cells weui_cells_access" style="margin-top: 0em;">
	<a class="weui_cell" href="javascript:updateTitle();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>网站标题</p>
		</div>
		<div class="weui_cell_ft" id="nameDiv">${site.name }</div>
	</a>
	<a class="weui_cell" href="javascript:updateDomain_info();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>分配域名</p>
		</div>
		<div class="weui_cell_ft" id="domainInput">
			<span style="float: left;">
				<div id="domainInput_wangmarket">http://${site.domain }.${autoAssignDomain }</div>
			</span>
		</div>
	</a>
	<a class="weui_cell" href="javascript:updateBindDomain_info();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>绑定域名</p>
		</div>
		<div class="weui_cell_ft" id="domainInput">
			<span style="float: left;">
				<div id="domainInput_bind">${site.bindDomain }</div>
			</span>
		</div>
	</a>
	<a class="weui_cell" href="javascript:updateDiBuLianXi();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>联系人姓名</p>
		</div>
		<div class="weui_cell_ft">${site.username }</div>
	</a>
	<a class="weui_cell" href="javascript:updateDiBuLianXi();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>联系电话</p>
		</div>
		<div class="weui_cell_ft">${site.phone }</div>
	</a>
	<a class="weui_cell" href="javascript:updateDiBuLianXi();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>联系QQ</p>
		</div>
		<div class="weui_cell_ft">${site.qq }</div>
	</a>
	<a class="weui_cell" href="javascript:updateDiBuLianXi();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>公司(团体)名</p>
		</div>
		<div class="weui_cell_ft">${site.companyName }</div>
	</a>
	<a class="weui_cell" href="javascript:updateDiBuLianXi();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>办公地址</p>
		</div>
		<div class="weui_cell_ft">${site.address }</div>
	</a>
	
	<% //判断是否已开启邮件发送功能
		if(MailUtil.username != null && MailUtil.username.length() > 0 || MailUtil.password != null && MailUtil.password.length() > 0){
	%>
		<a class="weui_cell" href="javascript:;">
			<div class="weui_cell_bd weui_cell_primary">
				<p>我的邮箱</p>
			</div>
			<div class="weui_cell_ft">${user.email }</div>
		</a>
	<% } %>
	
	

</div>
<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

//改网站名字
function updateTitle(){
	parent.layer.close(index);
	//window.parent.siteNameClick();
	parent.updateSiteName();
}

//更改网站二级域名
function updateDomain_info(){
	parent.layer.close(index);
	parent.$('#subWindowsParam').text('${site.domain }');
	parent.updateDomain();
}

//更改网站自己绑定的域名
function updateBindDomain_info(){
	//parent.layer.close(index);
	//parent.$('#subWindowsParam').text('${site.bindDomain }');
	parent.updateBindDomain();
}

//更改网站的相关联系信息
function updateDiBuLianXi(){
	parent.layer.close(index);
	parent.updateFooterSiteInfo();
}
</script>

</body>
</html>