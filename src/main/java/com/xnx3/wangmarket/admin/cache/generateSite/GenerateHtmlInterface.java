package com.xnx3.wangmarket.admin.cache.generateSite;

import com.xnx3.j2ee.vo.BaseVO;

/**
 * 生成网站，生成整站,写出html文件
 * @author 管雷鸣
 *
 */
public interface GenerateHtmlInterface {
	
	/**
	 * 写出string文本文件，也就是生成html页面
	 * @param text 写出的文本文件的内容，文本。也就是写出html的内容
	 * @param path 生成html文件的路径。 传入如 index.html 
	 */
	public BaseVO putStringFile(String text, String path);
	
}
