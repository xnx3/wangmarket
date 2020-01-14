package com.xnx3.wangmarket.agencyadmin.util;

import java.util.Map;
import com.xnx3.wangmarket.admin.entity.InputModel;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.vo.TemplateVarVO;
import com.xnx3.wangmarket.agencyadmin.entity.Agency;
import com.xnx3.wangmarket.agencyadmin.entity.AgencyData;

/**
 * 网市场代理后台的sessison
 * @author 管雷鸣
 *
 */
public class SessionUtil extends com.xnx3.wangmarket.admin.util.SessionUtil{
	//我的代理信息，如果我是代理的话，才有内容
	public static final String PLUGIN_NAME_AGENCY_MY = "wangmarket_agency_my";	
	//我的代理信息-边长表agency_data的信息，如果我是代理的话，才有内容
	public static final String PLUGIN_NAME_AGENCY_DATA_MY = "wangmarket_agency_data_my";	
	
	//我的上级代理信息，如果我有上级代理的话，才有内容
	public static final String PLUGIN_NAME_AGENCY_PARENT = "wangmarket_agency_parent";	
	//我的上级代理信息-变长表agency_data的信息，如果我有上级代理的话，才有内容
	public static final String PLUGIN_NAME_AGENCY_DATA_PARENT = "wangmarket_agency_data_parent";	
	
	
	/**
	 * 获取我的代理信息
	 */
	public static Agency getAgency(){
		return getPlugin(PLUGIN_NAME_AGENCY_MY);
	}
	
	/**
	 * 设置我的代理信息
	 */
	public static void setAgency(Agency agency){
		setPlugin(PLUGIN_NAME_AGENCY_MY, agency);
	}
	
	/**
	 * 获取我的代理信息-变长表的信息
	 */
	public static AgencyData getAgencyData(){
		return getPlugin(PLUGIN_NAME_AGENCY_DATA_MY);
	}
	/**
	 * 设置我的代理信息-变长表的信息
	 */
	public static void setAgencyData(AgencyData agencyData){
		setPlugin(PLUGIN_NAME_AGENCY_DATA_MY, agencyData);
	}
	
	

	/**
	 * 获取我的上级代理信息
	 */
	public static Agency getParentAgency(){
		return getPlugin(PLUGIN_NAME_AGENCY_PARENT);
	}
	
	/**
	 * 设置我的上级代理信息
	 */
	public static void setParentAgency(Agency agency){
		setPlugin(PLUGIN_NAME_AGENCY_PARENT, agency);
	}

	/**
	 * 获取我的上级代理信息-变长表的信息
	 */
	public static AgencyData getParentAgencyData(){
		return getPlugin(PLUGIN_NAME_AGENCY_DATA_PARENT);
	}
	/**
	 * 设置我的上级代理信息-变长表的信息
	 */
	public static void setParentAgencyData(AgencyData agencyData){
		setPlugin(PLUGIN_NAME_AGENCY_DATA_PARENT, agencyData);
	}
	
}

