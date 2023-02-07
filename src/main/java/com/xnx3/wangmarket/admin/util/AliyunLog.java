package com.xnx3.wangmarket.admin.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
		insert(generateLogMap(), null, goalid, action, remark);
	}
	

	/**
	 * 增加动作日志
	 * @param goalid 操作的目标id,若没有，则为0
	 * @param action 动作
	 * @param remark 备注，说明
	 */
	public static void addActionLog(int goalid, String action){
		insert(generateLogMap(), null, goalid, action, "");
	}
	
	/**
	 * 增加动作日志
	 * @param action 动作
	 * @param remark 备注，说明
	 */
	public static void addActionLog(String action, String remark){
		insert(generateLogMap(), null, 0, action, remark);
	}
	
	
}
