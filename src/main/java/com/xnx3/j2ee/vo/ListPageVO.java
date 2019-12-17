package com.xnx3.j2ee.vo;

import java.util.List;
import java.util.Map;

import com.xnx3.j2ee.util.Page;

/**
 * 通用列表分页，返回 List<Map>
 * @author 管雷鸣
 *
 */
public class ListPageVO extends BaseVO{
	
	private List<Map<String,Object>> list;
	private Page page;
	
	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	@Override
	public String toString() {
		return "ListPageVO [list=" + list + ", page=" + page + ", getResult()=" + getResult() + ", getInfo()="
				+ getInfo() + "]";
	}
	
}
