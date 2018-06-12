package com.xnx3.wangmarket.admin.pluginManage;

import com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister;


/**
 * 网站管理后台插件的管理Bean
 * @author Administrator
 *
 */
public class SitePluginBean {
	public Class c;	//注册类的class
//	public String className;	//Class的名字，如  com.xnx3.wangmarket.admin.pluginManage.SitePluginBean， 会根据此创建对象。
	
	public String id;	//该插件的唯一标识。如自定义表单插件，唯一标识便是 formManage 。注意不能与其他插件重名
	
	//作为网站管理后台左侧 功能插件 菜单下的子菜单所显示的入口，入口菜单超链接所显示的标题及超链接所连接的页面
	public String menuTitle;	// 如：表单反馈
	public String menuHref;		// 如：站外的绝对路径，或站内的相对路径  ../../column/popupListForTemplate.do
	
	public boolean applyToCMS = false;	//是否适用于CMS类型网站管理后台， true：是
	public boolean applyToPC = false;	//是否适用于PC类型网站管理后台， true：是
	public boolean applyToWAP = false;	//是否适用于WAP类型网站管理后台， true：是
	
	
//	public SitePluginBean(Class c, String menuTitle, String menuHref, boolean applyToCMS, boolean applyToPC, boolean applyToWAP) {
//		this.c = c;
//		this.menuTitle = menuTitle;
//		this.menuHref = menuHref;
//		this.applyToCMS = applyToCMS;
//		this.applyToPC = applyToPC;
//		this.applyToWAP = applyToWAP;
//	}
	
	/**
	 * 将增加注解 PluginRegister 的 class 注册类传入，获取其 SitePluginBean 信息
	 * @param c
	 */
	public SitePluginBean(Class c) {
		this.c = c;
		PluginRegister an = (PluginRegister) c.getAnnotation(PluginRegister.class);
		if(an != null){
			this.id = an.id();
			this.menuTitle = an.menuTitle();
			this.menuHref = an.menuHref();
			this.applyToCMS = an.applyToCMS();
			this.applyToPC = an.applyToPC();
			this.applyToWAP = an.applyToWAP();
        }
	}

	public Class getC() {
		return c;
	}

	public void setC(Class c) {
		this.c = c;
	}

	public String getMenuTitle() {
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	public String getMenuHref() {
		return menuHref;
	}

	public void setMenuHref(String menuHref) {
		this.menuHref = menuHref;
	}

	public boolean isApplyToCMS() {
		return applyToCMS;
	}

	public void setApplyToCMS(boolean applyToCMS) {
		this.applyToCMS = applyToCMS;
	}

	public boolean isApplyToPC() {
		return applyToPC;
	}

	public void setApplyToPC(boolean applyToPC) {
		this.applyToPC = applyToPC;
	}

	public boolean isApplyToWAP() {
		return applyToWAP;
	}

	public void setApplyToWAP(boolean applyToWAP) {
		this.applyToWAP = applyToWAP;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "SitePluginBean [c=" + c + ", menuTitle=" + menuTitle
				+ ", menuHref=" + menuHref + ", applyToCMS=" + applyToCMS
				+ ", applyToPC=" + applyToPC + ", applyToWAP=" + applyToWAP
				+ "]";
	}
	
	
}
