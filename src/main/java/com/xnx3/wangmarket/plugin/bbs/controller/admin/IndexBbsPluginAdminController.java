package com.xnx3.wangmarket.plugin.bbs.controller.admin;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.controller.BaseController;
import com.xnx3.wangmarket.plugin.bbs.entity.PostClass;
import com.xnx3.wangmarket.plugin.bbs.service.PostService;

/**
 * 论坛管理后台首页
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/plugin/bbs/admin/")
public class IndexBbsPluginAdminController extends BaseController {

	/**
	 * 管理后台首页
	 */
	@RequestMapping("index${url.suffix}")
	public String list(HttpServletRequest request,Model model){
		return "/plugin/bbs/admin/index";
	}
	
	
	@RequestMapping("welcome${url.suffix}")
	public String welcome(HttpServletRequest request,Model model){
		return "/plugin/bbs/admin/welcome";
	}
	
}
