package com.xnx3.wangmarket.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.wangmarket.domain.bean.SimpleSite;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SpringUtil;
import com.xnx3.j2ee.util.SystemUtil;

/**
 * 项目启动初始化，从数据库加载域名列表缓存到内存
 * @author 管雷鸣
 */
@Component
public class InitLoadDomainByDB {
	public static boolean cache = false;	//是否已缓存，记录。若为true，则是已缓存
	
	public InitLoadDomainByDB() {
		new Thread(new Runnable() {
			
			public void run() {
				boolean b = true;
				while(b){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(SystemUtil.get("ATTACHMENT_FILE_MODE") == null || SystemUtil.get("ATTACHMENT_FILE_MODE").length() == 0){
						//项目还未启动，system数据都还没有加载，继续等待
					}else{
						//system数据表数据已加载入内存，可以进行初始化域名数据了，退出等待
						b = false;
					}
				}
				
				domainBind();
			}
		}).start();
		
	}
	
	/**
	 * 启动项目时，进行域名绑定，从数据库中取域名相关数据
	 */
	public void domainBind(){
		if(cache){
			//已缓存，无需再缓存了
			return;
		}
		InitLoadDomainByDB.cache = true;
		
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
		
		
		
//		ResultSet rs;
//		try {
//			rs = com.xnx3.j2ee.func.DB.getResultSet("SELECT id,client,domain,bind_domain,state,template_id FROM site");
//			//如果有这行存在
//	        while(rs.next()){
//	        	SimpleSite ss = new SimpleSite();
//	        	ss.setBindDomain(rs.getString("bind_domain"));
//	        	ss.setClient(rs.getInt("client"));
//	        	ss.setDomain(rs.getString("domain"));
//	        	ss.setId(rs.getInt("id"));
//	        	ss.setState(rs.getShort("state"));
//	        	ss.setTemplateId(rs.getInt("template_id"));
//	        	
//	        	if(ss.getDomain() != null && ss.getDomain().length() > 0){
//					G.putDomain(ss.getDomain(), ss);
//				}
//				if(ss.getBindDomain() != null && ss.getBindDomain().length() > 2){
//					G.putBindDomain(ss.getBindDomain(), ss);
//				}
//	        }
//	        
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		ConsoleUtil.info("共缓存二级域名："+G.getDomainSize()+"个， 绑定域名："+G.getBindDomainSize()+"个");
	}
	
}
