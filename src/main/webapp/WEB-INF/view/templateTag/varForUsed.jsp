<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>模板变量_模板页面_网市场模版标签</title>
	<link href="${STATIC_RESOURCE_PATH}module/editor/css/editormd.css" rel="stylesheet">
</head>
<body style="">
	<div class="layui-main site-inline doc1dakuang" style="">
		<div class="site-content markdown-body editormd-html-preview" id="content" style="box-sizing: border-box;">
			<h1 id="iw_title" style="">使用说明</h1>

			<h2 id="h2-u8BF4u660E">
				<a name="说明" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>
				说明
			</h2>
			<p>
				当多个模板页面中，有共同的某段内容(或代码)相同时，可以将这段通用的代码拿出来，变为一个模板变量。<br>然后再使用这段代码的模版页面中，使用调取标签：
			</p>
			<blockquote>
				<p>{include=模版变量的名字}</p>
			</blockquote>
			<p>来调取这段代码直接在模板页面中显示出来。</p>
			<h2 id="h2-u53EFu7528u6807u7B7E">
				<a name="可用标签" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>
				可用标签
			</h2>
			<table>
				<thead>
					<tr>
						<th>名字</th>
						<th>是否可用</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>模版变量</td>
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
		</div>
	</div>
</body>