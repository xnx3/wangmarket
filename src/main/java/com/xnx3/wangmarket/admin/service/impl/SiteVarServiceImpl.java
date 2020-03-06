package com.xnx3.wangmarket.admin.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.service.SiteVarService;
import net.sf.json.JSONObject;

@Service("siteVarService")
public class SiteVarServiceImpl implements SiteVarService {

	@Resource
	private SiteService siteService;

	@Override
	public JSONObject getVar(int siteid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getVar(int siteid, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVar(int siteid, String key, String value) {
		// TODO Auto-generated method stub
		
	}

}
