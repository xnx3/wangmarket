package com.xnx3.wangmarket.plugin.learnExample;
import com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister;

/**
 * 插件学习入门示例。这个便是将插件注册进网市场云建站系统中，让某个指定的管理后台中，出现这个插件的入口菜单。
 * @author 管雷鸣
 */
@PluginRegister(id="learnExample" , menuTitle = "入门示例",menuHref="../../plugin/learnExample/index.do", applyToSuperAdmin=true)
public class Plugin{
	
}