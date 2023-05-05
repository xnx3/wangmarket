package com.xnx3.wangmarket.plugin.HtmlVisualEditor;

import javax.servlet.http.HttpServletRequest;
import com.xnx3.j2ee.pluginManage.PluginRegister;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.pluginManage.bean.HtmlVisualBean;
import com.xnx3.wangmarket.admin.pluginManage.interfaces.HtmlVisualInterface;

/**
 * html可视化编辑，采用 https://github.com/xnx3/HtmlVisualEditor
 * @author 管雷鸣
 */
//@PluginRegister(version="1.0", versionMin="6.2")
//public class Plugin implements HtmlVisualInterface{
public class Plugin{

//	@Override
	public HtmlVisualBean htmlVisualEditBefore(HttpServletRequest request, Site site) {
		HtmlVisualBean bean = new HtmlVisualBean();
		bean.setId("HtmlVisualEditor");
		bean.setName("HtmlVisualEditor 编辑模式");
		bean.setUrl("/plugin/HtmlVIsualEditor/plugin.js");
		return bean;
	}
}