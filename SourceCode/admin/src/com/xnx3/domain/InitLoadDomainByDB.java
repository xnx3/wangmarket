package com.xnx3.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.xnx3.DateUtil;
import com.xnx3.domain.bean.SimpleSite;
import com.xnx3.j2ee.Global;

/**
 * 项目启动初始化，从数据库加载域名列表缓存到内存
 * @author 管雷鸣
 */
@Component
public class InitLoadDomainByDB {
	public static boolean cache = false;	//是否已缓存，记录。若为true，则是已缓存
	
	public InitLoadDomainByDB() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				boolean b = true;
				while(b){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(Global.get("SITE_NAME") == null || Global.get("SITE_NAME").length() == 0){
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
		
		ResultSet rs;
		try {
			rs = com.xnx3.j2ee.func.DB.getResultSet("SELECT id,client,domain,bind_domain,state,template_id FROM site");
			//如果有这行存在
	        while(rs.next()){
	        	SimpleSite ss = new SimpleSite();
	        	ss.setBindDomain(rs.getString("bind_domain"));
	        	ss.setClient(rs.getInt("client"));
	        	ss.setDomain(rs.getString("domain"));
	        	ss.setId(rs.getInt("id"));
	        	ss.setState(rs.getShort("state"));
	        	ss.setTemplateId(rs.getInt("template_id"));
	        	
	        	if(ss.getDomain() != null && ss.getDomain().length() > 0){
					G.putDomain(ss.getDomain(), ss);
				}
				if(ss.getBindDomain() != null && ss.getBindDomain().length() > 2){
					G.putBindDomain(ss.getBindDomain(), ss);
				}
	        }
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("共缓存二级域名："+G.getDomainSize()+"个， 绑定域名："+G.getBindDomainSize()+"个");
	}
	
}
