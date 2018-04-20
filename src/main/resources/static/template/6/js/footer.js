/*设置底部的网站相关信息*/
document.getElementById('footerCopyRight').innerHTML = 'Copyright（c）2016 版权所有：'+site['companyName']+' All Rights Reserved';
document.getElementById('footerContactsCompanyName').innerHTML = site['companyName'];	
document.getElementById('footerContactsUsername').innerHTML = site['username'];	
document.getElementById('footerContactsPhone').innerHTML = site['phone'];
document.getElementById('footerContactsQQ').innerHTML = site['qq'];
document.getElementById('footerDomainName').innerHTML = 'http://'+getSiteUrl();
document.getElementById('footerContactsAddress').innerHTML = site['address'];

//修改站点底部联系信息
function updateFooterSiteInfo(){}

/* 刚打开初始化栏目、导航的缓存数据 */
function initFooterSiteColumnData() {
	/* 首先先判断排序跟栏目导航数据个数是否一样 */
	if (siteColumnRank.langth != siteColumn.length) {
		var content = '<a href="index.html">首页</a>';
		
		/* 先遍历排序数据，根据排序拿到栏目导航数据 */
		for (var i = 0; i < siteColumnRank.length; i++) {
			var rank = siteColumnRank[i];
			for (var j = 0; j < siteColumn.length; j++) {
				if (siteColumn[j]['id'] == siteColumnRank[i]) {
					var url = siteColumn[j]['url'];
					var type = siteColumn[j]['type'];
					switch (type) {
					case '1':
						/* 新闻 */
						url = '<a href="lc'+siteColumn[j]['id']+'_1.html">' + siteColumn[j]['name'] +"</a>";
						break;
					case '2':
						/* 图文 */
						url = '<a href="lc'+siteColumn[j]['id']+'_1.html">' + siteColumn[j]['name'] +"</a>";
						break;
					case '3':
						/* 独立页面 */
						url = '<a href="c'+siteColumn[j]['id']+'.html">' + siteColumn[j]['name'] +"</a>";
						break;
					case '4':
						/* 留言板 */
						break;
					case '5':
						/* 超链接 */
						break;
					}
					content = content + url;
					break;
				}
			}
		}

		document.getElementById('footerColumnList').innerHTML = content;
	} else {
		alert(siteColumnRank.langth + "," + siteColumn.length);
		alert('栏目导航排序规则跟数据不一致！');
	}
}
initFooterSiteColumnData();

try{
	/*百度分享：尾部、划词、图片获取焦点*/
	document.getElementById('bdsharebuttonbox').innerHTML = '<div class="bdsharebuttonbox"><a href="#" class="bds_more" data-cmd="more"></a><a href="#" class="bds_sqq" data-cmd="sqq" title="分享到QQ好友"></a><a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"></a><a href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a><a href="#" class="bds_tqq" data-cmd="tqq" title="分享到腾讯微博"></a><a href="#" class="bds_renren" data-cmd="renren" title="分享到人人网"></a><a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a><a href="#" class="bds_mshare" data-cmd="mshare" title="分享到一键分享"></a></div><script>';
	window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"1","bdSize":"24"},"share":{},"image":{"viewList":["sqq","qzone","tsina","tqq","renren","weixin","mshare"],"viewText":"分享到：","viewSize":"16"},"selectShare":{"bdContainerClass":null,"bdSelectMiniList":["sqq","qzone","tsina","tqq","renren","weixin","mshare"]}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];
	window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"1","bdSize":"24"},"share":{}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];
}catch(err){}

/*2016.10.9升级*/
function qrImageOnError(){
	setTimeout("document.getElementById('qrImage').src=attachmentFileUrl+'image/weixin_gzh.png'; ",2000);//延时2秒 ，避免找不到而一直加载
}
try{
	document.getElementById('qrImg').innerHTML='<img id="qrImage" src="'+resBasePath+'image/loading.svg" style="height: 150px;" alt="二维码" onerror="qrImageOnError();"><div id="footerQrImage">微&nbsp;信&nbsp;扫&nbsp;一&nbsp;扫</div>';
	document.getElementById('fenxiangdao').innerHTML='分享到：';
}catch(err){}
/*动态加载底部又下角的二维码*/
document.getElementById('qrImage').src=attachmentFileUrl+'site/'+site['id']+'/images/qr.jpg?v='+time;
//修改底部，为二维码的文字赋予值 “微信扫一扫”，适应之前版本。2017年，将之删除
document.getElementById("footerQrImage").innerHTML='微&nbsp;信&nbsp;扫&nbsp;一&nbsp;扫';

try{
	//填充右下角的联系电话
	document.getElementById('footerPhone').innerHTML='<img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACYAAAAmCAYAAACoPemuAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCRkRCMTFBQzREOEIxMUU2QkJGOUU3NTI0QzdBMzNDRiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCRkRCMTFBRDREOEIxMUU2QkJGOUU3NTI0QzdBMzNDRiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkJGREIxMUFBNEQ4QjExRTZCQkY5RTc1MjRDN0EzM0NGIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkJGREIxMUFCNEQ4QjExRTZCQkY5RTc1MjRDN0EzM0NGIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+EmfoowAAA6NJREFUeNq0mFtIFVEUho+KZplpVyvD0iLFRIXIzErzoQyzFEuD6sEyKsiKEgJB0qiX8KGEEuyloIgKoZv40MWgQNFudDEyMoqwDMsuoGUqp3/HGljs9szZc2Zc8OFZe9Za87vPzD5r7wCv1+vxw5aAZLAApIP5IIKuDYA3oB08By9As+07CGE2KAWfvP9bL3hJfFRcHwSVYKzuvXQF7ZZuWAc2gxQQqogPBvGgGNSAnyz3iEmOLWHh4DErWq1T1ISdYIjNcKy/wvLBNyp0CsywISKOZjNMGh8Dqtg/uteusGUsOV9TTACoB+9Ybg+YrRCYwWJKdYXlUoJ4LlI1RYmH+qlXbQWgAzTQs2fkzKFx5czJNxgPhik43sZX12wiqluanZNSXgR77lL4tUBp9XgAgkAO6NRccQ6BbJNrdyQ/S/J/gET6LNa9MNU6VkbK623MVLrX2vJAIfOvmtTZT9drVF/lL7oYbENYtw9hIuYa87db1OqhmEgubBcNHrAh6oQPUVvAYub3Kd5OznKKu8KFjYDfNkRlmIgZAMdBJsX1SkJ91e2k2GjhZJFzzIawRwpR30EMiwkCCWADoVO3mmptFc4+ctZpJq8wma31fv5UcRYay4pwzpMTo5EYAj4oRN1wQZTBH/DFeBuGNZPWmsxWnIvC2kRBscBGgQ7NxbRMMfYQvPW4Z/+0GCt/t0bCXLBKMV7ucde+cmFDGglVirE2cM9lYV4ubLKP4FCwUTF+weO+hRvCRmhTYWVJIEQx/mwUhMUawlpBJJhgEbzGZLxrFISlickKZK3JIovgYpPxBJdFxYCJoFEIe0WDqSbBs1jPJNtFMM5FYYVGHyeE3SRnj8XUmtkgiAYlLgnLo7+txmp7mlbwIsVKvMOitVkJVtPnWocrfhTVec3bnmC2Y5YTEqktUnUTYbT79lLv7kRYI9XJlDvYSrpwWJF0VCFMbNcOMr/JgSijSbxltktqoYBUabvfQo2kYYnUa3FLdiCsi2okWe0r31PQdPLrWFu0iXqmCklUiQNR7aq9pdnmdRuYRn6/4tm7T8X6Sbi/omrN+jmd5GFq3uTxqWCKA1GtJOq2v8dQZ6hAoUuNYC57k687OR8LZ9v4AgeCosFZ9lyWu3Fwl8YKXqaNxxiNvJl0LnZJ2h9k+8oNsHEGK9qRc2ApG2uhM9bPoA8Eg0n0Yyx+yuaxWHEuWwEadG5mR5hhOXQ4UiTdWGV3QRN4ojhgsbS/AgwA36FSkn+yAlsAAAAASUVORK5CYII="><span id="footerPhoneAndShare_phone">'+site['phone']+'</span>';
}catch(err){}	

/*兼容以前模版中，qrImage直接写在模版里的height*/
document.getElementById("qrImage").style.height="160px";

/*2016.10.21升级，将fun.js头部加载得这些辅助JS仍到尾部进行加载，提高页面打开速度*/
var scriptArray = document.getElementsByTagName("script");
var haveAssistJs = false;//是否有了辅助文件得加载，适应老版本fun.js以及缓存
try{
	for(i=0;i<scriptArray.length;i++){
		if(document.getElementsByTagName("script")[i].src.indexOf("hm.baidu.com") > -1){
			haveAssistJs = true;
			break;
		}
	}
}catch(err){}	
console.log("haveAssistJs:"+haveAssistJs);
if(!haveAssistJs){
	/*Baidu tongji*/
	var _hmt = _hmt || [];
	(function() {
	  var hm = document.createElement("script");
	  hm.src = "//hm.baidu.com/hm.js?9074d34d253e26da296dafbeccd0fed7";
	  var s = document.getElementsByTagName("script")[0]; 
	  s.parentNode.insertBefore(hm, s);
	})();

	/*Baidu 链接提交 自动推送工具代码*/
	(function(){
		/*如果URL的后缀名是html，或者无文件(默认index.html)的话，才开启百度自动推送*/
		if(window.location.pathname.split('.').pop().toLowerCase() == 'html' || window.location.pathname.split('.').pop().toLowerCase() == '/'){
			var bp = document.createElement('script');
		    var curProtocol = window.location.protocol.split(':')[0];
		    if (curProtocol === 'https') {
		        bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';        
		    }else {
		        bp.src = 'http://push.zhanzhang.baidu.com/push.js';
		    }
		    var s = document.getElementsByTagName("script")[0];
		    s.parentNode.insertBefore(bp, s);
		}
	})();

	/*360 SEO 链接提交，自动推送工具*/
	(function(){
		/*如果URL的后缀名是html，或者无文件(默认index.html)的话，才开启360自动推送*/
	   var src = (document.location.protocol == "http:") ? "http://js.passport.qihucdn.com/11.0.1.js?568d0f71d2e79af4ab27da345712bc03":"https://jspassport.ssl.qhimg.com/11.0.1.js?568d0f71d2e79af4ab27da345712bc03";
	   document.write('<script src="' + src + '" id="sozz"><\/script>');
	   var qihucdn = document.createElement('script');
	   qihucdn.src = (document.location.protocol == "http:") ? "http://js.passport.qihucdn.com/11.0.1.js?568d0f71d2e79af4ab27da345712bc03":"https://jspassport.ssl.qhimg.com/11.0.1.js?568d0f71d2e79af4ab27da345712bc03";;
	   qihucdn.id = "sozz";
	   var s = document.getElementsByTagName("script")[0];
	   s.parentNode.insertBefore(qihucdn,s);

	})();
}else{	//使用了之前得缓存js，那么只加载360的即可
	/*360 SEO 链接提交，自动推送工具*/
	(function(){
	   var src = (document.location.protocol == "http:") ? "http://js.passport.qihucdn.com/11.0.1.js?568d0f71d2e79af4ab27da345712bc03":"https://jspassport.ssl.qhimg.com/11.0.1.js?568d0f71d2e79af4ab27da345712bc03";
	   document.write('<script src="' + src + '" id="sozz"><\/script>');
	})();
}