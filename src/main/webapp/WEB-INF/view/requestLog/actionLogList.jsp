<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="操作日志"/>
</jsp:include>

<!-- 表单搜索 -->
<form method="get" class="layui-form" style="display:none;">
	<div class="layui-form-item">
		<label class="layui-form-label" style="width: auto;">
			关键词
			<i class="layui-icon" style="border: 1px solid #e2e2e2; padding:3px;border-radius: 30px; color: #626262;font-size: 12px; cursor: pointer;" onclick="window.open('https://help.aliyun.com/document_detail/29060.html');">&#xe607;</i>
		</label>
 		<div class="layui-input-inline">
			<input type="text" name="queryString" placeholder="请输入关键词" autocomplete="off" class="layui-input" value="<%=request.getParameter("queryString")==null? "":request.getParameter("queryString")  %>">
		</div>
		
		<input class="layui-btn" type="submit" value="搜索"/>
	</div>
</form>  
  
<!-- 列表数据 -->  
<table class="layui-table" style="margin-top:0px;">
	<thead>
		<tr>
			<th>动作</th>
			<th>备注说明</th>
			<th>操作时间</th>
		</tr> 
	</thead>
	<tbody>
		<c:forEach items="${list}" var="log">
			<tr>
				<td>${log.action }</td>
				<td>${log.remark }</td>
				<td><x:time linuxTime="${log.logtime }" format="yyyy-MM-dd HH:mm"></x:time></td>
			</tr>
		</c:forEach>	
	</tbody>
</table>  

<!-- 通用分页跳转 -->
<jsp:include page="../iw/common/page.jsp"></jsp:include>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>