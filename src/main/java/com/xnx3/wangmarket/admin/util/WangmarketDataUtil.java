package com.xnx3.wangmarket.admin.util;

import java.io.File;

import com.xnx3.FileUtil;
import com.xnx3.j2ee.util.SystemUtil;

/**
 * wangmarket 数据相关的data文件相关
 * @author 管雷鸣
 *
 */
public class WangmarketDataUtil {
	private static String wangmarketDataRootPath = null;
	
	/**
	 * 获取当前 wangmarket_data 文件夹所在的路径
	 * @return 返回如：  /mnt/tomcat/wangmarket_data/
	 */
	public static String getWangmarketDataRootPath() {
		if(wangmarketDataRootPath != null) {
			return wangmarketDataRootPath;
		}
		
		
		String path = SystemUtil.getProjectPath();
		path = path.replace("target/classes/", "wangmarket_data/");
		path = path.replace("webapps/ROOT/", "wangmarket_data/");
		
		//如果第一次用，是没有 wangmarket_data 这个目录的，自动创建这个目录，这个目录位于 tomcat下，跟webapps平级。  实际开发中，这个目录跟 target、src 是平级
		if(!FileUtil.exists(path)){
			File file = new File(path);
			file.mkdir();
		}
		
		return path;
	}
	
}
