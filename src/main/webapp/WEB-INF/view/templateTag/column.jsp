<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>栏目标签_模板页面_网市场模版标签</title>
	<link href="${STATIC_RESOURCE_PATH}module/editor/css/editormd.css" rel="stylesheet">
</head>
<body style="">
	<div class="layui-main site-inline doc1dakuang" style="">
		<div class="site-content markdown-body editormd-html-preview" id="content" style="box-sizing: border-box;">
			<h1 id="iw_title" style="">栏目标签</h1>
			<h2 id="h2-u9002u7528u8303u56F4">
				<a name="适用范围" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>
				适用范围
			</h2>
			<table>
				<thead>
					<tr>
						<th>功能模块</th>
						<th>分类</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><a href="/templateTag/home.do">模板页面</a></td>
						<td>√ <a href="/templateTag/list.do">列表页</a></td>
					</tr>
				</tbody>
			</table>
			
			<h2 id="h2-u6807u7B7Eu5217u8868">
				<a name="标签列表" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>
				标签列表
			</h2>
			
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
						<td>{siteColumn.id}</td>
						<td>栏目的id</td>
						<td>整数</td>
						<td>${siteColumn.id}</td>
					</tr>
					<tr>
						<td>{siteColumn.name}</td>
						<td>栏目的名字</td>
						<td>字符串</td>
						<td>${siteColumn.name}</td>
					</tr>
					<tr>
						<td>{siteColumn.url}</td>
						<td>栏目的链接地址</td>
						<td>URL</td>
						<td>${siteColumn.url}(绝对路径)</td>
					</tr>
					<tr>
						<td>{siteColumn.type}</td>
						<td>栏目的类型</td>
						<td>整数</td>
						<td>${siteColumn.type}</td>
					</tr>
					<tr>
						<td>{siteColumn.used}</td>
						<td>栏目是否启用/显示</td>
						<td>整数</td>
						<td>${siteColumn.used}</td>
					</tr>
					<tr>
						<td>{siteColumn.codeName}</td>
						<td>栏目代码</td>
						<td>字母</td>
						<td>${siteColumn.codeName}</td>
					</tr>
					<tr>
						<td>{siteColumn.parentCodeName}</td>
						<td>当前栏目的父栏目代码</td>
						<td>字母</td>
						<td>${siteColumn.parentCodeName}如果当前栏目已经是顶级栏目，没有父栏目了，则这里返回的是上面的栏目代码</td>
					</tr>
					<tr>
						<td>{siteColumn.icon}</td>
						<td>栏目图片</td>
						<td>URL</td>
						<td><a href="${siteColumn.icon}">${siteColumn.icon}</a></td>
					</tr>
					<tr>
						<td>{siteColumn.keywords}</td>
						<td>SEO关键字</td>
						<td>字符串</td>
						<td>${siteColumn.keywords}</td>
					</tr>
					<tr>
						<td>{siteColumn.description}</td>
						<td>SEO描述</td>
						<td>字符串</td>
						<td>${siteColumn.description}</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
