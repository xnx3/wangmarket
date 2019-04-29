package com.xnx3.wangmarket.admin.pluginManage.newSave;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;

/**
 * 文章保存时，针对news、news_date 的预处理
 * @author 管雷鸣
 *
 */
public interface NewsSaveInterface {
	
	/**
	 * 拦截 News 进行预处理。这里是在保存入数据库之前拦截下来，进行处理，处理完后将其存入数据库
	 * @param news 要处理的 {@link News}
	 * @return 已处理过的 news
	 */
	public News interceptNews(HttpServletRequest request, HttpServletResponse response, News news);
	
	/**
	 * 拦截 NewsData 进行预处理。这里是在保存入数据库之前拦截下来，进行处理，处理完后将其存入数据库
	 * @param newsData 要处理的 {@link NewsData}
	 * @return 已处理过的 newsData
	 */
	public NewsData interceptNewsData(HttpServletRequest request, HttpServletResponse response,NewsData newsData);
}