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
			<h1 id="iw_title" style="">动态栏目调用</h1>
			<div class="markdown-toc editormd-markdown-toc">
				<ul class="markdown-toc-list">
					<li><a class="toc-level-2" href="#功能" level="2">功能</a></li>
					<li><a class="toc-level-2" href="#适用范围" level="2">适用范围</a></li>
					<li><a class="toc-level-2" href="#参数说明" level="2">参数说明</a>
						<ul>
							<li><a class="toc-level-6" href="#codeName" level="6">codeName</a></li>
							<li><a class="toc-level-6" href="#number" level="6">number</a></li>
							<li><a class="toc-level-6" href="#List_Start — List_End"
								level="6">List_Start — List_End</a></li>
							<li><a class="toc-level-6" href="#SubColumnList_Start — SubColumnList_End" level="6">SubColumnList_Start — SubColumnList_End</a></li>
						</ul>
					</li>
					<li><a class="toc-level-4" href="#调取某个栏目名字、URL" level="4">调取某个栏目名字、URL</a></li>
					<li><a class="toc-level-4" href="#调取文章列表" level="4">调取文章列表</a>
						<ul>
							<li><a class="toc-level-6" href="#应用案例-调取指定栏目下的文章列表" level="6">应用案例-调取指定栏目下的文章列表</a></li>
						</ul>
					</li>
					<li><a class="toc-level-4" href="#调取栏目列表" level="4">调取栏目列表</a>
						<ul>
							<li><a class="toc-level-6" href="#应用案例1-调取指定父栏目下所有子栏目列表"
								level="6">应用案例1-调取指定父栏目下所有子栏目列表</a></li>
							<li><a class="toc-level-6" href="#应用案例2-调取当前网站中，所有顶级栏目列表" level="6">应用案例2-调取当前网站中，所有顶级栏目列表</a></li>
							<li><a class="toc-level-6" href="#应用案例3-调取指定父栏目下，所有子栏目的列表及每个子栏目的内容列表" level="6">应用案例3-调取指定父栏目下，所有子栏目的列表及每个子栏目的内容列表</a>
							</li>
						</ul>
					</li>
				</ul>
			</div>
			<h2 id="h2-u529Fu80FD">
				<a name="功能" class="reference-link"></a><span
					class="header-link octicon octicon-link"></span>功能
			</h2>
			<ol>
				<li>可调取当前网站的所有一级栏目列表</li>
				<li>可调取某个指定栏目的所有子栏目列表</li>
				<li>可根据栏目代码调取该栏目的属性信息、及该栏目下所有的文章信息列表。</li>
			</ol>
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
				
						<td>√ <a href="/templateTag/home.do">模板页面</a></td>
						<td>√ <a href="/templateTag/home.do">首页</a></td>
						<td>√ <a href="/templateTag/list.do">列表页</a></td>
						<td>√<a href="/templateTag/details.do">详情页</a></td>
					</tr>
					<tr>
						<td>√ <a href="/templateTag/var.do">模板变量</a></td>
					</tr>
				</tbody>
			</table>
			<h2 id="h2-u53C2u6570u8BF4u660E">
				<a name="参数说明" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>
				参数说明
			</h2>
			<h6 id="h6-codename">
				<a name="codeName" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>
				codeName
			</h6>
			<p>
				栏目代码。可在网站管理后台右侧的 “栏目管理” 中查看。<br>
				注意，其中是<strong>没有空格</strong>的！<br>
				使用示例：
			</p>
			<blockquote>
				<p><span>&lt;</span>!—codeName=xinwenzixun—<span>&gt;</span></p>
			</blockquote>
			<p>
				此为指明要调取的是哪个栏目，当指定栏目代码后才能调取该栏目的一些信息。不然它怎么知道你要调取哪个栏目的信息呢。<br>
				提示：栏目代码若具体到某个指定栏目，则获取指定栏目的信息；若是栏目代码填写的是一个大的顶级栏目，其下有很多子栏目，则会调取所有子栏目内的信息。<br>
				其内支持 栏目标签 可在栏目类型的模板页面、详情类型的模板页面中，动态调取栏目信息，如
			</p>
			<blockquote>
				<p><span>&lt;</span>!—codeName={siteColumn.codeName}—<span>&gt;</span></p>
			</blockquote>
			<hr>
				<h6 id="h6-number"><a name="number" class="reference-link"></a><span class="header-link octicon octicon-link"></span>number</h6><p>如果要调取此栏目的文章列表，将文章以列表的形式调取出来，那么这里指明当前要调取出几条文章信息。值为整数型。<br>若在调取文章列表时，不填写值、或者无这个参数配置，则默认显示6条文章<br>此参数是配合 List_Start 一块使用。</p>
			<hr>
				<h6 id="h6-list_start-list_end"><a name="List_Start — List_End" class="reference-link"></a><span class="header-link octicon octicon-link"></span>List_Start — List_End</h6><p>此标签为 调取指定栏目下的文章信息列表。<br>前提：必须配置 codeName 栏目代码。不然它怎么知道要调取哪个栏目下的文章列表呢。<br>配合 number 一块使用。可指定调取多少条。若不配置 number， 则默认调取6条文章列表<br>其内支持 文章信息标签</p>
			<hr>
				<h6 id="h6-subcolumnlist_start-subcolumnlist_end"><a name="SubColumnList_Start — SubColumnList_End" class="reference-link"></a><span class="header-link octicon octicon-link"></span>SubColumnList_Start — SubColumnList_End</h6><p>此标签为 调取指定栏目下的子栏目列表<br>如果配合 codeName 使用，则是调取指定父栏目下的所有子栏目列表<br>如果不加 codeName 参数，则是调取当前网站下所有顶级栏目(一级栏目)列表<br>其内支持 栏目标签</p>
			<hr>
			<h4 id="h4--url"><a name="调取某个栏目名字、URL" class="reference-link"></a><span class="header-link octicon octicon-link"></span>调取某个栏目名字、URL</h4><p>根据栏目代码（codeName）来调取该栏目的名字、URL链接地址。<br>模板中的代码书写示例：</p>
			<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="com">&lt;!--SiteColumn_Start--&gt;</span></code></li><li class="L1"><code><span class="pln">    </span><span class="com">&lt;!--codeName=xinwenzixun--&gt;</span></code></li><li class="L2"><code><span class="pln">    </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"{siteColumn.url}"</span><span class="tag">&gt;</span><span class="pln">{siteColumn.name}</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L3"><code><span class="com">&lt;!--SiteColumn_End--&gt;</span></code></li></ol></pre><hr>
			<h4 id="h4-u8C03u53D6u6587u7AE0u5217u8868"><a name="调取文章列表" class="reference-link"></a><span class="header-link octicon octicon-link"></span>调取文章列表</h4><p>根据栏目代码（codeName）来调取该栏目下的文章列表。<br>当然，也可以是调取某父栏目下，所有的子栏目的文章列表。<br>模板中的代码书写示例：</p>
			<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="com">&lt;!--SiteColumn_Start--&gt;</span></code></li><li class="L1"><code><span class="pln">    </span><span class="com">&lt;!--codeName=xinwenzixun--&gt;</span></code></li><li class="L2"><code><span class="pln">    </span><span class="com">&lt;!--number=6--&gt;</span></code></li><li class="L3"><code><span class="pln">    </span><span class="com">&lt;!--List_Start--&gt;</span></code></li><li class="L4"><code><span class="pln">        这里面可使用 文章标签</span></code></li><li class="L5"><code><span class="pln">        </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"{news.url}"</span><span class="tag">&gt;</span><span class="pln">{news.title}</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L6"><code><span class="pln">    </span><span class="com">&lt;!--List_End--&gt;</span></code></li><li class="L7"><code><span class="com">&lt;!--SiteColumn_End--&gt;</span></code></li></ol></pre><h6 id="h6--"><a name="应用案例-调取指定栏目下的文章列表" class="reference-link"></a><span class="header-link octicon octicon-link"></span>应用案例-调取指定栏目下的文章列表</h6><p>列出栏目代码为 gongsidongtai 的栏目，最新的6条信息</p>
			<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="com">&lt;!--SiteColumn_Start--&gt;</span></code></li><li class="L1"><code><span class="pln">    </span><span class="com">&lt;!--codeName=gongsidongtai--&gt;</span></code></li><li class="L2"><code><span class="pln">    </span><span class="tag">&lt;div&gt;</span></code></li><li class="L3"><code><span class="pln">        </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"{siteColumn.url}"</span><span class="pln"> </span><span class="atn">title</span><span class="pun">=</span><span class="atv">"{siteColumn.name}"</span><span class="tag">&gt;</span><span class="pln">{siteColumn.name}</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L4"><code><span class="pln">        </span><span class="tag">&lt;ul&gt;</span></code></li><li class="L5"><code><span class="pln">    </span><span class="com">&lt;!--number=6--&gt;</span></code></li><li class="L6"><code><span class="pln">    </span><span class="com">&lt;!--List_Start--&gt;</span></code></li><li class="L7"><code><span class="pln">        </span><span class="tag">&lt;li&gt;&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"{news.url}"</span><span class="tag">&gt;</span><span class="pln">{news.title}</span><span class="tag">&lt;/a&gt;&lt;/li&gt;</span></code></li><li class="L8"><code><span class="pln">    </span><span class="com">&lt;!--List_End--&gt;</span></code></li><li class="L9"><code><span class="pln">        </span><span class="tag">&lt;/ul&gt;</span></code></li><li class="L0"><code><span class="pln">    </span><span class="tag">&lt;/div&gt;</span></code></li><li class="L1"><code><span class="com">&lt;!--SiteColumn_End--&gt;</span></code></li></ol></pre><p>生成的HTML：</p>
			<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="tag">&lt;div&gt;</span></code></li><li class="L1"><code><span class="pln">  </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"lc376_1.html"</span><span class="pln"> </span><span class="atn">title</span><span class="pun">=</span><span class="atv">"新闻动态"</span><span class="tag">&gt;</span><span class="pln">新闻动态</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L2"><code><span class="pln">  </span><span class="tag">&lt;ul&gt;</span></code></li><li class="L3"><code><span class="pln">    </span><span class="tag">&lt;li&gt;&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"460.html"</span><span class="tag">&gt;</span><span class="pln">展会策划之超维震撼亮相2014深圳礼品展</span><span class="tag">&lt;/a&gt;&lt;/li&gt;</span></code></li><li class="L4"><code><span class="pln">    </span><span class="tag">&lt;li&gt;&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"460.html"</span><span class="tag">&gt;</span><span class="pln">打造尚品魔芋全新品牌形象</span><span class="tag">&lt;/a&gt;&lt;/li&gt;</span></code></li><li class="L5"><code><span class="pln">    </span><span class="tag">&lt;li&gt;&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"460.html"</span><span class="tag">&gt;</span><span class="pln">喜贺签约佛山赛玛电子科技旗下新品牌策划及VI设计</span><span class="tag">&lt;/a&gt;&lt;/li&gt;</span></code></li><li class="L6"><code><span class="pln">    </span><span class="tag">&lt;li&gt;&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"460.html"</span><span class="tag">&gt;</span><span class="pln">品牌VI设计之“租车在线”品牌部分提案鉴赏</span><span class="tag">&lt;/a&gt;&lt;/li&gt;</span></code></li><li class="L7"><code><span class="pln">    </span><span class="tag">&lt;li&gt;&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"460.html"</span><span class="tag">&gt;</span><span class="pln">品牌策划应邀参加贵阳保得公司品牌发展培训交流会</span><span class="tag">&lt;/a&gt;&lt;/li&gt;</span></code></li><li class="L8"><code><span class="pln">    </span><span class="tag">&lt;li&gt;&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"460.html"</span><span class="tag">&gt;</span><span class="pln">品牌VI设计之王子电下品牌形象策划提案部分鉴赏</span><span class="tag">&lt;/a&gt;&lt;/li&gt;</span></code></li><li class="L9"><code><span class="pln">  </span><span class="tag">&lt;/ul&gt;</span></code></li><li class="L0"><code><span class="tag">&lt;/div&gt;</span></code></li></ol></pre><p>效果：</p>
			<div style="width:100%; text-align:center;"><br><img src="http://cdn.weiunity.com/site/491/news/355a885be55448bd8c0daefdbeada09a.png" style="width:60%;"><br></div>
			<hr>
			<h4 id="h4-u8C03u53D6u680Fu76EEu5217u8868">
				<a name="调取栏目列表" class="reference-link"></a>
				<span class="header-link octicon octicon-link"></span>
				调取栏目列表
			</h4>
			<ol>
				<li>可调取某个指定父栏目下，所有子栏目列表</li>
				<li>可调取整个网站中，所有顶级栏目(一级栏目)列表</li>
			</ol>
			<p>模板代码实现：</p>
			<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="com">&lt;!--SiteColumn_Start--&gt;</span></code></li><li class="L1"><code><span class="pln">    </span><span class="com">&lt;!--codeName=xinwenzixun--&gt;</span></code></li><li class="L2"><code><span class="pln">    </span><span class="com">&lt;!--SubColumnList_Start--&gt;</span></code></li><li class="L3"><code><span class="pln">        这里面支持 栏目标签</span></code></li><li class="L4"><code><span class="pln">        </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"{siteColumn.url}"</span><span class="tag">&gt;</span><span class="pln">{siteColumn.name}</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L5"><code><span class="pln">    </span><span class="com">&lt;!--SubColumnList_End--&gt;</span></code></li><li class="L6"><code><span class="com">&lt;!--SiteColumn_End--&gt;</span></code></li></ol></pre><h6 id="h6--1-"><a name="应用案例1-调取指定父栏目下所有子栏目列表" class="reference-link"></a><span class="header-link octicon octicon-link"></span>应用案例1-调取指定父栏目下所有子栏目列表</h6><p>要调取的目标父栏目：新闻资讯(栏目代码:xinwenzixun)<br>它有两个子栏目：</p>
			<ol>
				<li>公司新闻</li>
				<li>行业资讯</li>
			</ol>
			<p>现根据父栏目的栏目代码（xinwenzixun），调出其下所有子栏目的列表。<br>模板代码示例：</p>
			<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="com">&lt;!--SiteColumn_Start--&gt;</span></code></li><li class="L1"><code><span class="pln">    </span><span class="com">&lt;!--codeName=xinwenzixun--&gt;</span></code></li><li class="L2"><code><span class="pln">    </span><span class="tag">&lt;h2&gt;</span><span class="pln">{siteColumn.name}</span><span class="tag">&lt;/h2&gt;</span></code></li><li class="L3"><code><span class="pln">    </span><span class="com">&lt;!--SubColumnList_Start--&gt;</span></code></li><li class="L4"><code><span class="pln">        </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"{siteColumn.url}"</span><span class="tag">&gt;</span><span class="pln">{siteColumn.name}</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L5"><code><span class="pln">    </span><span class="com">&lt;!--SubColumnList_End--&gt;</span></code></li><li class="L6"><code><span class="com">&lt;!--SiteColumn_End--&gt;</span></code></li></ol></pre><p>生成的HTML：</p>
			<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="tag">&lt;div&gt;</span></code></li><li class="L1"><code><span class="pln">    </span><span class="tag">&lt;h2&gt;</span><span class="pln">新闻资讯</span><span class="tag">&lt;/h2&gt;</span></code></li><li class="L2"><code><span class="pln">    </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"gsxy.html"</span><span class="tag">&gt;</span><span class="pln">公司新闻</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L3"><code><span class="pln">    </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"hyzx.html"</span><span class="tag">&gt;</span><span class="pln">行业资讯</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L4"><code><span class="tag">&lt;/div&gt;</span></code></li></ol></pre><hr>
			<h6 id="h6--2-"><a name="应用案例2-调取当前网站中，所有顶级栏目列表" class="reference-link"></a><span class="header-link octicon octicon-link"></span>应用案例2-调取当前网站中，所有顶级栏目列表</h6><p>同上，只需把 codeName 参数去掉即可。去掉具体栏目的指定，即调取所有顶级栏目列表。<br>模板代码示例：</p>
			<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="com">&lt;!--SiteColumn_Start--&gt;</span></code></li><li class="L1"><code><span class="pln">    </span><span class="com">&lt;!--SubColumnList_Start--&gt;</span></code></li><li class="L2"><code><span class="pln">        </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"{siteColumn.url}"</span><span class="tag">&gt;</span><span class="pln">{siteColumn.name}</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L3"><code><span class="pln">    </span><span class="com">&lt;!--SubColumnList_End--&gt;</span></code></li><li class="L4"><code><span class="com">&lt;!--SiteColumn_End--&gt;</span></code></li></ol></pre><h6 id="h6--3-"><a name="应用案例3-调取指定父栏目下，所有子栏目的列表及每个子栏目的内容列表" class="reference-link"></a><span class="header-link octicon octicon-link"></span>应用案例3-调取指定父栏目下，所有子栏目的列表及每个子栏目的内容列表</h6><pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code></code></li><li class="L1"><code><span class="com">&lt;!--SiteColumn_Start--&gt;</span></code></li><li class="L2"><code><span class="pln">    </span><span class="com">&lt;!--codeName=doc--&gt;</span></code></li><li class="L3"><code><span class="pln">        </span><span class="com">&lt;!--SubColumnList_Start--&gt;</span></code></li><li class="L4"><code><span class="pln">            </span><span class="tag">&lt;h2&gt;</span><span class="pln">{siteColumn.name}</span><span class="tag">&lt;/h1&gt;</span></code></li><li class="L5"><code></code></li><li class="L6"><code><span class="pln">            </span><span class="com">&lt;!--number=30--&gt;</span></code></li><li class="L7"><code><span class="pln">            </span><span class="com">&lt;!--List_Start--&gt;</span></code></li><li class="L8"><code><span class="pln">                </span><span class="tag">&lt;li&gt;&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"{news.url}"</span><span class="tag">&gt;</span><span class="pln">{news.title}</span><span class="tag">&lt;/a&gt;&lt;/li&gt;</span></code></li><li class="L9"><code><span class="pln">            </span><span class="com">&lt;!--List_End--&gt;</span></code></li><li class="L0"><code><span class="pln">    </span><span class="com">&lt;!--SubColumnList_End--&gt;</span></code></li><li class="L1"><code><span class="com">&lt;!--SiteColumn_End--&gt;</span></code></li></ol></pre><p>效果：</p>
			<div style="width:100%; text-align:center;"><br>    <img src="http://cdn.weiunity.com/site/491/news/d0895e2db507432bae3b1336b81a02d5.jpg" style="width:50%;"><br></div>
		</div>
	</div>
</body>