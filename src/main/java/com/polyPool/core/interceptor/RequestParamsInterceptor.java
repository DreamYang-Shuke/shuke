package com.polyPool.core.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * 请求拦截器
 */
@Slf4j
public class RequestParamsInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		requestInfoPring(request, handler);
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o, Exception e) throws Exception {
		printOverInfo(httpServletRequest);
	}
	
	/**
	 * 打印request中的信息
	 * @Date 2018/6/3 0003  20:16 
	 * @param request
	 * @param handler
	 * @return
	 */
	private void requestInfoPring(HttpServletRequest request, Object handler) {
		String addr = request.getRemoteHost() + ":" + request.getRemotePort() + "@" + request.getServerName();
		String className = null;
		try {
			if (handler instanceof HandlerMethod) {
				className = ((HandlerMethod)handler).getBeanType().getName();
			}
		} catch (Exception ignored) {
		}
		log.info("Request Begin>>>>：" + request.getRequestURI() + "\t" + request.getMethod() + ";  " + addr + "; Request Class: " + className);
		Map<String, String[]> parameterMap = request.getParameterMap();
		for (String key : parameterMap.keySet()) {
			log.info("\tRequest params：" + key + " : " + Arrays.toString(parameterMap.get(key)));
		}
		// 记录开始时间
		request.setAttribute("requestTime", System.currentTimeMillis());
	}
	
	/**
	 * 打印调试信息
	 * @param request
	 */
	private void printOverInfo(HttpServletRequest request) {
		//输出最终耗时
		Long begTime = (Long)request.getAttribute("requestTime");
		long time = System.currentTimeMillis() - begTime;
		long sss = time % 1000;
		time /= 1000;
		long s = time % 60;
		time /= 60;
		long m = time % 60;
		time /= 60;
		long h = time;
		log.info("Request End<<<<：{} time：{}", request.getRequestURI(), String.format("%02d:%02d:%02d.%03d", h, m, s, sss));
	}
}
