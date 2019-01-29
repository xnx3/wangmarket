<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="用户动作"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Log_type.js"></script>

<table class="layui-table iw_table">
	<thead>
		<tr>
			<th>操作人</th>
			<th>网站名字</th>
			<th>动作</th>
			<th>操作时间</th>
			<th>执行方法</th>
		</tr> 
	</thead>
	<tbody>
		<c:forEach items="${list}" var="log">
			<tr>
				<td onclick="userView(${log.userid });" style="width:70px; cursor: pointer;"><x:substring maxLength="10" text="${log.username }"></x:substring> </td>
				<td><a href="http://${log.siteDomain }" target="_black" title="${log.siteDomain }"><x:substring maxLength="12" text="${log.siteName }"></x:substring> </a></td>
				<td><a href="javascript:;" title="${log.remark }" style="cursor: default;"><x:substring maxLength="30" text="${log.remark }"></x:substring> </a></td>
				<td style="width:100px;"><x:time linuxTime="${log.logtime }" format="yy-MM-dd hh:mm"></x:time></td>
				<td style="width:100px;"><a href="javascript:;" title="${log.className }.${log.methodName }()"><x:substring maxLength="12" text="${log.methodName }"></x:substring> </td>
			</tr>
		</c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../../iw/common/page.jsp" ></jsp:include>

<script>
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