package com.xnx3.j2ee.func;

/**
 * 版本相关
 * @author 管雷鸣
 *
 */
public class VersionUtil {
	
	/**
	 * 将 int 格式的版本号转化为 给人所看的版本 如:
	 * 		<ul>
	 * 			<li>400900100 转化为 4.9.1</li>
	 * 			<li>400900000 转化为 4.9</li>
	 * 			<li>400000100 转化为 4.0</li>
	 *  	</ul>
	 * @return 字符串 如： 4.9.1  、 4.8 、 5.0  
	 */
	public static String intToStr(int version){
		String versionStr = version+"";
		String first = versionStr.substring(0, 3);
		String two = versionStr.substring(3, 6);
		String three = versionStr.substring(6, 9);
		
		first = first.replaceAll("0", "");
		two = two.replaceAll("0","");
		three = three.replaceAll("0","");
		
		String str = first;
		if(two.length() == 0){
			two = "0";
		}
		str = str + "." + two;
		if(three.length() > 0){
			str = str + "." + three;
		}
		return str;
	}
	
	/**
	 * 将 String 格式版本号转化为int格式 ，如:
	 * 		<ul>
	 * 			<li>4.9.1 转化为 400900100</li>
	 * 			<li>4.9 转化为 400900000</li>
	 * 			<li>4.0 转化为 400000100</li>
	 *  	</ul>
	 * @return 字符串 如： 4.9.1  、 4.8 、 5.0  
	 */
	public static int strToInt(String version){
		
		return 0;
	}
	
	public static void main(String[] args) {
		System.out.println(intToStr(400900000));;
	}
}
