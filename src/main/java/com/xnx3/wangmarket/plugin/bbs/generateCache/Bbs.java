package com.xnx3.wangmarket.plugin.bbs.generateCache;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.xnx3.j2ee.generateCache.BaseGenerate;
import com.xnx3.wangmarket.plugin.bbs.entity.PostClass;

/**
 *  论坛相关缓存
 * @author 管雷鸣
 *
 */
@Component
public class Bbs extends BaseGenerate {
	public Bbs() {
	}
	
	/**
	 * 生成论坛板块数据缓存，数组存在
	 * @param list 传入如：postClassService.findAll()
	 */
	public void postClass(List<PostClass> list){
		createCacheObject("classid");
		for (int i = 0; i < list.size(); i++) {
			PostClass pc = list.get(i);
			cacheAdd(pc.getId(), pc.getName());
		}
		generateCacheFile();
	}
	
	/**
	 * 生成论坛板块数据缓存，数组存在，此处是在项目初始化时， {@link InitServlet}中自动创建的
	 */
	public void postClassByListMap(List<Map<String,Object>> list){
		createCacheObject("classid");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String id = map.get("id").toString();
			String name = map.get("name").toString();
			cacheAdd(id, name);
		}
		generateCacheFile();
	}
	
}
