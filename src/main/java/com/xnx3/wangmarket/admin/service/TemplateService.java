package com.xnx3.wangmarket.admin.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.TemplatePage;
import com.xnx3.wangmarket.admin.entity.TemplatePageData;
import com.xnx3.wangmarket.admin.entity.TemplateVar;
import com.xnx3.wangmarket.admin.entity.TemplateVarData;
import com.xnx3.wangmarket.admin.vo.GenerateSiteVO;
import com.xnx3.wangmarket.admin.vo.TemplatePageListVO;
import com.xnx3.wangmarket.admin.vo.TemplatePageVO;
import com.xnx3.wangmarket.admin.vo.TemplateVO;
import com.xnx3.wangmarket.admin.vo.TemplateVarAndDataMapVO;
import com.xnx3.wangmarket.admin.vo.TemplateVarListVO;
import com.xnx3.wangmarket.admin.vo.TemplateVarVO;

/**
 * 用户自定义模版相关
 * @author 管雷鸣
 */
public interface TemplateService {
	
	/**
	 * 获取当前登陆用户当前网站所使用的模版页列表，这个列表仅是 {@link TemplatePage}的列表，不包含 {@link TemplatePageData}
	 * @return {@link TemplatePageListVO}
	 */
	public TemplatePageListVO getTemplatePageListByCache(HttpServletRequest request);
	
	/**
	 * 从数据库中，获取当前登陆用户当前网站所使用的模版页列表，这个列表仅是 {@link TemplatePage}的列表，不包含 {@link TemplatePageData}。每次执行此方法，都会查询一次数据库
	 * @param site {@link Site} 要获取的是那个网站的模版页面
	 * @return {@link TemplatePageListVO}
	 */
	public TemplatePageListVO getTemplatePageListByDatabase(Site site);
	
	/**
	 * 获取当前登陆用户当前网站所使用的模版页列表，这个列表是 {@link TemplatePage} + {@link TemplatePageData} 的list列表
	 * @return {@link TemplatePageListVO}
	 */
	public TemplatePageListVO getTemplatePageAndDateListByCache(HttpServletRequest request);
	
	/**
	 * 获取指定模版页面的页面内容，根据模版页面的模版页id
	 * @param templatePageId {@link TemplatePage}.id，要获取其内容的模版页面id
	 * @param request
	 * @return 模版页内容。若是没有发现模版内容，返回null
	 */
	public String getTemplatePageTextByCache(int templatePageId, HttpServletRequest request);
	
	/**
	 * 获取指定模版页面的页面内容，根据模版页面的名字
	 * 
	 * 待做二级缓存：
	 * 	一级缓存直接用Map存储key:name  value:text
	 * 	二级缓存为当前的List存储
	 * 使用此方法，先找一级缓存
	 * 
	 * @param templatePageName {@link TemplatePage}.name，要获取其内容的模版页面的名字
	 * @param request
	 * @return 模版页内容。若是没有发现模版内容，返回null
	 */
	public String getTemplatePageTextByCache(String templatePageName, HttpServletRequest request);
	
	/**
	 * 获取当前网站的首页模版，从缓存中
	 * @param request
	 * @return {@link TemplatePageVO}
	 */
	public TemplatePageVO getTemplatePageIndexByCache(HttpServletRequest request);
	
	/**
	 * 更新当前用户存储在Session中缓存的当前网站使用的模版页。更改、新增都支持
	 * @param templatePage {@link TemplatePage} 当前要更新的模版页
	 * @param templatePageData {@link TemplatePageData}当前要更新的模版页页面内容。若为null，则此次不更新 {@link TemplatePageData}
	 * @return {@link BaseVO} 目前都是true，可以不用判断这个
	 */
	public BaseVO updateTemplatePageForCache(TemplatePage templatePage, TemplatePageData templatePageData, HttpServletRequest request);
	
	/**
	 * 保存模版页面的text内容，将用户传过来的内容进行整理后，存到数据库，同时更新缓存
	 * @param fileName 当前模版页的页面名字
	 * @param html 用户保存过来的html模版内容
	 * @param request 用于更新Session中缓存用
	 * @return {@link TemplatePageVO} 若成功，返回的vo包含当前模版页的Page跟PageData
	 */
	public TemplatePageVO saveTemplatePageText(String fileName, String html, HttpServletRequest request);
	
	/**
	 * 更新当前缓存的模版变量。若当前缓存中没有，则先执行从数据库读出到缓存
	 */
	public void updateTemplateVarForCache(TemplateVar templateVar, TemplateVarData templateVarData);
	
	/**
	 * 从数据库中，获取模版变量信息，包含 {@link TemplateVar} 跟 {@link TemplateVarData} 组合好的信息
	 * @param site 获取的是哪个网站的模版变量信息
	 */
	public TemplateVarAndDataMapVO getTemplateVarAndDataByDatabase(Site site);
	
	/**
	 * 更新模版变量，从数据库更新到Session缓存
	 */
	public void loadDatabaseTemplateVarToCache();
	
	/**
	 * 更换模版，改变模版。同时也会将栏目复制过去.
	 * <br/>若此模版当前站点未使用过，会将模版页面跟模版变量复制过来。若此模版该网站之前使用过了，只需吧site.templateName改动一下名字即可
	 * @param site 要操作的网站
	 * @param templateName 要变为的模版名字
	 * @param copySiteColumn 是否再改变为新模版的同时，将其模版所拥有的栏目也一并复制过来。只对之前没有使用过的模版会复制栏目，若以前使用过此模版了，那么不会再复制栏目
	 * 				<ul>
	 * 					<li>true：复制</li>
	 * 					<li>false：不复制</li>
	 * 				</ul>
	 * @return {@link BaseVO}
	 * @deprecated
	 * @see #importTemplate(String, boolean)
	 */
	public BaseVO changeTemplate(Site mysite, String templateName, boolean copySiteColumn);
	
	/**
	 * 给网站增加一个新的首页模版页，作为默认首页
	 * <br/>前提是确保此网站没有首页模版！
	 * @param site 要增加的首页模版页的站点
	 * @return {@link BaseVO}
	 */
	public BaseVO addNewDefaultTemplatePageForIndex(Site site);
	
	/**
	 * 根据模版变量的名字，从缓存中取该模版变量（前提：此模版变量是自己的）
	 * @param templateVarName 模版变量的名字
	 * @return {@link BaseVO}
	 * 				<ul>
	 * 					<li>若result成功，则info为模版变量的内容，原始内容，数据库中保存的内容，未被替换过</li>
	 * 					<li>若result失败，则info为失败原因</li>
	 * 				</ul>
	 */
	public TemplateVarVO getTemplateVarByCache(String templateVarName);
	
	/**
	 * 将当前网站的模版，导出为模版文件。从缓存中取数据导出。若缓存中没有再读数据库
	 * <br/>导出内容包括：模版页面、模版变量、栏目
	 * @param request 取session，从缓存中拿数据
	 * @return true:要导出的数据在info中
	 */
	public BaseVO exportTemplate(HttpServletRequest request);
	
	/**
	 * 模版导入、模版恢复
	 * @param templateText 导入的模版文件的内容，字符串
	 * @param copySiteColumn 是否再改变为新模版的同时，将其模版所拥有的栏目也一并复制过来。只对之前没有使用过的模版会复制栏目，若以前使用过此模版了，那么不会再复制栏目
	 * 				<ul>
	 * 					<li>true：复制</li>
	 * 					<li>false：不复制</li>
	 * 				</ul>
	 * @return
	 */
	public BaseVO importTemplate(String templateText, boolean copySiteColumn, HttpServletRequest request);
	
	/**
	 * 获取当前网站所用模版的模版变量列表。读数据库
	 * @return 模版变量列表
	 */
	public List<TemplateVar> getTemplateVarList();
	
	/**
	 * 删除当前用户存储在Session中缓存的当前网站使用的模版页，仅仅只是删除缓存中得
	 * @param templatePageId {@link TemplatePage}.id 要删除得模版页得id
	 * @return {@link BaseVO} 目前都是true，可以不用判断这个
	 */
	public BaseVO deleteTemplatePageForCache(int templatePageId, HttpServletRequest request);
	
	/**
	 * 通过模版页的名字，获取模版页的信息，包含 {@link TemplatePage} 、 {@link TemplatePageData} 的信息
	 * @param templatePageName 模版页的名字 {@link TemplatePage}.name
	 * @return
	 */
	public TemplatePageVO getTemplatePageByNameForCache(HttpServletRequest request, String templatePageName);
	
	/**
	 * 获取当前网站所用模版的模版变量列表,模板变量是缓存的原始的，数据库中的模板变量。
	 * <br/>从缓存中获取
	 * @return 模版变量列表
	 */
	public TemplateVarListVO getTemplateVarListByCache();
	
	/**
	 * 获取当前登陆用户当前网站所使用的模版变量列表，这个列表是 {@link TemplateVar} + {@link TemplateVarData} 的list列表
	 * @return {@link TemplateVarListVO}
	 */
	public TemplateVarListVO getTemplateVarAndDateListByCache();
	
	/**
	 * 删除当前用户存储在Session中缓存的当前网站使用的模版变量，仅仅只是删除缓存中得
	 * @param templateVarId {@link TemplateVar}.id 要删除得模版变量得id
	 * @return {@link BaseVO} 目前都是true，可以不用判断这个
	 */
	public BaseVO deleteTemplateVarForCache(int templateVarId);
	
	/**
	 * 通过模版名字，获取模版信息。 这个模版信息是从数据库中获取的
	 * @param name 要获取的模版的名字
	 * @return 如果 {@link TemplateVO#getResult()} 为 success，则可以获取到 {@link TemplateVO#getTemplate()} 。当然，获取到的也就只有 getTemplate() 了
	 */
//	public TemplateVO getTemplateForDatabase(String name);
	
	/**
	 * 获取 TemplateVarVO 的map数据，从缓存中。若缓存中不存在，再从数据库中缓存，并获取。
	 */
	public Map<String, TemplateVarVO> getTemplateVarVoMapByCache();
	
	/**
	 * 生成整站html
	 * @param site 如果不传入，则是使用Session缓存的,， 如果传入，则是某个网站操作其他的网站生成，都是从数据库读取的
	 */
	public GenerateSiteVO generateSiteHTML(HttpServletRequest request, Site site);
	
}
