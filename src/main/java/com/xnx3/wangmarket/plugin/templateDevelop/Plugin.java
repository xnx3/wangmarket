package com.xnx3.wangmarket.plugin.templateDevelop;

import com.xnx3.j2ee.pluginManage.PluginRegister;

/**
 * CMS模式模版开发插件。只是适用于本地开发模版使用，仅限localhost或者 127.0.0.1请求时可用
 * @author 管雷鸣
 */
@PluginRegister(menuTitle = "模版开发",menuHref="../../plugin/templateDevelop/index.do", applyToCMS=true, intro="模版开发人员使用，必须本地使用localhost才可以使用此插件", version="1.2.0", versionMin="5.0")
public class Plugin{
	
}