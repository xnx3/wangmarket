package com.xnx3.wangmarket.admin.bean;


/**
 * 信息相关，相当于 {@link com.xnx3.wangmarket.admin.entity.News}，只不过加了排序功能
 * @author 管雷鸣
 */
public class News implements Comparable {
	private com.xnx3.wangmarket.admin.entity.News news;
	private Integer rank;	//相当于news.id，排序使用

	public com.xnx3.wangmarket.admin.entity.News getNews() {
		return news;
	}

	public void setNews(com.xnx3.wangmarket.admin.entity.News news) {
		this.news = news;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public int compareTo(Object o) {
		News obj = (News)o;
		return this.rank.compareTo(obj.getRank());
	}

}
