package com.xnx3.j2ee.controller.admin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xnx3.MD5Util;
import com.xnx3.j2ee.Func;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.Log;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.pluginManage.PluginManage;
import com.xnx3.wangmarket.admin.pluginManage.SitePluginBean;

/**
 * 管理后台首页
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/index")
public class AdminIndexController_ extends BaseController{
	
	/**
	 * 管理后台首页
	 */
	@RequiresPermissions("adminIndexIndex")
	@RequestMapping("index${url.suffix}")
	public String index(HttpServletRequest request, Model model){
		//登录成功后，管理后台的主题页面，默认首页的url
		String url = "admin/index/welcome.do"; 
		
		//这里可以根据不同的管理级别，来指定显示默认是什么页面
		if(Func.isAuthorityBySpecific(getUser().getAuthority(), Global.get("ROLE_SUPERADMIN_ID"))){
			//有超级管理员权限
			url = "admin/index/welcome.do";
			
			//获取网站后台管理系统有哪些功能插件，也一块列出来,以直接在网站后台中显示出来
			String pluginMenu = "";
			if(PluginManage.superAdminClassManage.size() > 0){
				for (Map.Entry<String, SitePluginBean> entry : PluginManage.superAdminClassManage.entrySet()) {
					SitePluginBean bean = entry.getValue();
					pluginMenu += "<dd><a id=\""+entry.getKey()+"\" class=\"subMenuItem\" href=\"javascript:loadUrl('"+bean.getMenuHref()+"'), notUseTopTools();\">"+bean.getMenuTitle()+"</a></dd>";
				}
			}
			model.addAttribute("pluginMenu", pluginMenu);
		}else{
			//代理
			url = "agency/index.do";
			
			String pluginMenu = "";
			if(PluginManage.agencyClassManage.size() > 0){
				for (Map.Entry<String, SitePluginBean> entry : PluginManage.agencyClassManage.entrySet()) {
					SitePluginBean bean = entry.getValue();
					pluginMenu += "<dd><a id=\""+entry.getKey()+"\" class=\"subMenuItem\" href=\"javascript:loadUrl('"+bean.getMenuHref()+"'), notUseTopTools();\">"+bean.getMenuTitle()+"</a></dd>";
				}
			}
			model.addAttribute("pluginMenu", pluginMenu);
		}
		
		User user = getUser();
		model.addAttribute("password", MD5Util.MD5(user.getPassword()));
		model.addAttribute("user", user);
		model.addAttribute("indexUrl", url);	//首页(欢迎页)url
		model.addAttribute("useSMS", G.aliyunSMSUtil == null? "1":"0");	//若是使用SMS短信，开启了，则为1，否则没有开通短信的花则为0
		model.addAttribute("im_kefu_websocketUrl", com.xnx3.wangmarket.im.Global.websocketUrl);
		model.addAttribute("useDomainLog", com.xnx3.wangmarket.domain.Log.aliyunLogUtil != null);	//是否启用了阿里云日志服务，若未启用，则是false
		
		return "/iw_update/admin/index/index";
	}
	
}
