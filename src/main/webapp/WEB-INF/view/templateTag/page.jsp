<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>分页标签_模板页面_网市场模版标签</title>
	<link href="${STATIC_RESOURCE_PATH}module/editor/css/editormd.css" rel="stylesheet">
</head>
<body style="">
	<div class="layui-main site-inline doc1dakuang" style="">
		<div class="site-content markdown-body editormd-html-preview" id="content" style="box-sizing: border-box;">
			<h1 id="iw_title" style="">分页标签</h1>

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
						<td>√<a href="/templateTag/list.do">列表页</a></td>
					</tr>
				</tbody>
			</table>
			<h2 id="h2-u6807u7B7Eu5217u8868">
				<a name="标签列表" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>标签列表
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
						<td>{page.allRecordNumber}</td>
						<td>列表总共有多少条信息</td>
						<td>整数</td>
						<td>100</td>
					</tr>
					<tr>
						<td>{page.currentPageNumber}</td>
						<td>当前第几页，当前的页数</td>
						<td>整数</td>
						<td>3</td>
					</tr>
					<tr>
						<td>{page.lastPageNumber}</td>
						<td>最后一页的页码、共有多少页</td>
						<td>整数</td>
						<td>10</td>
					</tr>
					<tr>
						<td>{page.firstPage}</td>
						<td>首页,第一页的链接地址</td>
						<td>URL</td>
						<td>project.html</td>
					</tr>
					<tr>
						<td>{page.upPage}</td>
						<td>上一页的超链接</td>
						<td>URL</td>
						<td>project_2.html</td>
					</tr>
					<tr>
						<td>{page.nextPage}</td>
						<td>下一页的链接地址</td>
						<td>URL</td>
						<td>project_4.html</td>
					</tr>
					<tr>
						<td>{page.lastPage}</td>
						<td>尾页，最后一页的链接地址</td>
						<td>URL</td>
						<td>project_10.html</td>
					</tr>
					<tr>
						<td>{page.haveUpPage}</td>
						<td>是否有上一页</td>
						<td>Boolean</td>
						<td>true</td>
					</tr>
					<tr>
						<td>{page.haveNextPage}</td>
						<td>是否有下一页</td>
						<td>Boolean</td>
						<td>false</td>
					</tr>
					<tr>
						<td>{page.upList}</td>
						<td>上几页的页码点击跳转</td>
						<td>HTML</td>
						<td>包含在li中输出，输出多个li标签</td>
					</tr>
					<tr>
						<td>{page.nextList}</td>
						<td>下几页的页码点击跳转</td>
						<td>HTML</td>
						<td>包含在li中输出，输出多个li标签</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>