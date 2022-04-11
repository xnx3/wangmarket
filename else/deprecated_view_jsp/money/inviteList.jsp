<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="我邀请的下级列表"/>
</jsp:include>

<body>

<table class="layui-table" lay-even style="margin-top:0px; margin-bottom: -1px;" id="table">
  <colgroup>
    <col width="80">
    <col>
  </colgroup>
  <thead>
    <tr>
      <th>用户名</th>
      <th>注册时间</th>
    </tr> 
  </thead>
  <tbody>
	<c:forEach items="${userList}" var="user">
	    <tr>
	      <td>${user.username }</td>
	      <td><x:time linuxTime="${user.regtime }"></x:time></td>
	    </tr>
	</c:forEach>
  </tbody>
</table>
<blockquote class="layui-elem-quote layui-quote-nm" style="margin-bottom: 0px;
    margin-top: 20px;
    border-width: 1px 1px 1px 1px;
    margin: 15px;
    border-radius: 10px;" id="tishi">	
	您当前还没有下线，赶紧邀请他人注册成为你的下线吧！
	<br/>
	<a href="index.do" class="layui-btn" target="_black" style="margin-top:8px;">查看如何邀请他人注册</a>
</blockquote>

<script>
if('${size}' == '0'){
	document.getElementById("table").style.display='none';
	document.getElementById("tishi").style.display='';
}else{
	document.getElementById("table").style.display='';
	document.getElementById("tishi").style.display='none';
}
</script>


</body>
</html>