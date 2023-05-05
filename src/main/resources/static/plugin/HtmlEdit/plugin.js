//wangmarket 6.2 之前的版本的可视化都是用的这种
edit.extend.HtmlEdit = {
	init:function(){
		//加载相关支持文件
		//wm.load.synchronizesLoadJs('/plugin/HtmlEdit/HtmlEdit/htmledit.js');
		//wm.load.css('/plugin/HtmlEdit/HtmlEdit/htmledit.css');
	},
	/*
	 * 进入使用这种编辑模式进行编辑
	 * html 要编辑时，其中默认的html
	 */ 
	edit:function(html){
		if(html.indexOf('</head>') > -1 && html.indexOf('<!--XNX3HTMLEDIT-->') == -1){
			html = html.replace('</head>', '<!--XNX3HTMLEDIT-->'+
					'<script>var masterSiteUrl = \''+masterSiteUrl+'\'; var htmledit_upload_url=\'/template/uploadImage.do\'; </script>'+
					'<script src="https://res.zvo.cn/js/jquery-2.1.4.js"></script>'+
					'<script src="https://res.zvo.cn/module/layer/layer.js"></script>'+
					'<script src="/plugin/HtmlEdit/HtmlEdit/htmledit.js"></script>'+
					'</head>');
		}
		
		var o = document.getElementById("iframe");
		ed = document.all ? o.contentWindow.document : o.contentDocument;
		ed.open();
		ed.write(html);
		ed.close();
	},
	//获取当前编辑的html的内容
	html:function(){
		return $(window.parent.document).contents().find("#iframe")[0].contentWindow.xnx3_getHtmlSource();
	}
}