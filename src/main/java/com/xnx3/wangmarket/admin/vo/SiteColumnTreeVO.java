package com.xnx3.wangmarket.admin.vo;

import java.util.List;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.SiteColumn;

/**
 * 站点栏目列表树，有顶级栏目、二级栏目区分，栏目缓存于Session使用。第一序列是一级栏目，而后getList是其下级栏目
 * @author 管雷鸣
 */
public class SiteColumnTreeVO extends BaseVO{
	private int level;	//当前栏目的级别，1：顶级、一级栏目；  2：二级栏目
	private SiteColumn siteColumn;	//栏目信息
	private List<SiteColumnTreeVO> list;	//其下级栏目列表
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public SiteColumn getSiteColumn() {
		return siteColumn;
	}
	public void setSiteColumn(SiteColumn siteColumn) {
		this.siteColumn = siteColumn;
	}
	public List<SiteColumnTreeVO> getList() {
		return list;
	}
	public void setList(List<SiteColumnTreeVO> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "SiteColumnTreeVO [level=" + level + ", siteColumn="
				+ siteColumn + ", list=" + list + "]";
	}

}
