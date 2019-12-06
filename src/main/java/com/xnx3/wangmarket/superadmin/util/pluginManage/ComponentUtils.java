package com.xnx3.wangmarket.superadmin.util.pluginManage;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 对SpringIOC容器中的组件进行操作的工具类
 * @author 李鑫
 */
public class ComponentUtils {
	
	/**
	 * 在SpringIOC容器中移除组件
	 * @author 李鑫
	 * @param className 需要移除的类的全限定名
	 * @param compomentName 组件在容器中的id
	 * @param applicationContext 当前环境下的ApplicationContext
	 * @throws ClassNotFoundException
	 */
	public static void removeBean(String className, String compomentName, ApplicationContext applicationContext) throws ClassNotFoundException {
		// 获取需要移除组件的class类型
		Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(className);
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(loadClass);
		// 移除组件
		removeBean(compomentName, beanDefinitionBuilder.getRawBeanDefinition(), applicationContext);
	}

	/**
	 * 在IOC容器中注册新的组件
	 * @author 李鑫
	 * @param compomentName 组件在容器中的id
	 * @param beanDefinition 暂时不知道干嘛的，网上复制的
	 * @param applicationContext 当前环境下的ApplicationContext
	 */
	public static void registerBean(String compomentName, AbstractBeanDefinition beanDefinition,
			ApplicationContext applicationContext) {
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
		BeanDefinitionRegistry beanDefinitonRegistry = (BeanDefinitionRegistry) configurableApplicationContext
				.getBeanFactory();
		beanDefinitonRegistry.registerBeanDefinition(compomentName, beanDefinition);
	}
	
	
	/**
	 * 向SpringIOC容器中注册组件
	 * @author 李鑫
	 * @param className 需要注册的类的全限定名
	 * @param compomentName 组件在容器中的id
	 * @param propertyMap 组件的属性值 key：属性名；value：属性值
	 * @param applicationContext  当前环境下的ApplicationContext
	 * @throws ClassNotFoundException
	 */
	public static void addBean(String className, String compomentName, Map<Object, Object> propertyMap,
		ApplicationContext applicationContext) throws ClassNotFoundException {
		// 获取需要注册组件的class类型
		Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(className);
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(loadClass);
		// 对需要注入的bean进行赋值
		if (propertyMap != null) {
			// 循环遍历map
			Iterator<?> entries = propertyMap.entrySet().iterator();
			Map.Entry<?, ?> entry;
			while (entries.hasNext()) {
				entry = (Map.Entry<?, ?>) entries.next();
				String key = (String) entry.getKey();
				Object val = entry.getValue();
				beanDefinitionBuilder.addPropertyValue(key, val);
			}
		}
		// 注册组件
		registerBean(compomentName, beanDefinitionBuilder.getRawBeanDefinition(), applicationContext);
	}
	
	/**
	 * 移除组件操作
	 * @author 李鑫
	 * @param compomentName 组件在容器中的id
	 * @param beanDefinition 暂时不知道干嘛的，网上复制的
	 * @param applicationContext 当前环境下的ApplicationContext
	 */
	public static void removeBean(String compomentName, AbstractBeanDefinition beanDefinition,
			ApplicationContext applicationContext) {
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
		BeanDefinitionRegistry beanDefinitonRegistry = (BeanDefinitionRegistry) configurableApplicationContext
				.getBeanFactory();
		beanDefinitonRegistry.removeBeanDefinition(compomentName);
	}
	
	/**
	 * 对SpringMvc注册新的映射
	 * @author 李鑫
	 * @param applicationContext 当前环境的组件管理器
	 * @param method 映射所执行的方法
	 * @param componentClass 注册方法所属于的实体类Class
	 * @param mappingUrl 注册映射的url
	 */
	@SuppressWarnings("rawtypes")
	public static void registerMapping(ApplicationContext applicationContext, Method method, Class componentClass, String mappingUrl) {
		// 获取注册的bean在容器中的id数组
		String[] beanNamesForType = applicationContext.getBeanNamesForType(componentClass);
		// 获取注册映射的组件
		RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
		// 创建新的映射
		PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(mappingUrl);
		RequestMethodsRequestCondition requestMethodsRequestCondition = new RequestMethodsRequestCondition();
		// 合成新的映射
		RequestMappingInfo mapping_info = new RequestMappingInfo(patternsRequestCondition, requestMethodsRequestCondition, null, null, null, null, null);
		// 检查该映射是否应该被注册
		if(!judgeMappingIsRegister(applicationContext, mappingUrl)) {
			// 将映射注册到组件中
			requestMappingHandlerMapping.registerMapping(mapping_info, beanNamesForType[0], method); 
		}
	}
	
	/**
	 * 对指定的组件里的RequestMapping进行注册
	 * @author 李鑫
	 * @param clazz 需要注册的组件的class
	 * @param applicationContext 当前环境主容器类
	 * @param mappingRegex 映射中需要替换的字符串的原型。 如不需要，请传null
	 * @param mappingReplacement 映射中需要退换的字符串的目标数据。 如不需要，请传null
	 *        例： 将"/index${url.suffix}" 替换为 "/index.do"。originString：${url.suffix}",replaceString:".do"；
	 *        注：mappingRegex，mappingReplacement都不为null是进行替换操作
	 */
	public static void registerMapping4Class(Class<?> clazz, ApplicationContext applicationContext, String mappingRegex, String mappingReplacement) {
		/*
		 * 得到类上的RequestMapping注解的值
		 */
		RequestMapping clazzMappingAnnotation = clazz.getAnnotation(RequestMapping.class);
		List<String> clazzMappingList = Arrays.asList(clazzMappingAnnotation.value());
		Iterator<String> clazzMappingIterator = clazzMappingList.iterator();
		// 得到有RequestMapping注解的方法列表
		List<Method> methodList = ScanClassesUtil.getMethodsByAnnotationExist(clazz, RequestMapping.class);
		// 遍历方法
		Iterator<Method> iterator = methodList.iterator();
		while (iterator.hasNext()) {
			Method method = (Method) iterator.next();
			//得到该方法的注解上的url列表
			RequestMapping annotation = method.getAnnotation(RequestMapping.class);
			List<String> methodMappingList = Arrays.asList(annotation.value());
			Iterator<String> methodMappingIterator = methodMappingList.iterator();
			// 刷新clazzMappingIterator
			clazzMappingIterator = clazzMappingList.iterator();
			while (clazzMappingIterator.hasNext()) {
				// 类上的request的value
				String classMappingValue = (String) clazzMappingIterator.next();
				// 遍历url
				while (methodMappingIterator.hasNext()) {
					String mappingUrl = (String) methodMappingIterator.next();
					// 如果需要替换MappingUrl中的字符的话进行替换
					if(mappingRegex != null && mappingReplacement != null) {
						mappingUrl = mappingUrl.replaceAll(mappingRegex, mappingReplacement);
					}
					// 注册映射，
					registerMapping(applicationContext, method, clazz, classMappingValue + mappingUrl);
				}		
			}
		}
	}
	
	/**
	 * 移除指定RequestMapping映射
	 * @author 李鑫
	 * @param applicationContext 当前的组件容器类
	 * @param mappingUrl 需要移除Mapping的Url
	 */
	public static void unregisterMapping(ApplicationContext applicationContext, String mappingUrl) {
		// 获取注册映射的组件
		RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
		PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(mappingUrl);
		RequestMethodsRequestCondition requestMethodsRequestCondition = new RequestMethodsRequestCondition();
		// 合成新的映射
		RequestMappingInfo mapping_info = new RequestMappingInfo(patternsRequestCondition, requestMethodsRequestCondition, null, null, null, null, null);
		// 将映射在组件中移除
		if(judgeMappingIsRegister(applicationContext, mappingUrl)) {
			requestMappingHandlerMapping.unregisterMapping(mapping_info);
		}
	}
	
	
	/**
	 * 对指定的组件里的RequestMapping进行移除
	 * @author 李鑫
	 * @param clazz 需要注册的组件的class
	 * @param applicationContext 当前环境主容器类	 
	 * @param mappingRegex 映射中需要替换的字符串的原型。 如不需要，请传null
	 * @param mappingReplacement 映射中需要退换的字符串的目标数据。 如不需要，请传null
	 *        例： 将"/index${url.suffix}" 替换为 "/index.do"。originString：${url.suffix}",replaceString:".do"；
	 *        注：mappingRegex，mappingReplacement都不为null是进行替换操作
	 */
	public static void unregisterMapping4Class(Class<?> clazz, ApplicationContext applicationContext, String mappingRegex, String mappingReplacement) {
		/*
		 * 得到类上的RequestMapping注解的值
		 */
		RequestMapping clazzMappingAnnotation = clazz.getAnnotation(RequestMapping.class);
		List<String> clazzMappingList = Arrays.asList(clazzMappingAnnotation.value());
		Iterator<String> clazzMappingIterator = clazzMappingList.iterator();
		// 得到有RequestMapping注解的方法列表
		List<Method> methodList = ScanClassesUtil.getMethodsByAnnotationExist(clazz, RequestMapping.class);
		// 遍历方法
		Iterator<Method> iterator = methodList.iterator();
		while (iterator.hasNext()) {
			Method method = (Method) iterator.next();
			//得到该方法的注解上的url列表
			RequestMapping annotation = method.getAnnotation(RequestMapping.class);
			List<String> methodMappingList = Arrays.asList(annotation.value());
			Iterator<String> methodMappingIterator = methodMappingList.iterator();
			// 刷新clazzMappingIterator
			clazzMappingIterator = clazzMappingList.iterator();
			while (clazzMappingIterator.hasNext()) {
				String clazzMappingValue = (String) clazzMappingIterator.next();
				// 遍历url
				while (methodMappingIterator.hasNext()) {
					String mappingUrl = (String) methodMappingIterator.next();
					// 如果需要替换MappingUrl中的字符的话进行替换
					if(mappingRegex != null && mappingReplacement != null) {
						mappingUrl = mappingUrl.replaceAll(mappingRegex, mappingReplacement);
					}
					// 移除映射
					unregisterMapping(applicationContext, clazzMappingValue + mappingUrl);
				}		
			}
		}
	}
	/**
	 * 判断该映射路径是否已经被注册
	 * @author 李鑫
	 * @param applicationContext 当前环境的容器
	 * @param mappingName 检索的Mapping映射名称  例："/leimingyun/index.do"
	 * @return true ： 该映射已经被注册； 反之为false。
	 */
	public static boolean judgeMappingIsRegister(ApplicationContext applicationContext,String mappingName) {
		// 获取控制映射的类
		RequestMappingHandlerMapping mappingHandler= applicationContext.getBean(RequestMappingHandlerMapping.class);
		// 得到控制映射名称和放大的map集合
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = mappingHandler.getHandlerMethods();
		// 遍历map
		Set<RequestMappingInfo> keySet = handlerMethods.keySet();
		Iterator<RequestMappingInfo> keySetIterator = keySet.iterator();
		while (keySetIterator.hasNext()) {
			// 得到当前遍历的映射详情
			RequestMappingInfo requestMappingInfo = (RequestMappingInfo) keySetIterator.next();
			// 得到映射
			PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
			// 得到映射的id
			Set<String> patterns = patternsCondition.getPatterns();
			Iterator<String> patternsIterator = patterns.iterator();
			while (patternsIterator.hasNext()) {
				// 当前映射
				String mapping = (String) patternsIterator.next();
				// 判断是否与判断的映射相等。 相等标识已经被注册
				if(mapping.equals(mappingName)) {
					return true;
				}
			}
		}
		// 遍历完成未找到相同的映射。标识没有被注册
		return false;
	}
}
