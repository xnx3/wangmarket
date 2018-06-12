package com.xnx3.wangmarket.formManage;

import com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister;

/**
 * 将此万能表单插件注册到网市场中的网站管理后台中
 * @author 管雷鸣
 *
 */
@PluginRegister(id="formManage" , menuTitle = "表单反馈",menuHref="../../form/list.do", applyToCMS=true, applyToPC=false, applyToWAP=false)
public class Plugin{
	
}