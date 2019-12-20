<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.shiro.ShiroFunc"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="代理欢迎页面"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Role_role.js"></script>
<script src="${STATIC_RESOURCE_PATH}js/fun.js"></script>
<script src="${STATIC_RESOURCE_PATH}js/admin/commonedit.js?v=<%=Global.VERSION %>" type="text/javascript"></script>


<style>
.iw_table tbody tr .iw_table_td_view_name{
	width:50%;
	padding-left:25%;
}
</style>

<div style="text-align:center; font-size:29px; padding-top:35px; padding-bottom: 10px;">
	欢迎登录 <%=Global.get("SITE_NAME") %>云建站系统
</div>


<div class="layui-tab" id="gonggao" style="display:none; margin-left: 30px; margin-right: 30px;">
  <ul class="layui-tab-title">
    <li class="layui-this">公告信息</li>
    <li>联系</li>
  </ul>
  <div class="layui-tab-content" style="font-size:14px;">
    <div class="layui-tab-item layui-show" id="parentAgencyNotice">${parentAgencyNotice }</div>
    <div class="layui-tab-item">
    	名称：${parentAgency.name }<br/>
    	电话：${parentAgency.phone }<br/>
    	QQ：${parentAgency.qq }<br/>
    	地址：${parentAgency.address }
    </div>
  </div>
</div>
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

<table class="layui-table iw_table" lay-even lay-skin="nob" style="margin:3%; width:94%;">
	<tbody>
		<tr>
			<td class="iw_table_td_view_name">公司名称</td>
			<td>${agency.name }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">到期时间</td>
			<td>
				<x:time linuxTime="${agency.expiretime }"></x:time>
				<a href="javascript:jumpParentAgency();" id="yanchangriqi" class="layui-btn layui-btn-primary" style="height: 30px;line-height: 30px;padding: 0 10px;font-size: 12px;margin-right: 10px;">延长</a>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">权限</td>
			<td><script type="text/javascript">writeName('${user.authority }');</script></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">最后登陆</td>
			<td><x:time linuxTime="${user.lasttime }"></x:time></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">最后登陆ip</td>
			<td>${user.lastip }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">我的上级</td>
			<td onclick="jumpParentAgency();" style="cursor:pointer;">${parentAgency.name }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">账户余额</td>
			<td>${agency.siteSize }<%=Global.get("CURRENCY_NAME") %>
				<a href="javascript:jumpParentAgency();" id="chongzhianniu" class="layui-btn layui-btn-primary" style="height: 30px;line-height: 30px;padding: 0 10px;font-size: 12px;margin-right: 10px;">充值</a>
            	<div style="margin-top: -23px;margin-left: 145px;">
            		1<%=Global.get("CURRENCY_NAME") %> = 开通一个网站/年<br/>
            		1<%=Global.get("CURRENCY_NAME") %> = 续费一个网站/年<br/>
            		<%=G.agencyAddSubAgency_siteSize %><%=Global.get("CURRENCY_NAME") %> = 开通一个下级代理/年<br/>
            		<%=G.agencyAddSubAgency_siteSize %><%=Global.get("CURRENCY_NAME") %> = 续费一个下级代理/年<br/>
            	</div>
            </td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">网站限制</td>
			<td>
            	为保障网站不被非法利用，做如下限制：使用1站币开通或续费的网站：
            	<br/>1.限制存储空间&nbsp;<b>1GB</b>（一般正常使用大约占用不足 百MB）
            	<div style="padding-left:30px; padding-bottom:10px;">用满可续费，再原本的基础上增加，价格为&nbsp;1站币/1GB/年</div>
            	2.限制月流量&nbsp;<b>10GB/月</b>（一般正常使用每月大约消耗 5MB~200MB）
            	<div style="padding-left:30px; padding-bottom:10px;">不做非法用途，不用担心用超！</div>
            	3.限制文章条数&nbsp;<b>1000条</b>（一般正常使用，企业网站也就用个几十条，做SEO优化一周四篇文章，也足够用五年！）
            	<div style="padding-left:30px; padding-bottom:10px;">用满可续费，在原本的基础上增加，价格为&nbsp;1站币/500条/年</div>
            </td>
		</tr>
    </tbody>
</table>


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
versionUpdateRemind('<%=Global.VERSION %>');
</script>
<!-- 版本提示结束 -->

<jsp:include page="../iw/common/foot.jsp"></jsp:include>  