<%@page import="com.xnx3.j2ee.Global"%>
<%@page import="com.xnx3.j2ee.shiro.ShiroFunc"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<jsp:include page="../publicPage/adminCommon/head.jsp">
    	<jsp:param name="title" value="添加代理"/>
    </jsp:include>
    <script src="<%=basePath+Global.CACHE_FILE %>Site_client.js"></script>
</head>
<body>

<section id="container" >
<jsp:include page="../publicPage/adminCommon/topHeader.jsp"></jsp:include>     
<aside>
    <div id="sidebar" class="nav-collapse">
        <jsp:include page="../publicPage/adminCommon/menu.jsp"></jsp:include>         
    </div>
</aside>
    <!--main content start-->
    <section id="main-content" >
        <section class="wrapper">
            <div class="row">
            <div class="col-lg-12">
            <!--tab nav start-->
            <section class="panel">
                <header class="panel-heading tab-bg-dark-navy-blue ">
                    <ul class="nav nav-tabs">
                        <li class="active">
                        	<a data-toggle="tab" href="">
                            	添加代理
                            </a>
                        </li>
                    </ul>
                </header>
                <div class="panel-body">
                    <div class="tab-content">
                        <div id="" class="tab-pane active">
                        <form action="addSave.do" class="form-horizontal">
                        	<input type="hidden" value="<%=ShiroFunc.getUser().getId() %>" name="inviteid" />
                        	<div class="form-group">
                                <label class="col-sm-2 control-label">代理类型：</label>
                                <div class="col-sm-6">
                                    <select name="client" id="client" onchange="selectClientChange()" class="form-control">
					                	<script type="text/javascript">writeSelectAllOptionForclient_('','请选择');</script>
					                </select> 
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">联系人姓名：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="contactUsername" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">联系人手机：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="sitePhone" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">联系人QQ：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="siteQQ" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">公司名称：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="companyName" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">公司地址：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="address" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">电子邮箱：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="email" value="">
                                </div>
                            </div>
                            
                            <hr/>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">登陆账号：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="username" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">登陆密码：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="password" value="">
                                </div>
                            </div>
                            
                            <div class="panel-body news-btn">
                            	<input type="submit" class="btn btn-primary" value="确认开通" style="margin-left: 25%;" />
                            </div>
                        </form>
                        </div>
                    </div>
                </div>
            </section>
            </div>
            </div>
        </section>
    </section>
    <!--main content end-->

</section>

<jsp:include page="../publicPage/adminCommon/footImport.jsp"></jsp:include>  
</body>
</html>
