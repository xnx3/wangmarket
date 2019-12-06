package com.xnx3.wangmarket.superadmin.util.pluginManage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 用于tomcat重新启动的类
 * @author 李鑫
 */
public class TomcatUtil implements Runnable{
	
	Map<String, String> environmentMap;
	
	/**
	 * @author 李鑫
	 * @param environmentMap 环境的信息
	 * 	key : runtimeEnvironment 当前运行的环境 。 例： windows linux  mac
	 * 	binPath: 当前运行tomcat的bin目录路径
 	 */
	public TomcatUtil(Map<String, String> environmentMap) {
		this.environmentMap = environmentMap;
	}
	/**
	 * 执行命令行
	 * @author 李鑫
	 * @param command 需要执行的命令行，多条命令行用 && 隔开 
	 * 	例：cd /Users/a/GitHub/wangmarket/target/classes/&&java com.TomcatApplication
	 */
	public static void exeCommand(String command) {
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
	/**
	 * windows关闭tomcat
	 * @author 李鑫
	 * @param binPath tomcat的bin目录
	 * @throws IOException
	 */
	private static void shutDownTomcatForWindows(String binPath) throws IOException {
		// 执行的cmd命令组
		String shutDownCommand = "cmd /c cd /d " + binPath + "&&shutdown.bat";
		/*
		 * 运行命令关闭tomcat
		 */
		Runtime runtime = Runtime.getRuntime();
		Process exec = runtime.exec(shutDownCommand);
		InputStream inputStream = exec.getInputStream();
		InputStreamReader isr = new InputStreamReader(inputStream);//将字节流转化成字符流
		BufferedReader br = new BufferedReader(isr);//将字符流以缓存的形式一行一行输出
		/* 为"错误输出流"单独开一个线程读取之,否则会造成标准输出流的阻塞 */
		Thread t = new Thread(new InputStreamRunnable(exec.getErrorStream(), "ErrorStream"));
		t.start();
		String line = null;
		// 输出一下返回信息
		while((line = br.readLine()) != null) {
			System.out.println(line);
		}
		br.close();
		isr.close();
		inputStream.close();		
	}
	@Override
	public void run() { 
		if(environmentMap.get("runtimeEnvironment").indexOf("indow") != -1) {
			/*
			 * Windows环境下
			 */
			try {
				// 获取bin路径
				String realPath = environmentMap.get("realPath");
				int indexOf = realPath.indexOf("webapps");
				String binPath = realPath.substring(0, indexOf) + "bin";
				// 关闭tomcat服务
				shutDownTomcatForWindows(binPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			/*
			 * linux环境下
			 */
			// tomcat的启动文件和关闭文件的bin路径
			String realPath = environmentMap.get("realPath");
			int indexOf = realPath.indexOf("webapps/");
			String binPath = realPath.substring(0, indexOf) + "bin/";
			createShFileForWLinux(binPath, realPath);
			String command = "cd " + realPath + " && ./restart.sh";
			exeCommand(command);
		}
	}
}
