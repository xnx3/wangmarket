package com.xnx3.j2ee.func;

/**
 * 日志打印
 * @author 管雷鸣
 */
public class Log {
	public static boolean debug = false;	//默认为false，不开启
	public static boolean error = false;	//默认为false，不开启
	public static boolean info = false;	//默认为false，不开启
	
	/**
	 * 增加一条错误的log信息
	 */
	public static void debug(String text){
		if(debug){
			StackTraceElement st = Thread.currentThread().getStackTrace()[2];
			System.out.println(text+" \t "+st.getClassName()+"."+st.getMethodName()+"() "+st.getLineNumber()+" Line");
		}
	}
	
	public static void error(String text){
		if(error){
			StackTraceElement st = Thread.currentThread().getStackTrace()[2];
			System.err.println(text+" \t "+st.getClassName()+"."+st.getMethodName()+"() "+st.getLineNumber()+" Line");
		}
	}
	
	public static void main(String[] args) {
		debug = true;
		debug("测试一下");
	}
	
	public static void log(String text){
		System.out.println(text);
	}
	public static void info(String text){
		if(info){
			StackTraceElement st = Thread.currentThread().getStackTrace()[2];
			System.out.println(text+" \t "+st.getClassName()+"."+st.getMethodName()+"() "+st.getLineNumber()+" Line");
		}
	}
}
