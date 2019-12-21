package com.xnx3.j2ee.util;

import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 验证码
 * @author 管雷鸣
 */
public class CaptchaUtil {

	/**
	 * 显示图片验证码，直接创建出jpg格式图片
	 * @param captchaUtil {@link CaptchaUtil} 可对其自定义验证码图片的属性、显示、格式等
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws IOException
	 */
	public static void showImage(com.xnx3.media.CaptchaUtil captchaUtil, HttpServletRequest request, HttpServletResponse response) throws IOException{
		//将验证码保存到Session中。
		HttpSession session = request.getSession();
		session.setAttribute("code", captchaUtil.getCode());
		session.setAttribute("codeIsUsed", "0");	//标记未被使用

		//禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		
		response.setContentType("image/jpeg");

		//将图像输出到Servlet输出流中。
		ServletOutputStream sos = response.getOutputStream();
		ImageIO.write(captchaUtil.createImage(), "jpeg", sos);
		sos.close();
	}
	
	/**
	 * 显示图片验证码，直接创建出jpg格式图片
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws IOException
	 */
	public static void showImage(HttpServletRequest request, HttpServletResponse response) throws IOException{
		showImage(new com.xnx3.media.CaptchaUtil(), request, response);
	}
	
	/**
	 * 用户输入的验证码，与系统存储的进行比较，返回结果
	 * @param inputCode 用户输入的验证码
	 * @param request HttpServletRequest 主要用于从其中获取Session
	 * @return {@link BaseVO}
	 */
	public static BaseVO compare(String inputCode, HttpServletRequest request){
		BaseVO vo = new BaseVO();
		if(inputCode == null || inputCode.length() == 0){
			vo.setBaseVO(BaseVO.FAILURE, "请输入验证码");
			return vo;
		}
		
		//获取Session记录的验证码
		HttpSession session = request.getSession();
		Object codeObj = session.getAttribute("code");
		if(codeObj == null){
			vo.setBaseVO(BaseVO.FAILURE, "出错，系统中没有存储您的验证码！");
			return vo;
		}
		String code = (String) codeObj;	//Session中存储的验证码
		if(inputCode.equalsIgnoreCase(code)){
			String codeIsUsed = (String)session.getAttribute("codeIsUsed");	//验证码是否被使用，1:被使用了；0未被使用
			if(codeIsUsed.equals("1")){
				vo.setBaseVO(BaseVO.FAILURE, "此验证码已被使用过了！");
				return vo;
			}else{
				//设置验证码已被使用
				session.setAttribute("codeIsUsed", "1");
				return vo;
			}
		}else{
			vo.setBaseVO(BaseVO.FAILURE, "验证码出错");
			return vo;
		}
	}
}
