package com.xnx3.wangmarket.plugin.huaWeiYunServiceCreate.controller;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.service.SystemService;
import com.xnx3.wangmarket.plugin.base.controller.BasePluginController;

/**
 * 华为云环境配置入口
 * @author 李鑫
 */
@Controller
@RequestMapping("/plugin/huaWeiYunServiceCreate/")
public class HuaWeiYunServiceCreatePluginController extends BasePluginController {
	
	@Resource
	SystemService systemService;
	
	/**
	 * 总管理后台-功能插件进来的首页
	 */
	@RequestMapping("index${url.suffix}")
	public String index(Model model){
		systemService.refreshSystemCache();
		// 是否有管理员权限
		if(!haveSuperAdminAuth()){
			return error(model, "无权使用");
		}
		// 是否设置了地域信息，没有的话再检查又没有设置华为秘钥信息
		if(Global.get("HUAWEIYUN_COMMON_ENDPOINT") != null && Global.get("HUAWEIYUN_COMMON_ENDPOINT").length() > 3){
			return "plugin/huaWeiYunServiceCreate/index";
		}else{
			return redirect("plugin/huaWeiYunServiceCreate/set/setAccessKey.do");
		}
	}
	
	
	
}