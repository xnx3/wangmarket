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
 *
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
	
}
