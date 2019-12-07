package com.xnx3.j2ee.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.configuration.ConfigurationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.DateUtil;
import com.xnx3.FileUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.func.Log;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.SystemService;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.net.OSSUtil;

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
		ActionLogCache.insert(request, "进入install安装首页");
		return "domain/welcome";
	}
	

	
	/**
	 * 第一步，选择存储方式，使用阿里云OSS，还是服务器磁盘存储
	 * @throws ConfigurationException 
	 */
	@RequestMapping("/selectAttachment${url.suffix}")
	public String selectAttachment(HttpServletRequest request, Model model){
		if(!Global.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(model, jinzhianzhuang, "login.do");
		}
		ActionLogCache.insert(request, "进入install安装-选择存储方式");
		
		model.addAttribute("AttachmentFile_MODE_LOCAL_FILE", AttachmentFile.MODE_LOCAL_FILE);
		model.addAttribute("AttachmentFile_MODE_ALIYUN_OSS", AttachmentFile.MODE_ALIYUN_OSS);
		return "iw_update/install/selectAttachment";
	}
	
	/**
	 * 服务于第一步，设置当前存储方式（保存）
	 * @throws ConfigurationException 
	 */
	@RequestMapping("/setAttachmentMode${url.suffix}")
	public String setLocalAttachmentFile(HttpServletRequest request, Model model,
			@RequestParam(value = "mode", required = false, defaultValue="") String mode){
		if(!Global.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(model, jinzhianzhuang, "login.do");
		}
		
		String m = AttachmentFile.MODE_LOCAL_FILE;	//默认使用服务器进行存储
		if(mode.equals(AttachmentFile.MODE_ALIYUN_OSS)){
			//使用阿里云OSS
			m = AttachmentFile.MODE_ALIYUN_OSS;
		}
		sqlService.executeSql("update system set value = '"+m+"' WHERE name = 'ATTACHMENT_FILE_MODE'");
		
		//更新缓存
		systemService.refreshSystemCache();
		
		ActionLogCache.insertUpdateDatabase(request, "进入install安装-设置当前存储方式（保存）");
		
		return redirect("install/systemSet.do");
	}
	

	/**
	 * 第二步，设置项目的系统参数，system中的系统参数，比如网站泛解析的域名，自己的邮箱、访问域名
	 */
	@RequestMapping("/systemSet${url.suffix}")
	public String systemSet(HttpServletRequest request, Model model){
		if(!Global.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(model, jinzhianzhuang, "login.do");
		}
		
		//系统访问域名
		String fangwenyuming = Global.get("MASTER_SITE_URL");
		if(fangwenyuming == null || fangwenyuming.length() < 6){
			fangwenyuming = request.getRequestURL().toString().replace("install/systemSet.do", "");
		}
		
		//附件访问域名
		String fujianyuming = Global.get("ATTACHMENT_FILE_URL");
		if(fujianyuming == null || fujianyuming.length() < 6){
			fujianyuming = fangwenyuming;
		}
		
		ActionLogCache.insert(request, "进入install安装-设置项目的系统参数，system中的系统参数，比如网站泛解析的域名，自己的邮箱、访问域名");
		
		model.addAttribute("fangwenyuming", fangwenyuming);
		model.addAttribute("fujianyuming", fujianyuming);
		return "iw_update/install/systemSet";
	}
	

	/**
	 * 第二步，提交，验证AccessKey的id、screct的有效性，并初始化创建OSS
	 */
	@RequestMapping("/systemSetSave${url.suffix}")
	@ResponseBody
	public BaseVO systemSetSave(HttpServletRequest request,
			@RequestParam(value = "MASTER_SITE_URL", required = false, defaultValue="") String MASTER_SITE_URL,
			@RequestParam(value = "ATTACHMENT_FILE_URL", required = false, defaultValue="") String ATTACHMENT_FILE_URL,
			@RequestParam(value = "AUTO_ASSIGN_DOMAIN", required = false, defaultValue="") String AUTO_ASSIGN_DOMAIN,
			@RequestParam(value = "SERVICE_MAIL", required = false, defaultValue="") String SERVICE_MAIL
			){
		if(!Global.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(jinzhianzhuang);
		}
		
		//将其存入system数据表
		sqlService.executeSql("update system set value = '"+MASTER_SITE_URL+"' WHERE name = 'MASTER_SITE_URL'");
		sqlService.executeSql("update system set value = '"+ATTACHMENT_FILE_URL+"' WHERE name = 'ATTACHMENT_FILE_URL'");
		sqlService.executeSql("update system set value = '"+AUTO_ASSIGN_DOMAIN+"' WHERE name = 'AUTO_ASSIGN_DOMAIN'");
		sqlService.executeSql("update system set value = '"+SERVICE_MAIL+"' WHERE name = 'SERVICE_MAIL'");
		
		if(Global.get("ATTACHMENT_FILE_MODE").equals(AttachmentFile.MODE_ALIYUN_OSS)){
			//阿里云oss，还要继续配置阿里云参数
		}else{
			//如果是服务器存储，那到这一步就结束了，在此禁用install安装
			sqlService.executeSql("update system set value = 'false' WHERE name = 'IW_AUTO_INSTALL_USE'");
		}
		
		//如果 附件url域名设置了，那么更新内存缓存
		if(ATTACHMENT_FILE_URL.length() > 5){
			AttachmentFile.netUrl = ATTACHMENT_FILE_URL;
		}
		
		//更新缓存
		systemService.refreshSystemCache();
		
		ActionLogCache.insertUpdateDatabase(request, "进入install安装-提交，验证AccessKey的id、screct的有效性，并初始化创建OSS");
		
		return success();
	}
	
	/**
	 * 第三步，设置AccessKey的id、screct的页面
	 * @throws ConfigurationException 
	 */
	@RequestMapping("/accessKey${url.suffix}")
	public String accessKey(HttpServletRequest request, Model model){
		if(!Global.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(model, jinzhianzhuang, "login.do");
		}
		ActionLogCache.insert(request, "进入install安装-设置AccessKey的id、screct的页面");
		return "iw_update/install/accessKey";
	}
	
	/**
	 * 第三步，验证AccessKey的id、screct的有效性，并初始化创建OSS
	 */
	@RequestMapping("/accessKeySave${url.suffix}")
	@ResponseBody
	public BaseVO accessKeySave(HttpServletRequest request,
			@RequestParam(value = "id", required = false, defaultValue="") String id,
			@RequestParam(value = "secret", required = false, defaultValue="") String secret
			){
		if(!Global.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(jinzhianzhuang);
		}
		if(id.length() == 0){
			return error("请输入 Access Key ID");
		}
		if(secret.length() == 0){
			return error("请输入 Access Key Secret");
		}
		
		//进行自动创建OSS测试
		String bucketName = Global.get("ALIYUN_OSS_BUCKETNAME");
		if(bucketName == null){
			return error("数据表system中没有ALIYUN_OSS_BUCKETNAME，数据表有缺，初始化OSS失败！");
		}else{
			if(bucketName.equals("auto")){
				//若是为auto，则是第一次刚开始用，自动创建
				bucketName = "wangmarket"+DateUtil.timeForUnix10();
			}
		}
		
		OSSUtil.accessKeyId = id;
		OSSUtil.accessKeySecret = secret;
		OSSUtil.bucketName = bucketName;
		OSSUtil.endpoint = ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("aliyunOSS.endpoint");
		OSSUtil.refreshUrl();
		
		try {
			//判断这个bucketName是否存在
			boolean have = OSSUtil.getOSSClient().doesBucketExist(bucketName);
			if(!have){
				//如果不存在这个bucketName，则自动创建一个
				CreateBucketRequest createBucketRequest= new CreateBucketRequest(bucketName);
				createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);	// 设置bucket权限为公共读，默认是私有读写
				OSSUtil.getOSSClient().createBucket(createBucketRequest);
				System.out.println("自动创建buckname: "+bucketName);
			}else{
				System.out.println("OSS未自动创建！因为检测到BucketName已存在！若不是您手动创建的，则建议您按照以下两点进行操作，然后再来创建。");
				System.out.println("1.将/src/xnx3Config.xml文件中，aliyunOSS节点下的bucketName设置为空，即将其中配置的数据删除掉");
				System.out.println("2.将数据表system中，name为ALIYUN_OSS_BUCKETNAME的这一列，将其值改为auto");
				return error("OSS未自动创建！因为检测到BucketName已存在！若不是您手动创建的，则建议您按照以下两点进行操作，然后再来创建。");
			}
		} catch (Exception e) {
			return error("操作失败！错误代码:"+e.getMessage().toString());
		}
		
		//将其存入system数据表
		sqlService.executeSql("update system set value = '"+id+"' WHERE name = 'ALIYUN_ACCESSKEYID'");
		sqlService.executeSql("update system set value = '"+secret+"' WHERE name = 'ALIYUN_ACCESSKEYSECRET'");
		sqlService.executeSql("update system set value = '"+bucketName+"' WHERE name = 'ALIYUN_OSS_BUCKETNAME'");
		
		//设置为禁止安装
		sqlService.executeSql("update system set value = 'false' WHERE name = 'IW_AUTO_INSTALL_USE'");
		
		//更新缓存
		systemService.refreshSystemCache();
		ActionLogCache.insertUpdateDatabase(request, "进入install安装-验证AccessKey的id、screct的有效性，并初始化创建OSS");
		
		return success();
	}
	
	
	/**
	 * 安装成功页面
	 */
	@RequestMapping("/installSuccess${url.suffix}")
	public String installSuccess(HttpServletRequest request, Model model){
		ActionLogCache.insert(request, "进入install安装-安装成功页面");
		return "iw_update/install/installSuccess";
	}
	
	/**
	 * 自删除,将此installController.class删除掉
	 */
	@RequestMapping("/delete${url.suffix}")
	public String delete(HttpServletRequest request, Model model){
		String thisClassPath = this.getClass().getResource("/com/xnx3/j2ee/controller/InstallController_.class").getPath();
		boolean d = FileUtil.deleteFile(thisClassPath);
		if(d){
			ActionLogCache.insert(request, "进入install安装-自删除,将此installController.class删除掉");
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
		if(!Global.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(model, jinzhianzhuang, "login.do");
		}
		
		//系统访问域名
		String autoAssignDomain = Global.get("AUTO_ASSIGN_DOMAIN");
		if(autoAssignDomain == null || autoAssignDomain.length() < 4){
			autoAssignDomain = "";
		}
		
		ActionLogCache.insert(request, "进入install安装-设置域名");
		model.addAttribute("autoAssignDomain", autoAssignDomain);
		return "iw_update/install/domainSet";
	}
	

	/**
	 * 域名更改保存 v4.11增加
	 */
	@RequestMapping("/domainSetSave${url.suffix}")
	@ResponseBody
	public BaseVO domainSetSave(HttpServletRequest request,
			@RequestParam(value = "autoAssignDomain", required = false, defaultValue="") String autoAssignDomain
			){
		if(!Global.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(jinzhianzhuang);
		}
		
		if(autoAssignDomain.length() < 3){
			return error("请正确设置您的域名");
		}
		
		//更新附件域名的内存缓存
		AttachmentFile.netUrl = "http://cdn."+autoAssignDomain+"/";
		
		//将其存入system数据表
		sqlService.executeSql("update system set value = 'http://admin."+autoAssignDomain+"/' WHERE name = 'MASTER_SITE_URL'");
		sqlService.executeSql("update system set value = 'http://cdn."+autoAssignDomain+"/' WHERE name = 'ATTACHMENT_FILE_URL'");
		sqlService.executeSql("update system set value = '"+autoAssignDomain+"' WHERE name = 'AUTO_ASSIGN_DOMAIN'");
		
		//到这一步就结束了，在此禁用install安装
		sqlService.executeSql("update system set value = 'false' WHERE name = 'IW_AUTO_INSTALL_USE'");
		
		//更新缓存
		systemService.refreshSystemCache();
		ActionLogCache.insertUpdateDatabase(request, "进入install安装-域名更改保存", autoAssignDomain);
		return success();
	}
	
	/**
	 * 设置域名为测试使用的，只为快速测试体验而设置的域名
	 */
	@RequestMapping("/setLocalDomain${url.suffix}")
	public String setLocalDomain(HttpServletRequest request, Model model){
		if(!Global.get("IW_AUTO_INSTALL_USE").equals("true")){
			return error(model, jinzhianzhuang, "login.do");
		}
		
		//获取域名 ，格式如 http://www.leimingyun.com/
		String domain = request.getRequestURL().toString().replace("install/setLocalDomain.do", "");
		
		//更新附件域名的内存缓存
		AttachmentFile.netUrl = domain;
		
		Log.info("快速测试体验，域名自动获取："+domain);
		
		//将其存入system数据表
		sqlService.executeSql("update system set value = '"+domain+"' WHERE name = 'MASTER_SITE_URL'");
		sqlService.executeSql("update system set value = '"+domain+"' WHERE name = 'ATTACHMENT_FILE_URL'");
		sqlService.executeSql("update system set value = 'wang.market' WHERE name = 'AUTO_ASSIGN_DOMAIN'");
		
		//到这一步就结束了，在此禁用install安装
		sqlService.executeSql("update system set value = 'false' WHERE name = 'IW_AUTO_INSTALL_USE'");
		
		//更新缓存
		systemService.refreshSystemCache();
		ActionLogCache.insertUpdateDatabase(request, "进入install安装-设置域名为测试使用的，只为快速测试体验而设置的域名");
		return redirect("install/installSuccess.do");
	}
	
}
