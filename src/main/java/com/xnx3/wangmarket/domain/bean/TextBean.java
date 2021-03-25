package com.xnx3.wangmarket.domain.bean;

/**
 * 缓存的对象，从obs中读取的文本文件，都会先进行缓存，避免被攻击后频繁读取obs
 * @author 管雷鸣 
 */
public class TextBean implements java.io.Serializable{
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
