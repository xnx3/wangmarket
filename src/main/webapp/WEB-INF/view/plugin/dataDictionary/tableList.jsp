<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../../iw/common/head.jsp">
        <jsp:param name="title" value="数据表列表"/>
    </jsp:include>
    <script src="/<%=Global.CACHE_FILE %>Form_state.js"></script>
</head>
<body>


<div style="padding: 12px;">
	共有 ${tableNum } 个数据表
</div>
<!-- 列表数据 -->
<table class="layui-table iw_table">
    <thead>
        <tr onclick="">
            <th>数据表名字</th>
            <th class="numeric">备注</th>
            <th class="numeric">已存储行数</th>
            <th class="numeric">数据大小</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${list}" var="item">
            <tr onclick="view('${item['TABLE_NAME'] }');" style="cursor:pointer; ">
                <td style="width: 190px;">${item['TABLE_NAME'] }</td>
                <td style="">${item['TABLE_COMMENT'] }</td>
                <td style="width:90px;">${item['TABLE_ROWS'] }</td>
                <td style="width:60px;">${item['DATA_LENGTH'] }</td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<div style="height: 30px;">&nbsp;</div>

<script>
//查看数据表详情信息
function view(tableName){
	layer.open({
		type: 2, 
		title:'查看数据表 '+tableName, 
		area: ['980px', '470px'],
		shadeClose: true, //开启遮罩关闭
		content: '/plugin/dataDictionary/tableView.do?tableName='+tableName
	});
}
</script>

<jsp:include page="../../iw/common/foot.jsp"></jsp:include>  