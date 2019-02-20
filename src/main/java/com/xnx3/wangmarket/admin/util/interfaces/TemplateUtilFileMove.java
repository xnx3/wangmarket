package com.xnx3.wangmarket.admin.util.interfaces;

import java.io.InputStream;

import com.xnx3.wangmarket.admin.util.TemplateUtil;

public interface TemplateUtilFileMove {
	
	/**
	 * 上传文件。上传后的文件名固定。
	 * 本接口用于 {@link TemplateUtil#filterTemplateFile(java.io.File)}
	 * @param path 上传到哪里，包含上传后的文件名，如"image/head/123.jpg"
	 * @param inputStream 文件
	 */
	public void move(String path,InputStream inputStream);
}
