<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="IM临时聊天查看"/>
</jsp:include>

<script type="text/javascript">
var sendUserName = '';	//发送人昵称
</script>
<div style="padding-top:10px; padding-left:10px;">发送方昵称：<span id="sendUserName">加载中...</span></div>
<table class="layui-table">
  <thead>
    <tr>
      <th style="width:80px; min-width:80px;">发送时间</th>
      <th>内容</th>
    </tr> 
  </thead>
  <tbody>
	<c:forEach items="${list}" var="log">
			<tr>
				<td><x:time linuxTime="${log.logtime }" format="MM-dd HH:mm"></x:time></td>
				<td>${log.content }</td>
				<script> if(sendUserName == ''){ sendUserName='${log.sendUserName }'; } </script>
			</tr>
	</c:forEach>	
	<script> document.getElementById("sendUserName").innerHTML = sendUserName; </script>
  </tbody>
</table>
<div style="padding:10px; color:gray;">
	提示，通过此通道所查看到的信息：<br/>
	1.只保留最近30天时间的消息<br/>
	2.只显示前100条信息记录<br/>
	3.缓存时间1分钟，即用户咨询留言后，并不会立马在这里显示，要过1分钟之后才会在这里显示出来
</div>


<jsp:include page="../iw/common/foot.jsp"></jsp:include>  