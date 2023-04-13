package com.xnx3.wangmarket.admin.pluginManage.interfaces;

import javax.servlet.http.HttpServletRequest;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.vo.NewsVO;

/**
 * 内容管理-编辑内容时的处理
 * @author 管雷鸣
 */
public interface NewsEditInterface {
	
	/**
	 * 在内容管理-编辑内容时，右侧可以展开更多编辑项，这个就是在编辑项上进行的追加html的输入项
	 * 注意，追加的js是在html最末尾追加的
	 * @param news 当前编辑的文章，对应 news 数据表
	 * @param siteColumn 当前编辑文章所属的栏目
	 * @param site 当前编辑的文章所属的站点
	 * @return 要追加的html
	 */
	public String newsEditAppendHtml(News news, SiteColumn siteColumn, Site site);
	
	/**
	 * 拦截 News 进行预处理。这里是在保存入数据库之前拦截下来，进行处理，处理完后将其存入数据库
	 * 在以下情况，触发此方法：
	 * 	<ul>
	 * 		<li>内容管理中，新增文章，点击保存时</li>
	 * 		<li>内容管理中，编辑文章，点击保存时</li>
	 * 	</ul>
	 * @param news 当前要处理的 {@link News}
	 * @return 已处理过的 news，会将其进行保存进数据库。如果返回 result 失败，则info返回失败原因，并且用户端会讲这个原因展示出保存失败的提醒。
	 */
	public NewsVO newsEditSaveBefore(HttpServletRequest request, News news);
}