<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="基本信息"/>
</jsp:include>
<!-- weui，一个UI框架，这个包含weui，依赖Jquery -->
<script src="${STATIC_RESOURCE_PATH}js/jquery-weui.js"></script>
<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}css/jquery-weui.css">
<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}css/weui.min.css">


<body style="text-align:left; min-width:10px;">
<script src="/<%=Global.CACHE_FILE %>Site_mShowBanner.js"></script>

<div class="weui_cells weui_cells_access" style="margin-top: 0em;">
	
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

</div>

<div class="weui_cells" style="margin-top:0px;">
  
  <div class="weui_cell" style="display:none;">
    <div class="weui_cell_bd weui_cell_primary">
      <p>我的<%=Global.get("CURRENCY_NAME") %></p>
    </div>
    <div class="weui_cell_ft" style="color: #9717F7;cursor: pointer;" onclick="window.parent.openMoneyIndex();">${user.currency }<%=Global.get("CURRENCY_NAME") %></div>
  </div>
  <div class="weui_cell">
    <div class="weui_cell_bd weui_cell_primary">
      <p>到期时间</p>
    </div>
    <a href="javascript:parent.jumpParentAgency();;" id="yanchangriqi" class="layui-btn layui-btn-primary" style="height: 30px;line-height: 30px;padding: 0 10px;font-size: 12px;margin-right: 10px;">延长</a>
    <div class="weui_cell_ft">
    	<x:time linuxTime="${site.expiretime }" format="yyyy-MM-dd"></x:time>
    </div>
  </div>
  <div class="weui_cell">
    <div class="weui_cell_bd weui_cell_primary">
      <p>空间占用</p>
    </div>
    <div class="weui_cell_ft" id="ossSize">计算中...</div>
  </div>
  <div class="weui_cell">
    <div class="weui_cell_bd weui_cell_primary">
      <p>空间总量</p>
    </div>
    <div class="weui_cell_ft">
    	${site.attachmentSizeHave}&nbsp;MB
    	<!-- <a href="../productPrice.do" class="weui_btn weui_btn_mini weui_btn_primary" style="margin-left:10px;">升级</a> -->
    </div>
  </div>
  <div class="weui_cell">
    <div class="weui_cell_bd weui_cell_primary">
      <p>剩余空间</p>
    </div>
    <div class="weui_cell_ft" id="residueSize">计算中...</div>
  </div>
  <div class="weui_cell">
    <div class="weui_cell_bd weui_cell_primary">
      <p>注册时间</p>
    </div>
    <div class="weui_cell_ft"><x:time linuxTime="${user.regtime }"></x:time></div>
  </div>
  <div class="weui_cell">
    <div class="weui_cell_bd weui_cell_primary">
      <p>最后登陆</p>
    </div>
    <div class="weui_cell_ft"><x:time linuxTime="${user.lasttime }"></x:time></div>
  </div>
</div>

<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);


$(function(){
	//延长期限按钮
	var yanchangriqi_tipindex = 0;
	$("#yanchangriqi").hover(function(){
		yanchangriqi_tipindex = layer.tips('点击按钮联系我们，为您延长使用期限', '#yanchangriqi', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['200px' , 'auto']
		});
	},function(){
		layer.close(yanchangriqi_tipindex);
	})
});

$.post("/sites/getOSSSize.do", function(data){
	if(data.result == '1'){
		document.getElementById('ossSize').innerHTML = (data.info/1000)+'&nbsp;MB';
		document.getElementById('residueSize').innerHTML = (${site.attachmentSizeHave}-(data.info/1000))+'&nbsp;MB';
	}else{
		parent.msg.failure(data.info);
	}
});

</script>

</body>
</html>