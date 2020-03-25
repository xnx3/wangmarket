<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>通用标签_动态标签_网市场模版标签</title>
	<link href="${STATIC_RESOURCE_PATH}module/editor/css/editormd.css" rel="stylesheet">
</head>
<body style="">
	<div class="layui-main site-inline doc1dakuang" style="">
		<div class="site-content markdown-body editormd-html-preview" id="content" style="box-sizing: border-box;">
			<h1 id="iw_title" style="">通用标签</h1>
			<h2 id="h2-u9002u7528u8303u56F4">
				<a name="适用范围" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>适用范围
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
						<td>√ <a href="/templateTag/home.do">首页</a></td>
						<td>√ <a href="/templateTag/list.do">列表页</a></td>
						<td>√<a href="/templateTag/details.do">详情页</a></td>
					</tr>
					<tr>
						<td>√ <a href="/templateTag/var.do">模板变量</a></td>
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
						<td>{site.name}</td>
						<td>网站名称</td>
						<td>字符串</td>
						<td>${site.name}</td>
					</tr>
					<tr>
						<td>{site.id}</td>
						<td>网站编号</td>
						<td>整数</td>
						<td>${site.id}</td>
					</tr>
					<tr>
						<td>{site.domain}</td>
						<td>网站分配的免费二级域名前缀</td>
						<td>字符串</td>
						<td>${site.domain}</td>
					</tr>
					<tr>
						<td>{site.username}</td>
						<td>网站的联系人姓名</td>
						<td>字符串</td>
						<td>${site.username}</td>
					</tr>
					<tr>
						<td>{site.phone}</td>
						<td>网站的联系人手机</td>
						<td>数字</td>
						<td>${site.phone}</td>
					</tr>
					<tr>
						<td>{site.qq}</td>
						<td>网站的联系人QQ</td>
						<td>数字</td>
						<td>${site.qq}</td>
					</tr>
					<tr>
						<td>{site.address}</td>
						<td>当前网站的联系人办公地址</td>
						<td>字符串</td>
						<td>${site.address}</td>
					</tr>
					<tr>
						<td>{site.companyName}</td>
						<td>当前网站的公司名</td>
						<td>字符串</td>
						<c:if test="${site.companyName == ''}">
							<td>例:阿里巴巴网络技术有限公司</td>
						</c:if>
						<c:if test="${site.companyName != ''}">
							<td>${site.companyName}</td>
						</c:if>
					</tr>
					<tr>
						<td>{linuxTime}</td>
						<td>当前10位Unix时间戳</td>
						<td>整数</td>
						<td>${linuxTime}</td>
					</tr>
					<tr>
						<td>index.html</td>
						<td>网站首页</td>
						<td>URL</td>
						<td>index.html (固定，只要是本系统做的网站，首页一定是这个 )</td>
					</tr>
					<tr>
						<td>{masterSiteUrl}</td>
						<td>调取你主站(管理后台)的域名</td>
						<td>URL</td>
						<td><a href="${masterSiteUrl}">${masterSiteUrl}</a></td>
					</tr>
					<tr>
						<td>{templatePath}</td>
						<td>调取模版资源文件的url前缀，开发模版使用。<a href="/templateTag/resource.do">详细点此查看</a></td>
						<td>URL</td>
						<td><a href="${templatePath }">${templatePath }</a></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>