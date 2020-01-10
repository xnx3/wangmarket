package com.xnx3.wangmarket.plugin.learnExample;

import com.xnx3.j2ee.pluginManage.PluginRegister;

/**
 * 插件学习入门示例。这个便是将插件注册进网市场云建站系统中，让某个指定的管理后台中，出现这个插件的入口菜单。
 * @author 管雷鸣
 */
@PluginRegister(version = "1.2", menuTitle = "插件开发示例",intro="适用于网市场云建站系统v5.0及以后的版本。", menuHref="/plugin/learnExample/index.do", applyToSuperAdmin=true, versionMin="5.0")
public class Plugin{}