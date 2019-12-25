package com.xnx3.j2ee.pluginManage;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * 插件注册的注解，将插件注册进管理后台中
 * @author 管雷鸣
 * @since 5.0
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PluginRegister {
	
	/**
	 * 该插件的唯一标识。如自定义表单插件，唯一标识便是 formManage 。注意不能与其他插件重名
	 * <br/>如果不设置，会自动取 com.xnx3.wangmarket.plugin.cnzz.xxxxx  中的cnzz这个位置的值。
	 * <br/>建议使用默认，不需要设置
	 * @deprecated
	 * @return
	 */
	String id() default "";
	
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
