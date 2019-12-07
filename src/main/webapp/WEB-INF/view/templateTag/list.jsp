<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>列表页模版_模板页面_网市场模版标签</title>
	<link href="${STATIC_RESOURCE_PATH}module/editor/css/editormd.css" rel="stylesheet">
</head>
<body style="">
	<div class="layui-main site-inline doc1dakuang" style="">
		<div class="site-content markdown-body editormd-html-preview" id="content" style="box-sizing: border-box;">
			<h1 id="iw_title" style="">列表页模版</h1>
			
			<h2 id="h2-u8BF4u660E">
				<a name="说明" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>说明
			</h2>
			<p>列表页模版类型的模板页面，在一个网站中可存在零个或多个。如产品列表、新闻列表等，不同的列表展示形式，会用到不同的列表页模版</p>
			<h2 id="h2-u53EFu7528u6807u7B7E">
				<a name="可用标签" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>
				可用标签
			</h2>
			<h5 id="h5-u53EFu7528u6807u7B7Eu5217u8868">
				<a name="可用标签列表" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>
				可用标签列表
			</h5>
			<table>
				<thead>
						<tr>
							<th>名字</th>
							<th>是否可用</th>
						</tr>
					</thead>
				<tbody>
					<tr>
						<td><a href="/templateTag/var.do">模版变量</a></td>
						<td>可用</td>
					</tr>
					<tr>
						<td><a href="/templateTag/common.do">通用标签</a></td>
						<td>可用</td>
					</tr>
					<tr>
						<td><a href="/templateTag/column.do">栏目标签</a></td>
						<td>不可用</td>
					</tr>
					<tr>
						<td><a href="/templateTag/page.do">分页标签</a></td>
						<td>不可用</td>
					</tr>
					<tr>
						<td><a href="/templateTag/news.do">文章信息标签</a></td>
						<td>不可用</td>
					</tr>
					<tr>
						<td><a href="/templateTag/dynamic.do">动态栏目调用</a></td>
						<td>可用</td>
					</tr>
				</tbody>
			</table>
			<h4 id="h4-u8BE6u60C5u9875u72ECu6709u6807u7B7E"><a name="详情页独有标签" class="reference-link"></a><span class="header-link octicon octicon-link"></span>详情页独有标签</h4><p>将列表页面显示的文章列表调取出：</p>
			<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="com">&lt;!--TemplateListItemStart--&gt;</span></code></li><li class="L1"><code><span class="pln">    这里面可使用文章信息标签、栏目标签</span></code></li><li class="L2"><code><span class="pln">    </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"{news.url}"</span><span class="tag">&gt;</span><span class="pln">{news.title}</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L3"><code><span class="com">&lt;!--TemplateListItemEnd--&gt;</span></code></li></ol></pre><p>生成的列表页HTML：</p>
			<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"451.html"</span><span class="tag">&gt;</span><span class="pln">网站正常运行1</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L1"><code><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"452.html"</span><span class="tag">&gt;</span><span class="pln">网站正常运行2</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L2"><code><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"453.html"</span><span class="tag">&gt;</span><span class="pln">网站正常运行3</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L3"><code><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"454.html"</span><span class="tag">&gt;</span><span class="pln">网站正常运行4</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L4"><code><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"455.html"</span><span class="tag">&gt;</span><span class="pln">网站正常运行5</span><span class="tag">&lt;/a&gt;</span></code></li></ol></pre><h4 id="h4-u5176u4ED6u8BF4u660E"><a name="其他说明" class="reference-link"></a><span class="header-link octicon octicon-link"></span>其他说明</h4><p>若在其中使用栏目标签，则调出的是当前这篇文章所属的栏目。如：当前文章在一个二级栏目中，则调出的是二级栏目的属性信息</p>
		</div>
	</div>
</body>