/*加载LOGO图*/
/* document.getElementById("img_logo").src="https://www.baidu.com/img/baidu_jgylogo3.gif"; */
document.getElementById("img_logo").style.display='none';
document.getElementById("logogramName").innerHTML = site['name'];/*设置LOGO位置字符*/
/*若显示Banner图*/
if(site['mShowBanner'] == '1'){
	/*设置banner图的点击链接地址*/
	for(var c=0;c<carouselList.length;c++){
		if(carouselList[c]['type'] == null){
			//默认为1，兼容原本的
			carouselList[c]['type'] == '1';
		}
		if(document.getElementById("img_banner") != null){
			//1是内页的banner
			if(carouselList[c]['type'] == '1'){
				if(edit){
					document.getElementById("banner_img").innerHTML='<img id="img_banner" src="'+carouselList[c]['image']+'" alt="'+site['name']+'" onclick="updateBanner();" />';
				}else{
					document.getElementById("img_banner").src=carouselList[c]['image'];
				}
			}
		}else{
			//首页是不在header加载Banner的，在首页模版中
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
		var content = '';
		if(isIndex){
			content = '<a href="#indexBigImage">首页</a>';
		}else{
			content = '<a href="index.html">首页</a>';
		}
		
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
						if(isIndex){
							url = '<a href="#imageList">' + siteColumn[j]['name'] +"</a>";
						}else{
							url = '<a href="lc'+siteColumn[j]['id']+'_1.html">' + siteColumn[j]['name'] +"</a>";
						}
						break;
					case '3':
						/* 独立页面 */
						if(isIndex){
							//是首页
							
							if(siteColumn[j]['id'] == site['aboutUsCid']){
								//如果是关于我们，则描点定位到关于我们页面
								url = '<a href="#aboutUs">' + siteColumn[j]['name'] +"</a>";
							}else{
								//联系我们
								url = '<a href="#contactUs">' + siteColumn[j]['name'] +"</a>";
							}
						}else{
							//不是首页
							if(siteColumn[j]['id'] == site['aboutUsCid']){
								//关于我们，跳转到内页
								url = '<a href="c'+siteColumn[j]['id']+'.html">' + siteColumn[j]['name'] +"</a>";
							}else{
								//如果不是关于我们，那么一定就是联系我们了～～后续可以自己建立栏目时，再修改
								//跳转到首页的联系我们
								url = '<a href="index.html#contactUs">' + siteColumn[j]['name'] +"</a>";
							}
						}
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
