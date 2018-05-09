package com.xnx3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 运行入口
 * @author 管雷鸣
 *
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
    	com.xnx3.j2ee.func.Log.debug = true;
    	com.xnx3.j2ee.func.Log.info = true;
    	com.xnx3.j2ee.func.Log.error = true;
        SpringApplication.run(Application.class, args);
        com.xnx3.j2ee.func.Log.info("网市场云建站系统已开启！");
    }
    
}
