<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="聊天记录"/>
</jsp:include>
<style>
	body .layim-chat-main{height: auto;}
</style>


<div class="layim-chat-main">
  <ul id="LAY_view"></ul>
</div>

<div id="LAY_page" style="margin: 0 10px;"></div>
<textarea title="消息模版" id="LAY_tpl" style="display:none;">
	<c:forEach items="${list}" var="log">
		<c:choose>
		    <c:when test="${log.sendId != user.id}">
		    	<li>
		    </c:when>
		    <c:otherwise>
		    	<li class="layim-chat-mine">
		    </c:otherwise>
		</c:choose>
		
			<div class="layim-chat-user">
				<img src="http://cdn.weiunity.com/site/219/images/a8f08a77353a4a519465637fc9f4e522.png">
				<c:choose>
				    <c:when test="${log.sendId != user.id}">
						<cite>${log.sendUserName }<i><x:time linuxTime="${log.logtime }"></x:time></i></cite>
				    </c:when>
				    <c:otherwise>
				        <cite><x:time linuxTime="${log.logtime }"></x:time><i style="padding-left: 15px;padding-right: 0px;">${log.sendUserName }</i></cite>
				    </c:otherwise>
				</c:choose>
				
			</div>
			<div class="layim-chat-text">${log.content }</div>
		</li>
	</c:forEach>
</textarea>

<div style="padding:10px; color:gray;">
	提示，通过此所查看到的信息：<br/>
	1.只保留30天时间<br/>
	2.只显示前100条信息记录<br/>
	3.缓存时间1分钟，即用户会话沟通后，并不会立马在这里显示，要过1分钟之后才会在这里显示出来
	<a href="#foot" name="foot" id="gun_foot"></a>
</div>

<script src="http://res.weiunity.com/layui222/layui.js"></script>
<script>
layui.use(['layim', 'laypage'], function(){
  var layim = layui.layim
  ,layer = layui.layer
  ,laytpl = layui.laytpl
  ,$ = layui.jquery
  ,laypage = layui.laypage;
  
  //聊天记录的分页此处不做演示，你可以采用laypage，不了解的同学见文档：http://www.layui.com/doc/modules/laypage.html
  
  
  //开始请求聊天记录
  var param =  location.search //获得URL参数。该窗口url会携带会话id和type，他们是你请求聊天记录的重要凭据
  
  //实际使用时，下述的res一般是通过Ajax获得，而此处仅仅只是演示数据格式
  ,res = {
    code: 0
    ,msg: ''
    ,data: []
  }
  var html = laytpl(LAY_tpl.value).render({
    data: res.data
  });
  $('#LAY_view').html(html);
  //当前页面打开默认是显示最底部的
  document.getElementById('gun_foot').scrollIntoView()
});
</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>  