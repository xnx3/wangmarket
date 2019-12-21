package com.xnx3.j2ee.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.impl.SqlServiceImpl;

/**
 * Spring相关，如获取spring中的bean
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    /**
     * 上下文对象实例
     */
    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean。
     * <br/>使用示例：
     * 	<pre>
     * 		SqlService sqlService = SpringContextUtils.getBean(SqlServiceImpl.class);
     * 	</pre>
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
    

	/**
	 * 获取数据库的操作 {@link SqlService}
	 */
	public static SqlService getSqlService(){
		return getBean(SqlServiceImpl.class);
	}
	
}