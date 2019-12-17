package com.xnx3.j2ee.system;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.Lang;

/**
 * Spring AOP相关
 * @author 管雷鸣
 */
@Aspect
@Component
public class Aop {
		public static boolean useExecuteTime = false;	//是否使用Controller函数记录执行时间的功能
		public static long recordTime = 0;	//若执行时间超过多少毫秒，就在控制台打印出来，这里的单位是毫秒
		
		static{
			useExecuteTime = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("ExecuteTime.controller.used").equals("true");
			int recordTimeInt = Lang.stringToInt(ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("ExecuteTime.recordTime"), 0);
			recordTime = recordTimeInt;
		}
		
		/**
		 * 统计 com.xnx3.j2ee包下的 Service、Dao、Controller 中方法执行的时间
		 * @param joinPoint
		 * @throws Throwable
		 */
		@Around("execution(* com.xnx3.j2ee..*.*(..))")
//		@Around("execution(* com.xnx3.j2ee..*.*(..)) || execution(* com.baidu..*.*(..))")
		public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
			long start = System.currentTimeMillis();
			Object object = joinPoint.proceed();
			long end = System.currentTimeMillis();
			long t = end - start;
			String tmp = joinPoint.getSignature().toString();
			if(t > recordTime){
				System.out.println(String.format("ImplExecuteTime : %s , class : %s",t,tmp));
			}
			return object;
		}
	
}
