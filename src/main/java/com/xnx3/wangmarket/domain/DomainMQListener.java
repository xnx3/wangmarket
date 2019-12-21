package com.xnx3.wangmarket.domain;

import java.util.Map;
import org.springframework.stereotype.Component;

import com.xnx3.j2ee.util.ConsoleUtil;
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
		ConsoleUtil.info("domain load binddomain MQListener");
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
				mqBean.setSimpleSite(simpleSite);
				
				switch (json.getInt("type")) {
				case MQBean.TYPE_BIND_DOMAIN:
					//更改自己绑定的顶级域名
					
					SimpleSite ss = null;
					
					//判断以下几种情况
					if(simpleSite.getBindDomain().length() > 0){
						//是更改，或第一次新绑定顶级域名
						
						if(mqBean.getOldValue().length() > 0){
							//oldvalue有值，有旧域名，那么就是对原本绑定的域名进行更改
							ss = G.bindDomainSiteMap.get(mqBean.getOldValue());
							if(ss == null){
								//如果为空，那么重新创建一个。理论上这个是只有在系统启动时，数据库的数据尚未加载过来才会找不到，可以约等于是不存在的！应警报！
								ConsoleUtil.error("如果为空，那么重新创建一个。理论上这个是只有在系统启动时，数据库的数据尚未加载过来才会找不到，可以约等于是不存在的！应警报！--"+mqBean);
							}
							//吧旧的域名删除掉，让旧域名无效
							G.bindDomainSiteMap.remove(mqBean.getOldValue());
						}else{
							//之前没有绑定顶级域名，所以oldvalue是空，那么这里是新站刚绑定一个顶级域名
							//绑定自己的域名，从原本的二级域名中，获取缓存信息。因为创建网站后，二级域名便一直存在的
							ss = G.domainSiteMap.get(simpleSite.getDomain());
						}
						
						//更新绑定的域名信息
						ss.setBindDomain(simpleSite.getBindDomain());
						
						//更新缓存
						G.bindDomainSiteMap.put(simpleSite.getBindDomain(), ss);
					}else{
						//新绑定的域名长度为0，那肯定就是解除绑定了。解除绑定，拿到就的域名，直接将这个map的key删掉
						G.bindDomainSiteMap.remove(mqBean.getOldValue());
					}
					
					break;
				case MQBean.TYPE_DOMAIN:
					//更改系统分配的二级域名
					SimpleSite ss2 = G.domainSiteMap.get(mqBean.getOldValue());
					ss2.setDomain(simpleSite.getDomain());
					//删除旧的
					G.domainSiteMap.remove(mqBean.getOldValue());
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

