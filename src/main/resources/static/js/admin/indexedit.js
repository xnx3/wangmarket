/* 主要为 PC模式网站 使用， CMS模式网站只是用来做鼠标跟随提醒 */

/*首页关于我们图片的点击*/
function aboutUsImgClick(){
	var auc = site['aboutUsCid'];
	if(auc > 0){
		//很早之前建立的网站，有的没有生成site['aboutUsCid']参数
		//window.location.href=masterSiteUrl+'page/updateTitlepic.do?cid='+site['aboutUsCid']+'&client=pc';
		updateAboutUsImage();
	}
}

try{
	//当前首页是否要清除缓存,判断是否get传递了此参数
	if(getCookie('pcIndexReloadCleanCache') == '1'){
		setCookie('pcIndexReloadCleanCache','0');
		$.showLoading('更新缓存中');
		location.reload(true);	//相当于按住CTRL＋F5
	}
}catch(err){}

$(function(){
	//网站设置
	var wangzhanshuxing_tipindex = 0;
	$("#wangzhanshuxing").hover(function(){
		wangzhanshuxing_tipindex = layer.tips('网站的基本设置，比如访问域名、绑定域名等等', '#wangzhanshuxing', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(wangzhanshuxing_tipindex);
	})
	//当点击时才会出现
	$("#wangzhanshuxing").click(function(){  
		var wangzhanshuxing_open = layer.open({
			type: 2,
			closeBtn: 1, //不显示关闭按钮
			anim: 3, 
			area:['515px','352px'],
			shadeClose: true, //开启遮罩关闭
			content: masterSiteUrl+'sites/popupInfo.do',
			title: false
		});
		layer.style(wangzhanshuxing_open, {
		  overflow: 'hidden'
		}); 
	})
	
	//基本信息
	var jibenxinxi_tipindex = 0;
	$("#jibenxinxi").hover(function(){
		jibenxinxi_tipindex = layer.tips('基本的信息，比如网站空间的占用情况、最后的登陆情况等', '#jibenxinxi', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(jibenxinxi_tipindex);
	})
	//当点击时才会出现
	$("#jibenxinxi").click(function(){  
		openJiBenXinXi();
	})
	
	//访问统计
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
	
	//xin访问统计
	var rzfw_fangwentongji_tipindex = 0;
	$("#rzfw_fangwentongji").hover(function(){
		rzfw_fangwentongji_tipindex = layer.tips('统计昨天、今天每个时间段的网站访问情况', '#rzfw_fangwentongji', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(rzfw_fangwentongji_tipindex);
	})
	
	//爬虫统计
	var rzfw_pachongtongji_tipindex = 0;
	$("#rzfw_pachongtongji").hover(function(){
		rzfw_pachongtongji_tipindex = layer.tips('列出你的一周内，各大搜索引擎的爬虫抓取情况', '#rzfw_pachongtongji', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(rzfw_pachongtongji_tipindex);
	})
	
	//操作日志
	var rzfw_caozuorizhi_tipindex = 0;
	$("#rzfw_caozuorizhi").hover(function(){
		rzfw_caozuorizhi_tipindex = layer.tips('将网站最近一个月的后台操作日志列出', '#rzfw_caozuorizhi', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(rzfw_caozuorizhi_tipindex);
	})
	
	
	//积分兑换
	var jifenduihuan_tipindex = 0;
	$("#jifenduihuan").hover(function(){
		jifenduihuan_tipindex = layer.tips('邀请注册获得积分、使用积分兑换域名、空间、苹果手机等，更多兑换品正在筹备上架', '#jifenduihuan', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(jifenduihuan_tipindex);
	})
	//当点击时才会出现
	$("#jifenduihuan").click(function(){  
		openMoneyIndex();
	})
	
	
	//更换模版
	var changetemplate_tipindex = 0;
	$("#changetemplate").hover(function(){
		changetemplate_tipindex = layer.tips('更换一个新的模版，让您的网站大变样。更换完成后若不理想，则建议您刷新一下页面再看', '#changetemplate', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(changetemplate_tipindex);
	})
	//当点击时才会出现
	$("#changetemplate").click(function(){  
		layer.open({
			type: 2,
			closeBtn: 0, //不显示关闭按钮
			anim: 3, area:['600px','675px'],
			shadeClose: true, //开启遮罩关闭
			content: masterSiteUrl+'sites/templateList.do?siteid='+site['id']+'&client=pc',
			title: '更换模版',
			closeBtn: 1
		});
	})
	
	//联系信息，底部信息
	var dibuxinxi_tipindex = 0;
	$("#dibuxinxi").hover(function(){
		dibuxinxi_tipindex = layer.tips('底部相关联系信息的修改', '#dibuxinxi', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(dibuxinxi_tipindex);
	})
	//当点击时才会出现
	$("#dibuxinxi").click(function(){  
		updateFooterSiteInfo();
	})
	
	
	//SEO优化
	var seoyouhua_tipindex = 0;
	$("#seoyouhua").hover(function(){
		seoyouhua_tipindex = layer.tips('SEO优化排名相关设置', '#seoyouhua', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(seoyouhua_tipindex);
	})
	//当点击时才会出现
	$("#seoyouhua").click(function(){  
		layer.open({
			type: 2,
			closeBtn: 0, //不显示关闭按钮
			anim: 3, area:['600px','442px'],
			shadeClose: true, //开启遮罩关闭
			content: masterSiteUrl+'sitePc/seo.do',
			title: false,
			closeBtn: 1
		});
	})
	
	//查看网站，预览网站
	var chakanwangzhan_tipindex = 0;
	$("#chakanwangzhan").hover(function(){
		chakanwangzhan_tipindex = layer.tips('实时预览查看网站', '#chakanwangzhan', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(chakanwangzhan_tipindex);
	})
	
	//问题反馈
	var wentifankui_tipindex = 0;
	$("#wentifankui").hover(function(){
		wentifankui_tipindex = layer.tips('使用时，遇到什么问题了？或者有什么意见建议了，都可以反馈给我们。', '#wentifankui', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(wentifankui_tipindex);
	})
	
	//网站高级设置
	var gaojishezhi_tipindex = 0;
	$("#gaojishezhi").hover(function(){
		gaojishezhi_tipindex = layer.tips('网站高级设置，建议懂点技术相关的人士使用，有利于网站的扩展。若对网站、html等技术不太了解，可以不用管此处', '#gaojishezhi', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(gaojishezhi_tipindex);
	})
	//当点击时才会出现
	$("#gaojishezhi").click(function(){  
		layer.open({
			type: 2,
			closeBtn: 0, //不显示关闭按钮
			anim: 3, area:['600px','695px'],
			shadeClose: true, //开启遮罩关闭
			content: masterSiteUrl+'sitePc/popupGaoJiSet.do',
			title: false,
			closeBtn: 1
		});
	})
	
	//退出登陆
	var tuichudenglu_tipindex = 0;
	$("#tuichudenglu").hover(function(){
		tuichudenglu_tipindex = layer.tips('退出，注销当前登陆的用户', '#tuichudenglu', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(tuichudenglu_tipindex);
	})
	
	// IM 聊天，客服设置
	var im_tipindex = 0;
	$("#im_menu").hover(function(){
		im_tipindex = layer.tips('<b>网站在线客服</b><br/>您可以选择启用在线客服功能。启用后，您还可以实时看到当前网站中有谁在访问，并且可以有您主动发起跟访客的聊天，比如，主动询问一下某个访客，有什么可以帮助您的。<br/>只要停留在此页面，便可以接收到网站上访客发起的咨询。您可以自己打开网站，找到网站底部的在线客服，说说话，试试效果', '#im_menu', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(im_tipindex);
	})
	

	//客服管理-历史咨询
	var im_hostory_tipindex = 0;
	$("#im_hostory").hover(function(){
		im_hostory_tipindex = layer.tips('历史咨询对话查看<br/>比如，您有好几天未上线，那么可以通过这里看看，这几天是否有人咨询过什么问题；<br/>又或者查看之前跟访客的某条聊天记录等', '#im_hostory', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(im_hostory_tipindex);
	})
	
	//修改密码
	var xiugaimima_tipindex = 0;
	$("#xiugaimima").hover(function(){
		xiugaimima_tipindex = layer.tips('修改您当前账户登录所的密码。', '#xiugaimima', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true
		});
	},function(){
		layer.close(xiugaimima_tipindex);
	})
	
})



/* v3.0更新增加，主要用于电脑模式网站，可视化编辑首页的鼠标跟随提示 */

//同 Site 类，1:pc  2:wap  3:cms， 若都不是，默认为0
var siteClient = (site != null && site['client'] != null)? site['client']:0;
if(siteClient == 1){
	//PC模式网站
	dynamicLoading.css(resBasePath+'layui2/css/layui.css');
	loadJs(resBasePath+'layui2/layui.all.js', function(){
		
		//做底部、二维码、banner、关于我们，进行鼠标实时跟随的提示
		//底部
		//联系人，电话等信息
		var footerSiteInfoString = '<div style="font-size:14px;">点击此处可修改联系人、电话、地址等信息</div>';
		var footerSiteInfo_tipindex = 0;
		$("#footerSiteInfo").hover(function(){
			footerSiteInfo_tipindex = layer.tips(footerSiteInfoString, '#footerSiteInfo', {
				tips: [2, '#0FA6A8'], //还可配置颜色
				time:0,
				tipsMore: true
			});
		},function(){
			layer.close(footerSiteInfo_tipindex);
		})
		//底部的电话，使用的跟上面的一样即可
		var footerPhoneAndShare_phone_tipindex = 0;
		$("#footerPhoneAndShare_phone").hover(function(){
			footerPhoneAndShare_phone_tipindex = layer.tips(footerSiteInfoString, '#footerPhoneAndShare_phone', {
				tips: [2, '#0FA6A8'], //还可配置颜色
				time:0,
				tipsMore: true
			});
		},function(){
			layer.close(footerPhoneAndShare_phone_tipindex);
		})
		
		//关于我们的图片
		var aboutUsImg_tipindex = 0;
		$(".aboutUsImg").hover(function(){
			aboutUsImg_tipindex = layer.tips('<div style="font-size:14px;">点击修改此图</div>', '.aboutUsImg', {
				tips: [1, '#0FA6A8'], //还可配置颜色
				time:0,
				tipsMore: true
			});
		},function(){
			layer.close(aboutUsImg_tipindex);
		})
		
		//修改二维码图片
		var qrImage_tipindex = 0;
		$("#qrImage").hover(function(){
			qrImage_tipindex = layer.tips('<div style="font-size:14px;">点击修改微信二维码。<br/>建议上传正方形的二维码图片</div>', '#qrImage', {
				tips: [1, '#0FA6A8'], //还可配置颜色
				time:0,
				tipsMore: true
			});
		},function(){
			layer.close(qrImage_tipindex);
		})
		
		//修改标题
		var logogramName_tipindex = 0;
		$("#logogramName").hover(function(){
			logogramName_tipindex = layer.tips('<div style="font-size:14px;">点击修改网站名字</div>', '#logogramName', {
				tips: [1, '#0FA6A8'], //还可配置颜色
				time:0,
				tipsMore: true
			});
		},function(){
			layer.close(logogramName_tipindex);
		})
		
		//banner图
		var banner_tipindex = 0;
		$("#banner").hover(function(){
			banner_tipindex = layer.tips('<div style="font-size:14px;">点击修改Banner图片</div>', '#banner', {
				tips: [1, '#0FA6A8'], //还可配置颜色
				time:0,
				tipsMore: true
			});
		},function(){
			layer.close(banner_tipindex);
		})
		
		
		/** 绑定点击事件 **/
		//当点击二维码时
		//当点击底部的联系我们修改信息时
		$("#footerSiteInfo").click(function(){  
			updateFooterSiteInfo();
		})
		$("#footerPhoneAndShare_phone").click(function(){  
			updateFooterSiteInfo();
		})
		//网站名字
		$("#logogramName").click(function(){  
			siteNameClick();
		})
		
		
		//初始化，绑定点击修改Banner图片的id. 需在 dynamicLoadJquery() layui加载后执行绑定
		layui.use('upload', function(){
			var upload = layui.upload;
			
			//模版3、5、6的Banner图
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
		            	parent.layer.msg('修改成功');
		            	//重新刷新，不缓存
		            	location.reload(true);
		            }
			    }
			    ,error: function(){
			    	//请求异常回调
			    	$.hideLoading();
			    	parent.layer.msg('上传出错！');
			    }
			});
			
			//底部微信二维码
			upload.render({
				elem: '#qrImage' //绑定元素
			    ,url: masterSiteUrl+'sites/popupQrImageUpdateSubmit.do' //上传接口
			    ,exts: 'jpg|jpeg|gif|png|bmp'
			    ,field: 'qrImageFile'
			    ,before: function(res){
			    	//上传前执行
			    	parent.$.showLoading('上传中...');
			    }
			    ,done: function(data){
			    	//上传完毕回调
			    	parent.$.hideLoading();
			    	
		            if(data.result=='0'){
		            	parent.layer.msg(data.info);
		            }else{
		            	//上传成功
		            	parent.layer.msg('修改成功');
		            	//重新刷新，不缓存
		            	location.reload(true);
		            }
			    }
			    ,error: function(){
			    	//请求异常回调
			    	$.hideLoading();
			    	parent.layer.msg('上传出错！');
			    }
			});
			
			//关于我们
			upload.render({
				elem: '.aboutUsImg' //绑定元素
			    ,url: masterSiteUrl+'sites/popupAboutUsImageUpdateSubmit.do' //上传接口
			    ,exts: 'jpg|jpeg|gif|png|bmp'
			    ,field: 'titlePicFile'
			    ,before: function(res){
			    	//上传前执行
			    	parent.$.showLoading('上传中...');
			    }
			    ,done: function(data){
			    	//上传完毕回调
			    	parent.$.hideLoading();
			    	
		            if(data.result=='0'){
		            	parent.layer.msg(data.info);
		            }else{
		            	//上传成功
		            	parent.layer.msg('修改成功');
		            	//重新刷新，不缓存
		            	location.reload(true);
		            }
			    }
			    ,error: function(){
			    	//请求异常回调
			    	$.hideLoading();
			    	parent.layer.msg('上传出错！');
			    }
			});
			
			//如果使用的是第七套模版，那么这套模版是有不同的，要单独设置
			if(site['templateId'] != null && site['templateId'] == 7){
				//banner图是整屏幕，图像只是一个背景，没有banner的id，那么，作用的id会有变动
				upload.render({
					elem: '#index_first_page' //绑定元素
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
			            	parent.layer.msg('修改成功');
			            	//重新刷新，不缓存
			            	location.reload(true);
			            }
				    }
				    ,error: function(){
				    	//请求异常回调
				    	$.hideLoading();
				    	parent.layer.msg('上传出错！');
				    }
				});
			}
			
			
		});
		
	});
}