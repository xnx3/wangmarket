package com.xnx3.j2ee.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

import com.xnx3.j2ee.bean.ActiveUser;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.shiro.ShiroFunc;

import net.sf.json.JSONObject;

/**
 * session 操作
 * @author 管雷鸣
 *
 */
public class SessionUtil extends ShiroFunc{
	//是否允许百度UEDITOR上传图片、文件。 true允许，false不允许
	public static final String PLUGIN_NAME_UEDITOR_ALLOW_UPLOAD = "system_ueditor_allowUploadForUEditor";
	//UEditor上传文件相关的参数，可用{uploadParam1}来调用。每个人的上传参数都会不同
	public static final String PLUGIN_NAME_UEDITOR_UPLOAD_PARAM_1 = "system_ueditor_ueUploadParam1";
	//当前使用哪个语言包
	public static final String PLUGIN_NAME_LANGUAGE_PACKAGE_NAME = "system_languagePackageName";
	
	/**
	 * 从Shrio的Session中获取当前用户的缓存信息
	 */
//	public static UserBean getUserBeanForSession(){
//		return getUserBeanForShiroSession();
//	}
	//如果使用redis，那么将每个用户的 activeUser.getPluginMap() 作为value存入到redis的key，如 plugin_map_userid_1
	public static final String REDIS_PLUGIN_MAP_NAME = "com.xnx3.j2ee.bean.ActiveUser.plugin.userid";
	public static final int REDIS_PLUGIN_MAP_EXPIRETIME = 2*60*60*24;	//pluginMap 在redis中存在的有效期是2天
	
	/**
	 * 获取当前用户是否已经登录
	 * @return true:已登录；  false:未登录
	 */
	public static boolean isLogin(){
		Subject subject = SecurityUtils.getSubject();
		//取身份信息
		ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
		return activeUser != null;
	}

	/**
	 * 从Shrio的Session中获取当前用户的缓存信息
	 */
	public static ActiveUser getActiveUser(){
		if(SecurityUtils.getSubject() == null){
			return null;
		}else{
			Subject subject = SecurityUtils.getSubject();
			//取身份信息
			ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
			if(activeUser != null){
				return activeUser;
			}else{
				return null;
			}
		}
	}
	
	/**
	 * 获取用户Session中缓存的某个插件信息。这里的信息，可以用 {@link #setPlugin(String, Object)} 来设置写入
	 * 这里是从 {@link ActiveUser#getPluginMap()} 中取的
	 * 可以从session中获取该用户某个插件的缓存信息，避免频繁查数据库。
	 * 前提是已经将该用户的插件的信息缓存进去了
	 * @param key 整个网市场云建站系统中，pluginMap.key,唯一的，具体插件可以为 kefu、cnzz， 网站管理后台缓存网站对象的key可以为 wangmarket_site
	 * @return 如果获取到，返回。如果获取不到，如用户未登录、插件信息不存在，则返回null
	 */
	public static <T> T getPlugin(String key){
		ActiveUser activeUser = getActiveUser();
		if(activeUser == null){
			return null;
		}
		
		Object obj = null;
		User user = activeUser.getUser();
		if(user == null){
			//未登陆
			return null;
		}
		//判断是从redis中取，还是从本地服务器中取
		if(RedisUtil.isUse()){
			Object redisObj = RedisUtil.getObject(REDIS_PLUGIN_MAP_NAME+user.getId());
			if(redisObj == null){
				return null;
			}
			Map<String, Object> map = (HashMap<String, Object>) redisObj;
			obj = map.get(key);
		}else{
			obj = activeUser.getPluginMap().get(key);
		}
		
		if(obj == null){
			return null;
		}
		return (T)obj;
	}
	
	/**
	 * 获取用户Session中缓存的某个插件信息。这里的信息，可以用 {@link #setPlugin(String, Object)} 来设置写入
	 * 这里是从 {@link ActiveUser#getPluginMap()} 中取的
	 * 可以从session中获取该用户某个插件的缓存信息，避免频繁查数据库。
	 * 前提是已经将该用户的插件的信息缓存进去了
	 * @param key 整个网市场云建站系统中，pluginMap.key,唯一的，具体插件可以为 kefu、cnzz， 网站管理后台缓存网站对象的key可以为 wangmarket_site
	 * @return 如果获取到，返回。如果获取不到，如用户未登录、插件信息不存在，则返回null
	 */
	public static Object getPluginObject(String key){
		ActiveUser activeUser = getActiveUser();
		if(activeUser == null){
			return null;
		}
		Object obj = null;
		User user = activeUser.getUser();
		if(user == null){
			//未登陆
			return null;
		}
		
		//判断是从redis中取，还是从本地服务器中取
		if(RedisUtil.isUse()){
			Object redisObj = RedisUtil.getObject(REDIS_PLUGIN_MAP_NAME+user.getId());
			if(redisObj == null){
				return null;
			}
			Map<String, Object> map = (HashMap<String, Object>) redisObj;
			obj = map.get(key);
		}else{
			obj = activeUser.getPluginMap().get(key);
		}
		
		if(obj == null){
			return null;
		}
		return obj;
	}
	
	
	/**
	 * 设置用户Session中缓存的某个插件信息。这里设置的，可以用 {@link #getPlugin(String)} 取出来
	 * @param key 唯一key
	 * @param value 存储的信息对象
	 */
	public static void setPlugin(String key, Object value){
		ActiveUser activeUser = getActiveUser();
		if(activeUser == null){
			return;
		}
		User user = activeUser.getUser();
		if(user == null){
			//未登陆
			return;
		}
		//判断是存到redis中，还是本地服务器中
		if(RedisUtil.isUse()){
			Map map = null;
			Object obj = RedisUtil.getObject(REDIS_PLUGIN_MAP_NAME+user.getId());
			if(obj == null){
				map = new HashMap<String, Object>();
			}else{
				map = (HashMap<String, Object>) obj;
			}
			map.put(key, value);
			RedisUtil.setObject(REDIS_PLUGIN_MAP_NAME+user.getId(), map, REDIS_PLUGIN_MAP_EXPIRETIME);
		}else{
			activeUser.getPluginMap().put(key, value);
		}
	}
	
	/**
	 * 是否允许用户通过富文本编辑器(UEditor)上传文件、图片
	 * @return true：允许上传；  false：不允许上传。 如果之前没设置过，默认是能正常上传的。
	 */
	public static boolean isAllowUploadForUEditor(){
		Object obj = getPluginObject(PLUGIN_NAME_UEDITOR_ALLOW_UPLOAD);
		if(obj == null){
			return true;
		}
		return (boolean) obj;
	}
	
	/**
	 * 是否允许用户通过富文本编辑器(UEditor)上传文件、图片
	 * @param isAllow true：允许上传；  false：不允许上传。
	 */
	public static void setAllowUploadForUEditor(boolean isAllow){
		setPlugin(PLUGIN_NAME_UEDITOR_ALLOW_UPLOAD, isAllow);
	}
	
	/**
	 * 获取UEditor上传文件相关的参数，可用{uploadParam1}来调用。每个人的上传参数都会不同
	 * @return 字符串 如果之前没设置过，则默认获取到 “defaultparam1”
	 */
	public static String getUeUploadParam1(){
		String param = getPlugin(PLUGIN_NAME_UEDITOR_UPLOAD_PARAM_1);
		if(param == null){
			return "defaultparam1";
		}
		return param;
	}
	/**
	 * 设置UEditor上传文件相关的参数，可用{uploadParam1}来调用。每个人的上传参数都会不同
	 * @param param 字符串,路径，也就是文件夹名字
	 */
	public static void setUeUploadParam1(String param){
		setPlugin(PLUGIN_NAME_UEDITOR_UPLOAD_PARAM_1, param);
	}
	
	/**
	 * 获取当前使用的是哪个语言包
	 * @return 语言包的名字
	 */
	public static String getLanguagePackageName(){
		return getPlugin(PLUGIN_NAME_LANGUAGE_PACKAGE_NAME);
	}
	/**
	 * 设置当前使用的是哪个语言包
	 * @param name 语言包的名字
	 */
	public static void setLanguagePackageName(String name){
		setPlugin(PLUGIN_NAME_LANGUAGE_PACKAGE_NAME, name);
	}
	
	/**
	 * 推出登录
	 */
	public static void logout(){
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			int userid = getUserId();
			if(userid > 0){
//				RedisUtil.delkeyObject(key);
			}
			subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
		}
	}
	

	/**
	 * Session中更新当前登录用户的信息。当然，只有用户登录了，才能更新
	 */
	public static void setUser(User user){
		ActiveUser activeUser = SessionUtil.getActiveUser();
		if(activeUser != null){
			activeUser.setUser(user);
		}else{
			return;
		}
	}
	
	/**
	 * 清理掉当前登录用户的session，也就是根据某个用户id，删除这个用户的session，让这个用户下线
	 * @param userid 对应User.id 要删除哪个用户的登录状态，就传入哪个用户的id
	 */
	public static void removeSession(int userid){
		//获取shiro的sessionManager
		DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
	    DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
	    // 4.获取所有已登录用户的session列表
	    Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
	 
	    if (sessions.size() > 0) {
	        for(Session onlineSession:sessions){
	            // 清除当前用户以前登录时保存的session会话
	        	
	        	SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) onlineSession.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
	        	if(principalCollection == null){
	        		continue;
	        	}
                Object obj = principalCollection.getPrimaryPrincipal();
                if(obj == null){
                	continue;
                }
        		ActiveUser activeUser = (ActiveUser)obj;
        		User user= activeUser.getUser();
        		if(user == null){
        			continue;
        		}
    			if(user.getId() - userid == 0){
    				sessionManager.getSessionDAO().delete(onlineSession);
    			}
	        }
	    }
	}
}
