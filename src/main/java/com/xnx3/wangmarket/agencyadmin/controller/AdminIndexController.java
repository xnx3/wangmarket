package com.xnx3.wangmarket.agencyadmin.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.xnx3.ClassUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.pluginManage.PluginManage;
import com.xnx3.j2ee.pluginManage.PluginRegister;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.wangmarket.agencyadmin.entity.Agency;
import com.xnx3.wangmarket.agencyadmin.entity.AgencyData;
import com.xnx3.wangmarket.agencyadmin.pluginManage.interfaces.manage.AgencyAdminIndexPluginManage;

/**
 * 代理后台管理首页
 * @author 管雷鸣
 */
@Controller(value="AgencyAdminIndexController")
@RequestMapping("/agency")
public class AdminIndexController extends BaseController{
	@Resource
	private SqlService sqlService;
	
	/**
	 * 代理后台首页
	 * @param jumpUrl 这里在应用插件里面，安装插件后要刷新页面，所以加入了jumpUrl，传入加载地址，如果有jumpUrl，那么默认页面就是访问这个。这里传入的地址，如 plugin/pluginManage/index.do 
	 */
	@RequestMapping("index${url.suffix}")
	public String index(HttpServletRequest request, Model model,
			@RequestParam(value = "jumpUrl", required = false , defaultValue="") String jumpUrl){
		ActionLogUtil.insert(request, "进入代理后台首页");
		
		String pluginMenu = "";
		if(PluginManage.agencyClassManage.size() > 0){
			for (Map.Entry<String, PluginRegister> entry : PluginManage.agencyClassManage.entrySet()) {
				PluginRegister plugin = entry.getValue();
				pluginMenu += "<dd><a id=\""+entry.getKey()+"\" class=\"subMenuItem\" href=\"javascript:loadUrl('"+ plugin.menuHref()+"'), notUseTopTools();\">"+ plugin.menuTitle()+"</a></dd>";
			}
		}
		model.addAttribute("pluginMenu", pluginMenu);
		
		/**** 针对html追加的插件 ****/
		try {
			String pluginAppendHtml = AgencyAdminIndexPluginManage.manage();
			model.addAttribute("pluginAppendHtml", pluginAppendHtml);
		} catch (InstantiationException | IllegalAccessException
				| NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		User user = getUser();
		model.addAttribute("user", user);
		
		return "/agency/index";
	}
	
	/**
	 * 代理商后台欢迎页面
	 */
	@RequestMapping("welcome${url.suffix}")
	public String welcome(HttpServletRequest request, Model model){
		Agency agency = getMyAgency();
		if(agency == null){
			return error(model, "代理信息出错！");
		}
		//上级代理的变长表数据
		AgencyData parentAgencyData = getParentAgencyData();
		
		ActionLogUtil.insert(request, agency.getId(), "进入代理商后台首页");
		User user = sqlService.findById(User.class, getUserId());
		
		model.addAttribute("user", user);
		model.addAttribute("agency", agency);
		model.addAttribute("parentAgency", getParentAgency());	//上级代理
		//上级代理的公告内容，要显示出来的
		model.addAttribute("parentAgencyNotice", parentAgencyData == null ? "":parentAgencyData.getNotice());
		model.addAttribute("AGENCYUSER_FIRST_USE_EXPLAIN_URL", SystemUtil.get("AGENCYUSER_FIRST_USE_EXPLAIN_URL"));
		return "agency/welcome";
	}
	
}
