package com.xnx3.wangmarket.admin.util;

import java.util.Date;
import com.aliyun.openservices.log.common.LogItem;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.entity.Site;

/**
 * 用户操作日志统计
 * @author 管雷鸣
 * @deprecated 请使用 {@link com.xnx3.wangmarket.admin.util.ActionLogCache}
 * 
 */
public class AliyunLog extends ActionLogUtil {
	
	/**
	 * 增加动作日志
	 * @param goalid 操作的目标id,若没有，则为0
	 * @param action 动作
	 * @param remark 备注，说明
	 */
	public static void addActionLog(int goalid, String action, String remark){
		if(aliyunLogUtil == null){
			//不使用日志服务，终止即可
			return;
		}
		Site site = SessionUtil.getSite();
		
		String siteName = "";
		String siteDomain = "";
		int siteid = 0;
		if(site != null){
			siteName = site.getName();
			siteDomain = Func.getDomain(site);
			siteid = site.getId();
		}
		
		LogItem logItem = new LogItem((int) (new Date().getTime() / 1000));
		logItem.PushBack("siteName", siteName);
		logItem.PushBack("siteDomain", siteDomain);
		logItem.PushBack("siteid", siteid+"");
		insert(logItem, null, goalid, action, remark);
	}
	

	/**
	 * 增加动作日志
	 * @param goalid 操作的目标id,若没有，则为0
	 * @param action 动作
	 * @param remark 备注，说明
	 */
	public static void addActionLog(int goalid, String action){
		if(aliyunLogUtil == null){
			//不使用日志服务，终止即可
			return;
		}
		Site site = SessionUtil.getSite();
		
		String siteName = "";
		String siteDomain = "";
		int siteid = 0;
		if(site != null){
			siteName = site.getName();
			siteDomain = Func.getDomain(site);
			siteid = site.getId();
		}
		
		LogItem logItem = new LogItem((int) (new Date().getTime() / 1000));
		logItem.PushBack("siteName", siteName);
		logItem.PushBack("siteDomain", siteDomain);
		logItem.PushBack("siteid", siteid+"");
		insert(logItem, null, goalid, action, "");
	}
	
	/**
	 * 增加动作日志
	 * @param action 动作
	 * @param remark 备注，说明
	 */
	public static void addActionLog(String action, String remark){
		if(aliyunLogUtil == null){
			//不使用日志服务，终止即可
			return;
		}
		Site site = SessionUtil.getSite();
		
		String siteName = "";
		String siteDomain = "";
		int siteid = 0;
		if(site != null){
			siteName = site.getName();
			siteDomain = Func.getDomain(site);
			siteid = site.getId();
		}
		
		LogItem logItem = new LogItem((int) (new Date().getTime() / 1000));
		logItem.PushBack("siteName", siteName);
		logItem.PushBack("siteDomain", siteDomain);
		logItem.PushBack("siteid", siteid+"");
		insert(logItem, null, 0, action, remark);
	}
	
	
	
}
