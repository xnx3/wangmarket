package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * 运行入口
 * @author 管雷鸣
 *
 */
@SpringBootApplication
@ServletComponentScan
public class Application {
	
    public static void main(String[] args) {
    	com.xnx3.j2ee.func.Log.debug = true;
    	com.xnx3.j2ee.func.Log.info = true;
    	com.xnx3.j2ee.func.Log.error = true;
    	SpringApplication.run(Application.class, args);
    	
        com.xnx3.j2ee.func.Log.info("网市场云建站系统已开启！我们官方网站为  http://www.wang.market 帮助说明等，可到我们官网查看。");
    }
    
}
