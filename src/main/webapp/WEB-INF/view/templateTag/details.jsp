<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>详情页模板_模板页面_网市场模版标签</title>
	<link href="${STATIC_RESOURCE_PATH}module/editor/css/editormd.css" rel="stylesheet">
</head>
<body style="">
	<div class="layui-main site-inline doc1dakuang" style="">
		<div class="site-content markdown-body editormd-html-preview" id="content" style="box-sizing: border-box;">
			<h1 id="iw_title" style="">详情页模版</h1>
			<h2 id="h2-u8BF4u660E">
				<a name="说明" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>
				说明
			</h2>
			<p>详情页模版类型的模板页面，在一个网站可存在零个或多个。如产品详情、新闻详情等，不同的内容页面展示形式，会用到不同的详情页模版</p>
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
			<h5 id="h5-u8BE6u60C5u9875u72ECu6709u6807u7B7E">
				<a name="详情页独有标签" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>
				详情页独有标签
			</h5>
			<table>
				<thead>
					<tr>
						<th>标签</th>
						<th>说明</th>
						<th>类型</th>
						<th>调出值（示例）</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><span>{</span>upPage}</td>
						<td>上一篇文章。如果没有上一篇文章时，则是返回列表页面</td>
						<td>HTML</td>
						<td><code>&lt;a href="#"&gt;这是文章的标题&lt;/a&gt;</code></td>
					</tr>
					<tr>
						<td><span>{</span>nextPage}</td>
						<td>下一篇文章。适用范围同上</td>
						<td>HTML</td>
						<td><code>&lt;a href="#"&gt;这是文章的标题&lt;/a&gt;</code></td>
					</tr>
					<tr>
						<td><span>{</span>upPageUrl}</td>
						<td>上一篇文章的链接地址。适用范围同上</td>
						<td>URL</td>
						<td>1234.html</td>
					</tr>
					<tr>
						<td><span>{</span>nextPageUrl}</td>
						<td>下一篇文章的链接地址。适用范围同上</td>
						<td>URL</td>
						<td>1236.html</td>
					</tr>
					<tr>
						<td>
							<del>
								<span>{</span>text}
							</del>
						</td>
						<td>文章内容。v4.3版本以后，推荐使用<span>{</span>news.text}</td>
						<td>HTML</td>
						<td><code>&lt;p&gt;我是内容&lt;/p&gt;</code></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>