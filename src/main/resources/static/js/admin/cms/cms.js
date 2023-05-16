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
	//console.log('getTemplatePageText.do?pageName='+document.getElementById("currentTemplatePageName").value);
	//document.getElementById("iframe").src='getTemplatePageText.do?pageName='+document.getElementById("currentTemplatePageName").value;
	var url ='/template/getTemplatePageText.do?pageName='+document.getElementById("currentTemplatePageName").value;
	msg.loading('加载中');
	/*
	wm.post(url, {}, function(data){
		msg.close();
		//console.log(edit.currentExtend)
		edit.extend[edit.currentExtend].edit(data);
	});
	*/
	
	$.post(url, function(data){
		msg.close();	//关闭“操作中”的等待提示
		edit.extend[edit.currentExtend].edit(data);
	},'text');
	
}


//在主题内容区域iframe中加载制定的页面
function loadIframeByUrl(url){
	if(currentMode == 2){
		//如果当前是编辑模版页的代码模式下，将其切换回智能模式的显示形态，只是显示形态，将iframe显示。不然内容管理等这些就显示不出来了
		document.getElementById("iframe").style.display='';
		document.getElementById("iframe").style.marginTop='0px';
		document.getElementById("htmlMode").style.display='none';
		document.getElementById("htmledit_mode").innerHTML = '代码模式';
		currentMode = 1;
	}
	msg.closeAllSimpleMsg();	//修复模板页面保存后立即点别的如栏目管理，提示信息不自动取消的问题
	
	edit.destroy(); //销毁可视化，如果是可视化编辑情况下的话
	
	document.getElementById("iframe").src=url;
}



/************ 可视化编辑 */
var edit = {
	//当前编辑的模式，有两种， code 代码模式  visual 可视化模式
	// HtmlVisualEditor htmledit
	currentExtend:'HtmlVisualEditor',
	extend:{
		
		
		
	},
	//销毁可视化
	destroy:function(){
		try{
			if(typeof(edit) != 'undefined' && edit != null && typeof(edit.currentExtend) != 'undefined' && edit.currentExtend.length > 0){
				if(typeof(edit.extend[edit.currentExtend]) != 'undefined' && typeof(edit.extend[edit.currentExtend].destroy) != 'undefined'){
					edit.extend[edit.currentExtend].destroy(); //执行可视化的销毁
				}
			}
		}catch(e){
			console.log(e);
		}
	}
	
	/*
	* 进入使用可视化编辑模式
	* html 要可视化编辑时，其中默认的html
	*/ 
	/*
	visualEdit:function(html){
		
		document.getElementById("iframe").style.display='';
		document.getElementById("iframe").style.marginTop='0px';
		document.getElementById("htmlMode").style.display='none';
		
		 // //将editormd的值转到textarea中
		document.getElementById("html_textarea").value = html;
		
		var o = document.getElementById("iframe");
		ed = document.all ? o.contentWindow.document : o.contentDocument;
		ed.open();
		ed.write(html);
		ed.close();
		
		document.getElementById("htmledit_mode").innerHTML = '代码模式';
		this.currentMode = 'visual';
	}
	*/
};

//editormd 的代码编辑
edit.extend.editormd = {
	editormdObj : null,
	
	/*
	 * 进入使用这种编辑模式进行编辑
	 * html 要编辑时，其中默认的html
	 */ 
	edit:function(html){
		if(typeof(this.editormdObj) != 'undefined'){
			try{
				//清空上次的
				this.editormdObj.setValue('');
			}catch(e){}
		}
		
		this.editormdObj = editormd("editormd", {
			width			: "100%",
			height			: "100%",
			watch			: false,
			toolbar			: false,
			codeFold		: true,
			searchReplace	: true,
			placeholder		: "请输入html代码",
			value			: html,
			theme			: "default",
			mode			: "text/html",
			//path			: '${STATIC_RESOURCE_PATH}module/editor/lib/'
			path			: '/module/editor/lib/'
		});
		
	},
	//获取当前编辑的html的内容
	html:function(){
		if(typeof(this.editormdObj) != 'undefined'){
			return this.editormdObj.getValue();
		}
		return '异常，编辑对象未发现';
	}
}
