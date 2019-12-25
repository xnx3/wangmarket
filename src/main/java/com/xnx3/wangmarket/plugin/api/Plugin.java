package com.xnx3.wangmarket.plugin.api;

import com.xnx3.j2ee.pluginManage.PluginRegister;

/**
 * 将API接口插件注册到网市场中的代理后台、总管理后台中
 * @author 管雷鸣
 *
 */
@PluginRegister(version="1.2.0", menuTitle = "API接口",menuHref="/plugin/api/index.do", applyToAgency=true, applyToSuperAdmin=true, intro="提供api接口，如开通网站等，让网市场云建站系统轻松嵌入你原本的系统之中。")
public class Plugin{
}