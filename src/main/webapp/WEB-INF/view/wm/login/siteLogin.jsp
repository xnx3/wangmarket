<%@page import="com.xnx3.j2ee.util.SafetyUtil"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	//登陆用户名
	String username = SafetyUtil.filter(request.getParameter("username"));
	if(username == null || (username != null && username.equals("null"))){
		username = "";
	}
	
	//登陆密码
	String password = SafetyUtil.filter(request.getParameter("password"));
	if(password == null || (password != null && password.equals("null"))){
		password = "";
	}
 %>
<jsp:include page="/wm/common/head.jsp">
	<jsp:param name="title" value="登录"/>
</jsp:include>
    <!-- Meta tag Keywords -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <link href="//fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@400;500;700;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/plugin/login/css/style.css" type="text/css" media="all" />
    <link rel="stylesheet" href="/plugin/login/css/font-awesome.min.css" type="text/css" media="all">


    <div id="block" class="w3lvide-content" data-vide-bg="images/freelan" data-vide-options="position: 0% 50%">
            <!-- /form -->
            <div class="workinghny-form-grid">
                <div class="main-hotair">
                    <div class="content-wthree">
                        <h1><%=Global.get("SITE_NAME") %> 平台登陆</h1>
                        <form action="#" method="post">
                            <input type="text" class="text" name="text" placeholder="请输入 用户名/邮箱" required="" autofocus>
                            <input type="email" class="text" name="text" placeholder="Email" required="" autofocus>
                            <input type="password" class="password" name="password" placeholder="User Password" required="" autofocus>
                            <button class="btn" type="submit">Sign Up</button>
                        </form>
                        
                        
                        <!-- 
                        <p class="continue"><span>Or create account using social media!</span></p>
                        <div class="social-login">
                            <a href="#facebook">

                                <span class="fa fa-facebook" aria-hidden="true"></span>


                            </a>
                            <a href="#twiter">

                                <span class="fa fa-twitter" aria-hidden="true"></span>

                            </a>
                            <a href="#google">

                                <span class="fa fa-google-plus" aria-hidden="true"></span>

                            </a>

                            <a href="#insta">

                                <span class="fa fa-pinterest" aria-hidden="true"></span>
                            </a>
                        </div>
                         -->
                        
                    </div>
                  
                </div>
           
     
        <!-- copyright-->
        <div class="copyright text-center" style="padding-top:3rem;">
            <p class="copy-footer-29">power by <a href="http://cms.zvo.cn" target="_black">wangmarket CMS </a></p>
        </div>
       </div>
        <!-- //copyright-->
    </div>
    <!-- //form section start -->
      <!-- js -->
  <script src="js/jquery.min.js"></script>
  <!-- //js -->
  <script src="js/jquery.vide.js"></script>
  <script>
    //    $(document).ready(function () {
    //        $("#block").vide("video/ocean"); // Non declarative initialization
    //
    //        var instance = $("#block").data("vide"); // Get instance
    //        var video = instance.getVideoObject(); // Get video object
    //        instance.destroy(); // Destroy instance
    //    });
  </script>

<jsp:include page="/wm/common/foot.jsp"></jsp:include> 
<style> /* 显示多语种切换 */ .translateSelectLanguage{ display:block; } </style>