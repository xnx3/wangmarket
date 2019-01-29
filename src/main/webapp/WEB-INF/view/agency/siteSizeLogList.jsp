<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="资金变动日志"/>
</jsp:include>

<table class="layui-table iw_table">
	<thead>
		<tr>
			<th>变动数量</th>
			<th>变动前</th>
			<th>变动后</th>
			<th>描述</th>
			<th>时间</th>
		</tr> 
	</thead>
	<tbody>
		<c:forEach items="${list}" var="json">
			<tr>
                <td>${json['agencySiteSizeChange'] }</td>
                <td>${json['changeBefore'] }</td>
                <td class="numeric">${json['changeAfter'] }</td>
                <td class="numeric">${json['remark'] }</td>
                <td ><x:time linuxTime="${json['logtime'] }" format="yy-MM-dd hh:mm"></x:time></td>
			</tr>
		</c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../iw/common/page.jsp" ></jsp:include>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>