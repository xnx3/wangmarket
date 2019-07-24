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
    	
        com.xnx3.j2ee.func.Log.info("\n"
        		+ "***************************\n"
        		+ "网市场云建站系统已开启完毕！您可打开浏览器，访问 localhost 进行使用\n"
        		+ "***************************\n"
        		+ "  官网： www.wang.market\n"
        		+ "  官网： www.leimingyun.com\n"
        		+ "  网站管理后台使用说明： http://help.wscso.com/5732.html\n"
        		+ "  自行部署文档查阅系统步骤演示： http://help.wscso.com/5744.html\n"
        		+ "  在线开通网站体验网站管理后台： http://wang.market/regByPhone.do?inviteid=50\n"
        		+ "  作者微信：  xnx3com  欢迎各位朋友加微信，交个朋友。  祝您使用愉快！  \n"
        		+ "***************************\n");
    }
    
}
