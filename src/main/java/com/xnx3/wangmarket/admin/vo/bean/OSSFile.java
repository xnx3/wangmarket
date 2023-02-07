package com.xnx3.wangmarket.admin.vo.bean;

import com.xnx3.wangmarket.admin.vo.SiteFileListVO;

/**
 * OSS文件相关，服务于 {@link SiteFileListVO}
 * {@link OSSObjectSummary}的简化
 * <br>已废弃，文件操作相关已迁移至 https://github.com/xnx3/FileUpload
 * @author 管雷鸣
 * @deprecated
 */
public class OSSFile {
	private String key;
	private long size;
	
	public OSSFile() {
	}
	
	public OSSFile(String key, long size) {
		this.key = key;
		this.size = size;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	
}
