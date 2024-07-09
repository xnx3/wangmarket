package com.xnx3.wangmarket.domain;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.xnx3.Lang;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SpringUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.wangmarket.domain.bean.SimpleSite;

/**
 * 项目启动初始化，从数据库加载域名列表缓存到内存
 * @author 管雷鸣
 */
@Component
public class InitLoadDomainByDB implements com.xnx3.j2ee.pluginManage.interfaces.DatabaseLoadFinishInterface{
	
	/**
	 * 启动项目时，进行域名绑定，从数据库中取域名相关数据
	 */
	public void domainBind(){
		ConsoleUtil.log(" domainBind start ... ");
		List<Map<String, Object>> list = SpringUtil.getSqlService().findMapBySqlQuery("SELECT id,client,domain,bind_domain,state,template_id FROM site");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			
			SimpleSite ss = new SimpleSite();
			ss.setBindDomain(map.get("bind_domain") != null? map.get("bind_domain").toString():"");
			ss.setClient(map.get("client") != null? Lang.stringToInt(map.get("client").toString(), 1):1);
			ss.setDomain(map.get("domain") != null? map.get("domain").toString():"");
			ss.setId(map.get("id") != null? Lang.stringToInt(map.get("id").toString(), 0) : 0);
			ss.setState((short) (map.get("state") == null ? 1:Lang.stringToInt(map.get("state").toString(), 1)));
			Object templateIdObj = map.get("template_id");
			int templateId = 0;
			if(templateIdObj != null){
				String ti = templateIdObj.toString();
				if(ti.length() > 0){
					templateIdObj = Lang.stringToInt(map.get("template_id").toString(), 0);
				}
			}
			ss.setTemplateId(templateId);
			
			if(ss.getDomain() != null && ss.getDomain().length() > 0){
				G.putDomain(ss.getDomain(), ss);
			}
			if(ss.getBindDomain() != null && ss.getBindDomain().length() > 2){
				G.putBindDomain(ss.getBindDomain(), ss);
			}
		}
		
		ConsoleUtil.info("共缓存二级域名："+G.getDomainSize()+"个， 绑定域名："+G.getBindDomainSize()+"个");
	}

	@Override
	public void databaseLoadFinish() {
		domainBind();
	}
	
}
