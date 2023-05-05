/**
 * 因网市场云建站系统而做，可视化编辑html
 * author: 管雷鸣
 * url: http://www.wang.market
 * QQ: 921153866
 * email: mail@xnx3.com
 * 微信: xnx3com
 * 最后修改 2019.4.1  同网市场v4.9版本而修改
 * 参数定义：
 * resBasePath 资源文件的路径，如 / ，又如 //res.weiunity.com/
 * htmledit_upload_url 图片上传接口的URL请求地址。post上传 ，input名字为 image
 * 
 * masterSiteUrl 已经废弃，只是为适应网市场老版本而预留
 * 
 */
//网市场服务器域名，需在其父页面定义变量masterSiteUrl，如 var masterSiteUrl = '//wang.market/' ， 然后此可可视化编辑的页面，是页面iframe引入的，可使用 parent.masterSiteUrl 调用;

//当前版本
var htmledit_version = '2.2';
//上传图片最大可允许上传的大小，单位是KB
if(typeof(htmledit_maxsize) == 'undefined'){
	//如果未设置，默认是 3MB 
	htmledit_maxsize = 3000;
}



//资源文件
//var resBasePath = "//res.weiunity.com/";
if(typeof(resBasePath) == 'undefined'){
	resBasePath = "//res.weiunity.com/";
}
if(typeof(htmledit_upload_url) == 'undefined'){
	//若不设置，兼容以前
	if(typeof(masterSiteUrl) == 'undefined'){
		htmledit_upload_url = '';
		xnx3_log('Not set image upload api');
	}else{
		htmledit_upload_url = masterSiteUrl+'template/uploadImage.do';
	}
}


/* http://tool.lu/html/ 压缩 */
//编辑A标签
var aVar = '<div class="xnx3_panel" style="overflow:hidden"><input class="xnx3_dttc" id="aVar_isCreate" type="hidden" value="1"><div class="xnx3_row" style="padding-top:10px"><label class="xnx3_inputLeftLabel" onmouseover="xnx3_htmledit_aVar_href_over()" onmouseout="xnx3_htmledit_aVar_href_out()">链接网址</label><input class="xnx3_inputText" id="aVar_href" type="text" placeholder="此处超链接URL"></div><div class="xnx3_row"><label class="xnx3_inputLeftLabel" onmouseover="xnx3_htmledit_aVar_target_over()" onmouseout="xnx3_htmledit_aVar_target_out()">打开位置</label><select class="xnx3_inputText" id="aVar_target"><option class="xnx3_dttc" value="_self" selected>当前页面</option><option class="xnx3_dttc" value="_black">新开页面</option></select></div><div class="xnx3_row"><label class="xnx3_inputLeftLabel">显示内容</label><textarea class="xnx3_textarea" id="aVar_text" style="width:98%;padding:1%" placeholder="此处为要显示的内容"></textarea></div><div class="xnx3_row"><button class="xnx3_botton" onclick="xnx3_saveATag()">保存</button></div></div>';
//编辑img标签
var imgVar = '<div class=\'xnx3_panel\'><input class=\'xnx3_dttc\' id=\'imgVar_isCreate\' type=\'hidden\' value=\'1\'><div class=\'xnx3_row\' style="padding-top: 10px;"><label class=\'xnx3_inputLeftLabel\' onmouseover=\'xnx3_htmledit_imgVar_src_over()\' onmouseout=\'xnx3_htmledit_imgVar_src_out()\'>图片地址</label><input class=\'xnx3_inputText\' id=\'imgVar_src\' style=\'width:180px;float:left\' placeholder=\'此处为图片所在的路径\'> <img class=\'xnx3_dttc\' id=\'imgVar_imgTag_preview\' alt=\'预览\' onclick=\'window.open(this.src)\' src=\'//res.weiunity.com/image/loading.svg\' width=\'40\' height=\'38\' style=\'float:left;padding-top:2px;margin-top:0;padding-left:5px;cursor:pointer\'> <input class=\'xnx3_dttc\' type=\'file\' name=\'image\' onchange=\'xnx3_uploadImageFileOnChange()\' style=\'width:45px;display:none\' id=\'xnx3_uploadImageFile\'> <button class=\'xnx3_dttc\' onclick=\'openUploadImageBox()\' class=\'xnx3_botton\' style=\'height:38px;padding-left:5px;padding-right:5px;font-size:15px;padding-top:2px;margin-top:2px;cursor:pointer;margin-left:-45px\'>上传新图片</button></div><div class=\'xnx3_row\'><label class=\'xnx3_inputLeftLabel\' onmouseover=\'xnx3_htmledit_imgVar_alt_over()\' onmouseout=\'xnx3_htmledit_imgVar_alt_out()\'>提示文字</label><input class=\'xnx3_inputText\' id=\'imgVar_alt\' placeholder=\'鼠标放到图片上后提示的文字\'></div><div class=\'xnx3_row\'><label class=\'xnx3_inputLeftLabel\' onmouseover=\'xnx3_htmledit_imgVar_width_over()\' onmouseout=\'xnx3_htmledit_imgVar_width_out()\'>图片宽度</label><input class=\'xnx3_inputText\' id=\'imgVar_width\' readonly placeholder=\'在页面上显示的宽度\'></div><div class=\'xnx3_row\'><label class=\'xnx3_inputLeftLabel\' onmouseover=\'xnx3_htmledit_imgVar_height_over()\' onmouseout=\'xnx3_htmledit_imgVar_height_out()\'>图片高度</label><input class=\'xnx3_inputText\' id=\'imgVar_height\' readonly placeholder=\'在页面上显示的高度\'></div><div class=\'xnx3_row\'><button class=\'xnx3_botton\' onclick=\'xnx3_saveImgTag()\'>保存</button></div></div>';
//编辑div、p、span等标签内的内容，不包含标签本身
var innerHTMLVar = '<div class="xnx3_panel" style="overflow:hidden"><textarea class="xnx3_textarea" rows="20" cols="5" id="innerHTMLVar_innerHTML" style="padding:0;padding-left:1%;padding-right:1.1%;padding-top:1%;"></textarea><div class="xnx3_row"><button class="xnx3_botton" onclick="xnx3_saveInnerHTML()">保存</button></div></div>';
//编辑img＋a， a标签中只有一个img标签的情况
var a_imgVar = '<div class="xnx3_panel"><div class="xnx3_row" style="padding-top:10px"><label class="xnx3_inputLeftLabel" onmouseover="xnx3_htmledit_imgVar_src_over()" onmouseout="xnx3_htmledit_imgVar_src_out()">图片地址</label><input class="xnx3_inputText" id="imgVar_src" type="text" style="width:180px;float:left" placeholder="此处为图片所在的路径"> <img class="xnx3_dttc" id="imgVar_imgTag_preview" alt="预览" onclick="window.open(this.src)" src="//res.weiunity.com/image/loading.svg" width="40" height="38" style="float:left;padding-top:2px;margin-top:0;padding-left:5px;cursor:pointer"> <input class="xnx3_dttc" type="file" name="image" onchange="xnx3_uploadImageFileOnChange()" style="width:45px;display:none" id="xnx3_uploadImageFile"> <button class="xnx3_dttc" onclick="openUploadImageBox()" class="xnx3_botton" style="height:38px;padding-left:5px;padding-right:5px;font-size:15px;margin-top:2px;cursor:pointer;margin-left:-45px">上传新图片</button></div><div class="xnx3_row"><label class="xnx3_inputLeftLabel" onmouseover="xnx3_htmledit_imgVar_alt_over()" onmouseout="xnx3_htmledit_imgVar_alt_out()">提示文字</label><input class="xnx3_inputText" id="imgVar_alt" type="text" placeholder="鼠标放到图片上后提示的文字"></div><div class="xnx3_row"><label class="xnx3_inputLeftLabel" onmouseover="xnx3_htmledit_imgVar_width_over()" onmouseout="xnx3_htmledit_imgVar_width_out()">图片宽度</label><input class="xnx3_inputText" id="imgVar_width" readonly type="text" placeholder="在页面上显示的宽度"></div><div class="xnx3_row"><label class="xnx3_inputLeftLabel" onmouseover="xnx3_htmledit_imgVar_height_over()" onmouseout="xnx3_htmledit_imgVar_height_out()">图片高度</label><input class="xnx3_inputText" id="imgVar_height" readonly type="text" placeholder="在页面上显示的高度"></div><div class="xnx3_row"><label class="xnx3_inputLeftLabel" onmouseover="xnx3_htmledit_aVar_href_over()" onmouseout="xnx3_htmledit_aVar_href_out()">链接网址</label><input class="xnx3_inputText" id="aVar_href" type="text" placeholder="此处超链接URL"></div><div class="xnx3_row"><label class="xnx3_inputLeftLabel" onmouseover="xnx3_htmledit_aVar_target_over()" onmouseout="xnx3_htmledit_aVar_target_out()">打开位置</label><select class="xnx3_inputText" id="aVar_target"><option class="xnx3_dttc" value="_self" selected>当前页面</option><option class="xnx3_dttc" value="_black">新开页面</option></select></div><div class="xnx3_row"><button class="xnx3_botton" onclick="xnx3_saveA_ImgTag()">保存</button></div></div>';


/**
 * 日志打印
 */
function xnx3_log(text){
	try {
		console.log(text);
	} catch (e) {
	}
}


/**
 * 鼠标监听
 */
function mouseListener(){
	$(document).mouseover(function(e){
		if(xnx3_isUsed(e)){
			if(typeof($.smartMenu) != 'undefined'){
				//隐藏上次弹出的右键菜单
				$.smartMenu.hide();
				$.smartMenu.remove();	//清除动态数据
			}else{
				loadSmartMenu();
			}
			
			e.target.style.border='2px dashed';
			e.target.style.boxSizing='border-box';
		}
	}).mouseout(function(e){
		e.target.style.border='';
		e.target.style.boxSizing='';
	}).mousedown(function(e){
		var je = $(e.target);
		//alert(e.which) // 1 = 鼠标左键 left; 2 = 鼠标中键; 3 = 鼠标右键 
		//试着chrome反着，去掉
		if(e.which == 3){
		}
		if(xnx3_isUsed(e)){
			currenctMouseRightElement = e.target;
			xnx3_mouseDown_imgTag(e.target);
		}
		
	});
	xnx3_log("create mouse listener");
}

/**
 * 当当前HTML页面加载完毕后，执行的操作
 * <br/>1.设置当前页面可自由编辑
 */
function loadFinishInit(){
	try {
		document.body.contentEditable=true;
	} catch (e) {
	}
}

//加载func.js文件
function loadFuncJsFile(){
	//加载fun.js
	if(typeof(getTemplateId) == 'undefined'){
		jQuery.getScript(resBasePath+"js/fun.js", function(data, status, jqxhr) {
			xnx3_log('Load func.js');
			loadSupportFile();
		});
	}else{
		loadSupportFile();
	}
}

/**
 * 加载所需的支持文件，如func.js等，在JQuery之后调用
 */
function loadSupportFile(){
	var html = xnx3_getHtmlSource();
	
	loadSmartMenu();
	
	//加载layer
	if(typeof(layer) == 'undefined'){
		dynamicLoading.css(resBasePath+"layer/skin/default/layer.css");
		dynamicLoading.js(resBasePath+"layer/layer.js");
		xnx3_log('Load layer');
	}
	
	//加载htmledit.css
	if(html.indexOf("htmledit/htmledit.css") == -1){
		dynamicLoading.css(resBasePath+'htmledit/htmledit.css');
		xnx3_log('Load this css');
	}
	
	//创建所有对象的鼠标监听
	mouseListener();
	
	//加载完后的初始化
	loadFinishInit();
}

//加载 smartMenu.js
function loadSmartMenu(){
	//加载 smartMenu
	if(typeof($.smartMenu) == 'undefined'){
		dynamicLoading.js(resBasePath+'smartMenu/jquery-smartMenu.js');
		dynamicLoading.css(resBasePath+'smartMenu/smartMenu.css');
		xnx3_log('Load smartMenu.js');
	}
}

if(typeof($) == 'undefined'){
	//加载Jquery
	var _doc=document.getElementsByTagName('head')[0];  
	var script=document.createElement('script');  
	script.setAttribute('type','text/javascript');  
	script.setAttribute('src',resBasePath+'js/jquery-2.1.4.js');  
	_doc.appendChild(script);  
	script.onload=script.onreadystatechange=function(){  
		if(!this.readyState||this.readyState=='loaded'||this.readyState=='complete'){  
			xnx3_log('Load JQuery success');
			loadFuncJsFile();
		}
		script.onload=script.onreadystatechange=null;  
	}
}else{
	//加载Func.js
	loadFuncJsFile();
}

//var xnx3_editButton = document.createElement('div');
//xnx3_editButton.innerHTML='修改';
//xnx3_editButton.id="xnx3_editButton";
var currenctMouseRightElement;	//当前右键操作的元素


//鼠标按下时， 标签的右键菜单
function xnx3_mouseDown_imgTag(element){
	var opertionn = {
        name: time,
        offsetX: -5,
        offsetY: -5,
        textLimit: 7,
        beforeShow: $.noop,
        afterShow: $.noop
	};
	
	var attr = new Array();
	//如果是A标签右键点击，会出现进入的菜单项
	xnx3_log(element.nodeName);
	if(element.nodeName == 'A'){
		attr[attr.length] = {
			text: "打开",
			func: function () {
				window.open(element.href);
			}
		}
	}
	
	//图片的二级菜单
	var imageTagMenuData = [
		[
			{
				text: "修改",
				func: function () {
					xnx3_updateTag(element);
				}
			}
//			,{
//				text: "插入",
//				data: [[{
//		            text: "文字",
//		            func: function() {
//		            	element.innerHTML = element.innerHTML + '<span>文字</span>';
//		            }
//		        }, {
//		            text: "换行",
//		            func: function() {
//		            	element.innerHTML = element.innerHTML + '<br/>';
//		            }
//		        }, {
//		            text: "图片",
//		            func: function() {
//		            	layer.open({
//		            		type: 1,
//		            		area: ['500px', '335px'], //宽高
//		            		content: imgVar,
//		            		shadeClose:true,
//		            		title:'添加图片',
//		            		maxmin:true
//		            	});
//		            }
//		        }, {
//		            text: "超链接",
//		            func: function() {
//		            	layer.open({
//		            		type: 1,
//		            		area: ['470px', '490px'], //宽高
//		            		content: aVar,
//		            		shadeClose:true,
//		            		title:'添加超链接',
//		            		maxmin:true
//		            	});
//		            	
//		            	//currenctMouseRightElement.innerHTML = currenctMouseRightElement.innerHTML + '<a href="">超链接</a>';
//		            }
//		        }]]
//			}
			, {
				text: "删除",
				func: function () {
					removeElement(element);
				}
			}
		]
	];
	
	//动态加载当前元素的菜单项，将其加到最前面
	for(var a=0; a<attr.length; a++){
		imageTagMenuData[imageTagMenuData.length-1].push(attr[a]);
	}
	
	try{
		$(element).smartMenu(imageTagMenuData, opertionn);
	}catch(err){
		xnx3_log('try error, cache err :');
		xnx3_log(err);
	}
}

/**
 * 修改当前选中的某个标签
 * @param element 当前选中的Element
 */
function xnx3_updateTag(element){
	switch(element.nodeName){
	case 'A':
		//获取到下级元素
		var child = element.children;
		if(child.length == 1 && child[0].nodeName == 'IMG'){
			//A标签中只包含了一个IMG标签，那么设置当前定位的元素为A下面的IMG标签，而非A标签
			currenctMouseRightElement = child[0];
			xnx3_updateTag_A_ImgTag(currenctMouseRightElement);
			return;
		}
		
		xnx3_updateTag_ATag(element);
	  break;
	case 'IMG':
		//判断此标签是否是混合标签
		var oneDom = element.parentNode;	//1级父元素
		if(oneDom != null){
			//A标签中只包含了一个IMG标签
			if(oneDom.nodeName == 'A' && oneDom.children.length == 1){
				xnx3_updateTag_A_ImgTag(element);
				return;
			}
		}
		
		//单纯只是修改图片
		xnx3_updateTag_ImgTag(element);
	  break;
	default:
		xnx3_updateTag_ElseTag(element);
	}
}

/**
 * 修改A标签，弹出修改弹出框
 * @param element 当前选中的Element
 */
function xnx3_updateTag_ATag(element){
	layer.open({
		type: 1,
		area: ['470px', 'auto'], //宽高
		content: aVar,
		shadeClose:true,
		title:'编辑超链接',
		scrollbar: false,
		maxmin:true
	});
	document.getElementById("aVar_isCreate").value = "0";
	document.getElementById("aVar_href").value = element.getAttribute("href");
	document.getElementById("aVar_text").value = element.innerHTML;
	
	var target = element.target;
	if(target == null){
		target = "_self";
	}
	setSelectChecked("aVar_target",target);
}

/**
 * 修改IMG标签，弹出修改弹出框
 * @param element 当前选中的Element
 */
function xnx3_updateTag_ImgTag(element){
	layer.open({
		type: 1,
		area: ['500px', 'auto'], //宽高
		content: imgVar,
		shadeClose:true,
		scrollbar: false,
		title:'编辑图片',
		maxmin:true
	});
	document.getElementById("imgVar_isCreate").value = "0";
	document.getElementById("imgVar_src").value = element.src;
	document.getElementById("imgVar_alt").value = element.alt;
	document.getElementById("imgVar_width").value = element.naturalWidth+'px';
	document.getElementById("imgVar_height").value = element.naturalHeight+'px';
	document.getElementById("imgVar_imgTag_preview").src=element.src;	//预览小图
}

/**
 * 修改A、IMG标签，弹出修改弹出框
 * @param element 当前选中的Element
 */
function xnx3_updateTag_A_ImgTag(element){
	layer.open({
		type: 1,
		area: ['500px', 'auto'], //宽高
		content: a_imgVar,
		shadeClose:true,
		scrollbar: false,
		title:'编辑图片跟超链接',
		maxmin:true
	});
	document.getElementById("imgVar_src").value = element.src;
	document.getElementById("imgVar_alt").value = element.alt;
	document.getElementById("imgVar_width").value = element.naturalWidth+'px';
	document.getElementById("imgVar_height").value = element.naturalHeight+'px';
	document.getElementById("imgVar_imgTag_preview").src=element.src;	//预览小图
	
	var oneDom = element.parentNode;	//1级父元素
	document.getElementById("aVar_href").value = oneDom.getAttribute("href");
	var target = oneDom.target;
	if(target == null){
		target = "_self";
	}
	setSelectChecked("aVar_target",target);
}

/**
 * 修改除了a、img标签外的其他标签，会先判断其中是否是只有一个元素，如果只有一个元素的话，很可能就是当前的元素只起到包含的作用，那么只需要修改其中的元素即可
 * @param element 当前选中的Element
 */
function xnx3_updateTag_ElseTag(element){
	//如果其中只有一个元素，那么考虑直接编辑其中的元素
	if(element.childNodes.length == 1){
		if(element.childNodes[0].nodeType == 3){
			//如果为3，则是TEXT内容，代表元素或属性中的文本内容。不做操作，直接编辑即可
		}else{
			xnx3_updateTag(element.childNodes[0]);
			return;
		}
	}
	
	//判断要修改的代码的字符长度，若是少于500字符，那么会自动格式化
	var text = element.innerHTML;
//	if(text.length < 500){
//		text = jsAndHtmlFormat(text);
//	}
	
	//打开修改弹出框，修改其内的源代码
	layer.open({
		type: 1,
		area: ['575px', '380px'], //宽高
		content: innerHTMLVar,
		shadeClose:true,
		maxmin:true,
		scrollbar: false,
		title:'编辑内容'
	});
	document.getElementById("innerHTMLVar_innerHTML").value = text;
}

/**
 * 插入／修改一个超链接，A标签
 */
function xnx3_saveATag(){
	if(document.getElementById("aVar_isCreate").value == '1'){
		//创建
		currenctMouseRightElement.innerHTML = currenctMouseRightElement.innerHTML + '<a href="'+document.getElementById("aVar_href").value+'" target="'+document.getElementById("aVar_target").value+'">'+document.getElementById("aVar_text").value+'</a>';
	}else{
		//修改
		currenctMouseRightElement.href = document.getElementById("aVar_href").value;
		currenctMouseRightElement.target = document.getElementById("aVar_target").value;
		currenctMouseRightElement.innerHTML = document.getElementById("aVar_text").value;
	}
	
	layer.closeAll('page');
}
/**
 * 插入／修改一个图片
 */
function xnx3_saveImgTag(){
	xnx3_log('图片操作－'+document.getElementById("imgVar_isCreate").value);
	if(document.getElementById("imgVar_isCreate").value == '1'){
		//创建
		if(width.length > 0){
			width = ' width="'+width+'" ';
		}
		if(height.length > 0){
			height = ' height="'+height+'" ';
		}
		currenctMouseRightElement.innerHTML = currenctMouseRightElement.innerHTML + '<img src="'+document.getElementById("imgVar_src").value+'" alt="'+document.getElementById("imgVar_alt").value+'" '+width+height+' />';
	}else{
		//修改
		xnx3_log('修改图片');
		currenctMouseRightElement.src = document.getElementById("imgVar_src").value;
		currenctMouseRightElement.alt = document.getElementById("imgVar_alt").value;
	}
	
	layer.closeAll('page');
}

/**
 * 修改一个超链接包含的图片，这个超链接内只有这一个图片
 */
function xnx3_saveA_ImgTag(){
	//修改图片
	currenctMouseRightElement.src = document.getElementById("imgVar_src").value;
	currenctMouseRightElement.alt = document.getElementById("imgVar_alt").value;
	
	//修改超链接
	var oneDom = currenctMouseRightElement.parentNode;	//1级父元素
	oneDom.href = document.getElementById("aVar_href").value;
	oneDom.target = document.getElementById("aVar_target").value;

	layer.closeAll('page');
}

/**
 * 保存innerHTML标签的源代码内容
 */
function xnx3_saveInnerHTML(){
	currenctMouseRightElement.innerHTML = document.getElementById("innerHTMLVar_innerHTML").value;
	xnx3_log("id:"+currenctMouseRightElement.id);
	xnx3_log("neirong:"+currenctMouseRightElement.innerHTML);
	layer.closeAll('page');
}

//是否开启鼠标修改元素，若为true，则开启。
//如果是在右键菜单(smart_menu)、js绘出的动态填充(class中有 xnx3_ || e.target.className.indexOf("xnx3_") > -1)，layui-layer 则不触发任何事件
//xnx3_支持向上4级父元素的检索，如这个2级父元素<div class="xnx3_"><div><span>这里的不会出现可编辑区域</span></div></div>
function xnx3_isUsed(e){
	var classNames = e.target.className;
	
	var oneDom = e.target.parentNode;	//1级父元素
	if(oneDom != null){
		if(oneDom.className != null){
			classNames = classNames + ' ' + oneDom.className;
		}
		
		var twoDom = oneDom.parentNode;	//2级父元素
		if(twoDom != null){
			if(twoDom.className != null){
				classNames = classNames + ' ' + twoDom.className;
			}
			
			var threeDom = twoDom.parentNode;	//3级父元素
			if(threeDom != null){
				if(threeDom.className != null){
					classNames = classNames + ' ' + threeDom.className;
				}
				
				var fourDom = threeDom.parentNode;	//4级父元素
				if(fourDom != null){
					if(fourDom.className != null){
						classNames = classNames + ' ' + fourDom.className;
					}
				}
			}
		}
	}
	
	if(e.target.className.indexOf("smart_menu") > -1 || e.target.className.indexOf("xnx3_") > -1 || e.target.className.indexOf("layui-layer-") > -1){
		return false;
	}else{
		if(classNames.indexOf("xnx3_") > -1){
			return false;
		}
		return true;
	}
}

/**
 * 获取当前网页HTML源代码
 * @returns {String}
 */
function xnx3_getHtmlSource(){
	var a = '<!DOCTYPE html><html lang="zh-cn">';
    var z = "</html>"
    var by = $(":root").html();
    return a+by+z;
}

/**
 * 设置当前网页的源代码
 * @param html 要设置的源代码
 */
function xnx3_setHtmlSource(html){
	$(":root").html(html);
}

/**
 * 删除某个元素
 * @param _element 要删除的元素
 */
function removeElement(_element){
    var _parentElement = _element.parentNode;
    if(_parentElement){
           _parentElement.removeChild(_element);  
    }
}

/** 
 * 设置select选中 
 * @param selectId select的id值 
 * @param checkValue 选中option的值 
*/  
function setSelectChecked(selectId, checkValue){  
    var select = document.getElementById(selectId);  
    for(var i=0; i<select.options.length; i++){  
        if(select.options[i].value == checkValue){  
            select.options[i].selected = true;  
            break;  
        }  
    }  
}; 

/**
 * 打开上传图片的选择图片框
 */
function openUploadImageBox(){
//	document.getElementById("xnx3_uploadImageFile").click();
	if(htmledit_upload_url.length == 0){
		layer.alert('未开启图片上传功能！您可配置 htmledit_upload_url 上传接口来启用上传功能');
	}
	document.getElementById("xnx3_uploadImageFile").click();
	//$("#xnx3_uploadImageFile").click();
}
/**
 * 当选择了上传的图片后，执行图片上传操作
 */
function xnx3_uploadImageFileOnChange(){
	//创建FormData对象
    var data = new FormData();
    //为FormData对象添加数据
    $.each($('#xnx3_uploadImageFile')[0].files, function(i, file) {
        data.append('image', file);
    });
    var xnx3_uploadImageFileOnChangeVar = layer.msg('上传中', {
    	  icon: 16
    	  ,shade: 0.01,
    	  time:0
    });
    $.ajax({
        url:htmledit_upload_url,
        type:'POST',
        data:data,
        cache: false,
        size:htmledit_maxsize,
        contentType: false,    //不可缺
        processData: false,    //不可缺
        success:function(data){
        	layer.close(xnx3_uploadImageFileOnChangeVar);
            if(data.result=='0'){
            	alert(data.info);
            }else{
            	//上传成功
            	document.getElementById("imgVar_src").value=data.url;  
            	document.getElementById("imgVar_imgTag_preview").src=data.url;  	//预览的
            }
        },
        error:function(){
        	layer.close(xnx3_uploadImageFileOnChangeVar);
        	alert('上传出错！');
        }
    });
}

/**** 以下为网市场v3.0版本增加函数 ****/

/*创建依赖于layer的鼠标监听，以弹出帮助说明*/

//编辑图片时，图片宽度的说明
var xnx3_htmledit_imgVar_width_tipindex = 0;
function xnx3_htmledit_imgVar_width_over(){
	xnx3_htmledit_imgVar_width_tipindex = layer.tips('当前图片的原始宽度。<br/>如果您要修改当前图片，为了使您新上传的图片看起来不失真，更好看，建议您上传的图片的宽度跟这个宽度一样。', '#imgVar_width', {
		tips: [3, '#0FA6A8'], //还可配置颜色
		time:0,
		tipsMore: true
	});
}
function xnx3_htmledit_imgVar_width_out(){
	layer.close(xnx3_htmledit_imgVar_width_tipindex);
}

//编辑图片时，图片高度的说明
var xnx3_htmledit_imgVar_height_tipindex = 0;
function xnx3_htmledit_imgVar_height_over(){
	xnx3_htmledit_imgVar_height_tipindex = layer.tips('当前图片的原始高度。<br/>如果您要修改当前图片，为了使您新上传的图片看起来不失真，更好看，建议您上传的图片的高度跟这个宽度一样。', '#imgVar_height', {
		tips: [1, '#0FA6A8'], //还可配置颜色
		time:0,
		tipsMore: true
	});
}
function xnx3_htmledit_imgVar_height_out(){
	layer.close(xnx3_htmledit_imgVar_height_tipindex);
}

//编辑图片时，图片地址imput的说明
var xnx3_htmledit_imgVar_src_tipindex = 0;
function xnx3_htmledit_imgVar_src_over(){
	xnx3_htmledit_imgVar_src_tipindex = layer.tips('此处图片的网址。若要修改：<br/><b>1&nbsp.</b>&nbsp;点击右侧的上传图片按钮，直接上传图片。上传图片成功后此处便会变为新的图片网址。<br/><b>2&nbsp.</b>&nbsp;您也可以将其他网站或者其他网页的图片，将其图片的网址直接复制过来，粘贴到这里即可（判断这个其他地方的图片是否是正确的，可以在浏览器地址栏里粘贴上，访问这个网址，看能不能显示出图片来）<br/><hr/>任意一种方式改完后，点击本窗口下方的“保存”按钮即可', '#imgVar_src', {
		tips: [3, '#0FA6A8'], //还可配置颜色
		area: ['330px','auto'],
		time:0,
		tipsMore: true
	});
}
function xnx3_htmledit_imgVar_src_out(){
	layer.close(xnx3_htmledit_imgVar_src_tipindex);
}

//编辑图片时，图片alt的说明 imgVar_alt
var xnx3_htmledit_imgVar_alt_tipindex = 0;
function xnx3_htmledit_imgVar_alt_over(){
	xnx3_htmledit_imgVar_alt_tipindex = layer.tips('图片的说明文字。非必填<br/><b>1&nbsp;.</b>&nbsp;当图片加载缓慢或者图片加载失败，而加载不出图片的时候，会在显示此图片的区域内，显示此文字。<br/><b>2&nbsp;.</b>&nbsp;当鼠标放到图片上时，也会在图片上显示此处文字。<br/>若是不懂，忽略即可', '#imgVar_alt', {
		tips: [3, '#0FA6A8'], //还可配置颜色
		area: ['260px','auto'],
		time:0,
		tipsMore: true
	});
}
function xnx3_htmledit_imgVar_alt_out(){
	layer.close(xnx3_htmledit_imgVar_alt_tipindex);
}

//编辑超链接时，href
var xnx3_htmledit_aVar_href_tipindex = 0;
function xnx3_htmledit_aVar_href_over(){
	xnx3_htmledit_aVar_href_tipindex = layer.tips('超链接的网址，点击后要跳转到的网址。<br/>友情提示：如果要跳转到其他网站，别漏掉http://', '#aVar_href', {
		tips: [3, '#0FA6A8'], //还可配置颜色
		area: ['250px','auto'],
		time:0,
		tipsMore: true
	});
}
function xnx3_htmledit_aVar_href_out(){
	layer.close(xnx3_htmledit_aVar_href_tipindex);
}

//编辑超链接时，打开位置, target
var xnx3_htmledit_aVar_target_tipindex = 0;
function xnx3_htmledit_aVar_target_over(){
	xnx3_htmledit_aVar_target_tipindex = layer.tips('链接网址要在哪里打开。<br/><b>1.&nbsp;当前页面</b>&nbsp;在当前页面打开超链接的网址显示网页；<br/><b>2.&nbsp;新开页面</b>&nbsp;新打开一个窗口或者标签页面，超链接在新打开的窗口里打开显示网页<br/>友情提示：若不是很懂，可忽略此项，默认即可', '#aVar_target', {
		tips: [1, '#0FA6A8'], //还可配置颜色
		time:0,
		tipsMore: true
	});
}
function xnx3_htmledit_aVar_target_out(){
	layer.close(xnx3_htmledit_aVar_target_tipindex);
}