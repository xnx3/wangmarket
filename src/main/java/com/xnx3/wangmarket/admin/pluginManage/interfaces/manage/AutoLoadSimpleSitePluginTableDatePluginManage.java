package com.xnx3.wangmarket.admin.pluginManage.interfaces.manage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.xnx3.Lang;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.PluginUtil;
import com.xnx3.j2ee.util.SpringUtil;
import com.xnx3.wangmarket.domain.util.PluginCache;

/**
 * 自动加载插件表的数据到内存，关联上 siteid
 * @author 管雷鸣
 *
 */
@Component(value="PluginManageForAutoLoadSimpleSitePluginTableDate")
public class AutoLoadSimpleSitePluginTableDatePluginManage {
	//处理html源代码的插件，这里开启项目时，便将有关此的插件加入此处
	public static List<Class<?>> classList;
	static{
		classList = new ArrayList<Class<?>>();
		
		new Thread(new Runnable() {
			public void run() {
				while(SpringUtil.getApplicationContext() == null){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				//当 SpringUtil 被Spring 加载后才会执行
				List<Class<?>> allClassList = ScanClassUtil.getClasses("com.xnx3.wangmarket");
				classList = ScanClassUtil.searchByInterfaceName(allClassList, "com.xnx3.wangmarket.admin.pluginManage.interfaces.AutoLoadSimpleSitePluginTableDateInterface");
				for (int i = 0; i < classList.size(); i++) {
					ConsoleUtil.info("装载 AutoLoadSimpleSitePluginTableDate 插件："+classList.get(i).getName());
				}
				
				execute();
			}
		}).start();
	}
	
	/**
	 * 自动加载简单的插件表的数据，如cnzz插件，只有一个数据表的
	 * @return 更改后的针对url的设定，会讲这个返回值重新赋予 Shiro
	 */
	public static void execute(){
		SqlService sqlservice = SpringUtil.getSqlService();
		
		/**** 针对html源代码处理的插件 ****/
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			
			try {
				Object invoke = c.newInstance();
				//运用newInstance()来生成这个新获取方法的实例  
				Method m = c.getMethod("autoLoadSimpleSitePluginTableDate",new Class[]{});	//获取要调用的init方法  
				//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
				Object o = m.invoke(invoke, new Object[]{});
				if(o != null && !o.equals("null")){
					String sql = o.toString();
					
					//当前插件id
					String pluginId = PluginUtil.getPluginId(c.getName());
					if(pluginId == null || pluginId.length() == 0){
						return;
					}
					List<Map<String, Object>> list = sqlservice.findMapBySqlQuery(sql);
					//将数据库查询到的结果，以 key ： siteid，  value ：查询到的结果 ， 的方式，存入map，以便根据siteid来取信息
					for (int j = 0; j < list.size(); j++) {
						Map<String, Object> map = list.get(j);
						if(map.get("siteid") != null){
							int siteid = Lang.stringToInt(map.get("siteid").toString(), 0);
							PluginCache.setPluginMap(siteid, pluginId, map);
						}
					}
					ConsoleUtil.info("AutoLoadSimpleSitePluginTableDate , plugin_"+pluginId+" load "+list.size()+" number data");
				}
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
