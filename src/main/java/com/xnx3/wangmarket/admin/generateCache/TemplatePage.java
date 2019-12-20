package com.xnx3.wangmarket.admin.generateCache;

import org.springframework.stereotype.Component;
import com.xnx3.j2ee.generateCache.BaseGenerate;

/**
 * 模版页面
 * @author 管雷鸣
 */
@Component
public class TemplatePage extends BaseGenerate{
	
	public TemplatePage() {
		type();
		editMode();
	}
	
	public void type(){
		createCacheObject("type");
		cacheAdd(com.xnx3.wangmarket.admin.entity.TemplatePage.TYPE_INDEX, "首页模版");
		cacheAdd(com.xnx3.wangmarket.admin.entity.TemplatePage.TYPE_NEWS_LIST, "列表页模版");
		cacheAdd(com.xnx3.wangmarket.admin.entity.TemplatePage.TYPE_NEWS_VIEW, "详情页模版");
		generateCacheFile();
	}
	
	public void editMode(){
		createCacheObject("editMode");
		cacheAdd(com.xnx3.wangmarket.admin.entity.TemplatePage.EDIT_MODE_VISUAL, "可视化编辑");
		cacheAdd(com.xnx3.wangmarket.admin.entity.TemplatePage.EDIT_MODE_CODE, "纯代码编辑");
		generateCacheFile();
	}
	
}
