/*设置底部的网站相关信息*/
document.getElementById('footerCopyRight').innerHTML = 'Copyright（c）2016 版权所有：'+site['companyName']+' All Rights Reserved';
document.getElementById('footerContactsCompanyName').innerHTML = site['companyName'];	
document.getElementById('footerContactsUsername').innerHTML = site['username'];	
document.getElementById('footerContactsPhone').innerHTML = site['phone'];
document.getElementById('footerContactsQQ').innerHTML = site['qq'];
document.getElementById('footerDomainName').innerHTML = 'http://'+getSiteUrl();
document.getElementById('footerContactsAddress').innerHTML = site['address'];

/* 刚打开初始化栏目、导航的缓存数据 */
function initFooterSiteColumnData() {
	/* 首先先判断排序跟栏目导航数据个数是否一样 */
	if (siteColumnRank.langth != siteColumn.length) {
		var contentedit = '<a href="'+masterSiteUrl+'site/editPcIndex.do?siteid='+site['id']+'">首页</a>';
		
		/* 先遍历排序数据，根据排序拿到栏目导航数据 */
		for (var i = 0; i < siteColumnRank.length; i++) {
			var rank = siteColumnRank[i];
			for (var j = 0; j < siteColumn.length; j++) {
				if (siteColumn[j]['id'] == siteColumnRank[i]) {
					var urledit = siteColumn[j]['url'];
					var type = siteColumn[j]['type'];
					switch (type) {
					case '1':
						/* 新闻 */
						urledit = '<a href="'+masterSiteUrl+'news/list.do?cid='+siteColumn[j]['id']+'&client=pc&editMode=edit">' + siteColumn[j]['name'] +"</a>";
						break;
					case '2':
						/* 图文 */
						urledit = '<a href="'+masterSiteUrl+'news/list.do?cid='+siteColumn[j]['id']+'&client=pc&editMode=edit">' + siteColumn[j]['name'] +"</a>";
						break;
					case '3':
						/* 独立页面 */
						urledit = '<a href="'+masterSiteUrl+'page/page.do?cid='+siteColumn[j]['id']+'&client=pc&editMode=edit">' + siteColumn[j]['name'] +"</a>";
						break;
					case '4':
						/* 留言板 */
						break;
					case '5':
						/* 超链接 */
						urledit = '<a href="'+masterSiteUrl+'column/column.do?id='+siteColumn[j]['id']+'&client=pc">' + siteColumn[j]['name'] +"</a>";
						break;
					}
					contentedit = contentedit + urledit;
					break;
				}
			}
		}

		document.getElementById('footerColumnList').innerHTML = contentedit;
	} else {
		alert(siteColumnRank.langth + "," + siteColumn.length);
		alert('栏目导航排序规则跟数据不一致！');
	}
}
initFooterSiteColumnData();

try{
	/*百度分享：尾部、划词、图片获取焦点*/
	document.getElementById('bdsharebuttonbox').innerHTML = '<div class="bdsharebuttonbox"><a href="#" class="bds_more" data-cmd="more"></a><a href="#" class="bds_sqq" data-cmd="sqq" title="分享到QQ好友"></a><a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"></a><a href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a><a href="#" class="bds_tqq" data-cmd="tqq" title="分享到腾讯微博"></a><a href="#" class="bds_renren" data-cmd="renren" title="分享到人人网"></a><a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a><a href="#" class="bds_mshare" data-cmd="mshare" title="分享到一键分享"></a></div><script>';
	//window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"1","bdSize":"24"},"share":{},"image":{"viewList":["sqq","qzone","tsina","tqq","renren","weixin","mshare"],"viewText":"分享到：","viewSize":"16"},"selectShare":{"bdContainerClass":null,"bdSelectMiniList":["sqq","qzone","tsina","tqq","renren","weixin","mshare"]}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];
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

try{
	//填充右下角的联系电话
	document.getElementById('footerPhone').innerHTML='<img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACYAAAAmCAYAAACoPemuAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCRkRCMTFBQzREOEIxMUU2QkJGOUU3NTI0QzdBMzNDRiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCRkRCMTFBRDREOEIxMUU2QkJGOUU3NTI0QzdBMzNDRiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkJGREIxMUFBNEQ4QjExRTZCQkY5RTc1MjRDN0EzM0NGIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkJGREIxMUFCNEQ4QjExRTZCQkY5RTc1MjRDN0EzM0NGIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+EmfoowAAA6NJREFUeNq0mFtIFVEUho+KZplpVyvD0iLFRIXIzErzoQyzFEuD6sEyKsiKEgJB0qiX8KGEEuyloIgKoZv40MWgQNFudDEyMoqwDMsuoGUqp3/HGljs9szZc2Zc8OFZe9Za87vPzD5r7wCv1+vxw5aAZLAApIP5IIKuDYA3oB08By9As+07CGE2KAWfvP9bL3hJfFRcHwSVYKzuvXQF7ZZuWAc2gxQQqogPBvGgGNSAnyz3iEmOLWHh4DErWq1T1ISdYIjNcKy/wvLBNyp0CsywISKOZjNMGh8Dqtg/uteusGUsOV9TTACoB+9Ybg+YrRCYwWJKdYXlUoJ4LlI1RYmH+qlXbQWgAzTQs2fkzKFx5czJNxgPhik43sZX12wiqluanZNSXgR77lL4tUBp9XgAgkAO6NRccQ6BbJNrdyQ/S/J/gET6LNa9MNU6VkbK623MVLrX2vJAIfOvmtTZT9drVF/lL7oYbENYtw9hIuYa87db1OqhmEgubBcNHrAh6oQPUVvAYub3Kd5OznKKu8KFjYDfNkRlmIgZAMdBJsX1SkJ91e2k2GjhZJFzzIawRwpR30EMiwkCCWADoVO3mmptFc4+ctZpJq8wma31fv5UcRYay4pwzpMTo5EYAj4oRN1wQZTBH/DFeBuGNZPWmsxWnIvC2kRBscBGgQ7NxbRMMfYQvPW4Z/+0GCt/t0bCXLBKMV7ucde+cmFDGglVirE2cM9lYV4ubLKP4FCwUTF+weO+hRvCRmhTYWVJIEQx/mwUhMUawlpBJJhgEbzGZLxrFISlickKZK3JIovgYpPxBJdFxYCJoFEIe0WDqSbBs1jPJNtFMM5FYYVGHyeE3SRnj8XUmtkgiAYlLgnLo7+txmp7mlbwIsVKvMOitVkJVtPnWocrfhTVec3bnmC2Y5YTEqktUnUTYbT79lLv7kRYI9XJlDvYSrpwWJF0VCFMbNcOMr/JgSijSbxltktqoYBUabvfQo2kYYnUa3FLdiCsi2okWe0r31PQdPLrWFu0iXqmCklUiQNR7aq9pdnmdRuYRn6/4tm7T8X6Sbi/omrN+jmd5GFq3uTxqWCKA1GtJOq2v8dQZ6hAoUuNYC57k687OR8LZ9v4AgeCosFZ9lyWu3Fwl8YKXqaNxxiNvJl0LnZJ2h9k+8oNsHEGK9qRc2ApG2uhM9bPoA8Eg0n0Yyx+yuaxWHEuWwEadG5mR5hhOXQ4UiTdWGV3QRN4ojhgsbS/AgwA36FSkn+yAlsAAAAASUVORK5CYII="><span id="footerPhoneAndShare_phone">'+site['phone']+'</span>';
}catch(err){}	

/*兼容以前模版中，qrImage直接写在模版里的height*/
document.getElementById("qrImage").style.height="160px";
