<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../common/head.jsp">
	<jsp:param name="title" value="我的关注列表"/>
</jsp:include>


<div style="text-align:left; padding:20px;">
	<div>
	<a href="addPost.do?classid=${post.classid }" class="btn fR">我要发帖</a>
	<a href="list.do?classid=${postVO.post.classid }" class="btn fR">返回${postVO.postClass.name }</a>
	</div>
	<div>
		帖子id编号：${postVO.post.id }<br/>
		帖子标题：${postVO.post.title }<br/>
		帖子简介：${postVO.post.info }<br/>
		帖子点击量：${postVO.post.view }<br/>
		帖子发布时间：<x:time linuxTime="${postVO.post.addtime }"></x:time><br/>
		帖子内容：${postVO.text }<br/>
	</div>
	<br/>
	<div>
		帖子发布者用户id：${postVO.user.id }<br/>
		帖子发布者用户名：${postVO.user.username }<br/>
		帖子发布者用户昵称：${postVO.user.nickname }<br/>
		帖子发布者用户邮箱：${postVO.user.email }<br/>
		帖子发布者用户手机号：${postVO.user.phone }<br/>
		帖子发布者用户金钱：${postVO.user.currency }<br/>
		帖子发布者用户最后上线时间：<x:time linuxTime="${postVO.user.lasttime }"></x:time><br/>
		帖子发布者用户注册时间：<x:time linuxTime="${postVO.user.regtime }"></x:time><br/>
		帖子发布者用户最后上线ip：${postVO.user.lastip }<br/>
		帖子发布者用户头像：<img src="<%=basePath %>upload/userHead/${postVO.user.head }" /><br/>
	</div>
	<br/>
	<div>
		此贴所属栏目id编号：${postVO.postClass.id }<br/>
		此贴所属栏目名称：${postVO.postClass.name }<br/>
	</div>
	<br/>
	<div>
		此贴评论总数：${postVO.commentCount }<br/>
	</div>
	<br/>
	<br/>
	<br/>
	<h2>帖子回复</h2>
	<hr/>
	<div>
		<c:forEach items="${commentList}" var="postComment">
			<div style="padding:15px; width:250px; float:left;">
				回帖者用户id：${postComment['id'] }<br/>
				回帖者头像：<img src="<%=basePath %>upload/userHead/${postComment['head'] }"><br/>
				回帖者昵称：${postComment['nickname'] }<br/>
				回帖时间：<x:time linuxTime="${postComment['addtime'] }"></x:time><br/>
				回帖内容：${postComment['text'] }<br/>	
			</div>
		</c:forEach>
		<div style="padding:15px; width:200px; float:none;">&nbsp;</div>
	</div>
	<div>
		<form action="addCommentSubmit.do" method="post">
			<input type="hidden" name="postid" value="${postVO.post.id }" />
			<textarea rows="8" cols="50" name="text"></textarea>限制<%=Global.bbs_commentTextMinLength %>~<%=Global.bbs_commentTextMaxLength %>字母及汉字
			<br/>
			<input type="submit" value="提交" class="btn btn01"></li>
		</form>
	</div>
</div>

<jsp:include page="../common/foot.jsp"></jsp:include> 