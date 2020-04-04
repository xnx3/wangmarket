package com.xnx3.j2ee.util.AttachmentMode.bean;

/**
 * 子文件信息
 * @author 管雷鸣
 *
 */
public class SubFileBean {
	private String path;	//文件路径，相对路径，如 site/219/index.html
	private long size;		//文件大小，单位B
	private long lastModified;	//上次修改日期，单位是毫秒
	private boolean folder;	//是否是文件夹？如果是，则是true

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public boolean isFolder() {
		return folder;
	}

	public void setFolder(boolean folder) {
		this.folder = folder;
	}
	
}
