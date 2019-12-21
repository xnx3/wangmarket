package com.xnx3.wangmarket.domain;

import java.util.HashMap;
import java.util.Map;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.net.AliyunLogUtil;
import com.xnx3.wangmarket.domain.bean.SimpleSite;

/**
 * 全局信息
 * @author 管雷鸣
 */
public class G {
	//二级域名数组，中间存放着用于使用的二级域名的主域名。其中，第一个(twoDomainArray[0])会在程序中显示出来
	//private static String[] twoDomainArray = null;
	
	
	//CDN缓存的资源文件，包括框架的js、css文件、模版style.css文件等。
	public static final String RES_CDN_DOMAIN = "http://res.weiunity.com/";	
	
	/*
	 * 使用系统赠送的二级域名访问，只要是建立过的网站，都会加入此,持久缓存
	 * key: domain 二级域名的名字，不含 .wang.market
	 */
	public static Map<String, SimpleSite> domainSiteMap = new HashMap<String, SimpleSite>();
	
	//使用绑定后的域名访问，这里只有绑定域名后才会加入此处,持久缓存
	public static Map<String, SimpleSite> bindDomainSiteMap = new HashMap<String, SimpleSite>();
	
	
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
	
	/**
	 * 获取泛解析的主域名列表，获取到的域名列表便是分配给用户的二级域名。
	 * 注意的是，第一个域名会作为网站的官分配的二级域名，在程序里会体现第一个域名。其他的都是备用的，在程序中不会体现，只有用户使用二级域名访问时才会有效
	 */
	public static String[] getAutoAssignDomain(){
//		if(twoDomainArray == null){
//			//system表中AUTO_ASSIGN_DOMAIN载入
//			String d = Global.get("AUTO_ASSIGN_DOMAIN");
//			twoDomainArray = d.split(",");
//		}
		//system表中AUTO_ASSIGN_DOMAIN载入
		String d = SystemUtil.get("AUTO_ASSIGN_DOMAIN");
		
		return d.split(",");
	}
	
	/**
	 * 返回当前系统中，网站创建成功后自动分配的二级域名，AUTO_ASSIGN_DOMAIN的第一个，会显示给用户的那个域名
	 * v4.8版本废弃。由 com.xnx3.wangmarket.admin.G.getFirstAutoAssignDomain() 代替
	 */
//	public static String getAutoAssignMainDomain(){
//		if(getAutoAssignDomain().length > 0){
//			return getAutoAssignDomain()[0];
//		}else{
//			return "未设置自动分配的二级(主)域名！";
//		}
//	}
	
}
