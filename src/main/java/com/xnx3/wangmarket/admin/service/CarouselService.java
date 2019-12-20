package com.xnx3.wangmarket.admin.service;

import java.util.List;
import com.xnx3.wangmarket.admin.entity.Carousel;

/**
 * 轮播图（banner）相关
 * @author 管雷鸣
 */
public interface CarouselService {
	
	/**
	 * 根据siteid查询属于此网站的轮播图，所有type 排序 rank ASC
	 * @param siteid
	 */
	public List<Carousel> findBySiteid(int siteid);

	/**
	 * 根据一条最新的轮播图，排序 rank ASC 。若不存在，返回null
	 * @param siteid
	 * @param type 类型，如{@link Carousel#TYPE_DEFAULT_PAGEBANNER}
	 */
	public Carousel findAloneBySiteid(int siteid, short type);
	
}
