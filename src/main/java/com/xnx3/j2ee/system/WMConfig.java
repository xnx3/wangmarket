package com.xnx3.j2ee.system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import com.xnx3.j2ee.service.*;
import com.xnx3.j2ee.service.impl.*;
import com.xnx3.j2ee.util.ConsoleUtil;

/**
 * WM 的配置，service、dao等
 * @author 管雷鸣
 * com.xnx3.j2ee.dao.SqlDAO,
 */
@Configuration
@Order(11)
public class WMConfig {
	
	public WMConfig() {
		ConsoleUtil.info("Spring Scan : WMConfig");
	}
	
	@Bean
	public ApiService apiService(){
		return new ApiServiceImpl();
	}
	@Bean
	public RoleService roleService(){
		return new RoleServiceImpl();
	}
	@Bean
	public SmsService smsService(){
		return new SmsServiceImpl();
	}
	@Bean
	public SqlCacheService sqlCacheService(){
		return new SqlCacheServiceImpl();
	}
	@Bean
	public SqlService sqlService(){
		return new SqlServiceImpl();
	}
	@Bean
	public SystemService systemService(){
		return new SystemServiceImpl();
	}
	@Bean
	public UserService userService(){
		return new UserServiceImpl();
	}
	
}
