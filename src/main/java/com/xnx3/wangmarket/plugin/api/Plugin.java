package com.xnx3.wangmarket.plugin.api;

import com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister;

/**
 * 将API接口插件注册到网市场中的代理后台、总管理后台中
 * @author 管雷鸣
 *
 */
@PluginRegister(id="api" , menuTitle = "API接口",menuHref="../../api/index.do", applyToAgency=true, applyToSuperAdmin=true)
public class Plugin{
	
}