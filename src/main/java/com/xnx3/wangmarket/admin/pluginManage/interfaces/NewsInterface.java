package com.xnx3.wangmarket.admin.pluginManage.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;

/**
 * 文章保存时，针对news、news_date 的预处理
 * @author 管雷鸣
 */
public interface NewsInterface {
	
	/**
	 * 拦截 News 进行预处理。这里是在保存入数据库之前拦截下来，进行处理，处理完后将其存入数据库
	 * 在以下情况，触发此方法：
	 * 	<ul>
	 * 		<li>内容管理中，新增文章，点击保存时</li>
	 * 		<li>内容管理中，编辑文章，点击保存时</li>
	 * 		<li>内容管理中，修改文章的发布时间</li>
	 * 	</ul>
	 * @param news 要处理的 {@link News}
	 * @return 已处理过的 news，会将其进行保存进数据库
	 */
	public News newsSaveBefore(HttpServletRequest request, News news);
	

	/**
	 * 这里是已经保存入数据库之后，进行处理
	 * 在以下情况，触发此方法：
	 * 	<ul>
	 * 		<li>内容管理中，新增文章，保存成功后</li>
	 * 		<li>内容管理中，编辑文章，保存成功后</li>
	 * 		<li>内容管理中，修改文章的发布时间，保存成功后</li>
	 * 	</ul>
	 * @param news 当前操作的{@link News}
	 */
	public void newsSaveFinish(HttpServletRequest request, News news);
	
	/**
	 * 拦截 NewsData 进行预处理。这里是在保存入数据库之前拦截下来，进行处理，处理完后将其存入数据库
	 * 在以下情况，触发此方法：
	 * 	<ul>
	 * 		<li>内容管理中，新增文章，点击保存时</li>
	 * 		<li>内容管理中，编辑文章，点击保存时</li>
	 * 	</ul>
	 * @param newsData 要处理的 {@link NewsData}
	 * @return 已处理过的 newsData，会将其进行保存进数据库
	 */
	public NewsData newsDataSaveBefore(HttpServletRequest request, NewsData newsData);
	
	/**
	 * 拦截 NewsData 进行预处理。这里是在保存入数据库之前拦截下来，进行处理，处理完后将其存入数据库
	 * 在以下情况，触发此方法：
	 * 	<ul>
	 * 		<li>内容管理中，新增文章，保存成功后</li>
	 * 		<li>内容管理中，编辑文章，保存成功后</li>
	 * 	</ul>
	 * @param newsData 当前保存的 {@link NewsData}
	 */
	public void newsDataSaveFinish(HttpServletRequest request,NewsData newsData);
	
	
	/**
	 * {@link News} 文章删除时会触发此方法。当文章点击删除后，在文章删除成功之后，会调用此方法
	 * 在以下情况，触发此方法：
	 * 	<ul>
	 * 		<li>内容管理中，文章列表，点击某篇文章进行删除成功后</li>
	 * 	</ul>
	 * @param news 被删除的文章
	 */
	public void newsDeleteFinish(HttpServletRequest request, News news);
}