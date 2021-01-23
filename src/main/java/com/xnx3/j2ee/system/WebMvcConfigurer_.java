package com.xnx3.j2ee.system;

import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.xnx3.j2ee.pluginManage.interfaces.manage.SpringMVCInterceptorPluginManage;
import com.xnx3.j2ee.util.ConsoleUtil;

/**
 * WebMvcConfigurer
 * @author 管雷鸣
 *
 */
@Configuration
public class WebMvcConfigurer_ implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        for (int i = 0; i < SpringMVCInterceptorPluginManage.handlerInterceptorList.size(); i++) {
        	Map<String, Object> map = SpringMVCInterceptorPluginManage.handlerInterceptorList.get(i);
        	HandlerInterceptor handler = (HandlerInterceptor) map.get("class");
        	List<String> pathPatterns = (List<String>) map.get("pathPatterns");
        	registry.addInterceptor(handler).addPathPatterns(pathPatterns);
		}
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
			ConsoleUtil.info("jar包方式运行，配置虚拟路径 /site、   /cache 、 /head");
		}
		
	}

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController( "/" ).setViewName("forward:/index.html" );
		registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
	}
    
    
}