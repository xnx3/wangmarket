/**
 * 可视化编辑html
 * author: 管雷鸣
 * QQ: 921153866
 * email: mail@xnx3.com
 * 微信: xnx3com
 * 开源仓库 https://github.com/xnx3/HtmlVisualEditor
 */
HtmlVisualEditor = {
	//版本
	version : '3.0',
	//相关配置
	config:{
		//资源文件所在
		resBasePath:'https://res.zvo.cn/',
		//上传图片最大可允许上传的大小，单位是KB。如果未设置，默认是 3MB 
		//uploadImageMaxSize:3000, 
		//上传图片保存的api接口。待补充接口规范约束。
		//设置入 http://xxxx.com/uploadImage.json
		uploadImageApi : '', 
	},
	util:{
		//同步方式加载js文件. url 传入相对路径，前面会自动拼接上 HtmlVisualEditor.config.resBasePath
		syncLoadJs(url){
			/*
			// 使用 XMLHttpRequest 对象发送同步请求
			var xhr = new XMLHttpRequest ();
			xhr.open ('GET', HtmlVisualEditor.config.resBasePath+url, false); // 第三个参数为 false 表示同步请求
			xhr.send ();
			//console.log(xhr.responseText);
			// 使用 eval 函数执行响应文本
			eval (xhr.responseText);
			*/
			var  xmlHttp = null;  
			if(window.ActiveXObject){//IE  
				try {  
					//IE6以及以后版本中可以使用  
					xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");  
				} catch (e) {  
					//IE5.5以及以后版本可以使用  
					xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");  
				}  
			}else if(window.XMLHttpRequest){  
				//Firefox，Opera 8.0+，Safari，Chrome  
				xmlHttp = new XMLHttpRequest();  
			}  
			//采用同步加载  
			xmlHttp.open("GET",HtmlVisualEditor.config.resBasePath+url,false);  
			//发送同步请求，如果浏览器为Chrome或Opera，必须发布后才能运行，不然会报错  
			xmlHttp.send(null);  
			//4代表数据发送完毕  
			if( xmlHttp.readyState == 4 ){  
				//0为访问的本地，200到300代表访问服务器成功，304代表没做修改访问的是缓存  
				if((xmlHttp.status >= 200 && xmlHttp.status <300) || xmlHttp.status == 0 || xmlHttp.status == 304){  
					var myBody = document.getElementsByTagName("HTML")[0];  
					var myScript = document.createElement( "script" );  
					myScript.language = "javascript";  
					myScript.type = "text/javascript";  
					try{  
						//IE8以及以下不支持这种方式，需要通过text属性来设置  
						myScript.appendChild(document.createTextNode(xmlHttp.responseText));  
					}catch (ex){  
						myScript.text = xmlHttp.responseText;  
					}  
					myBody.appendChild(myScript);  
					return true;  
				}else{  
					return false;  
				}  
			}else{  
				return false;  
			}  

		},
		//异步方式加载css文件. url 传入相对路径，前面会自动拼接上 HtmlVisualEditor.config.resBasePath
		loadCss(url){
			var head = document.getElementsByTagName('head')[0];
			var link = document.createElement('link');
			link.href = HtmlVisualEditor.config.resBasePathurl;
			link.rel = 'stylesheet';
			link.type = 'text/css';
			head.appendChild(link);
		}
	},
	//监听
	listener:{
		//针对鼠标的监听
		mouse:function(){

			console.log('mouse listener add');

			// 给 div 元素添加一个 click 事件处理函数
			HtmlVisualEditor.document.get().body.addEventListener("click", function (e) {
			  //console.log (e);
				
			});

			$(HtmlVisualEditor.document.get()).mouseover(function(e){
				e.target.style.border='2px dashed';
				e.target.style.boxSizing='border-box';
			}).mouseout(function(e){
				e.target.style.border='';
				e.target.style.boxSizing='';
			}).mousedown(function(e){
				var je = $(e.target);
				//alert(e.which) // 1 = 鼠标左键 left; 2 = 鼠标中键; 3 = 鼠标右键 
				//试着chrome反着，去掉
				if(e.which == 1){
					//console.log(e);
					HtmlVisualEditor.document.editElement(e);
				}
				
			});
		}
	},
	//当前操作的document对象 . 此段来源于 https://gitee.com/mail_osc/kefu.js/blob/main/kefu.js
	document:{
		iframeId:'', //iframe的id,在iframe中显示，那这里是显示界面的iframe 的 id 
		//获取当前kefu.js操作的document对象
		get:function(){
			if(HtmlVisualEditor.document.iframeId.length < 1){
				//不在iframe显示，那在当前页面显示
				return document;
			}else{
				return document.getElementById(HtmlVisualEditor.document.iframeId).contentWindow.document;
			}
		},
		//设置当前kefu.js操作的document对象。如果不设置，默认操作的是当前页面，如果设置，传入iframe元素id，则kefu显示在iframe中，避免被当前页面的css影响
		set:function(iframe_id){
			HtmlVisualEditor.document.iframeId = iframe_id;
		},
		//设置可视化编辑的html源码
		setHtml:function(html){
			var o = document.getElementById(HtmlVisualEditor.document.iframeId);
			ed = document.all ? o.contentWindow.document : o.contentDocument;
			ed.open();
			ed.write(html);
			ed.close();
			
			setTimeout('HtmlVisualEditor.life.loadFinish()', 1000);
		},

		//获取可视化编辑的html源码
		getHtml(){
			//将body的contentEditable设为false
			try{
				HtmlVisualEditor.document.get().body.contentEditable=false;
			}catch(e){ console.log(e); }
			

			//取html内容
			var html = HtmlVisualEditor.document.get().documentElement.outerHTML;

			//再讲页面显示相关还原回去
			try{
				HtmlVisualEditor.document.get().body.contentEditable=true;			
			}catch(e){ console.log(e); }
			

			//对html进行处理
			//将 style="" 去掉
			html = html.replace (/\s*style=\"\"/g, "");
			//将js限制求掉
			html = html.replace (/<meta http-equiv="content-security-policy" content="script-src 'none'">/g, "");
			//将 body 的  contenteditable="false" 去掉
			html = html.replace (/\scontenteditable=\"false\"/g, "");

			return html;
		},
		//获取父页面的document
		parent:function(){
			return window.parent.document;
		},
		editPanel:function(){
			return HtmlVisualEditor.document.parent().getElementById('HtmlVisualEditor_EditPanel');
		},
		//是否有子元素，true 有
		hasChild:function(obj){
			for(var i = 0; i<obj.children.length; i++){
				console.log(obj.children[i]);
			}
			return obj.children.length > 0;

		},
		//鼠标放上修改元素时
		editElement:function(obj){
			//if(obj.target.childNodes.length > 0){
				//还有子元素，那忽略他
			//	return;
			//}
			var tagname = obj.target.tagName;
			console.log(tagname+', '+HtmlVisualEditor.document.hasChild(obj.target));
			HtmlVisualEditor.editPanel.current = obj.target;

			//判断当前是否设置了背景图
			var backgroundImage = HtmlVisualEditor.document.getStyleBackgroundImage(obj.target);
			console.log(backgroundImage)


			var html = '';
			switch(tagname){
				case 'IMG':
					html = HtmlVisualEditor.editPanel.tag.img;
				break;
				case 'A':
					html = HtmlVisualEditor.editPanel.tag.a;
				break;
				default:
					if(backgroundImage != null){
						//设置了背景图
						html = HtmlVisualEditor.editPanel.css.backgroundImage;
						html = html.replace (/{src}/g, backgroundImage);
					}
			}

				
				
			//底部保存按钮
			if(html.length > 0){
				html = html + HtmlVisualEditor.editPanel.foot;
			}else{
				html = '请点击右侧要修改的区域';
			}

			//头部标签属性
			html = HtmlVisualEditor.editPanel.head + html;


			//数据赋予
			if(html.indexOf('{id}') > -1){
				html = html.replace (/{id}/g, obj.target.id);
			}
			if(html.indexOf('{tag}') > -1){
				html = html.replace (/{tag}/g, obj.target.tagName);
			}
			if(html.indexOf('{text}') > -1){
				html = html.replace (/{text}/g, obj.target.innerHTML);
			}
			if(html.indexOf('{src}') > -1){
				html = html.replace (/{src}/g, obj.target.src);
			}
			if(html.indexOf('{href}') > -1){
				html = html.replace (/{href}/g, obj.target.getAttribute('href'));
			}
			if(html.indexOf('{alt}') > -1){
				html = html.replace (/{alt}/g, obj.target.alt);
			}

			
			HtmlVisualEditor.document.editPanel().innerHTML = html;

			//弹窗使用说明参考 https://gitee.com/leimingyun/dashboard/wikis/leimingyun/msgjs/preview?sort_id=4112035&doc_id=1473987
			/*
			msg.popups({
			    text:'<div>'+html+'</div>',
			    padding:'1px',
			    height:'600px',
			    //background:'#FFFFFF',
			    opacity:80,
			    padding:'1rem'
			});
			*/

		},
		//获取元素的css设置的背景图的url。如果没有设置，则返回null
		getStyleBackgroundImage:function(element){
			//使用window.getComputedStyle()方法的优点是可以获取元素的计算样式，即最终应用到元素上的样式，包括样式表、媒体查询、继承等因素的影响。缺点是这个方法返回的是一个只读的对象，无法修改元素的样式，而且在某些浏览器中，这个方法可能比较耗时。
			var style = window.getComputedStyle(element);
			var bg = style.backgroundImage;
			if(bg == null || typeof(bg) == 'undefined' || bg == 'none'){
				return null;
			}
			var url = bg.match(/url\(\"(.*)\"\)/)[1]; //使用正则表达式匹配url()中的内容
			return url;
		}
	},
	//生命周期
	life:{
		//加载支持文件完成，func.js等，在JQuery之后调用
		loadSupportFileFinsh:function(){
			
			//HtmlVisualEditor.util.syncLoadJs('js/jquery-2.1.4.js');
			//HtmlVisualEditor.util.syncLoadJs('js/fun.js');

			//HtmlVisualEditor.util.loadCss('htmledit.css');


		},
		//加载完毕，要进入编辑模式了
		loadFinish:function(){
			HtmlVisualEditor.document.get().body.contentEditable=true;

			//加入鼠标监听
			HtmlVisualEditor.listener.mouse();
			console.log('life loadFilish');
		}
	},
	//编辑面板
	editPanel:{
		current:null,	//当前操作的元素
		head: `
			<div class="head">
				<div><label>标签：</label><span>{tag}</span></div>
				<div><label>ID：</label><span>{id}</span></div>
				<div><label>操作：</label><a href="javascript:HtmlVisualEditor.editPanel.remove()" class="delete">删除</a> | <a href="javascript:alert(HtmlVisualEditor.editPanel.current.innerHTML)" class="source">源码</a> </div>
			</div>
		`,
		foot: `
			<div onclick="HtmlVisualEditor.editPanel.save();" id="editPanel_Save">保存</div>
		`,
		tag:{
			img : `
				<h2>图片(img)</h2>
				<div class="img">
					<label>图片(src)</label>
					<a id="preview_img_a" href="{src}" target="_black"><img id="preview_img" src="{src}"></a>
					<input type="file" style="display:none;" id="HtmlVisualEditor_img_input_file" value="" />
					<input type="text" name="src" id="HtmlVisualEditor_img_src" value="{src}" />
					<span onclick="HtmlVisualEditor.editPanel.uploadImage();" class="upload">上传</span>
				</div>
				<div><label>说明(alt)</label><input type="text" name="alt" value="{alt}" /></div>
			`,
			a : `
				<h2>超链接(a)</h2>
				<div><label>文字</label><input type="text" name="text" value="{text}" /></div>
				<div><label>链接(href)</label><input type="text" name="href" value="{href}" /></div>
			`,
		},
		css:{
			backgroundImage: `
				<h2>背景图片(background-image)</h2>
				<div>
					<label>图片(src)</label>
					<a href="{src}" target="_black"><img id="preview_img" src="{src}" style="height:30px;"></a>
					<input type="file" style="display:none;" id="HtmlVisualEditor_img_input_file" value="" />
					<input type="text" name="style.backgroundImage" id="HtmlVisualEditor_img_src" value="{src}" />
					<span onclick="HtmlVisualEditor.editPanel.uploadImage();" style="border-style: groove; padding-left: 10px;padding-right: 10px;cursor: pointer;">上传</span>
				</div>
			`
		},
		//触发后可后上传图片
		uploadImage:function(){
			if(HtmlVisualEditor.config.uploadImageApi.length == 0){
				msg.alert('未开启图片上传功能！您可配置 HtmlVisualEditor.config.uploadImageApi 上传接口来启用上传功能');
				return;
			}

			
			var input = document.getElementById('HtmlVisualEditor_img_input_file');
			// 给文件输入框添加改变事件，获取选择的文件并上传
			input.addEventListener("change", function() {
				

			  /*
			  // 获取选择的文件对象
			  var fileObj = input.files[0];
			  // 创建一个表单数据对象
			  var formData = new FormData();
			  // 将文件对象添加到表单数据对象中，键名为upload
			  formData.append("file", fileObj);
			  // 创建一个XMLHttpRequest对象
			  var xhr = new XMLHttpRequest();
			  // 设置请求方法和地址
			  xhr.open("POST", HtmlVisualEditor.config.uploadImageApi);
			  // 发送请求
			  xhr.send(formData);
			*/
			  msg.loading('上传中');
			  request.upload(
			  		HtmlVisualEditor.config.uploadImageApi,
			  		//{"source":"HtmlVisualEditor"},
			  		{},
			  		input.files[0],
			  		function(data){
						msg.close();
						if(data.result == 1){
							msg.success('上传成功');
							//input 输入框中的值
							document.getElementById('HtmlVisualEditor_img_src').value = data.url;
							//输入框边上的预览图片的小图
							document.getElementById('preview_img').src = data.url;
							//输入框边上的预览图片的小图-的超链接url
							document.getElementById('preview_img_a').href = data.url;

						}else{
							msg.alert(data.info);
						}
			  		},
			  		{},
			  		function(xhr){
			  			msg.close();
			  			msg.alert('响应异常');
			  			console.log(xhr);
			  		}
			  );

			});
			input.click();




			//var file = input.files[0]
			//request.upload('upload.json',{}, input.files[0]);
		},
		save:function(){
			var form = wm.getJsonObjectByForm($('#HtmlVisualEditor_EditPanel'));
			console.log(form);
			if(HtmlVisualEditor.editPanel.current == null){
				msg.alert('error');
				return;
			}

			for (var key in form) {
				console.log(key + ": " + form[key]); // 输出 name: Alice 和 age: 20
				if(typeof(HtmlVisualEditor.editPanel.current[key]) != 'undefined'){
					//是html本身的属性，那么直接赋予
					HtmlVisualEditor.editPanel.current[key] = form[key];
				}else if(key.indexOf('style.backgroundImage') > -1){
					//是css的属性 - 设置背景图
					HtmlVisualEditor.editPanel.current.style.backgroundImage = "url('"+form[key]+"')";
				}
			}
			if(typeof(form['text']) != 'undefined'){
				HtmlVisualEditor.editPanel.current.innerHTML = form['text'];
			}

		},
		//删除当前选定的元素
		remove:function(){
			if(HtmlVisualEditor.editPanel.current == null){
				return;
			}

			HtmlVisualEditor.editPanel.current.remove();
		},


	},
	//初始化，比如加载js依赖
	init:function(){
		
		//加载JS
		if(typeof($) == 'undefined'){
			HtmlVisualEditor.util.syncLoadJs('js/jquery-2.1.4.js');
		}
		if(typeof(wm) == 'undefined'){
			HtmlVisualEditor.util.syncLoadJs('wm/wm.js');
		}
		if(typeof(request) == 'undefined'){
			HtmlVisualEditor.util.syncLoadJs('request/request.js');
		}
		if(typeof(msg) == 'undefined'){
			HtmlVisualEditor.util.syncLoadJs('msg/msg.js');
		}
		
		//加载CSS
		HtmlVisualEditor.util.loadCss('./HtmlVisualEditor.css');
	}

};

HtmlVisualEditor.init();