package com.xnx3.wangmarket.domain.bean;

/**
 * 缓存的对象，从obs中读取的文本文件，都会先进行缓存，避免被攻击后频繁读取obs
 * @author 管雷鸣 
 */
public class TextBean implements java.io.Serializable{
	private String text;
	
	/**
	 * 是否存在于内存中
	 * 如果存在于内存中，那么text的就是文本内容了
	 * 如果为false，不存在于内存中，那么就是文本内容太大，存在了磁盘中进行临时存储，text存储的是存储的文件所在的磁盘的路径
	 */
	private boolean isExistMenory;	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isExistMenory() {
		return isExistMenory;
	}

	public void setExistMenory(boolean isExistMenory) {
		this.isExistMenory = isExistMenory;
	}
	
	
}
