package com.xnx3.wangmarket.domain.mq;

/**
 * Java 模拟的 MQ ，将此作为 value 存入 Map
 * @author 管雷鸣
 *
 */
public class JavaQueueBean {
	private int addtime;	//加入的时间，生成的时间
	private String content;	//消息主体，消息内容
	
	public int getAddtime() {
		return addtime;
	}

	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "JavaQueueBean [addtime=" + addtime + ", content=" + content + "]";
	}
	
}
