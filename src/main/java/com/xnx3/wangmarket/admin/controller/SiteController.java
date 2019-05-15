package com.xnx3.wangmarket.admin.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.StringUtil;
import com.xnx3.wangmarket.im.service.ImService;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.media.ImageUtil;
import com.xnx3.net.MailUtil;
import com.xnx3.net.OSSUtil;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.cache.pc.Index;
import com.xnx3.wangmarket.admin.cache.pc.IndexAboutUs;
import com.xnx3.wangmarket.admin.entity.Feedback;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.entity.SiteData;
import com.xnx3.wangmarket.admin.service.NewsService;
import com.xnx3.wangmarket.admin.service.SiteColumnService;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.util.AliyunLog;
import com.xnx3.wangmarket.admin.vo.SiteDataVO;
import com.xnx3.wangmarket.admin.vo.SiteVO;
import com.xnx3.wangmarket.domain.bean.MQBean;
import com.xnx3.wangmarket.domain.bean.SimpleSite;

/**
 * 公共的
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/sites")
public class SiteController extends BaseController {
	@Resource
	private SqlService sqlService;
	@Resource
	private SiteColumnService siteColumnService;
	@Resource
	private SiteService siteService;
	@Resource
	private NewsService newsService;
	@Resource
	private UserService userService;
	@Resource
	private ImService imService;
	
	
//
//	/**
//	 * 用户自助创建网站，免费创建网站页面，可以选择创建手机的，还是电脑的网站
//	 */
//	@RequestMapping("createSite")
//	public String createSite(Model model,HttpServletRequest request){
//		//判断一下当前用户是否已经有了站点了
//		Site site = getSite();
//		if(site != null && site.getId() > 0){
//			return error(model, "您已经创建过网站了，无法再创建！");
//		}
//		
//		site = new Site();
//		site.setId(0);
//		site.setTemplateId(1);
//		site.setmShowBanner(Site.MSHOWBANNER_SHOW);
//		
//		AliyunLog.addActionLog(0, "进入自助创建网站页面");
//		
//		return "site/createSite";
//	}
//	
	/**
	 * 网站修改设置信息后保存，修改的信息如联系人、手机号、QQ、办公地址、公司名
	 * 废弃，应该没有用到了。下个版本考虑删除
	 * @param s {@link Site}用户创建网站填写的信息,用里面的 address、companyName、phone、qq、username
	 */
//	@RequestMapping("saveSiteInfo___")
//	@ResponseBody
//	public SiteVO saveSiteInfo(Site s, HttpServletRequest request){
//		Site site = sqlService.findById(Site.class, getSiteId());
//		site.setAddress(s.getAddress());
//		site.setCompanyName(s.getCompanyName());
//		site.setPhone(s.getPhone());
//		site.setQq(s.getQq());
//		site.setUsername(s.getUsername());
//		
//		AliyunLog.addActionLog(getSiteId(), "保存站点联系信息");
//		
//		return siteService.saveSite(site, getUserId(), request);
//	}

	/**
	 * 站点信息提交保存
	 */
	@RequestMapping(value="saveSite${url.suffix}", method = RequestMethod.POST)
	public String saveSite(Site s,Model model,HttpServletRequest request){
		SiteVO siteVO = siteService.saveSite(s, getUserId(), request);
		if(siteVO.getResult() == SiteVO.SUCCESS){
			AliyunLog.addActionLog(getSiteId(), "保存网站");
			return success(model, s.getId()>0? "保存网站成功！":"创建网站成功！", Func.getConsoleRedirectUrl());
		}else{
			return error(model, "保存失败！");
		}
	}
	
	
//	/**
//	 * 修改首页顶图的Banner图片是否显示
//	 * 此目前没有用到。在网站设置页面中会有这个是否显示或者隐藏banner得设置，只不过隐藏了。
//	 * 预留。没准以后可能会用到
//	 * @deprecated
//	 */
//	@RequestMapping("updateBanner")
//	@ResponseBody
//	public BaseVO updateBanner(@RequestParam(value = "mShowBanner", required = false , defaultValue="0") Short mShowBanner,
//			Model model,HttpServletRequest request){
//		BaseVO vo = new BaseVO();
//		Site site = sqlService.findById(Site.class, getSiteId());
//		
//		site.setmShowBanner(mShowBanner);
//		sqlService.save(site);
//		//创建数据js缓存 site.js
//		new com.xnx3.wangmarket.admin.cache.Site().site(site, imService.getImByCache());
//		
//		AliyunLog.addActionLog(getSiteId(), "修改Banner图为"+(mShowBanner==Site.MSHOWBANNER_SHOW?"显示":"隐藏"));
//		
//		return vo;
//	}
	
	/**
	 * 修改站点名称
	 */
	@RequestMapping(value="updateName${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO updateName(Model model,HttpServletRequest request,
			@RequestParam(value = "name", required = false , defaultValue="") String name){
		BaseVO vo = new BaseVO();
		Site site = sqlService.findById(Site.class, getSiteId());
		
		if(name.length() == 0){
			name = "此人很懒，暂未起名";
		}
		site.setName(filter(name));
		sqlService.save(site);
		
		AliyunLog.addActionLog(getSiteId(), "修改站点名字为："+site.getName());
		
		//更新当前Session缓存
		Func.getUserBeanForShiroSession().setSite(site);
		
		//刷新site.js
		new com.xnx3.wangmarket.admin.cache.Site().site(site,imService.getImByCache());
		
		//如果是PC端网站，需要刷新一些东西
		if(site.getClient() - Site.CLIENT_PC == 0){
			//刷新首页
			Index.updateSiteName(site, site.getName());
			
		}
		return vo;
	}

	/**
	 * 修改站点 Keywords
	 */
	@RequestMapping(value="updateKeywords${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO updateKeywords(Model model,HttpServletRequest request,
			@RequestParam(value = "keywords", required = false , defaultValue="") String keywords){
		BaseVO vo = new BaseVO();
		Site site = sqlService.findById(Site.class, getSiteId());
		site.setKeywords(filter(keywords));
		sqlService.save(site);
		
		AliyunLog.addActionLog(getSiteId(), "修改站点Keywords为："+site.getKeywords());
		
		//刷新首页
		Index.updateKeywords(site, keywords);
		
		//刷新site.js
		new com.xnx3.wangmarket.admin.cache.Site().site(site,imService.getImByCache());
		
		return vo;
	}
	

	/**
	 * 修改站点的SEO描述
	 */
	@RequestMapping(value="updateDescription${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO updateDescription(@RequestParam(value = "description", required = false , defaultValue="") String description,
			Model model,HttpServletRequest request){
		BaseVO vo = new BaseVO();
		Site site = sqlService.findById(Site.class, getSiteId());
		
		//获取其SiteData
		SiteData siteData = sqlService.findById(SiteData.class, site.getId());
		if(siteData == null){
			siteData = new SiteData();
			siteData.setId(site.getId());
			siteData.setIndexDescription(site.getName());
		}
		
		siteData.setIndexDescription(filter(description));
		sqlService.save(siteData);
		
		AliyunLog.addActionLog(getSiteId(), "修改站点Description为："+siteData.getIndexDescription());
		
		//刷新首页
		Index.updateDescription(site, siteData.getIndexDescription());
		return vo;
	}
	
	/**
	 * 修改站点绑定的域名
	 * @param siteid v2.1版本中以废弃，从Session中拿Site
	 */
	@RequestMapping(value="updateBindDomain${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO updateBindDomain(Model model,HttpServletRequest request,
			@RequestParam(value = "bindDomain", required = false , defaultValue="") String bindDomain){
		BaseVO vo = new BaseVO();
		
		bindDomain = StringUtil.filterXss(bindDomain);
		
		//v3.0版本更新，若不填写，则是绑定空的字符串，也就是解除之前的域名绑定！
		if(bindDomain.length() == 0){
			//为空，则是取消域名绑定
		}else{
			//查询此域名是否被绑定过了
			int scount = sqlService.count("site", "WHERE bind_domain = '"+bindDomain+"'");
			if(scount > 0){
				vo.setBaseVO(BaseVO.FAILURE, "此域名已经被绑定过了！");
				return vo;
			}
		}
		
		//v2.1更新，直接从Session中拿site.id
		Site site = sqlService.findById(Site.class, getSiteId());
		String oldBindDomain = site.getBindDomain();
		site.setBindDomain(bindDomain);
		sqlService.save(site);
		
		//更新域名服务器
		MQBean mqBean = new MQBean();
		mqBean.setType(MQBean.TYPE_BIND_DOMAIN);
		mqBean.setOldValue(oldBindDomain);
		mqBean.setSimpleSite(new SimpleSite(site));
		siteService.updateDomainServers(mqBean);
		
		//刷新Session缓存
		Func.getUserBeanForShiroSession().setSite(site);
		
		//刷新site.js
		new com.xnx3.wangmarket.admin.cache.Site().site(site,imService.getImByCache());
		
		AliyunLog.addActionLog(site.getId(), "修改站点绑定的域名为："+site.getBindDomain());
		return vo;
	}
	
	/**
	 * 刷新站点，重新创建HTML文件
	 * 高级功能中使用。已去掉高级功能，注释。以备后续使用
	 */
//	@RequestMapping(value="refreshSiteGenerateHtml", method = RequestMethod.POST)
//	public String refreshSiteGenerateHtml(Model model,HttpServletRequest request){
//		BaseVO baseVO = siteService.refreshSiteGenerateHtml(request);
//		if(baseVO.getResult() == BaseVO.SUCCESS){
//			AliyunLog.addActionLog(getSiteId(), "刷新站点，重新创建HTML文件");
//			return success(model, "刷新成功！");
//		}else{
//			return error(model, baseVO.getInfo());
//		}
//	}
	
	/**
	 * 站点属性，我的属性，我的信息
	 */
	@RequestMapping("popupBasicInfo${url.suffix}")
//	@RequestMapping("baseInfo")
	public String baseInfo(Model model,HttpServletRequest request){
		model.addAttribute("user", getUser());
		model.addAttribute("site", getSite());
		
		AliyunLog.addActionLog(getSiteId(), "查看我的站点属性信息");
		siteService.getTemplateCommonHtml(getSite(), "站点属性", model);
		return "site/baseInfo";
	}

	/**
	 * 站点属性，我的基本(信息)设置
	 */
//	@RequestMapping("baseSet")
	@RequestMapping("popupInfo${url.suffix}")
	public String baseSet(Model model,HttpServletRequest request){
		model.addAttribute("user", getUser());
		model.addAttribute("site", getSite());
		
		AliyunLog.addActionLog(getSiteId(), "弹出框口查看我的站点属性信息");
		
		
		siteService.getTemplateCommonHtml(getSite(), "站点属性", model);
		model.addAttribute("autoAssignDomain", G.getFirstAutoAssignDomain());
		return "site/baseSet";
	}
	
	/**
	 * 获取用户的OSS已用空间
	 */
	@RequestMapping(value="getOSSSize${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO getOSSSize(){
		//判断一下，如果是使用的阿里云OSS存储，但是没有配置，会拦截提示
		if(AttachmentFile.isMode(AttachmentFile.MODE_ALIYUN_OSS) && OSSUtil.getOSSClient() == null){
			return error("未开通阿里云OSS服务");
		}
		
		BaseVO vo = new BaseVO();
		//获取其下有多少网站
		List<Site> list = sqlService.findBySqlQuery("SELECT * FROM site WHERE userid = "+getUserId(), Site.class);
		//属于该用户的这些网站共占用了多少存储空间去
		long sizeB = 0;
		for (int i = 0; i < list.size(); i++) {
			sizeB += AttachmentFile.getDirectorySize("site/"+list.get(i).getId()+"/");
		}
		
		int kb = Math.round(sizeB/1024);
		String currentDate = DateUtil.currentDate("yyyyMMdd");
		sqlService.executeSql("UPDATE user SET oss_update_date = '"+currentDate+"' , oss_size = "+kb+" WHERE id = "+getUserId());
		vo.setBaseVO(BaseVO.SUCCESS, kb+"");
		
		AliyunLog.addActionLog(getSiteId(), "计算我当前网站使用了多少存储空间，已使用："+Lang.fileSizeToInfo(sizeB));
		
		ShiroFunc.setUEditorAllowUpload(kb<ShiroFunc.getUser().getOssSizeHave()*1000);
		return vo;
	}

	/**
	 * 获取当前站点信息 Site 信息
	 * @param siteid 以废弃，不需要
	 */
//	@RequestMapping("getSite")
//	@ResponseBody
//	public SiteVO getSitess(){
//		SiteVO vo = new SiteVO();
//		
//		//从数据库获取，拿到最新的站点信息
//		Site site = (Site) sqlService.findById(Site.class, getSiteId());
//		
//		AliyunLog.addActionLog(getSiteId(), "获取当前站点信息，已废弃接口");
//		
//		vo.setSite(site);
//		return vo;
//	}
	
	/**
	 * 获取当前站点信息 Site 信息
	 */
	@RequestMapping(value="getSiteData${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public SiteDataVO getSiteData(){
		SiteDataVO vo = new SiteDataVO();
		
		AliyunLog.addActionLog(getSiteId(), "获取当前站点信息");
		
		SiteData siteData = (SiteData) sqlService.findById(SiteData.class, getSiteId());
		vo.setSite(getSite());
		vo.setSiteData(siteData);
		return vo;
	}
	
	/**
	 * 更改网站的Domain二级域名
	 * @param siteid v2.1版本中以废弃，从Session中拿Site
	 */
	@RequestMapping(value="updateDomain${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO updateDomain(@RequestParam(value = "siteid", required = false , defaultValue="0") int siteid,
			@RequestParam(value = "domain", required = false , defaultValue="") String domain,
			Model model,HttpServletRequest request){
		BaseVO vo = new BaseVO();
		
		domain = filter(domain);
		//是否是不可使用的域名列表中的
		if(G.forbidDomain.indexOf(","+domain+",") > -1){
			vo.setBaseVO(BaseVO.FAILURE, "此域名不可使用！请更换再试");
			return vo;
		}
		
		//查询这个domain是否有使用的了
		int count = sqlService.count("site", "WHERE domain = '"+domain+"'");
		if(count > 0){
			vo.setBaseVO(BaseVO.FAILURE, "此域名已被使用！请更换再试");
			return vo;
		}
		
		//v2.1更新，直接从Session中拿siteid
		Site site = sqlService.findById(Site.class, getSiteId());
		String oldDomain = site.getDomain(); 
		site.setDomain(domain);
		sqlService.save(site);
		
		//更新域名服务器
		MQBean mqBean = new MQBean();
		mqBean.setType(MQBean.TYPE_DOMAIN);
		mqBean.setOldValue(oldDomain);
		mqBean.setSimpleSite(new SimpleSite(site));
		siteService.updateDomainServers(mqBean);
		
//		if(G.SITE_DEPLOYMODE_SHARE){
//			//更新域名服务器
//			siteService.updateDomainServers(site);
//		}else{
//			//更新当前的域名服务器
//			siteService.updateDomainServers(site);
//		}
		
		//刷新Session缓存
		Func.getUserBeanForShiroSession().setSite(site);
		
		//刷新site.js
		new com.xnx3.wangmarket.admin.cache.Site().site(site,imService.getImByCache());
		
		AliyunLog.addActionLog(site.getId(), "更换站点二级域名为："+site.getDomain());
		
		return vo;
	}

	/**
	 * 选择模版(包含通用的手机、电脑模版。不含CMS模版)
	 */
	@RequestMapping("templateList${url.suffix}")
	public String templateList(Model model,HttpServletRequest request){
		Site site = getSite();
		
		model.addAttribute("user", getUser());
		model.addAttribute("site", site);
		model.addAttribute("client", site.getClient());
		
		AliyunLog.addActionLog(site.getId(), "打开通用模式下选择模版页面");
		
		return "site/templateList";
	}
	
	/**
	 * 保存手机模版，同时刷新首页，重新生成首页的html
	 */
	@RequestMapping(value="templateSave${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO templateSave(Model model,HttpServletRequest request,
			@RequestParam(value = "templateId", required = false , defaultValue="0") int templateId){
		Site site = sqlService.findById(Site.class, getSiteId());
		
		if(templateId == 0){
			return error("请传入要使用的模版编号");
		}
		
		AliyunLog.addActionLog(site.getId(), "保存通用模式下选择的模版");
		
		//模版跟之前的模版不一样，才有修改的必要
		if(site.getTemplateId() != templateId){
			site.setTemplateId(templateId);
			SiteVO siteVO = null;
			if(site.getClient() - Site.CLIENT_WAP == 0){
				siteVO = siteService.saveSite(site, site.getUserid(), request);
			}else{
				siteVO = siteService.saveSite(site, site.getUserid(), request);
				//更新后，刷新PC首页重新生成
				BaseVO vo = siteService.refreshIndex(site); 
				if(vo.getResult() - BaseVO.FAILURE == 0){
					return error(vo.getInfo());
				}
			}
			
			if(siteVO.getResult() == SiteVO.SUCCESS){
				return success();
			}else{
				return error(siteVO.getInfo());
			}
		}else{
			return error("未选择新模版");
		}
	}
	
	/**
	 * HTML后缀捕获转发，主要用于电脑模式网站，可视化状态
	 * @param request {@link HttpServletRequest}
	 * @param htmlFile 访问的html文件，如访问c202.html ，则传入c202，会自动拼接上.html
	 */
	@RequestMapping("html${url.suffix}")
	public String html(HttpServletRequest request,Model model,
			@RequestParam(value = "htmlFile", required = false , defaultValue="") String htmlFile){
		if(htmlFile.equals("")){
			return error(model, "意外故障！");
		}
		
		Site site = getSite();
		if(site == null){
			return error(model, "登陆状态丢失，重新登陆");
		}
		String redirectUrl = "";	//跳转的url
		
		if(htmlFile.equals("index")){
			//是首页
			redirectUrl = "sitePc/index.do";
		}else if (htmlFile.indexOf("lc") > -1 && htmlFile.indexOf("_") > -1) {
			//是列表页面，获取到列表的id，SiteColumn.id
			String sid = Lang.subString(htmlFile, "lc", "_");
			int siteColumnId = Lang.stringToInt(sid, 0);
			if(siteColumnId > 0){
				redirectUrl = "news/listForTemplate.do?cid="+siteColumnId;
			}else{
				return error(model, "未获取到栏目编号");
			}
		}else if(htmlFile.indexOf("c") > -1){
			//是独立页面
			String sid = Lang.subString(htmlFile, "c", null);
			int siteColumnId = Lang.stringToInt(sid, 0);
			if(siteColumnId > 0){
				redirectUrl = "news/updateNewsByCid.do?cid="+siteColumnId;
			}else{
				return error(model, "未获取到栏目编号");
			}
		}else{
			//应该就是单页面了,提取页面id
			int newsId = Lang.stringToInt(htmlFile, 0);
			if(newsId > 0){
				redirectUrl = "news/news.do?id="+newsId;
			}else{
				System.out.println("未知页面 : "+htmlFile);
				return error(model, "未知页面");
			}
		}
		
		return redirect(redirectUrl);
	}
	
	/**
	 * 刷新，重新生成sitemap.xml文件
	 * @return
	 */
	@RequestMapping("refreshSitemap${url.suffix}")
	public String refreshSitemap(@RequestParam(value = "siteid", required = true) int siteid,
			Model model){
		
		Site site = sqlService.findAloneBySqlQuery("SELECT * FROM site WHERE id = "+siteid, Site.class);
		siteService.refreshSiteMap(site);
		
		AliyunLog.addActionLog(site.getId(), "刷新，重新生成sitemap.xml文件");
		
		return success(model, "成功");
	}
	
	/**
	 * 刷新，重新生成首页（PC）
	 * @return
	 */
	@RequestMapping("refreshIndex${url.suffix}")
	public String refreshIndex(Model model){
		BaseVO vo = siteService.refreshIndex(getSite()); 
		if(vo.getResult() - BaseVO.SUCCESS == 0){
			AliyunLog.addActionLog(getSiteId(), "通用电脑模式下，刷新，重新生成首页");
			return success(model, "刷新成功，若首页不是最新的，可按住CTRL＋F5 强制刷新后在看");
		}else{
			return error(model, vo.getInfo());
		}
	}
	
	/**
	 * 弹出框，站点信息修改
	 * 网站设置－修改联系信息，如地址、QQ等
	 */
	@RequestMapping("popupSiteUpdate${url.suffix}")
	public String popupSiteUpdate(Model model){
		AliyunLog.addActionLog(getSiteId(), "弹出弹出框，显示站点信息修改");
		
		model.addAttribute("site", getSite());
		return "site/popup_site";
	}
	
	/**
	 * 保存弹出框修改的站点信息
	 */
	@RequestMapping(value="savePopupSiteUpdate${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO savePopupSiteUpdate(Site s,Model model,HttpServletRequest request){
		Site site = sqlService.findById(Site.class, getSite().getId());
		site.setUsername(filter(s.getUsername()));
		site.setPhone(filter(s.getPhone()));
		site.setQq(filter(s.getQq()));
		site.setCompanyName(filter(s.getCompanyName()));
		site.setAddress(filter(s.getAddress()));
		SiteVO siteVO = siteService.saveSite(site, getUserId(), request);
		if(siteVO.getResult() == SiteVO.SUCCESS){
			AliyunLog.addActionLog(site.getId(), "保存弹出框修改的站点信息成功");
			return success();
		}else{
			return error(siteVO.getInfo());
		}
	}
	
	/**
	 * 通用电脑模式，更改底部的二维码，提交保存
	 */
	@RequestMapping(value = "popupQrImageUpdateSubmit${url.suffix}", method = RequestMethod.POST)
	public void popupQrImageUpdateSubmit(Model model,HttpServletRequest request,HttpServletResponse response,
			@RequestParam("qrImageFile") MultipartFile multipartFile) throws IOException{
		JSONObject json = new JSONObject();
		Site site = getSite();
		
		if(!(multipartFile.getContentType().equals("image/pjpeg") || multipartFile.getContentType().equals("image/jpeg") || multipartFile.getContentType().equals("image/png") || multipartFile.getContentType().equals("image/gif"))){
			json.put("result", "0");
			json.put("info", "请传入jpg、png、gif格式的二维码图");
		}else{
			//格式转换
			BufferedImage bufferedImage = ImageUtil.inputStreamToBufferedImage(multipartFile.getInputStream());
	        BufferedImage tag = ImageUtil.formatConversion(bufferedImage);
	        BufferedImage tag1 = ImageUtil.proportionZoom(tag, 400);
			
			//上传
	        AttachmentFile.put("site/"+site.getId()+"/images/qr.jpg", ImageUtil.bufferedImageToInputStream(tag1, "jpg"));
			
			AliyunLog.addActionLog(getSiteId(), "通用电脑模式，更改底部的二维码，提交保存");
			
			json.put("result", "1");
		}
		
		response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");  
	    PrintWriter out = null;  
	    try {  
	        out = response.getWriter();  
	        out.append(json.toString());
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (out != null) {  
	            out.close();  
	        }  
	    }  
	}
	
	/**
	 * 电脑模式，更改关于我们的图片　，提交
	 */
	@RequestMapping(value="popupAboutUsImageUpdateSubmit${url.suffix}", method = RequestMethod.POST)
	public void popupAboutUsImageUpdateSubmit(HttpServletRequest request,Model model,HttpServletResponse response,
			@RequestParam("titlePicFile") MultipartFile multipartFile){
		JSONObject json = new JSONObject();
		Site site = getSite();
		
		News news = (News) sqlService.findAloneBySqlQuery("SELECT news.* FROM news WHERE news.cid = "+getSite().getAboutUsCid(), News.class);
		if(news == null){
			//关于我们栏目未发现
			return;
		}
		
		UploadFileVO uploadFileVO = AttachmentFile.uploadImage("site/"+site.getId()+"/news/", request, "titlePicFile", G.NEWS_TITLEPIC_MAXWIDTH);
		String oldTitlePic = "";	//旧的栏目导航图名字
		if(uploadFileVO.getResult() == UploadFileVO.SUCCESS){
			oldTitlePic = (news.getTitlepic()==null||news.getTitlepic().length()==0)? "":news.getTitlepic();
			
			news.setTitlepic(uploadFileVO.getFileName());
			sqlService.save(news);
			
			//如果有旧图，删除掉旧的图片
			if(oldTitlePic.length() > 0 && (oldTitlePic.indexOf("http://") == -1 && oldTitlePic.indexOf("https://") == -1 && oldTitlePic.indexOf("//") == -1)){
				AttachmentFile.deleteObject("site/"+site.getId()+"/news/"+oldTitlePic);
			}
			
			//更新首页
			SiteColumn siteColumn = (SiteColumn) sqlService.findById(SiteColumn.class, news.getCid());
			NewsData newsData = (NewsData) sqlService.findById(NewsData.class, news.getId());
			IndexAboutUs.refreshIndexData(site, siteColumn, news, newsData == null ? "":newsData.getText());
			
			AliyunLog.addActionLog(getSiteId(), "通用电脑模式，关于我们图片更改成功");
			
			json.put("result", "1");
		}else{
			json.put("result", "0");
			json.put("info", uploadFileVO.getInfo());
		}
		
		
		response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");  
	    PrintWriter out = null;  
	    try {  
	        out = response.getWriter();  
	        out.append(json.toString());
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (out != null) {  
	            out.close();  
	        }  
	    }  
	}
	
	/**
	 * 删除当前网站内的文件，删除OSS存储的数据
	 * @param fileName 传入oss的key，如345.html、news/asd.jpg
	 */
	@RequestMapping(value="deleteOssData${url.suffix}", method = RequestMethod.POST)
	public void deleteOssData(Model model,HttpServletResponse response,
			@RequestParam(value = "fileName", required = true) String fileName){
		fileName = filter(fileName);
		AttachmentFile.deleteObject("site/"+getSiteId()+"/"+fileName);
		
		AliyunLog.addActionLog(getSiteId(), "删除当前网站内存储的文件："+fileName);
		
		JSONObject json = new JSONObject();
		json.put("result", "1");
		response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");  
	    PrintWriter out = null;  
	    try { 
	        out = response.getWriter();  
	        out.append(json.toString());
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (out != null) {  
	            out.close();  
	        }  
	    }  
	}
	
	
	/**
	 * 问题反馈
	 */
	@RequestMapping(value="wenTiFanKui${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO wenTiFanKui(@RequestParam(value = "text", required = false , defaultValue="") String text){
		if(text.length() == 0){
			return error("说点什么呢？");
		}
		
		Feedback fb = new Feedback();
		fb.setAddtime(DateUtil.timeForUnix10());
		fb.setUserid(getUserId());
		fb.setText(filter(text));
		sqlService.save(fb);
		if(fb.getId() != null && fb.getId() > 0){
			
			String jianjie = fb.getText();
			if(jianjie.length() > 30){
				jianjie = jianjie.substring(0, 30);
			}
			AliyunLog.addActionLog(getSiteId(), "提交问题反馈："+jianjie);
			
			//发送邮件
			MailUtil.sendMail(Global.get("SERVICE_MAIL"), "有新的问题反馈", fb.toString());
			
			return success();
		}else{
			return error("保存出错！请重试");
		}
	}
	
	/**
	 * 刷新，重新生成所有wap、pc模式网站。不过此接口只能是用户id为1的超级管理员使用
	 */
	@RequestMapping("refreshGenerateAllSite${url.suffix}")
	public String refreshGenerateAllSite(Model model,HttpServletRequest request){
		User user = getUser();
		if(user.getId() - 1 == 0){
			
			List<Site> list = sqlService.findBySqlQuery("SELECT * FROM site WHERE client = "+Site.CLIENT_WAP+" OR client = "+Site.CLIENT_PC, Site.class);			
			for (int i = 0; i < list.size(); i++) {
				Site site = list.get(i);
				
				Func.getUserBeanForShiroSession().setSite(site);
				try {
					System.out.println(siteService.refreshSiteGenerateHtml(request));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		return "asd";
	}
	
	/**
	 * 网站更改密码
	 * 注意，username中带有ceshi这个字符的，并且是以其为开头的，是不能更改密码的
	 * @param newPassword 要更改上的新密码
	 */
	@RequestMapping(value="updatePassword${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO updatePassword(HttpServletRequest request,
			@RequestParam(value = "newPassword", required = false, defaultValue="") String newPassword){
		User user = getUser();
		if(user.getUsername().indexOf("ceshi") == 0){
			return error("测试体验的网站无法修改密码！");
		}
		return userService.updatePassword(getUserId(), newPassword);
	}
	

	/**
	 * 上传图片接口
	 */
	@RequestMapping(value="uploadImage${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public UploadFileVO uploadImage(Model model,HttpServletRequest request){
		UploadFileVO uploadFileVO = AttachmentFile.uploadImage("site/"+getSiteId()+"/news/", request, "image", 0);
		
		if(uploadFileVO.getResult() == UploadFileVO.SUCCESS){
			//上传成功，写日志
			AliyunLog.addActionLog(getSiteId(), "内容管理上传图片成功："+uploadFileVO.getFileName(), uploadFileVO.getPath());
		}
		
		return uploadFileVO;
	}
	
	/**
	 * 弹出框，修改邮箱
	 * 网站设置－修改联系信息，如地址、QQ等
	 */
	@RequestMapping("popupUpdateEmailSave${url.suffix}")
	public String popupUpdateEmailSave(Model model){
		AliyunLog.addActionLog(getSiteId(), "弹出弹出框，修改邮箱","原本的邮箱:"+getUser().getEmail());
		
		model.addAttribute("user", getUser());
		return "site/popup_updateEmail";
	}
	
	/**
	 * 弹出框，绑定自己的域名
	 */
	@RequestMapping("popupBindDomain${url.suffix}")
	public String popupBindDomain(Model model,HttpServletRequest request){
		model.addAttribute("user", getUser());
		model.addAttribute("site", getSite());
		
		AliyunLog.addActionLog(getSiteId(), "弹出框口，绑定自己的域名");
		
		return "site/popup_bindDomain";
	}
	
	
	/**
	 * 系统管理-预览网站 v4.9
	 */
	@RequestMapping("sitePreview${url.suffix}")
	public String sitePreview(Model model,HttpServletRequest request){
		return redirect("index.html?domain="+getSite().getDomain()+"."+G.getFirstAutoAssignDomain());
	}
	
}