<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<!DOCTYPE html>
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="当前在线用户列表"/>
</jsp:include>

<table class="layui-table iw_table">
  <thead>
    <tr>
		<th>ID</th>
        <th>用户名</th>
        <th>昵称</th>
        <th>手机号</th>
        <th>最后上线时间</th>
    </tr> 
  </thead>
  <tbody>
  	<c:forEach items="${list}" var="user">
  		<tr>
          <td style="width:28px; cursor: pointer;" onclick="userView(${user.id });">${user.id }</td>
          <td style="width:58px; cursor: pointer;" onclick="userView(${user.id });">${user.username }</td>
          <td style="cursor: pointer;" onclick="userView(${user.id });"><x:substring maxLength="15" text="${user.nickname }"></x:substring> </td>
          <td style="width: 90px;">${user.phone }</td>
          <td style="width:100px;"><x:time linuxTime="${user.lasttime }" format="yy-MM-dd hh:mm"></x:time></td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<script>
//查看用户向信息信息
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

<jsp:include page="../../common/foot.jsp"></jsp:include>