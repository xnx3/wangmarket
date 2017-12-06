package com.xnx3.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xnx3.ConfigManagerUtil;
import com.xnx3.net.AliyunLogUtil;
import com.xnx3.net.MNSUtil;
import com.xnx3.domain.bean.SimpleSite;

/**
 * 全局信息
 * @author 管雷鸣
 */
public class G {
	//二级域名数组，中间存放着用于使用的二级域名的主域名。其中，第一个(twoDomainArray[0])会在程序中显示出来
	public static String[] twoDomainArray;
	
	//日志服务，用于统计访问日志。 topic:访问域名
	public static AliyunLogUtil aliyunLogUtil;
	
	//由domainConfig.xml 的 aliyunMNS_Domain节点加载数据
	public static String mnsDomain_accessKeyId = "";
	public static String mnsDomain_accessKeySecret = "";
	public static String mnsDomain_endpoint = "";
	public static String mnsDomain_queueName = "";
	public static MNSUtil domainMNSUtil;
	
	static{
		ConfigManagerUtil wangMarketConfig = ConfigManagerUtil.getSingleton("domainConfig.xml");
		
		//将domainConfig.xml中的二级域名配置的主域名，加载入
		List<String> list = wangMarketConfig.getList("twoDomain.domain");
		twoDomainArray = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			twoDomainArray[i] = list.get(i);
		}
		
		aliyunLogUtil = new AliyunLogUtil(wangMarketConfig.getValue("aliyunLog.endpoint"), wangMarketConfig.getValue("aliyunLog.accessKeyId"), wangMarketConfig.getValue("aliyunLog.accessKeySecret"), wangMarketConfig.getValue("aliyunLog.project"), wangMarketConfig.getValue("aliyunLog.logstore"));	
		
		//加载domain 的 MNS 数据
		mnsDomain_accessKeyId = wangMarketConfig.getValue("aliyunMNS_Domain.accessKeyId");
		mnsDomain_accessKeySecret = wangMarketConfig.getValue("aliyunMNS_Domain.accessKeySecret");
		mnsDomain_endpoint = wangMarketConfig.getValue("aliyunMNS_Domain.endpoint");
		mnsDomain_queueName = wangMarketConfig.getValue("aliyunMNS_Domain.queueName");
		if(mnsDomain_accessKeyId == null || mnsDomain_accessKeyId.length() == 0){
			System.out.println("您未开启分布式域名更新功能(MQ)，若是后台跟网站访问分开部署的，此项为必须配置的！若是后台跟网站访问是在一块的，此项无需配置。此条提示忽略即可");
		}else{
			domainMNSUtil = new MNSUtil(G.mnsDomain_accessKeyId, G.mnsDomain_accessKeySecret, G.mnsDomain_endpoint);
		}
		
	}
	
	//CDN缓存的资源文件，包括框架的js、css文件、模版style.css文件等。
	public static final String RES_CDN_DOMAIN = "http://res.weiunity.com/";	
	
	//使用系统赠送的二级域名访问，只要是建立过的网站，都会加入此
	private static Map<String, SimpleSite> domainSiteMap = new HashMap<String, SimpleSite>();
	
	//使用绑定后的域名访问，这里只有绑定域名后才会加入此处
	private static Map<String, SimpleSite> bindDomainSiteMap = new HashMap<String, SimpleSite>();
	
	/**
	 * 更新站点的二级域名缓存，二级域名是系统自己分配的
	 * @param domain 二级域名的名字，不含 .wang.market
	 * @param ss {@link SimpleSite}
	 */
	public static void putDomain(String domain, SimpleSite ss){
		domainSiteMap.put(domain, ss);
	}
	
	/**
	 * 更新站点自己绑定的域名 缓存
	 * @param domain 自己绑定的域名，如 www.xnx3.com
	 * @param ss {@link SimpleSite}
	 */
	public static void putBindDomain(String bindDomain, SimpleSite ss){
		bindDomainSiteMap.put(bindDomain, ss);
	}
	
	/**
	 * 通过站点自动分配的二级域名（不包含.wang.market ,仅仅只是二级域名的名字）获取站点信息
	 * @param domain 二级域名（不包含.wang.market ,仅仅只是二级域名的名字）
	 * @return {@link SimpleSite}
	 */
	public static SimpleSite getDomain(String domain){
		return domainSiteMap.get(domain);
	}
	
	/**
	 * 通过站点的绑定的域名获取站点信息
	 * @param bindDomain 绑定的域名 
	 * @return {@link SimpleSite}
	 */
	public static SimpleSite getBindDomain(String bindDomain){
		return bindDomainSiteMap.get(bindDomain);
	}
	
	/**
	 * 获取自动分配的二级域名的个数
	 * @return
	 */
	public static int getDomainSize(){
		return domainSiteMap.size();
	}
	
	/**
	 * 获取绑定的域名的个数
	 * @return
	 */
	public static int getBindDomainSize(){
		return bindDomainSiteMap.size();
	}
}
