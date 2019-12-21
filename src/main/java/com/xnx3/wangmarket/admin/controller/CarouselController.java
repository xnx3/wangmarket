package com.xnx3.wangmarket.admin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.entity.Carousel;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.service.CarouselService;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;

/**
 * 轮播广告，轮播图
 * @author 管雷鸣
 * @deprecated wap、pc模式使用的，现在前两种模式废除了，都使用CMS模式
 */
@Controller
@RequestMapping("carousel")
public class CarouselController extends BaseController {
	@Resource
	private SqlService sqlService;
	@Resource
	private CarouselService carouselService;
	@Resource
	private SiteService siteService;

	/**
	 * 列表
	 */
	@RequestMapping("list${url.suffix}")
	public String list(HttpServletRequest request,Model model){
		Sql sql = new Sql(request);
		sql.setSearchColumn(new String[]{"value","isshow=","siteid="});
		sql.appendWhere("userid = "+getUserId());
		int count = sqlService.count("carousel", sql.getWhere());
		Page page = new Page(count, SystemUtil.getInt("LIST_EVERYPAGE_NUMBER"), request);
		sql.setSelectFromAndPage("SELECT * FROM carousel", page);
		sql.setDefaultOrderBy("id DESC");
		List<Carousel> list = sqlService.findBySql(sql, Carousel.class);
		
		ActionLogUtil.insert(request, "查看轮播图列表", "第"+page.getCurrentPageNumber()+"页");
		
		model.addAttribute("page", page);
		model.addAttribute("list", list);
		return "/carousel/"+getPcOrWapPrefix(request)+"list";
	}
	
	/**
	 * 添加、编辑轮播
	 */
	@RequestMapping("carousel${url.suffix}")
	public String carousel(HttpServletRequest request, Model model,
			@RequestParam(value = "id" , defaultValue="0" , required = false) int id ,
			@RequestParam(value = "siteid" , defaultValue="0" , required = false) int siteid){
		Site site = getSite();
		String title = "";
		if(id > 0){
			Carousel carousel = sqlService.findById(Carousel.class, id);
			if(carousel == null){
				return error(model, "要修改得轮播图不存在");
			}
			if(carousel.getSiteid() - site.getId() != 0){
				return error(model, "此图所属的站点不属于您，无法操作！");
			}
			siteid = carousel.getSiteid();
			String image = carousel.getImage().indexOf("://")==-1? AttachmentUtil.netUrl()+"site/"+getSiteId()+"/carousel/"+carousel.getImage():carousel.getImage();
			title = "修改轮播图";
			ActionLogUtil.insert(request, carousel.getId(), "进入修改轮播图页面", carousel.getUrl());
			
			model.addAttribute("carousel", carousel);
			model.addAttribute("imageImage", "<img src=\""+image+"\" height=\"30\">");
		}else{
			//新增
			if(siteid == 0){
				return error(model, "如果是新增图，请传入要新增到哪个站点");
			}
			title = "添加轮播图";
			ActionLogUtil.insert(request, 0, "进入添加轮播图页面");
			
			model.addAttribute("imageImage", "");
		}
		
		
		siteService.getTemplateCommonHtml(site, title, model);
		
		model.addAttribute("site", site);
		return "/carousel/"+getPcOrWapPrefix(request)+"carousel";
	}
	
	/**
	 * 提交保存
	 */
	@RequestMapping("carouselSubmit${url.suffix}")
	public String carouselSubmit(Carousel carousel , 
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
			HttpServletRequest request,Model model){
		//轮播图所属站点，是否是属于这个人
		if(!(carousel.getSiteid() != null || carousel.getSiteid() > 0)){
			return error(model, "此图属于哪个站点呢？");
		}
		Site site = sqlService.findById(Site.class, carousel.getSiteid());
		if(site == null){
			return error(model, "此图所属的站点不存在！");
		}
		if(site.getUserid() != getUserId()){
			return error(model, "此图所属的站点不属于您，无法操作！");
		}
		
		Carousel c = new Carousel();
		boolean add = false;
		if(!(carousel.getId() == null || carousel.getId() == 0)){
			c = sqlService.findById(Carousel.class, carousel.getId());
			if(c == null){
				return error(model, "要修改的轮播图不存在");
			}
		}else{
			add = true;
			c.setAddtime(DateUtil.timeForUnix10());
			c.setUserid(getUserId());
			c.setType(carousel.getType());
		}
		
		//轮播图上传/更新
		String oldImage = null;
		if(!imageFile.isEmpty()){
			//判断，如果是PC端的话，不用进行压缩
			UploadFileVO uploadFile;
			if(site.getClient() - Site.CLIENT_PC == 0){
				uploadFile = AttachmentUtil.uploadImageByMultipartFile(G.getCarouselPath(site), imageFile, 0);
			}else{
				uploadFile = AttachmentUtil.uploadImageByMultipartFile(G.getCarouselPath(site), imageFile, G.CAROUSEL_MAXWIDTH);
			}
			if(uploadFile.getResult() == UploadFileVO.FAILURE){
				return error(model, uploadFile.getInfo());
			}
			
			oldImage = c.getImage();
			c.setImage(uploadFile.getFileName());
		}
		c.setIsshow(carousel.getIsshow());
		c.setRank(carousel.getRank());
		c.setSiteid(carousel.getSiteid());
		c.setUrl(filter(carousel.getUrl()));
		sqlService.save(c);
		if(c.getId() > 0){
			if(add){
				ActionLogUtil.insertUpdateDatabase(request, c.getId(),"添加轮播图", c.getUrl());
			}else{
				ActionLogUtil.insertUpdateDatabase(request, c.getId(),"修改轮播图", c.getUrl());
				
				//删除之前传的那个图片文件
				if(!(oldImage == null || oldImage.length() == 0)){
					if(oldImage.indexOf("/") == -1){
						AttachmentUtil.deleteObject(G.getCarouselPath(site)+oldImage);
					}
				}
			}
			ActionLogUtil.insertUpdateDatabase(request, c.getId(), (add?"添加":"修改")+"轮播图页面", c.getUrl());
			//更新数据缓存
			new com.xnx3.wangmarket.admin.cache.Site().carousel(carouselService.findBySiteid(c.getSiteid()),site);
		}
		
		String pw = siteService.isPcClient(site)? "Pc":"Wap";
		return success(model, "保存成功" ,"site/edit"+pw+"Index.do?siteid="+site.getId());
	}
	
	/**
	 * 计划废弃，修改轮播图时将鼠标放到轮播图上，会自动出现跟随文字指导
	 * 通过siteid更改carousel图，只限一个站点有一张图的情况, wap模式建站／ （可能pc模式也用到）
	 */
	@RequestMapping("updateBySiteId${url.suffix}")
	public String updateBySiteId(HttpServletRequest request,Model model){
		Site site = getSite();
		
		Carousel carousel = sqlService.findAloneBySqlQuery("SELECT * FROM carousel WHERE siteid = "+site.getId(), Carousel.class);
		if(carousel == null){
			//意外情况，正常情况下，轮播图是不能不存在的,这个不存在，那么新建一个
			carousel = new Carousel();
			carousel.setAddtime(DateUtil.timeForUnix10());
			carousel.setIsshow(Carousel.ISSHOW_SHOW);
			carousel.setRank(1);
			carousel.setSiteid(site.getId());
			carousel.setType(Carousel.TYPE_INDEXBANNER);
			carousel.setUserid(site.getUserid());
			carousel.setImage("");
			
			//记录意外日志
			ActionLogUtil.insertError(request, "网站id("+site.getId()+")无默认轮播图");
		}
		String image = carousel.getImage().indexOf("://")==-1? AttachmentUtil.netUrl()+"site/"+site.getId()+"/carousel/"+carousel.getImage():carousel.getImage();
		
		ActionLogUtil.insert(request, carousel.getId() != null? carousel.getId():0, "进入修改站点公用的轮播图页面", carousel.getUrl());
		
		model.addAttribute("carousel", carousel);
		model.addAttribute("imageImage", "<img src=\""+image+"\" height=\"30\">");
		model.addAttribute("site", site);
		return "carousel/"+getPcOrWapPrefix(request)+"carousel";
	}
	
	/**
	 * 修改轮播图，一个网站一个图
	 * @param type 类型
	 * 			<ul>
	 * 				<li>1:默认，不传递也默认是1，内页通用的banner</li>
	 * 				<li>2:只首页用的banner</li>
	 * 			</ul>
	 * @return
	 */
	@RequestMapping("popupCarouselUpdate${url.suffix}")
	public String popupCarouselUpdate(HttpServletRequest request,Model model,
			@RequestParam(value = "type", required = false , defaultValue="1") int type){
		Carousel carousel = sqlService.findAloneBySqlQuery("SELECT * FROM carousel WHERE siteid = "+getSiteId() +" AND type = "+type, Carousel.class);
		if(carousel == null && type - Carousel.TYPE_INDEXBANNER == 0){
			//如果是查询首页的时候出现了空数据，那么，就自动复制一份内页banner的到首页
			Carousel defaultC = carouselService.findAloneBySiteid(getSiteId(), Carousel.TYPE_DEFAULT_PAGEBANNER);
			if(defaultC == null){
				return error(model, "该站点无内页轮播图，请联系管理说明");
			}else{
				carousel = new Carousel();
				carousel.setAddtime(DateUtil.timeForUnix10());
				carousel.setImage(defaultC.getImage());
				if(defaultC.getImage() != null && defaultC.getImage().length() > 3 && defaultC.getImage().indexOf("http://") == -1){
					//如果是单独上传的图，需要将此图也复制一份
					AttachmentUtil.copyObject("site/"+getSiteId()+"/carousel/"+defaultC.getImage(), "site/"+getSiteId()+"/carousel/"+"c"+defaultC.getImage());
					carousel.setImage("c"+defaultC.getImage());
				}
				carousel.setIsshow(Carousel.ISSHOW_SHOW);
				carousel.setRank(1);
				carousel.setSiteid(defaultC.getSiteid());
				carousel.setType(Carousel.TYPE_INDEXBANNER);
				carousel.setUrl(defaultC.getUrl());
				carousel.setUserid(defaultC.getUserid());
				sqlService.save(carousel);
				if(carousel.getId() != null && carousel.getId() > 0){
					//正常流程
					
				}else{
					return error(model, "自动创建首页Banner失败，请联系管理");
				}
			}
			
//			return error(model, "当前站点无"+(type == 1 ? "内页/通用":"首页")+"轮播图");
		}
		
		ActionLogUtil.insert(request, carousel.getId(), "进入修改轮播图页面", carousel.getUrl());
		
		String image = carousel.getImage().indexOf("://")==-1? AttachmentUtil.netUrl()+"site/"+getSiteId()+"/carousel/"+carousel.getImage():carousel.getImage();
		model.addAttribute("carousel", carousel);
		model.addAttribute("image", image);
		return "/carousel/popup_carousel";
	}
	

	/**
	 * 修改轮播图，一个网站一个图,PC
	 * @return
	 */
	@RequestMapping(value="popupCarouselUpdateSubmit${url.suffix}", method = RequestMethod.POST)
	public void popupCarouselUpdateSubmit(HttpServletRequest request,Model model,HttpServletResponse response,
			@RequestParam("imageFile") MultipartFile imageFile,
			@RequestParam(value = "id", required = true) int id){
		Site site = getSite();
		JSONObject json = new JSONObject();
		Carousel carousel = sqlService.findById(Carousel.class, id);
		if(carousel == null){
			json.put("result", "0");
			json.put("info", "当前站点无轮播图，修改失败");
		}else{
			if(carousel.getSiteid() - site.getId() != 0){
				json.put("result", "0");
				json.put("info", "图不属于你，无法修改");
			}else{

				//轮播图上传/更新
				String oldImage = null;
				if(!imageFile.isEmpty()){
					
					//判断，如果是PC端的话，不用进行压缩
					UploadFileVO uploadFile = AttachmentUtil.uploadImageByMultipartFile(G.getCarouselPath(site), imageFile, 0);
					if(uploadFile.getResult() == UploadFileVO.FAILURE){
						json.put("result", "0");
						json.put("info", uploadFile.getInfo());
					}else{
						oldImage = carousel.getImage();
						carousel.setImage(uploadFile.getFileName());
						
						sqlService.save(carousel);
						ActionLogUtil.insertUpdateDatabase(request, carousel.getId(), "修改轮播图", carousel.getUrl());
						
						//删除之前传的那个图片文件
						if(!(oldImage == null || oldImage.length() == 0)){
							if(oldImage.indexOf("/") == -1){
								AttachmentUtil.deleteObject(G.getCarouselPath(site)+oldImage);
							}
						}
						
						//更新数据缓存
						new com.xnx3.wangmarket.admin.cache.Site().carousel(carouselService.findBySiteid(getSiteId()),site);
						
						json.put("result", "1");
					}
				}else{
					json.put("result", "0");
					json.put("info", "未上传图片");
				}
			}
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
	 * 修改轮播图，一个网站一个图的情况，修改banner时，直接点击选择图片进行上传。适用于 模板 1~6 的网站
	 */
	@RequestMapping(value="updateSubmitForAloneCarousel${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO updateSubmitForAloneCarousel(HttpServletRequest request,Model model,HttpServletResponse response,
			@RequestParam("imageFile") MultipartFile imageFile){
		Site site = getSite();
		Carousel carousel = sqlService.findAloneBySqlQuery("SELECT * FROM carousel WHERE siteid = "+site.getId()+" AND type = "+Carousel.TYPE_DEFAULT_PAGEBANNER+" ORDER BY rank ASC ", Carousel.class);
		if(carousel == null){
			//意外情况，正常情况下，轮播图是不能不存在的,这个不存在，那么新建一个
			carousel = new Carousel();
			carousel.setAddtime(DateUtil.timeForUnix10());
			carousel.setIsshow(Carousel.ISSHOW_SHOW);
			carousel.setRank(1);
			carousel.setSiteid(site.getId());
			carousel.setType(Carousel.TYPE_DEFAULT_PAGEBANNER);
			carousel.setUserid(site.getUserid());
			carousel.setImage("");
			
			//记录意外日志
			ActionLogUtil.insertError(request, "网站id("+site.getId()+")无默认轮播图");
		}
	
		//轮播图上传/更新
		String oldImage = null;
		if(!imageFile.isEmpty()){
			//上传了图片，进行判断
			UploadFileVO uploadFile = AttachmentUtil.uploadImageByMultipartFile(G.getCarouselPath(site), imageFile, 0);
			if(uploadFile.getResult() == UploadFileVO.SUCCESS){
				//上传成功
				//记下旧图片的地址，以便删除
				oldImage = carousel.getImage() != null ? carousel.getImage() : "";
				carousel.setImage(uploadFile.getFileName());
				sqlService.save(carousel);
				
				ActionLogUtil.insertUpdateDatabase(request, carousel.getId(), "修改Banner", carousel.getUrl());
				
				//删除之前传的那个图片文件
				if(!(oldImage == null || oldImage.length() == 0)){
					if(oldImage.indexOf("/") == -1){
						AttachmentUtil.deleteObject(G.getCarouselPath(site)+oldImage);
					}
				}
				
				//更新JS缓存
				List<Carousel> list = new ArrayList<Carousel>();
				list.add(carousel);
				new com.xnx3.wangmarket.admin.cache.Site().carousel(list, site);
				
				return success();
			}else{
				return error(uploadFile.getInfo());
			}
	
		}else{
			return error("上传的图片不存在");
		}
	}
	
	
}
