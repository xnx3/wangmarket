<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.shiro.ShiroFunc"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="高级设置"/>
</jsp:include>

<div class="weui_panel">
  <div class="weui_panel_bd">
    <div class="weui_media_box weui_media_text" style="cursor: pointer;" onclick="shuaxinshouye();">
      <h4 class="weui_media_title">刷新首页</h4>
      <p class="weui_media_desc">
      	通过模版重新生成首页，若之前修改过首页HTML代码了，此操作会删除掉之前的操作！相当于初始化首页<br/>
      </p>
      <p class="weui_media_desc">
      	比如，发布新闻了、发布图文了，首页没有显示最新的，可以使用此处。
      </p>
      <p class="weui_media_desc">
      	绝大多数时候，此处是不需要使用的。
      </p>
      <ul class="weui_media_info">
        <li class="weui_media_info_meta">谨慎使用</li>
        <li class="weui_media_info_meta"></li>
      </ul>
    </div>
    
    <div class="weui_media_box weui_media_text" style="cursor: pointer;" onclick="divindexhtml();">
      <h4 class="weui_media_title">自定义首页HTML</h4>
      <p class="weui_media_desc">如果您懂HTML代码，可以使用此处来更强大您的首页！可自由编辑首页的HTML代码</p>
      <p class="weui_media_desc">注意：</p>
      <p class="weui_media_desc">1.比如关于我们、新闻信息等数据，都是可动态调用的，使用的标签看不懂的建议保留，不要随便删除标签、HTML注释，其中很多HTML注释是有特定含义的，跟动态调用有关，误删会导致文章更新后首页不会变</p>
      <p class="weui_media_desc">2.自定义首页HTML后，请不要再使用上面的刷新首页功能，不然你所修改的HTML会被冲掉，不可找回！切记</p>
      <ul class="weui_media_info">
        <li class="weui_media_info_meta">技术门槛</li>
        <li class="weui_media_info_meta"></li>
      </ul>
    </div>
    
    <div class="weui_media_box weui_media_text" style="cursor: pointer;" onclick="customPageList();">
      <h4 class="weui_media_title">HTML页面管理</h4>
      <p class="weui_media_desc">如果您懂HTML代码，可以使用此处来更强大您的网站！不必依赖于现有的模版，使您的网站可无限扩展，可自由编辑任何页面的HTML代码</p>
      <p class="weui_media_desc">注意：</p>
      <p class="weui_media_desc">1.纯数字.html、c数字.html、lc数字_数字.html 的为系统自动生成的页面，尽量不要改动，不然修改文章时会被冲掉，改动会丢失。</p>
      <p class="weui_media_desc">2.添加的html文件会自动放到网站的根目录，添加完毕后，可直接访问查看效果</p>
      <ul class="weui_media_info">
        <li class="weui_media_info_meta">技术门槛</li>
        <li class="weui_media_info_meta">专业人士操作</li>
      </ul>
    </div>
    
    <div class="weui_media_box weui_media_text" style="cursor: pointer;" onclick="columnList();">
      <h4 class="weui_media_title">导航栏目管理</h4>
      <p class="weui_media_desc">如果您追求更好，现有的网站栏目不能满足您的需要，您可以重新规划网站的导航栏目</p>
      <p class="weui_media_desc">添加、删除栏目、任意顺序调整，托转排序、对导航栏目进行自定义超链接、将某个栏目从导航栏中隐藏，导航中不显示</p>
      <p class="weui_media_desc">注意：若改动系统自动创建的栏目，如关于我们、产品展示、新闻咨询，首页相关联的新闻、简介、图文将无法更新！</p>
      <ul class="weui_media_info">
        <li class="weui_media_info_meta"></li>
        <li class="weui_media_info_meta"></li>
      </ul>
    </div>
</div>

<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

//刷新首页，由模版重新生成首页
function shuaxinshouye(){
	parent.layer.close(index);
	window.parent.refreshIndex();
}

//自定义首页html代码
function divindexhtml(){
	parent.layer.close(index);
	window.parent.updateIndexHtmlSource();
}

//自定义html页面列表
function customPageList(){
	parent.layer.close(index);
	window.parent.openCustomPageList();
}

//栏目管理
function columnList(){
	parent.layer.close(index);
	window.parent.openColumnList();
}

</script>

</body>
</html>