<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.shiro.ShiroFunc"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="/wm/common/head.jsp">
	<jsp:param name="title" value="newsSearch"/>
</jsp:include>
<link href="https://res.zvo.cn/module/editor/css/editormd.css" rel="stylesheet">
<style>
.editormd-preview-container, .editormd-html-preview{
	box-sizing: border-box;
}
</style>

<div class="site-content markdown-body editormd-html-preview" id="content" style="">
    <h1 id="iw_title" style="">网站中的站内文章搜索</h1>
    
<p>在模板中，你可以复制以下代码到您的某个模版页面中，让你的网站快速拥有全站文章搜索功能。</p>
<h2 id="h2-u6A21u7248u4E2Du8981u590Du5236u7684u4EE3u7801"><a name="模版中要复制的代码" class="reference-link"></a><span class="header-link octicon octicon-link"></span>模版中要复制的代码</h2><p>您可将下面代码直接复制到你的某个模版页面中，此模版页面便拥有了站内文章搜索功能！（本页面拥有搜索功能）</p>
<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="pun">关键词：&lt;</span><span class="pln">input type</span><span class="pun">=</span><span class="str">"input"</span><span class="pln"> id</span><span class="pun">=</span><span class="str">"newsSearchKeyword"</span><span class="pln"> name</span><span class="pun">=</span><span class="str">"title"</span><span class="pln"> value</span><span class="pun">=</span><span class="str">""</span><span class="pln"> placeholder</span><span class="pun">=</span><span class="str">"请输入要搜索的关键词，搜索当前网站中，所有标题中含义此关键词的文章"</span><span class="pln"> </span><span class="pun">/&gt;</span></code></li><li class="L1"><code><span class="pun">&lt;</span><span class="pln">button onclick</span><span class="pun">=</span><span class="str">"requestNewsSearchApi(1);"</span><span class="pun">&gt;搜索&lt;/</span><span class="pln">button</span><span class="pun">&gt;</span></code></li><li class="L2"><code><span class="pun">&lt;!--</span><span class="pln"> </span><span class="pun">搜索结果，</span><span class="pln">ajax</span><span class="pun">获取到数据后，会在这个</span><span class="pln">div</span><span class="pun">里面列出来。这个</span><span class="pln">div</span><span class="pun">内是列表中某条文章的显示模版</span><span class="pln"> </span><span class="pun">--&gt;</span></code></li><li class="L3"><code><span class="pun">&lt;</span><span class="pln">div id</span><span class="pun">=</span><span class="str">"searchResultList"</span><span class="pln"> style</span><span class="pun">=</span><span class="str">"display:none;"</span><span class="pun">&gt;</span></code></li><li class="L4"><code><span class="pln">    </span><span class="pun">&lt;</span><span class="pln">a href</span><span class="pun">=</span><span class="str">"news.id.html"</span><span class="pln"> target</span><span class="pun">=</span><span class="str">"_black"</span><span class="pln"> </span><span class="kwd">class</span><span class="pun">=</span><span class="str">"xnx3_news_item"</span><span class="pun">&gt;</span><span class="pln">news</span><span class="pun">.</span><span class="pln">title </span><span class="pun">[</span><span class="pln">news</span><span class="pun">.</span><span class="pln">addtime</span><span class="pun">]&lt;/</span><span class="pln">a</span><span class="pun">&gt;</span></code></li><li class="L5"><code><span class="pun">&lt;/</span><span class="pln">div</span><span class="pun">&gt;</span></code></li><li class="L6"><code><span class="str">&lt;script&gt;</span></code></li><li class="L7"><code><span class="pln">    </span><span class="kwd">var</span><span class="pln"> siteid </span><span class="pun">=</span><span class="pln"> </span><span class="pun">{</span><span class="pln">site</span><span class="pun">.</span><span class="pln">id</span><span class="pun">};</span><span class="pln">    </span><span class="com">//指定网站的编号，无需改动</span></code></li><li class="L8"><code><span class="pln">    </span><span class="kwd">var</span><span class="pln"> masterSiteUrl </span><span class="pun">=</span><span class="pln"> </span><span class="str">'{masterSiteUrl}'</span><span class="pun">;</span><span class="pln">    </span><span class="com">//指定管理后台的url，无需改动</span></code></li><li class="L9"><code><span class="pln">    </span><span class="kwd">var</span><span class="pln"> everyPageNumber </span><span class="pun">=</span><span class="pln"> </span><span class="lit">10</span><span class="pun">;</span><span class="pln">    </span><span class="com">//设定每页显示10条结果。数值可设定2～30之间，可自由设定。</span></code></li><li class="L0"><code><span class="pun">&lt;/</span><span class="pln">script</span><span class="pun">&gt;</span></code></li><li class="L1"><code><span class="pun">&lt;!--</span><span class="pln"> </span><span class="pun">以下固定即可，无需改动</span><span class="pln"> </span><span class="pun">--&gt;</span></code></li><li class="L2"><code><span class="pun">&lt;</span><span class="pln">div id</span><span class="pun">=</span><span class="str">"xnx3_page"</span><span class="pun">&gt;&lt;!--</span><span class="pln"> </span><span class="pun">这里显示分页跳转相关</span><span class="pln"> </span><span class="pun">--&gt;&lt;/</span><span class="pln">div</span><span class="pun">&gt;</span></code></li><li class="L3"><code><span class="pun">&lt;</span><span class="pln">script src</span><span class="pun">=</span><span class="str">"{masterSiteUrl}plugin/newsSearch/js/newsSearch.js"</span><span class="pun">&gt;&lt;/</span><span class="pln">script</span><span class="pun">&gt;</span></code></li></ol></pre><h4 id="h4-u6CE8u610Fu4E8Bu9879"><a name="注意事项" class="reference-link"></a><span class="header-link octicon octicon-link"></span>注意事项</h4><ol>
<li><strong>input</strong> 输入框，以及 <strong>newsSearchKeyword</strong> 这个id是必须存在的，搜索时会从这里取搜索的字符串</li><li>搜索按钮的 onclick 触发的 <strong>requestNewsSearchApi(1);</strong> 其内的1表示获取搜索结果的第一页数据。</li><li><strong>div id=”searchResultList”</strong> 这个div中包含的html，即获取到搜索结果列表后，每一项文章显示的模版。当然，你可以去掉这个div，去掉的话就不会再有搜索结果显示。这个div内支持的动态变量包括：<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="pln">news</span><span class="pun">.</span><span class="pln">id </span><span class="pun">文章的编号，可以用此</span><span class="pln"> </span><span class="pun">+</span><span class="pln"> </span><span class="pun">.</span><span class="pln">html </span><span class="pun">来组合出文章的</span><span class="pln">url</span><span class="pun">地址。如该文章编号为</span><span class="lit">123</span><span class="pun">，</span><span class="pln"> </span><span class="pun">那么</span><span class="pln"> </span><span class="lit">123.html</span><span class="pln"> </span><span class="pun">便是文章的</span><span class="pln">url</span></code></li><li class="L1"><code><span class="pln">news</span><span class="pun">.</span><span class="pln">title </span><span class="pun">文章的标题</span></code></li><li class="L2"><code><span class="pln">news</span><span class="pun">.</span><span class="pln">addtime </span><span class="pun">文章的发布时间，会调取出</span><span class="pln"> yyyy</span><span class="pun">-</span><span class="pln">mm</span><span class="pun">-</span><span class="pln">dd </span><span class="pun">这样格式的时间</span></code></li><li class="L3"><code><span class="pln">news</span><span class="pun">.</span><span class="pln">titlepic </span><span class="pun">文章的封面图片、标题图片。</span></code></li><li class="L4"><code><span class="pln">news</span><span class="pun">.</span><span class="pln">intro </span><span class="pun">文章简介</span></code></li><li class="L5"><code><span class="pln">news</span><span class="pun">.</span><span class="pln">cid </span><span class="pun">文章所属栏目的编号，目前应该用不到，预留功能</span></code></li></ol></pre>例如，使用下面模版代码进行数据调取<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="tag">&lt;div</span><span class="pln"> </span><span class="atn">id</span><span class="pun">=</span><span class="atv">"searchResultList"</span><span class="tag">&gt;</span></code></li><li class="L1"><code><span class="pln">     </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"news.id.html"</span><span class="pln"> </span><span class="atn">target</span><span class="pun">=</span><span class="atv">"_black"</span><span class="pln"> </span><span class="atn">class</span><span class="pun">=</span><span class="atv">"xnx3_news_item"</span><span class="tag">&gt;</span><span class="pln">news.title [news.addtime]</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L2"><code><span class="tag">&lt;/div&gt;</span></code></li></ol></pre>调取出的结果为：<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="tag">&lt;div</span><span class="pln"> </span><span class="atn">id</span><span class="pun">=</span><span class="atv">"searchResultList"</span><span class="tag">&gt;</span></code></li><li class="L1"><code><span class="pln">     </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"12.html"</span><span class="pln"> </span><span class="atn">target</span><span class="pun">=</span><span class="atv">"_black"</span><span class="pln"> </span><span class="atn">class</span><span class="pun">=</span><span class="atv">"xnx3_news_item"</span><span class="tag">&gt;</span><span class="pln">CMS v4.2 版本升级,优化文档、插件机制 [2018-08-05]</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L2"><code><span class="pln">     </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"11.html"</span><span class="pln"> </span><span class="atn">target</span><span class="pun">=</span><span class="atv">"_black"</span><span class="pln"> </span><span class="atn">class</span><span class="pun">=</span><span class="atv">"xnx3_news_item"</span><span class="tag">&gt;</span><span class="pln">CSM v4.1升级,以真正实用建站商盈利为目的建站系统 [2018-06-15]</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L3"><code><span class="pln">     </span><span class="tag">&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"10.html"</span><span class="pln"> </span><span class="atn">target</span><span class="pun">=</span><span class="atv">"_black"</span><span class="pln"> </span><span class="atn">class</span><span class="pun">=</span><span class="atv">"xnx3_news_item"</span><span class="tag">&gt;</span><span class="pln">CMS v3.9 发布,增加模版插件功能 [2018-02-11]</span><span class="tag">&lt;/a&gt;</span></code></li><li class="L4"><code><span class="tag">&lt;/div&gt;</span></code></li></ol></pre></li><li><strong>script</strong> 中设定的几个参数<ol>
<li><strong>siteid</strong> 指定网站的编号，无需改动</li><li><strong>masterSiteUrl</strong> 指定管理后台的url，无需改动</li><li><strong>everyPageNumber</strong> 设定每页显示10条结果。数值可设定2～30之间，可自由设定。如果不设置此参数，则默认显示10条搜索结果</li></ol>
</li><li><strong>div id=”xnx3_page</strong> 这个div中显示分页跳转相关，如首页、上一页、下一页、尾页等。如果没有这个div，则不会显示分页跳转的这些链接。</li></ol>
<h2 id="h2--"><a name="需求示例：点搜索后，跳转到新打开的搜索页面显示结果" class="reference-link"></a><span class="header-link octicon octicon-link"></span>需求示例：点搜索后，跳转到新打开的搜索页面显示结果</h2><p>即输入搜索关键词，点击搜索后，跳转到一个专门的搜索页面，并非在本页显示搜索结果<br>这种情况，就需要进行get传递参数。<br>例如，有这么个需求，首页顶部要放个搜索功能，点击搜索后能跳转到搜索的结果页，显示搜索的结果、或者再次搜索其他关键词。</p>
<h5 id="h5--html-"><a name="第一步:首页加入搜索的html代码" class="reference-link"></a><span class="header-link octicon octicon-link"></span>第一步:首页加入搜索的html代码</h5><p>html代码为：</p>
<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="tag">&lt;input</span><span class="pln"> </span><span class="atn">type</span><span class="pun">=</span><span class="atv">"input"</span><span class="pln"> </span><span class="atn">id</span><span class="pun">=</span><span class="atv">"jumpInput"</span><span class="pln"> </span><span class="atn">value</span><span class="pun">=</span><span class="atv">"CMS"</span><span class="pln"> </span><span class="tag">/&gt;</span></code></li><li class="L1"><code><span class="tag">&lt;button</span><span class="pln"> </span><span class="atn">onclick</span><span class="pun">=</span><span class="atv">"</span><span class="pln">window</span><span class="pun">.</span><span class="pln">location</span><span class="pun">.</span><span class="pln">href</span><span class="pun">=</span><span class="str">'search.html?searchKeyword='</span><span class="pun">+</span><span class="pln">escape</span><span class="pun">(</span><span class="pln">document</span><span class="pun">.</span><span class="pln">getElementById</span><span class="pun">(</span><span class="str">'jumpInput'</span><span class="pun">).</span><span class="pln">value</span><span class="pun">)</span><span class="atv">"</span><span class="tag">&gt;</span><span class="pln">搜索</span><span class="tag">&lt;/button&gt;</span></code></li></ol></pre><p>点击搜索按钮，就会跳转到 search.html 这个搜索页面中去。<br>当然，搜索的关键词，会通过get传递过去，get的参数 searchKeyword 便是搜索的关键词，为了避免乱码，必须使用 js 的 escape 转码，就像上面 html 代码中的。<br>你可以直接将上面代码，复制到你想要放搜索框的位置便可。</p>
<h5 id="h5--"><a name="第二步:增加一个模版页面" class="reference-link"></a><span class="header-link octicon octicon-link"></span>第二步:增加一个模版页面</h5><p>这里增加的模版页面，便是 search.html 这个页面的模版，要显示成什么样子，都是在这个模版页面中进行调节。<br>新增一个模版页面，类型是 “<strong>详情页模版</strong>” ，模版页面增加完毕后，编辑其内容，可将以下内容粘贴进去</p>
<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="dec">&lt;!DOCTYPE html&gt;</span></code></li><li class="L1"><code><span class="tag">&lt;html&gt;</span></code></li><li class="L2"><code><span class="pln">    </span><span class="tag">&lt;head&gt;</span></code></li><li class="L3"><code><span class="pln">        </span><span class="tag">&lt;meta</span><span class="pln"> </span><span class="atn">charset</span><span class="pun">=</span><span class="atv">"utf-8"</span><span class="tag">&gt;</span></code></li><li class="L4"><code><span class="pln">        </span><span class="tag">&lt;title&gt;</span><span class="pln">站内搜索</span><span class="tag">&lt;/title&gt;</span></code></li><li class="L5"><code><span class="pln">    </span><span class="tag">&lt;/head&gt;</span></code></li><li class="L6"><code><span class="tag">&lt;body&gt;</span></code></li><li class="L7"><code><span class="pln">关键词：</span><span class="tag">&lt;input</span><span class="pln"> </span><span class="atn">type</span><span class="pun">=</span><span class="atv">"input"</span><span class="pln"> </span><span class="atn">id</span><span class="pun">=</span><span class="atv">"newsSearchKeyword"</span><span class="pln"> </span><span class="atn">name</span><span class="pun">=</span><span class="atv">"title"</span><span class="pln"> </span><span class="atn">value</span><span class="pun">=</span><span class="atv">""</span><span class="pln"> </span><span class="atn">placeholder</span><span class="pun">=</span><span class="atv">"请输入要搜索的关键词，搜索当前网站中，所有标题中含义此关键词的文章"</span><span class="pln"> </span><span class="tag">/&gt;</span></code></li><li class="L8"><code><span class="tag">&lt;button</span><span class="pln"> </span><span class="atn">onclick</span><span class="pun">=</span><span class="atv">"</span><span class="pln">requestNewsSearchApi</span><span class="pun">(</span><span class="lit">1</span><span class="pun">);</span><span class="atv">"</span><span class="tag">&gt;</span><span class="pln">搜索</span><span class="tag">&lt;/button&gt;</span></code></li><li class="L9"><code><span class="tag">&lt;div</span><span class="pln"> </span><span class="atn">id</span><span class="pun">=</span><span class="atv">"searchResultList"</span><span class="pln"> </span><span class="atn">style</span><span class="pun">=</span><span class="atv">"</span><span class="pln">display</span><span class="pun">:</span><span class="pln">none</span><span class="pun">;</span><span class="atv">"</span><span class="tag">&gt;&lt;a</span><span class="pln"> </span><span class="atn">href</span><span class="pun">=</span><span class="atv">"news.id.html"</span><span class="pln"> </span><span class="atn">target</span><span class="pun">=</span><span class="atv">"_black"</span><span class="pln"> </span><span class="atn">class</span><span class="pun">=</span><span class="atv">"xnx3_news_item"</span><span class="tag">&gt;</span><span class="pln">news.title [news.addtime]</span><span class="tag">&lt;/a&gt;&lt;/div&gt;</span></code></li><li class="L0"><code><span class="tag">&lt;script&gt;</span></code></li><li class="L1"><code><span class="pln">    </span><span class="kwd">var</span><span class="pln"> siteid </span><span class="pun">=</span><span class="pln"> </span><span class="pun">{</span><span class="pln">site</span><span class="pun">.</span><span class="pln">id</span><span class="pun">};</span><span class="pln">    </span><span class="com">//指定网站的编号，无需改动</span></code></li><li class="L2"><code><span class="pln">    </span><span class="kwd">var</span><span class="pln"> masterSiteUrl </span><span class="pun">=</span><span class="pln"> </span><span class="str">'{masterSiteUrl}'</span><span class="pun">;</span><span class="pln">    </span><span class="com">//指定管理后台的url，无需改动</span></code></li><li class="L3"><code><span class="pln">    </span><span class="kwd">var</span><span class="pln"> everyPageNumber </span><span class="pun">=</span><span class="pln"> </span><span class="lit">3</span><span class="pun">;</span><span class="pln">    </span><span class="com">//设定每页显示10条结果。数值可设定2～30之间，可自由设定。</span></code></li><li class="L4"><code><span class="tag">&lt;/script&gt;</span></code></li><li class="L5"><code><span class="tag">&lt;div</span><span class="pln"> </span><span class="atn">id</span><span class="pun">=</span><span class="atv">"xnx3_page"</span><span class="tag">&gt;&lt;/div&gt;</span></code></li><li class="L6"><code><span class="tag">&lt;script</span><span class="pln"> </span><span class="atn">src</span><span class="pun">=</span><span class="atv">"{masterSiteUrl}plugin/newsSearch/js/newsSearch.js"</span><span class="tag">&gt;&lt;/script&gt;</span></code></li><li class="L7"><code><span class="tag">&lt;/body&gt;</span></code></li><li class="L8"><code><span class="tag">&lt;/html&gt;</span></code></li></ol></pre><h6 id="h6-u63D0u793A"><a name="提示" class="reference-link"></a><span class="header-link octicon octicon-link"></span>提示</h6><ol>
<li>这里的代码，跟一开始，本文章最开始贴出的代码，是一样的，就是加了 html头、尾、以及 head 标签而已。</li><li>默认会获取get方式传递来的 searchKeyword 的值作为关键词进行搜索，若未发现这个传入，则不会进行搜索</li></ol>
<h5 id="h5--"><a name="第三步:增加一个栏目" class="reference-link"></a><span class="header-link octicon octicon-link"></span>第三步:增加一个栏目</h5><p>因为第一步是要跳转到 search.html 这个页面，所以我们要增加这个页面。<br>在栏目管理中，增加一个栏目，<br>栏目类型，选择 “<strong>独立页面</strong>” ,<br>内容模版，选上第二步中，增加的模版页面，<br>栏目代码，填入 <strong>search</strong> ，因为在创建页面时，会自动生成 <strong>栏目代码.html</strong> 页面，这里，便可以生成 search.html<br>高级设置中，编辑方式，选上 “ <strong>模版式编辑</strong> ”</p>
<div style="width:100%; text-align:center;"><br>    <img src="http://cdn.weiunity.com/site/491/news/49cfba1bdcfb45c7a17a5767435de933.png" style="width:70%;"><br></div><br><div style="width:100%; text-align:center;"><br>    <img src="http://cdn.weiunity.com/site/491/news/c43c3253549d439eae5fda9ba5a8f0e2.png" style="width:70%;"><br></div>

<h5 id="h5--"><a name="第四步:使用" class="reference-link"></a><span class="header-link octicon octicon-link"></span>第四步:使用</h5><p>经过上面三步，便已经增加好搜索功能了。<br>你可以生成整站，然后试试搜索功能效果如何。别忘记增加几篇文章，避免搜不到东西。（也就是你网站内的栏目，栏目类型要有新闻信息、图文信息，这两种类型的，这两种类型的栏目，其内添加的信息才会被搜索到）。<br>至于 search.html 页面的美观成都，你可以在第二步中，自行对模版页面进行美化。</p>
<h2 id="h2-u622Au56FEu793Au4F8B"><a name="截图示例" class="reference-link"></a><span class="header-link octicon octicon-link"></span>截图示例</h2><div style="width:100%; text-align:center;"><br>    <img src="http://cdn.weiunity.com/site/491/news/5c104f1b47c840a28d838b9ba1e7c9ba.png" style="width:70%;"><br></div>


<h2 id="h2-u5728u7EBFu4F53u9A8C"><a name="在线体验" class="reference-link"></a><span class="header-link octicon octicon-link"></span>在线体验</h2><p>在线体验地址：<br><a href="http://newssearchplugin.wscso.com/">http://newssearchplugin.wscso.com/</a></p>
<h1 id="h1-u63A5u53E3u8BF4u660E"><a name="接口说明" class="reference-link"></a><span class="header-link octicon octicon-link"></span>接口说明</h1><p>若你想自己修改定义 newsSearch.js ，可看以下说明。<br>如果只是使用，请忽略以下即可。</p>
<h2 id="h2-u8BF7u6C42u53C2u6570u8BF4u660E"><a name="请求参数说明" class="reference-link"></a><span class="header-link octicon octicon-link"></span>请求参数说明</h2><table>
<thead>
<tr>
<th>传递的参数</th>
<th>是否必填</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>siteid</td>
<td>必填</td>
<td>网站编号，模版中可使用 {site.id} 调取当前网站的编号</td>
</tr>
<tr>
<td>keyword</td>
<td>必填</td>
<td>要搜索的关键词，搜索当前网站中，所有标题中含义此关键词的文章</td>
</tr>
<tr>
<td>everyPageNumber</td>
<td>非必填</td>
<td>每页显示多少条信息。取值范围限制 2 ～ 30 之间。若不传递此参数，默认每页显示10条</td>
</tr>
<tr>
<td>currentPage</td>
<td>非必填</td>
<td>请求第几页的数据，不传此参数，默认是请求第一页</td>
</tr>
</tbody>
</table>
<h2 id="h2-u8FD4u56DEu503C"><a name="返回值" class="reference-link"></a><span class="header-link octicon octicon-link"></span>返回值</h2><p>返回json格式数据。此数据已在 newsSearch.js 中进行了处理，可以直接用来显示即可。如果你方有更高的要求，可自由组合，将 newsSearch.js 中的js代码复制出来，进行改动即可。</p>
<table>
<thead>
<tr>
<th>参数</th>
<th>参数</th>
<th>数据类型</th>
<th>说明</th>
</tr>
</thead>
<tbody>
<tr>
<td>result</td>
<td></td>
<td>整数</td>
<td>当前搜索请求的结果 0:失败； 1:成功</td>
</tr>
<tr>
<td>info</td>
<td></td>
<td>字符串</td>
<td>若 result=0 失败，这里便是失败原因</td>
</tr>
<tr>
<td>list</td>
<td></td>
<td>json列表</td>
<td>搜索到的信息列表，其中某条信息包含以下字段</td>
</tr>
<tr>
<td></td>
<td>id</td>
<td>整数</td>
<td>信息编号，如 id为 123 ，则此文章的url为 123.html</td>
</tr>
<tr>
<td></td>
<td>title</td>
<td>字符串</td>
<td>标题</td>
</tr>
<tr>
<td></td>
<td>addtime</td>
<td>整数</td>
<td>发布时间，linux 10位时间戳</td>
</tr>
<tr>
<td></td>
<td>titlepic</td>
<td>URL</td>
<td>封面图片，标题图片,图片的绝对路径url</td>
</tr>
<tr>
<td></td>
<td>intro</td>
<td>字符串</td>
<td>内容简介</td>
</tr>
<tr>
<td></td>
<td>cid</td>
<td>整数</td>
<td>该信息所属栏目的编号，此项可能用不到。预留。</td>
</tr>
<tr>
<td>page</td>
<td></td>
<td>json列表</td>
<td>分页相关</td>
</tr>
<tr>
<td></td>
<td>allRecordNumber</td>
<td>整数</td>
<td>总条数</td>
</tr>
<tr>
<td></td>
<td>currentFirstPage</td>
<td>boolean</td>
<td>当前是否是首页，第一页。 true:是 ； false:不是</td>
</tr>
<tr>
<td></td>
<td>currentLastPage</td>
<td>boolean</td>
<td>当前是否是最后一页。 true:是 ； false:不是</td>
</tr>
<tr>
<td></td>
<td>currentPageNumber</td>
<td>整数</td>
<td>当前页面，当前第几页</td>
</tr>
<tr>
<td></td>
<td>everyNumber</td>
<td>整数</td>
<td>每页显示多少条</td>
</tr>
<tr>
<td></td>
<td>firstPage</td>
<td>URL</td>
<td>首页，第一页ajax请求的URL</td>
</tr>
<tr>
<td></td>
<td>haveNextPage</td>
<td>boolean</td>
<td>是否还有下一页。 true:是 ； false:不是</td>
</tr>
<tr>
<td></td>
<td>haveUpPage</td>
<td>boolean</td>
<td>是否还有上一页。 true:是 ； false:不是</td>
</tr>
<tr>
<td></td>
<td>lastPage</td>
<td>URL</td>
<td>尾页URL，最后一页ajax请求的URL</td>
</tr>
<tr>
<td></td>
<td>lastPageNumber</td>
<td>整数</td>
<td>最后一页是编号为几的页数，如最后一页是第三页，这里便是 3</td>
</tr>
<tr>
<td></td>
<td>nextList</td>
<td>json列表</td>
<td>下几页的json列表</td>
</tr>
<tr>
<td></td>
<td>nextPage</td>
<td>URL</td>
<td>下一页URL，下一页ajax请求的URL</td>
</tr>
<tr>
<td></td>
<td>nextPageNumber</td>
<td>整数</td>
<td>下一页的编号。如上一页是第3页，这里便是 3</td>
</tr>
<tr>
<td></td>
<td>upList</td>
<td>json列表</td>
<td>上几页的json列表</td>
</tr>
<tr>
<td></td>
<td>upPage</td>
<td>URL</td>
<td>上一页URL，ajax请求的URL</td>
</tr>
<tr>
<td></td>
<td>upPageNumber</td>
<td>整数</td>
<td>上一页的编号，如上一页是第2页，这里便是 2</td>
</tr>
</tbody>
</table>
<p>下面是每页显示两条信息的返回数据示例</p>
<pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="pun">{</span></code></li><li class="L1"><code><span class="pln">    </span><span class="str">"result"</span><span class="pun">:</span><span class="lit">1</span><span class="pun">,</span></code></li><li class="L2"><code><span class="pln">    </span><span class="str">"info"</span><span class="pun">:</span><span class="str">"成功"</span><span class="pun">,</span></code></li><li class="L3"><code><span class="pln">    </span><span class="str">"list"</span><span class="pun">:[</span></code></li><li class="L4"><code><span class="pln">        </span><span class="pun">{</span></code></li><li class="L5"><code><span class="pln">            </span><span class="str">"titlepic"</span><span class="pun">:</span><span class="str">""</span><span class="pun">,</span></code></li><li class="L6"><code><span class="pln">            </span><span class="str">"id"</span><span class="pun">:</span><span class="lit">8</span><span class="pun">,</span></code></li><li class="L7"><code><span class="pln">            </span><span class="str">"title"</span><span class="pun">:</span><span class="str">"如何正确运用PHPjson_encode函数进行中文转换"</span><span class="pun">,</span></code></li><li class="L8"><code><span class="pln">            </span><span class="str">"addtime"</span><span class="pun">:</span><span class="lit">1538384317</span><span class="pun">,</span></code></li><li class="L9"><code><span class="pln">            </span><span class="str">"intro"</span><span class="pun">:</span><span class="str">"json_encode 和 json_decode这两个函数的具体用法 网上有很多相关的文章 ，本文主要介绍 用json_encode 时中文无法转换的解决方案，本文假设文件所用的编码为gb2312；先写出所需的数组array nbsp39id39 gt 391339nbsp39name39 gt 39乒乓球39nbs"</span><span class="pun">,</span></code></li><li class="L0"><code><span class="pln">            </span><span class="str">"cid"</span><span class="pun">:</span><span class="lit">5</span></code></li><li class="L1"><code><span class="pln">        </span><span class="pun">},</span></code></li><li class="L2"><code><span class="pln">        </span><span class="pun">{</span></code></li><li class="L3"><code><span class="pln">            </span><span class="str">"titlepic"</span><span class="pun">:</span><span class="str">""</span><span class="pun">,</span></code></li><li class="L4"><code><span class="pln">            </span><span class="str">"id"</span><span class="pun">:</span><span class="lit">7</span><span class="pun">,</span></code></li><li class="L5"><code><span class="pln">            </span><span class="str">"title"</span><span class="pun">:</span><span class="str">"Jackson2.6.7发布，高性能JSON处理"</span><span class="pun">,</span></code></li><li class="L6"><code><span class="pln">            </span><span class="str">"addtime"</span><span class="pun">:</span><span class="lit">1538384248</span><span class="pun">,</span></code></li><li class="L7"><code><span class="pln">            </span><span class="str">"intro"</span><span class="pun">:</span><span class="str">"Jackson 267 发布了，下载地址：Source codenbspzipSource codenbsptargz点击查看官方发行说明Jackson 是一个 Java 用来处理 JSON 格式数据的类库，性能非常好。"</span><span class="pun">,</span></code></li><li class="L8"><code><span class="pln">            </span><span class="str">"cid"</span><span class="pun">:</span><span class="lit">5</span></code></li><li class="L9"><code><span class="pln">        </span><span class="pun">}</span></code></li><li class="L0"><code><span class="pln">    </span><span class="pun">],</span></code></li><li class="L1"><code><span class="pln">    </span><span class="str">"page"</span><span class="pun">:{</span></code></li><li class="L2"><code><span class="pln">        </span><span class="str">"limitStart"</span><span class="pun">:</span><span class="lit">0</span><span class="pun">,</span></code></li><li class="L3"><code><span class="pln">        </span><span class="str">"allRecordNumber"</span><span class="pun">:</span><span class="lit">4</span><span class="pun">,</span></code></li><li class="L4"><code><span class="pln">        </span><span class="str">"currentPageNumber"</span><span class="pun">:</span><span class="lit">1</span><span class="pun">,</span></code></li><li class="L5"><code><span class="pln">        </span><span class="str">"everyNumber"</span><span class="pun">:</span><span class="lit">2</span><span class="pun">,</span></code></li><li class="L6"><code><span class="pln">        </span><span class="str">"lastPageNumber"</span><span class="pun">:</span><span class="lit">2</span><span class="pun">,</span></code></li><li class="L7"><code><span class="pln">        </span><span class="str">"nextPage"</span><span class="pun">:</span><span class="str">"http://localhost:8080/plugin/newsSearch/search.do?currentPage=2"</span><span class="pun">,</span></code></li><li class="L8"><code><span class="pln">        </span><span class="str">"upPage"</span><span class="pun">:</span><span class="str">"http://localhost:8080/plugin/newsSearch/search.do?currentPage=1"</span><span class="pun">,</span></code></li><li class="L9"><code><span class="pln">        </span><span class="str">"lastPage"</span><span class="pun">:</span><span class="str">"http://localhost:8080/plugin/newsSearch/search.do?currentPage=2"</span><span class="pun">,</span></code></li><li class="L0"><code><span class="pln">        </span><span class="str">"firstPage"</span><span class="pun">:</span><span class="str">"http://localhost:8080/plugin/newsSearch/search.do?currentPage=1"</span><span class="pun">,</span></code></li><li class="L1"><code><span class="pln">        </span><span class="str">"haveNextPage"</span><span class="pun">:</span><span class="kwd">true</span><span class="pun">,</span></code></li><li class="L2"><code><span class="pln">        </span><span class="str">"haveUpPage"</span><span class="pun">:</span><span class="kwd">false</span><span class="pun">,</span></code></li><li class="L3"><code><span class="pln">        </span><span class="str">"currentLastPage"</span><span class="pun">:</span><span class="kwd">false</span><span class="pun">,</span></code></li><li class="L4"><code><span class="pln">        </span><span class="str">"currentFirstPage"</span><span class="pun">:</span><span class="kwd">true</span><span class="pun">,</span></code></li><li class="L5"><code><span class="pln">        </span><span class="str">"upPageNumber"</span><span class="pun">:</span><span class="lit">1</span><span class="pun">,</span></code></li><li class="L6"><code><span class="pln">        </span><span class="str">"nextPageNumber"</span><span class="pun">:</span><span class="lit">2</span><span class="pun">,</span></code></li><li class="L7"><code><span class="pln">        </span><span class="str">"upList"</span><span class="pun">:[</span></code></li><li class="L8"><code></code></li><li class="L9"><code><span class="pln">        </span><span class="pun">],</span></code></li><li class="L0"><code><span class="pln">        </span><span class="str">"nextList"</span><span class="pun">:[</span></code></li><li class="L1"><code><span class="pln">            </span><span class="pun">{</span></code></li><li class="L2"><code><span class="pln">                </span><span class="str">"href"</span><span class="pun">:</span><span class="str">"http://localhost:8080/plugin/newsSearch/search.do?currentPage=2"</span><span class="pun">,</span></code></li><li class="L3"><code><span class="pln">                </span><span class="str">"pageNumber"</span><span class="pun">:</span><span class="lit">2</span><span class="pun">,</span></code></li><li class="L4"><code><span class="pln">                </span><span class="str">"title"</span><span class="pun">:</span><span class="str">"2"</span></code></li><li class="L5"><code><span class="pln">            </span><span class="pun">}</span></code></li><li class="L6"><code><span class="pln">        </span><span class="pun">]</span></code></li><li class="L7"><code><span class="pln">    </span><span class="pun">}</span></code></li><li class="L8"><code><span class="pun">}</span></code></li></ol></pre></div>


<jsp:include page="/wm/common/foot.jsp"></jsp:include> 
