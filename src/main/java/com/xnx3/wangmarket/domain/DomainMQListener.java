package com.xnx3.wangmarket.domain;

import org.springframework.stereotype.Component;
import com.xnx3.j2ee.func.Log;
import com.xnx3.wangmarket.domain.G;
import com.xnx3.wangmarket.domain.bean.MQBean;
import com.xnx3.wangmarket.domain.bean.SimpleSite;
import com.xnx3.wangmarket.domain.mq.DomainMQ;
import com.xnx3.wangmarket.domain.mq.ReceiveDomainMQ;
import net.sf.json.JSONObject;

/**
 * MQ消息监听
 * @author 管雷鸣
 *
 */
@Component
public class DomainMQListener {
	public DomainMQListener() {
		Log.info("domain load binddomain MQListener");
		DomainMQ.receive("domain", new ReceiveDomainMQ() {
			public void receive(String content) {
				JSONObject json = JSONObject.fromObject(content);
				JSONObject simpleSiteJson = json.getJSONObject("simpleSite");
				
				MQBean mqBean = new MQBean();
				mqBean.setType(json.getInt("type"));
				mqBean.setOldValue(json.getString("oldValue"));
				SimpleSite simpleSite = new SimpleSite();
				simpleSite.setBindDomain(simpleSiteJson.getString("bindDomain"));
				simpleSite.setDomain(simpleSiteJson.getString("domain"));
				simpleSite.setState((short)simpleSiteJson.getInt("state"));
				simpleSite.setClient(simpleSiteJson.getInt("client"));
				simpleSite.setId(simpleSiteJson.getInt("id"));
				simpleSite.setTemplateId(simpleSiteJson.getInt("templateId"));
				
				switch (json.getInt("type")) {
				case MQBean.TYPE_BIND_DOMAIN:
					//更改自己绑定的顶级域名
					//判断是否有旧的顶级域名，是在修改，还是第一次绑定
					SimpleSite ss = G.bindDomainSiteMap.get(mqBean.getOldValue());
					if(ss == null){
						//是新绑定的
						ss = new SimpleSite();
						ss.clone(simpleSite);
					}
					//修改自己绑定的顶级域名
					ss.setBindDomain(simpleSite.getBindDomain());
					G.bindDomainSiteMap.put(simpleSite.getBindDomain(), ss);
					
					break;
				case MQBean.TYPE_DOMAIN:
					//更改系统分配的二级域名
					SimpleSite ss2 = G.domainSiteMap.get(mqBean.getOldValue());
					ss2.setDomain(simpleSite.getDomain());
					G.domainSiteMap.put(simpleSite.getDomain(), ss2);
					
					break;
				case MQBean.TYPE_NEW_SITE:
					//新建立的网站
					G.domainSiteMap.put(simpleSite.getDomain(), simpleSite);
					
					break;
				case MQBean.TYPE_STATE:
					//更改网站的状态
					
					//判断是否有自己绑定的顶级域名
					SimpleSite ss41 = G.bindDomainSiteMap.get(simpleSite.getBindDomain());
					if(ss41 != null){
						//有顶级域名，那么更新其顶级域名的缓存
						ss41.setState(simpleSite.getState());
						G.bindDomainSiteMap.put(simpleSite.getBindDomain(), ss41);
					}
					
					//更新二级域名的
					SimpleSite ss42 = G.domainSiteMap.get(simpleSite.getDomain());
					if(ss42 != null){
						//有二级域名，那么更新其顶级域名的缓存。 理论上二级域名是必须有的
						ss42.setState(simpleSite.getState());
						G.domainSiteMap.put(simpleSite.getDomain(), ss42);
					}
					
					break;
				default:
					break;
				}
				
				
			}
		});
		
	}
	
}

