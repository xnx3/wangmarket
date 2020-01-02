package com.xnx3.j2ee.pluginManage.interfaces.manage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.springframework.stereotype.Component;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SystemUtil;

/**
 * Shiro 权限，哪个url、目录需要登录，哪个不需要登录，在这里修改
 * @author 管雷鸣
 *
 */
@Component(value="PluginManageForDatabaseLoadFinish")
public class DatabaseLoadFinishPluginManage {
	//处理html源代码的插件，这里开启项目时，便将有关此的插件加入此处
	public static List<Class<?>> classList;
	static{
		List<Class<?>> allClassList = ScanClassUtil.getClasses("com.xnx3");
		classList = ScanClassUtil.searchByInterfaceName(allClassList, "com.xnx3.j2ee.pluginManage.interfaces.DatabaseLoadFinishInterface");
		for (int i = 0; i < classList.size(); i++) {
			ConsoleUtil.info("装载 DatabaseLoadFinish 插件："+classList.get(i).getName());
		}
		
		new DatabaseLoadFinishThread().start();
	}
	
}
class DatabaseLoadFinishThread extends Thread{
	
	@Override
	public void run() {
		boolean b = true;
		while(b){
			try {
				//延迟 1 秒
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(SystemUtil.get("ALLOW_USER_REG") != null && SystemUtil.get("ALLOW_USER_REG").length() > 0){
				b = false;
			}else{
				//项目中的二级域名个数是0，还没有加载域名，等待加载完域名数据后，在进行加载这里的数据
			}
		}
		
		
		for (int i = 0; i < DatabaseLoadFinishPluginManage.classList.size(); i++) {
			try {
				Class<?> c = DatabaseLoadFinishPluginManage.classList.get(i);
				Object invoke = null;
				invoke = c.newInstance();
				//运用newInstance()来生成这个新获取方法的实例  
				Method m = c.getMethod("databaseLoadFinish",new Class[]{});	//获取要调用的init方法  
				//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
				m.invoke(invoke, new Object[]{});
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| InstantiationException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}	
		}
		
	}
	
}
