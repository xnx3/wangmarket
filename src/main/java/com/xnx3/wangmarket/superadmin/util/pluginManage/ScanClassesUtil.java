package com.xnx3.wangmarket.superadmin.util.pluginManage;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.xnx3.ScanClassUtil;


/**
 * 对指定文件夹下的类扫描的工具类
 * @author 李鑫
 */
public class ScanClassesUtil {
	
	/**
	 * 扫描有指定名称的注解的class类
	 * @author 李鑫
	 * @param list 需要扫描的class列表
	 * @param annotations 需要扫描的注解
	 * @return 扫描结果的Class类型列表
	 */
	public static List<Class<?>>  getClassSearchAnnotationsName(List<Class<?>> list, List<String> annotations) {
		// 用于保存结果的list
		List<Class<?>> classList = new LinkedList<Class<?>>();
		// 循环遍历注解列表
		Iterator<String> iterator = annotations.iterator();
		String annotation = null;
		List<Class<?>> sreachList = null;
		while (iterator.hasNext()) {
			annotation = iterator.next();
			// 对当前遍历的注解进行扫描
			sreachList = ScanClassUtil.getClassSearchAnnotationsName(list, annotation);
			// 对扫描结果进行保存
			classList.addAll(sreachList);
		}
		return classList;
	}
	
	/**
	 * 扫描多个文件夹下的class类
	 * @author 李鑫
	 * @param fileList 需要扫面的文件夹列表
	 * @param parentPackageName 文件夹代表包的父包 例：扫描com.scan包、参数则为 com
	 * @return
	 */
	public static List<Class<?>> getPluginClassList(List<File> fileList,String parentPackageName) {
		List<Class<?>> classList = new LinkedList<Class<?>>();
		// 循环遍历文件夹
		Iterator<File> iterator = fileList.iterator();
		String scanPackage = null;
		while (iterator.hasNext()) {
			File file = iterator.next();
			// 拼装进行扫面的包名
			scanPackage = parentPackageName + "." + file.getName();
			// 扫描class类型
			List<Class<?>> classes = ScanClassUtil.getClasses(scanPackage);
			// 保存扫描结果
			classList.addAll(classes);
		}
		return classList;
		
	}
	
	/**
	 * 得到指定类的上注解的集合
	 * @author 李鑫
	 * @param clazz 需要查找的类型的class
	 * @param annotationClass 寻找的注解的class
	 * @return 扫描的注解集合
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings({"rawtypes" })
	public static Annotation[] getAnnotation4Class(Class<?> clazz, Class annotationClass) throws ClassNotFoundException {
		return clazz.getAnnotations();
	}
	
	/**
	 * 判断该方法是否有指定的注解
	 * @author 李鑫
	 * @param <T>
	 * @param method 需要检索的方法
	 * @param annotationClass 需要检索的注解的Class
	 * @return true：存在； false：不存在。
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean getAnnotationExist4Method(Method method, Class annotationClass) {
		// 在方法上寻找注解
		Annotation[] annotationsByType = method.getAnnotationsByType(annotationClass);
		// 如果是没有数值 返回 false ， 否则返回 true
		if(annotationsByType.length == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获取指定类方法上有指定注解集合的方法
	 * @author 李鑫
	 * @param clazz 需要检索的类Class
	 * @param annotationClass 需要检索的注解的class
	 * @return 具有指定注解的方法集合
	 */
	@SuppressWarnings({ "rawtypes"})
	public static List<Method> getMethodsByAnnotationExist(Class<?> clazz, Class annotationClass) {
		List<Method> methodList = new LinkedList<Method>();
		// 获取类中的所有方法集合,将集合转为List
		List<Method> asList = Arrays.asList(clazz.getMethods());
		// 遍历List
		Iterator<Method> iterator = asList.iterator();
		while (iterator.hasNext()) {
			Method method = (Method) iterator.next();
			// 如果方法上有该注解，加入list中
			if(getAnnotationExist4Method(method, annotationClass)) {
				methodList.add(method);
			}
		}
		return methodList;
	}
}
