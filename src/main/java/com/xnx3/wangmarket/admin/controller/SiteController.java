package com.xnx3.wangmarket.admin.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.SystemUtil;
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
import com.xnx3.wangmarket.admin.util.ActionLogUtil;
import com.xnx3.wangmarket.admin.util.SessionUtil;
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
//	@Resource
//	private ImService imService;
	
	/**
	 * 站点信息提交保存
	 */
	@RequestMapping(value="saveSite${url.suffix}", method = RequestMethod.POST)
	public String saveSite(Site s,Model model,HttpServletRequest request){
		SiteVO siteVO = siteService.saveSite(s, getUserId(), request);
		if(siteVO.getResult() == SiteVO.SUCCESS){
			ActionLogUtil.insert(request, "保存网站信息",s.toString());
			return success(model, s.getId()>0? "保存网站成功！":"创建网站成功！", Func.getConsoleRedirectUrl());
		}else{
			return error(model, "保存失败！");
		}
	}
//	
//	@RequestMapping(value="saveSite${url.suffix}", method = RequestMethod.POST)
//	public String saveSite(Site s,Model model,HttpServletRequest request){
//		SiteVO siteVO = siteService.saveSite(s, getUserId(), request);
//		if(siteVO.getResult() == SiteVO.SUCCESS){
//			ActionLogUtil.insert(request, "保存网站信息",s.toString());
//			return success(model, s.getId()>0? "保存网站成功！":"创建网站成功！", Func.getConsoleRedirectUrl());
//		}else{
//			return error(model, "保存失败！");
//		}
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
		site.setName(StringUtil.filterXss(name));
		sqlService.save(site);
		
		ActionLogUtil.insertUpdateDatabase(request, "修改站点名字", site.getName());
		
		//更新当前Session缓存
		SessionUtil.setSite(site);
		//如果是PC端网站，需要刷新一些东西,不过这个已废弃，好不容易写的，还是保留吧
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
		
		ActionLogUtil.insertUpdateDatabase(request, "修改站点Keywords", site.getKeywords());
		
		//刷新首页
		Index.updateKeywords(site, keywords);
		
		//刷新site.js ， pc、wap用到，已废弃
//		new com.xnx3.wangmarket.admin.cache.Site().site(site,imService.getImByCache());
		
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
		
		ActionLogUtil.insertUpdateDatabase(request, "修改站点Description", siteData.getIndexDescription());
		
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
		SessionUtil.setSite(site);
		ActionLogUtil.insertUpdateDatabase(request, "修改站点绑定的域名", site.getBindDomain());
		return vo;
	}
	
	/**
	 * 站点属性，我的属性，我的信息
	 */
	@RequestMapping("popupBasicInfo${url.suffix}")
	public String baseInfo(Model model,HttpServletRequest request){
		model.addAttribute("user", getUser());
		model.addAttribute("site", getSite());
		
		ActionLogUtil.insert(request, "查看我的站点属性信息");
		siteService.getTemplateCommonHtml(getSite(), "站点属性", model);
		return "site/baseInfo";
	}

	/**
	 * 站点属性，我的基本(信息)设置
	 */
	@RequestMapping("popupInfo${url.suffix}")
	public String baseSet(Model model,HttpServletRequest request){
		model.addAttribute("user", getUser());
		model.addAttribute("site", getSite());
		
		ActionLogUtil.insert(request, "弹出框口查看我的站点属性信息");
		
		siteService.getTemplateCommonHtml(getSite(), "站点属性", model);
		model.addAttribute("autoAssignDomain", G.getFirstAutoAssignDomain());
		return "site/baseSet";
	}
	
	/**
	 * 获取用户的OSS已用空间
	 */
	@RequestMapping(value="getOSSSize${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO getOSSSize(HttpServletRequest request){
		//判断一下，如果是使用的阿里云OSS存储，但是没有配置，会拦截提示
		if(AttachmentUtil.isMode(AttachmentUtil.MODE_ALIYUN_OSS) && OSSUtil.getOSSClient() == null){
			return error("未开通阿里云OSS服务");
		}
		
		BaseVO vo = new BaseVO();
		//获取其下有多少网站
		Site site = sqlService.findById(Site.class, SessionUtil.getSite().getId());
		//网站共占用了多少存储空间
		long sizeB = AttachmentUtil.getDirectorySize("site/"+site.getId()+"/");
		
		int kb = Math.round(sizeB/1024);
		String currentDate = DateUtil.currentDate("yyyyMMdd");
		sqlService.executeSql("UPDATE site SET attachment_update_date = '"+currentDate+"' , attachment_size = "+kb+" WHERE id = "+site.getId());
		vo.setBaseVO(BaseVO.SUCCESS, kb+"");
		
		ActionLogUtil.insertUpdateDatabase(request, "计算我当前网站使用了多少存储空间", "已使用："+Lang.fileSizeToInfo(sizeB));
		
		SessionUtil.setAllowUploadForUEditor(kb < SessionUtil.getSite().getAttachmentSizeHave()*1000);
		return vo;
	}

	/**
	 * 获取当前站点信息 Site 信息
	 */
	@RequestMapping(value="getSiteData${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public SiteDataVO getSiteData(HttpServletRequest request){
		SiteDataVO vo = new SiteDataVO();
		SiteData siteData = (SiteData) sqlService.findById(SiteData.class, getSiteId());
		vo.setSite(getSite());
		vo.setSiteData(siteData);
		
		ActionLogUtil.insert(request, "获取当前站点信息", siteData != null? siteData.toString():"");
		return vo;
	}
	
	/**
	 * 更改网站的Domain二级域名
	 * @param siteid v2.1版本中以废弃，从Session中拿Site
	 */
	@RequestMapping(value="updateDomain${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO updateDomain(HttpServletRequest request,
			@RequestParam(value = "domain", required = false , defaultValue="") String domain){
		BaseVO vo = new BaseVO();
		
		domain = filter(domain);
		//是否是不可使用的域名列表中的
		//system表中，保留不给用户申请的二级域名。v4.11增加，多个用|分割，且填写字符必须小写，如 m|wap|www  如果留空不填则无保留域名
		String forbidDomain = SystemUtil.get("FORBID_DOMAIN");	
		if(forbidDomain != null && forbidDomain.length() > 0){
			String[] fs = forbidDomain.split("\\|");
			for (int i = 0; i < fs.length; i++) {
				if(fs[i] != null && fs[i].length() > 0){
					if(domain.toLowerCase().equals(fs[i])){
						vo.setBaseVO(BaseVO.FAILURE, fs[i]+" 此域名属于系统保留域名，不可使用！请更换一个吧");
						return vo;
					}
				}
			}
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
		
		//刷新Session缓存
		SessionUtil.setSite(site);
		
		ActionLogUtil.insertUpdateDatabase(request, "更换站点二级域名", site.getDomain());
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
		
		ActionLogUtil.insert(request, "打开通用模式下选择模版页面");
		
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
		
		ActionLogUtil.insertUpdateDatabase(request, templateId, "保存通用模式下选择的模版");
		
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
				return error(model, "未知页面");
			}
		}
		
		ActionLogUtil.insert(request, "HTML后缀捕获转发，主要用于电脑模式网站，可视化状态");
		return redirect(redirectUrl);
	}
	
	/**
	 * 刷新，重新生成sitemap.xml文件
	 * @return
	 */
	@RequestMapping("refreshSitemap${url.suffix}")
	public String refreshSitemap(@RequestParam(value = "siteid", required = true) int siteid,
			Model model, HttpServletRequest request){
		
		Site site = sqlService.findAloneBySqlQuery("SELECT * FROM site WHERE id = "+siteid, Site.class);
		siteService.refreshSiteMap(site);
		
		ActionLogUtil.insert(request, "刷新，重新生成sitemap.xml文件");
		
		return success(model, "成功");
	}
	
	/**
	 * 刷新，重新生成首页（PC）
	 * @return
	 */
	@RequestMapping("refreshIndex${url.suffix}")
	public String refreshIndex(Model model, HttpServletRequest request){
		BaseVO vo = siteService.refreshIndex(getSite()); 
		if(vo.getResult() - BaseVO.SUCCESS == 0){
			ActionLogUtil.insert(request, "通用电脑模式下，刷新，重新生成首页");
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
	public String popupSiteUpdate(Model model, HttpServletRequest request){
		ActionLogUtil.insert(request, "弹出弹出框，显示站点信息修改");
		
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
			ActionLogUtil.insert(request, "保存弹出框修改的站点信息成功");
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
	        AttachmentUtil.put("site/"+site.getId()+"/images/qr.jpg", ImageUtil.bufferedImageToInputStream(tag1, "jpg"));
			
	        ActionLogUtil.insert(request, "通用电脑模式，更改底部的二维码，提交保存");
			
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
		
		UploadFileVO uploadFileVO = AttachmentUtil.uploadImage("site/"+site.getId()+"/news/", request, "titlePicFile", G.NEWS_TITLEPIC_MAXWIDTH);
		String oldTitlePic = "";	//旧的栏目导航图名字
		if(uploadFileVO.getResult() == UploadFileVO.SUCCESS){
			oldTitlePic = (news.getTitlepic()==null||news.getTitlepic().length()==0)? "":news.getTitlepic();
			
			news.setTitlepic(uploadFileVO.getFileName());
			sqlService.save(news);
			
			//如果有旧图，删除掉旧的图片
			if(oldTitlePic.length() > 0 && (oldTitlePic.indexOf("http://") == -1 && oldTitlePic.indexOf("https://") == -1 && oldTitlePic.indexOf("//") == -1)){
				AttachmentUtil.deleteObject("site/"+site.getId()+"/news/"+oldTitlePic);
			}
			
			//更新首页
			SiteColumn siteColumn = (SiteColumn) sqlService.findById(SiteColumn.class, news.getCid());
			NewsData newsData = (NewsData) sqlService.findById(NewsData.class, news.getId());
			IndexAboutUs.refreshIndexData(site, siteColumn, news, newsData == null ? "":newsData.getText());
			
			ActionLogUtil.insertUpdateDatabase(request, "通用电脑模式，关于我们图片更改成功");
			
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
	public void deleteOssData(Model model,HttpServletResponse response,HttpServletRequest request,
			@RequestParam(value = "fileName", required = true) String fileName){
		fileName = filter(fileName);
		AttachmentUtil.deleteObject("site/"+getSiteId()+"/"+fileName);
		
		ActionLogUtil.insert(request, "删除当前网站内存储的文件："+fileName);
		
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
	public BaseVO wenTiFanKui(HttpServletRequest request,
			@RequestParam(value = "text", required = false , defaultValue="") String text){
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
			ActionLogUtil.insertUpdateDatabase(request, "提交问题反馈", jianjie);
			
			//发送邮件
			MailUtil.sendMail(SystemUtil.get("SERVICE_MAIL"), "有新的问题反馈", fb.toString());
			
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
				
				SessionUtil.setSite(site);
				try {
					System.out.println(siteService.refreshSiteGenerateHtml(request));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		ActionLogUtil.insert(request, "刷新，重新生成所有wap、pc模式网站。不过此接口只能是用户id为1的超级管理员使用");
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
		ActionLogUtil.insertUpdateDatabase(request, "网站更改密码", newPassword);
		return userService.updatePassword(getUserId(), newPassword);
	}
	

	/**
	 * 上传图片接口
	 */
	@RequestMapping(value="uploadImage${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public UploadFileVO uploadImage(Model model,HttpServletRequest request){
		UploadFileVO uploadFileVO = AttachmentUtil.uploadImage("site/"+getSiteId()+"/news/", request, "image", 0);
		
		if(uploadFileVO.getResult() == UploadFileVO.SUCCESS){
			//上传成功，写日志
			ActionLogUtil.insert(request, "网站上传图片", uploadFileVO.getPath());
		}
		
		return uploadFileVO;
	}
	
	/**
	 * 弹出框，修改邮箱
	 * 网站设置－修改联系信息，如地址、QQ等
	 */
	@RequestMapping("popupUpdateEmailSave${url.suffix}")
	public String popupUpdateEmailSave(HttpServletRequest request, Model model){
		ActionLogUtil.insert(request, "弹出弹出框，修改邮箱");
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
		ActionLogUtil.insert(request, "弹出框口，绑定自己的域名");
		
		return "site/popup_bindDomain";
	}
	
	
	/**
	 * 系统管理-预览网站 v4.9
	 */
	@RequestMapping("sitePreview${url.suffix}")
	public String sitePreview(Model model,HttpServletRequest request){
		ActionLogUtil.insert(request, "系统管理-预览网站", getSite().getDomain()+"."+G.getFirstAutoAssignDomain());
		return redirect("index.html?domain="+getSite().getDomain()+"."+G.getFirstAutoAssignDomain());
	}
	
}