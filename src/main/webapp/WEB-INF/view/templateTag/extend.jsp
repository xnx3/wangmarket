<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>输入模型之字段无限扩展_其他功能_网市场模版标签</title>
	<link href="${STATIC_RESOURCE_PATH}module/editor/css/editormd.css" rel="stylesheet">
</head>
<body style="">
	<div class="layui-main site-inline doc1dakuang" style="">
		<div class="site-content markdown-body editormd-html-preview" id="content" style="box-sizing: border-box;">
			<h1 id="iw_title" style="">输入模型之字段无限扩展</h1>
			<h2 id="h2-u7B80u4ECB"><a name="简介" class="reference-link"></a><span class="header-link octicon octicon-link"></span>简介</h2><p>可以在内容管理中，将内容输入项（字段）无限扩展！</p>
			<h2 id="h2-u8F93u5165u6A21u578Bu4E2Du7684u4F7Fu7528"><a name="输入模型中的使用" class="reference-link"></a><span class="header-link octicon octicon-link"></span>输入模型中的使用</h2><h4 id="h4--"><a name="首先，来看下现有的，文章标题的调用" class="reference-link"></a><span class="header-link octicon octicon-link"></span>首先，来看下现有的，文章标题的调用</h4><pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="tag">&lt;input</span><span class="pln"> </span><span class="atn">type</span><span class="pun">=</span><span class="atv">"text"</span><span class="pln"> </span><span class="atn">name</span><span class="pun">=</span><span class="atv">"title"</span><span class="pln"> </span><span class="atn">value</span><span class="pun">=</span><span class="atv">"{news.title}"</span><span class="tag">&gt;</span></code></li></ol></pre><p>上面这是系统中，已经使用的一个字段，也就是文章的标题。其 input 标签的name为 “title”, 其value为 “{news.title}”,这个 {news.title} 便是调取某篇文章当前的内容显示。当然，如果是没有值，则是空字符串。</p>
			<h4 id="h4-u6269u5C55u5B57u6BB5u4F7Fu7528"><a name="扩展字段使用" class="reference-link"></a><span class="header-link octicon octicon-link"></span>扩展字段使用</h4><h5 id="h5--"><a name="注意事项：" class="reference-link"></a><span class="header-link octicon octicon-link"></span>注意事项：</h5><ol>
			<li>input 标签的 name 必须以 “extend.” 开头，如年龄，则 name=“extend.age”</li><li>input 标签的 value 必须以 {news.extend. 开头，如年龄，则是 value=“{news.extend.age}”</li></ol>
			<h5 id="h5--"><a name="示例：" class="reference-link"></a><span class="header-link octicon octicon-link"></span>示例：</h5><pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="pun">姓名：</span></code></li><li class="L1"><code><span class="pun">&lt;</span><span class="pln">input type</span><span class="pun">=</span><span class="str">"text"</span><span class="pln"> name</span><span class="pun">=</span><span class="str">"extend.username"</span><span class="pln"> value</span><span class="pun">=</span><span class="str">"{news.extend.username}"</span><span class="pun">&gt;</span></code></li><li class="L2"><code></code></li><li class="L3"><code><span class="pun">年龄：</span></code></li><li class="L4"><code><span class="pun">&lt;</span><span class="pln">input type</span><span class="pun">=</span><span class="str">"text"</span><span class="pln"> name</span><span class="pun">=</span><span class="str">"extend.age"</span><span class="pln"> value</span><span class="pun">=</span><span class="str">"{news.extend.age}"</span><span class="pun">&gt;</span></code></li></ol></pre><h4 id="h4-u6A21u7248u4E2Du4F7Fu7528u793Au4F8B"><a name="模版中使用示例" class="reference-link"></a><span class="header-link octicon octicon-link"></span>模版中使用示例</h4><p><strong>{news.extend.???}</strong> 隶属于文章标签，是文章中的一个扩展标签。在模版中使用时，可以直接使用 {news.extend.???} 调取文章相关信息。<br>如，上面的示例中，姓名跟年龄，在网站管理后台中-内容管理中，将姓名、年龄录入进去了，那么，可以在模版页面中，可以使用文章标签的地方，使用 {news.extend.username} 、 {news.extend.age} 来将数据调取出来。</p>
		</div>
	</div>
</body>