/*模版3、5、6都是用的这个*/

/*加载LOGO图*/
/* document.getElementById("img_logo").src="https://www.baidu.com/img/baidu_jgylogo3.gif"; */
document.getElementById("img_logo").style.display='none';
document.getElementById("logogramName").innerHTML = site['name'];/*设置LOGO位置字符*/
/*若显示Banner图*/
if(site['mShowBanner'] == '1'){
	/*设置banner图的点击链接地址*/
	for(var c=0;c<carouselList.length;c++){
		//为null则是兼容以前创建的网站，只有当banner的格式为1时，才会显示图片（不适用模版7）
		if(carouselList[c]['type']==null || carouselList[0]['type']=='1'){
			document.getElementById("img_banner").src=carouselList[c]['image'];
			break;
		}
	}
}else{
	document.getElementById("banner").style.display='none';/*如不显示，直接让其隐藏*/
}

//点击左上方站点的名字或LOGO
function siteNameClick(){
	window.location.href="index.html";
}

var addSiteColumnHTML = document.getElementById('columnList').innerHTML;
/* 刚打开初始化栏目、导航的缓存数据 */
function initSiteColumnData() {
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

		//document.getElementById('columnList').innerHTML = addSiteColumnHTML+content;
		document.getElementById('columnList').innerHTML = content;
	} else {
		alert(siteColumnRank.langth + "," + siteColumn.length);
		alert('栏目导航排序规则跟数据不一致！');
	}
}
initSiteColumnData();
