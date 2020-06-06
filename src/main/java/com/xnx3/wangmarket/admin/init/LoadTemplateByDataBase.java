package com.xnx3.wangmarket.admin.init;

import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SpringUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.wangmarket.admin.entity.Template;
import com.xnx3.wangmarket.admin.util.TemplateUtil;


/**
 * 从数据库中，加载本地模版库的模版，将之存入内存，以便随时调用。分布式部署时，本地私有模版库同步延迟：24小时。如A服务器增加了本地模版库，那么A服务器本地模版库会立马变成最新的，但是B服务器会有个延迟，最多会在24小时内同步为最新的
 * 系统启动时跟随启动
 * @author 管雷鸣
 */
@Component
public class LoadTemplateByDataBase {
	
	public LoadTemplateByDataBase() {
		new Thread(new Runnable() {
			public void run() {
				ConsoleUtil.info("start database template refresh thread.");
				while(true){
					try {
						//避免出问题中断
						load();
						
						//一天同步一次
						Thread.sleep(1000 * 60 * 60 * 24);
					} catch (Exception e) {
						e.printStackTrace();

						//避免出现异常，走了cache，导致死循环，从而使  catalina.out 沾满整个磁盘
						try {
							Thread.sleep(1000 * 60);	//延迟1分钟
						} catch (InterruptedException e1) {
						}
					}
				}
			}
		}).start();
	}
	
	public void load(){
		//等待数据库加载完毕
		while(SystemUtil.get("ALLOW_USER_REG") == null){
			try {
				Thread.sleep(1000);	//延迟1秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//数据库基本配置参数加载完毕后，进行加载本地模版，将之从数据库加载入内存
		List<Template> list = SpringUtil.getSqlService().findByProperty(Template.class, "iscommon", Template.ISCOMMON_YES);
		if(list.size() == 0){
			//如果没有取出数据，那么就不用执行下面的了
			ConsoleUtil.log("LoadTemplateByDataBase Finish , not find local Template");
			return;
		}
		
		//清理之前的数据库内存缓存
		TemplateUtil.databaseTemplateMapForName.clear();
		TemplateUtil.databaseTemplateMapForType.clear();
		//将之加入 内存缓存
		for (int i = 0; i < list.size(); i++) {
			Template template = list.get(i);
			TemplateUtil.databaseTemplateMapForName.put(template.getName(), template);
			
			if(TemplateUtil.databaseTemplateMapForType.get(template.getType()) == null){
				TemplateUtil.databaseTemplateMapForType.put(template.getType(), new HashMap<String, Template>());
			}
			ConsoleUtil.info("load local template : "+template.getName());
			TemplateUtil.databaseTemplateMapForType.get(template.getType()).put(template.getName(), template);
		}
		ConsoleUtil.info("LoadTemplateByDataBase Finish ! local all "+list.size()+" number template");
	}
}
