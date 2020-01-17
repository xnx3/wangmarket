package com.xnx3.wangmarket.plugin.form;

import com.xnx3.j2ee.pluginManage.PluginRegister;

/**
 * 将此万能表单插件注册到网市场中的CMS模式网站管理后台中
 * @author 管雷鸣
 *
 */
@PluginRegister(version="1.2.0", menuTitle = "表单反馈",menuHref="/plugin/form/list.do", applyToCMS=true, intro="万能表单反馈系统，让您的网站快速拥有表单提交功能，可以用来做留言、活动预约……等等，字段多少无限制", versionMin="5.0")
public class Plugin{
}