package com.xnx3.wangmarket.admin.util;

import java.util.Map;
import com.xnx3.wangmarket.admin.entity.InputModel;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.entity.SiteUser;
import com.xnx3.wangmarket.admin.vo.TemplateVarVO;

/**
 * 网市场后台相关的sessison
 * @author 管雷鸣
 *
 */
public class SessionUtil extends com.xnx3.j2ee.util.SessionUtil{
	//缓存用户自己站点的信息， session中的ActiveUser的 pluginMap.key
	public static final String PLUGIN_NAME_SITE = "wangmarket_site";	
	//用户网站的模版变量，可能是已被编译(替换)过标签的内容了。key:template.name	value:模版变量的内容
	public static final String PLUGIN_NAME_TEMPLATEVAR_COMPILE_DATA_MAP = "wangmarket_templateVarCompileDataMap";
	//原始的模版变量，其内包含模版变量的数据库中的原始内容. key:templateVar.name
	public static final String PLUGIN_NAME_TEMPLATEVAR_ORIGINAL_MAP = "wangmarket_templateVarMapForOriginal";
	//缓存的当前用户的栏目信息 key:siteColumn.id 登录时不会缓存此处，在使用时才会缓存
	public static final String PLUGIN_NAME_SITECOLUMN_MAP = "wangmarket_siteColumnMap";
	//当前CMS网站的输入模型，由 inputModelService 初始化赋予值。在用户进入内容管理，编辑时才会判断，如果此为null，才会从数据库加载数据
	public static final String PLUGIN_NAME_INPUTMODEL_MAP = "wangmarket_inputModelMap";
	//网站管理后台的左侧菜单使用权限，只限网站用户有效。key: id，也就是左侧菜单的唯一id标示，比如模版管理是template，生成整站是 shengchengzhengzhan， 至于value，无意义，1即可
	public static final String PLUGIN_NAME_SITE_MENU_ROLE = "wangmarket_siteMenuRole";
	//site_user 表的信息
	public static final String PLUGIN_NAME_SITE_USER = "wangmarket_site_user";
	
	/**
	 * 获取当前用户登陆的站点信息。若是不存在，则返回null
	 * @return
	 */
	public static Site getSite(){
		return getPlugin(PLUGIN_NAME_SITE);
	}
	
	/**
	 * 设置当前登录的站点信息
	 * @param site {@link Site}当前登录用户所管理的站点信息
	 */
	public static void setSite(Site site){
		setPlugin(PLUGIN_NAME_SITE, site);
	}
	
	/**
	 * 获取我当前使用的模版变量，可能是已被编译(替换)过标签的内容了。
	 * @return map <ul>
	 * 					<li>key:template.name</li>
	 * 					<li>value:模版变量的内容</li>
	 * 				</ul>
	 */
	public static Map<String, String> getTemplateVarCompileDataMap(){
		return getPlugin(PLUGIN_NAME_TEMPLATEVAR_COMPILE_DATA_MAP);
	}
	
	/**
	 * 设置我当前使用的模版变量，可能是已被编译(替换)过标签的内容了。
	 * @param map <ul>
	 * 					<li>key:template.name</li>
	 * 					<li>value:模版变量的内容</li>
	 * 				</ul>
	 */
	public static void setTemplateVarCompileDataMap(Map<String, String> map){
		setPlugin(PLUGIN_NAME_TEMPLATEVAR_COMPILE_DATA_MAP, map);
	}
	
	/**
	 * 获取原始的模版变量，其内包含模版变量的数据库中的原始内容.
	 * @return map key:templateVar.name
	 */
	public static Map<String, TemplateVarVO> getTemplateVarMapForOriginal(){
		return getPlugin(PLUGIN_NAME_TEMPLATEVAR_ORIGINAL_MAP);
	}
	
	/**
	 * 设置 获取原始的模版变量，其内包含模版变量的数据库中的原始内容.
	 * @param map key:templateVar.name
	 */
	public static void setTemplateVarMapForOriginal(Map<String, TemplateVarVO> map){
		setPlugin(PLUGIN_NAME_TEMPLATEVAR_ORIGINAL_MAP, map);
	}
	
	/**
	 * 获取缓存的当前用户的栏目信息 登录时不会缓存此处，在使用时才会缓存
	 * @return map.key:siteColumn.id 
	 */
	public static Map<Integer, SiteColumn> getSiteColumnMap(){
		return getPlugin(PLUGIN_NAME_SITECOLUMN_MAP);
	}
	
	/**
	 * 设置缓存的当前用户的栏目信息 登录时不会缓存此处，在使用时才会缓存
	 * @param map key:siteColumn.id 
	 */
	public static void setSiteColumnMap(Map<Integer, SiteColumn> map){
		setPlugin(PLUGIN_NAME_SITECOLUMN_MAP, map);
	}
	
	/**
	 * 获取当前CMS网站的输入模型，由 inputModelService 初始化赋予值。在用户进入内容管理，编辑时才会判断，如果此为null，才会从数据库加载数据
	 * @return 
	 */
	public static Map<Integer, InputModel> getInputModel(){
		return getPlugin(PLUGIN_NAME_INPUTMODEL_MAP);
	}
	
	/**
	 * 获取当前CMS网站的输入模型，由 inputModelService 初始化赋予值。在用户进入内容管理，编辑时才会判断，如果此为null，才会从数据库加载数据
	 * @param map 
	 */
	public static void setInputModel(Map<Integer, InputModel> map){
		setPlugin(PLUGIN_NAME_INPUTMODEL_MAP, map);
	}
	
	/**
	 * 网站管理后台的左侧菜单使用权限，只限网站用户有效。
	 * @return key: id，也就是左侧菜单的唯一id标示，比如模版管理是template，生成整站是 shengchengzhengzhan， 至于value，无意义，1即可
	 */
	public static Map<String, String> getSiteMenuRole(){
		return getPlugin(PLUGIN_NAME_SITE_MENU_ROLE);
	}
	
	/**
	 * 网站管理后台的左侧菜单使用权限，只限网站用户有效。
	 * @param map key: id，也就是左侧菜单的唯一id标示，比如模版管理是template，生成整站是 shengchengzhengzhan， 至于value，无意义，1即可
	 */
	public static void setSiteMenuRole(Map<String, String> map){
		setPlugin(PLUGIN_NAME_SITE_MENU_ROLE, map);
	}
	
	
	
	/**
	 * 获取当前用户登陆的站点信息。若是不存在，则返回null
	 * @return
	 */
	public static SiteUser getSiteUser(){
		return getPlugin(PLUGIN_NAME_SITE_USER);
	}
	
	/**
	 * 设置当前登录的站点信息
	 * @param site {@link Site}当前登录用户所管理的站点信息
	 */
	public static void setSiteUser(SiteUser siteUser){
		setPlugin(PLUGIN_NAME_SITE_USER, siteUser);
	}
	
}

