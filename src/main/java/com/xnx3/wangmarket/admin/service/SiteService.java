package com.xnx3.wangmarket.admin.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.entity.SiteData;
import com.xnx3.wangmarket.admin.vo.SiteRemainHintVO;
import com.xnx3.wangmarket.admin.vo.SiteVO;
import com.xnx3.wangmarket.admin.vo.bean.TemplateCommon;
import com.xnx3.wangmarket.domain.bean.MQBean;
import com.xnx3.wangmarket.agencyadmin.entity.Agency;

/**
 * 站点相关
 * @author 管雷鸣
 */
public interface SiteService {
	
	/**
	 * 创建／修改站点。若是 site.id 大于0，则是修改站点
	 * @param s {@link Site} 要增加或者修改的site对象
	 * @param siteCreateUserId 站点创建人的用户id
	 * @param request
	 * @return {@link SiteVO}
	 */
	public SiteVO saveSite(Site s, int siteCreateUserId, HttpServletRequest request);
	
	
	/**
	 * 生成站点WAP首页的模版
	 * @param site
	 */
	public void generateSiteIndex(Site site);
	
	/**
	 * 生成站点PC首页的模版
	 * @param site {@link Site}站点
	 * @param aboutUsColumn 关于我们的栏目信息
	 * @param newsColumn 新闻的栏目信息
	 * @param imagesColumn 产品的栏目信息
	 */
	public void generateSitePcIndex(Site site,SiteColumn aboutUsColumn, SiteColumn newsColumn, SiteColumn imagesColumn,SiteData siteData);
	
	
	/**
	 * 刷新站点，重新创建HTML文件
	 */
	public BaseVO refreshSiteGenerateHtml(HttpServletRequest request);
	
	/**
	 * 获取编辑模式下，jsp页面上显示的通用的头部跟底部
	 * @param site {@link Site}，可传null，默认使用1号模版
	 * @param title 标题（非html的title，而是网页的头部显示的title横条）
	 * @param model {@link Model}
	 * @return {@link TemplateCommon}
	 */
	public TemplateCommon getTemplateCommonHtml(Site site, String title, Model model);
	
	/**
	 * 更换Wap模版，刷新Html页面
	 * @param site 更换模版后的site
	 */
//	public SiteVO updateWapTemplateRefreshHtml(Site site, HttpServletRequest request);
	
	/**
	 * 判断访问得是否是PC端，根据Site.client。只要client不是pc，那么都是wap访问
	 * @param site
	 * @return true:是PC访问
	 */
	public boolean isPcClient(Site site);
	
	/**
	 * 当网站的二级域名、绑定域名、以及网站状态发生变化时，更新此处。
	 */
//	public void updateDomainServers(int siteid);
	public void updateDomainServers(MQBean mqBean);
	
	/**
	 * 修改、新增、隐藏某个栏目后，设置 Site.columnId，这里返回columnId的值
	 * 若栏目为新闻或者图文列表，且Site表里没有记录该栏目，则将该栏目的id加入Site表的栏目id中。反之，如果隐藏不显示栏目，也会将其从Site.columnId去除
	 * @param sc 有改动的{@link SiteColumn}
	 * @param site {@link Site}
	 * @return 若是为null，则不予理会，若不是null，则需要保存更新Site
	 */
	public String getSiteColumnId(SiteColumn sc, Site site);
	
	/**
	 * 传入一个站点，重新刷新生成这个站点的sitemap.xml文件
	 * @param site 站点对象信息
	 */
	public void refreshSiteMap(Site site);
	
	/**
	 * 传入一个站点，重新刷新生成这个站点首页 html 文件（初始化重新生成）
	 * @param site
	 * @return
	 */
	public BaseVO refreshIndex(Site site);
	
	/**
	 * 整站刷新，自定义模版
	 * @param request
	 * @return
	 */
//	public BaseVO refreshForTemplate(HttpServletRequest request);
	
	/**
	 * 根据 site.id 返回 {@link Site}，会读取数据库
	 * <br/>判断是否是当前这个用户的,当前用户是否可操作
	 * @param id site.id 
	 * @return {@link SiteVO}
	 */
//	public SiteVO findByIdForCurrentUser(int id);
	
	/**
	 * 获取网站的过期时间以及提示说明（联系的直属上级代理）提示让其找上级代理续费
	 * @param site 检测的是否过期的网站
	 * @param agency 检测的网站的直属上级代理。因最开始的时候可能会是系统开通，没有上级代理，有可能为空，以后的不会，都是不为空的
	 * @return 返回的result参数：
	 * 			<ul>
	 * 				<li>当result为成功( SiteRemainHintVO.SUCCESS )时，网站正常不需要过期提醒。</li>
	 * 				<li>当result为失败( ( SiteRemainHintVO.FAILURE ) )时，就是快要过期了，使用过时间还不到两个月了，需要提示</li>
	 * 				<li>当result为 3 时，网站已经到期</li>
	 * 			</ul>
	 */
	public SiteRemainHintVO getSiteRemainHint(Site site, Agency agency);
	
	/**
	 * 获取网站的过期时间以及提示说明（联系的直属上级代理）提示让其找上级代理续费
	 * <br/>这里会直接返回JavaScript脚本，直接输出在JSP页面上。附带script标签，需要提前导入layer.js
	 * @param site 检测的是否过期的网站
	 * @param agency 检测的网站的直属上级代理。因最开始的时候可能会是系统开通，没有上级代理，有可能为空，以后的不会，都是不为空的
	 * @return 若是不需要提示，这里返回""空的字符串。若是需要提示，这里就会返回带有script标签的弹出框提示
	 */
	public String getSiteRemainHintForJavaScript(Site site, Agency agency);
}
