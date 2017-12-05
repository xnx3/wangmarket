/*加载LOGO图*/
/* document.getElementById("img_logo").src="https://www.baidu.com/img/baidu_jgylogo3.gif"; */
document.getElementById("img_logo").style.display='none';
document.getElementById("logogramName").innerHTML = site['name'];/*设置LOGO位置字符*/
/*若显示Banner图*/
if(site['mShowBanner'] == '1'){
	/*设置banner图的点击链接地址*/
	for(var c=0;c<carouselList.length;c++){
		if(document.getElementById("img_banner") != null){
			if(edit){
				document.getElementById("banner_img").innerHTML='<img id="img_banner" src="'+carouselList[c]['image']+'" alt="'+site['name']+'" onclick="updateBanner();" />';
			}else{
				document.getElementById("img_banner").src=carouselList[c]['image'];
			}
		}else{
			//首页是不加载Banner的
		}
	}
}else{
	document.getElementById("banner").style.display='none';/*如不显示，直接让其隐藏*/
}

//点击左上方站点的名字或LOGO
function siteNameClick(){
	//调用 commonedit.js中的修改站点名字
	updateSiteName();
}

var addSiteColumnHTMLedit = document.getElementById('columnList').innerHTML;
/* 刚打开初始化栏目、导航的缓存数据 */
function initSiteColumnData() {
	/* 首先先判断排序跟栏目导航数据个数是否一样 */
	if (siteColumnRank.langth != siteColumn.length) {
		var contentedit = '<a href="'+masterSiteUrl+'sitePc/editIndex.do">首页</a>';
		
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
						urledit = urledit + '<ul><li><a href="'+masterSiteUrl+'news/listForTemplate.do?cid='+siteColumn[j]['id']+'">内容填充</a></li><li><a href="javascript:updateSiteColumn(\''+siteColumn[j]['id']+'\');">栏目属性</a></li></ul>';
						break;
					case '2':
						/* 图文 */
						urledit = '<a href="'+masterSiteUrl+'news/list.do?cid='+siteColumn[j]['id']+'&client=pc&editMode=edit">' + siteColumn[j]['name'] +"</a>";
						urledit = urledit + '<ul><li><a href="'+masterSiteUrl+'news/listForTemplate.do?cid='+siteColumn[j]['id']+'">内容填充</a></li><li><a href="javascript:updateSiteColumn(\''+siteColumn[j]['id']+'\');">栏目属性</a></li></ul>';
						break;
					case '3':
						/* 独立页面 */
						urledit = '<a href="'+masterSiteUrl+'news/updateNewsByCid.do?cid='+siteColumn[j]['id']+'">' + siteColumn[j]['name'] +'</a><ul><li><a href="'+masterSiteUrl+'news/updateNewsByCid.do?cid='+siteColumn[j]['id']+'">内容填充</a></li><li><a href="javascript:updateSiteColumn(\''+siteColumn[j]['id']+'\');">栏目属性</a></li></ul>';
						break;
					case '4':
						/* 留言板 */
						break;
					case '5':
						/* 超链接 */
						urledit = '<a href="'+masterSiteUrl+'column/column.do?id='+siteColumn[j]['id']+'&client=pc">' + siteColumn[j]['name'] +"</a>";
						urledit = urledit + '<ul><li><a href="javascript:updateSiteColumn(\''+siteColumn[j]['id']+'\');">栏目属性</a></li></ul>';
						break;
					}
					contentedit = contentedit + urledit;
					break;
				}
			}
		}

		//document.getElementById('columnList').innerHTML = addSiteColumnHTMLedit+contentedit;
		document.getElementById('columnList').innerHTML = contentedit;
	} else {
		alert(siteColumnRank.langth + "," + siteColumn.length);
		alert('栏目导航排序规则跟数据不一致！');
	}
}
initSiteColumnData();

/*设置轮播图，放上点击修改的按钮*/
/*获取原本的赋值*/
var banner_img_edit;
var updateHeadImageHtml = '<div><span onclick="updateBanner();">修改图片</div></div>';
if(document.getElementById('banner_img') == null){
	//没有Banner图那便是首页了，首页不予处理，交给index.html模版中处理
	
}else{
	banner_img_edit = document.getElementById('banner_img').innerHTML;
	/*计算原本＋附加编辑按钮的赋值*/
	banner_img_edit =  + banner_img_edit;
	/*重新赋值*/
	document.getElementById('banner_img').innerHTML = banner_img_edit;
}


/* 保存栏目导航的排序,top中会出现模版替换问题，所以拿到此处 */
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

(function() {
	try{
		/* 拖动布局 */
		[].forEach.call(body.getElementsByClassName('content'),
			function(el) {
				new Sortable(el, {
					group : 'content',
					draggable: "div",   // 指定那些选项需要排序
					onUpdate : function(evt) {
						//var d = HTML.query("#columnList").div[2];
						//saveRank();
					}
				});
			});
	}catch(err){}	
})();