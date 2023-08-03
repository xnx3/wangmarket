<%@page import="com.xnx3.wangmarket.Authorization"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.xnx3.wangmarket.admin.G"%><!DOCTYPE html>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="网站管理后台"/>
</jsp:include>

<script src="/js/fun.js"></script>
<script src="/js/admin/cms/cms.js?v=<%=G.VERSION %>"></script>
<script>
var masterSiteUrl = window.location.protocol+'//'+location.host+'/';
var autoAssignDomain = '${autoAssignDomain }';

//判断是否有从get传递来的token参数。这个时 adminapi 接口有使用，自动过来登录
var token = getUrlParams('token');
if(token != null && token.length > 10){
	wm.token.set(token);	//更新浏览器token缓存
}
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
	background-color: #393d49;
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
/* 避免屏幕高度太小，造成左侧菜单拉太长，最下面的收缩侧边栏遮挡功能菜单 */
.layui-nav-item{
	background-color: #393D49;
	z-index:99;
}
.subMenuItem{
	font-size:13px;
}
body{
	margin: 0;
	padding: 0px;
	height: 100%;
	overflow: hidden;
}
</style>
<div style="width:100%;height:100%;">

	<div id="leftMenu" class="layui-nav layui-nav-tree layui-nav-side menu">
		<ul class="">
			
			${menuHTML }
			
			<div id="menuAppend" style="margin-left: 3px;">
				<!-- 插件扩展菜单项。追加的值如： -->
				<!-- <li class="layui-nav-item" >
					<a href="/user/logout.do">
						<i class="layui-icon firstMenuIcon">&#xe633;</i>
						<span class="firstMenuFont">退出登陆</span>
					</a>
				</li>
				 -->
			</div>
			
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
	<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}module/editor/css/editormd.css" />
	<script src="${STATIC_RESOURCE_PATH}module/editor/editormd.js"></script>

	<div id="content" style="width: 100%;height:100%;position: fixed;left: 150px;word-wrap: break-word;border-right: 150px;box-sizing: border-box; border-right-style: dotted;">
		<div id="htmledit_openButton" onclick="showHiddenTopNav();" class="top_htmledit_buttom"><i class="layui-icon">&#xe603;</i>隐藏</div>
		<div id="topNav" style="background-color: #2b2e37; color:#009688; height: 50px;line-height: 50px;padding-left: 12px; margin-left:-1px;position: absolute; z-index: 99999999999;width: 100%;">
			<div style="padding-left: 50px;">
				当前修改<span id="currentTemplateType">首页模版</span>：<input type="text" id="currentTemplatePageName" value="" readOnly="true" style="border: 0px; background-color: #2b2e37; overflow: hidden; color: #009688; width: 100px;"/>
				<button id="htmledit_mode" onclick="htmledit_mode();">代码模式</button>
				<span id="baocuninit"><button id="save" onclick="saveHtmlSource();">保存更改</button></span>
				<span>&nbsp;&nbsp;&nbsp;</span>
				可用：
					<button onclick="popupTemplateTagHelp('模版变量-简化模式','/template/templateVarListForUsed.do', '520', '360');">模版变量</button>
					<button id="tongyong" onclick="popupTemplateTagHelp('通用标签','/templateTag/common.do#%E6%A0%87%E7%AD%BE%E5%88%97%E8%A1%A8', '770', '630');">通用标签</button>
					<button id="lanmu" onclick="popupTemplateTagHelp('栏目标签','/templateTag/column.do#%E6%A0%87%E7%AD%BE%E5%88%97%E8%A1%A8','870', '510');">栏目标签</button>
					<button id="fenye" onclick="popupTemplateTagHelp('分页标签','/templateTag/page.do#%E6%A0%87%E7%AD%BE%E5%88%97%E8%A1%A8', '760','605');">分页标签</button>
					<button id="wenzhang" onclick="popupTemplateTagHelp('文章信息标签','/templateTag/news.do#%E6%A0%87%E7%AD%BE%E5%88%97%E8%A1%A8','870', '660');">文章信息标签</button>
					<button id="dongtailanmu" onclick="popupTemplateTagHelp('动态栏目调用','/templateTag/dynamic.do','770', '650');">动态栏目调用</button>
					<button id="xiangqingduyou" onclick="popupTemplateTagHelp('详情页独有','/templateTag/details.do#%E8%AF%A6%E6%83%85%E9%A1%B5%E7%8B%AC%E6%9C%89%E6%A0%87%E7%AD%BE','920', '340');">详情页独有</button>
					<button id="liebiaoduyou" onclick="popupTemplateTagHelp('列表页独有','/templateTag/list.do#%E8%AF%A6%E6%83%85%E9%A1%B5%E7%8B%AC%E6%9C%89%E6%A0%87%E7%AD%BE','670', '425');">列表页独有</button>
			</div>
		</div>
		<style>
			.headerBar{
				display: flex;
				align-items: center;
				justify-content: space-between;
				width: 100%;
				height: 50px;
				background-color: #FFFFFF;
				padding: 0 10px 0 20px;
				box-sizing: border-box;
				border-bottom: 1px solid whitesmoke;
				-webkit-box-shadow: 0 1px 4px rgba(0,21,41,.08);
				box-shadow: 0 1px 4px rgba(0,21,41,.08);
			}
			.headerBar .layui-nav{
				background-color: #FFFFFF !important;
			}
			.headerBar .layui-nav-item{
				background-color: #FFFFFF;
				color: #0C1206;
				line-height: 50px;
			}
			.headerBar .layui-nav .layui-nav-item a {
				color: #0C1206;
			}
			.header_right>ul>#translate {
				display: inline;
			}
			.headerBar .layui-nav .layui-nav-bar{
				top: 0 !important;
				height: 0 !important;
			}
			.headerBar .layui-nav .layui-this:after{
				width: 0 !important;
			}
			.headerBar .layui-nav .layui-nav-more{
				border-top-color: rgba(0,0,0,.7) !important;
			}
			.headerBar .layui-nav .layui-nav-mored{
				transform: rotate(180deg);
			}
			.headerBar .layui-nav .layui-nav-child dd.layui-this a,.headerBar .layui-nav-child dd.layui-this {
				 background-color: #fff;
				 color: #0C1206;
			}
			.headerBar #exit-fullscreen{
				display: none;
			}

			.iframe_wrap{
				width: 100%;
				height: calc(100% - 50px);
				padding: 0px;
				background-color: #f2f2f2;
				box-sizing: border-box;
			}
			#htmlMode{
				padding-bottom: 0!important;
			}
			iframe{
				background-color: #FFFFFF;
				border-radius: 4px;
				margin-top: 0!important;
				padding-bottom: 0!important;
			}
			select {
				/* 鼠标移上，变小手 */
				cursor: pointer;
				padding: 0 10px;
				/* 清除默认的箭头样式 */
				appearance: none;
				-moz-appearance: none;
				-webkit-appearance: none;
				/* 右侧添加箭头的背景图  自行调整位置 */
				background: url('/plugin/login/images/xiala.png') 70px center no-repeat ;
				background-size: 16px;
			}
		</style>
		<div class="headerBar">
			<div class="header_left">
				<a id="showHiddenLeftMenu" href="javascript:zoomLeftMenu();">
					<i class="layui-icon" id="showHiddenLeftMenu_icon2">&#xe668;</i>
					<span class=""></span>
				</a>
			</div>
			<div class="header_right">
				<ul class="layui-nav">
					<li class="layui-nav-item" id="translate">
					</li>
					<li class="layui-nav-item layui-hide-xs" >
						<a href="javascript:;" class="layui-icon layui-icon-screen-full" id="full-screen"></a>
						<a href="javascript:;" class="layui-icon layui-icon-screen-restore" id="exit-fullscreen"></a>
					</li>
					<li class="layui-nav-item layui-hide-xs">
						<a href="javascript:window.open('/sites/sitePreview.do');" id="website" class="layui-icon layui-icon-website"></a>
					</li>
					<li class="layui-nav-item">
						<a class="layui-icon layui-icon-username" href="javascript:;">
							${user.username }
								<i class="layui-icon layui-icon-down layui-nav-more"></i>
						</a>
						<dl class="layui-nav-child">
							<dd><a href="javascript:updatePassword();">修改密码</a></dd>
							<dd><a href="../user/logout.do" class="">退出登录</a></dd>
						</dl>
					</li>
				</ul>
			</div>
		</div>


		<div class="iframe_wrap">
			<iframe name="iframe" id="iframe" frameborder="0" style="width:100%;height:100%;padding-bottom: 0px;box-sizing: border-box;"></iframe>

			<div id="htmlMode" style="width:100%;height:100%; display:none; padding-bottom: 0px;box-sizing: border-box;">
				<style>
					.CodeMirror-linenumber{
						padding:0px;
						padding-left:1px;
					}
				</style>
				<div id="editormd" style="width:100%; height:100%; margin-top: 0px;"></div>
				<textarea id="html_textarea"></textarea>
			</div>
		</div>
	</div>
</div>

<!-- commonedit.js 需要的变量 -->
<script>
	var resBasePath = '${STATIC_RESOURCE_PATH}';
</script>
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
//var testEditor;

//以代码模式编辑模版页面
function codeEditMode(){
	useTopTools();
	document.getElementById("htmledit_mode").style.display='none';	//将 代码模式、智能模式切换的按钮隐藏。代码模式下，不需要再有智能模式
	
	/*
	if(typeof(textEditor) != 'undefined'){
		try{
			//清空上次的
			testEditor.setValue('');
		}catch(e){}
	}
	*/
	//判断一下，如果上次是智能模式，那么切换回代码模式
	if(currentMode == 1){
		//由智能模式切换代码模式
		document.getElementById("iframe").style.display='none';
		document.getElementById("iframe").style.marginTop='0px';
		document.getElementById("htmlMode").style.display='';
		
		document.getElementById("htmledit_mode").innerHTML = '智能模式';
		currentMode = 2;
	}
	
	msg.loading("加载中");	//显示“操作中”的等待提示
	$.post("/template/getTemplatePageText.do?pageName="+document.getElementById("currentTemplatePageName").value, function(data){
		msg.close();	//关闭“操作中”的等待提示
		document.getElementById("html_textarea").value=data;
		
		/*
		testEditor = editormd("editormd", {
			width			: "100%",
			height			: "100%",
			watch			: false,
			toolbar			: false,
			codeFold		: true,
			searchReplace	: true,
			placeholder		: "请输入html代码",
			value			: data,
			theme			: "default",
			mode			: "text/html",
			path			: '${STATIC_RESOURCE_PATH}module/editor/lib/'
		});
		*/
		//edit.extend[edit.currentMode].edit(data);
		edit.extend.editormd.edit(data);
		
	},'text');
}


function htmledit_mode(){
	msg.loading('切换中');
	document.getElementById("htmledit_mode").style.display='';	//将 代码模式、智能模式切换的按钮显示。智能模式下，一直优惠智能模式、代码模式的切换按钮
	if(currentMode == 1){
		//由智能模式切换代码模式
		edit.destroy(); //销毁可视化
		
		document.getElementById("iframe").style.display='none';
		document.getElementById("iframe").style.marginTop='0px';
		document.getElementById("htmlMode").style.display='';
		
		//判断一下，如果模版页面不是正常的HTML模版，那么在切换到代码模式时，不进行赋值textarea的操作
		var html = '';
		try {
			//html = getHtmlSource();
			html = edit.extend[edit.currentExtend].html();
			//console.log(html);
		} catch(error) { console.log(error); }
		
		if(html != ''){
			//由智能模式切换代码模式
			//var htmlsource = getHtmlSource();
			var htmlsource = edit.extend[edit.currentExtend].html();
			
			document.getElementById("html_textarea").value=htmlsource;
			/*
			testEditor = editormd("editormd", {
				width			: "100%",
				height			: "100%",
				watch			: false,
				toolbar			: false,
				codeFold		: true,
				searchReplace	: true,
				placeholder		: "请输入html代码",
				value			: document.getElementById("html_textarea").value,
				theme			: "default",
				mode			: "text/html",
				path			: '${STATIC_RESOURCE_PATH}module/editor/lib/'
			});
			*/
			edit.extend.editormd.edit(htmlsource);
		}
		
		document.getElementById("htmledit_mode").innerHTML = '智能模式';
		currentMode = 2;
		
		
	}else{
		//由代码模式切换智能模式
		
		document.getElementById("iframe").style.display='';
		document.getElementById("iframe").style.marginTop='0px';
		document.getElementById("htmlMode").style.display='none';
		
		//将editormd的值转到textarea中
		//document.getElementById("html_textarea").value = testEditor.getValue();
		var htmlsource = edit.extend.editormd.html(); //取editormd 编辑器中的html
		document.getElementById("html_textarea").value = htmlsource;
		/*
		var o = document.getElementById("iframe");
		ed = document.all ? o.contentWindow.document : o.contentDocument;
		ed.open();
		ed.write(testEditor.getValue());
		ed.close();
		*/
		edit.extend[edit.currentExtend].edit(htmlsource);
		
		document.getElementById("htmledit_mode").innerHTML = '代码模式';
		currentMode = 1;
	}
	msg.close();
}

/*
function getHtmlSource(){
	return $(window.parent.document).contents().find("#iframe")[0].contentWindow.xnx3_getHtmlSource();
}
*/

//保存HTML源代码
function saveHtmlSource(){
	//html源代码
	var html;
	if(currentMode == 1){
		//傻瓜模式，再iframe中
		//html = getHtmlSource();
		html = edit.extend[edit.currentExtend].html();
	}else{
		//开发者模式，在 editormd 中获取，（已不在textarea中，textarea只是切换智能模式与代码模式的中转站）
		
		//将editormd的值转到textarea中
		var html = edit.extend.editormd.html(); //取editormd 编辑器中的html
		//document.getElementById("html_textarea").value = testEditor.getValue();
		document.getElementById("html_textarea").value = html;
	}
	
	msg.loading('保存中');
	var pageName = document.getElementById("currentTemplatePageName").value;
	$.post("/template/saveTemplatePageText.do", {pageName: pageName, html: html}, function(data){
		msg.close();
		if(data.result == 1){
			msg.success("保存成功");
		}else{
			msg.failure(data.info);
		}
	});
}


//Jquery layer 提示

$(function(){
	//编辑模式
	msg.tip({id:'htmledit_mode',text:'可以快速您网站当前的编辑模式<br/><b>智能模式</b>：想改哪里点哪里，点了直接改！修改时右键还会有更多的选择！<br/><b>代码模式</b>：以最原始代码的形式提供编辑。建议有一定编程基础或者稍微懂点的专业人士使用'})

	//保存按钮
	msg.tip({id:'save',text:'将您当前所操作的页面保存。保存后可预览查看，刷新一下看看其是否已改变'})

	//全局变量
	msg.tip({id:'quanjubianliang',text:'当前网站模板中可以调用的变量，可再模版页面、模板变量中，使用<div style="padding:5px; font-size:16px">{var.xxx}</div>将再此设定的全局变量的值调出。'})

	//模版变量
	msg.tip({id:'mobanbianliang',text:'当前网站模版页面所使用的动态变量内容，可在模版页面中使用<div style="padding:5px; font-size:16px">{include=变量名}</div>随处调出使用、显示'})

	//模版页面
	msg.tip({id:'mobanyemian',text:'当前网站所使用的模版，如首页、列表、内容详情等。'})

	//输入模型
	msg.tip({id:'shurumoxing',text:'自定义用户内容管理时，文章编辑的添加数据的模版，比如，有的栏目有标题图片，有的栏目没有，都可以在这里配置 <br/><b>供开发模版的专业人员使用，建议使用网站的用户不懂的情况下不要修改此处</b>'})

	//导入模版
	msg.tip({id:'daorutemplate',text:'1.&nbsp;&nbsp;当网站刚创建，但尚未有模版时，此处可从云端模版库、或者本地自有模版中导入现有的模版，快速创建全局变量、模版页面、模版变量、输入模型、栏目，实现极速建站<br/>2.&nbsp;&nbsp;当模版已经存在，使用了模版后，可以通过此处进行还原操作。还原回模版某一时刻的样子'})

	//导出模版
	msg.tip({id:'daochutemplate',text:'1.&nbsp;&nbsp;将当前网站的模版导出，可以在新网站中将导出的模版文件导入，实现快速复制网站。<br/>2.&nbsp;&nbsp;也会起到备份模版的作用，网站如果操作失误删除东西了，可以通过此处进行还原回来'})

	msg.tip({id:"mobanshiyongshuoming",text:'若您使用的是官方制作的云端模版，这里可显示当前模版的使用说明，快速上手知晓如何使用'})

	msg.tip({id:"fangwentongji",text:'当前网站的访问情况、访问记录，以及各搜索引擎抓取情况等'})
	
	//模版的开发文档
	msg.tip({id:"mobankaifa",text:'模版开发入门、模版制作帮助文档，帮您十分钟入门，2天成大神。<br/>文档正在完善中'})

	//栏目管理
	msg.tip({id:"column",width:"auto",text:"网站的栏目信息"})

	//内容管理
	msg.tip({id:"news",text:"网站内容，如关于我们、新闻、产品的内容，都是在这里"})
	
	//生成整站
	msg.tip({id:"generatehtml",text:'网站添加新闻了、产品了、修改过模版了，凡是网站有过修改，都要生成整站后，网站中才会变过来'})
	
	
	//帮助说明－基本功能 说明
	msg.tip({id:"shiyongrumen",text:'网站基本功能使用说明，基本功能介绍，网站使用引导。看完15分钟视频，便可熟练操作网站'})


	//模版管理－模版插件
	msg.tip({id:"templateplugin",text:'在您现有的模版基础上，扩展更多功能！让您的网站，拥有无限可能！<br/>更多插件持续增加中，敬请期待'})

	
	//技术支持，显示自己的上级代理商
	msg.tip({id:"support",text:'操作网站的过程中，有什么不懂的地方，尽可以联系我们，帮助您拥有自己的网站！'})


	// 未授权用户，请尊重作者劳动成果，保留我方版权标示及链接！授权参见：http://www.wang.market/price.html 
	<% if(Authorization.copyright){ %>
	//显示、隐藏侧边栏
	msg.tip({id:"showHiddenLeftMenu",width:"auto",text:'power by 网市场'})

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
		try{
			document.getElementById("jibenxinxi").innerHTML = '基本信息';
		}catch(e){}
		try{
			document.getElementById("wangzhanshuxing").innerHTML = '网站设置';
		}catch(e){}
		try{
			document.getElementById("chakanwangzhan").innerHTML = '预览网站';
		}catch(e){}
		try{
			document.getElementById("wentifankui").innerHTML = '问题反馈';
		}catch(e){}
		try{
			document.getElementById("quanjubianliang").innerHTML = '全局变量';
		}catch(e){}
		try{
			document.getElementById("mobanbianliang").innerHTML = '模版变量';
		}catch(e){}
		try{
			document.getElementById("mobanyemian").innerHTML = '模版页面';
		}catch(e){}
		try{
			document.getElementById("daochutemplate").innerHTML = '导出/备份';
		}catch(e){}
		try{	
			document.getElementById("daorutemplate").innerHTML = '导入/还原';
		}catch(e){}
		try{
			document.getElementById("mobankaifa").innerHTML = '开发文档';
		}catch(e){}
		try{
			document.getElementById("mobanshiyongshuoming").innerHTML = '使用说明';
		}catch(e){}
		try{
			document.getElementById("shiyongrumen").innerHTML = '基本使用';
		}catch(e){}
		try{
			document.getElementById("shurumoxing").innerHTML = '输入模型';
		}catch(e){}
		try{
			document.getElementById("xiugaimima").innerHTML = '修改密码';
		}catch(e){}
		try{
			document.getElementById("templateplugin").innerHTML = '模版插件';
		}catch(e){}
		
		
		//左下角的缩放按钮
		document.getElementById("showHiddenLeftMenu_icon").innerHTML = '&#xe603;';
		document.getElementById("showHiddenLeftMenu_icon2").innerHTML = '&#xe668;';
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
		try{
			document.getElementById("jibenxinxi").innerHTML = '信息';
		}catch(e){}
		try{
			document.getElementById("wangzhanshuxing").innerHTML = '设置';
		}catch(e){}
		try{
			document.getElementById("chakanwangzhan").innerHTML = '预览';
		}catch(e){}
		try{
			document.getElementById("wentifankui").innerHTML = '反馈';
		}catch(e){}
		
		try{
			document.getElementById("quanjubianliang").innerHTML = '全局';
		}catch(e){}
		try{
			document.getElementById("mobanbianliang").innerHTML = '变量';
		}catch(e){}
		try{
			document.getElementById("mobanyemian").innerHTML = '页面';
		}catch(e){}
		try{
			document.getElementById("daochutemplate").innerHTML = '备份';
		}catch(e){}
		try{
			document.getElementById("daorutemplate").innerHTML = '还原';
		}catch(e){}
		try{
			document.getElementById("mobankaifa").innerHTML = '文档';
		}catch(e){}
		try{
			document.getElementById("mobanshiyongshuoming").innerHTML = '使用';
		}catch(e){}
		try{
			document.getElementById("shiyongrumen").innerHTML = '基本';
		}catch(e){}
		try{
			document.getElementById("shurumoxing").innerHTML = '输入';
		}catch(e){}
		try{
			document.getElementById("xiugaimima").innerHTML = '改密';
		}catch(e){}
		try{
			document.getElementById("templateplugin").innerHTML = '插件';
		}catch(e){}
		
		
		//左下角的缩放按钮
		document.getElementById("showHiddenLeftMenu_icon").innerHTML = '&#xe602;';
		document.getElementById("showHiddenLeftMenu_icon2").innerHTML = '&#xe66b;';
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
			,content: '${STATIC_RESOURCE_PATH}template/'+tn+'/useExplain.html'
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
	
	msg.popups({
		text:content,
		padding:'1px',
		height:'255px',
		width:'300px',
	});

}

//入门视频，引导
function helpVideo(){
	//收起所有
	backAllMenu();
	
	//展开某个一级栏目
	unfoldFirstColumn('help');
	//选中某个菜单
	selectedLeftMenu('dd_shiyongrumen');
	
	//打开视频说明
	loadIframeByUrl('<%=Global.get("SITEUSER_FIRST_USE_EXPLAIN_URL") %>');
}


//模版开发入门-引导
function templateDevHelp(){
	//收起所有展开的一级栏目、一级取消选中的功能菜单
	backAllMenu();
	
	//展开某个一级栏目
	unfoldFirstColumn('help');
	//选中某个菜单
	selectedLeftMenu('dd_mobankaifa');
	
	//打开模版管理
	loadIframeByUrl('<%=Global.get("SITE_TEMPLATE_DEVELOP_URL") %>');
}

//右侧弹出提示
function rightTip(){
	layer.open({
		title: '演示站点提示文字',offset: 'rb', shadeClose:true, shade:0
		,area: ['500px', 'auto']
		,btn: ['我知道了'] //可以无限个按钮
		,content: '若我方对你有用，我们愿与各行业进行合作、资源交换！网站可由代理平台在线开通，或由用户自己自助开通完全无人干预！<a href="http://www.wang.market/index.html#join" target="_black" style="text-decoration: underline;color: blue;">合作方式</a><br/>'+
			'若您只是想要个此类网站，你可关注我们微信公众号： wangmarket'+
			'<div style="text-align:center;"><img src="${STATIC_RESOURCE_PATH}image/weixin_gzh.png" style="width:150px; height:150px;" /></div>'+
			'回复“要网站”即可免费得到一个跟此一样的网站。无任何广告！'+
			'另外您有什么问题、资源交换、各种合作意向，都可关注后跟我们在线沟通咨询<br/>'+
			'我们官网：<a href="http://www.wang.market" target="_black" style="text-decoration: underline;color: blue;">www.wang.market</a><br/>'+
			'我的微信：xnx3com &nbsp;&nbsp;&nbsp;QQ：921153866 <br/>'+
			'本程序已在GitHub开源：<a href="https://github.com/xnx3/wangmarket" target="_black" style="text-decoration: underline;color: blue;">github.com/xnx3/wangmarket</a><br/>'+
			'<div style="padding-top:35px;color: lightcoral; padding-left: 35px;">以高精尖技术压缩建站成本，以超低价甚至免费享受高端体验。<br/>网·市场，让每个人都有自己的网站，让价格不再是阻碍的门槛！</div>'
	
	});
}

//向扩展菜单的div中，加入html。也就是往里再增加别的菜单。 appendHtml要追加的html，这里一般都是追加li
function menuAppend(appendHtml){
	document.getElementById("menuAppend").innerHTML = document.getElementById("menuAppend").innerHTML + appendHtml; 
}

//只有用户名带有ceshi的才会弹出合作联系的提示
if('${user.username}'.indexOf('ceshi') > -1){
	//v6.1更新，去掉这个了
	//setTimeout("rightTip()",8000);
}


</script>

<!-- 引导 -->
<script src="${STATIC_RESOURCE_PATH}module/driver.js/driver.min.js"></script>
<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}module/driver.js/driver.min.css">
<script>
/**
 * 运行引导。这个是在选择了 lmyglm1模板后触发的引导
 */
function yindaoStart(){
	const driver = new Driver({
		doneBtnText: '结束指引', // 最终按钮上的文本 Text on the final button
		closeBtnText: '关闭', // 当前步骤关闭按钮上的文本 Text on the close button for this step
		nextBtnText: '下一步', //当前步骤下一步按钮上的文本 Next button text for this step
		prevBtnText: '上一步', // 当前步骤上一步按钮上的文本 Previous button text for this step
		onReset: function(Element) {
			//打开全局变量
			loadIframeByUrl('/siteVar/list.do');
		},		// 遮罩将要关闭时调用
	});
	//Define the steps for introduction
	driver.defineSteps([
		{
			element: '#quanjubianliang',
			popover: {
				title: '模板管理下的全局变量',
				description: '像是一些基本的信息，如LOGO、Banner、地址...等信息，都可以在这里面进行修改',
				position: 'right'
			}
		},
		{
			element: '#li_news',
			popover: {
				title: '内容管理',
				description: '像是新闻资讯等文章的发布、产品的添加、关于我们公司荣誉等页面内容的填充等，都是在内容管理中操作的',
				position: 'right'
			}
		},
		{
			element: '#li_generatehtml',
			popover: {
				title: '点击[生成整站]来生成网站',
				description: '网站添加新闻了、产品了、修改过模版了，凡是网站有过修改，都要生成整站后，网站中才会变过来',
				position: 'right'
			}
		}
	]);

	//展开模板管理
	unfoldFirstColumn('li_template');
	//Start the introduction
	driver.start();
}

</script>

${pluginAppendHtml}

<jsp:include page="/wm/common/foot.jsp"></jsp:include>

<%--全屏切换--%>
<script>
	(function () {
		var viewFullScreen = document.getElementById("full-screen");
		if (viewFullScreen) {
			viewFullScreen.addEventListener("click", function () {
				viewFullScreen.style.display="none"
				cancelFullScreen.style.display="block"
				var docElm = document.documentElement;
				if (docElm.requestFullscreen) {
					docElm.requestFullscreen();
				}
				else if (docElm.msRequestFullscreen) {
					docElm.msRequestFullscreen();
				}
				else if (docElm.mozRequestFullScreen) {
					docElm.mozRequestFullScreen();
				}
				else if (docElm.webkitRequestFullScreen) {
					docElm.webkitRequestFullScreen();
				}
			}, false);
		}

		var cancelFullScreen = document.getElementById("exit-fullscreen");
		if (cancelFullScreen) {
			cancelFullScreen.addEventListener("click", function () {
				viewFullScreen.style.display="block"
				cancelFullScreen.style.display="none"
				if (document.exitFullscreen) {
					document.exitFullscreen();
				}
				else if (document.msExitFullscreen) {
					document.msExitFullscreen();
				}
				else if (document.mozCancelFullScreen) {
					document.mozCancelFullScreen();
				}
				else if (document.webkitCancelFullScreen) {
					document.webkitCancelFullScreen();
				}
			}, false);
		}


		var fullscreenState = document.getElementById("fullscreen-state");
		if (fullscreenState) {
			document.addEventListener("fullscreenchange", function () {
				fullscreenState.innerHTML = (document.fullscreenElement) ? "" : "not ";
			}, false);

			document.addEventListener("msfullscreenchange", function () {
				fullscreenState.innerHTML = (document.msFullscreenElement) ? "" : "not ";
			}, false);

			document.addEventListener("mozfullscreenchange", function () {
				fullscreenState.innerHTML = (document.mozFullScreen) ? "" : "not ";
			}, false);

			document.addEventListener("webkitfullscreenchange", function () {
				fullscreenState.innerHTML = (document.webkitIsFullScreen) ? "" : "not ";
			}, false);
		}

	})();
</script>
<%--顶部nav提示tip--%>
<script>
	// function show(msg, id){
	// 	layer.tips(''+ msg, '#'+id, {tips: [1,'#10a6a8']});
	// }
	// function close_tips(){
	// 	layer.closeAll('tips');
	// }
	msg.tip({id:"full-screen",direction:"bottom",width:"auto",text:"进入全屏"})
	msg.tip({id:"exit-fullscreen",direction:"bottom",width:"auto",text:'退出全屏'})
	msg.tip({id:"website",direction:"bottom",width:"auto",text:'预览网站'})
</script>

${HtmlVisualPluginList}
<!-- HtmlVisual 插件 -->
<c:forEach var="htmlVisual" items="${HtmlVisualPluginList}">
<script>
if('${htmlVisual.name}'.length > 0){
	console.log('加载 HtmlVisual 插件 ：${htmlVisual.name}');
	wm.load.synchronizesLoadJs('${htmlVisual.url}');
	//加载完毕后执行初始化方法
	edit.extend['${htmlVisual.id}'].init();
	//设置当前可视化变为方式为这个插件
	edit.currentExtend = '${htmlVisual.id}';
}
</script>
</c:forEach>

<style> /* 显示多语种切换 */ .translateSelectLanguage{ display:block; top: 0;padding: 0.3rem 0.6rem!important;border-radius: 30px;transform: translateY(-4px);font-size: 12px; width:94px;} </style>