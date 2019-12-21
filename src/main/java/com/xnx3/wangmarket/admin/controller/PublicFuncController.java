package com.xnx3.wangmarket.admin.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;

/**
 * 公共的一些
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/")
public class PublicFuncController extends BaseController {
	
	/**
	 * 模版列表
	 */
	@RequestMapping("template${url.suffix}")
	public String templat(HttpServletRequest request,Model model){
		ActionLogUtil.insert(request, "模版列表");
		model.addAttribute("AttachmentFileUrl", AttachmentUtil.netUrl());
		return "template";
	}
	
}
