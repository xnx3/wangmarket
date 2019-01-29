<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.shiro.ShiroFunc"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="SEO设置"/>
</jsp:include>

<div class="weui_panel">
	<div class="weui_panel_hd">建议不要频繁改动，最好一次改好后就不要再改了，频繁改动对SEO优化影响非常大！</div>

  <div class="weui_panel_bd">
    <div class="weui_media_box weui_media_text" style="cursor: pointer;" onclick="updateTitle();">
      <h4 class="weui_media_title" style="font-size:18px;">网站标题，网站名，title</h4>
      <p class="weui_media_desc">
      	您网站的名字，对SEO影响最大，建议不要超过10个字。很多时候搜索这个就能搜到咱的网站
      </p>
      <p class="weui_media_desc">
      	1.您可以直接将您公司名字、或者店名写上
      </p>
      <p class="weui_media_desc">
      	2.如果您想做SEO优化，比如想做汽车制造，可以填写“高新区汽车制造”，加上所在地可以更快的上百度首页
      </p>
    </div>
    
    <div class="weui_media_box weui_media_text" style="cursor: pointer;" onclick="keywords();">
      <h4 class="weui_media_title" style="font-size:18px;">首页关键词，Keywords</h4>
      <p class="weui_media_desc">搜索什么词可以搜索到你的网站。</p>
      <p class="weui_media_desc">多个用“,”分割，建议输入4个以内的关键词</p>
      <p class="weui_media_desc">每个关键词建议在4到10个字之间，关键词越长对优化越好</p>
    </div>
    
    <div class="weui_media_box weui_media_text" style="cursor: pointer;" onclick="description();">
      <h4 class="weui_media_title" style="font-size:18px;">首页描述，Description</h4>
      <p class="weui_media_desc">通过百度搜索到您的网站时，显示的说明文字，建议不超过100个字</p>
      <p class="weui_media_desc">一个合理的Description可能会提升用户对网页的兴趣</p>
      <p class="weui_media_desc">切忌：</p>
      <p class="weui_media_desc">1.内容与网站内容不相关;</p>
      <p class="weui_media_desc">2.其中要出现你设置的某个关键词，或某几个，但切勿关键字堆积太多</p>
    </div>
    
</div>

<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

function keywords(){
	parent.layer.close(index);
	window.parent.updateSiteKeywords();
}

function updateTitle(){
	parent.layer.close(index);
	parent.updateSiteName();
}
function description(){
	parent.layer.close(index);
	window.parent.updateSiteDataDescription();
}
</script>

</body>
</html>