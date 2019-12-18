package com.xnx3.wangmarket.admin.cache.pc;

import com.xnx3.wangmarket.admin.cache.GenerateHTML;
import com.xnx3.wangmarket.admin.entity.Site;

/**
 * PC首页的一些处理
 * @author 管雷鸣
 * @deprecated 已在v4.0版本就已废弃PC模式
 */
public class Index {
	
	/**
	 * 更新首页的 keywords
	 * @param site {@link Site}
	 * @param keywords 要更新为的 keywords，如   寻仙,寻仙网
	 */
	public static void updateKeywords(Site site,String keywords){
		GenerateHTML gh = new GenerateHTML(site);
		String html = gh.getGeneratePcIndexHtml();
		html = html.replaceAll("<meta name=\"keywords\" content=\".*\" />", "<meta name=\"keywords\" content=\""+keywords+"\" />");
		gh.generateIndexHtml(html);
	}
	
	/**
	 * 更新首页的 description
	 * @param site {@link Site}
	 * @param description 要更新为的 description
	 */
	public static void updateDescription(Site site,String description){
		GenerateHTML gh = new GenerateHTML(site);
		String html = gh.getGeneratePcIndexHtml();
		html = html.replaceAll("<meta name=\"description\" content=\".*\" />", "<meta name=\"description\" content=\""+description+"\" />");
		gh.generateIndexHtml(html);
	}
	
	/**
	 * 更新首页的 title 标题
	 * @param site {@link Site}
	 * @param name 要更改的站点的名字
	 */
	public static void updateSiteName(Site site,String name){
		GenerateHTML gh = new GenerateHTML(site);
		String html = gh.getGeneratePcIndexHtml();
		html = html.replaceAll("<title>.*</title>", "<title>"+name+"</title>");
		gh.generateIndexHtml(html);
	}
	
}
