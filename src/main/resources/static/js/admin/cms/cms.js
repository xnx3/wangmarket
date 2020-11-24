var currentMode = 1;	//默认为智能模式，1智能模式、2代码模式


//显示、隐藏顶部的导航栏
var currentTopNavShow = true;	//当前默认的顶部导航是显示的 
function showHiddenTopNav(){
	if(currentTopNavShow){
		currentTopNavShow = false;
		document.getElementById("htmledit_openButton").innerHTML = '展开<i class="layui-icon">&#xe602;</i>';
		document.getElementById("topNav").style.display='none';
		
		//主体区域的iframe跟textarea的高度变为100%
		document.getElementById("iframe").style.paddingBottom='0px';
		document.getElementById("htmlMode").style.paddingBottom='0px';
	}else{
		currentTopNavShow = true;
		document.getElementById("htmledit_openButton").innerHTML = '<i class="layui-icon">&#xe603;</i>隐藏';
		document.getElementById("topNav").style.display='';
		
		//主体区域的iframe跟textarea的高度变为底部缩紧38px
		document.getElementById("iframe").style.paddingBottom='38px';
		document.getElementById("htmlMode").style.paddingBottom='38px';
	}
}


/**
 * 弹出模版标签帮助窗口
 * @param title 窗口的标题文字
 * @param htmlNameTag 帮助的网址后面的描点名字。v4.8更新以后，这里便是打开的url
 * @param height 弹出窗口的宽度,整数。会自动拼接px
 * @param height 弹出窗口的高度,整数。会自动拼接px
 */ 
function popupTemplateTagHelp(title,htmlNameTag, width, height){
	var url = htmlNameTag;
//	if(htmlNameTag.indexOf('http://') > -1){
//		url = htmlNameTag;
//	}else{
//		url = 'http://res.weiunity.com/html/templateTag/index.html#'+htmlNameTag;
//	}
	layer.open({
		type: 2 //iframe
		,title:title
		,area: [width+'px', height+'px']
		,shade: 0
		,offset: [ //为了演示，随机坐标
			Math.random()*($(window).height()-300)
			,Math.random()*($(window).width()-390)
		]
 		,maxmin: true
		,content: url
		,zIndex: layer.zIndex //重点1
		,success: function(layero){
			layer.setTop(layero); //重点2
  		}
	});
}


//使用头部工具栏，使用时，会在头部出现工具栏，或者出现展开的按钮。用于模版页面的编辑
function useTopTools(){
	document.getElementById("htmledit_openButton").style.display='';
	if(currentTopNavShow == false){
		document.getElementById("htmledit_openButton").innerHTML = '展开<i class="layui-icon">&#xe602;</i>';
		document.getElementById("topNav").style.display='none';
		
		//主体区域的iframe跟textarea的高度变为100%
		document.getElementById("iframe").style.paddingBottom='0px';
		document.getElementById("htmlMode").style.paddingBottom='0px';
	}else{
		document.getElementById("htmledit_openButton").innerHTML = '<i class="layui-icon">&#xe603;</i>隐藏';
		document.getElementById("topNav").style.display='';
		
		//主体区域的iframe跟textarea的高度变为底部缩紧38px
		document.getElementById("iframe").style.paddingBottom='38px';
		document.getElementById("htmlMode").style.paddingBottom='38px';
	}
	document.getElementById("iframe").style.marginTop='38px';
}

//不使用头部工具栏，不使用时，头部什么也没有。用于模版页面的编辑
function notUseTopTools(){
	//隐藏头部
	document.getElementById("htmledit_openButton").style.display = 'none';
	document.getElementById("topNav").style.display='none';
	
	//主体区域的iframe跟textarea的高度变为100%
	document.getElementById("iframe").style.paddingBottom='0px';
	document.getElementById("iframe").style.marginTop='0px';
	document.getElementById("htmlMode").style.paddingBottom='0px';
}



//收起、取消选中所有一级栏目、二级栏目的,将其恢复至初始状态
function backAllMenu(){
	//将所有id为 dd_ 的class name 去除，二级菜单使用的这个
	$("dd[id^=dd_]").attr("class","");
	
	//一级菜单
	$("li[id^=li_]").attr("class","layui-nav-item");

	try{ 
		document.getElementById("plugin").setAttribute("class", "layui-nav-item");
	}catch(e){}
}


/**
 * 展开某个一级栏目，将某个一级栏目的二级菜单展开。
 * @param id 要展开的栏目id，包含： li_system 系统管理；li_template 模版管理； li_log 日志管理 ； li_help 帮助 ； li_kefu 客服 ； plugin 功能插件
 */
function unfoldFirstColumn(id){
	document.getElementById(id).setAttribute("class", "layui-nav-item layui-nav-itemed");
}

/**
 * 左侧菜单模拟选中
 * @param id dd标签的id
 */
function selectedLeftMenu(id){
	document.getElementById(id).setAttribute("class", "layui-this");
}


/**
 * 模版页面列表，引导打开
 * @param templatePageName templatePage.name 要打开编辑的模版页面的name。  如果传入''空字符串，则直接进入模版页面列表，不自动打开某个模版页面。 另外，如果传入 templatepage_type_index 则会编辑首页
 */
function openTemplatePageList(templatePageName){
	//收起所有菜单
	backAllMenu();
	//展开某个一级栏目
	unfoldFirstColumn('li_template');
	//选中某个菜单
	selectedLeftMenu('dd_mobanyemian');
	
	
	if(templatePageName.length == 0){
		//打开模版页面列表
		loadIframeByUrl('templatePageList.do');
	}else{
		//编辑某个模版页面
		loadIframeByUrl('templatePageList.do?templatePageName='+templatePageName);
	}
}



//绑定域名，引导打开
function openBindDomain(){
	//收起所有一级栏目
	backAllMenu();
	//展开某个一级栏目
	unfoldFirstColumn('li_system');
	//选中某个菜单
	selectedLeftMenu('dd_domainBind');
	
	//调出绑定域名
	updateBindDomain();
}


//加载iframe，默认加载首页index
//document.getElementById("currentTemplatePageName").value = '${templatePage.name}';	//当前模版页面的名字，修改时必须跟iframe.src一块改动
function loadIframe(){
	useTopTools();
	document.getElementById("iframe").src='getTemplatePageText.do?pageName='+document.getElementById("currentTemplatePageName").value;
}


//在主题内容区域iframe中加载制定的页面
function loadIframeByUrl(url){
	if(currentMode == 2){
		//如果当前是编辑模版页的代码模式下，将其切换回智能模式。不然内容管理等这些就显示不出来了
		htmledit_mode();
	}
	document.getElementById("iframe").src=url;
}
