/*服务于iw快速开发架构。author:管雷鸣，github：https://github.com/xnx3/iw */

/**
 * 获取网址的get参数。
 * @param name get参数名
 * @returns value
 */
function GetQueryString(name){
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

/**
 * 列表页右上方的排序方式按钮
 * @param content 传入值如：orderBy('id_DESC=编号倒序,lasttime_DESC=最后登陆时间倒序');
 */
var defaultShow_index = 0;	//当前选中的select 的 index(第几个) ，0为没有选中任何的， 1为第一个....，会在页面加载完毕后，给设置当前排序是使用的什么排序，显示出来， 2.当选择使用其他方式排序时，使用此进行判断，是否选择的有改变，有改变的话，进行跳转操作
function orderBy(content){
	var ds = '排序方式';		//相当于final，默认显示的文字
	var lh = location.href;
	var defaultShow = ds;	//默认显示的文字
	var mainUrl = '';		//网址主url，后不带?name=value这些get参数
	var queryStringFilter = '';		//过滤掉orderBy参数后，url的附带参数集合，结果如： username=a&name=b
	if(lh.indexOf("orderBy")>-1){
		//如果orderBy有传入，那么将传入的这个，作为排序默认的
		defaultShow = GetQueryString('orderBy');
	}	

	var num=lh.indexOf("?") 
	queryString=lh.substr(num+1); //取得所有参数   stringvar.substr(start [, length ]
	var mainUrl = lh.split('?')[0];
	var arr=queryString.split("&"); //各个参数放到数组里
    for(var i=0;i < arr.length;i++){ 
	    num=arr[i].indexOf("="); 
	    if(num>0){
		    name=arr[i].substring(0,num);
		    value=arr[i].substr(num+1);
		    if(value.length>0 && name!='orderBy'){
		    	if(queryStringFilter.length==0){
		    		queryStringFilter = name+"="+value;
		    	}else{
		    		queryStringFilter = queryStringFilter+"&"+name+"="+value;
		    	}
		    }
	    } 
    } 
	
	
	//组合lh网址
	if(queryStringFilter.length>0){
		lh = mainUrl+"?"+queryStringFilter+"&"
	}else{
		lh = mainUrl+"?"
	}
	
	var c = '<option value="">排序方式</option>'
	var ob = content.split(',');
	for(i=0;i<ob.length;i++){
		var o = ob[i].split('=');
		var key = o[0];
		var value = o[1];
		
		c = c + '<option value="'+lh+'orderBy='+key+'">'+value+'</option>';
		if(defaultShow==key){
			defaultShow = value;
			defaultShow_index = i+1;
		}
	}
	
	if(content.indexOf(defaultShow)==-1 || defaultShow.length==0){
		defaultShow = ds;
	}
	c = '<div class="layui-form" style="width: 140px; padding-right:10px;"><select lay-filter="selectOrderBy" id="selectOrderBy_xnx3_id">'+c+'<select></div>';
	document.write(c);
	
	document.getElementById("selectOrderBy_xnx3_id").options[defaultShow_index].selected = true;  
	//layui.form().render('select')	刷新dom
}


//用于记录弹出失败、成功、加载中等最后一次弹出的编号，用来主动关闭时使用
var iw_currentLayerLoadingTipIndex = 0;
var iw = {
	/**
	 * 弹出友好的失败提示，2秒后自动消失
	 * @param text 提示的内容。若不填写，则默认是“操作失败”
	 */ 
	msgFailure: function(text){
		var msgText = '';
		if(typeof(text) == 'undefined'){
			msgText = '操作失败';
		}else{
			msgText = text;
		}
		
		layer.msg(msgText, {
			icon: 2,
			time: 2000 //2秒关闭（如果不配置，默认是3秒）
		}, function(){
			//do something
		});
	},
	/**
	 * 弹出友好的成功提示，2秒后自动消失
	 * @param text 提示的内容。若不填写，则默认是“操作成功”
	 */
	msgSuccess: function(text){
		var msgText = '';
		if(typeof(text) == 'undefined'){
			msgText = '操作成功';
		}else{
			msgText = text;
		}
		layer.msg(msgText, {
			icon: 1,
			time: 2000 //2秒关闭（如果不配置，默认是3秒）
		}, function(){
			//do something
		});
	},
	/**
	 * 显示等待提示，可用来显示ajax表单提交等
	 * @param text 提示文字，若不传，默认显示“加载中..”
	 */
	loading: function(text){
		var msgText = '';
		if(typeof(text) == 'undefined'){
			msgText = '加载中...';
		}else{
			msgText = text;
		}
		iw_currentLayerLoadingTipIndex = layer.msg('<img src="http://res.weiunity.com/image/loading.svg"  style="width:55px; padding:10px; padding-top:15px;" /><div style="padding-top:10px; padding-bottom:5px;">'+msgText+'</div>', {
			icon: -1,
				time: 60000 //60秒关闭（如果不配置，默认是3秒）
			}, function(){
				//do something
			}); 
	},
	/**
	 * 隐藏等待提示，如提交中的等待提示。隐藏的是最近的一次弹出提示框
	 */
	loadClose: function(){
		layer.close(iw_currentLayerLoadingTipIndex);
	}
}

/**
 * 弹出友好的失败提示，2秒后自动消失
 * <br/>已废弃。请使用  iw.msgFailure(text)
 * @param text 提示的内容。若不填写，则默认是“操作失败”
 * @deprecated
 */
function msgFailure(text){
	iw.msgFailure(text);
}

/**
 * 弹出友好的成功提示，2秒后自动消失
 * @param text 提示的内容。若不填写，则默认是“操作成功”
 */
function msgSuccess(text){
	iw.msgSuccess(text);
}

/**
 * 显示等待提示，可用来显示ajax表单提交等
 * @param text 提示文字，若不传，默认显示“加载中..”
 */
function loading(text){
	iw.loading(text);
}

/**
 * 隐藏等待提示，如提交中的等待提示。隐藏的是最近的一次弹出提示框
 */
function loadClose(){
	iw.loadClose();
}