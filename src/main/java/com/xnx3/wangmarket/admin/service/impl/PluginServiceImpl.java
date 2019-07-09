package com.xnx3.wangmarket.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xnx3.file.FileUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.cache.Template;
import com.xnx3.wangmarket.admin.entity.InputModel;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.pluginManage.PluginManage;
import com.xnx3.wangmarket.admin.pluginManage.SitePluginBean;
import com.xnx3.wangmarket.admin.service.InputModelService;
import com.xnx3.wangmarket.admin.service.PluginService;
import com.xnx3.wangmarket.admin.util.AliyunLog;
import com.xnx3.wangmarket.admin.vo.bean.NewsInit;

@Service("PluginService")
public class PluginServiceImpl implements PluginService {

	public Map<String, SitePluginBean> getCurrentPluginMap() {
		//将所有当前安装的插件，都加入这个map中。因为有可能一个插件在总管理后台中出现，也会在代理后台中出现，用map也会进行排重，所以不用list
		Map<String, SitePluginBean> pluginMap = new HashMap<String, SitePluginBean>();
		
		//将 总管理后台中的插件加入 pluginMap
		for (Map.Entry<String, SitePluginBean> entry : PluginManage.superAdminClassManage.entrySet()) {
			pluginMap.put(entry.getKey(), entry.getValue());
		}
		//将 代理后台中的插件加入 pluginMap
		for (Map.Entry<String, SitePluginBean> entry : PluginManage.agencyClassManage.entrySet()) {
			pluginMap.put(entry.getKey(), entry.getValue());
		}
		//将 网站管理后台中的插件加入 pluginMap
		for (Map.Entry<String, SitePluginBean> entry : PluginManage.cmsSiteClassManage.entrySet()) {
			pluginMap.put(entry.getKey(), entry.getValue());
		}
		
		return pluginMap;
	}
	
}
