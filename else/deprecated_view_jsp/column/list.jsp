<%@page import="com.xnx3.wangmarket.admin.entity.Site"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="栏目导航"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_used.js"></script>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_type.js"></script>

 
<div>
    <!-- 表单搜索 -->
    <form method="get" style="margin-top:20px;">
        <input type="hidden" name="orderBy" value="<%=request.getParameter("orderBy") %>" />
        <span style="float:left;line-height:34px;margin-left:10px;">站点ID：</span>
            <div class="input-group m-bot15 " style="width: 15%;float: left;">
                <input type="text" name="siteid" class="form-control" value="<%=request.getParameter("siteid")==null? "":request.getParameter("siteid")  %>">
            </div>
        <span style="float:left;line-height:34px;margin-left:10px;">显示启用：</span>
            <div class="input-group m-bot15 " style="width: 15%;float: left;">
                <select name="used" class="form-control">
                    <script type="text/javascript">writeSelectAllOptionForused('<%=request.getParameter("used") %>');</script>
                </select> 
            </div>
        
        <div class="input-group m-bot15 " style="width: 100px; float: left;">
            <span class="input-group-btn">
                <input class="btn btn-success" type="submit" value="搜索">
                <i class="fa fa-search"></i>
            </span>
        </div>
        
        <div style="float: right">
            <a type="button" class="btn btn-primary" style="float: left;margin-right: 10px" href="column.do?siteid=<%=request.getParameter("siteid") %>">
                <i class="fa fa-plus"></i>
            </a>
        </div>
    </form>  
     
</div>
 
<!-- 列表数据 -->
<table class="table table-bordered table-striped table-condensed">
    <thead>
        <tr>
            <th>编号</th>
            <th>名称</th>
            <th class="numeric">图标</th>
            <th class="numeric">类型</th>
            <th class="numeric">是否启用</th>
            <th class="numeric">操作</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${list}" var="column">
            <tr>
                <td>${column['id'] }</td>
                <td>${column['name'] }</td>
                <td><img src="<x:imgUrl img="${column['icon'] }" prefixUrl="${AttachmentFileUrl }site/${siteid }/column_icon/"></x:imgUrl>" alt="${column['icon'] }" height="20" /></td>
                <td><script type="text/javascript">document.write(type['${column['type']}']);</script></td>
                <td><script type="text/javascript">document.write(used['${column['used']}']);</script></td>
                <td>
					<a type="button" class="btn btn-success btn-sm" data-toggle="modal" target="_black" href="column.do?id=${column['id'] }">
	                	<i class="fa fa-pencil">编辑栏目</i>
	                </a>
	                <a type="button" class="btn btn-success btn-sm" data-toggle="modal" target="_black" href="../page/page.do?cid=${column['id'] }">
	                	<i class="fa fa-pencil">编辑内容</i>
	                </a>
				</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
 
<!-- 通用分页跳转 -->
<jsp:include page="../iw/common/page.jsp">
    <jsp:param name="page" value="${page }"/>
</jsp:include>
 
<jsp:include page="../iw/common/foot.jsp"></jsp:include>
</body>
</html>