<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.shiro.ShiroFunc"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="代理首页"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Role_role.js"></script>
<script src="<%=Global.get("ATTACHMENT_FILE_URL") %>js/fun.js"></script>
<script src="<%=Global.get("ATTACHMENT_FILE_URL") %>js/admin/commonedit.js?v=<%=G.VERSION %>" type="text/javascript"></script>


<style>
.iw_table tbody tr .iw_table_td_view_name{
	width:50%;
	padding-left:25%;
}
</style>


<script>
//注意：选项卡 依赖 element 模块，否则无法进行功能性操作
layui.use('element', function(){
  var element = layui.element;
});
try{
	document.getElementById('parentAgencyNotice').innerHTML = document.getElementById('parentAgencyNotice').innerHTML.replace(/\n/g,"<br/>");
}catch(e){}
try{
	if(document.getElementById('parentAgencyNotice').innerHTML.length > 1){
		document.getElementById('gonggao').style.display='';
	}
}catch(e){}
</script>



<script type="text/javascript">


//Jquery layer 提示
$(function(){
	//延长期限按钮
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
	
	//站币的充值按钮
	var chongzhianniu_tipindex = 0;
	$("#chongzhianniu").hover(function(){
		chongzhianniu_tipindex = layer.tips('联系您的上级，向其购买站币', '#chongzhianniu', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(chongzhianniu_tipindex);
	})
})


//服务于上级代理显示的窗口
function getTr(name, value){
	if(typeof(value) == 'undefined' || value == null || value.length == 0){
		//忽略
		return "";
	}else{
		return '<tr><td style="width:45px;">'+name+'</td><td>'+value+'</td></tr>';
	}
}
//弹出其上级代理的信息
function jumpParentAgency(){
	content = '<table class="layui-table" style="margin:0px;"><tbody>'
			+getTr('名称', '${parentAgency.name}')
			+getTr('QQ', '${parentAgency.qq}')
			+getTr('手机', '${parentAgency.phone}')
			+getTr('地址', '${parentAgency.address}')
			+'</tbody></table>';
	
	layer.open({
    type: 1
    ,title: '我的上级信息'
    ,content: content
    ,shade: false
    ,resize: false
  });
}


//代理开通15日内，登录会弹出网站快速开通的视频说明
try {
	var currentTime = Date.parse( new Date() ).toString();
	currentTime = currentTime.substr(0,10);
	if(currentTime - ${user.regtime } < 1296000){
		//多窗口模式，层叠置顶
		layer.open({
		  type: 2 //此处以iframe举例
		  ,title: '90秒学会，快速开通网站视频教程'
		  ,area: ['390px', '100%']
		  ,shade: 0
		  ,offset: 'rb'
		  ,maxmin: true
		  ,content: '${AGENCYUSER_FIRST_USE_EXPLAIN_URL}'
		  ,zIndex: layer.zIndex //重点1
		});
	}
} catch(error) {}
</script>

<script type="text/javascript">
//得到当前版本号，用于版本更新后提醒更新内容
versionUpdateRemind('<%=G.VERSION %>');
</script>
<!-- 版本提示结束 -->

<jsp:include page="../iw/common/foot.jsp"></jsp:include>  