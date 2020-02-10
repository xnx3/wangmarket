package com.xnx3.j2ee.util;

import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

/**
 * 读取 application.properties 属性，可以直接使用 ApplicationProperties.getProperty("key"); 进行调用
 * @author 管雷鸣
 *
 */
@Component
public class ApplicationPropertiesUtil {
	private static Properties properties;	//application.properties
	
	static{
		new ApplicationPropertiesUtil();
	}
	public ApplicationPropertiesUtil() {
		if(properties == null){
			try {
	            Resource resource = new ClassPathResource("/application.properties");//
	            properties = PropertiesLoaderUtils.loadProperties(resource);
	            System.out.println("---"+properties);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
    
	/**
	 * 获取 application.properties 的配置属性
	 * @param key 要获取的配置的名字，如 database.name
	 * @return 获取的配置的值。需要判断是否为null
	 */
    public static String getProperty(String key){
    	if(properties == null){
    		return null;
    	}
    	return properties.getProperty(key);
    }
}
