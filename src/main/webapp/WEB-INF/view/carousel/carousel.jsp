<%@page import="com.xnx3.wangmarket.admin.entity.Carousel"%>
<%@page import="com.xnx3.wangmarket.admin.entity.Site"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%
Site site = (Site)request.getAttribute("site");
%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="编辑轮播"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Carousel_isshow.js"></script>

<section id="container" >
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
                            	<c:choose>
							       <c:when test="${carousel.id > 0}">
							             编辑轮播图
							       </c:when>
							       <c:otherwise>
							             新增轮播图
							       </c:otherwise>
								</c:choose>
                            </a>
                        </li>
                    </ul>
                </header>
                <div class="panel-body">
                    <div class="tab-content">
                        <div id="" class="tab-pane active">
                        <form action="carouselSubmit.do" enctype="multipart/form-data" method="post" class="form-horizontal">
                        	<input type="hidden" value="${carousel.id }" name="id" />
                        	<input type="hidden" value="${site.id }" name="siteid" />
                           <div class="form-group">
                                <label class="col-sm-2 control-label">轮播图</label>
                                <div class="col-sm-6">
                                	<input type="file" value="" name="imageFile" class="form-control"> 
                                	
                                </div>
                            </div>
                           <div class="form-group">
                                <label class="col-sm-2 control-label">跳转链接</label>
                                <div class="col-sm-6">
                                	<input type="text" value="${carousel.url}" name="url" class="form-control"> 
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">显示</label>
                                <div class="col-sm-6">
                                	<select name="isshow" class="form-control">
		                        		<script type="text/javascript">writeSelectAllOptionForisshow('${carousel.isshow}');</script>
		                        	</select>
                                </div>
                            </div>
                           <div class="form-group">
                                <label class="col-sm-2 control-label">排序</label>
                                <div class="col-sm-6">
                                	<input type="number" value="${carousel.rank}" name="rank" class="form-control">（越小越靠前） 
                                </div>
                            </div>
                            
                            <div class="panel-body news-btn">
                            	<input type="submit" class="btn btn-primary" value="保存" />
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

<jsp:include page="../iw/common/foot.jsp"></jsp:include>  
</body>
</html>
