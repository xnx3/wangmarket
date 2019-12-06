package com.xnx3.wangmarket.superadmin.util.pluginManage;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 执行windowscmd命令时，开一线程打印错误信息
 * @author 李鑫
 */
public class InputStreamRunnable implements Runnable{
	private BufferedReader bReader = null;
	private String type; // 输出流的类型
	/**
	 * 
	 * @author 李鑫
	 * @param is 运行cmd命令信息的流
	 * @param type 流的类型  例：“ErrorStream”
	 */
	InputStreamRunnable(InputStream is, String type) {
		this.type = type;
		try {
			bReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is)));
		} catch (Exception ex) {
			ex.printStackTrace();	
		}
	}
	 @Override
	 public void run() {
		 String line;
		 int num = 1;
		 try {
			 while ((line = bReader.readLine()) != null) {
				 System.out.println(type + "---->"+String.format("%02d",num++)+" "+line);
			 }
			 bReader.close();
		 } catch (Exception ex) {
			 ex.printStackTrace();
		 }
	 }
}
	