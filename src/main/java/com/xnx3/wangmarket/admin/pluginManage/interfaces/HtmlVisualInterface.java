package com.xnx3.wangmarket.admin.pluginManage.interfaces;

import javax.servlet.http.HttpServletRequest;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.pluginManage.bean.HtmlVisualBean;

/**
 * html 可视化编辑的插件，主要在网站管理后台-模版管理-模版页面 的编辑时使用。
 * @author 管雷鸣
 */
public interface HtmlVisualInterface {
	
	/**
	 * @param site 当前操作的网站
	 * @param basevo.info 返回js插件的url。这里可以时绝对路径，也可以是相对路径，如 /plugin/HtmlVisualEditor/plugin.js
	 */
	public HtmlVisualBean htmlVisualEditBefore(HttpServletRequest request, Site site);
	
}