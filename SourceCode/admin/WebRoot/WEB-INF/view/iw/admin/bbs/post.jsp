<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="编辑帖子"/>
</jsp:include>
<script src="<%=basePath+Global.CACHE_FILE %>Bbs_postClass.js"></script>

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
							       <c:when test="${post.id > 0}">
							             编辑：${post.title } 
							       </c:when>
							       <c:otherwise>
							             新增
							       </c:otherwise>
								</c:choose>
                            </a>
                        </li>
                    </ul>
                </header>
                <div class="panel-body">
                    <div class="tab-content">
                        <div id="" class="tab-pane active">
                        <form action="savePost.do" method="post" class="form-horizontal">
                        	<input type="hidden" value="${post.id }" name="id" />
                        	<div class="form-group">
                                <label class="col-sm-2 control-label">发布于</label>
                                <div class="col-sm-6">
                                    <select name="classid" class="form-control">
		                        		<script type="text/javascript">
		                        			for(var p in postClass){  
		                        				if(p == '${post.classid}'){
		                        					document.write('<option value="'+p+'" selected="selected">'+postClass[p]+'</option>');
		                        				}else{
		                        					document.write('<option value="'+p+'">'+postClass[p]+'</option>');
		                        				}
		  									}  
		                        		</script>
									</select>
                                    
                                </div>
                            </div>
                        	
                            <div class="form-group">
                                <label class="col-sm-2 control-label">标题</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="title" value="${post.title }">
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">内容</label>
                                <div class="col-sm-6">
                                	
                                
                                	<!-- 加载编辑器的容器 -->
								    <!-- 加载编辑器的容器 -->
								        <script id="text" name="text" style="width:700px;height:500px;" type="text/plain">
            								这里写你的初始化内容
        								</script>
                                    <!-- 配置文件 -->
								    <script type="text/javascript" src="<%=basePath %>/module/ueditor/ueditor.config.js"></script>
								    <!-- 编辑器源码文件 -->
								    <script type="text/javascript" src="<%=basePath %>/module/ueditor/ueditor.all.js"></script>
								    <!-- 实例化编辑器 -->
								    <script type="text/javascript">
								        var ue = UE.getEditor('text');
								        //var ue = UE.getContent();
										//对编辑器的操作最好在编辑器ready之后再做
										ue.ready(function() {
										    //设置编辑器的内容
										    ue.setContent('${postData.text }');
										    //获取html内容，返回: <p>hello</p>
										    var html = ue.getContent();
										    //获取纯文本内容，返回: hello
										    var txt = ue.getContentTxt();
										});
								    </script>
                                </div>
                            </div>
                            
                            
                            <div class="panel-body news-btn">
                            	<input type="submit" class="btn btn-primary" value="确认添加" />
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

<jsp:include page="../../common/foot.jsp"></jsp:include> 