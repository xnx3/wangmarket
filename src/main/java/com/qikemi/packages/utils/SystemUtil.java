package com.qikemi.packages.utils;

import java.util.Properties;

import com.xnx3.j2ee.util.ConsoleUtil;

/**
 * System Utils
 * 
 * @create date : 2014年10月28日 上午9:13:10
 * @Author XieXianbin<a.b@hotmail.com>
 * @Source Repositories Address: <https://github.com/qikemi/UEditor-for-aliyun-OSS>
 */
public class SystemUtil {

	// 系统相关路径
	private static String rootPath = null;
	private static String classesPath = null;
	private static String projectName = null;

	/**
	 * 获取系统编译文件的路径
	 * 
	 * @return
	 */
	public static String getProjectClassesPath() {
		if (classesPath == null) {
			classesPath = SystemUtil.class.getClassLoader().getResource("")
					.getPath().trim();
			if (!isLinux()) {
				classesPath = classesPath.replaceFirst("/", "");
			}
		}
		return classesPath;
	}

	// 系统类型
	private static String osName = null;

	/**
	 * GET Project Root Path
	 * 
	 * @return /var/opt/tomcat/../project_name/
	 */
	public static String getProjectRootPath() {
		if (rootPath == null) {
			String classesPath = getProjectClassesPath();
			// java
			if (classesPath.endsWith("build/classes/")) {
				rootPath = classesPath.replace("build/classes/", "");
			} else if (classesPath.endsWith("WEB-INF/classes/")) {
				// java web
				rootPath = classesPath.replace("WEB-INF/classes/", "");
			}else{
				rootPath = classesPath;
			}
		}
		return rootPath;
	}

	/**
	 * 获取项目名称
	 * 
	 * @return project_name
	 */
	public static String getProjectName() {
		if (projectName == null) {
			String classesPath = getProjectClassesPath();
			// java
			String rootPath = "";
			if (classesPath.endsWith("build/classes/")) {
				rootPath = classesPath.replace("build/classes/", "");
			} else if (classesPath.endsWith("WEB-INF/classes/")) {
				// java web
				rootPath = classesPath.replace("WEB-INF/classes/", "");
			}
			rootPath += "__";
			rootPath = rootPath.replace("/__", "");
			rootPath = rootPath.replaceAll("/", "/__");
			int index = rootPath.lastIndexOf("/__");
			if (index == -1) {
				return "";
			}
			projectName = rootPath.substring(index + 3);
			ConsoleUtil.debug("ueditor projectName : "+ projectName);
			if(projectName.equals("ROOT")){
				//如果是ROOT，那么就为空，ROOT是tomcat默认的,是无需带路径的
				projectName = "";
				ConsoleUtil.debug("Tomcat Ignore the ROOT path ");
			}
		}
		return projectName;
	}

	/**
	 * 获取系统的类型
	 * 
	 * @return
	 */
	public static String getOsName() {
		if (osName == null) {
			Properties prop = System.getProperties();
			osName = prop.getProperty("os.name");
		}
		return osName;
	}

	/**
	 * 判断系统是否为Linux
	 * 
	 * @return true：linux false: win
	 */
	public static boolean isLinux() {
		if (getOsName().startsWith("win") || getOsName().startsWith("Win")) {
			return false;
		}
		return true;
	}

	// public static void main(String[] args) {
	// System.out.println(getProjectName());
	// }
}