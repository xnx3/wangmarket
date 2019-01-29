<%@page import="com.xnx3.wangmarket.domain.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="common/head.jsp"></jsp:include>
<link type="text/css" href="<%=G.RES_CDN_DOMAIN %>css/preview_v0.css" rel="stylesheet">
<script src="<%=G.RES_CDN_DOMAIN %>js/share.js"></script>
<script src="<%=G.RES_CDN_DOMAIN %>js/qrcode.js"></script>

<body style="padding:0px; margin:0px;">
<div class="preview_wrap">
    <section class="iphone_wrap">
        <div class="iphone_con">
        	
            <div id="phone-cont" class="phone" style="overflow: hidden; padding: 0px;">
              <!-- =s phone -->
              <iframe src="${url }" style="width: 316px; height:572px;"></iframe>
                    <!-- =E phone -->
            </div>
        </div>
        <div class="explain_bg"><span class="arrow"></span></div>
        <div class="explain">
            <div class="preview_title">用手机浏览器访问</div>
            <div class="web_link"><%=request.getServerName() %></div>
            <div class="preview_title">手机扫描二维码即可访问</div>
            <span class="code" id="qrcode"><!-- 二维码生成区域 --></span>
            <div class="bdsharebuttonbox bdshare-button-style1-16" data-bd-bind="1470896104059" style="display:none;">
            	<a href="javascript:void(0)" class="bds_more" data-cmd="more">分享到：</a>
            	<a href="javascript:void(0)" class="bds_weixin" data-cmd="weixin" title="分享到微信">微信</a>
            	<a href="javascript:void(0)" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博">新浪微博</a>
            	<a href="javascript:void(0)" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间">QQ空间</a>
            </div>
            <script type="text/javascript">
            	// 创建二维码
		        qrcode = new QRCode(document.getElementById("qrcode"), {
		            width : 133,//设置宽高
		            height : 133
		        });
		
		        qrcode.makeCode('http://<%=request.getServerName() %>');
            </script>	
            <script>window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"我今天开通了我的移动微站，高大上有木有！进入[网·市场]，用手机即可搭建移动微官网！","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"1","bdSize":"16"},"share":{"bdSize":16}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];</script>
        	<div class="weizhan_link">本站点由 <%=Global.get("SITE_NAME") %> 提供</div>
        </div>
    </section>
    <footer>
        <div class="footer"><%=Global.get("SITE_NAME") %></div>
    </footer>
</div>

</body></html>