/** 网站通用函数库，此需要在加载site.js之后 **/

//判断是否加载了site.js，若没有加载，默认一个
if(typeof site == "undefined"){
	var site = new Array();
	site['id'] = 1;
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

//获取当前所用模版的编号
function getTemplateId(){
	if(typeof(site) != "undefined"){
		return site['templateId'];
	}else{
		return '1';
	}
}

//获取当前网站的URL地址。若是未绑定域名，则返回二级域名，若是绑定域名了，则返回绑定的域名。不带http://
function getSiteUrl(){
	if(typeof(site['bindDomain']) == "undefined" || site['bindDomain'] == ''){
		return site['domain']+'.wang.market';
	}else{
		return site['bindDomain'];
	}
}

//当前的时间戳
var time=new Date().getTime();

//当前的时间戳
var myDate = new Date();
//当前的时间戳，具体到天
var simpleTime = myDate.getFullYear()+''+myDate.getMonth()+''+myDate.getDate();

/**
 * 当前网市场的版本号
 * 2016.7~2017.3 通用模版为1.0版本
 * 2017.3~		自定义模版为2.0版本
 */
var version = "2.1.1";

//res 资源文件的请求网址
var resBasePath = "//res.weiunity.com/";

/*当前管理的路径*/
var basePath = 'http://wang.market/';

/*设置OSSUtil.url*/
var OSSUrl = '//cdn.weiunity.com/';
if(typeof(attachmentFileUrl) != 'undefined'){
	//若 attachmentFileUrl 设置了，那么将其赋予OSSUrl。 OSSUrl是很早之前，1.0版本下来的，这里为了兼容以前的网站。以后直接用attachmentFileUrl即可
	OSSUrl = attachmentFileUrl;
}

/*当前网站所在的CDN路径*/
var siteUrl = OSSUrl+'site/'+site['id']+'/';

/*当前是否是位于首页，true:是在首页*/
var pathnameSplit = window.location.pathname.split('/');  
var lastpathName = pathnameSplit[pathnameSplit.length-1];
var isIndex = lastpathName == 'index.html' || lastpathName == '' || lastpathName == 'editPcIndex.do' > -1;

//设置顶部title区块的标题内容
function setTitle(t){
	document.getElementById('title').innerHTML = t;
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

//设置Cookie
function editCookie(name,value,expiresHours){
	var cookieString=name+"="+escape(value); 
	//判断是否设置过期时间,0代表关闭浏览器时失效
	if(expiresHours>0){ 
		var date=new Date(); 
		date.setTime(date.getTime+expiresHours*3600*1000); //单位是多少小时后失效
		cookieString=cookieString+"; expires="+date.toGMTString(); 
	}
	document.cookie=cookieString; 
}

/**
 * 设置Cookie，失效时间一年。
 * @param name
 * @param value
 */
function setCookie(name,value){
	editCookie(name, value, 8640);
}

//获取Cookie。若是不存再，返回空字符串
function getCookie(name){ 
	var strCookie=document.cookie; 
	var arrCookie=strCookie.split("; "); 
	for(var i=0;i<arrCookie.length;i++){ 
		var arr=arrCookie[i].split("="); 
		if(arr[0]==name){
			return unescape(arr[1]);
		}
	}
	return "";
}

//获取URL的GET参数。若没有，返回""
function getUrlParam(name){
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return "";
}

