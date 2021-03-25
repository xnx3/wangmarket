<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.entity.User"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="站点信息"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Site_client.js"></script>

<table class="layui-table iw_table">
	<tbody>
		<tr>
			<td class="iw_table_td_view_name">站点编号</td>
			<td>${site.id }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">站点名称</td>
			<td>${site.name }</td>
		</tr>
		<tr>
			<td>网站类型</td>
			<td><script type="text/javascript">document.write(client[${site['client']}]);</script></td>
		</tr>
		
		<tr>
			<td class="iw_table_td_view_name">联系人</td>
			<td>${site.username }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">联系电话</td>
			<td>${site.phone }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">站点QQ</td>
			<td>${site.qq }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">二级域名</td>
			<td><a href="http://${site.domain }.<%=com.xnx3.wangmarket.admin.G.getFirstAutoAssignDomain() %>" target="_black">${site.domain }.<%=com.xnx3.wangmarket.admin.G.getFirstAutoAssignDomain() %></a></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">绑定域名</td>
			<td><a href="http://${site.bindDomain }" target="_black">${site.bindDomain }</a></td>
		</tr>
		<tr>
			<td>公司名</td>
			<td>${site.companyName }</td>
		</tr>
		<tr>
			<td>地址</td>
			<td>${site.address }</td>
		</tr>     
		<tr>
			<td class="iw_table_td_view_name">建立时间</td>
			<td><x:time linuxTime="${site['addtime'] }"></x:time></td>
		</tr>
    </tbody>
</table>


<script type="text/javascript">
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

function deleteNews(id){
	if(confirm("确定要删除吗？删除后不可恢复！")){
		window.location.href="delete.do?id="+id;
	}
}
function cancelLegitimate(id){
	window.location.href="cancelLegitimate.do?id="+id;
}
</script>

<jsp:include page="../../iw/common/foot.jsp"></jsp:include>  