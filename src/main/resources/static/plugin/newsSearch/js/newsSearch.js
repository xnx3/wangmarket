var newsListItemTemplate = ''; //搜索结果的模版，即 searchResultList 的 innerHTML
if(document.getElementById('searchResultList') != null){
	//如果不存在 searchResultList 这个div，那么就不初始化
	document.getElementById('searchResultList').style.display = 'none';	//初始时，将搜索结果设为空
	newsListItemTemplate = document.getElementById('searchResultList').innerHTML;		//搜索到的结果的列表模版
}

//动态异步加载js、css文件
var dynamicLoading = {
	css: function(path){
		if(!path || path.length === 0){
			throw new Error('argument "path" is required !');
		}
		var head = document.getElementsByTagName('head')[0];
		var link = document.createElement('link');
		//link.href = path+'?v='+simpleTime;
		link.href = path;
		link.rel = 'stylesheet';
		link.type = 'text/css';
		head.appendChild(link);
	},
	js: function(path){
		if(!path || path.length === 0){
			throw new Error('argument "path" is required !');
		}
		var head = document.getElementsByTagName('head')[0];
		var script = document.createElement('script');
//		script.src = path+'?v='+simpleTime;
		script.src = path;
		script.type = 'text/javascript';
		head.appendChild(script);
	}
}
//加载 jquery
dynamicLoading.js("http://res.weiunity.com/js/jquery-2.1.4.js");
//加载搜索的分页、信息列表的 css
dynamicLoading.css("http://res.weiunity.com/plugin/newsSearch/css/style.css");

/*
 * 将10位linux时间戳转为字符串描述
 * @param addtime 10位时间戳，也就是新闻的发布时间
 * @return 时间描述，即 yyyy-MM-dd 这种格式
 */
function timeLinuxToString(addtime){
	var date = new Date(addtime*1000);
	Y = date.getFullYear();
	M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1);
	D = date.getDate();
	h = date.getHours();
	m = date.getMinutes();
	s = date.getSeconds();
	return Y+'-'+M+'-'+D;
}
/*
 * 请求文章搜索接口
 * @param pageNumber 要请求的页数，是请求第几页
 */
function requestNewsSearchApi(pageNumber){
	if(typeof(everyPageNumber) == 'undefined'){
		everyPageNumber = 10;
	}
	typeof($) != "undefined"
	
	$.post(masterSiteUrl + "plugin/newsSearch/search.do",
        {
        "siteid": siteid,
        "title":document.getElementById('newsSearchKeyword').value,
        "everyPageNumber":everyPageNumber,
		"currentPage":pageNumber
        },
        function(data){
            if(data.result != '1'){
                //获取搜索结果失败，搜索失败
                alert(data.info);
            }else{
                //获取搜索结果成功
                drawList(data.list);
                drawPage(data.page);
            }
        },
    "json");
}

/*
 * 绘制搜索到的列表数据，显示出来
 * @param list 搜索到的列表数据
 */
function drawList(list){
	if(document.getElementById('searchResultList') == null){
		//如果不存在 searchResultList 这个div，那么就不显示搜索的具体结果
		return;
	}
	var content = '';    //结果组合的字符串
	for(var i=0,l=list.length;i<l;i++){
		var newsJson = list[i];
		var newsContent = newsListItemTemplate;
		newsContent = newsContent.replace(/news.id/g, newsJson.id);
		newsContent = newsContent.replace(/news.titlepic/g, newsJson.titlepic);
		newsContent = newsContent.replace(/news.title/g, newsJson.title);
		newsContent = newsContent.replace(/news.addtime/g, timeLinuxToString(newsJson.addtime));
		newsContent = newsContent.replace(/news.intro/g, newsJson.intro);
		newsContent = newsContent.replace(/news.cid/g, newsJson.cid);
		content = content + newsContent;
	}
	if(list.length == 0){
		content = '未搜索到符合的信息';
	}
	
	document.getElementById('searchResultList').style.display = '';	//显示搜索结果的内容区域
	document.getElementById('searchResultList').innerHTML = content;
}
/*
 * 绘制分页，将分页绘制出来显示
 * @param page 分页的json对象，即请求接口获得到的 data.page
 */
function drawPage(page){
	if(document.getElementById('xnx3_page') == null){
		//如果不存在 xnx3_page 这个div，那么就不显示分页功能
		return;
	}
    var pageHtml = '';
    //判断当前页面是否是列表页第一页，若不是第一页，那会显示首页、上一页的按钮
    if(!page.currentFirstPage){
        pageHtml = pageHtml + '<li style="xnx3_page_home"><a href="javascript:requestNewsSearchApi(1);">首页</a></li>';
        pageHtml = pageHtml + '<li style="xnx3_page_up"><a href="javascript:requestNewsSearchApi('+page.upPageNumber +');">上一页</a></li>';
    }
    //输出上几页的连续几页的按钮
    for(var i=0,l=page.upList.length;i<l;i++){
        var aTag = page.upList[i];
        pageHtml = pageHtml + '<li style="xnx3_page_upList"><a href="javascript:requestNewsSearchApi('+aTag.pageNumber +');">'+aTag.title+'</a></li>';
    }
    //当前页面，当前第几页
    pageHtml = pageHtml + '<li class="xnx3_page_currentPage"><a href="#">'+page.currentPageNumber+'</a></li>';
    //输出下几页的连接按钮
    for(var i=0,l=page.nextList.length;i<l;i++){
        var aTag = page.nextList[i];
        pageHtml = pageHtml + '<li style="xnx3_page_nextList"><a href="javascript:requestNewsSearchApi('+aTag.pageNumber +');">'+aTag.title+'</a></li>';
    }
    //判断当前页面是否是列表页最后一页，若不是最后一页，那会显示下一页、尾页的按钮
    if(!page.currentLastPage){
        pageHtml = pageHtml + '<li style="xnx3_page_next"><a href="javascript:requestNewsSearchApi('+page.nextPageNumber +');">下一页</a></li>';
        pageHtml = pageHtml + '<li style="xnx3_page_last"><a href="javascript:requestNewsSearchApi('+page.lastPageNumber+');">尾页</a></li>';
    }
    //统计信息
    pageHtml = pageHtml + '<li class="statistics">共'+page.allRecordNumber+'条，'+page.currentPageNumber+'/'+page.lastPageNumber+'页</li>';
    if(page.allRecordNumber > 0){
    	//当搜索条数大于0时，才会显示分页跳转
    	document.getElementById('xnx3_page').innerHTML = '<ul>' + pageHtml + '</ul>';
    }
}

//获取URL的GET参数。若没有，返回""
function getUrlParam(name){
   var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
   var r = window.location.search.substr(1).match(reg);
   if(r!=null)return  unescape(r[2]); return "";
}

//获取get传递来的参数进行搜索。get名字为 searchKeyword， value使用 escape 转码
function gainGetParamSearch(){
	//获取get传递来的参数
	var keyword = unescape(getUrlParam('searchKeyword'));
	if(keyword.length > 0){
		//有get参数，那么就要请求接口，获取搜索结果。
		//因为jquery是异步加载，需要现等待jquery加载完毕才能进行接口请求，所以避免时间比较长，用户体验考虑，出现加载中的提示
		if(document.getElementById('searchResultList') != null){
			document.getElementById('searchResultList').innerHTML = '加载中...';
		}
		document.getElementById('newsSearchKeyword').style.display='';
		document.getElementById('newsSearchKeyword').value = keyword;
		
		//循环，避免jquery加载慢导致请求接口失败
		setTimeout("gainGetParamSearch_DelayRequest()","200");
	}
}
var loadJquerySuccess = false;	//记录jquery是否加载完毕
function gainGetParamSearch_DelayRequest(){
	while(typeof($) != 'undefined'){
		if(!loadJquerySuccess){
			requestNewsSearchApi(1);
			loadJquerySuccess = true;
			return;
		}
	}
	setTimeout("gainGetParamSearch_DelayRequest()","200");
};
//获取传递来的searchKeyword关键词进行搜索。若未发现这个传入，则不搜索
gainGetParamSearch();