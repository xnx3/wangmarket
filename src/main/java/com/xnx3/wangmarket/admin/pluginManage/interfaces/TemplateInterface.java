package com.xnx3.wangmarket.admin.pluginManage.interfaces;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.cache.TemplateCMS;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.SiteColumn;

/**
 * 模板、模板标签替换的扩展
 * <p>这个接口主要是给第三方系统使用网市场模板系统时，自定义一些模板标签所预留使用的</p>
 * @author 管雷鸣
 */
public interface TemplateInterface {
	
	/**
	 * 对公共标签进行一些附加。相当于是对这些的扩展 http://tag.wscso.com/2936.html
	 * @param text 要替换的源内容
	 * @param template TemplateCMS
	 * @return 如果result成功，则info为text替换完成的内容
	 */
	public BaseVO publicTag(String text, TemplateCMS template);
	
	/**
	 * 栏目标签的附加，相当于是对这些的扩展 http://tag.wscso.com/2937.html
	 * @param text 要替换的源内容
	 * @param siteColumn 某个栏目的信息
	 * @param template TemplateCMS
	 * @return 如果result成功，则info为text替换完成的内容
	 */
	public BaseVO siteColumnTag(String text, SiteColumn siteColumn, TemplateCMS template);
	
	/**
	 * 
	 * @param newsText
	 * @param news
	 * @param siteColumn
	 * @param newsDataBean
	 * @param template
	 * @return
	 */
	public BaseVO newsTag(String text,News news, SiteColumn siteColumn, NewsDataBean newsDataBean, TemplateCMS template);
	
	/**
	 * 写出string文本文件之前，也就是生成html页面之前会先执行的。
	 * <p>这个接口主要是给第三方系统使用网市场模板系统时，自定义一些模板标签所预留使用的</p>
	 * @param text 写出的文本文件的内容，文本。也就是写出html的内容
	 * @param path 要生成html文件的路径。 传入如 index.html 、 sitemap.xml
	 * @return 如果成功，则info中是再接口中已经处理过的，要生成html页面的最新text内容。所以如果这个接口要是不用，则要返回 BaseVO.success(text); 
	 * 		当然，如果返回result为false、以及null，那证明接口实现出问题了，则都会使用传入的text作为最新的html内容
	 */
	public BaseVO publishHtmlFileBefore(String text, String path, TemplateCMS template);
	
}