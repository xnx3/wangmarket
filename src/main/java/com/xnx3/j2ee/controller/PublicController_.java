package com.xnx3.j2ee.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 通用的一些
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/")
public class PublicController_ extends BaseController {
	
	/**
	 * 403
	 */
	@RequestMapping("403${url.suffix}")
	public String error403(HttpServletRequest request){
		return "iw/403";
	}
	
	/**
	 * 404
	 */
	@RequestMapping("404${url.suffix}")
	public String error404(HttpServletRequest request){
		return "iw/404";
	}
	

	/**
	 * 406，这里用406错误代码，来表示文件上传太大的返回http响应
	 */
	@RequestMapping("406${url.suffix}")
	@ResponseBody
//	@ResponseStatus(HttpStatus.OK)  
	public BaseVO error406(HttpServletRequest request){
		System.out.println("406--");
		return error("请上传大小在 "+ AttachmentFile.getMaxFileSize()+" 之内的文件");
	}
	
	/**
	 * 500
	 */
	@RequestMapping("500${url.suffix}")
	public String error500(HttpServletRequest request){
		return "iw/500";
	}
	
}
