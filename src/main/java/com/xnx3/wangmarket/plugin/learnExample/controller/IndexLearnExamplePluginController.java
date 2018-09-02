package com.xnx3.wangmarket.plugin.learnExample.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.wangmarket.plugin.base.controller.BasePluginController;

/**
 * CMS模式下，输入模型相关操作
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/plugin/learnExample/")
public class IndexLearnExamplePluginController extends BasePluginController {
	/**
	 * 当点击 功能插件 下的子菜单 入门示例 时，会进入此页面（ 因为在 Plugin 类中，注册的 menuHref 填写的url是这个 ）
	 */
	@RequestMapping("index${url.suffix}")
	public String index(){
		
		/*
		 * 这里可进行逻辑控制等
		 */
		
		//这里定位到 src/main/webapp/WEB-INF/view/plugin/learnExample/index.jsp
		return "plugin/learnExample/index";
	}
}