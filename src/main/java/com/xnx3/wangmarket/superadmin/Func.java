package com.xnx3.wangmarket.superadmin;

import com.xnx3.wangmarket.admin.bean.UserBean;
import com.xnx3.wangmarket.superadmin.entity.Agency;
import com.xnx3.wangmarket.superadmin.entity.AgencyData;

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
		UserBean agencyBean = com.xnx3.wangmarket.admin.Func.getUserBeanForShiroSession();
		if(agencyBean == null){
			return null;
		}else{
			return agencyBean.getParentAgency();
		}
	}
	
	/**
	 * 获取当前登陆用户的上级用户代理信息的变长表 (agency_data) 数据。如果当前用户的上级有，且是代理的话
	 * @return {@link AgencyData} 或 null
	 */
	public static AgencyData getParentAgencyData(){
		UserBean agencyBean = com.xnx3.wangmarket.admin.Func.getUserBeanForShiroSession();
		if(agencyBean == null){
			return null;
		}else{
			return agencyBean.getParentAgencyData();
		}
	}
	
	
	/**
	 * 获取当前登陆用户的代理信息。如果当前用户是代理的话
	 * @return {@link Agency} 或 null
	 */
	public static Agency getMyAgency(){
		UserBean agencyBean = com.xnx3.wangmarket.admin.Func.getUserBeanForShiroSession();
		if(agencyBean == null){
			return null;
		}else{
			return agencyBean.getMyAgency();
		}
	}
	

	/**
	 * 获取当前代理信息的变长表 (agency_data) 数据。如果当前用户的上级有，且是代理的话
	 * @return {@link AgencyData} 或 null
	 */
	public static AgencyData getMyAgencyData(){
		UserBean agencyBean = com.xnx3.wangmarket.admin.Func.getUserBeanForShiroSession();
		if(agencyBean == null){
			return null;
		}else{
			return agencyBean.getMyAgencyData();
		}
	}
}
