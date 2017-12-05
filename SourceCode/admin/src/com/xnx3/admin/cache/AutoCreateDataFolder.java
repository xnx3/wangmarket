package com.xnx3.admin.cache;

import java.io.File;

import org.springframework.stereotype.Component;

import com.xnx3.file.FileUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.admin.G;

/**
 * 自动在/cache/下创建data文件夹，用于缓存js数据
 * @author 管雷鸣
 */
@Component
public class AutoCreateDataFolder {
	
	public AutoCreateDataFolder() {
		//初始化缓存文件夹，若根目录下没有缓存文件夹，自动创建
		if(!FileUtil.exists(Global.getProjectPath()+G.CACHE_FILE)){
			String path = Global.getProjectPath();
			System.out.println("create -- data --"+new File(path+G.CACHE_FILE).mkdir());
		}
	}
	
}
