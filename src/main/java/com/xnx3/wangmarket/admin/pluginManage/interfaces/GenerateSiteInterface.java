package com.xnx3.wangmarket.admin.pluginManage.interfaces;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.vo.TemplatePageListVO;

/**
 * 生成整站之后的触发。点击网站管理后台-生成整站后，在生成成功后触发接口
 * @author 管雷鸣
 */
public interface GenerateSiteInterface {
	
	/**
	 * 点击生成整站按钮后，并且尚未生成整站之前，触发此方法。
	 * @param site 生成整站的网站 {@link Site}
	 * @return {@link BaseVO} 有以下几种情况：
	 * 			<ul>
	 * 				<li>返回 null 则忽略，允许继续执行</li>
	 * 				<li>BaseVO.getResult() == BaseVO.SUCCESS 则是允许继续执行</li>
	 * 				<li>BaseVO.getResult() == BaseVO.FAILURE 则是不允许继续执行了。如果返回此，那么用户点击生成整站按钮会，不会触发生成整站的方法，同时用户看到弹出错误提示。这个错误提示的文字，便是 BaseVO.getInfo() </li>
	 * 			</ul> 
	 */
	public BaseVO generateSiteBefore(HttpServletRequest request, Site site);
	
	/**
	 * 点击生成整站按钮后，已经生成完成整站，触发的接口
	 * @param site 生成整站的网站 {@link Site}
	 * @param siteColumnMap 当前网站的所有栏目。 key: {@link SiteColumn}.code，  value:{@link SiteColumn}
	 * @param newsMap 当前网站的文章数据， 以栏目codeName为key，将文章List加入进自己对应的栏目。同时，若传入父栏目代码，其栏目下有多个新闻子栏目，会调出所有子栏目的内容
	 * @param newsDataMap 当前网站的文章详情的数据 key: {@link News}.id  value: {@link NewsDataBean}
	 */
	public void generateSiteFinish(HttpServletRequest request, Site site, Map<String, SiteColumn> siteColumnMap, Map<String, List<News>> newsMap, Map<Integer, NewsDataBean> newsDataMap);
	
}