package com.xnx3.wangmarket.admin.service;

import java.util.List;
import java.util.Map;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.InputModel;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.pluginManage.SitePluginBean;
import com.xnx3.wangmarket.admin.vo.bean.NewsInit;

/**
 * 插件服务
 * @author 管雷鸣
 */
public interface PluginService {
	
	/**
	 * 获取当前系统已安装的所有功能插件
	 * @return map key: 插件的id
	 */
	public Map<String, SitePluginBean> getCurrentPluginMap();
}
