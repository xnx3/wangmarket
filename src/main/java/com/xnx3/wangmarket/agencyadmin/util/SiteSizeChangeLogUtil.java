package com.xnx3.wangmarket.agencyadmin.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import com.xnx3.Lang;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.util.ConsoleUtil;
import cn.zvo.log.DatasourceInterface;
import cn.zvo.log.Log;
import cn.zvo.log.datasource.console.ConsoleDataSource;
import cn.zvo.log.framework.springboot.ApplicationConfig;

/**
 * 日志
 * @author 管雷鸣
 */
@EnableConfigurationProperties(ApplicationConfig.class)
@Configuration
public class SiteSizeChangeLogUtil{
	public static Log log;
    @Resource
    private ApplicationConfig config;

	static{
		new SiteSizeChangeLogUtil().init();
	}
	
	/**
	 * 初始化
	 */
	public void init() {
		loadConfig(this.config);
	}

    /**
     * 加载配置 {@link ApplicationConfig} 文件，通过其属性来决定使用何种配置。
     * <br>这个其实就相当于用java代码来动态决定配置
     * @param config
     */
    public static void loadConfig(ApplicationConfig config) {
    	if(config == null) {
    		return;
    	}
    	
    	log = new Log();
    	
    	if(config.getCacheMaxNumber() != null && config.getCacheMaxNumber().trim().length() > 0) {
			log.setCacheMaxNumber(Lang.stringToInt(config.getCacheMaxNumber(), 100));
		}
		if(config.getCacheMaxTime() != null && config.getCacheMaxTime().trim().length() > 0) {
			log.setCacheMaxTime(Lang.stringToInt(config.getCacheMaxTime(), 60));
		}

		if(config.getDataSource() != null) {
			for (Map.Entry<String, Map<String, String>> entry : config.getDataSource().entrySet()) {
				//拼接，取该插件在哪个包
				String datasourcePackage = "cn.zvo.log.datasource."+entry.getKey();
				
				List<Class<?>> classList = ScanClassUtil.getClasses(datasourcePackage);
				//搜索继承StorageInterface接口的
				List<Class<?>> logClassList = ScanClassUtil.searchByInterfaceName(classList, "cn.zvo.log.DatasourceInterface");
				for (int i = 0; i < logClassList.size(); i++) {
					Class logClass = logClassList.get(i);
					ConsoleUtil.debug("log datasource : "+logClass.getName());
					try {
						Object newInstance = logClass.getDeclaredConstructor(Map.class).newInstance(entry.getValue());
//						LogInterface logInterface = (LogInterface) newInstance;
						DatasourceInterface datasource = (DatasourceInterface)newInstance;
						log.setDatasource(datasource);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException| InvocationTargetException  | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		log.setCacheMaxNumber(1); //不缓存，直接保存，不能丢掉金钱相关记录
    }
    
    /**
     * 获取log对象
     */
    public static Log getLog() {
    	if(log == null) {
    		new SiteSizeChangeLogUtil().init();
    		if(log == null) {
    			log = new Log();
    		}
    	}
    	return log;
    }
    
	
	/**
	 * 增加动作日志。此方法不可直接调用，需间接
	 * @param userid 当前日志属于哪个登录用户的，对应其user.id
	 * @param username 当前日志属于哪个登录用户的，对应其user.username
	 * @param agencyName 当前登录的代理的 {@link Agency}.name 若是不是代理，那就是管理员了，管理员可传入空字符串
	 * @param remark 备注说明
	 * @param agencySiteSizeChange 代理变动的“站”余额的多少，消耗为负数，增加为正数  {@link Agency}.siteSize
	 * @param changeBefore 变动前，站点的站余额是多少， {@link Agency}.siteSize
	 * @param changeAfter 变化之后的站点的站余额是多少。{@link Agency}.siteSize
	 * @param goalid 其余额变动，是开通的哪个站点引起的，记录站点的id，或者是哪个人给他增加的，记录给他增加的人的userid
	 * @param ip 操做人的ip地址
	 * @param topic 主题，分类。减去站币，消费，传入"xiaofei"， 增加站币，充值，传入"chongzhi"
	 */
	private static void addChangeLog(int userid, String username, String agencyName, String remark, int agencySiteSizeChange, int changeBefore, int changeAfter, int goalid, String ip, String topic){
		StackTraceElement st = Thread.currentThread().getStackTrace()[3];
		
		Map<String, Object> params = new HashMap<String, Object>();
		/*用户相关信息*/
		params.put("userid", userid+"");
		params.put("username", username);
		/*日志信息*/
		params.put("goalid", goalid+"");
		params.put("remark", remark);
		/*代理信息，如果是代理操作的话*/
		params.put("agencyName", agencyName);
		params.put("changeBefore", changeBefore+"");
		params.put("changeAfter", changeAfter+"");
		params.put("agencySiteSizeChange", agencySiteSizeChange+"");
		/*使用的类的信息，来源位置*/
		params.put("class", st.getClassName());
		params.put("method", st.getMethodName());
		
		getLog().add(params);
	}
	
	/**
	 * 消费日志，那肯定就是有代理操作的。管理员操作只是充值
	 * @see #addChangeLog(int, String, String, String, int, int, int, int, String, String)
	 */
	public static void xiaofei(String agencyName, String remark, int agencySiteSizeChange, int changeBefore, int changeAfter, int goalid, String ip){
		//当前登录用户信息
		User user = ShiroFunc.getUser();
		int userid = 0;
		String username = "";
		if(user != null){
			userid = user.getId();
			username = user.getUsername();
		}
		
		addChangeLog(userid, username, agencyName, remark, agencySiteSizeChange, changeBefore, changeAfter, goalid, ip, "xiaofei");
	}
	
	/**
	 * 充值日志，那肯定就是有代理操作的。（管理员操作只能是消费，给代理充值后是消费日志）充值大多都是我(当前登录用户)给对方充值，所以对方是未登录的，手动传入其用户信息
	 * @param agencyName
	 * @param remark
	 * @param agencySiteSizeChange
	 * @param changeBefore
	 * @param changeAgter
	 * @param goalid
	 * @param ip
	 */
	public static void chongzhi(int userid, String username, String agencyName, String remark, int agencySiteSizeChange, int changeBefore, int changeAgter, int goalid, String ip){
		addChangeLog(userid, username, agencyName, remark, agencySiteSizeChange, changeBefore, changeAgter, goalid, ip, "chongzhi");
	}
}
