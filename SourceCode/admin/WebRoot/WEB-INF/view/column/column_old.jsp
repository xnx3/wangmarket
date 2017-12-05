<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../publicPage/adminCommon/head.jsp">
	<jsp:param name="title" value="编辑栏目导航"/>
</jsp:include>
<script src="<%=basePath+Global.CACHE_FILE %>SiteColumn_used.js"></script>
<script src="<%=basePath+Global.CACHE_FILE %>SiteColumn_type.js"></script>
<script type="text/javascript">
	function selectTypeChange(){
		if(document.getElementById("type").options[5].selected){
			document.getElementById("urlDiv").style.display="inline";
		}else{
			document.getElementById("urlDiv").style.display="none";
		}
	}
</script>

<body>
<section id="container" >
<!--header start-->
	<jsp:include page="../publicPage/adminCommon/topHeader.jsp"></jsp:include>     
<!--header end-->
<aside>
    <div id="sidebar" class="nav-collapse">
        <!-- sidebar menu start-->
        	<jsp:include page="../publicPage/adminCommon/menu.jsp"></jsp:include>         
		<!-- sidebar menu end-->
    </div>
</aside>
<!--sidebar end-->
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
						       <c:when test="${siteColumn.id > 0}">
						              编辑栏目导航：${siteColumn.name } 
						       </c:when>
						       <c:otherwise>
						              添加栏目导航
						       </c:otherwise>
							</c:choose>
							</a>
                        </li>
                    </ul>
                </header>
                <div class="panel-body">
                    <div class="tab-content">
                        <div id="" class="tab-pane active">
                        <form action="saveColumn.do" class="form-horizontal" enctype="multipart/form-data" method="post">
                        	<input type="hidden" value="${siteColumn.id }" name="id">
                        	<input type="hidden" value="${site.id }" name="siteid">
                        	<div class="form-group">
                                <label class="col-sm-2 control-label">所属站点：</label>
                                <div class="col-sm-6">
                                	${site.name }
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">导航名称</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" value="${siteColumn.name }" name="name">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">图标</label>
                                <div class="col-sm-6">
                                    <input type="file"  class="form-control" value="" name="iconFile">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">类型</label>
                                <div class="col-sm-6">
                                	<select name="type" class="form-control" onchange="selectTypeChange();">
					                    <script type="text/javascript">writeSelectAllOptionFortype('${siteColumn['type'] }');</script>
					                </select> 
                                </div>
                            </div>
                            <!-- 
                            <div class="form-group">
                                <label class="col-sm-2 control-label">排序</label>
                                <div class="col-sm-6">
                                    <input type="number" class="form-control" value="${siteColumn.rank }" name="rank">(越小越靠前)
                                </div>
                            </div>
                             -->
                            <div class="form-group">
                                <label class="col-sm-2 control-label">启用显示</label>
                                <div class="col-sm-6">
                                    <select name="used" class="form-control">
			                        	<script type="text/javascript">writeSelectAllOptionForused('${siteColumn.used }');</script>
			                        </select>
                                </div>
                            </div>
                            
                            <!-- 类型选择为超链接时可修改 -->
                            <div class="form-group" id="urlDiv">
                                <label class="col-sm-2 control-label">网址</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" value="${siteColumn.url }" name="url">
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

<!-- Placed js at the end of the document so the pages load faster -->
<jsp:include page="../publicPage/adminCommon/footImport.jsp"></jsp:include>  
</body>
</html>
