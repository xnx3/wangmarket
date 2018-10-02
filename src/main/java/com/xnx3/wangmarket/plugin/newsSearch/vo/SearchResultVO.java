package com.xnx3.wangmarket.plugin.newsSearch.vo;

import java.util.List;
import java.util.Map;

import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 搜索的结果返回
 * @author 管雷鸣
 *
 */
public class SearchResultVO extends BaseVO{
	private List<Map<String,Object>> list;
	private Page page;
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "SearchResultVO [list=" + list + ", page=" + page + "]";
	}
	
}
