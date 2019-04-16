package com;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 放到Tomcat中时用
 * @author 管雷鸣
 *
 */
@ServletComponentScan
public class ServletInitializer extends SpringBootServletInitializer {
    
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	com.xnx3.j2ee.func.Log.debug = true;
    	com.xnx3.j2ee.func.Log.info = true;
    	com.xnx3.j2ee.func.Log.error = true;
    	com.xnx3.j2ee.Global.isJarRun = false;	//自行放到tomcat中运行
    	return application.sources(Application.class);
    }
}
