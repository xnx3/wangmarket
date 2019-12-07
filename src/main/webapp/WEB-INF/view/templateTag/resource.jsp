<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>静态资源_模板页面_网市场模版标签</title>
	<link href="${STATIC_RESOURCE_PATH}module/editor/css/editormd.css" rel="stylesheet">
</head>
<body style="">
	<div class="layui-main site-inline doc1dakuang" style="">
		<div class="site-content markdown-body editormd-html-preview" id="content" style="box-sizing: border-box;">
			<h1 id="iw_title" style="">模版中js、css的存放</h1>
			<div style="display: none;">
				## 存放方式一(推荐)： 注意，此方式近v4.7及以上版本才可以使用。v4.7以前的版本，请使用第二种方式。
				请点击下载Windows一键运行包 http://www.wang.market/down.html 下载后直接在本地运行起来，使用账号
				wangzhan 密码 wangzhan 登陆网站管理后台 登陆成功后，在左侧功能菜单下，找到 功能插件 - 模版开发
				，点开根据其内步骤即可开发模版
				<div style="text-align: center;">
					<img src="${siteResUrl}site/491/news/0c0ea671b8d14ad9a423eda384b4122f.png" style="width: 70%;">
				</div>
				## 存放方式二： 适用于v4.7以前版本使用。 模版中，通常会引入一些js、css、图片等文件。
				但是网市场云建站系统中，并没有提供这些js、css的上传方式。 你需要将这些资源文件单独上传到你自己的服务器上，比如
				FTP空间、或者阿里云OSS。 在模版中，通过绝对路径，直接引入使用。
			</div>

			<h2 id="h2--">
				<a name="存放方式一(推荐)：" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>
				存放方式一(推荐)：
			</h2>
			<p>
				注意，此方式近v4.7及以上版本才可以使用。v4.7以前的版本，请使用第二种方式。<br>请点击下载Windows一键运行包
				<a href="https://down.leimingyun.com/wangmarket/wangmarket_windows_x64.zip">点击此处下载</a><br>下载后直接在本地运行起来，使用账号
				wangzhan 密码 wangzhan 登陆网站管理后台<br>登陆成功后，在左侧功能菜单下，找到 功能插件 - 模版开发
				，点开根据其内步骤即可开发模版
			</p>
			<div style="text-align: center;">
				<br><img src="http://cdn.weiunity.com/site/491/news/0c0ea671b8d14ad9a423eda384b4122f.png" style="width: 70%;"><br>
			</div>

			<h2 id="h2--">
				<a name="存放方式二：" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>
				存放方式二：
			</h2>
			<p>
				适用于v4.7以前版本使用。<br>模版中，通常会引入一些js、css、图片等文件。<br>但是网市场云建站系统中，并没有提供这些js、css的上传方式。<br>你需要将这些资源文件单独上传到你自己的服务器上，比如
				FTP空间、或者阿里云OSS。<br>在模版中，通过绝对路径，直接引入使用。
			</p>
		</div>
	</div>
</body>