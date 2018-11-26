package com.xnx3.wangmarket.plugin.formManage;

import com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister;

/**
 * 将此万能表单插件注册到网市场中的CMS模式网站管理后台中
 * @author 管雷鸣
 *
 */
@PluginRegister(id="formManage", version="1.0", menuTitle = "表单反馈",menuHref="../../form/list.do", applyToCMS=true, intro="万能表单反馈系统，让您的网站快速拥有表单提交功能，可以用来做留言、活动预约……等等，字段多少无限制")
public class Plugin{
	
}