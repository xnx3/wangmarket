package com.xnx3.wangmarket.superadmin.vo;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.superadmin.bean.PluginRegisterBean;

/**
 * 已安装的应用插件列表,传递给jsp使用
 * @author 管雷鸣
 *
 */
public class InstallApplicationVO extends BaseVO{
	private PluginRegisterBean pluginRegisterBean;	//插件的PluginRegisterBean
	private boolean findNewVersion;	//是否发现新版本，如果发现了新版本，则是true
	
	public PluginRegisterBean getPluginRegisterBean() {
		return pluginRegisterBean;
	}
	public void setPluginRegisterBean(PluginRegisterBean pluginRegisterBean) {
		this.pluginRegisterBean = pluginRegisterBean;
	}
	public boolean isFindNewVersion() {
		return findNewVersion;
	}
	public void setFindNewVersion(boolean findNewVersion) {
		this.findNewVersion = findNewVersion;
	}
}
