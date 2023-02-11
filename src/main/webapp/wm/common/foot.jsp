<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
</body>
<script type="text/javascript">
//form组件，开启select
layui.use(['form'], function(){
	var form = layui.form;
	//判断是否加载了JQuery，正常情况下，只要引入了head，自然也就引入了JQuery
	if(typeof(jQuery)!="undefined"){
		//页面上有select标签，才会进行绑定
		if(typeof($('select')[0])!="undefined"){
			//绑定iw的orderBy自动排序，当编辑方式发生变动改变
			form.on('select(selectOrderBy)', function (data) {
				var selObj = document.getElementById("selectOrderBy_xnx3_id");
				var xnx3_sel_index = selObj.selectedIndex;
				if(xnx3_sel_index != defaultShow_index){
					var url = selObj.options[xnx3_sel_index].value;
					window.location.href = url;
				}
			});
			
			
		}
		
	}
	
});
</script>

<!-- 国际化支持 -->
<style>
.translateSelectLanguage{
	position: absolute;
    right: 2rem;
    top: 2rem;
    font-size: 1rem;
    padding: 0.3rem;
    padding-left: 0.5rem;
    padding-right: 0.5rem;
    border: 1px solid #C9C9C9;
    background-color: #fff;
    color: #555;
}
</style>
<script>
//你也可以忽略 base 设定的根目录，直接在 extend 指定路径（主要：该功能为 layui 2.2.0 新增）
layui.extend({
	translate: '{/}https://mail_osc.gitee.io/translate_layui/layui_exts/translate/translate' // {/}的意思即代表采用自有路径，即不跟随 base 路径
	//translate: '{/}./layui_exts/translate/translate' // 本地测试可以用这个
})
//使用拓展模块
layui.use(['translate'], function(){
	var translate = layui.translate;
	translate.language.setLocal('chinese_simplified'); //设置本地语种（当前网页的语种）。如果不设置，默认就是 chinese_simplified 简体中文 
	translate.listener.start();	//开启html页面变化的监控，对变化部分会进行自动翻译。注意，这里变化区域，是指使用 translate.setDocuments(...) 设置的区域。如果未设置，那么为监控整个网页的变化
	translate.ignore.id.push('editormd'); //不翻译markdown编辑器。翻译时追加上自己想忽略不进行翻译的id的值，凡是在这里面的，都不进行翻译。
	
	//当页面加载完后执行翻译操作
	window.onload = function () {
		translate.execute();
	};  
});
</script>
</html>