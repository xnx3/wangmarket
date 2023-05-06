//采用 https://github.com/xnx3/HtmlVisualEditor 进行html可视化编辑
edit.extend.HtmlVisualEditor = {
	//初始化设置，在当前js载入完成时自动执行init方法进行初始化（比如加载一些外部js、css等）
	init(){
		//加载HtmlVisualEditor 相关支持包
		wm.load.synchronizesLoadJs('/plugin/HtmlVisualEditor/HtmlVisualEditor/HtmlVisualEditor.js');
		wm.load.css('/plugin/HtmlVisualEditor/HtmlVisualEditor/HtmlVisualEditor.css');
		HtmlVisualEditor.document.iframeId = 'iframe';
		HtmlVisualEditor.config.uploadImageApi = 'https://api.kefu.leimingyun.com/kefu/chat/file/uploadImage.json';
		//HtmlVisualEditor.config.resBasePath = './';
		//HtmlVisualEditor.document.setHtml('<html><head><meta http-equiv="content-security-policy" content="script-src \'none\'"></head><body>'+document.getElementById('cont').innerHTML+'</body></html>');
		//HtmlVisualEditor.life.loadSupportFileFinsh();
		//HtmlVisualEditor.life.loadFinish();
	},
	
	/*
	 * 进入使用这种编辑模式进行编辑
	 * html 要编辑时，其中默认的html
	 */ 
	edit:function(html){
		htmls = html;
		HtmlVisualEditor.document.setHtml(html);
	},
	//获取当前编辑的html的内容
	html:function(){
		return HtmlVisualEditor.document.getHtml();
	}
}
