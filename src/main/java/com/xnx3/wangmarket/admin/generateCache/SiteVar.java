package com.xnx3.wangmarket.admin.generateCache;

import org.springframework.stereotype.Component;
import com.xnx3.j2ee.generateCache.BaseGenerate;

/**
 * 模板变量
 * @author 管雷鸣
 */
@Component
public class SiteVar extends BaseGenerate {
	public SiteVar() {
		type();
	}
	
	public void type(){
		createCacheObject("type");
		cacheAdd(com.xnx3.wangmarket.admin.entity.SiteVar.TYPE_TEXT, "文本输入");
		cacheAdd(com.xnx3.wangmarket.admin.entity.SiteVar.TYPE_NUMBER, "整数输入");
		cacheAdd(com.xnx3.wangmarket.admin.entity.SiteVar.TYPE_IMAGE, "单图片上传");
		cacheAdd(com.xnx3.wangmarket.admin.entity.SiteVar.TYPE_IMAGE_GROUP, "多图片上传");
		cacheAdd(com.xnx3.wangmarket.admin.entity.SiteVar.TYPE_SELECT, "下拉选择");
		generateCacheFile();
	}

}
