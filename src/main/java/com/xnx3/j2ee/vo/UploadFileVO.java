package com.xnx3.j2ee.vo;

/**
 * aliyun OSS文件上传
 * @author 管雷鸣
 */
public class UploadFileVO extends BaseVO {
	/**
	 * 无文件
	 */
	public final static int NOTFILE=2;
	
	private String fileName;	//上传成功后的文件名，如 "xnx3.jar"
	private String path;		//上传成功后的路径，如 "/jar/file/xnx3.jar"
	private String url;		//文件上传成功后，外网访问的url
	private long size;		//上传的文件的大小，单位 KB
	
	public UploadFileVO() {
	}
	
	/**
	 * 这是OSS上传成功后的返回值
	 * @param fileName 上传成功后的文件名，如 "xnx3.jar"
	 * @param path 上传成功后的路径，如 "/jar/file/xnx3.jar"
	 * @param url 文件上传成功后，外网访问的url
	 */
	public UploadFileVO(String fileName,String path,String url) {
		this.fileName = fileName;
		this.path = path;
	}
	
	/**
	 * 上传成功后的文件名，如 "xnx3.jar"
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 上传成功后的文件名，如 "xnx3.jar"
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * 上传成功后的路径，如 "/jar/file/xnx3.jar"
	 * @return
	 */
	public String getPath() {
		return path;
	}
	/**
	 * 上传成功后的路径，如 "/jar/file/xnx3.jar"
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * 文件上传成功后，外网访问的url
	 * @return
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * 文件上传成功后，外网访问的url
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "UploadFileVO [fileName=" + fileName + ", path=" + path
				+ ", url=" + url + ", getResult()=" + getResult()
				+ ", getInfo()=" + getInfo() + "]";
	}

	
}
