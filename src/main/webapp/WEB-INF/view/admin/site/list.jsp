<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="网站列表"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Site_client.js"></script>


<jsp:include page="../../iw/common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="站名"/>
		<jsp:param name="iw_name" value="name"/>
	</jsp:include>
	
	<input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
</form>	
                   
<table class="layui-table iw_table">
	<thead>
		<tr>
			<th>编号ID</th>
			<th>用户ID</th>
			<th>网站名</th>
			<th>域名</th>
			<th>创建时间</th>
			<th>到期时间</th>
		</tr> 
	</thead>
	<tbody>
		<c:forEach items="${list}" var="site">
			<tr>
				<td style="width:55px;"><a href="view.do?id=${site['id'] }">${site['id'] }</a></td>
				<td onclick="userView(${site['userid'] });" style="cursor: pointer; width:55px;">${site['userid'] }</td>
				<td><a href="javascript:siteView('${site['id'] }');" title="${site['name'] }"><x:substring maxLength="20" text="${site['name'] }"></x:substring> </a></td>
				<c:choose>
				    <c:when test="${!site.bindDomain.isEmpty() && fn:length(site.bindDomain) > 0}">
				        <td onclick="window.open('http://${site['bindDomain'] }'); " style="cursor: pointer; width: 180px;">${site['bindDomain'] }</td>
				    </c:when>
				    <c:otherwise>
				        <td onclick="window.open('http://${site['domain'] }.<%=com.xnx3.wangmarket.admin.G.getFirstAutoAssignDomain() %>'); " style="cursor: pointer; width: 180px;">${site['domain'] }.<%=com.xnx3.wangmarket.admin.G.getFirstAutoAssignDomain() %></td>
				    </c:otherwise>
				</c:choose>
				
				<td style="width:100px;"><x:time linuxTime="${site['addtime'] }" format="yy-MM-dd hh:mm"></x:time></td>
				<td style="width:100px;"><x:time linuxTime="${site['expiretime'] }" format="yy-MM-dd hh:mm"></x:time></td>
			</tr>
		</c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../../iw/common/page.jsp" ></jsp:include>

<script>
//查看站点详情信息
function siteView(id){
	layer.open({
		type: 2, 
		title:'查看站点信息', 
		area: ['460px', '470px'],
		shadeClose: true, //开启遮罩关闭
		content: '/admin/site/view.do?id='+id
	});
}

//查看用户信息
function userView(id){
	layer.open({
		type: 2, 
		title:'查看用户信息', 
		area: ['460px', '630px'],
		shadeClose: true, //开启遮罩关闭
		content: '/admin/user/view.do?id='+id
	});
}
</script>
<jsp:include page="../../iw/common/foot.jsp"></jsp:include>                  