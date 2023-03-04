package com.polyPool.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SpringContextUtils implements ApplicationContextAware {

	/**
	 * 上下文对象实例
	 */
	private static ApplicationContext applicationContext;
	/** 用于获取application.yml的配置属性 */
	private static Environment env;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}

	/**
	 * 获取applicationContext
	 *
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取HttpServletRequest
	 */
	public static HttpServletRequest getHttpServletRequest() {
		return getRequestAttributes().getRequest();
	}

	/**
	 * 获取HttpServletResponse
	 */
	public static HttpServletResponse getHttpServletResponse() {
		return getRequestAttributes().getResponse();
	}
	
	/**
	 * 通过class获取Bean.
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	/**
	 * 获取application配置文件的属性值
	 * @Date 2018/6/2 0002  11:44 
	 * @param keyName 配置属性
	 * @return java.lang.String
	 */
	public static String getApplicationValue(String keyName) {
		if (env == null) {
			env = getBean(Environment.class);
		}
		return env.getProperty(keyName);
	}

	/**
	 * 获取application配置文件的属性值
	 * @Date 2018/6/2 0002  11:44 
	 * @param keyName 配置属性44 
	 * @param cls 返回类型
	 * @return java.lang.String
	 */
	public static <T> T getApplicationValue(String keyName, Class<T> cls) {
		if (env == null) {
			env = getBean(Environment.class);
		}
		return env.getProperty(keyName, cls);
	}

	/**
	 * 获取ServletRequestAttributes
	 * @Date 2018/6/3 0003  19:49 
	 * @return org.springframework.web.context.request.ServletRequestAttributes
	 */
	private static ServletRequestAttributes getRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	}

}
