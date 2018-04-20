package com.xnx3.wangmarket.admin.vo;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.News;

/**
 * 信息相关
 * @author 管雷鸣
 */
public class NewsVO extends BaseVO {
	private News news;

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	
}
