package com.xnx3.wangmarket.admin.util;

import java.util.List;

import com.xnx3.wangmarket.admin.entity.Site;
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
	
	/**
	 * 判断这个网站是否允许上传
	 * @param site 要判断的网站
	 * @return true 允许上传
	 */
	public static boolean isUploadFile(Site site) {
		//判断空间是否超出，是否能上传。 换算为 kb 进行计算
		if(site.getAttachmentSizeHave() * 1024 <= site.getAttachmentSize()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 传入 siteColumn.listRank 返回 sql 的order语句，返回如 addtime DESC
	 * @param listRank siteColumn.listRank
	 * @return 返回 sql 的order语句，返回如 addtime DESC
	 */
	public static String columnListRankToSql(Short listRank) {
		if(listRank == null) {
			return "addtime DESC";
		}
		
		if(listRank - SiteColumn.LIST_RANK_ADDTIME_DESC == 0) {
			return "addtime DESC";
		}else if(listRank - SiteColumn.LIST_RANK_ADDTIME_ASC == 0) {
			return "addtime ASC";
		}else if(listRank - SiteColumn.LIST_RANK_ID_DESC == 0) {
			return "id DESC";
		}else if(listRank - SiteColumn.LIST_RANK_ID_ASC == 0) {
			return "id ASC";
		}
		
		return "addtime DESC";
	}
}
