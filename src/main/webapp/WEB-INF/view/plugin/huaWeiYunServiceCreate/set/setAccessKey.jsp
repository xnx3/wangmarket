<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../../iw/common/head.jsp">
	<jsp:param name="title" value="系统参数"/>
</jsp:include>

<table class="layui-table iw_table">
  <tbody>
	   	<tr>
	   		<td>Access Key Id ：</td>
	   		<td><%=Global.get("HUAWEIYUN_ACCESSKEYID") %></td>
			<td style="width:110px;">
				<botton class="layui-btn layui-btn-sm" onclick="variable('HUAWEIYUN_ACCESSKEYID');" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
			</td>
		</tr>
		<tr>
	   		<td>Access Key Secret ：</td>
	   		<td><%=Global.get("HUAWEIYUN_ACCESSKEYSECRET") %></td>
			<td style="width:110px;">
				<botton class="layui-btn layui-btn-sm" onclick="variable('HUAWEIYUN_ACCESSKEYSECRET');" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
			</td>
		</tr>
  </tbody>
</table>
<div style="height: 80px;line-height: 80px;text-align: right;padding-right:40px; font-size: 16px;">
	<span style="color: red;">注：</span>如果您尚未了解华为云Access Key Id 和 secret Access Keyd等信息，请<a href = "http://help.wang.market/16666.html" style="color: blue;" target = "_blank">&nbsp;点击这里&nbsp;</a>查看帮助文档
</div>	

<script type="text/javascript">
	/* 
	 * 修改／新增变量
	 * id 要修改的变量的id，若是为0，则是新增
	 */
	function variable(name){
		layer.open({
			type: 2, 
			title: name.length==0? '新增系统变量':'修改系统变量：&nbsp;&nbsp;'+name+'&nbsp;', 
			area: ['380px', '370px'],
			shadeClose: true, //开启遮罩关闭
			content: '/admin/system/variable.do?name='+name
		});
	}
</script>

<jsp:include page="../../../iw/common/foot.jsp"></jsp:include> 