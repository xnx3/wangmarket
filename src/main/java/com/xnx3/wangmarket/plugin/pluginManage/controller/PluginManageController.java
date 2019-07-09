package com.xnx3.wangmarket.plugin.pluginManage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.SmsLog;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.service.ApiService;
import com.xnx3.j2ee.service.SmsLogService;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UserVO;
import com.xnx3.wangmarket.superadmin.entity.Agency;
import com.xnx3.wangmarket.superadmin.entity.AgencyData;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.bean.UserBean;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.pluginManage.PluginManage;
import com.xnx3.wangmarket.admin.pluginManage.SitePluginBean;
import com.xnx3.wangmarket.admin.service.PluginService;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.util.AliyunLog;
import com.xnx3.wangmarket.admin.vo.SiteVO;
import com.xnx3.wangmarket.plugin.base.controller.BasePluginController;

/**
 * 帮助说明
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/plugin/pluginManage/")
public class PluginManageController extends BasePluginController {
	@Resource
	private PluginService pluginService;
	
	
	/**
	 * 入口页面
	 */
	@RequestMapping("/index${url.suffix}")
	public String index(HttpServletRequest request ,Model model){
		return "plugin/pluginManage/index";
	}
	
	/**
	 * 所有云端插件，云端所有插件列表
	 */
	@RequestMapping("/allList${url.suffix}")
	public String allList(HttpServletRequest request ,Model model){
		return "plugin/pluginManage/allList";
	}
	
	/**
	 * 当前已使用的插件列表
	 */
	@RequestMapping("/myList${url.suffix}")
	public String myList(HttpServletRequest request ,Model model){
		List<SitePluginBean> pluginList = new ArrayList<SitePluginBean>();
		
		//获取当前已经安装的所有的插件
		Map<String, SitePluginBean> pluginMap = pluginService.getCurrentPluginMap();
		for (Map.Entry<String, SitePluginBean> entry : pluginMap.entrySet()) {
			pluginList.add(entry.getValue());
		}
		
		model.addAttribute("pluginList", pluginList);
		return "plugin/pluginManage/myList";
	}
	
	/**
	 * 可以安装的插件列表
	 */
	@RequestMapping("/keyongList${url.suffix}")
	public String keyongList(HttpServletRequest request ,Model model){
		return "plugin/pluginManage/keyongList";
	}
	
	
	
}
