package com.xnx3.j2ee.system;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.xnx3.j2ee.func.Log;
import com.xnx3.j2ee.system.interceptor.AllInterceptor;

/**
 * 拦截器
 * @author 管雷鸣
 *
 */
@Configuration
public class WebMvcConfigurer_ implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AllInterceptor()).addPathPatterns("/**");
    }

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if(com.xnx3.j2ee.Global.isJarRun){
			//如果是以jar方式运行，则要虚拟路径
			registry.addResourceHandler("/site/**").addResourceLocations("classpath:/site/");
//			registry.addResourceHandler("/site/**").addResourceLocations("src/main/webapp/site/");
			registry.addResourceHandler("/cache/**").addResourceLocations("classpath:/cache/");
			registry.addResourceHandler("/plugin_data/**").addResourceLocations("classpath:/plugin_data/");
			registry.addResourceHandler("/head/**").addResourceLocations("classpath:/head/");
			registry.addResourceHandler("/websiteTemplate/**").addResourceLocations("classpath:/websiteTemplate/");	//v4.7增加
			Log.info("jar包方式运行，配置虚拟路径 /site、   /cache 、 /head");
		}
		
	}

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController( "/" ).setViewName("forward:/index.html" );
		registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
	}
    
    
}