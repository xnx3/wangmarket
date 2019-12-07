package com.xnx3.wangmarket.pluginManage.extend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import com.xnx3.Lang;
import com.xnx3.j2ee.func.Log;
import com.xnx3.wangmarket.domain.G;
import com.xnx3.wangmarket.domain.util.PluginCache;
import com.xnx3.wangmarket.pluginManage.Func;
import com.xnx3.wangmarket.pluginManage.PluginExtend;

/**
 * 当数据库加载完成后执行某某方法。 关联 {@link PluginExtend#databaseLoadFinish()}
 * @author 管雷鸣
 * @since 5.0
 */
public class DatabaseLoadFinishThread extends Thread{
	private Class c;
	private Method method;
	
	/**
	 * 自动加载插件表的数据,执行
	 * @param c 扫描到继承 {@link PluginExtend#autoLoadPluginTableDate()}并实现了自动记载数据方法的 Class
	 * @param method 扫描到的{@link PluginExtend#autoLoadPluginTableDate()}
	 */
	public DatabaseLoadFinishThread(Class c, Method method){
		this.c = c;
		this.method = method;
	}
	
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
			if(G.getDomainSize() > 0){
				b = false;
			}else{
				//项目中的二级域名个数是0，还没有加载域名，等待加载完域名数据后，在进行加载这里的数据
			}
		}
		
		try {
			method.invoke(c.newInstance());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| InstantiationException e) {
			e.printStackTrace();
		}
	}
	
}
