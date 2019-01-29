<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/head.jsp">
	<jsp:param name="title" value="邀请注册"/>
</jsp:include>

<div style="padding:10px; font-size:24px; text-align:center;">
	我的一级下线列表，共${size}人
</div>
<table class="layui-table iw_table">
	<thead>
		<tr>
			<th>ID</th>
	        <th>用户名</th>
	        <th>注册时间</th>
		</tr>
	</thead>
	<tbody>
	  	<c:forEach items="${userList}" var="user">
		    <tr>
		    	<td>${user.id }</td>
				<td>${user.username }</td>
				<td><x:time linuxTime="${user.regtime }" format="yy-MM-dd HH:mm"></x:time></td>
		    </tr>
		</c:forEach>
	</tbody>
</table>