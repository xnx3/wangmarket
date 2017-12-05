<%@page import="com.xnx3.admin.G"%>
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
	<jsp:include page="../publicPage/agencyCommon/head.jsp">
    	<jsp:param name="title" value="添加用户站点"/>
    </jsp:include>
    <script src="<%=basePath+Global.CACHE_FILE %>Site_client.js"></script>
	
	<script type="text/javascript">
		function selectClientChange(){
			if(document.getElementById("client").options[1].selected){
				//client＝pc
				document.getElementById("pc_autoCreateColumn").style.display="";
				
				document.getElementById("cms_explain").style.display="none";
				document.getElementById("cms_template").style.display="none";
			}else if(document.getElementById("client").options[3].selected){
				//client=cms
				document.getElementById("cms_explain").style.display="";
				document.getElementById("cms_template").style.display="";
				
				document.getElementById("pc_autoCreateColumn").style.display="none";
			}else{
				//client=wap
				document.getElementById("pc_autoCreateColumn").style.display="none";
				document.getElementById("cms_explain").style.display="none";
				document.getElementById("cms_template").style.display="none";
			}
		}
	</script>
<style type="text/css">
.form-group span{
	font-size: 15px;
	line-height: 33px;
	cursor: pointer;
}
</style>
<style>
.templateList{
}
.templateList div{
	float:left;
	width:48%;
	padding:1%;
}
.templateList div img{
	width:96%;
	padding:2%;
	cursor: pointer;
}
.templateList div div{
	width:96%;
	padding:2%;
	text-align: center;
}
</style>
</head>
<body>

<section id="container" >
<jsp:include page="../publicPage/agencyCommon/topHeader.jsp"></jsp:include>     
<aside>
    <div id="sidebar" class="nav-collapse">
        <jsp:include page="../publicPage/agencyCommon/menu.jsp"></jsp:include>         
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
                            	添加用户站点
                            </a>
                        </li>
                    </ul>
                </header>
                <div class="panel-body">
                    <div class="tab-content">
                        <div id="" class="tab-pane active">
                        <form action="addSave.do" method="post" class="form-horizontal">
                        	<input type="hidden" value="<%=ShiroFunc.getUser().getId() %>" name="inviteid" />
                        	<div class="form-group">
                                <label class="col-sm-2 control-label">网站类型：</label>
                                <div class="col-sm-6">
                                    <select name="client" id="client" onchange="selectClientChange()" class="form-control">
					                	<script type="text/javascript">writeSelectAllOptionForclient_('','请选择');</script>
					                </select> 
                                </div>
                            </div>
                            <div class="form-group" id="pc_autoCreateColumn" style="display:none;">
                                <label class="col-sm-2 control-label">自动创建：</label>
                                <div class="col-sm-6">
                                	<label class="col-sm-2 control-label" style="width: 100%;text-align: left;">关于我们(栏目)&nbsp;,&nbsp;新闻咨询(栏目)&nbsp;,&nbsp;产品展示(栏目)&nbsp;,&nbsp;联系我们(栏目)</label>
                                </div>
                            </div>
                            <div class="form-group" id="cms_explain" style="display:none;">
                                <label class="col-sm-2 control-label">功能介绍：</label>
                                <div class="col-sm-6">
                                	<label class="col-sm-2 control-label" style="width: 100%;text-align: left; font-weight: bold;">
                                		<!-- 现在开通的网站，我们承诺，这些网站享有永久免费使用权！
                                		<br/>我们会在软件优化到比较完美的程度后，进行按量收费，如：开通一个网站收几块钱，当然，这估计也是几年后的事了。
                                		<br/>当前，我们专注于产品，专注于技术，将其做好！让您用的满意！ -->
                                		类似于织梦CMS、帝国CMS这种建站系统，只不过，我们的把它做到云上！统一维护，你只需要开通一个账号，或者选择一个模版，一建创建网站！改改其中内容就可以交给客户了，无需在操作服务器，担心服务器被黑客挂马等，专心做自己的事，让你专心做网站！免去后顾之忧！
                                		<br/>
                                		<a href="<%=basePath %>templateUseExplain.do">使用说明</a>
                                	</label>
                                </div>
                            </div>
                            
                            <div class="form-group" id="cms_template" style="display:none;">
                                <label class="col-sm-2 control-label">选择模版：</label>
                                <div class="col-sm-6">
                                	<input type="hidden" id="templateName" name="templateName" />
                                	<div class="templateList">
										<div>
											<img class="preview" onclick="selectTemp('');" src="<%=G.RES_CDN_DOMAIN %>res_images/template_null.jpg" />
											<div>
												<span id="tempSelect_"><img style="width: 40px;" src="<%=G.RES_CDN_DOMAIN %>image/dui.jpg" /></span>
												<span onclick="selectTemp('');">空白纯净网站</span>
												<br/>
												(全部自定义模版，想哪个页面什么样，有什么页面，全由你决定！)
											</div>
										</div>
										
										<div>
											<img class="preview" onclick="selectTemp('qiye1');" src="<%=G.RES_CDN_DOMAIN %>template/qiye1/preview.jpg" />
											<div>
												<span id="tempSelect_qiye1" style="display:none;"><img style="width: 40px;" src="<%=G.RES_CDN_DOMAIN %>image/dui.jpg" />️</span>
												<span onclick="selectTemp('qiye1');">标准企业站,模版编号:qiye1</span><a style="padding:15px; color:blue; font-size: 16px;" href="http://qiye1.wscso.com" target="_black">点此预览</a>
												<br/>
												(手机电脑通用，模版跟栏目已全内置好，直接拿来改改内容即可使用)
											</div>
										</div>
										
										
									</div>
									
                                </div>
                            </div>
                            
                            <hr/>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">网站名称：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="siteName" value="" placeholder="网站的名称，限40个字以内">
                                </div>
                                <span onclick="alert('若是做优化，这里可以只填写一个最重要的关键词');">说明</span>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">联系人姓名：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="contactUsername" value="" placeholder="请输入5个字以内的联系人名字">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">联系人手机：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="sitePhone" value="" placeholder="可以填写手机、座机、400电话等，限14个字符以内">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">联系人QQ：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="siteQQ" value="" placeholder="填写后生成网站时会自动设置，减少后续修改时的工作量(可不填)">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">公司名称：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="companyName" value="" placeholder="限30个字以内，可填写公司名、个体工商户名，若都没可填写个人名字">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">公司地址：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="address" value="" placeholder="限60个字以内，公司或者办公地点、工作的地址">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">电子邮箱：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="email" value="" placeholder="填写后生成网站时会自动设置，减少后续修改时的工作量">
                                </div>
                            </div>
                            
                            <hr/>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">登陆账号：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="username" value="" placeholder="限20个英文或汉字，开通网站后，用户用此账号登陆wang.market管理网站">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">登陆密码：</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="password" value="" placeholder="限20个英文或汉字，用户管理网站登陆的密码">
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

<script>
//CMS类型使用，更改当前模版，修改全局化模版参数
function selectTemp(tempId){
	document.getElementById('templateName').value=tempId;
	
	//隐藏所有组件的选中效果
	document.getElementById('tempSelect_').style.display = 'none';
	document.getElementById('tempSelect_qiye1').style.display = 'none';
	
	//为指定的选中的设置显示选中状态
	document.getElementById('tempSelect_'+tempId).style.display = 'inline';
}

</script>

<jsp:include page="../publicPage/agencyCommon/footImport.jsp"></jsp:include>  
</body>
</html>
