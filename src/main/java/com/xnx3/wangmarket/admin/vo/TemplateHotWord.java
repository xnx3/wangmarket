package com.xnx3.wangmarket.admin.vo;

import com.xnx3.j2ee.vo.BaseVO;

/**
 * 模版的热词，外部模版列表出现频率高的词，用自动分词取得的结果
 * @author 管雷鸣
 */
public class TemplateHotWord extends BaseVO{
	private String word;	//词
	private Integer num;	//出现的次数
	
	public TemplateHotWord(String word, int num) {
		this.word = word;
		this.num = num;
	}
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "TemplateHotWord [word=" + word + ", num=" + num + "]";
	}

}
