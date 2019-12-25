package com.xnx3.wangmarket.superadmin.bean;

import com.xnx3.j2ee.pluginManage.PluginRegister;

/**
 * {@link PluginRegister} 的 bean，方便 springmvc 向 view 传递数据
 * @author 管雷鸣
 *
 */
public class PluginRegisterBean {
	public String id;	//该插件的唯一标识。如自定义表单插件，唯一标识便是 formManage 。注意不能与其他插件重名
	
	//作为网站管理后台左侧 功能插件 菜单下的子菜单所显示的入口，入口菜单超链接所显示的标题及超链接所连接的页面
	public String menuTitle;	// 如：表单反馈
	public String menuHref;		// 如：站外的绝对路径，或站内的相对路径  ../../column/popupListForTemplate.do
	
	public boolean applyToCMS = false;	//是否适用于CMS类型网站管理后台， true：是
	public boolean applyToAgency = false;	//是否适用于代理后台， true：是
	public boolean applyToSuperAdmin = false;	//是否适用于总管理后台， true：是
	
	//v4.6增加
	public String intro;	//该插件的简介说明
	public String detailUrl;	//该插件的详情说明的网址，点击后进入这个url查看详细说明
	public String version;	//当前插件的版本号
	public String versionCheckUrl;	//远程版本检测的url地址。 其内返回值为 最新版本号|提示有新版本后点击进入的网址|  如： 1.0|http://www.wang.market/wangmarket.html|
	
	/**
	 * 将增加注解 PluginRegister 传入，将其转为 Bean 的形态
	 * @param pluginRegister {@link PluginRegister}
	 */
	public PluginRegisterBean(PluginRegister pluginRegister) {
		if(pluginRegister != null){
			this.id = pluginRegister.id();
			this.menuTitle = pluginRegister.menuTitle();
			this.menuHref = pluginRegister.menuHref();
			this.applyToCMS = pluginRegister.applyToCMS();
			this.applyToAgency = pluginRegister.applyToAgency();
			this.applyToSuperAdmin = pluginRegister.applyToSuperAdmin();
			this.intro = pluginRegister.intro();
			this.detailUrl = pluginRegister.detailUrl();
			this.version = pluginRegister.version();
        }
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


	public String getId() {
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	
	public boolean isApplyToAgency() {
		return applyToAgency;
	}

	public boolean isApplyToSuperAdmin() {
		return applyToSuperAdmin;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersionCheckUrl() {
		return versionCheckUrl;
	}

	public void setVersionCheckUrl(String versionCheckUrl) {
		this.versionCheckUrl = versionCheckUrl;
	}

	public void setApplyToAgency(boolean applyToAgency) {
		this.applyToAgency = applyToAgency;
	}

	public void setApplyToSuperAdmin(boolean applyToSuperAdmin) {
		this.applyToSuperAdmin = applyToSuperAdmin;
	}


}
