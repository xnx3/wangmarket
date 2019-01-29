<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../common/head.jsp">
	<jsp:param name="title" value="个人中心"/>
</jsp:include>

<div style="padding:30px; text-align:center; font-size:30px;">个人中心接口及功能示例</div>

<hr/>
<div>
	<h3>修改头像</h3> 
	<img src="/upload/userHead/${user.head }" alt="">
	你可以选择png/jpg作为头像
	<form action="uploadHead.do" enctype="multipart/form-data" method="post">
		<input type="file" name="head" >
		<input type="submit" value="保存">
	</form>
</div>
<hr/>

<div>
修改昵称
<br/>
	<form method="post" action="updateNickName.do">
		<input type="text" name="nickname" value="${user.nickname}">
		<input type="submit" value="保存">
	</form>
</div>
<hr/>


<div>
修改密码
<br/>
	<form method="post" action="updatePassword.do">
		原密码：<input type="password" name="oldPassword">
		<br/>
		新密码：<input type="password" name="newPassword">
		<br/>
		<input type="submit" value="修改">
	</form>
</div>
<hr/>


<div>
发展下线
<br/>
	<a href="invite.do">邀请注册(发展下线)</a>｜
	<a href="inviteList.do">我的一级下线列表</a>
</div>
<hr/>

<div>
站内信息
<br/>
	<a href="../message/add.do">发送信息</a>｜
	<a href="../message/list.do">信息列表</a>
</div>
<hr/>

<div>
论坛
<br/>
	<a href="../bbs/list.do">论坛帖子列表</a>｜
	<a href="../bbs/addPost.do">我要发帖</a> 
</div>
<hr/>

<div>
关注
<br/>
	<a href="../collect/myList.do">关注我的列表</a>｜<a href="../collect/otherList.do">我关注的别人列表</a>｜
	<a href="../collect/add.do">添加关注</a> 
</div>
<hr/>

<div>
<a href="logout.do">退出登录</a>
</div>
	
<jsp:include page="../common/foot.jsp"></jsp:include> 