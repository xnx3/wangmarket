package com.xnx3.wangmarket.formManage;

import com.xnx3.wangmarket.admin.pluginManage.PluginInterface;
import com.xnx3.wangmarket.admin.pluginManage.PluginRegister;

@PluginRegister(id="formManage" , menuTitle = "表单反馈",menuHref="../../form/list.do", applyToCMS=true, applyToPC=false, applyToWAP=false)
public class Plugin implements PluginInterface{
	
	public void a(){
		System.out.println("aaa");
	}
	
	public boolean init() {
		System.out.println("iii");
		return true;
	}

	public boolean install() {
		return true;
	}

	public boolean uninstall() {
		return true;
	}

}
