<%@page import="com.xnx3.wangmarket.admin.entity.TemplatePage"%>
<%@page import="com.xnx3.wangmarket.admin.entity.Site"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="通用标签_动态标签_网市场模版标签"/>
</jsp:include>
	<link href="${STATIC_RESOURCE_PATH}template/doc1/css/style.css" rel="stylesheet">
<body style="">
	<div class="layui-main site-inline doc1dakuang" style="">

		<div class="site-content markdown-body editormd-html-preview" id="content" style="box-sizing: border-box;">
			<h1 id="iw_title" style="">首页模版</h1>
			<h2 id="h2-u8BF4u660E">
				<a name="说明" class="reference-link"></a><span class="header-link octicon octicon-link"></span>说明
			</h2>
			<p>
				首页模版类型的模板页面，在每个网站中只能存在一个！为当前网站首页的模版。<br>同样，首页模板也是必须存在的，不然访问网址的时候，是要显示拿个页面呢。
			</p>
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
		</div>
	</div>
</body>