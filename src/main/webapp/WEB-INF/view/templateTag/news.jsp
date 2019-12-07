<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>文章信息标签_动态标签_网市场模版标签</title>
	<link href="${STATIC_RESOURCE_PATH}module/editor/css/editormd.css" rel="stylesheet">
</head>
<body style="">
	<div class="layui-main site-inline doc1dakuang" style="">
		<div class="site-content markdown-body editormd-html-preview" id="content" style="box-sizing: border-box;">
			<h1 id="iw_title" style="">文章信息标签</h1>
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
						<td>√<a href="/templateTag/details.do">详情页</a></td>
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
						<td>{news.id}</td>
						<td>文章编号</td>
						<td>整数</td>
						<td>${news.id}</td>
					</tr>
					<tr>
						<td>{news.title}</td>
						<td>文章的标题</td>
						<td>字符串</td>
						<td>${news.title}</td>
					</tr>
					<tr>
						<td>{news.titlepic}</td>
						<td>文章的列表图</td>
						<td>URL</td>
						<td>例：<a href="http://cdn.weiunity.com/res/glyph-icons/world.png">http://cdn.weiunity.com/res/glyph-icons/world.png</a></td>
					</tr>
					<tr>
						<td>{news.intro}</td>
						<td>文章的简介</td>
						<td>字符串</td>
						<td>${news.intro}</td>
					</tr>
					<tr>
						<td>{news.url}</td>
						<td>该文章页面的链接地址</td>
						<td>URL</td>
						<td>1234.html</td>
					</tr>
					<tr>
						<td>{news.cid}</td>
						<td>该文章所属栏目的编号</td>
						<td>整数</td>
						<td>${news.cid}</td>
					</tr>
					<tr>
						<td>{news.text}</td>
						<td>文章内容</td>
						<td>HTML</td>
						<td>（文章的内容详情）</td>
					</tr>
					<tr>
						<td>{news.extend.photos}</td>
						<td>文章图集</td>
						<td>字符串</td>
						<td>（json格式的字符串，需要你自己用js在前端将其分解）</td>
					</tr>
					<tr>
						<td>{news.extend.???}</td>
						<td>自定义扩展标签。<a href="/templateTag/extend.do"
							target="_black">点此查看更多</a></td>
						<td>不限</td>
						<td>根据自己需求随意扩展</td>
					</tr>
					<tr>
						<td>{news.addtime}</td>
						<td>发布时间</td>
						<td>字符串</td>
						<td><x:time linuxTime="${news.addtime}" format="yyyy-MM-dd"></x:time></td>
					</tr>
					<tr>
						<td>{news.addtime.year}</td>
						<td>发布时间-年</td>
						<td>整数</td>
						<td>例：2019</td>
					</tr>
					<tr>
						<td>{news.addtime.month}</td>
						<td>发布时间-月</td>
						<td>整数</td>
						<td>例：09</td>
					</tr>
					<tr>
						<td>{news.addtime.day}</td>
						<td>发布时间-日</td>
						<td>整数</td>
						<td>例：03</td>
					</tr>
					<tr>
						<td>{news.addtime.hour}</td>
						<td>发布时间-时</td>
						<td>整数</td>
						<td>例：10</td>
					</tr>
					<tr>
						<td>{news.addtime.minute}</td>
						<td>发布时间-分</td>
						<td>整数</td>
						<td>例：23</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>