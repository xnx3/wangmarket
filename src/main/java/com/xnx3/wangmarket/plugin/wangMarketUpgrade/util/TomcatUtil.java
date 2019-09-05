package com.xnx3.wangmarket.plugin.wangMarketUpgrade.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * 用于tomcat重新启动的类
 * @author 李鑫
 */
public class TomcatUtil implements Runnable{
	
	/**
	 * 项目的根目录
	 */
	String rootPath;
	
	/**
	 * @author 李鑫
	 * @param rootPath 当前运行tomcat的bin目录路径
 	 */
	public TomcatUtil(String rootPath) {
		this.rootPath = rootPath;
	}
	/**
	 * 执行命令行
	 * @author 李鑫
	 * @param command 需要执行的命令行，多条命令行用 && 隔开 
	 * 	例：cd /Users/a/GitHub/wangmarket/target/classes/&&java com.TomcatApplication
	 */
	public static List<String> exeCommand(String command) {
		Process process = null; 
		List<String> processList = new ArrayList<String>(); 
		try {  
			process = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", command});  
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));  
			String line = "";  
			while ((line = input.readLine()) != null) {
				processList.add(line); 
			}  
			input.close();
		} catch (IOException e) {
			e.printStackTrace();  
		}
		return processList;
	}
	
	
	/**
	 * 创建linux环境下tomcat重新启动的文件
	 * @author 李鑫
	 * @param binPath tomcat的bin目录 例：/mnt/tomcat/bin/
	 * @param realPath 当前项目的真实路径 例：/mnt/tomcat/webapps/ROOT/
	 * @return 创建文件的绝对路径
	 */
	private String createShFileForWLinux(String binPath, String realPath) {
		// 写入文件中的内容
		String shellString = "#!/bin/bash\n";
		shellString += binPath + "shutdown.sh\n";
		shellString += "sleep 2s\n";
		shellString += binPath + "startup.sh\n";
		File file = new File(realPath + "restart.sh"); 
		
		FileOutputStream fileOutputStream = null;
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			fileOutputStream = new FileOutputStream(file);
			byte[] bytes = shellString.getBytes();
			fileOutputStream.write(bytes, 0, bytes.length);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 赋予文件权限
		String command = "chmod 777 " + file.getAbsolutePath();
		exeCommand(command);
		return file.getAbsolutePath();
		
	}
	
	@Override
	public void run() {
		// tomcat的启动文件和关闭文件的bin路径
		int indexOf = rootPath.indexOf("webapps/");
		String binPath = rootPath.substring(0, indexOf) + "bin/";
		createShFileForWLinux(binPath, rootPath);
		String command = "cd " + rootPath + " && ./restart.sh";
		exeCommand(command);
	}
}
