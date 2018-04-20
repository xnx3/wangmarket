package com.xnx3.j2ee.service;

import com.xnx3.j2ee.Global;

/**
 * 角色、权限相关
 * @author 管雷鸣
 *
 */
public interface SystemService {
	
	/**
	 * 刷新System数据表在内存的数据。即 {@link Global#get(String)} 、{@link Global#getInt(String)} 取到的值
	 */
	public void refreshSystemCache();
	
}