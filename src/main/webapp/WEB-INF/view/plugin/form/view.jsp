<%@page import="com.xnx3.j2ee.entity.User"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="信息详情"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Form_state.js"></script>

<div style="padding:10px;">
	<button class="layui-btn layui-btn-primary" onclick="javascript:history.go(-1);"><i class="layui-icon">&#xe603;</i>   返回列表</button>
</div>

<table class="layui-table iw_table">
	<tbody>
		<tr>
			<td class="iw_table_td_view_name">信息标题</td>
			<td >${form.title }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">创建时间</td>
			<td><x:time linuxTime="${form['addtime'] }"></x:time></td>
		</tr>
</tbody>
</table>
<table class="layui-table iw_table">
	<tbody id="text">

	</tbody>
</table>
		
		<textarea id="jsonArray" style="display:none;">${jsonArray}</textarea>
		<script>
			var jsonArray = JSON.parse(document.getElementById("jsonArray").value);
			var text = "";
			for(var item in jsonArray){//遍历json对象的每个key/value对,p为key
				console.log(jsonArray[item]);
				for(var i in jsonArray[item]){
					text = '<tr><td class="iw_table_td_view_name">'+i+'</td><td>'+jsonArray[item][i]+'</td></tr>' + text;
				}
			}
			document.getElementById("text").innerHTML = text;
			console.log(text);
		</script>
		
		
  </tbody>
</table>

<jsp:include page="../../iw/common/foot.jsp"></jsp:include>  