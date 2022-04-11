<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.shiro.ShiroFunc"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="我的下线"/>
</jsp:include>

${topHtml}

<div class="weui_cells_title">将此地址发送别人，只要通过此地址进入的，注册成功后都是你的下线</div>
<div class="weui_cells weui_cells_form">
  <div class="weui_cell">
    <div class="weui_cell_bd weui_cell_primary">
      <textarea class="weui_textarea"rows="2"><%=Global.get("MASTER_SITE_URL") %>regByPhone.do?id=<%=ShiroFunc.getUser().getId() %></textarea>
    </div>
  </div>
</div>

<div class="weui_cells_title">只要自己的下线用户充值付费，即可获得其1%～30%的充值金额！共有四级下线模式，根据级数的不同提成不同。敬请期待。</div>
<div class="weui_cells_title">我的下线列表：</div>
<div class="weui_cells">
	<c:forEach items="${list}" var="user">
		<div class="weui_cell">
			<div class="weui_cell_bd weui_cell_primary">
				<p>${user.username }</p>
			</div>
			<div class="weui_cell_ft"><x:time linuxTime="${user.regtime }"></x:time></div>
		</div>
	</c:forEach>
</div>
 
</body>
</html>