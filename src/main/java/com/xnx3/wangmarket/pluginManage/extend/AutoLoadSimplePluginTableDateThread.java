package com.xnx3.wangmarket.pluginManage.extend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import com.xnx3.Lang;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SpringUtil;
import com.xnx3.wangmarket.domain.G;
import com.xnx3.wangmarket.domain.util.PluginCache;
import com.xnx3.wangmarket.pluginManage.Func;
import com.xnx3.wangmarket.pluginManage.PluginExtend;

/**
 * 自动加载插件表的数据。 关联 {@link PluginExtend#autoLoadPluginTableDate()}
 * @author 管雷鸣
 *
 */
public class AutoLoadSimplePluginTableDateThread extends Thread{
	public boolean cache = false;	//是否已缓存，记录。若为true，则是已缓存
	public String pluginId;		//功能插件的id， @PluginRegister 注册的id， 如 cnzz统计插件，这里便是 cnzz
	public String querySql;		//查询数据的sql语句，传入如： SELECT * FROM plugin_cnzz
	
	/**
	 * 自动加载插件表的数据,执行
	 * @param c 扫描到继承 {@link PluginExtend#autoLoadPluginTableDate()}并实现了自动记载数据方法的 Class
	 * @param method 扫描到的{@link PluginExtend#autoLoadPluginTableDate()}
	 */
	public static void execute(Class c, Method method){
		String pluginId = Func.getPluginId(c.getName());
		if(pluginId == null || pluginId.length() == 0){
			return;
		}
		
		try {
			Object obj = method.invoke(c.newInstance());
			if(obj != null){
				String sql = obj.toString();
				AutoLoadSimplePluginTableDateThread thread = new AutoLoadSimplePluginTableDateThread();
				thread.pluginId = pluginId;
				thread.querySql = sql;
				thread.setName(pluginId);
				thread.start();
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| InstantiationException e) {
			e.printStackTrace();
		}
	}
	
		
	@Override
	public void run() {
		boolean b = true;
		while(b){
			try {
				//延迟 10 秒
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(G.getDomainSize() > 0){
				b = false;
			}else{
				//项目中的二级域名个数是0，还没有加载域名，等待加载完域名数据后，在进行加载这里的数据
			}
		}
		
		loadData();
		
	}
	

	/**
	 * 加载数据，从数据库中
	 */
	private void loadData(){
		if(this.cache){
			//已缓存，无需再缓存了
			return;
		}
		
		List<Map<String, Object>> list = SpringUtil.getSqlService().findMapBySqlQuery(this.querySql);
		//将数据库查询到的结果，以 key ： siteid，  value ：查询到的结果 ， 的方式，存入map，以便根据siteid来取信息
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			if(map.get("siteid") != null){
				int siteid = Lang.stringToInt(map.get("siteid").toString(), 0);
				PluginCache.setPluginMap(siteid, this.pluginId, map);
			}
		}
		ConsoleUtil.info("PluginExtend AutoLoadSimplePluginTableDate , plugin_"+this.pluginId+" load "+list.size()+" number data");
		
		this.cache = true;
	}
}
