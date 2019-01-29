<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE HTML>
<html>
<head>
	<jsp:include page="../common/head.jsp">
    	<jsp:param name="title" value="注册"/>
    </jsp:include>
	<link href="/style/user/css/login.css" rel="stylesheet" type="text/css">
	
</head>
<body>
	<article id="container">
		<section class="section"><h1>注册</h1>
			<div class="loginBox">
				<h2><span>用户注册</span></h2>
				<form method="post" action="regSubmit.do">
					<ul>
					<li>
							<input type="text" placeholder="用户名" class="textInput username" name="username" >
							<span class="icon"><img src="/style/user/img/img1.png" width="20"></span>
						</li>
						<li>
							<input type="text" placeholder="邮箱" class="textInput username" name="email" >
							<span class="icon">邮箱</span>
						</li>
						<li>
							<input type="password" placeholder="请输入密码" class="textInput password" name="password">
							<span class="icon"><img src="/style/user/img/img2.png" width="20"></span>
						</li>
						<!-- 
							<dt>验证码：</dt>
							<dd><input type="text" class="code textInput" placeholder="验证码"></dd>
						-->
						<li><input class="btn" type="submit" value="注册"></li>
					</ul>
					<div class="btnBox"></div>
				</form>
			</div>
		</section>
	</article>
</body>
	</html>