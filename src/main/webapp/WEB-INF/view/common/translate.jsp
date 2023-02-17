<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
</body>
<!-- 普通页面，未引入layui的页面使用 -->
<!-- 国际化支持 -->
<script src="https://res.zvo.cn/translate/translate.js"></script>
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
    display:none;
}
</style>
<script>
translate.language.setLocal('chinese_simplified'); //设置本地语种（当前网页的语种）。如果不设置，默认就是 chinese_simplified 简体中文 
translate.listener.start();	//开启html页面变化的监控，对变化部分会进行自动翻译。注意，这里变化区域，是指使用 translate.setDocuments(...) 设置的区域。如果未设置，那么为监控整个网页的变化

//当页面加载完后执行翻译操作
window.onload = function () {
	translate.execute();
};  
</script>
</html>