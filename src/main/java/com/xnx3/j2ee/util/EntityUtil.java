package com.xnx3.j2ee.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;

/**
 * 针对 数据表实体类 的工具
 * @author 管雷鸣
 *
 */
public class EntityUtil {
	
	/**
	 * 将 Entity 实体类 转化为 map 的形式。
	 * <br/>实体类必须是标准java bean
	 * <br/>key:数据表列名， value:field的值
	 * <br/>注意，key是数据表中列的名，并不是实体类的field名。支持 get 方法上加 {@link Column}标注数据库中对应的的列名 ）
	 * <br/>如果 get 方法上没有加  {@link Column} 标注对应的数据表的名字，那么 map key 默认是 field 的名字
	 * <br/>如果 get 方法上加了 {@link Column} ，并且指定了 name ，那么用 {@link Column#name()} 的值作为 map key
	 * @param entityObj 要转化为map的实体类对象
	 * @return map key:数据表列名， value:field的值
	 */
	public static Map<String, Object> entityToMap(Object entityObj){
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = entityObj.getClass().getDeclaredFields();
		Method[] methods = entityObj.getClass().getDeclaredMethods();
		Map<String, Method> methodMap = new HashMap<String, Method>();
		for (int i = 0; i < methods.length; i++) {
			methodMap.put(methods[i].getName().toLowerCase(), methods[i]);
		}
		
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			
			//前缀加get、is的方法名
			String methodname = field.getName();
			if(field.getType().getSimpleName().toLowerCase().equals("boolean")){
				//前缀加is的
				if(methodname.indexOf("is") == 0){
					//前缀是is开头的，不加前缀
				}else{
					//增加is前缀
					methodname = "is" + methodname;
				}
			}else{
				if(methodname.indexOf("get") == 0){
					//前缀是get开头的，不加前缀
				}else{
					//增加get前缀
					methodname = "get" + methodname;
				}
			}
			Method method = methodMap.get(methodname.toLowerCase());
			if(method != null){
				//有值，这个是对应着数据库字段的，要取出来
				String columnName = field.getName();	//默认就是 field 的那么，如果有注解标注字段名，则是取注解标注的
				javax.persistence.Column column=method.getAnnotation(javax.persistence.Column.class);
				if(column != null){
					columnName = column.name();
				}
				
				try {
					map.put(columnName, method.invoke(entityObj));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
}
