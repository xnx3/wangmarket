package com.xnx3.wangmarket.admin.util;

import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.wangmarket.admin.entity.Site;

/**
 * 域名相关
 * @author 管雷鸣
 *
 */
public class DomainUtil {
	
	/**
	 * 获取泛解析的主域名，泛解析的域名可能有多个，但是第一个是主域名，使用的，这里拿的就是 AUTO_ASSIGN_DOMAIN 里面的第一个
	 * @return 返回如 wang.market 若没有，返回空字符串""
	 */
	public static String getAssignMainDomain(){
		String domain = SystemUtil.get("AUTO_ASSIGN_DOMAIN");
		if(domain == null){
			return "";
		}
		String[] ds = domain.split(",");
		return ds[0];
	}
	

	/**
	 * 获取当前网站的访问域名。若是绑定顶级域名了，优先使用顶级域名。
	 * @return 访问域名，如 leiwen.wang.market
	 */
	public static String getDomain(Site site){
		if(site == null){
			return "";
		}
		if(site.getBindDomain() != null && site.getBindDomain().length() > 3){
			return site.getBindDomain();
		}else{
			return site.getDomain()+"."+getAssignMainDomain();
		}
	}
	
}
