/* 手机模式网站，手机网站后台、前台都会使用。根据edit来判断是否是后台模式
	v3.0 版本增加此文件
 */

//初始化底部相关显示信息
function initFooter(){
	/* 底部固定导航 */
	var footerContent = '';
	var footerNum = 0;
	
	/* 主页 */
	if(edit){
		//编辑模式下
		footerContent = footerContent + '<a href="'+masterSiteUrl+'siteWap/editIndex.do"><img src='+resPath+'"/image/loading.svg" id="img_home" /><div>首页</div></a>';
	}else{
		footerContent = footerContent + '<a href="index.html"><img src="'+resPath+'/image/loading.svg" id="img_home" /><div>首页</div></a>';
	}
	
	footerNum++;
	
	/* 打电话发短信 */
	if(site['phone'].length>0){
		if(edit){
			//编辑模式
			footerContent = footerContent + '<a href="javascript:parent.updateFooterSiteInfo();"><img src="'+resPath+'/image/loading.svg" id="img_phone" /><div>打电话</div></a>';
			footerNum++;
			footerContent = footerContent + '<a href="javascript:parent.updateFooterSiteInfo();"><img src="'+resPath+'/image/loading.svg" id="img_message" /><div>发短信</div></a>';
			footerNum++;
		}else{
			footerContent = footerContent + '<a href="tel:'+site['phone']+'"><img src="'+resPath+'/image/loading.svg" id="img_phone" /><div>打电话</div></a>';
			footerNum++;
			footerContent = footerContent + '<a href="sms:'+site['phone']+'"><img src="'+resPath+'/image/loading.svg" id="img_message" /><div>发短信</div></a>';
			footerNum++;
		}
	}
	/* 在线QQ */
	if(site['qq'].length>0){
		if(edit){
			//编辑模式
			footerContent = footerContent + '<a href="javascript:parent.updateFooterSiteInfo();"><img src="'+resPath+'/image/loading.svg" id="img_qq" /><div>QQ咨询</div></a>';
		}else{
			footerContent = footerContent + '<a href="http://wpa.qq.com/msgrd?v=3&uin='+site['qq']+'&site='+site['name']+'&menu=yes"><img src="'+resPath+'/image/loading.svg" id="img_qq" /><div>QQ咨询</div></a>';
		}
		footerNum++;
	}
	document.getElementById('footer').innerHTML = footerContent;
	
	/* 动态设置每个区块的宽度 */
	var footColumnArray = new Array();
	footColumnArray = HTML.query("#footer").a;
	for(var fc=0;fc<footColumnArray.length;fc++){
		footColumnArray[fc].style.width=(100/footerNum)+'%';
	}
	
	if(document.getElementById("img_home") != null){
		document.getElementById("img_home").src=resPath+'template/'+site['templateId']+'/image/home.png';
	}
	if(document.getElementById("img_phone") != null){
		document.getElementById("img_phone").src=resPath+'template/'+site['templateId']+'/image/phone.png';
	}
	if(document.getElementById("img_message") != null){
		document.getElementById("img_message").src=resPath+'template/'+site['templateId']+'/image/message.png';
	}
	if(document.getElementById("img_qq") != null){
		document.getElementById("img_qq").src=resPath+'template/'+site['templateId']+'/image/qq.png';
	}
}
initFooter();


/* 保存栏目导航的排序 */
function saveRank() {
	var column = new Array();
	column = HTML.query("#columnList").div;
	/* 比较获取到的导航栏目数量与初始的数量是否相同，若相同，才可以进入下一步保存 */
	if (siteColumn.length+1 == column.length) {
		var rankString = '';
		for (var i = 0; i < column.length; i++) {
			/* div的ID有add字符的，是增加栏目的：addSiteColumn */
			if(column[i].id.indexOf('add') == -1){	
				if (rankString.length == 0) {
					rankString = column[i].id;
				} else {
					rankString = rankString + ',' + column[i].id;
				}
			}
		}

		$.post(masterSiteUrl+"column/saveRank.do", {
			siteid : site['id'],
			rankString : rankString
		}, function(data, status) {
			if (status != 'success') {
				alert("Data: " + data.info + "\nStatus: " + status);
				console.log(data);
			}
		});
	} else {
		alert('排序后数据对比出错！需进行排序重置！');
		window.location.href=masterSiteUrl+'column/resetRank.do?siteid='+site['id'];
	}
}	

/* 点击顶部标题 */		
function site_edit(){
	parent.updateSiteName();
}

/*
 * 修改文章内容  
 * type 栏目板块的type 类型，如独立页面，超链接、新闻列表等
 * id 栏目id，column.id
 */
function updateNewsContent(type, id){
	if(type == 5){
		//超链接，要修改那么就要到修改栏目
		//parent.loadIframeByUrl(masterSiteUrl+'column/column.do?id='+id);
		updateColumn(id);
	}else if (type == 3) {
		//独立页面，修改内容，那么只有一个页面，直接进入这个页面内容的编辑
		parent.loadIframeByUrl(masterSiteUrl+'news/updateNewsByCid.do?cid='+id);
	}else{
		//新闻、图文列表，可以直接进入信息内容列表即可
		parent.loadIframeByUrl(masterSiteUrl+'news/listForTemplate.do?cid='+id);
	}
}


//修改栏目，若是id＝0，则是添加栏目
function updateColumn(id){
	//需依靠index.do引入的layer
	parent.layer.open({
		type: 2,
		closeBtn: 0, //不显示关闭按钮
		area:['440px','430px'],
		shadeClose: true, //开启遮罩关闭
		content: masterSiteUrl+'column/column.do?id='+id,
		title: '修改栏目',
		closeBtn: 1
	});
}

/*
 * 加载JS文件，加载完后执行指定的函数
 * jsUrl 要加载的JS文件的URL地址，绝对路径
 * exec 加载完JS后要执行的方法，传入 function(){}
 */
function loadJs(jsUrl, exec){
	var _doc=document.getElementsByTagName('head')[0];  
	var script=document.createElement('script');  
	script.setAttribute('type','text/javascript');  
	script.setAttribute('src',jsUrl);  
	_doc.appendChild(script);  
	script.onload=script.onreadystatechange=function(){  
		if(!this.readyState||this.readyState=='loaded'||this.readyState=='complete'){  
			exec();
		}
		script.onload=script.onreadystatechange=null;  
	}
}

//初始化，绑定点击修改Banner图片的id. 需在 dynamicLoadJquery() layui加载后执行绑定
function initBindUpdateBanner(){
	layui.use('upload', function(){
		var upload = layui.upload;
		upload.render({
			elem: '#banner' //绑定元素
		    ,url: masterSiteUrl+'carousel/updateSubmitForAloneCarousel.do' //上传接口
		    ,exts: 'jpg|jpeg|gif|png|bmp'
		    ,field: 'imageFile'
		    ,before: function(res){
		    	//上传前执行
		    	parent.$.showLoading('上传中...');
		    }
		    ,done: function(data){
		    	//上传完毕回调
		    	parent.$.hideLoading();
		    	
	            if(data.result=='0'){
	            	alert(data.info);
	            }else{
	            	//上传成功
	            	layer.msg('修改成功');
	            	//重新刷新，不缓存
	            	location.reload(true);
	            }
		    }
		    ,error: function(){
		    	//请求异常回调
		    	$.hideLoading();
		    	alert('上传出错！');
		    }
		});
	});
}

//加载JQuery，当JQuery加载完成后，为可视化界面增加鼠标跟随提示
function dynamicLoadJquery(){
	dynamicLoading.css(parent.resBasePath+"layer/skin/default/layer.css");
	dynamicLoading.css(parent.resBasePath+'layui2/css/layui.css');
	loadJs(parent.resBasePath+'js/jquery-2.1.4.js', function(){
		loadJs(parent.resBasePath+'layui2/layui.all.js', function(){
			var layer = layui.layer;
			//增加鼠标移动的事件绑定
			addMouseListener();
			
			//修改banner图的js事件绑定
			initBindUpdateBanner();
		});
		
	});
	
}

//此必须在 dynamicLoadJquery() 内才能调用。必须先加载Jquery
function addMouseListener(){
	
	//Banner增加鼠标跟随提示
	$(function(){
		var banner_tipindex = 0;
		$("#banner").hover(function(){
			banner_tipindex = layer.tips('点击图片可进行修改', '#banner', {
				tips: [3, '#0FA6A8'], //还可配置颜色
				time:0,
				tipsMore: true
			});
		},function(){
			layer.close(banner_tipindex);
		})
		
		var title_tipindex = 0;
		$("#title").hover(function(){
			title_tipindex = layer.tips('点击可进行修改标题及相关信息', '#title', {
				tips: [3, '#0FA6A8'], //还可配置颜色
				time:0,
				tipsMore: true
			});
		},function(){
			layer.close(title_tipindex);
		})
		
		var foot_index_tipindex = 0;
		$("#foot_index").hover(function(){
			foot_index_tipindex = layer.tips('点击回到首页的可视化编辑状态', '#foot_index', {
				tips: [3, '#0FA6A8'], //还可配置颜色
				time:0,
				tipsMore: true
			});
		},function(){
			layer.close(foot_index_tipindex);
		})
		
		
	})
}

if(typeof(edit) != 'undefined' && edit){
	//如果是编辑模式，那么加载JQuery、Layui，以及绑定元素等
	dynamicLoadJquery();
}

