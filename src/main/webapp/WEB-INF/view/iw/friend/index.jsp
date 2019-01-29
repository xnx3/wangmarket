<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../common/head.jsp">
	<jsp:param name="title" value="我的好友"/>
</jsp:include>

<script>
	$(function(){
		$.post('/friend/list.do', function (data) {
			if (data.result) {
			$('.iconText').html(data.size);
			$.each(data.list, function (index, value) {
					var str = '<li>';
					str += '<div class="photoBox" uid="'+ value.other +'">';
					str += '<img src="" alt="" class="photo"><img src="<%=basePath %>style/user/img/common/online.png" width="20" alt="" class="onLine">';
					str += '<span class="close">X</span></div>';
					str += '<p>'+ value.other +'</p></li>'
				$(".listUl").append(str);
			});
				$(".close").click(function(){
					var isDel = confirm('确认移除?');
					var uid = $(this).parent().attr("uid");
					var delre = $(this).parents("li");
					   		if (isDel) {
						   		$.post('/friend/delete.do', {id : uid}, function (data) {
						   				if (data.result) {
						   					delre.remove();
						   				} else {
						   					alert(data.info);
						   				}
						   			}, 'json');
						   		}
				});
			} else {
				alert(data.info);
			}
		}, 'json');
	//添加朋友
	$(".addDiv").click(function(){
		$("#addFriendInfo2").hide();
		$("#addFriendInfo").show();
	});
	$("#addFriendInfo .add").click(function(){
		var uname = $(".textInput");
		if(uname.val()==''){
			alert('添加好友不能为空');
			uname.focus();
			return;
		}
   		$.post('/friend/add.do', {param : uname.val()}, function (data) {

   				if (data.result==1) {
   				$("#addFriendInfo").hide();
   					var str = '<li>';
					str += '<div class="photoBox" uid="">';
					str += '<img src="" alt="" class="photo"><img src="<%=basePath %>style/user/img/common/online.png" width="20" alt="" class="onLine">';
					str += '<span class="close">X</span></div>';
					str += '<p>马云2</p></li>'
				$(".listUl").append(str);
   				} else {
   					alert(data.info);
   				}
   			}, 'json');
	});
	$("#addFriendInfo .invite").live("click",function(){
		$("#addFriendInfo").hide();
		$("#addFriendInfo2").show();
	});
	$("#addFriendInfo .winClose").live("click",function(){
		$("#addFriendInfo").hide();
	});
	$("#addFriendInfo2 .winClose").live("click",function(){
		$("#addFriendInfo2").hide();
	});
	$("#addFriendInfo2 .cancel").click(function(){
		$("#addFriendInfo2").hide();
	});
	$("#addFriendInfo2 .btn").click(function(){
		$("#addFriendInfo2").hide();
	});
	$(".addMail li button").live("click",function(){
		$(this).parents("li").remove();
	});
});
</script>

		<div id="conts">
			<ul class="naviUl clearfix">
				<li><a href="../user/info.do">用户中心</a></li>
				<li><a href="../project/list.do">项目组管理</a></li>
				<li class="on"><a href="./friend/index.do">好友管理</a></li>
			</ul>
			<h3 class="clearfix"><img src="/style/user/img/common/star01.png" alt="">所有成员<span class="iconText"></span></h3>
			<ul class="listUl clearfix">
				<li>
					<div class="addDiv">+</div>
					<p>添加成员</p>
				</li>
			</ul>
			<div id="addFriendInfo">
				<dl>
					<dt>添加好友<span class="winClose">X</span></dt>
					<dd>
						<form action="add.do">
							<input type="text" placeholder="请输入用户名或邮箱" class="textInput" name="param">
							<input type="submit" value="添加" class="add fR" />
						</form>
						
					</dd>
					<dd class="clearfix"><p class="fL" style="line-height: 40px;">好友还未注册? 赶紧通过邮件邀请吧!</p><button class="btn invite fR">邀请好友</button></dd>
				</dl>
			</div>
			<div id="addFriendInfo2" class="none">
				<form action="/user/inviteEmail.do" method="post">
					<dl>
						<dt>邀请好友<span class="winClose">X</span></dt>
						<dd class="borderB">
							<p class="colorRed">请输入好友邮箱</p>
							<ul class="addMail">
								<!--
		 							<li>
										<input type="email" name="email" placeholder="请输入邮箱">
										<button>删除</button>
									</li> 
								-->
								<li>
									<input type="email" name="email" placeholder="请输入邮箱">
									<button>删除</button>
								</li>
							</ul>
							<!-- <button class="add mb10">再添加一个</button> -->
							<br>
							<textarea cols="5" rows="5"></textarea>
							<div class="btnBox">
								<input type="submit" class="btn" value="发送邀请" />
								<button class="cancel">取消</button>
							</div>
						</dd>
					</dl>
				</form>	
			</div>
		</div>
		


<jsp:include page="../common/foot.jsp"></jsp:include> 