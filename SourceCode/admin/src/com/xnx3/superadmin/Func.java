package com.xnx3.superadmin;

import com.xnx3.admin.bean.UserBean;
import com.xnx3.superadmin.entity.Agency;

/**
 * 常用的一些函数
 * @author 管雷鸣
 */
public class Func {
	
	/**
	 * 获取当前登陆用户的上级用户代理信息。如果当前用户的上级有，且是代理的话
	 * @return {@link Agency} 或 null
	 */
	public static Agency getParentAgency(){
		UserBean agencyBean = com.xnx3.admin.Func.getUserBeanForShiroSession();
		if(agencyBean == null){
			return null;
		}else{
			return agencyBean.getParentAgency();
		}
	}
	
	/**
	 * 获取当前登陆用户的代理信息。如果当前用户是代理的话
	 * @return {@link Agency} 或 null
	 */
	public static Agency getMyAgency(){
		UserBean agencyBean = com.xnx3.admin.Func.getUserBeanForShiroSession();
		if(agencyBean == null){
			return null;
		}else{
			return agencyBean.getMyAgency();
		}
	}
	
}
