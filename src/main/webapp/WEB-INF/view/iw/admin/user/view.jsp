<%@page import="com.xnx3.j2ee.entity.User"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="会员资料信息"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Role_role.js"></script>
<script src="/<%=Global.CACHE_FILE %>User_isfreeze.js"></script>

<table class="layui-table iw_table">
	<tbody>
		<tr>
			<td class="iw_table_td_view_name">用户id</td>
			<td>${u.id }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">用户名</td>
			<td>${u.username }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">昵称</td>
			<td>${u.nickname }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">邮箱</td>
			<td>${u.email }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">手机号</td>
			<td>${u.phone }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">权限</td>
			<td>
	           	<script type="text/javascript">writeName('${u.authority }');</script>
	           	<botton class="layui-btn layui-btn-sm" onclick="editRole();" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">ta的推荐人</td>
			<td>${referrer }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">注册时间</td>
			<td><x:time linuxTime="${u.regtime }"></x:time></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">注册ip</td>
			<td>${u.regip }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">最后登陆时间</td>
			<td><x:time linuxTime="${u.lasttime }"></x:time></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">最后登陆ip</td>
			<td>${u.lastip }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">用户状态</td>
			<td>
				当前<script type="text/javascript">document.write(isfreeze['${u.isfreeze }']);</script>
				&nbsp;&nbsp;
				<c:choose>
					<c:when test="${u.isfreeze == 0}">
						<a class="layui-btn layui-btn-sm" href="/admin/user/updateFreeze.do?id=${u.id }&isfreeze=<%=User.ISFREEZE_FREEZE %>" style="margin-left: 3px;">冻结账户</a>
					</c:when>
					<c:otherwise>
						<a class="layui-btn layui-btn-sm" href="/admin/user/updateFreeze.do?id=${u.id }&isfreeze=<%=User.ISFREEZE_NORMAL %>" style="margin-left: 3px;">解除冻结</a>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">账户余额</td>
			<td>${u.money }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">冻结金额</td>
			<td>${u.freezemoney }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name"><%=Global.get("CURRENCY_NAME") %></td>
			<td>${u.currency }</td>
		</tr>
	</tbody>
</table>

<script type="text/javascript">
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

//修改权限
function editRole(){
	layer.open({
		type: 2, 
		title:'修改&nbsp;[&nbsp;${u.username}&nbsp;]&nbsp;权限', 
		area: ['auto', 'auto'],
		shadeClose: true, //开启遮罩关闭
		content: '/admin/role/editUserRole.do?userid=${u.id}'
	});
}
</script>
<jsp:include page="../../common/foot.jsp"></jsp:include>