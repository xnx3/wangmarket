<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="在线支付列表"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>PayLog_channel.js"></script>

<jsp:include page="../../common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="会员ID"/>
		<jsp:param name="iw_name" value="userid"/>
	</jsp:include>
	
    <jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="订单号"/>
		<jsp:param name="iw_name" value="orderno"/>
	</jsp:include>
    
    <jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="支付方式"/>
		<jsp:param name="iw_name" value="channel"/>
		<jsp:param name="iw_type" value="select"/>
	</jsp:include>
    
    <input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
    
</form>


<table class="layui-table iw_table">
  <thead>
    <tr>
		<th>编号</th>
        <th>用户昵称</th>
        <th>支付金额</th>
        <th>订单号</th>
        <th>支付方式</th>
        <th>操作时间</th>
    </tr> 
  </thead>
  <tbody>
  	<c:forEach items="${list}" var="payLog">
       	<tr>
            <td style="width:55px;">${payLog['id'] }</td>
            <td style="cursor: pointer;" onclick="userView(${payLog['userid'] });">${payLog['nickname'] }(ID:${payLog['userid'] })</td>
            <td>${payLog['money'] }</td>
            <td>${payLog['orderno'] }</td>
            <td><script type="text/javascript">document.write(channel['${payLog['channel']}']);</script></td>
            <td style="width:100px;"><x:time linuxTime="${payLog['addtime'] }" format="yy-MM-dd HH:mm"></x:time></td>
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