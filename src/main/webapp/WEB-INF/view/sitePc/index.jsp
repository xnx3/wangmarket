<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="电脑模式网站管理后台"/>
</jsp:include>
<script src="http://res.weiunity.com/js/fun.js"></script>
<script>
var masterSiteUrl = '//<%=request.getServerName() %>/';
var autoAssignDomain = '${autoAssignDomain }';
</script>
<style>
#editPanel{
	position: absolute;
    top: 0px;
    width:150px;
}
#editPanel span{
	width:100%;
}

.menu{
	width:150px;
	height:100%;
	background-color: #393D49;
	position: absolute;
}
.menu ul li{
	cursor: pointer;
}

/*顶部导航的按钮*/
#topNav div button{
    color: #5FB878;
    cursor: pointer;
    border-color: #009688;
    border: 1px;
    border-style: solid;
    padding: 3px;
    padding-left: 8px;
    padding-right: 8px;
    border-radius: 12px;
    background-color: #2b2e37;
}

/*左侧的一级菜单的图标*/
.firstMenuIcon{
	font-size:16px;
	padding-right:8px;
	font-weight: 700;
}
/*左侧的一级菜单的文字描述*/
.firstMenuFont{
}
.layui-nav-tree .layui-nav-child a{
	padding-left: 43px;
}
.top_htmledit_buttom{
    position: absolute;
    background-color: #4b2e47;
    padding: 4px;
    border-radius: 10px;
    margin-left: 2px;
    cursor: pointer;
    padding-top: 3px;
    padding-bottom: 3px;
    margin-top: 3px;
    color: #2FE878;
    opacity: 0.8;
}
</style>
</head>
<body style="
    margin: 0;
    padding: 0px;
    height: 100%;
    overflow: hidden;
">

<div style="width:100%;height:100%;">

	<div id="leftMenu" class="layui-nav layui-nav-tree layui-nav-side menu">
		<ul class="">
		
		<li class="layui-nav-item layui-this" id="indexEdit">
			<a href="javascript:openIndex();">
				<i class="layui-icon firstMenuIcon">&#xe632;</i>
				<span class="firstMenuFont">首页模式</span>
			</a>
		</li>
		<li class="layui-nav-item" id="jibenxinxi">
			<a href="javascript:;">
				<i class="layui-icon firstMenuIcon">&#xe60b;</i>
				<span class="firstMenuFont">基本信息</span>
			</a>
		</li>
		<li class="layui-nav-item" id="wangzhanshuxing">
			<a href="javascript:;">
				<i class="layui-icon firstMenuIcon">&#xe620;</i>
				<span class="firstMenuFont">网站设置</span>
			</a>
		</li>
		<li class="layui-nav-item" id="seoyouhua">
			<a href="javascript:;">
				<i class="layui-icon firstMenuIcon">&#xe628;</i>
				<span class="firstMenuFont">SEO优化</span>
			</a>
		</li>
		<li class="layui-nav-item" id="dibuxinxi">
			<a href="javascript:;">
				<i class="layui-icon firstMenuIcon">&#xe612;</i>
				<span class="firstMenuFont">联系信息</span>
			</a>
		</li>
<!-- 		
		<li class="layui-nav-item" id="jifenduihuan">
			<a href="javascript:;">
				<i class="layui-icon firstMenuIcon">&#xe600;</i>
				<span class="firstMenuFont">积分兑换</span>
			</a>
		</li>
 -->
 
		<li class="layui-nav-item" id="wapgenghuanmoban">
			<a href="javascript:loadIframeByUrl('/sites/templateList.do?client=pc')">
				<i class="layui-icon firstMenuIcon">&#xe61b;</i>
				<span class="firstMenuFont">更换模版</span>
			</a>
		</li>
		<% if(com.xnx3.wangmarket.domain.Log.aliyunLogUtil != null){ %>
		<li class="layui-nav-item">
			<a href="javascript:;" id="fangwentongji">
				<i class="layui-icon firstMenuIcon">&#xe62c;</i>
				<span class="firstMenuFont">日志访问</span>
			</a>
			<dl class="layui-nav-child">
				<dd><a id="rzfw_fangwentongji" class="subMenuItem" href="javascript:loadIframeByUrl('/requestLog/fangwentongji.do');">访问统计</a></dd>
				<dd><a id="rzfw_pachongtongji" class="subMenuItem" href="javascript:loadIframeByUrl('/requestLog/pachongtongji.do');">爬虫统计</a></dd>
				<dd><a id="rzfw_caozuorizhi" class="subMenuItem" href="javascript:loadIframeByUrl('/requestLog/actionLogList.do');">操作日志</a></dd>
			</dl>
		</li>
		<% } %>
		<li class="layui-nav-item" id="plugin" style="display:none;">
			<a href="javascript:;">
				<i class="layui-icon firstMenuIcon">&#xe857;</i>
				<span class="firstMenuFont">功能插件</span>
			</a>
			<dl class="layui-nav-child" id="plugin_submenu">
				${pluginMenu }
			</dl>
		</li>
		<script>
			if(document.getElementById('plugin_submenu').innerHTML.length > 5){
				document.getElementById('plugin').style.display = '';
			}
		</script>
		
		<li class="layui-nav-item">
			<a id="neirongguanli" href="javascript:loadIframeByUrl('/news/listForTemplate.do');">
				<i class="layui-icon firstMenuIcon">&#xe60a;</i>
				<span class="firstMenuFont">内容管理</span>
			</a>
		</li>
		
		<li class="layui-nav-item">
			<a id="chakanwangzhan" href="/index.html?domain=${site.domain }.<%=com.xnx3.wangmarket.admin.G.getFirstAutoAssignDomain() %>" target="_black">
				<i class="layui-icon firstMenuIcon">&#xe615;</i>
				<span class="firstMenuFont">预览网站</span>
			</a>
		</li>
		<li class="layui-nav-item" style="display:none;">
			<a id="wentifankui" href="javascript:openWenTiFanKui();">
				<i class="layui-icon firstMenuIcon">&#xe607;</i>
				<span class="firstMenuFont">问题反馈</span>
			</a>
		</li>
		<li class="layui-nav-item" >
			<a id="parentagency" href="javascript:jumpParentAgency();">
				<i class="layui-icon firstMenuIcon">&#xe612;</i>
				<span class="firstMenuFont">技术支持</span>
			</a>
		</li>
		
		
		<li class="layui-nav-item" style="position: absolute;bottom: 0px;">
			<a id="tuichudenglu" href="/user/logout.do">
				<i class="layui-icon firstMenuIcon">&#xe633;</i>
				<span class="firstMenuFont">退出登陆</span>
			</a>
		</li>
		 
		</ul>
	</div>
	
	<div id="content" style="width: 100%;height:100%;position: absolute;left: 150px;word-wrap: break-word;border-right: 150px;box-sizing: border-box; text-align:center; border-right-style: dotted;">
		
		<iframe name="iframe" id="iframe" frameborder="0" style="width:100%;height:100%;box-sizing: border-box;"></iframe>
		<div style="float:right;">提示：按住栏目拖动可调整其排序</div>
	</div>
</div>

<script src="/js/admin/commonedit.js?v=<%=G.VERSION %>"></script>
<script src="/js/admin/indexedit.js"></script>
<script>

layui.use('element', function(){
  var element = layui.element;
});

//Jquery layer 提示
$(function(){
	//编辑模式
	var indexEdit_tipindex = 0;
	$("#indexEdit").hover(function(){
		indexEdit_tipindex = layer.tips('打开首页进行编辑。想改哪里点哪里！', '#indexEdit', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(indexEdit_tipindex);
	})
	
	//更换模版
	
	var wapgenghuanmoban_tipindex = 0;
	$("#wapgenghuanmoban").hover(function(){
		wapgenghuanmoban_tipindex = layer.tips('更换其他样式模版，让您网站与众不同。选择一个你喜欢的网站模版吧！', '#wapgenghuanmoban', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(wapgenghuanmoban_tipindex);
	})
	
	var fangwentongji_tipindex = 0;
	$("#fangwentongji").hover(function(){
		fangwentongji_tipindex = layer.tips('当前网站的访问情况、访问记录，以及各搜索引擎抓取情况等', '#fangwentongji', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(fangwentongji_tipindex);
	})
	
	
	//栏目管理
	var lanmuguanli_tipindex = 0;
	$("#lanmuguanli").hover(function(){
		lanmuguanli_tipindex = layer.tips('网站的栏目信息', '#lanmuguanli', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(lanmuguanli_tipindex);
	})
	
	//内容管理
	var neirongguanli_tipindex = 0;
	$("#neirongguanli").hover(function(){
		neirongguanli_tipindex = layer.tips('网站内容，如关于我们、新闻、产品的内容，都是在这里', '#neirongguanli', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(neirongguanli_tipindex);
	})
	
	//生成整站
	var shengchengzhengzhan_tipindex = 0;
	$("#shengchengzhengzhan").hover(function(){
		shengchengzhengzhan_tipindex = layer.tips('网站添加新闻了、产品了、修改过模版什么的了，最后这里，生成整站后，别人访问你的网址才能看到效果', '#shengchengzhengzhan', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(shengchengzhengzhan_tipindex);
	})
	
	//技术支持，显示自己的上级代理商
	var parentagency_tipindex = 0;
	$("#parentagency").hover(function(){
		parentagency_tipindex = layer.tips('操作网站的过程中，有什么不懂的地方，尽可以联系我们，帮助您拥有自己的网站！', '#parentagency', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(parentagency_tipindex);
	})
})

//在主题内容区域iframe中加载制定的页面
function loadIframeByUrl(url){
	document.getElementById("iframe").src=url;
}



//服务于上级代理显示的窗口
function getTr(name, value){
	if(typeof(value) == 'undefined' || value == null || value.length == 0){
		//忽略
		return "";
	}else{
		return '<tr><td>'+name+'</td><td>'+value+'</td></tr>';
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
      ,title: '技术支持'
      ,content: content
      ,shadeClose:true
      ,resize: false
    });
}

//模版使用说明
function mobanshiyongshuoming(){
	var tn = '${site.templateName}';
	if(tn == 'null' || tn.length == 0){
		layer.msg('您当前没有使用云端模版，无云端使用说明');
	}else{
		layer.open({
		  type: 2
		  ,title : '当前使用的云模版，使用说明。可在左侧模版管理－使用说明中查看'
		  ,area :['700px','400px']
		  ,content: '//res.weiunity.com/template/'+tn+'/useExplain.html'
		});
	}
}

//刷新首页，并强制刷新页面清楚缓存。适用于更改模版后调用
function openIndexRefreshCache(){
	setCookie('pcIndexReloadCleanCache','1');
	openIndex();
	document.getElementById("indexEdit").click();	//控制台左侧选中首页
}

//打开首页
function openIndex(){
	loadIframeByUrl('/sitePc/editIndex.do');
}
openIndex();


//版本更新提示
setTimeout("versionUpdateRemind('<%=G.VERSION %>');",3000);//延时3秒 

</script>

${siteRemainHintJavaScript }

</body>
</html>