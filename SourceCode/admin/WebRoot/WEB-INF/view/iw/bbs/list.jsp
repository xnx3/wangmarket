<%@page import="com.xnx3.net.OSSUtil"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../common/head.jsp">
   	<jsp:param name="title" value="帖子列表"/>
</jsp:include>
<script src="<%=basePath+Global.CACHE_FILE %>Bbs_classid.js"></script>

<div style="padding:10px; font-size:20px;">
	<a href='addPost.do?classid=<%=request.getParameter("classid") == null? "":request.getParameter("classid")  %>' class="btn fR">我要发帖</a>
</div>

论坛板块：
<script type="text/javascript">writeSelectAllOptionForclassid('<%=request.getParameter("classid") %>');</script>


<div style="padding:3px; font-size:20px;">帖子列表</div>
<table class="layui-table iw_table">
	<thead>
		<tr>
			<th>用户头像</th>
	        <th>帖子标题</th>
	        <th>帖子简介</th>
	        <th>浏览数</th>
	        <th>发布时间</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="post">
           	<tr>
                 <td style="width:60px;"><img src="<%=OSSUtil.url %><%=Global.get("USER_HEAD_PATH") %>${post['head'] }" alt="${post['nickname'] }" width="50"></td>
                 <td><a href="view.do?id=${post['id'] }"><x:substring maxLength="10" text="${post['title'] }"></x:substring>  </a></td>
                 <td><a href="view.do?id=${post['id'] }"><x:substring maxLength="10" text="${post['info'] }"></x:substring></a></td>
                 <td>${post['view'] }</td>
                 <td><x:time linuxTime="${post['addtime'] }" format="yy-MM-dd HH:mm"></x:time></td>
             </tr>
           </c:forEach>
	</tbody>
</table>
<jsp:include page="../common/page.jsp"></jsp:include>
				
<jsp:include page="../common/foot.jsp"></jsp:include> 