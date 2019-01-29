<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="文章列表"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>News_type.js"></script>
<script src="/<%=Global.CACHE_FILE %>News_status.js"></script>
<script src="/<%=Global.CACHE_FILE %>News_legitimate.js"></script>

<jsp:include page="../../iw/common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="用户ID"/>
		<jsp:param name="iw_name" value="userid"/>
	</jsp:include>
	<jsp:include page="../../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="标题"/>
		<jsp:param name="iw_name" value="title"/>
	</jsp:include>
	<jsp:include page="../../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="是否合法"/>
		<jsp:param name="iw_name" value="legitimate"/>
		<jsp:param name="iw_type" value="select"/>
	</jsp:include>
	
	<input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
</form>	

<table class="layui-table iw_table">
	<thead>
		<tr>
			<th>文章id</th>
			<th>用户id</th>
			<th>标题</th>
			<th>类型</th>
			<th>合法性</th>
			<th>创建时间</th>
			<th>操作</th>
		</tr> 
	</thead>
	<tbody>
		<c:forEach items="${list}" var="news">
	       	<tr>
	            <td style="width:55px;">${news['id'] }</td>
	            <td onclick="userView(${news['userid'] });" style="cursor: pointer; width:55px;">${news['userid'] }</td>
	            <td onclick="newsView(${news['id']});" style="cursor: pointer; text-align:left;"><x:substring maxLength="30" text="${news['title'] }"></x:substring></td>
	            <td style="width:60px;"><script type="text/javascript">document.write(type[${news['type']}]);</script></td>
	            <td style="width:60px; cursor: pointer;" onclick="newsView(${news['id']});"><script type="text/javascript">document.write(legitimate[${news['legitimate']}]);</script></td>
	            <td style="width:100px;"><x:time linuxTime="${news['addtime'] }" format="yy-MM-dd hh:mm"></x:time></td>
	            <td style="width:100px;">
	            	<botton class="layui-btn layui-btn-sm" onclick="newsView(${news['id']});" style="margin-left: 3px;"><i class="layui-icon">&#xe60b;</i></botton>
	            	<a class="layui-btn layui-btn-sm" style="margin-left: 3px;" href="perview.do?id=${news['id']}" target="_black"><i class="layui-icon">&#xe615;</i></botton>
	            </td>
	        </tr>
		</c:forEach>
  </tbody>
</table>
                               
<!-- 通用分页跳转 -->
<jsp:include page="../../iw/common/page.jsp" ></jsp:include>

<script>
//查看文章详情信息
function newsView(id){
	layer.open({
		type: 2, 
		title:'查看文章信息', 
		area: ['460px', '580px'],
		shadeClose: true, //开启遮罩关闭
		content: '/admin/news/view.do?id='+id
	});
}

//查看用户信息
function userView(id){
	layer.open({
		type: 2, 
		title:'查看用户信息', 
		area: ['460px', '630px'],
		shadeClose: true, //开启遮罩关闭
		content: '/admin/user/view.do?id='+id
	});
}
</script>
<jsp:include page="../../iw/common/foot.jsp"></jsp:include>  