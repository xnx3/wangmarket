package com.xnx3.wangmarket.superadmin;

import com.xnx3.j2ee.pluginManage.PluginRegister;
import com.xnx3.j2ee.pluginManage.interfaces.SuperAdminIndexInterface;

/**
 * 总管理后台-welcome更换
 * @author 管雷鸣
 */
@PluginRegister(version="1.0", menuTitle = "欢迎页", intro="总管理后台、代理后台，登录成功后显示的welcome页面", versionMin="5.7")
public class Plugin implements SuperAdminIndexInterface  {
	
	@Override
	public String superAdminIndexAppendHtml() {
		String html = "<script type=\"text/javascript\">loadUrl('/superadmin/index/welcome.do');</script>";
		return html;
	}
	
}