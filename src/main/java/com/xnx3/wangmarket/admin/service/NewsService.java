package com.xnx3.wangmarket.admin.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import com.xnx3.j2ee.util.Page;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.vo.NewsVO;
import com.xnx3.wangmarket.admin.vo.bean.NewsInit;

/**
 * 新闻、图文相关
 * @author 管雷鸣
 */
public interface NewsService {
	
	/**
	 * 传入siteColumn，根据传入的栏目id，生成其静态列表页面
	 * @param siteColumnId {@link SiteColumn}.id
	 * @param newsList 此栏目下所有的信息
	 */
	public void generateListHtml(Site site, SiteColumn siteColumn,List<News> newsList, HttpServletRequest request);
	
	/**
	 * 传入siteColumn，根据传入的栏目id，生成其静态列表页面
	 * @param siteColumnId {@link SiteColumn}.id
	 */
	public void generateListHtml(Site site, SiteColumn siteColumn);
	
	/**
	 * 编辑模式下获得图文、新闻列表页面的HTML内容
	 * @param page {@link Page}
	 * @param siteColumn {@link SiteColumn}
	 * @param count 总条数
	 * @param list 当前列表页面的{@link News}
	 * @param site {@link Site}
	 * @return HTML源代码
	 */
	public String generateListHtml(Page page, SiteColumn siteColumn, int count, List<News> list, Site site);
	
	/**
	 * 通过普通、通用模版，生成内容详情页面，News的页面，包含独立页面、新闻详情、图文详情
	 * @param site 当前站点的site
	 * @param news 要生成的详情页的 {@link News}
	 * @param newsDataBean news_data 的整理及数据初始化
	 */
	public void generateViewHtml(Site site,News news, SiteColumn siteColumn, NewsDataBean newsDataBean, HttpServletRequest request);
	
	/**
	 * 通过高级自定义模版，生成内容详情页面，News的页面，包含独立页面、新闻详情、图文详情
	 * @param news 要生成的详情页的 {@link News}
	 * @param siteColumn 要生成的详情页所属的栏目 {@link SiteColumn}
	 * @param newsDataBean news_data 的整理及数据初始化
	 */
	public void generateViewHtmlForTemplate(Site site, News news, SiteColumn siteColumn, NewsDataBean newsDataBean, HttpServletRequest request);
	
	/**
	 * 新闻、独立页面、图文详情页面的内容text,在用户编辑完成后保存时，将附件、图片等OSS存储的资源路径替换，将 "http://......com/site/14/" 替换为 {prefixUrl}  
	 * @param text 内容详情
	 * @param siteId 站点的id
	 */
	public String setText(String text, Site site);
	
	/**
	 * 添加、修改新闻／图文的数据初始化
	 * @param request
	 * @param id news.id
	 * @param cid news.cid
	 * @param model
	 * @return 网页显示的标题，是添加还是修改
	 */
	public NewsInit news(HttpServletRequest request, int id,int cid,Model model);
	
	/**
	 * 根据 {@link News}.id删除新闻(无刷新html页，列表页、首页)
	 * @param id News.id
	 * @param authCheck 是否开启身份验证，验证此信息是本人发布
	 * @param 如果删除成功，会返回删除的News对象
	 */
	public NewsVO deleteNews(int id ,boolean authCheck);
}
