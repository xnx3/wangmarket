package com.xnx3.wangmarket.admin.pluginManage.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * 网站后台插件
 * @author 管雷鸣
 * @deprecated 已废弃，请使用{@link com.xnx3.wangmarket.pluginManage.PluginRegister}
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PluginRegister {
	
	/**
	 * 该插件的唯一标识。如自定义表单插件，唯一标识便是 formManage 。注意不能与其他插件重名
	 * @return
	 */
	String id();
	
	/**
	 * 在网站管理后台中，功能插件下，显示的菜单项的标题文字
	 * @return
	 */
	String menuTitle() default "";
	
	/**
	 * 在网站管理后台中，功能插件下，显示的菜单项的超链接网址。如：站外的绝对路径，或站内的相对路径  ../../column/popupListForTemplate.do
	 * @return
	 */
	String menuHref() default "";
	
	/**
	 * 是否在CMS模式网站管理后台的功能插件中显示， true：是, 不填默认是false
	 * @return
	 */
	boolean applyToCMS() default false;
	
	/**
	 * 是否在电脑(pc)模式网站管理后台的功能插件中显示， true：是, 不填默认是false
	 * @return
	 */
	boolean applyToPC() default false;
	
	/**
	 * 是否在手机(wap)模式网站管理后台的功能插件中显示， true：是, 不填默认是false
	 * @return
	 */
	boolean applyToWAP() default false;
	
	/**
	 * 是否在代理后台的功能插件中显示， true：是, 不填默认是false
	 * @return
	 */
	boolean applyToAgency() default false;
	
	/**
	 * 是否在总管理后台的功能插件中显示， true：是, 不填默认是false
	 * @return
	 */
	boolean applyToSuperAdmin() default false;
	
	/**
	 * 该插件的简介说明
	 * @return
	 */
	String intro() default "";
	
	/**
	 * 该插件的详情说明的网址，点击后进入这个url查看详细说明
	 * @return
	 */
	String detailUrl() default "";
	
	/**
	 * 当前插件的版本号 ， 如  1.0
	 * @return
	 */
	String version() default "";
	
	/**
	 * 远程版本检测的url地址    
	 * <br/> 其内返回值为 最新版本号|提示有新版本后点击进入的网址|  如： 1.0|http://www.wang.market/wangmarket.html|
	 * @return
	 */
	String versionCheckUrl() default "";
	
	/**
	 * 最低支持的版本, 填写如 4.7  ，v4.7增加
	 */
	String versionMin() default "";
	
}
