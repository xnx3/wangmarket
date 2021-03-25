package com.xnx3.wangmarket.superadmin.controller.admin;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.admin.entity.Site;

/**
 * News 文章管理
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/news")
public class AdminNewsController extends BaseController {
	@Resource
	private SqlService sqlService;
	
	/**
	 * 信息列表
	 */
	@RequiresPermissions("adminNewsList")
	@RequestMapping("list${url.suffix}")
	public String list(HttpServletRequest request, Model model){
		Sql sql = new Sql(request);
		sql.setSearchTable("news");
		sql.setSearchColumn(new String[]{"userid=","title","type=","status=","cid=","siteid=","legitimate="});
		int count = sqlService.count("news", sql.getWhere());
		Page page = new Page(count, G.PAGE_WAP_NUM, request);
		sql.setSelectFromAndPage("SELECT * FROM news", page);
		sql.setOrderBy("news.id DESC");
		List<News> list = sqlService.findBySql(sql, News.class);
		
		ActionLogUtil.insert(request, "总管理后台，News 文章管理，查看文章列表");
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "/superadmin/news/list";
	}
	
	/**
	 * 信息详情
	 * @param id News.id
	 */
	@RequiresPermissions("adminNewsView")
	@RequestMapping("view${url.suffix}")
	public String view(HttpServletRequest request,
			@RequestParam(value = "id", required = true , defaultValue="") int id, Model model){
		News news = sqlService.findById(News.class, id);
		if(news == null){
			return error(model, "信息不存在");
		}
		NewsData newsData = sqlService.findById(NewsData.class, id);
		
		Site site = sqlService.findById(Site.class, news.getSiteid());
		if(site == null){
			return error(model, "信息所属网站不存在");
		}
		
		ActionLogUtil.insert(request, news.getId(), "总管理后台，News 文章管理，查看文章文章详情", news.getTitle());
		model.addAttribute("text", StringUtil.filterXss(newsData.getText()));
		model.addAttribute("news", news);
		model.addAttribute("site", site);
		model.addAttribute("AttachmentFileUrl", AttachmentUtil.netUrl());
		return "/superadmin/news/view";
	}
	
	/**
	 * 删除文章
	 * @param id News.id
	 */
	@RequiresPermissions("adminNewsDelete")
	@RequestMapping("delete${url.suffix}")
	@ResponseBody
	public String delete(HttpServletRequest request,
			@RequestParam(value = "id", required = true , defaultValue="") int id, Model model){
		News news = sqlService.findById(News.class, id);
		if(news == null){
			return "信息不存在";
		}
		ActionLogUtil.insertUpdateDatabase(request, news.getId(), "总管理后台，News 文章管理，删除文章", news.getTitle());
		sqlService.delete(news);
		return success(model, "删除成功");
	}
	
	/**
	 * 取消违规标示，将其改为合法状态
	 * @param id News.id
	 */
	@RequiresPermissions("adminNewsCancelLegitimate")
	@RequestMapping("cancelLegitimate${url.suffix}")
	public String cancelLegitimate(HttpServletRequest request,
			@RequestParam(value = "id", required = true , defaultValue="") int id, Model model){
		News news = sqlService.findById(News.class, id);
		if(news == null){
			return "信息不存在";
		}
		news.setLegitimate(News.LEGITIMATE_OK);
		sqlService.save(news);
		ActionLogUtil.insertUpdateDatabase(request, news.getId(), "总管理后台，News 文章管理，取消违规标示，将其改为合法状态", news.getTitle());
		return success(model, "操作成功","admin/news/view.do?id="+id);
	}
	

	/**
	 * 查看此条文章的网站前端，对外的网站文章页面
	 * 传入要查看的news.id后，会自动重定向到网站对外展示的这个文章页面
	 * @param id News.id 要打开的那个文章页面的id编号
	 */
	@RequiresPermissions("adminNewsView")
	@RequestMapping("perview${url.suffix}")
	public String perview(HttpServletRequest request,
			@RequestParam(value = "id", required = true , defaultValue="0") int id, Model model){
		News news = sqlService.findById(News.class, id);
		if(news == null){
			return error(model, "信息不存在");
		}
		
		Site site = sqlService.findById(Site.class, news.getSiteid());
		if(site == null){
			return error(model, "信息所属网站不存在");
		}
		ActionLogUtil.insert(request, news.getId(), "查看此条文章的网站前端，对外的网站文章页面", news.getTitle());
		return redirect("http://"+Func.getDomain(site)+"/"+id+".html");
	}
}
