package com.xnx3.j2ee.controller.admin;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.pluginManage.PluginManage;
import com.xnx3.j2ee.pluginManage.PluginRegister;
import com.xnx3.j2ee.pluginManage.interfaces.manage.SuperAdminIndexPluginManage;

/**
 * 管理后台首页
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/index")
public class AdminIndexController_ extends BaseController{
	
	/**
	 * 管理后台首页
	 * @param jumpUrl 这里在应用插件里面，安装插件后要刷新页面，所以加入了jumpUrl，传入加载地址，如果有jumpUrl，那么默认页面就是访问这个。这里传入的地址，如 plugin/pluginManage/index.do 
	 */
	@RequiresPermissions("adminIndexIndex")
	@RequestMapping("index${url.suffix}")
	public String index(HttpServletRequest request, Model model,
			@RequestParam(value = "jumpUrl", required = false , defaultValue="") String jumpUrl){
		//登录成功后，管理后台的主题页面，默认首页的url
		String url = "admin/index/welcome.do"; 
		
		ActionLogUtil.insert(request, "进入管理后台首页");
		
		//这里在应用插件里面，安装插件后要刷新页面，所以加入了jumpUrl，传入加载地址，如果有jumpUrl，那么默认页面就是访问这个。v4.11增加
		if(jumpUrl.length() > 2){
			url = jumpUrl;
		}
		
		//获取网站后台管理系统有哪些功能插件，也一块列出来,以直接在网站后台中显示出来
		String pluginMenu = "";
		
		//pluginManage 插件管理 功能
		pluginMenu += "<dd><a id=\"pluginManage\" class=\"subMenuItem\" href=\"javascript:loadUrl('/plugin/pluginManage/index.do'), notUseTopTools();\">插件管理</a></dd>";	//第一个，插件管理
		
		if(PluginManage.superAdminClassManage.size() > 0){
			for (Map.Entry<String, PluginRegister> entry : PluginManage.superAdminClassManage.entrySet()) {
				PluginRegister plugin = entry.getValue();
				pluginMenu += "<dd><a id=\""+entry.getKey()+"\" class=\"subMenuItem\" href=\"javascript:loadUrl('"+plugin.menuHref()+"'), notUseTopTools();\">"+plugin.menuTitle()+"</a></dd>";
			}
		}
		model.addAttribute("pluginMenu", pluginMenu);
		
		/**** 针对html追加的插件 ****/
		try {
			String pluginAppendHtml = SuperAdminIndexPluginManage.manage();
			model.addAttribute("pluginAppendHtml", pluginAppendHtml);
		} catch (InstantiationException | IllegalAccessException
				| NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		User user = getUser();
		model.addAttribute("user", user);
		model.addAttribute("useSLS", ActionLogUtil.aliyunLogUtil != null); //是否使用日志服务.  true：使用，false不使用
		return "/iw_update/admin/index/index";
	}
	
}
