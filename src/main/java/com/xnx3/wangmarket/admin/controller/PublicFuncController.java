package com.xnx3.wangmarket.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.vo.BaseVO;

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
	public String templat(HttpServletRequest request){
		return "template";
	}
	
}
