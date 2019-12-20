package com.xnx3.j2ee.util;

import com.xnx3.Lang;

/**
 * 版本相关
 * @author 管雷鸣
 *
 */
public class VersionUtil {
	
	/**
	 * 将 int 格式的版本号转化为 给人所看的版本，
	 * 		<br/>大版本：直接写上
	 * 		<br/>小版本：三位数，如 001
	 * 		<br/>bug版本：三位数，如 001
	 *  如:
	 * 		<ul>
	 * 			<li> 4 009 001 转化为 4.9.1</li>
	 * 			<li> 4 009 000 转化为 4.9</li>
	 * 			<li> 4 000 000 转化为 4.0</li>
	 * 			<li>11 009 021 转化为 11.9.21</li>
	 *  	</ul>
	 * @return 字符串 如： 4.9.1  、 4.8 、 5.0  
	 */
	public static String intToStr(int version){
		String versionStr = version+"";
		if(versionStr.length() < 9){
			for (int i = versionStr.length(); i < 9 ; i++) {
				versionStr = "0"+ versionStr;
			}
		}
		
		String first = versionStr.substring(0, 3);
		String two = versionStr.substring(3, 6);
		String three = versionStr.substring(6, 9);
		
		int firstInt = Lang.stringToInt(first, 1);
		int twoInt = Lang.stringToInt(two, 0);
		int threeInt = Lang.stringToInt(three, 0);
		
		String str = firstInt+"";
		if(twoInt == 0){
			str = str + ".0";
		}else{
			str = str + "."+twoInt;
		}
		if(threeInt > 0){
			str = str + "." + threeInt;
		}
		return str;
	}
	
	/**
	 * 将 String 格式版本号转化为int格式 ，如:
	 * 		<ul>
	 * 			<li>4.9.1 转化为  4 009 001</li>
	 * 			<li>4.9 转化为 	 4 009 000</li>
	 * 			<li>4.0 转化为 	 4 000 000</li>
	 * 			<li>42.1 转化为 	42 001 000</li>
	 *  	</ul>
	 * @return int 。如果转换失败，则返回 0
	 */
	public static int strToInt(String version){
		if(version == null){
			return 0;
		}
		if (version.split("\\.").length == 2) {
			//如： 4.8  那么就补上 .0 变为 4.8.0
			version = version + ".0";
		}
		String[] vers = version.split("\\.");
		if(vers.length != 3){
			//异常，不应该存在的
			return 0;
		}
		
		int firstVersion = Lang.stringToInt(vers[0], 0);
		int twoVersion = Lang.stringToInt(vers[1], 0);
		int threeVersion = Lang.stringToInt(vers[2], 0);
		
		return firstVersion*1000000 + twoVersion*1000 + threeVersion;
	}
	
	public static void main(String[] args) {
		System.out.println(strToInt("4.8"));;
	}
}
