package com.xnx3.wangmarket.plugin.newsSearch;
import com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister;

/**
 * 插件学习入门示例。这个便是将插件注册进网市场云建站系统中，让某个指定的管理后台中，出现这个插件的入口菜单。
 * @author 管雷鸣
 */
@PluginRegister(id="newsSearch" , menuTitle = "站内搜索",menuHref="//tag.wscso.com/6740.html", applyToCMS=true, intro="让网站具备搜索功能，整站文章搜索。", version="1.1")
public class Plugin{
	
}