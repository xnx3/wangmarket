<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.xnx3.wangmarket.admin.G"%><!DOCTYPE html>
<html style="margin: 0;padding: 0px;height: 100%;overflow: hidden;"><head>
<meta charset="utf-8">
<title><%=Global.get("SITE_NAME") %></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<script src="//res.weiunity.com/js/jquery-2.1.4.js"></script>

<script src="/js/fun.js"></script>
<script src="/js/admin/cms/cms.js?v=<%=G.VERSION %>"></script>

<!-- 模版的加载都是动态加载，在下面js里 -->
<script src="//res.weiunity.com/layer/layer.js" type="text/javascript"></script>
<script src="//res.weiunity.com/layui2/layui.js"></script>
<link href="//res.weiunity.com/layui2/css/layui.css" rel="stylesheet" type="text/css">	

<script src="//res.weiunity.com/js/jquery-weui.js" type="text/javascript"></script>
<link href="//res.weiunity.com/css/weui.min.css" rel="stylesheet" type="text/css">	
<link href="//res.weiunity.com/css/jquery-weui.css" rel="stylesheet" type="text/css">

<script src="//res.weiunity.com/js/iw.js"></script>	
<script>
var masterSiteUrl = '//<%=request.getServerName() %>:<%=request.getServerPort() %>/';
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
    display:none;
}
</style>
</head>
<body style="
    margin: 0;
    padding: 0px;
    height: 100%;
    overflow: hidden;
">
<style>
/* 避免屏幕高度太小，造成左侧菜单拉太长，最下面的收缩侧边栏遮挡功能菜单 */
.layui-nav-item{
	background-color: #393D49;
	z-index:99;
}
</style>
<div style="width:100%;height:100%;">

	<div id="leftMenu" class="layui-nav layui-nav-tree layui-nav-side menu">
		<ul class="">
		<li class="layui-nav-item" id="li_system">
			<a href="javascript:;">
				<i class="layui-icon firstMenuIcon">&#xe620;</i>
				<span class="firstMenuFont">系统管理</span>
			</a>
			<dl class="layui-nav-child">
				<dd class="twoMenu"><a id="jibenxinxi" class="subMenuItem" href="javascript:;">基本信息</a></dd>
				<dd id="dd_wangzhanshuxing" class="twoMenu"><a id="wangzhanshuxing" class="subMenuItem" href="javascript:;">网站设置</a></dd>
				<dd class="twoMenu"><a id="xiugaimima" class="subMenuItem" href="javascript:updatePassword();">修改密码</a></dd>
				<dd class="twoMenu"><a id="chakanwangzhan" class="subMenuItem" href="javascript:window.open('/index.html?domain=${site.domain }.<%=G.getFirstAutoAssignDomain() %>');">预览网站</a></dd>
			</dl>
		</li>
		<li class="layui-nav-item" id="li_template">
			<a href="javascript:;">
				<i class="layui-icon firstMenuIcon">&#xe61b;</i>
				<span class="firstMenuFont">模版管理</span>
			</a>
			<dl class="layui-nav-child">
				<dd class="twoMenu"><a id="mobanbianliang" class="subMenuItem" href="javascript:loadIframeByUrl('/template/templateVarList.do'), notUseTopTools();">模版变量</a></dd>
				<dd id="dd_mobanyemian" class="twoMenu"><a id="mobanyemian" class="subMenuItem" href="javascript:loadIframeByUrl('/template/templatePageList.do'), notUseTopTools();">模版页面</a></dd>
				<dd class="twoMenu"><a id="shurumoxing" class="subMenuItem" href="javascript:loadIframeByUrl('/inputModel/list.do'), notUseTopTools();">输入模型</a></dd>
				<dd class="twoMenu"><a id="daochutemplate" class="subMenuItem" href="javascript:exportTemplate();">导出/备份</a></dd>
				<dd class="twoMenu"><a id="daorutemplate" class="subMenuItem" href="javascript:loadIframeByUrl('/template/selectTemplate.do'), notUseTopTools();">导入/还原</a></dd>
				<dd class="twoMenu"><a id="templateplugin" class="subMenuItem" href="javascript:loadIframeByUrl('/template/templatePlugin.do'), notUseTopTools();">模版插件</a></dd>
			</dl>
		</li>
		
		<% if(com.xnx3.wangmarket.domain.Log.aliyunLogUtil != null){ %>
		<li class="layui-nav-item" id="li_log">
			<a href="javascript:;">
				<i class="layui-icon firstMenuIcon">&#xe62c;</i>
				<span class="firstMenuFont">日志访问</span>
			</a>
			<dl class="layui-nav-child">
				<dd class="twoMenu"><a id="rzfw_fangwentongji" class="subMenuItem" href="javascript:loadIframeByUrl('/requestLog/fangwentongji.do'), notUseTopTools();">访问统计</a></dd>
				<dd class="twoMenu"><a id="rzfw_pachongtongji" class="subMenuItem" href="javascript:loadIframeByUrl('/requestLog/pachongtongji.do'), notUseTopTools();">爬虫统计</a></dd>
				<dd class="twoMenu"><a id="rzfw_caozuorizhi" class="subMenuItem" href="javascript:loadIframeByUrl('/requestLog/actionLogList.do'), notUseTopTools();">操作日志</a></dd>
			</dl>
		</li>
		<% } %>
		
		<li class="layui-nav-item" id="li_help">
			<a href="javascript:;">
				<i class="layui-icon firstMenuIcon">&#xe60b;</i>
				<span class="firstMenuFont">帮助说明</span>
			</a>
			<dl class="layui-nav-child">
				<dd id="dd_help_basicUse" class="twoMenu"><a id="help_basicUse" class="subMenuItem" href="javascript:loadIframeByUrl('<%=Global.get("SITEUSER_FIRST_USE_EXPLAIN_URL") %>'), notUseTopTools();">基本使用</a></dd>
				<dd style="display:none;" class="twoMenu"><a id="mobanshiyongshuoming" class="subMenuItem" href="javascript:mobanshiyongshuoming();">模版使用</a></dd>
				<dd id="dd_kaifawendang" class="twoMenu"><a id="kaifawendang" class="subMenuItem" href="javascript:loadIframeByUrl('<%=Global.get("SITE_TEMPLATE_DEVELOP_URL") %>'), notUseTopTools();">模版开发</a></dd>
				<dd style="display:none;" class="twoMenu"><a id="wentifankui" class="subMenuItem" target="_black" href="javascript:openWenTiFanKui();">问题反馈</a></dd>
			</dl>
		</li>
		
		<% if(com.xnx3.wangmarket.im.Global.kefuMNSUtil != null){ %>
		<li class="layui-nav-item" id="li_kefu">
			<a href="javascript:;">
				<i class="layui-icon firstMenuIcon">&#xe63a;</i>
				<span class="firstMenuFont">客服管理</span>
			</a>
			<dl class="layui-nav-child">
				<dd class="twoMenu"><a id="im_menu" class="subMenuItem" href="javascript:openKefuSet();">基本设置</a></dd>
				<dd class="twoMenu"><a id="im_hostory" class="subMenuItem" href="javascript:loadIframeByUrl('/im/hostoryChatList.do'), notUseTopTools();">历史咨询</a></dd>
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
		  	<a id="lanmuguanli" href="javascript:loadIframeByUrl('/column/popupListForTemplate.do'), notUseTopTools();">
		  		<i class="layui-icon firstMenuIcon">&#xe638;</i>
		  		<span class="firstMenuFont">栏目管理</span>
		  	</a>
		</li>
		<li class="layui-nav-item">
			<a id="neirongguanli" href="javascript:loadIframeByUrl('/news/listForTemplate.do'), notUseTopTools();">
				<i class="layui-icon firstMenuIcon">&#xe647;</i>
				<span class="firstMenuFont">内容管理</span>
			</a>
		</li>
		<li class="layui-nav-item">
			<a id="parentagency" href="javascript:jumpParentAgency();">
				<i class="layui-icon firstMenuIcon">&#xe612;</i>
				<span class="firstMenuFont">技术支持</span>
			</a>
		</li>
		<li class="layui-nav-item">
			<a id="shengchengzhengzhan" href="javascript:shengchengzhengzhan();">
				<i class="layui-icon firstMenuIcon">&#xe609;</i>
				<span class="firstMenuFont">生成整站</span>
			</a>
		</li>
		
		<li class="layui-nav-item" style="position: absolute;bottom: 46px;z-index: 2;">
			<a id="tuichudenglu" href="../user/logout.do">
				<i class="layui-icon firstMenuIcon">&#xe633;</i>
				<span class="firstMenuFont">退出登陆</span>
			</a>
		</li>
		
		<!-- 两个li的高度，避免遮挡 -->
		<!-- <div style="height:46px;z-index: 1;">&nbsp;</div> -->
		
		<li class="layui-nav-item" style="position: absolute;bottom: 0px;z-index: 2;">
			<a id="showHiddenLeftMenu" href="javascript:zoomLeftMenu();">
				<i class="layui-icon firstMenuIcon" id="showHiddenLeftMenu_icon">&#xe603;</i>
				<span class="firstMenuFont">缩小侧边栏</span>
			</a>
		</li>
		</ul>
	</div>
	
	
	<!-- 代码编辑模式所需资源 -->
	<link rel="stylesheet" href="//res.weiunity.com/editor/css/editormd.css" />
	<script src="//res.weiunity.com/editor/editormd.js"></script>

	<div id="content" style="width: 100%;height:100%;position: absolute;left: 150px;word-wrap: break-word;border-right: 150px;box-sizing: border-box; border-right-style: dotted;">
		<div id="htmledit_openButton" onclick="showHiddenTopNav();" class="top_htmledit_buttom"><i class="layui-icon">&#xe603;</i>隐藏</div>
		<div id="topNav" style="background-color: #2b2e37; color:#009688; height: 38px;line-height: 38px;padding-left: 12px; margin-left:-1px;">
			<div style="padding-left: 50px;">
				当前修改<span id="currentTemplateType">首页模版</span>：<input type="text" id="currentTemplatePageName" value="" readOnly="true" style="border: 0px; background-color: #2b2e37; overflow: hidden; color: #009688; width: 100px;"/>
				<button id="htmledit_mode" onclick="htmledit_mode();">代码模式</button>
				<span id="baocuninit"><button id="save" onclick="saveHtmlSource();">保存更改</button></span>
				<span>&nbsp;&nbsp;&nbsp;</span>
				可用：
					<button onclick="popupTemplateTagHelp('模版变量-简化模式','/template/templateVarListForUsed.do', '520', '260');">模版变量</button>
					<button id="tongyong" onclick="popupTemplateTagHelp('通用标签','http://tag.wscso.com/2936.html#%E6%A0%87%E7%AD%BE%E5%88%97%E8%A1%A8', '750', '680');">通用标签</button>
					<button id="lanmu" onclick="popupTemplateTagHelp('栏目标签','http://tag.wscso.com/2937.html#%E6%A0%87%E7%AD%BE%E5%88%97%E8%A1%A8','780', '540');">栏目标签</button>
					<button id="fenye" onclick="popupTemplateTagHelp('分页标签','http://tag.wscso.com/2938.html#%E6%A0%87%E7%AD%BE%E5%88%97%E8%A1%A8', '760','605');">分页标签</button>
					<button id="wenzhang" onclick="popupTemplateTagHelp('文章信息标签','http://tag.wscso.com/2939.html#%E6%A0%87%E7%AD%BE%E5%88%97%E8%A1%A8','870', '700');">文章信息标签</button>
					<button id="dongtailanmu" onclick="popupTemplateTagHelp('动态栏目调用','http://tag.wscso.com/2940.html#http://tag.wscso.com/2940.html','770', '650');">动态栏目调用</button>
					<button id="xiangqingduyou" onclick="popupTemplateTagHelp('详情页独有','http://tag.wscso.com/2971.html#%E8%AF%A6%E6%83%85%E9%A1%B5%E7%8B%AC%E6%9C%89%E6%A0%87%E7%AD%BE','920', '340');">详情页独有</button>
					<button id="liebiaoduyou" onclick="popupTemplateTagHelp('列表页独有','http://tag.wscso.com/2970.html#%E8%AF%A6%E6%83%85%E9%A1%B5%E7%8B%AC%E6%9C%89%E6%A0%87%E7%AD%BE','470', '480');">列表页独有</button>
			</div>
		</div>
		<iframe name="iframe" id="iframe" frameborder="0" style="width:100%;height:100%;padding-bottom: 38px;box-sizing: border-box;"></iframe>
		<div id="htmlMode" style="width:100%;height:100%; display:none; padding-bottom: 38px;box-sizing: border-box;">
			<style>
				.CodeMirror-linenumber{
					padding:0px;
					padding-left:1px;
				}
			</style>
			<div id="editormd" style="width:100%; height:100%;"></div>
			<textarea id="html_textarea"></textarea>
        </div>
	</div>
</div>

<script src="/js/admin/commonedit.js?v=<%=G.VERSION %>"></script>
<script src="/js/admin/indexedit.js"></script>
<script>
//默认不使用头部导航
notUseTopTools();

layui.use('element', function(){
  var element = layui.element;
});


//判断当前主体区域显示的内容
if('${needSelectTemplate}' == 1){
	//可能是新网站，还没有选择模版，首先会打开选择模版页面
	loadIframeByUrl('/template/selectTemplate.do');
}else{
	//有模版了，直接进入欢迎页面
	//loadIframe();
	loadIframeByUrl('welcome.do');
}


//模式切换
var testEditor;

//以代码模式编辑模版页面
function codeEditMode(){
	useTopTools();
	document.getElementById("htmledit_mode").style.display='none';	//将 代码模式、智能模式切换的按钮隐藏。代码模式下，不需要再有智能模式
	
	if(typeof(textEditor) != 'undefined'){
		try{
			//清空上次的
			testEditor.setValue('');
		}catch(e){}
	}

	//判断一下，如果上次是智能模式，那么切换回代码模式
	if(currentMode == 1){
		//由智能模式切换代码模式
		document.getElementById("iframe").style.display='none';
		document.getElementById("htmlMode").style.display='';
		
		document.getElementById("htmledit_mode").innerHTML = '智能模式';
		currentMode = 2;
	}
	
	iw.loading("加载中");    //显示“操作中”的等待提示
	$.post("/template/getTemplatePageText.do?pageName="+document.getElementById("currentTemplatePageName").value, function(data){
	    iw.loadClose();    //关闭“操作中”的等待提示
	    document.getElementById("html_textarea").value=data;
	    
	    testEditor = editormd("editormd", {
            width            : "100%",
            height           : "100%",
            watch            : false,
            toolbar          : false,
            codeFold         : true,
            searchReplace    : true,
            placeholder      : "请输入html代码",
            value            : data,
            theme            : "default",
            mode             : "text/html",
            path             : '//res.weiunity.com/editor/lib/'
        });
	    
	},'text');
}


function htmledit_mode(){
	$.showLoading('切换中...');
	document.getElementById("htmledit_mode").style.display='';	//将 代码模式、智能模式切换的按钮显示。智能模式下，一直优惠智能模式、代码模式的切换按钮
	if(currentMode == 1){
		//由智能模式切换代码模式
		document.getElementById("iframe").style.display='none';
		document.getElementById("htmlMode").style.display='';
		
		//判断一下，如果模版页面不是正常的HTML模版，那么在切换到代码模式时，不进行赋值textarea的操作
		var html = '';
		try {
			html = getHtmlSource();
		} catch(error) {}
		
		if(html != ''){
			//由智能模式切换代码模式
			document.getElementById("html_textarea").value=getHtmlSource();
			testEditor = editormd("editormd", {
	            width            : "100%",
	            height           : "100%",
	            watch            : false,
	            toolbar          : false,
	            codeFold         : true,
	            searchReplace    : true,
	            placeholder      : "请输入html代码",
	            value            : document.getElementById("html_textarea").value,
	            theme            : "default",
	            mode             : "text/html",
	            path             : '//res.weiunity.com/editor/lib/'
	        });
		}
		
		document.getElementById("htmledit_mode").innerHTML = '智能模式';
		currentMode = 2;
	}else{
		//由代码模式切换智能模式
		
		document.getElementById("iframe").style.display='';
		document.getElementById("htmlMode").style.display='none';
		
		//将editormd的值转到textarea中
		document.getElementById("html_textarea").value = testEditor.getValue();
		
		var o = document.getElementById("iframe");
        ed = document.all ? o.contentWindow.document : o.contentDocument;
        ed.open();
        ed.write(testEditor.getValue());
        ed.close();
        
    	document.getElementById("htmledit_mode").innerHTML = '代码模式';
		currentMode = 1;
	}
	$.hideLoading();
}

function getHtmlSource(){
	return $(window.parent.document).contents().find("#iframe")[0].contentWindow.xnx3_getHtmlSource();
}

//保存HTML源代码
function saveHtmlSource(){
	//html源代码
	var html;
	if(currentMode == 1){
		//傻瓜模式，再iframe中
		html = getHtmlSource();
	}else{
		//开发者模式，在 editormd 中获取，（已不在textarea中，textarea只是切换智能模式与代码模式的中转站）
		
		//将editormd的值转到textarea中
		document.getElementById("html_textarea").value = testEditor.getValue();
		html = document.getElementById("html_textarea").value
	}
	
	iw.loading('保存中');
	var pageName = document.getElementById("currentTemplatePageName").value;
	$.post("/template/saveTemplatePageText.do", {pageName: pageName, html: html}, function(data){
		iw.loadClose();
		if(data.result == 1){
			iw.msgSuccess("保存成功");
	     	//layer.msg('保存成功', {shade: 0.2});
		}else{
			layer.msg(data.info, {shade: 0.2});
		}
	});
}


//Jquery layer 提示

$(function(){
	//编辑模式
	var htmledit_mode_tipindex = 0;
	$("#htmledit_mode").hover(function(){
		htmledit_mode_tipindex = layer.tips('可以快速您网站当前的编辑模式<br/><b>智能模式</b>：想改哪里点哪里，点了直接改！修改时右键还会有更多的选择！<br/><b>代码模式</b>：以最原始代码的形式提供编辑。建议有一定编程基础或者稍微懂点的专业人士使用', '#htmledit_mode', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(htmledit_mode_tipindex);
	})
	
	
	//保存按钮
	var save_tipindex = 0;
	$("#save").hover(function(){
		save_tipindex = layer.tips('将您当前所操作的页面保存。保存后可预览查看，刷新一下看看其是否已改变', '#save', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(save_tipindex);
	})
	
	//模版变量
	var mobanbianliang_tipindex = 0;
	$("#mobanbianliang").hover(function(){
		mobanbianliang_tipindex = layer.tips('当前网站模版页面所使用的动态变量内容，可再模版页面中使用<div style="padding:5px; font-size:16px">{include=变量名}</div>随处调出使用、显示', '#mobanbianliang', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(mobanbianliang_tipindex);
	})
	
	//模版页面
	var mobanyemian_tipindex = 0;
	$("#mobanyemian").hover(function(){
		mobanyemian_tipindex = layer.tips('当前网站所使用的模版，如首页、列表、内容详情等。', '#mobanyemian', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(mobanyemian_tipindex);
	})
	
	//输入模型
	var shurumoxing_tipindex = 0;
	$("#shurumoxing").hover(function(){
		shurumoxing_tipindex = layer.tips('自定义用户内容管理时，文章编辑的添加数据的模版，比如，有的栏目有标题图片，有的栏目没有，都可以在这里配置 <br/><b>供开发模版的专业人员使用，建议使用网站的用户不懂的情况下不要修改此处</b>', '#shurumoxing', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(shurumoxing_tipindex);
	})
	
	//导入模版
	var daorutemplate_tipindex = 0;
	$("#daorutemplate").hover(function(){
		daorutemplate_tipindex = layer.tips('1.&nbsp;&nbsp;当网站刚创建，但尚未有模版时，此处可从云端模版库、或者本地自有模版中导入现有的模版，快速创建模版页面、模版变量、以及栏目，实现极速建站<br/>2.&nbsp;&nbsp;当模版已经存在，使用了模版后，可以通过此处进行还原操作。还原回模版某一时刻的样子', '#daorutemplate', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(daorutemplate_tipindex);
	})
	
	
	//导出模版
	var daochutemplate_tipindex = 0;
	$("#daochutemplate").hover(function(){
		daochutemplate_tipindex = layer.tips('1.&nbsp;&nbsp;将当前网站的模版导出，可以在新网站中将导出的模版文件导入，实现快速复制网站。<br/>2.&nbsp;&nbsp;也会起到备份模版的作用，网站如果操作失误删除东西了，可以通过此处进行还原回来', '#daochutemplate', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(daochutemplate_tipindex);
	})
	
	var mobanshiyongshuoming_tipindex = 0;
	$("#mobanshiyongshuoming").hover(function(){
		mobanshiyongshuoming_tipindex = layer.tips('若您使用的是官方制作的云端模版，这里可显示当前模版的使用说明，快速上手知晓如何使用', '#mobanshiyongshuoming', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(mobanshiyongshuoming_tipindex);
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
	
	
	//模版的开发文档
	var kaifawendang_tipindex = 0;
	$("#kaifawendang").hover(function(){
		kaifawendang_tipindex = layer.tips('模版开发入门、模版制作帮助文档，帮您十分钟入门，2天成大神。<br/>文档正在完善中', '#kaifawendang', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(kaifawendang_tipindex);
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
	
	
	//帮助说明－基本功能 说明
	var help_basicUse_tipindex = 0;
	$("#help_basicUse").hover(function(){
		help_basicUse_tipindex = layer.tips('网站基本功能使用说明，基本功能介绍，网站使用引导。看完15分钟视频，便可熟练操作网站', '#help_basicUse', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(help_basicUse_tipindex);
	})
	
	//模版管理－模版插件
	var template_templateplugin_tipindex = 0;
	$("#templateplugin").hover(function(){
		template_templateplugin_tipindex = layer.tips('在您现有的模版基础上，扩展更多功能！让您的网站，拥有无限可能！<br/>更多插件持续增加中，敬请期待', '#templateplugin', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(template_templateplugin_tipindex);
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
	
	// 未授权用户，请尊重作者劳动成果，保留我方版权标示及链接！授权参见：http://www.wang.market/price.html 
	<% if(G.copyright){ %>
	//显示、隐藏侧边栏
	var showHiddenLeftMenu_tipindex = 0;
	$("#showHiddenLeftMenu").hover(function(){
		showHiddenLeftMenu_tipindex = layer.tips('power by 网市场', '#showHiddenLeftMenu', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(showHiddenLeftMenu_tipindex);
	})
	<% } %>
	
})




//缩小左侧菜单
var currentZoomOut = false;	//当前左侧导航栏是否是缩小模式
function zoomLeftMenu(){
	if(currentZoomOut){
		//展开
		//缩小菜单时，会将菜单内容隐去，菜单图标缩小
		$(".firstMenuIcon").css("font-size", 16);
		$(".firstMenuFont").css('display','');
		
		//将左侧菜单宽度缩小至72px,同时，右侧也要向左移动
		document.getElementById("leftMenu").style.width = '150px';
		document.getElementById("content").style.left = '150px';
		document.getElementById("content").style.borderRight = '150px dotted';
		document.getElementById("content").style.boxSizing="border-box";
		
		//左侧菜单的二级菜单
		$(".subMenuItem").css("subMenuItem", "ellipsis");
		$(".subMenuItem").css("font-size", 14);
		$(".subMenuItem").css("paddingLeft", 43);
		document.getElementById("jibenxinxi").innerHTML = '基本信息';
		document.getElementById("wangzhanshuxing").innerHTML = '网站设置';
		document.getElementById("chakanwangzhan").innerHTML = '预览网站';
		document.getElementById("wentifankui").innerHTML = '问题反馈';
		document.getElementById("mobanbianliang").innerHTML = '模版变量';
		document.getElementById("mobanyemian").innerHTML = '模版页面';
		document.getElementById("daochutemplate").innerHTML = '导出/备份';
		document.getElementById("daorutemplate").innerHTML = '导入/还原';
		document.getElementById("kaifawendang").innerHTML = '开发文档';
		document.getElementById("mobanshiyongshuoming").innerHTML = '使用说明';
		try{
			document.getElementById("rzfw_fangwentongji").innerHTML = '访问统计';
			document.getElementById("rzfw_pachongtongji").innerHTML = '爬虫统计';
			document.getElementById("rzfw_caozuorizhi").innerHTML = '操作日志';
		} catch(error) {}
		document.getElementById("help_basicUse").innerHTML = '基本使用';
		document.getElementById("shurumoxing").innerHTML = '输入模型';
		<% if(com.xnx3.wangmarket.im.Global.kefuMNSUtil != null){ %>
			document.getElementById("im_menu").innerHTML = '基本设置';
			document.getElementById("im_hostory").innerHTML = '历史咨询';
		<% } %>
		document.getElementById("xiugaimima").innerHTML = '修改密码';
		document.getElementById("templateplugin").innerHTML = '模版插件';
		
		
		//左下角的缩放按钮
		document.getElementById("showHiddenLeftMenu_icon").innerHTML = '&#xe603;';
		
		currentZoomOut = false;
	}else{
		//缩小菜单时，会将菜单内容隐去，菜单图标扩大
		$(".firstMenuIcon").css("font-size", 22);
		$(".firstMenuFont").css('display','none');
		
		//将左侧菜单宽度缩小至72px,同时，右侧也要向左移动
		document.getElementById("leftMenu").style.width = '72px';
		document.getElementById("content").style.left = '72px';
		document.getElementById("content").style.borderRight = '72px dotted';
		document.getElementById("content").style.boxSizing="border-box";
		
		//左侧菜单的二级菜单
		$(".subMenuItem").css("textOverflow", "inherit");
		$(".subMenuItem").css("font-size", 12);
		$(".subMenuItem").css("paddingLeft", 26);
		document.getElementById("jibenxinxi").innerHTML = '信息';
		document.getElementById("wangzhanshuxing").innerHTML = '设置';
		document.getElementById("chakanwangzhan").innerHTML = '预览';
		document.getElementById("wentifankui").innerHTML = '反馈';
		document.getElementById("mobanbianliang").innerHTML = '变量';
		document.getElementById("mobanyemian").innerHTML = '页面';
		document.getElementById("daochutemplate").innerHTML = '备份';
		document.getElementById("daorutemplate").innerHTML = '还原';
		document.getElementById("kaifawendang").innerHTML = '文档';
		document.getElementById("mobanshiyongshuoming").innerHTML = '使用';
		try{
			document.getElementById("rzfw_fangwentongji").innerHTML = '访问';
			document.getElementById("rzfw_pachongtongji").innerHTML = '爬虫';
			document.getElementById("rzfw_caozuorizhi").innerHTML = '日志';
		} catch(error) {}
		document.getElementById("help_basicUse").innerHTML = '基本';
		document.getElementById("shurumoxing").innerHTML = '输入';
		<% if(com.xnx3.wangmarket.im.Global.kefuMNSUtil != null){ %>
			document.getElementById("im_menu").innerHTML = '设置';
			document.getElementById("im_hostory").innerHTML = '历史';
		<% } %>
		document.getElementById("xiugaimima").innerHTML = '改密';
		document.getElementById("templateplugin").innerHTML = '插件';
		
		
		
		//左下角的缩放按钮
		document.getElementById("showHiddenLeftMenu_icon").innerHTML = '&#xe602;';
		
		currentZoomOut = true;
	}
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
		  ,area :['700px','450px']
		  ,maxmin : true
		  ,content: '//res.weiunity.com/template/'+tn+'/useExplain.html'
		});
	}
}

//默认使用的是首页，那么顶部的导航，可用标签进行显示控制
document.getElementById("currentTemplateType").innerHTML = '首页模版';
document.getElementById("tongyong").style.display = '';
document.getElementById("lanmu").style.display = 'none';
document.getElementById("fenye").style.display = 'none';
document.getElementById("wenzhang").style.display = 'none';
document.getElementById("dongtailanmu").style.display = '';
document.getElementById("xiangqingduyou").style.display = 'none';
document.getElementById("liebiaoduyou").style.display = 'none';


//版本更新提示
setTimeout("versionUpdateRemind('<%=G.VERSION %>');",3000);//延时3秒 

</script>


${siteRemainHintJavaScript }


<script type="text/javascript">

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
      ,title: '技术支持'
      ,shadeClose:true
      ,content: content
      ,resize: false
    });
}

//入门视频，引导
function helpVideo(){
	//收起所有
	backAllMenu();
	
	//展开某个一级栏目
	unfoldFirstColumn('li_help');
	//选中某个菜单
	selectedLeftMenu('dd_help_basicUse');
	
	//打开视频说明
	loadIframeByUrl('<%=Global.get("SITEUSER_FIRST_USE_EXPLAIN_URL") %>');
}


//模版开发入门-引导
function templateDevHelp(){
	//收起所有展开的一级栏目、一级取消选中的功能菜单
	backAllMenu();
	
	//展开某个一级栏目
	unfoldFirstColumn('li_help');
	//选中某个菜单
	selectedLeftMenu('dd_kaifawendang');
	
	//打开模版管理
	loadIframeByUrl('<%=Global.get("SITE_TEMPLATE_DEVELOP_URL") %>');
}

//右侧弹出提示
function rightTip(){
	layer.open({
	  title: '演示站点提示文字',offset: 'rb', shadeClose:true, shade:0
	  ,area: ['500px', 'auto']
	  ,btn: ['我知道了'] //可以无限个按钮
	  ,content:  '若我方对你有用，我们愿与各行业进行合作、资源交换！网站可由代理平台在线开通，或由用户自己自助开通完全无人干预！<a href="http://www.wang.market/index.html#join" target="_black" style="text-decoration: underline;color: blue;">合作方式</a><br/>'+
	   			'若您只是想要个此类网站，你可关注我们微信公众号： wangmarket'+
	   			'<div style="text-align:center;"><img src="//res.weiunity.com/image/weixin_gzh.png" style="width:150px; height:150px;" /></div>'+
	   			'回复“要网站”即可免费得到一个跟此一样的网站。无任何广告！'+
	   			'另外您有什么问题、资源交换、各种合作意向，都可关注后跟我们在线沟通咨询<br/>'+
	   			'我们官网：<a href="http://www.wang.market" target="_black" style="text-decoration: underline;color: blue;">www.wang.market</a><br/>'+
	   			'我的微信：xnx3com &nbsp;&nbsp;&nbsp;QQ：921153866 <br/>'+
	   			'本程序已在GitHub开源：<a href="https://github.com/xnx3/wangmarket" target="_black" style="text-decoration: underline;color: blue;">github.com/xnx3/wangmarket</a><br/>'+
	   			'<div style="padding-top:35px;color: lightcoral; padding-left: 35px;">以高精尖技术压缩建站成本，以超低价甚至免费享受高端体验。<br/>网·市场，让每个人都有自己的网站，让价格不再是阻碍的门槛！</div>'
	  
	});
}
//只有用户名带有ceshi的才会弹出合作联系的提示
if('${user.username}'.indexOf('ceshi') > -1){
	setTimeout("rightTip()",8000);
}
</script>

<% if(com.xnx3.wangmarket.im.Global.kefuMNSUtil != null){ %>
<!-- IM start -->
<script src="//res.weiunity.com/layui217/layui.js"></script>
<script>
var id = ${user.id};	//用户的id，用户唯一
var password = "${password }";	//加密后密码
var username = "${user.nickname }";	//用户昵称，用户在聊天框显示的名字
var sign = '${siteUrl},${site.name}';	//当前用户签名
var socketUrl = '${im_kefu_websocketUrl}'; //socket的url请求地址
</script>
<script src="<%=Global.get("ATTACHMENT_FILE_URL") %>js/im/site.js"></script>
<!-- IM end -->
<% } %>

</body></html>