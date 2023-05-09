package com.xnx3.wangmarket.admin.util;

import java.util.List;

import com.xnx3.wangmarket.admin.entity.SiteColumn;

/**
 * CMS网站站点相关的工具类
 * @author 管雷鸣
 *
 */
public class SiteUtil {
	
	/**
	 * 判断网站中某个栏目是否是父栏目
	 * @param columnList 当前网站的所有栏目列表
	 * @param column 当前要判断的这个栏目
	 * @return true 是父栏目
	 */
	public static boolean isParentColumn(List<SiteColumn> columnList, SiteColumn column) {
		for (int i = 0; i < columnList.size(); i++) {	//遍历栏目，将对应的文章加入其所属栏目
			SiteColumn sub = columnList.get(i);
			if(sub.getParentCodeName() == null || sub.getParentCodeName().length() == 0) {
				continue;
			}
			if(sub.getParentCodeName().equals(column.getCodeName())) {
				return true;
			}
		}
		
		return false;
	}
	
	
}
