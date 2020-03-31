package com.xnx3.wangmarket.admin.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.wangmarket.admin.entity.SiteVar;
import com.xnx3.wangmarket.admin.service.SiteVarService;
import com.xnx3.wangmarket.admin.util.CacheUtil;
import net.sf.json.JSONObject;

@Service("siteVarService")
public class SiteVarServiceImpl implements SiteVarService {

	@Resource
	private SqlService sqlService;

	@Override
	public JSONObject getVar(int siteid) {
		//判断缓存中是否有
		String var = CacheUtil.getSiteVar(siteid);
		if(var == null){
			//缓存中没有，那么从数据库中取
			SiteVar siteVar = sqlService.findById(SiteVar.class, siteid);
			if(siteVar == null){
				//没有，返回一个不包含任何数据的JSON对象
				return new JSONObject();
			}
			//将查询出来的数据进行缓存
			CacheUtil.setSiteVar(siteVar);
			var = siteVar.getText();
		}
		
		return JSONObject.fromObject(var);
	}

	@Override
	public JSONObject getVar(int siteid, String key) {
		Object obj = getVar(siteid).get(key);
		if(obj == null){
			return new JSONObject();
		}
		return (JSONObject)obj;
	}

	@Override
	public void setVar(int siteid, SiteVar siteVar) {
		CacheUtil.setSiteVar(siteVar);
	}

}
