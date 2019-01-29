<%@page import="com.xnx3.wangmarket.admin.entity.Site"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%
// 这个页面是，当用户在编辑模版页面的时候，点击顶部的模版变量，可以调出可用的模版变量列表
%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="模版变量列表"/>
</jsp:include>

<table class="layui-table" id="xnx3_body" style="margin:0px;">
  <thead>
    <tr>
      <th>调用代码</th>
      <th>备注说明</th>
    </tr> 
  </thead>
  <tbody class="tile__listedit" id="columnList">
  	<!-- display 显示或者隐藏，是否在导航中显示。若为0，则不加入排序 -->
  	<c:forEach items="${list}" var="templateVar">
        <tr>
        	<!--display${column['used']}-->
            <td>{include=${templateVar['varName'] }}</td>
            <td>${templateVar['remark'] }</td>
        </tr>
    </c:forEach>
  </tbody>
</table>
<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

</script>
</body>
</html>