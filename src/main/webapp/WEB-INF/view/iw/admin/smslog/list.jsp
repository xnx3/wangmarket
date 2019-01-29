<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="手机短信验证码列表"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>SmsLog_used.js"></script>

<jsp:include page="../../common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="用户id"/>
		<jsp:param name="iw_name" value="userid"/>
	</jsp:include>
	
    <jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="手机号"/>
		<jsp:param name="iw_name" value="phone"/>
	</jsp:include>
    
    <jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="使用状态"/>
		<jsp:param name="iw_name" value="used"/>
		<jsp:param name="iw_type" value="select"/>
	</jsp:include>
    
    <input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
    
</form>


<table class="layui-table iw_table">
  <thead>
    <tr>
		<th>编号</th>
		<th>用户id</th>
        <th>手机号</th>
        <th>验证码</th>
        <th>是否使用</th>
        <th>时间</th>
    </tr> 
  </thead>
  <tbody>
  	<c:forEach items="${list}" var="smslog">
	   	<tr>
	        <td style="width:55px;">${smslog['id'] }</td>
	        <td style="width:58px; cursor: pointer;" onclick="userView(${smslog['userid'] });">${smslog['userid'] }</td>
	        <td>${smslog['phone'] }</td>
	        <td>${smslog['code'] }</td>
	        <td style="width:75px;"><script type="text/javascript">document.write(used[${smslog['used']}]);</script></td>
	        <td style="width:100px;"><x:time linuxTime="${smslog['addtime'] }" format="yy-MM-dd HH:mm"></x:time></td>
	    </tr>
   </c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../../common/page.jsp"></jsp:include>
<script type="text/javascript">
//查看用户详情信息
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

<jsp:include page="../../common/foot.jsp"></jsp:include>