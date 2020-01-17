<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../../iw/common/head.jsp">
        <jsp:param name="title" value="表单反馈管理"/>
    </jsp:include>
    <script src="/<%=Global.CACHE_FILE %>Form_state.js"></script>
</head>
<body>


<jsp:include page="../../iw/common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="状态"/>
		<jsp:param name="iw_name" value="state"/>
		<jsp:param name="iw_type" value="select"/>
	</jsp:include>
	
	<input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
	
	<div style="float:right; padding-right:10px;">
		<a class="layui-btn layui-btn-primary" href="http://tag.wscso.com/5022.html" target="_black">模板嵌入说明</a>
	</div>
</form>	


<style>
.read0{
	background-color:#fafafa;
}
</style>
<!-- 列表数据 -->
<table class="layui-table iw_table">
    <thead>
        <tr>
            <th>信息</th>
            <th class="numeric">状态</th>
            <th class="numeric">提交时间</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${list}" var="form">
            <tr class="read${form['state'] }">
                <td onclick="window.location.href='view.do?id=${form.id }';" style="cursor:pointer;">${form['title'] }</td>
                <td style="width:60px;"><script type="text/javascript">document.write(state['${form['state'] }']);</script></td>
                <td style="width:150px;"><x:time linuxTime="${form['addtime'] }" format="yy-MM-dd hh:mm"></x:time>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../../iw/common/page.jsp"></jsp:include>


<jsp:include page="../../iw/common/foot.jsp"></jsp:include>  