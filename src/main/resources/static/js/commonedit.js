/** 已废弃。新的在 admin/commonedit.js **/

try{
	//加载 layer
	dynamicLoading.js(resBasePath+"layer/layer.js");
	dynamicLoading.css(resBasePath+'layer/skin/default/layer.css');
	
	//加载layui
	//dynamicLoading.js(resBasePath+"layui2/layui.js");
	//dynamicLoading.css(resBasePath+'layui2/css/layui.css');
	
	//WEI UI
	dynamicLoading.js(resBasePath+"js/jquery-weui.js");
	dynamicLoading.css(resBasePath+"css/weui.min.css");
	dynamicLoading.css(resBasePath+"css/jquery-weui.css");

	/*拖拽控件*/
	dynamicLoading.js(resBasePath+"js/HTML.min.js");
	dynamicLoading.js(resBasePath+"js/Sortable.js");
}catch(err){}

try{
	//加载iw.js
	if(typeof(iw) == 'undefined'){
		dynamicLoading.js(resBasePath+"js/iw.js");
	}
}catch(err){}

// v4.8 废弃 masterSiteUrl 参数，以更好适配 https
//判断是否加载此js之前，定义了 masterSiteUrl
//if(typeof(masterSiteUrl)=="undefined" || masterSiteUrl.length < 2){
//	var masterSiteUrl = 'http://wang.market/';
//}
//判断是否加载此js之前，定义了 AUTO_ASSIGN_DOMAIN(system表的泛解析域名)，自动分配的二级域名，以及要绑定顶级域名的CNAME记录指向
if(typeof(autoAssignDomain)=="undefined" || autoAssignDomain.length < 2){
	var autoAssignDomain = 'wang.market';
}


//修改站点底部联系信息
function updateFooterSiteInfo(){
	layer.open({
		type: 2, 
		title:'修改联系信息', 
		area: ['600px', '460px'],
		shadeClose: true, //开启遮罩关闭
		content: '/site/popupSiteUpdate.do'
	});
}

/**
 * 修改首页的html源代码
 */
function updateIndexHtmlSource(){
//	var updateIndexHtmlSourceVar = layer.open({  
//        type: 2,  
//        content: '/site/editPageSource.do?fileName=index',
//        area: ['320px', '195px'],
//        maxmin: true,
//        shadeClose: true, //开启遮罩关闭
//        title: '修改HTML源代码'
//    });
//	layer.full(updateIndexHtmlSourceVar);
	updateHtmlSource('index');
}

//修改某个html页面的源代码
function updateHtmlSource(fileName){
	htmlSource('0', fileName);
}

/**
 * 打开：编辑自定义HTML页面，添加或编辑
 * @param fileName 要操作的页面的名字，不加.html，不加路径
 * @param isNewHtml 是否是新建，1:新建页面，0:修改页面
 */
function htmlSource(isNewHtml, fileName){
	var updateIndexHtmlSourceVar = layer.open({  
        type: 2,  
        content: '/sitePc/editPageSource.do?fileName='+fileName+(isNewHtml=='1' ? "&newHtml=1":""),
        area: ['320px', '195px'],
        maxmin: true,
        shadeClose: true, //开启遮罩关闭
        title: '编辑页面HTML源代码'
    });
	layer.full(updateIndexHtmlSourceVar);
}

/**
 * 打开积分首页，弹窗
 */
function openMoneyIndex(){
	var openMoneyIndexVar = layer.open({  
        type: 2,  
        content: '/currency/index.do',
        area: ['320px', '195px'],
        maxmin: true,
        shadeClose: true, //开启遮罩关闭
        title: '积分兑换'
    });
	layer.full(openMoneyIndexVar);
}

/**
 * 当前页面弹出我的下线用户列表
 */
function openMyInviteList(){
	layer.open({  
        type: 2,  
        content: '/currency/inviteList.do',
        area: ['390px', '500px'],
        maxmin: true,
        shadeClose: true, //开启遮罩关闭
        title: false
    });
}

//打开自定义HTML页面列表
function openCustomPageList(){
	var openCustomPageListVar = layer.open({
		type: 2,
		closeBtn: 1, //不显示关闭按钮
		anim: 3, area:['600px','680px'],
		shadeClose: true, //开启遮罩关闭
		content: '/page/customPageList.do',
		title: false,
		closeBtn: 1
	});
	layer.full(openCustomPageListVar);
}

//打开访问统计界面
function openFangWenTongJi(){
	var fangwentongjiVar = layer.open({  
        type: 2,  
        content: '/requestLog/index.do',
        area: ['320px', '195px'],
        maxmin: true,
        shadeClose: true, //开启遮罩关闭
        title: '访问统计'
    });
	layer.full(fangwentongjiVar);
}

//打开栏目管理，栏目列表，针对PC端通用模版模式下
function openColumnList(){
	var openColumnListVar = layer.open({
		type: 2,
		anim: 3, area:['733px','600px'],
		shadeClose: true, //开启遮罩关闭
		content: '/column/popupList.do',
		title: false,
		closeBtn: 1
	});
	layer.full(openColumnListVar);
}

//打开栏目管理，栏目列表，针对CMS模式下
function openColumnListForTemplate(){
	var openColumnListVar = layer.open({
		type: 2,
		anim: 3, area:['733px','600px'],
		shadeClose: true, //开启遮罩关闭
		content: '/column/popupListForTemplate.do',
		title: false,
		closeBtn: 1
	});
	layer.full(openColumnListVar);
}

//打开当前网站的模版变量列表，适用于自定义模版类型
function openTemplateVarList(){
	var openTemplateVarListVar = layer.open({
		type: 2,
		closeBtn: 1, //不显示关闭按钮
		anim: 3, area:['600px','680px'],
		shadeClose: true, //开启遮罩关闭
		content: '/template/templateVarList.do',
		title: '模版变量列表',
		closeBtn: 1
	});
	layer.full(openTemplateVarListVar);
}

//打开当前网站的模版页面列表，适用于自定义模版类型
function openTemplatePageList(){
	var openTemplatePageListVar = layer.open({
		type: 2,
		closeBtn: 1, //不显示关闭按钮
		anim: 3, area:['600px','680px'],
		shadeClose: true, //开启遮罩关闭
		content: '/template/templatePageList.do',
		title: '模版页面列表',
		closeBtn: 1
	});
	layer.full(openTemplatePageListVar);
}

//打开网站基本信息弹出框
function openJiBenXinXi(){
	layer.open({
		type: 2,
		closeBtn: 1, //不显示关闭按钮
		anim: 3, area:['390px','260px'],
		shadeClose: true, //开启遮罩关闭
		content: '/site/popupBasicInfo.do',
		title: false,
		closeBtn: 1
	});
}

//打开问题反馈弹出框
var wentifankui_index = 0;
function openWenTiFanKui(){
	wentifankui_index = layer.open({
      type: 1
      ,title: '问题反馈'
      ,id: 'wentifankui_popup'
      ,content: '<div class="runtest layui-layer-wrap" style="display:block"><textarea id="wentifankui_textarea" style="display:block; width: 300px; height: 160px; border: 10px solid #F8F8F8; border-top-width: 0; padding: 10px; line-height:20px; overflow:auto; background-color: #eeeeee; color: #111111; font-size:14px; font-family:Courier New;" placeholder="如果您使用过程中，遇到什么问题、或者有更好的意见建议，欢迎告诉我们。您也可以关注我们微信公众号“wangmarket”，使用中遇到问题时，可随时通过公众号进行回复您的疑问，实时解答"></textarea><buttom class="layui-btn layui-btn-normal" style="position: absolute; right: 20px; bottom: 20px;" onclick="wenTiFanKuiSubmit();">提交反馈信息</buttom></div>'
      ,shade: false
      ,resize: false
    });
}

//问题反馈提交。结合 openWenTiFanKui 一块使用
function wenTiFanKuiSubmit(){
	if(document.getElementById('wentifankui_textarea').value.length == 0){
		layer.msg('要说点什么呢？', {icon: 2});
		return;
	}
	var wtfk_load_index = layer.load(3);
	$.post("/site/wenTiFanKui.do", { "text":document.getElementById('wentifankui_textarea').value },
	   function(data){
			layer.close(wtfk_load_index);
			if(data.result != '1'){
				layer.msg(data.info, {icon: 2}); 
			}else{
				layer.close(wentifankui_index);
				layer.msg('反馈成功', {icon: 1}); 
			}
	   }, "json");
}

//修改Banner轮播图，内页通用的，默认type＝1
function updateBanner(){
	layer.open({
		type: 2, 
		title:'修改Banner图', 
		area: ['380px', '290px'],
		shadeClose: true, //开启遮罩关闭
		content: '/carousel/popupCarouselUpdate.do'
	});
}

//修改首页Banner轮播图，type＝2
function updateIndexBanner(){
	layer.open({
		type: 2, 
		title:'修改首页Banner图', 
		area: ['380px', '290px'],
		shadeClose: true, //开启遮罩关闭
		content: '/carousel/popupCarouselUpdate.do?type=2'
	});
}

//更改模版，需 getSubWindowsParam()支持
function updateTemplate(){
	$.showLoading('模版更换中');
	window.location.href='/site/templateSave.do?siteid='+site['id']+'&client=pc&templateId='+getSubWindowsParam();
}

//修改关于我们的图
function updateAboutUsImage(){
	layer.open({
		type: 2, 
		title:'修改关于我们的图片', 
		area: ['380px', '410px'],
		shadeClose: true, //开启遮罩关闭
		content: '/site/popupAboutUsImageUpdate.do'
	});
}

/**
 * 修改栏目属性，简单，弹出框更该栏目，简单，只是更改栏目名字
 * siteColumnId 要修改栏目得id
 */
function updateSiteColumn(siteColumnId){
	layer.open({
		type: 2, 
		title:'修改栏目', 
		area: ['460px', '220px'],
		shadeClose: true, //开启遮罩关闭
		content: '/column/popupColumnUpdate.do?id='+siteColumnId
	});
}

//修改网站的关键词
function updateSiteKeywords(){
	$.showLoading();
	$.post("/site/getSiteData.do", function(result){
		 $.hideLoading();
		if(result.result != '1'){
			alert(result.info);
		}else{
			console.log(result);
			$.prompt({
				text: "多个用“,”分割，建议输入3个以内的关键词",
				title: "更改网站 keywords",
				onOK: function(text) {
					iw.loading("修改中");
					$.post("/site/updateKeywords.do?siteid="+site['id']+"&keywords="+text, function(data){
						iw.loadClose();
						if(data.result == '1'){
							iw.msgSuccess("修改成功");
					 	}else if(data.result == '0'){
					 		iw.msgFailure(data.info);
					 	}else{
					 		iw.msgFailure();
					 	}
					});
					
			 },
			 input: result.site.keywords
			});
		}
	});
}


//点击左上方站点的名字或LOGO，更改站点名字
function updateSiteName(){
	
	iw.loading("加载中");    //显示“操作中”的等待提示
	$.post("/site/getSiteData.do", function(data){
	    iw.loadClose();    //关闭“操作中”的等待提示
	    if(data.result == '1'){
			$.prompt({
			 text: "请输入13个字以内的网站名称",
			 title: "更改网站名字",
			 onOK: function(text) {
				 iw.loading("修改中");
				 
				 $.post("/site/updateName.do", {"name":text }, function(data){
					 	iw.loadClose();
					 	if(data.result == '1'){
					 		iw.msgSuccess("操作成功");
					 		
					 		//电脑模式
					 		if(document.getElementById('logogramName') != null){
								//如果是通用的七套模版，则改完网站名后要将当前网站的名字同步变过来
								document.getElementById('logogramName').innerHTML = text;
							}
					 		
					 		//手机模式
					 		if(document.getElementById('title') != null){
								//如果是通用的七套模版，则改完网站名后要将当前网站的名字同步变过来
								document.getElementById('title').innerHTML = text;
							}
					 		
					  	}else if(data.result == '0'){
					  		iw.msgFailure(data.info);
					  	}else{
					  		iw.msgFailure();
					  	}
				    }, 
				"json");
				 
			 },
			 input: data.site.name
			});
		
	     }else if(data.result == '0'){
	         iw.msgFailure(data.info);
	     }else{
	         iw.msgFailure();
	     }
	});
	
	
}

//修改网站首页的描述
function updateSiteDataDescription(){
	iw.loading("加载中");
	
	$.post("/site/getSiteData.do", function(data){
		iw.loadClose();
		if(data.result == '1'){
			$.prompt({
				 text: "请输入200个汉字以内的网站首页描述",
				 title: "更改网站首页描述 Description",
				 onOK: function(text) {
					 iw.loading("修改中");
					 $.post("/site/updateDescription.do", { "siteid": site['id'], "description":text }, function(data2){
					 	iw.loadClose();
						if(data2.result != '1'){
							iw.msgFailure(data2.info);
						}else{
							iw.msgSuccess("操作成功");
						}
					 }, "json");
				 },
				 input: result.siteData.indexDescription
			});
	 	}else if(data.result == '0'){
	 		iw.msgFailure(data.info);
	 	}else{
	 		iw.msgFailure();
	 	}
	});
	
}

//刷新首页
function refreshIndex(){
	$.showLoading('重新创建中');
	window.location.href='/site/refreshIndex.do';
}

//更改二级域名,需 getSubWindowsParam()支持
function updateDomain(){
	$.prompt({
	 text: ""
		 +"<div style=\"text-align:left; padding-left:30px;\">"
		 +"如：您输入了“guan”"
		 +"<br/>那么您网站的域名可以为："
		 +"<div style=\"padding-left:15px;\">http://guan."+autoAssignDomain
		 +"</div>"
		 +"<br/><b>注意：改完后，最多会等待5分钟才会生效</b>"
		 +"<br/>请输入30个以内的英文或数字："
		 +"</div>",
	 title: "更换域名",
	 onOK: function(text) {
		 if(text.length > 30){
			 $.alert("输入30个字符以内的英文或数字", "提示");
		 }else{
			 if(text != getSubWindowsParam()){
				 iw.loading("修改中");
				 $.post("/site/updateDomain.do?siteid="+site['id']+"&domain="+text, function(data){
				 	iw.loadClose();
				 	if(data.result == '1'){
				 		iw.msgSuccess("删除成功");
				  	}else if(data.result == '0'){
				  		iw.msgFailure(data.info);
				  	}else{
				  		iw.msgFailure();
				  	}
				 });
				
			 }
		 }
	 },
	 input: getSubWindowsParam()
	});
}

//更改自己绑定的域名,需 getSubWindowsParam()支持
function updateBindDomain(){
	$.prompt({
	 text: "将域名做CNAME记录解析至“domain."+autoAssignDomain+"”" +
	 		"<br/><span style=\"padding-left:50px; font-size:12px;\">帮助：<a href=\"https://help.aliyun.com/knowledge_detail/39788.html\" target=\"_black\">阿里云域名</a>&nbsp;|&nbsp;<a href=\"http://jingyan.baidu.com/article/86fae346f9f62e3c49121a90.html?st=5&net_type=&bd_page_type=1&os=1&rst=&word=wang.market\" target=\"_black\">新网域名</a></span>&nbsp;|&nbsp;<span style=\"padding-left:5px;\" onmouseover=\"bindDomainWindow_mouseOver();\" id=\"bindDomainWindow_mouseOver_id\" onmouseout=\"bindDomainWindow_mouseOut();\"><a href=\"javascript:;\">其他帮助</a></span>" +
	 		"<br/>绑定顶级域名之后，网站便会默认使用顶级域名访问。" +
	 		"<br/>如果您想解除顶级域名绑定，可<button onclick=\"removeDomainBind();\" style=\"padding:2px; cursor:pointer;\">点击此处取消绑定</button>" +
	 		"<b><br/>中文域名无法绑定、改完后要等待5分钟之后才会生效</b>",
	 title: "绑定自己的顶级域名",
	 onOK: function(text) {
		 if(text == getSubWindowsParam()){
 			//域名没有变动，无需改动
		 }else{
			 iw.loading("修改中");
			 $.post("/site/updateBindDomain.do?siteid="+site['id']+"&bindDomain="+text, function(data){
			 	iw.loadClose();
			 	if(data.result == '1'){
			 		iw.msgSuccess("操作成功");
			  	}else if(data.result == '0'){
			  		iw.msgFailure(data.info);
			  	}else{
			  		iw.msgFailure();
			  	}
			 });
		}
	 },
	 input: getSubWindowsParam()
	});
}
//绑定顶级域名窗口，CNAME解析的帮助及说明
var bindDomainWindow_mouseOver_tipindex = 0;
function bindDomainWindow_mouseOver(){
	bindDomainWindow_mouseOver_tipindex = layer.tips("" +
			"<div style=\"text-align:left;\">例如，我有一个域名 “www.guanleiming.com” 要绑定到这个网站" +
			"<br/><b>第一步：</b>将这个域名的CNAME记录指向&nbsp;“&nbsp;domain."+autoAssignDomain+"&nbsp;”&nbsp;" +
			"<div style=\"padding-left:60px;\">" +
			"	不用备案，香港服务器：domain."+autoAssignDomain +
			"	<br/>必须备案，国内服务器：beian."+autoAssignDomain+"</div>" +
			"<b>第二步：</b>在这里填入要绑定的域名 &nbsp;“&nbsp;www.guanleiming.com&nbsp;”&nbsp;" +
			"<br/><b>第三步：</b>等待5分钟左右，即可访问&nbsp;“&nbsp;www.guanleiming.com&nbsp;”&nbsp;</div>", '#bindDomainWindow_mouseOver_id', {
		tips: [1, '#0FA6A8'], //还可配置颜色
		area: ['600px','auto'],
		time:0,
		tipsMore: true
	});
}
function bindDomainWindow_mouseOut(){
	layer.close(bindDomainWindow_mouseOver_tipindex);
}


/**
 * 高级自定义模版使用，用于生成整站HTML内容
 */
function shengchengzhengzhan(){
	$.showLoading('整站生成中<br/>此过程可能时间比较长，请耐心等待');
	
	$.post("/template/refreshForTemplate.do", function(data){
		$.hideLoading();
		if(data.result == '1'){
			iw.msgSuccess("操作成功");
	 	}else if(data.result == '0'){
	 		iw.msgFailure(data.info);
	 	}else{
	 		iw.msgFailure();
	 	}
	});
	
}

/**
 * 版本更新提醒，从cookie中获取上次存储的版本信息
 * @param version 当前最新的版本号，如 var version = '2.1';
 */
function versionUpdateRemind(version){
	var oldVersion = getCookie('version');
	if(oldVersion == null || oldVersion.length == 0){
		//没有保存上个版本的版本号，那么可能是第一次使用，也可能是浏览器不允许保存Cookie。
	}else{
		try{
			version.length > 0;
		}catch(err){
			var version = '';
		}
		
		//确定获取到当前cookie的版本号了，跟当前最新的版本号进行比对
		if(version.length > 0 && oldVersion != version){
			//版本有更新了，用户这此使用时，版本有更新了，弹出这次的版本更新提示
			layer.open({
				type: 1
				,title: false //不显示标题栏
				,closeBtn: false
				,area: '590px;'
				,shade: 0.8
				,id: 'versionUpdateTip' //设定一个id，防止重复弹出
				,resize: false
				,btn: ['我知道了']
				,btnAlign: 'c'
				,moveType: 1 //拖拽模式，0或者1
				,content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300; text-align:left;">'
					+'<div style="width:100%; text-align:center; font-size:22px;"> <span style="font-size:28px; padding-right:15px;">V&nbsp;'+version+'</span>建站系统升级了！ 更新功能如下</div><br>'
					+'<style> .xnx3_gxc li{list-style-type: decimal;} </style>'
					+'<ul style="list-style-type: decimal;" class="xnx3_gxc">'
					+'<li>总管理后台，功能插件下，增加 数据字典 插件，此插件只限已授权用户，数据库为 mysql 的使用。</li>'
					+'<li>网站访问时，增加未生成整站的提示引导。当用户创建CMS模式网站后，未生成整站就访问时，不再出现通用的 404 提示页面，而是出现引导页，引导用户登陆总管理后台，点击生成整站按钮。</li>'
					+'<li>网站访问时，增加未绑定域名的提示引导。当用户已解析到服务器，但网站管理后台中未进行绑定域名时，不再出现通用的 404 提示页面，而是出现引导页，引导用户登陆总管理后台，进入系统管理-网站设置-绑定域名，进行绑定。</li>'
					+'<li>将 api 插件优化，放到 com.xnx3.wangmarket.plugin 下，并将 api接口由 /api/.. 调整为 /plugin/api/..</li>'
					+'<li>CMS模式建站，文章调用标签增加 {news.text} ，可以随时随地方便调取文章内容显示。参见 http://tag.wscso.com/2939.html</li>'
					+'<li>总管理后台，安全考虑，文章管理中，弹出窗查看文章详情时，去除文章封面图的显示。</li>'
					+'<li>修复之前升级导致的404错误页面丢失问题。</li>'
					+'<li>去除 systemConfig.xml 配置文件的 OSS.imageSuffixList 配置，允许上传后缀配置统一改为 attachmentFile.allowUploadSuffix 中，并默认增加常见后缀： png、jpg、jpeg、gif、bmp、flv、swf、mkv、avi、rm、rmvb、mpeg、mpg、ogg、ogv、mov、wmv、mp4、webm、mp3、wav、mid、rar、zip、tar、gz、7z、bz2、cab、iso、doc、docx、xls、xlsx、ppt、pptx、pdf、txt、md、xml，以让网站有更多扩展。</li>'
					+'<li>修复网站管理后台中，内容管理-添加标题图片上传时，若图过大，上传失败时，提示异常的问题，改为明确提示：请上传xx大小以内的文件。这个大小的配置，位于配置文件 application.properties 中。</li>'
					+'<li>CMS模式网站管理后台中，输入模型，增加js 的 save() 方法，当用户点击文章的 “保存” 按钮时，会先执行 自定义的 save() 方法，在执行保存文章操作。另外，save() 方法若返回false，则不会进行保存操作。</li>'
					+'<li>优化输入模型中的图片输入，当栏目类型不是图文模式时 {titlepicImage} 不会调出任何数据，改为无论是栏目类型是什么，此标签都可正常调取数据。</li>'
					+'<li>CMS模式管理后台，输入模型中，增加 {news.titlepic} 的标签调用，可调取 news 数据表中原始的 titlepic 的值</li>'
					+'<li>修复代理平台续费延期，过期时间溢出问题。</li>'
					+'<li>优化网站管理后台中，内容管理的内容编辑器（UEditor）的美观度</li>'
					+'<li>修复域名绑定时，若将泛解析的子域名作为一个独立域名进行绑定时，绑定无效的问题。</li>'
					+'<li>项目最初始运行起来后，访问时，增加欢迎页面，安装指引，不再是404页面, 优化初始安装页面</li>'
					+'<li>修复列表排序的 orderBy 函数，排序时会丢失get参数的问题。</li>'
					//+'<li>增加个人博客模版，可用于个人博客、空间等。电脑网站通用。适用于CMS模式。 预览:  <a href="http://qiye8.wscso.com" target="_black" style="text-decoration: underline; color:#999">http://qiye8.wscso.com</a></li>'
					+'</ul></div>'
					
				,success: function(layero){
				}
			});
			
		}
	}
	
	setCookie('version',version);
}


//CMS模式下，导出当前模版
function exportTemplate(){
	var dtp_confirm = layer.confirm('确定要将当前网站的模版导出吗？<br/>导出内容包括模版页面、模版变量、栏目、输入模型<br/>导出的模版可以用于快速创建新网站;也可以作为备份,还原使用', {
	  btn: ['立即导出','取消'] //按钮
	}, function(){
		layer.close(dtp_confirm);
		window.open("/template/exportTemplate.do");
	}, function(){
	});
}

//v3.0 解除绑定顶级域名
function removeDomainBind(){
	iw.loading("解绑中...");    //显示“操作中”的等待提示
	$.post("/site/updateBindDomain.do?bindDomain=", function(data){
	    iw.loadClose();    //关闭“操作中”的等待提示
	    if(data.result == '1'){
	        iw.msgSuccess('解绑成功');
	     }else if(data.result == '0'){
	         iw.msgFailure(data.info);
	     }else{
	         iw.msgFailure();
	     }
	});
}

//v3.3
//在线客服设置
function openKefuSet(){
	layer.open({
		type: 2,
		closeBtn: 1, //不显示关闭按钮
		area:['430px','470px'],
		shadeClose: false, //开启遮罩关闭
		content: '/im/set.do',
		scrollbar: false,
		title: '网站在线客服设置',
		closeBtn: 1
	});
}

//v3.4
//修改密码
function updatePassword(){
	layer.prompt({
		  formType: 0,
		  value: '',
		  title: '请输入新密码'
	}, function(value, index, elem){
		layer.close(index);
		iw.loading('更改中...');
		$.post("/site/updatePassword.do", { "newPassword": value},
			function(data){
				iw.loadClose();
				if(data.result != '1'){
					iw.msgFailure(data.info);
				}else{
					iw.msgSuccess('修改成功！新密码：'+value);
				}
			}
		, "json");
	});
	
}


try{
	//此主要用于子页面向父页面传值使用
	var op=document.createElement("p");
	var oText=document.createTextNode("这个主要用于接收自页面传过来的参数");
	op.appendChild(oText);
	op.setAttribute("style", "display:none");
	op.setAttribute("id", "subWindowsParam");
	document.body.appendChild(op);
}catch(err){}

//获取自页面传递过来的参数，不可并发，不然会覆盖
function getSubWindowsParam(){
	return document.getElementById('subWindowsParam').innerHTML;
}