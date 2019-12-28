package com.xnx3.wangmarket.domain.pluginManage.interfaces.manage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.springframework.stereotype.Component;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.PluginUtil;
import com.xnx3.wangmarket.domain.mq.DomainMQ;

/**
 * 插件管理
 * @author 管雷鸣
 *
 */
@Component(value="PluginManageForCreateReceiveMQ")
public class CreateReceiveMQPluginManage {
	//处理html源代码的插件，这里开启项目时，便将有关此的插件加入此处
	public static List<Class<?>> classList;
	static{
		classList = ScanClassUtil.getClasses("com.xnx3.wangmarket");
		classList = ScanClassUtil.searchByInterfaceName(classList, "com.xnx3.wangmarket.domain.pluginManage.interfaces.CreateReceiveMQInterface");
		for (int i = 0; i < classList.size(); i++) {
			ConsoleUtil.info("装载 CreateReceiveMQ 插件："+classList.get(i).getName());
		}
		
		ConsoleUtil.info("classList.size() "+classList.size());
		new Thread(new Runnable() {
			public void run() {
				while(Global.system.size() < 5){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				execute();
			}
		}).start();
	}
	
	public static void execute(){
		/**** 针对html源代码处理的插件 ****/
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			//取得插件id
			String pluginId = PluginUtil.getPluginId(c.getName());
			if(pluginId == null || pluginId.length() == 0){
				return;
			}
			try {
				DomainMQ.receive(pluginId);
				ConsoleUtil.info("CreateReceiveMQPluginManage "+pluginId+" Finish");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				Object invoke = c.newInstance();
				//运用newInstance()来生成这个新获取方法的实例  
				Method m = c.getMethod("createReceiveMQForDomain",new Class[]{});	//获取要调用的init方法  
				//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
				m.invoke(invoke, new Object[]{});
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
}
