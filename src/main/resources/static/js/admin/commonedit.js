/**通用编辑模式下，要加载的JS文件，这里主要管的是功能性质。需要在
 * fun.js
 * iw.js
 * 加载后**/

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

//v4.8 废弃 masterSiteUrl 参数，以更好适配 https
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
		content: '/sites/popupSiteUpdate.do'
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
//function openTemplatePageList(){
//	var openTemplatePageListVar = layer.open({
//		type: 2,
//		closeBtn: 1, //不显示关闭按钮
//		anim: 3, area:['600px','680px'],
//		shadeClose: true, //开启遮罩关闭
//		content: '/template/templatePageList.do',
//		title: '模版页面列表',
//		closeBtn: 1
//	});
//	layer.full(openTemplatePageListVar);
//}

//打开网站基本信息弹出框
function openJiBenXinXi(){
	var jibenxinxi_open = layer.open({
		type: 2,
		closeBtn: 1, //不显示关闭按钮
		anim: 3, area:['390px','260px'],
		shadeClose: true, //开启遮罩关闭
		content: '/sites/popupBasicInfo.do',
		title: false,
		closeBtn: 1
	});
	layer.style(jibenxinxi_open, {
	  overflow: 'hidden'
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
	$.post("/sites/wenTiFanKui.do", { "text":document.getElementById('wentifankui_textarea').value },
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
	window.location.href='/sites/templateSave.do?siteid='+site['id']+'&client=pc&templateId='+getSubWindowsParam();
}

//修改关于我们的图
function updateAboutUsImage(){
	layer.open({
		type: 2, 
		title:'修改关于我们的图片', 
		area: ['380px', '410px'],
		shadeClose: true, //开启遮罩关闭
		content: '/sites/popupAboutUsImageUpdate.do'
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
	$.post("/sites/getSiteData.do", function(result){
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
					$.post("/sites/updateKeywords.do?siteid="+site['id']+"&keywords="+text, function(data){
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
	$.post("/sites/getSiteData.do", function(data){
	    iw.loadClose();    //关闭“操作中”的等待提示
	    if(data.result == '1'){
			$.prompt({
			 text: "请输入13个字以内的网站名称",
			 title: "更改网站名字",
			 onOK: function(text) {
				 iw.loading("修改中");
				 
				 $.post("/sites/updateName.do", {"name":text }, function(data){
					 	iw.loadClose();
					 	if(data.result == '1'){
					 		iw.msgSuccess("操作成功");
					 		console.log(site['client']);
					 		try{
					 			if(site['client'] == '1'){
					 				//电脑模式
							 		if(document.getElementById('logogramName') != null){
										//如果是通用的七套模版，则改完网站名后要将当前网站的名字同步变过来
										document.getElementById('logogramName').innerHTML = text;
									}
					 			}
					 		}catch(err){}
					 		
					 		try{
					 			if(site['client'] == '2'){
						 			//手机模式
					 				console.log(text);
					 				document.getElementById('iframe').contentWindow.document.getElementById('title').innerHTML = text;
					 				console.log(text);
					 			}
					 		}catch(err){
					 			console.log(err)
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
	
	$.post("/sites/getSiteData.do", function(data){
		iw.loadClose();
		if(data.result == '1'){
			$.prompt({
				 text: "请输入200个汉字以内的网站首页描述",
				 title: "更改网站首页描述 Description",
				 onOK: function(text) {
					 iw.loading("修改中");
					 $.post("/sites/updateDescription.do", { "siteid": site['id'], "description":text }, function(data2){
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
	window.location.href='/sites/refreshIndex.do';
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
				 $.post("/sites/updateDomain.do?siteid="+site['id']+"&domain="+text, function(data){
				 	iw.loadClose();
				 	if(data.result == '1'){
				 		iw.msgSuccess("修改成功");
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
	layer.open({
		type: 2,
		closeBtn: 1, //不显示关闭按钮
		area:['438px','400px'],
		shadeClose: false, //开启遮罩关闭
		content: '/sites/popupBindDomain.do',
		scrollbar: false,
		title: '您需要以下这几步，来进行绑定域名',
		closeBtn: 1
	});
}


/**
 * CMS网站，生成整站
 */
function generatehtml(){
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
					+'<li>增加应用插件市场！在总管理后台中，功能插件下，第一个 插件管理，便是。可以通过插件管理进行快速安装、卸载插件。</li>'
					+'<li>开放华为云OBS云存储的配置说明，免费用户也可进行配置云存储。</li>'
					+'<li>全面更新安装引导，优化域名设定，配置等步骤详细说明，按照步骤下来即可完成安装。另增加本地使用的判断，本地安装使用时，会自动弹出本地快速安装提示，点击一键安装完成快速体验使用。</li>'
					+'<li>在线客服插件，增加10个客服模版，由原先的3个客服模版变为13个。</li>'
					+'<li>优化域名绑定体系。</li>'
					+'<li>优化针对保留二级域名更改，由原本程序写死的，变为可在系统变量中自由更改配置。</li>'
					+'<li>优化网站管理后台-可视化页面顶部的功能按钮，避免可视化页面时有的子页面覆盖导致顶部功能按钮不显示</li>'
					+'<li>全面检查模版动态标签替换，避免有用户自己输入的表情符号等导致生成整站失败</li>'
					+'<li>Eclipse 项目启动完成后，控制台出现已成功启动的提示信息，并加入简单说明引导</li>'
					+'<li>修复私有模版库添加模版时，如果某个模版信息未填写，导致选择模版时某个分类下所有模版都不显示的问题。</li>'
					+'<li>修复开通下级代理账号时信息可为空的问题。</li>'
					+'<li>修复项目开启成功后，自动缓存域名绑定时，若网站名设为空，域名信息不装载的问题。</li>'
					+'<li>优化模版页面编辑过程中，上方的几个通用标签、文章标签等，点开后标题有网市场标签说明的标示，去掉。</li>'
					+'<li>优化模版页面内容，不支持表情等特殊字符的问题。</li>'
					+'<li>优化 login.do 登陆页面，增加可以传递get参数 username 、 password ，可以将用户名密码传递过去，只需要输入验证码即可登陆，更方便体验</li>'
					+'<li>修复刚开通的网站，进入后不选模版，点击内容管理进入后，提示信息显示完毕，倒计时完跳转时，当前页面自动关闭的问题。</li>'
					+'<li>修复私有化无网环境部署时，所做网站进行可视化编辑时，可视化js模块404的问题</li>'
					+'<li>修复私有模版库添加模版时，如果某个模版信息未填写，导致选择模版时某个分类下所有模版都不显示的问题。</li>'
					+'<li>修复输入模型中，更改时，自定义字段的说明文字错误的问题</li>'
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


/** v4.4
 * 绑定域名、解绑域名
 * domain 要绑定的域名。 若为空字符串，则是解除绑定
 */
function bindDomain(domain){
	if(domain.length == 0){
		parent.iw.loading("解绑中");
	}else{
		parent.iw.loading("绑定中");
	}
	$.post("/sites/updateBindDomain.do?siteid="+site['id']+"&bindDomain="+domain, function(data){
		parent.iw.loadClose();
		if(data.result == '1'){
			if(domain.length == 0){
				parent.iw.msgSuccess("已解绑");
			}else{
				parent.iw.msgSuccess("绑定成功");
			}
	 	}else if(data.result == '0'){
	 		parent.iw.msgFailure(data.info);
	 	}else{
	 		parent.iw.msgFailure();
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
		$.post("/sites/updatePassword.do", { "newPassword": value},
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