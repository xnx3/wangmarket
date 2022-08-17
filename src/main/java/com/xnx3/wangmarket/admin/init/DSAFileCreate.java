package com.xnx3.wangmarket.admin.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * 这个主要用于shell全自动安装创建key使用。mian必须要创建文件并输出key
 * @author 管雷鸣
 */
public class DSAFileCreate {
	
	public static void main(String[] args) {
		String path = new DSAFileCreate().getClass().getResource("/").getPath();
		path = path.replace("WEB-INF/classes/", "");
//		System.out.println("current path : "+path);
		
		String key = "wangmarket is very good ";
		try {
			File file = new File(path);
			String parentPath = file.getParentFile().getParentFile().getPath();
			String dasKeyPath = parentPath+File.separatorChar+"desKey.txt";
			if(!new File(dasKeyPath).exists()) {
				//创建
//				new File(dasKeyPath).createNewFile();
				if(!write(dasKeyPath, getRandom09AZ(16))) {
					//创建失败没有写出成功
					System.err.println("写出文件 "+dasKeyPath +" 失败！可能没有写出权限导致。如果后面有使用DAS加密解密相关出现异常，那可能就是这里的事");
				}
			}
			key = read(dasKeyPath, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		key = key.trim();
		System.out.println(key);
	}
	
	
	public static final char[] AZ09CHAR_36 = {'0','1','2','3','4','5','6','7','8','9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	 /**
     * 生成随机长度的英文+数字（0-9 10个数字、a－z，26个英文字母）
     * @param length 生成字符串的长度
     * @return 字符串
     */
    public static String getRandom09AZ(int length){
    	//字符长度
    	final int  maxNum = 36;
    	int i;  //生成的随机数
    	int count = 0; //生成的密码的长度
    	
    	StringBuffer pwd = new StringBuffer("");
    	Random r = new Random();
    	while(count < length){
    		//生成随机数，取绝对值，防止生成负数，
    		i = Math.abs(r.nextInt(maxNum));  //生成的数最大为26-1
    		if (i >= 0 && i < AZ09CHAR_36.length) {
    			pwd.append(AZ09CHAR_36 [i]);
    			count ++;
    		}
    	}
    	return pwd.toString();
    }
    
    /**
	 * 写文件
	 * @param path 传入要保存至的路径————如D:\\a.txt
	 * @param xnx3_content 传入要保存的内容
	 * @return 成功true；失败false
	 */
	public static boolean write(String path,String xnx3_content){
		try {
			FileWriter fw=new FileWriter(path);
			java.io.PrintWriter pw=new java.io.PrintWriter(fw);
			pw.print(xnx3_content);
			pw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 读文件，返回文件文本信息
	 * @param path 文件路径 C:\xnx3.txt
	 * @param encode 文件编码.如 FileUtil.GBK
	 * @return String 返回的文件文本信息
	 */
	public static String read(String path,String encode){
		StringBuffer xnx3_content=new StringBuffer();
		try{
			File file=new File(path);
			BufferedReader xnx3_reader=new BufferedReader(new InputStreamReader(new FileInputStream(file),encode));
			String date=null;
			while((date=xnx3_reader.readLine())!=null){
				xnx3_content.append(date+"\n");
			}
			xnx3_reader.close();
		}catch (Exception e) {
		}
		
		return xnx3_content.toString();
	}
}
