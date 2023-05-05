package com.xnx3.wangmarket.plugin.HtmlEdit;

import javax.servlet.http.HttpServletRequest;
import com.xnx3.j2ee.pluginManage.PluginRegister;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.pluginManage.bean.HtmlVisualBean;
import com.xnx3.wangmarket.admin.pluginManage.interfaces.HtmlVisualInterface;

/**
 * html可视化编辑，wangmarket v6.2 版本以前的可视化都是用的这个
 * @author 管雷鸣
 */
@PluginRegister(version="1.0", versionMin="6.2")
public class Plugin implements HtmlVisualInterface{

	@Override
	public HtmlVisualBean htmlVisualEditBefore(HttpServletRequest request, Site site) {
		HtmlVisualBean bean = new HtmlVisualBean();
		bean.setId("HtmlEdit");
		bean.setName("HtmlEdit");
		bean.setUrl("/plugin/HtmlEdit/plugin.js");
		return bean;
	}
}