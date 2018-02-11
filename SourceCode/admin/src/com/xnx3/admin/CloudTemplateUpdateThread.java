package com.xnx3.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.xnx3.j2ee.func.Log;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;

/**
 * 云端模版更新使用线程。每间隔1天同步一次云端模版库的最新模版
 * @author 管雷鸣
 */
@Component
public class CloudTemplateUpdateThread {
	
	public CloudTemplateUpdateThread() {
		
		new Thread(new Runnable() {
			public void run() {
				Log.error("启动cloudTnread");
				//10秒后启动
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				//从配置文件中加载云端模板信息
				G.cloudTemplateMap = new HashMap<String, String>();
				
				while(true){
					HttpUtil http = new HttpUtil(HttpUtil.UTF8);
					HttpResponse hr = http.get("http://res.weiunity.com/cloudControl/cmsTemplate.json");
					if(hr.getCode() - 200 == 0){
						//取到新数据了
						if(hr.getContent() != null){
							JSONObject json = null;
							try {
								//使用try，避免cdn出现问题，终止此模版更新线程
								json = JSONObject.fromObject(hr.getContent());
							} catch (Exception e) {
								Log.error(e.getMessage());
								e.printStackTrace();
							}
							
							if(json != null){
								if(json.get("result") != null){
									if(json.getString("result").equals("1")){
										//获取云模版成功，同步到系统中
										JSONArray jsonArray = json.getJSONArray("list");
										for (int i = 0; i < jsonArray.size(); i++) {
											JSONObject temp = jsonArray.getJSONObject(i);
											G.cloudTemplateMap.put(temp.getString("name"), temp.getString("intro"));
										}
										System.out.println("同步云模版库完成，共"+G.cloudTemplateMap.size()+"个");
									}
								}else{
									Log.error("非正常现象。同步云端模版库失败，接收json.result为null");
								}
							}else{
								Log.error("非正常现象。同步云端模版库失败，接收json为null");
							}
						}
					}
					
					
					try {
						//每间隔1天同步一次  1000 * 60 * 60 * 24 ＝ 86400 000
						Thread.sleep(86400000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}
	
}
