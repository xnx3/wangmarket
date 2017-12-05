<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<jsp:include page="../../publicPage/adminCommon/head.jsp">
    	<jsp:param name="title" value="代理列表"/>
    </jsp:include>
</head>
<body>
<section id="container" >
<jsp:include page="../../publicPage/adminCommon/topHeader.jsp"></jsp:include>   
<aside>
    <div id="sidebar" class="nav-collapse">
		<jsp:include page="../../publicPage/adminCommon/menu.jsp"></jsp:include>          
    </div>
</aside>
    <!--main content start-->
    <section id="main-content">
        <section class="wrapper">
        <div class="row">
            <div class="col-sm-12">
                <section class="panel">
                    <header class="panel-heading tab-bg-dark-navy-blue">
                        <ul class="nav nav-tabs">
                            <li class="active">
                                <a data-toggle="tab" href="">代理列表</a>
                            </li>
                        </ul>
                    </header>
                    <div class="panel-body">
                    <div class="space15"></div>
                    <div class="col-xs-12" style="padding:0;">
	                    <form method="get">
	                    	<input type="hidden" name="orderBy" value="<%=request.getParameter("orderBy") %>" />
	                        <span style="float:left;line-height:34px;margin-left:10px;">用户ID：</span>
	                        <div class="input-group m-bot15 " style="width: 16%;float: left;"> 
	                            <input type="text" name="userid" class="form-control" value="<%=request.getParameter("userid")==null? "":request.getParameter("userid")  %>">
	                        </div>
	                        <span style="float:left;line-height:34px;margin-left:10px;">名称：</span>
	                        <div class="input-group m-bot15 " style="width: 16%;float: left;">
	                            <input type="text" name="title" class="form-control" value="<%=request.getParameter("title")==null? "":request.getParameter("title")  %>">
	                        </div>
	                        <span style="float:left;line-height:34px;margin-left:10px;">姓名：</span>
	                        <div class="input-group m-bot15 " style="width: 16%;float: left;">
	                            <input type="text" name="username" class="form-control" value="<%=request.getParameter("username")==null? "":request.getParameter("username")  %>">
	                        </div>
	                        
	                        <div class="input-group m-bot15 " style="width: 100px; float: left;">
	                            <span class="input-group-btn">
	                            <input class="btn btn-success" type="submit" value="搜索">
	                            <i class="fa fa-search"></i>
	                            </span>
	                        </div>
                        </form>     
                        
                       <!-- <div style="float: right;">
	                      	<script type="text/javascript"> orderBy('id_DESC=编号'); </script>
                       </div> -->
                    </div>
                        <section id="unseen">
                            <table class="table table-bordered table-striped table-condensed">
                                <thead>
                                <tr>
                                    <th>文章ID</th>
                                    <th>用户ID</th>
                                    <th>栏目ID</th>
                                    <th class="numeric">标题</th>
                                    <th class="numeric">类型</th>
                                    <th class="numeric">合法性</th>
                                    <th class="numeric">发布时间</th>
                                </tr>
                                </thead>
                                <tbody>
                                
                                <c:forEach items="${list}" var="news">
                                	<tr>
	                                    <td>${news['id'] }</td>
	                                    <td onclick="window.open('../user/view.do?id=${news['userid']}'); " style="cursor: pointer;">${news['userid'] }</td>
	                                    <td>${news['cid'] }</td>
	                                    
	                                    <td class="numeric" onclick="window.open('view.do?id=${news['id']}'); " style="cursor: pointer; text-align:left;"><x:substring maxLength="30" text="${news['title'] }"></x:substring></td>
	                                    <td class="numeric"><script type="text/javascript">document.write(type[${news['type']}]);</script></td>
	                                    <td class="numeric"><script type="text/javascript">document.write(legitimate[${news['legitimate']}]);</script></td>
	                                    <td class="numeric"><x:time linuxTime="${news['addtime'] }"></x:time></td>
	                                </tr>
                                </c:forEach>
                               
                                </tbody>
                            </table>
                        </section>
                        <!-- 通用分页跳转 -->
                        <jsp:include page="../../publicPage/adminCommon/page.jsp">
                        	<jsp:param name="page" value="${page }"/>
                        </jsp:include>
                    </div>
                </section>
            </div>
        </div>
        </section>
    </section>
    <!--main content end-->

</section>

<jsp:include page="../../publicPage/adminCommon/footImport.jsp"></jsp:include>
</body>
</html>
