<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../common/head.jsp">
	<jsp:param name="title" value="站内信列表"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Message_state.js"></script>

<div style="padding:10px; font-size:24px;">
	<a href="/message/add.do"  style="color:blue;">新建信息</a>｜
	<a href="/message/list.do?state=0&box=inbox"  style="color:blue;">收件箱未读消息</a>｜
	<a href="/message/list.do?state=1&box=inbox"  style="color:blue;">收件箱已读消息</a>｜
	<a href="/message/list.do?state=0&box=outbox"  style="color:blue;">发件箱未读消息</a>｜
	<a href="/message/list.do?state=1&box=outbox"  style="color:blue;">发件箱已读消息</a>
</div>
<div>
	<div style="padding:3px; font-size:20px;">信息列表</div>
	<table class="layui-table iw_table">
		<thead>
			<tr>
				<th>编号</th>
		        <th>发信用户</th>
		        <th>收信用户</th>
		        <th>信息内容</th>
		        <th>发信时间</th>
		        <th>信息状态</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="message">
            	<tr onclick="window.location.href='view.do?id=${message.id }';" style="cursor: pointer;">
	                 <td style="width:60px;">${message['id'] }</td>
	                 <td>${message['self_nickname'] }(ID:${message['sender'] })</td>
	                 <td>${message['other_nickname'] }(ID:${message['recipientid'] })</td>
	                 <td>${message['content'] }</td>
	                 <td><x:time linuxTime="${message['time'] }" format="yy-MM-dd HH:mm"></x:time></td>
	                 <td>
	                 	<script type="text/javascript">document.write(state['${message['state'] }']);</script>
	                 </td>
	             </tr>
            </c:forEach>
		</tbody>
	</table>
	<jsp:include page="../common/page.jsp"></jsp:include>
	
</div>		
		
<jsp:include page="../common/foot.jsp"></jsp:include> 