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
//	dynamicLoading.js(resBasePath+"js/jquery-weui.js");
//	dynamicLoading.css(resBasePath+"css/weui.min.css");
//	dynamicLoading.css(resBasePath+"css/jquery-weui.css");

	/*拖拽控件*/
//	dynamicLoading.js(resBasePath+"js/HTML.min.js");
//	dynamicLoading.js(resBasePath+"js/Sortable.js");
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
	msg.loading('更换中');
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
	msg.failure("已去除这个接口");
//	msg.loading('设置中');
//	$.post("/sites/getSiteData.do", function(result){
//		 msg.close();
//		if(result.result != '1'){
//			alert(result.info);
//		}else{
//			console.log(result);
//			$.prompt({
//				text: "多个用“,”分割，建议输入3个以内的关键词",
//				title: "更改网站 keywords",
//				onOK: function(text) {
//					iw.loading("修改中");
//					$.post("/sites/updateKeywords.do?siteid="+site['id']+"&keywords="+text, function(data){
//						iw.loadClose();
//						if(data.result == '1'){
//							iw.msgSuccess("修改成功");
//					 	}else if(data.result == '0'){
//					 		iw.msgFailure(data.info);
//					 	}else{
//					 		iw.msgFailure();
//					 	}
//					});
//					
//			 },
//			 input: result.site.keywords
//			});
//		}
//	});
}


//点击左上方站点的名字或LOGO，更改站点名字
function updateSiteName(){
	msg.loading('加载中');
	$.post("/sites/getSiteData.do", function(data){
	    msg.close();    //关闭“操作中”的等待提示
	    if(data.result == '1'){
	    	layer.prompt({
		  		  formType: 0,
		  		  value: data.site.name,
		  		  title: '请输入13个字以内的网站名称'
		  	}, function(value, index, elem){
		  		layer.close(index);
		  		if(value != getSubWindowsParam()){
		  			msg.loading('更改中...');
		  			$.post("/sites/updateName.do", { "name": value},
		  				function(data){
		  					msg.close();
		  					if(data.result != '1'){
		  						msg.failure(data.info);
		  					}else{
		  						msg.success('修改成功');
		  						
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
		  					}
		  				}
		  			, "json");
		  		}
		  	});
		  	
	     }else if(data.result == '0'){
	         msg.failure(data.info);
	     }else{
	         msg.failure('失败');
	     }
	});
	
}

//修改网站首页的描述
function updateSiteDataDescription(){
	msg.loading("加载中");
	$.post("/sites/getSiteData.do", function(data){
		msg.close();
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
	msg.loading('重新创建中');
	window.location.href='/sites/refreshIndex.do';
}

//更改二级域名,需 getSubWindowsParam()支持
function updateDomain(){
	layer.prompt({
		  formType: 0,
		  value: getSubWindowsParam(),
		  title: '请输入新域名，不含&nbsp;'+autoAssignDomain
	}, function(value, index, elem){
		layer.close(index);
		if(value.length > 30){
			msg.failure("请输入30个字符以内的英文或数字");
			return;
		}
		if(value != getSubWindowsParam()){
			msg.loading('更改中...');
			$.post("/sites/updateDomain.do?siteid="+site['id'], { "domain": value},
				function(data){
					msg.close();
					if(data.result != '1'){
						msg.failure(data.info);
					}else{
						msg.success('修改成功');
					}
				}
			, "json");
		}
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
	msg.loading('整站生成中');
	$.post("/template/refreshForTemplate.do", function(data){
		msg.close();
		if(data.result == '1'){
			msg.success("操作成功");
	 	}else if(data.result == '0'){
	 		msg.failure(data.info);
	 	}else{
	 		msg.failure('操作失败');
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
					+'<li>模板变量大升级，增加文本、数字、图片上传、下拉选择等自定义输入方式，可以给完全不懂代码的客户定制人性化自由编辑信息的方式。</li>'
					+'<li>优化全局变量支持{templatePath}等标签</li>'
					+'<li>application.properties增加短信的 sms.uid、sms.password 配置</li>'
					+'<li>网站访问，增加htmlFile传入</li>'
					+'<li>优化本地模板库的模板加载线程</li>'
					+'<li>修复用户注册因特殊符号导致无法开通网站问题</li>'
					+'<li>修复华为云自动创建插件中，obs自动创建错误的问题</li>'
					+'<li>优化插件扫描的生命周期</li>'
					+'<li>加入 xnx3-cache.jar</li>'
					+'<li>将 layui 实现的弹出提示改为 msg.js 实现</li>'
					
					
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
	var dtp_confirm = layer.confirm('确定要将当前网站的模版导出吗？<br/>导出内容包括全局变量、模版页面、模版变量、栏目、输入模型<br/>导出的模版可以用于快速创建新网站;也可以作为备份,还原使用', {
	  btn: ['立即导出','取消'] //按钮
	}, function(){
		layer.close(dtp_confirm);
		window.open("/template/exportTemplate.do");
	}, function(){
	});
}


/** v4.4
 * 绑定域名、解绑域名
 * oldDomain 之前使用的域名
 * newDomain 要绑定的域名。 若为空字符串，则是解除绑定
 * 这个已废弃，转移到site/popup_bindDomain.jsp 中
 */
function bindDomain(oldDomain,newDomain){
	if(oldDomain == newDomain){
		if(oldDomain == ''){
			//还未绑定域名时，点击的取消绑定
			parent.msg.info('尚未绑定，无需解绑。');
		}else{
			parent.msg.success('您已绑定了');
		}
		return;
	}
	if(newDomain.length == 0){
		parent.msg.loading("解绑中");
	}else{
		parent.msg.loading("绑定中");
	}
	$.post("/sites/updateBindDomain.do?bindDomain="+newDomain, function(data){
		parent.msg.close();
		if(data.result == '1'){
			if(newDomain.length == 0){
				parent.msg.success("已解绑");
			}else{
				parent.msg.success("绑定成功");
			}
	 	}else if(data.result == '0'){
	 		parent.msg.failure(data.info);
	 	}else{
	 		parent.msg.failure('操作失败');
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
		msg.loading('更改中...');
		$.post("/sites/updatePassword.do", { "newPassword": value},
			function(data){
				msg.close();
				if(data.result != '1'){
					msg.failure(data.info);
				}else{
					msg.success('修改成功！新密码：'+value);
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