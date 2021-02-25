package com.xnx3.wangmarket.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.configuration.ConfigurationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.FileUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.SystemService;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * IW 快速开发底层架构的安装，比如，阿里云各种产品如OSS、日志服务等的创建等
 * 
 * OSS
 * 		创建Bucket
 * 日志服务
 * 		创建project
 * 		创建logstore
 * 		设置索引等
 * 发信的邮箱设置
 * 		帐号、密码等
 * 智能文本过滤服务
 * 
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/install/")
public class InstallController_ extends BaseController {
	private static String jinzhianzhuang = "您已经安装过了，系统已禁止再次安装。如果您想继续安装，可进入总管理后台，找到系统设置-系统变量,将变量 IW_AUTO_INSTALL_USE 的值设置为 true ，即可再次进行安装。";

	@Resource
	private SqlService sqlService;
	@Resource
	private SystemService systemService;
	
	/**
	 * 安装首页
	 */
	@RequestMapping("/index${url.suffix}")
	public String index(HttpServletRequest request, Model model){
//		if(!Global.get("IW_AUTO_INSTALL_USE").equals("true")){
//			return error(model, "系统已禁止使用此！");
//		}
		ActionLogUtil.insert(request, "进入install安装首页");
		return "domain/welcome";
	}
	

	
	/**
	 * 第一步，选择存储方式，使用阿里云OSS，还是服务器磁盘存储
	 * @throws ConfigurationException 
	 */
	@RequestMapping("/selectAttachment${url.suffix}")
	public String selectAttachment(HttpServletRequest request, Model model){
		if(!SystemUtil.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(model, jinzhianzhuang, "login.do");
		}
		ActionLogUtil.insert(request, "进入install安装-选择存储方式");
		
		model.addAttribute("AttachmentFile_MODE_LOCAL_FILE", AttachmentUtil.MODE_LOCAL_FILE);
		model.addAttribute("AttachmentFile_MODE_ALIYUN_OSS", AttachmentUtil.MODE_ALIYUN_OSS);
		return "/install/selectAttachment";
	}
	
	/**
	 * 服务于第一步，设置当前存储方式（保存）
	 * @throws ConfigurationException 
	 */
	@RequestMapping("/setAttachmentMode${url.suffix}")
	public String setLocalAttachmentFile(HttpServletRequest request, Model model,
			@RequestParam(value = "mode", required = false, defaultValue="") String mode){
		if(!SystemUtil.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(model, jinzhianzhuang, "login.do");
		}
		
		String m = AttachmentUtil.MODE_LOCAL_FILE;	//默认使用服务器进行存储
		if(mode.equals(AttachmentUtil.MODE_ALIYUN_OSS)){
			//使用阿里云OSS
			m = AttachmentUtil.MODE_ALIYUN_OSS;
		}
		sqlService.executeSql("update system set value = '"+m+"' WHERE name = 'ATTACHMENT_FILE_MODE'");
		
		//更新缓存
		systemService.refreshSystemCache();
		
		ActionLogUtil.insertUpdateDatabase(request, "进入install安装-设置当前存储方式（保存）");
		
		return redirect("install/systemSet.do");
	}
	

	/**
	 * 第二步，设置项目的系统参数，system中的系统参数，比如网站泛解析的域名，自己的邮箱、访问域名
	 */
	@RequestMapping("/systemSet${url.suffix}")
	public String systemSet(HttpServletRequest request, Model model){
		if(!SystemUtil.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(model, jinzhianzhuang, "login.do");
		}
		
		//系统访问域名
		String fangwenyuming = SystemUtil.get("MASTER_SITE_URL");
		if(fangwenyuming == null || fangwenyuming.length() < 6){
			fangwenyuming = request.getRequestURL().toString().replace("install/systemSet.do", "");
		}
		
		//附件访问域名
		String fujianyuming = SystemUtil.get("ATTACHMENT_FILE_URL");
		if(fujianyuming == null || fujianyuming.length() < 6){
			fujianyuming = fangwenyuming;
		}
		
		ActionLogUtil.insert(request, "进入install安装-设置项目的系统参数，system中的系统参数，比如网站泛解析的域名，自己的邮箱、访问域名");
		
		model.addAttribute("fangwenyuming", fangwenyuming);
		model.addAttribute("fujianyuming", fujianyuming);
		return "/install/systemSet";
	}
	

	/**
	 * 安装成功页面
	 */
	@RequestMapping("/installSuccess${url.suffix}")
	public String installSuccess(HttpServletRequest request, Model model){
		ActionLogUtil.insert(request, "进入install安装-安装成功页面");
		return "/install/installSuccess";
	}
	
	/**
	 * 自删除,将此installController.class删除掉
	 */
	@RequestMapping("/delete${url.suffix}")
	public String delete(HttpServletRequest request, Model model){
		String thisClassPath = this.getClass().getResource("/com/xnx3/j2ee/controller/InstallController_.class").getPath();
		boolean d = FileUtil.deleteFile(thisClassPath);
		if(d){
			ActionLogUtil.insert(request, "进入install安装-自删除,将此installController.class删除掉");
			return success(model, "安装文件已删除！登录后台 /login.do 使用吧", "/login.do");
		}else{
			return error(model, "删除失败，可能文件已删除了");
		}
	}
	
	

	/**
	 * 设置域名，v4.11 增加
	 */
	@RequestMapping("/domainSet${url.suffix}")
	public String domainSet(HttpServletRequest request, Model model){
		if(!SystemUtil.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(model, jinzhianzhuang, "login.do");
		}
		
		//系统访问域名
		String autoAssignDomain = SystemUtil.get("AUTO_ASSIGN_DOMAIN");
		if(autoAssignDomain == null || autoAssignDomain.length() < 4){
			autoAssignDomain = "";
		}
		
		ActionLogUtil.insert(request, "进入install安装-设置域名");
		model.addAttribute("autoAssignDomain", autoAssignDomain);
		return "/install/domainSet";
	}
	

	/**
	 * 域名更改保存 v4.11增加
	 */
	@RequestMapping(value="/domainSetSave${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO domainSetSave(HttpServletRequest request,
			@RequestParam(value = "autoAssignDomain", required = false, defaultValue="") String autoAssignDomain
			){
		if(!SystemUtil.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(jinzhianzhuang);
		}
		
		if(autoAssignDomain.length() < 3){
			return error("请正确设置您的域名");
		}
		
		//更新附件域名的内存缓存
		AttachmentUtil.netUrl = "http://cdn."+autoAssignDomain+"/";
		
		//将其存入system数据表
		sqlService.executeSql("update system set value = 'http://admin."+autoAssignDomain+"/' WHERE name = 'MASTER_SITE_URL'");
		sqlService.executeSql("update system set value = 'http://cdn."+autoAssignDomain+"/' WHERE name = 'ATTACHMENT_FILE_URL'");
		sqlService.executeSql("update system set value = '"+autoAssignDomain+"' WHERE name = 'AUTO_ASSIGN_DOMAIN'");
		
		//到这一步就结束了，在此禁用install安装
		sqlService.executeSql("update system set value = 'false' WHERE name = 'IW_AUTO_INSTALL_USE'");
		
		//更新缓存
		systemService.refreshSystemCache();
		ActionLogUtil.insertUpdateDatabase(request, "进入install安装-域名更改保存", autoAssignDomain);
		return success();
	}
	
	/**
	 * 设置域名为测试使用的，只为快速测试体验而设置的域名
	 */
	@RequestMapping("/setLocalDomain${url.suffix}")
	public String setLocalDomain(HttpServletRequest request, Model model){
		if(!SystemUtil.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(model, jinzhianzhuang, "login.do");
		}
		
		//获取域名 ，格式如 http://www.leimingyun.com/
		String domain = request.getRequestURL().toString().replace("install/setLocalDomain.do", "");
		
		//更新附件域名的内存缓存
		AttachmentUtil.netUrl = domain;
		
		ConsoleUtil.info("快速测试体验，域名自动获取："+domain);
		
		//将其存入system数据表
		sqlService.executeSql("update system set value = '"+domain+"' WHERE name = 'MASTER_SITE_URL'");
		sqlService.executeSql("update system set value = '"+domain+"' WHERE name = 'ATTACHMENT_FILE_URL'");
		sqlService.executeSql("update system set value = 'wang.market' WHERE name = 'AUTO_ASSIGN_DOMAIN'");
		
		//到这一步就结束了，在此禁用install安装
		sqlService.executeSql("update system set value = 'false' WHERE name = 'IW_AUTO_INSTALL_USE'");
		
		//更新缓存
		systemService.refreshSystemCache();
		ActionLogUtil.insertUpdateDatabase(request, "进入install安装-设置域名为测试使用的，只为快速测试体验而设置的域名");
		return redirect("install/installSuccess.do");
	}
	
}
