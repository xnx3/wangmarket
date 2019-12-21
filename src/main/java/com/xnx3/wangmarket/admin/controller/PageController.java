package com.xnx3.wangmarket.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.service.NewsService;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;

/**
 * 公共的
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/page")
public class PageController extends BaseController {
	@Resource
	private SqlService sqlService;
	@Resource
	private SiteService siteService;
	@Resource
	private NewsService newsService;

	/**
	 * 修改独立页面
	 * 以废弃，保留是为了兼容以前得。以后使用，用 NewsController.updateNewsByCid()
	 * @param cid {@link SiteColumn}.id
	 * @deprecated
	 */
	@RequestMapping("page${url.suffix}")
	public String page(HttpServletRequest request,
			@RequestParam(value = "cid", required = false , defaultValue="0") int cid,Model model){
		ActionLogUtil.insert(request, cid, "修改独立页面，已废弃的接口，不应在用");
		return redirect("news/updateNewsByCid.do?cid="+cid);
	}

	
//	/**
//	 * 自定义页面列表，可以获取当前网站所有的以html为后缀的页面
//	 * 临时用不到了，预留。没准什么时候用到
//	 */
//	@RequestMapping("customPageList.do")
//	public String customPageList(Model model){
//		List<OSSObjectSummary> allList = OSSUtil.getFolderObjectList("site/"+getSiteId()+"/");
//		List<OSSObjectSummary> htmlList = new ArrayList<OSSObjectSummary>();
//		for (int i = 0; i < allList.size(); i++) {
//			OSSObjectSummary obj = allList.get(i);
//			String suffix = Lang.findFileSuffix(obj.getKey());
//			if(suffix == null || !suffix.equals("html")){
//				continue;
//			}
//			obj.setKey(obj.getKey().replace("site/"+getSiteId()+"/", "").replace(".html", ""));
//			htmlList.add(obj);
//		}
//		
//		AliyunLog.addActionLog(getSiteId(), "打开当前网站所有以html为后缀的页面列表");
//		
//		siteService.getTemplateCommonHtml(getSite(), "自定义页面列表", model);
//		model.addAttribute("htmlList", htmlList);
//		return "page/customPageList";
//	}
}
