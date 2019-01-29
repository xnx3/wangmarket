<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../common/head.jsp">
	<jsp:param name="title" value="我的关注列表"/>
</jsp:include>
		
		<div><h1>我的关注列表</h1><a href="/collect/add.do"  style="color:blue;">添加关注</a></div>
		
		<section id="unseen">
	        <table class="table table-bordered table-striped table-condensed">
	            <thead>
		            <tr>
		                <th>用户ID</th>
		                <th>昵称</th>
		                <th class="numeric">关注时间</th>
		                <th class="numeric">操作</th>
		            </tr>
	            </thead>
	            
	            <tbody>
		            <c:forEach items="${list}" var="collect">
		            	<tr>
			                 <td>${collect['othersid'] }</td>
			                 <td>${collect['nickname'] }</td>
			                 <td class="numeric"><x:time linuxTime="${collect['addtime'] }"></x:time></td>
			                 <td class="numeric">
			                 	<a href="/collect/cancelCollect.do?othersid=${collect['othersid']}" style="color:blue;">取消关注</a>
			                 </td>
			             </tr>
		            </c:forEach>
	            </tbody>
	        </table>
	    </section>
	    <!-- 通用分页跳转 -->
	    <jsp:include page="../common/page.jsp"></jsp:include>
		
	</section>

<jsp:include page="../common/foot.jsp"></jsp:include> 