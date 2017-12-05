<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@page import="com.xnx3.admin.entity.Site"%><%@page import="com.xnx3.j2ee.Global"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="网站基本设置"/>
</jsp:include>
<script src="<%=basePath+Global.CACHE_FILE %>Site_mShowBanner.js"></script>

<body style="text-align:left; min-width:10px;">
<div class="weui_cells weui_cells_access" style="margin-top: 0em;">
	<a class="weui_cell" href="javascript:updateTitle();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>网站标题</p>
		</div>
		<div class="weui_cell_ft" id="nameDiv">${site.name }</div>
	</a>
	<a class="weui_cell" href="javascript:updateDomain_info();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>访问域名</p>
		</div>
		<div class="weui_cell_ft" id="domainInput">
			<span style="float: left;">
				<div id="domainInput_weiunity" style="display:none;">http://${site.domain }.weiunity.com</div>
				<div id="domainInput_wangmarket">http://${site.domain }.wang.market</div>
			</span>
		</div>
	</a>
	<a class="weui_cell" href="javascript:updateBindDomain_info();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>绑定域名</p>
		</div>
		<div class="weui_cell_ft" id="domainInput">
			<span style="float: left;">
				<div id="domainInput_bind">${site.bindDomain }</div>
			</span>
		</div>
	</a>
	<a class="weui_cell" href="javascript:updateDiBuLianXi();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>联系人姓名</p>
		</div>
		<div class="weui_cell_ft">${site.username }</div>
	</a>
	<a class="weui_cell" href="javascript:updateDiBuLianXi();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>联系电话</p>
		</div>
		<div class="weui_cell_ft">${site.phone }</div>
	</a>
	<a class="weui_cell" href="javascript:updateDiBuLianXi();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>联系QQ</p>
		</div>
		<div class="weui_cell_ft">${site.qq }</div>
	</a>
	<a class="weui_cell" href="javascript:updateDiBuLianXi();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>公司(团体)名</p>
		</div>
		<div class="weui_cell_ft">${site.companyName }</div>
	</a>
	<a class="weui_cell" href="javascript:updateDiBuLianXi();">
		<div class="weui_cell_bd weui_cell_primary">
			<p>办公地址</p>
		</div>
		<div class="weui_cell_ft">${site.address }</div>
	</a>

 <div class="weui_cell" style="display:none;">
  <div class="weui_cell_bd weui_cell_primary">
    <p>首页顶图</p>
  </div>
  <div class="weui_cell_ft"><input class="weui_input" style="text-align: right; width:auto;" id="mShowBanner" type="text" value="aa"></div>
 </div>     
<!-- 
 <a class="weui_cell" href="../productPrice.do" style="display:none;">
   <div class="weui_cell_bd weui_cell_primary">
     <p>产品介绍</p>
   </div>
   <div class="weui_cell_ft">按量计费</div>
 </a>
 <a class="weui_cell" href="../user/invite.do" style="display:none;">
   <div class="weui_cell_bd weui_cell_primary">
     <p>我的下线</p>
   </div>
   <div class="weui_cell_ft">下线列表</div>
 </a>
 -->
</div>
<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);


document.getElementById('mShowBanner').value = mShowBanner['${site.mShowBanner }'];

$("#mShowBanner").select({
  title: "是否显示首页顶部Banner图",
  items: ["显示", "隐藏"],
  onChange: function(d) {
  	var mBannerShow = '';
  	if(d.values == '显示'){
  		mBannerShow = '<%=Site.MSHOWBANNER_SHOW %>';
  	}else if(d.values == '隐藏'){
  		mBannerShow = '<%=Site.MSHOWBANNER_HIDDEN %>';
  	}
    $.getJSON("<%=basePath %>site/updateBanner.do?siteid=${site.id}&mShowBanner="+mBannerShow,function(result){
		if(result.result != '1'){
			alert(result.info);
		}
	});
  },
});

//改网站名字
function updateTitle(){
	parent.layer.close(index);
	//window.parent.siteNameClick();
	window.parent.updateSiteName();
}

//更改网站二级域名
function updateDomain_info(){
	parent.layer.close(index);
	parent.$('#subWindowsParam').text('${site.domain }');
	window.parent.updateDomain();
}

//更改网站自己绑定的域名
function updateBindDomain_info(){
	parent.layer.close(index);
	parent.$('#subWindowsParam').text('${site.bindDomain }');
	window.parent.updateBindDomain();
}

//更改网站的相关联系信息
function updateDiBuLianXi(){
	parent.layer.close(index);
	window.parent.updateFooterSiteInfo();
}
</script>

</body>
</html>