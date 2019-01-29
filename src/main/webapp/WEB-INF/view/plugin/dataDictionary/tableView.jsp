<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../../iw/common/head.jsp">
        <jsp:param name="title" value="数据表详情"/>
    </jsp:include>
    <script src="/<%=Global.CACHE_FILE %>Form_state.js"></script>
</head>
<body>


<!-- 列表数据 -->
<table class="layui-table iw_table">
    <thead>
        <tr>
            <th>列名</th>
            <th class="numeric">数据类型</th>
            <th class="numeric">是否为空</th>
            <th class="numeric">默认值</th>
            <th class="numeric">备注</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${list}" var="item">
            <tr>
                <td style="width: 150px;">${item['COLUMN_NAME'] }</td>
                <td style="width:120px;">${item['COLUMN_TYPE'] }</td>
                <td style="width:70px;">${item['IS_NULLABLE'] }</td>
                <td style="width:80px;">${item['COLUMN_DEFAULT'] }</td>
                <td style="">${item['COLUMN_COMMENT'] }</td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<script type="text/javascript">
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);
</script>

<jsp:include page="../../iw/common/foot.jsp"></jsp:include>  