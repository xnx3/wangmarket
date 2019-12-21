package com.xnx3.wangmarket.admin.init;

import java.io.File;
import org.springframework.stereotype.Component;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SystemUtil;

/**
 * 初始化模版文件夹，自动创建模版包上传的临时文件
 * @author 管雷鸣
 *
 */
@Component
public class TemplateTemporaryFolder {
	public static String folderPath;	//存放上传模版zip包的临时文件夹
	public static final int TIMEOUT = 120;	//过期时间120秒
	
	static{
		new TemplateTemporaryFolder();
		
		//定时巡检，删除过时文件
		new Thread(new Runnable() {
			public void run() {
				ConsoleUtil.info("start template temporary folder timing check thread.");
				while (true) {
					try {
						Thread.sleep(TIMEOUT*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					File file = new File(folderPath);
					if(file == null){
						ConsoleUtil.error("folderPath --- null");
					}
					File subFiles[] = file.listFiles();
					if(subFiles != null){
						for (int i = 0; i < subFiles.length; i++) {
							File subFile = subFiles[i];
							if((subFile.lastModified() + (TIMEOUT*1000)) < DateUtil.timeForUnix13()){
								//如果文件创建时间超过2分钟，那么就删除掉这个临时的模版文件夹
								subFile.delete();
								deleteFile(subFile);
							}
						}
					}
					
				}
			}
		}).start();
		
	}
	
	public TemplateTemporaryFolder() {
		if(SystemUtil.getProjectPath().indexOf("/target/classes") > 0){
			//包含这个路径，那么认为是在开发环境中
			folderPath = SystemUtil.getProjectPath()+"templateTemporaryFile/";
		}else{
			//正式运行环境
			folderPath = SystemUtil.getProjectPath()+"WEB-INF/classes/templateTemporaryFile/";
		}
		
		File templateTemporaryFile = new File(folderPath);
		if(!templateTemporaryFile.exists()){
			//如果文件夹不存在，那么自动创建
			templateTemporaryFile.mkdir();
			ConsoleUtil.info("auto create template temporary floder : "+folderPath);
		}
	}
	
	/**
	 * 删除文件
	 * @param file
	 */
	public static void deleteFile(File file){
		if(file.isDirectory()){
			//如果是目录，则便利其下的文件，将其删除掉
			File subFiles[] = file.listFiles();
			for (int i = 0; i < subFiles.length; i++) {
				File subFile = subFiles[i];
				deleteFile(subFile);
			}
		}
		//将当前文件或者文件夹删除掉
		file.delete();
	}
	
}
