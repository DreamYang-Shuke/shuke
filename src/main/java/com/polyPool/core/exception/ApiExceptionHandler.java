package com.polyPool.core.exception;

import com.polyPool.core.util.SpringContextUtils;
import com.polyPool.model.ApiResponeCode;
import com.polyPool.model.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 * @Author scott
 * @Date 2019
 */
@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Object handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		StringBuilder sb = new StringBuilder();
		sb.append("不支持");
		sb.append(e.getMethod());
		sb.append("请求方法，");
		sb.append("支持以下");
		String [] methods = e.getSupportedMethods();
		if(methods!=null){
			for(String str:methods){
				sb.append(str);
				sb.append("、");
			}
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}
		logErrorMsg(e, sb.toString());
		log.error(sb.toString(), e);
		return ApiResult.fail(ApiResponeCode.METHOD_NOT_SUPPORTED.getCode(), sb.toString());
	}
	
	@ExceptionHandler(XbbException.class)
	public Object handleXbbException(XbbException e) {
		logErrorMsg(e, null);
		return ApiResult.fail(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public Object handleException(Exception e) {
		logErrorMsg(e, "异常");
		return ApiResult.fail(e.getMessage());
	}
	
	@ExceptionHandler(Throwable.class)
	public Object handleThrowable(Throwable e) {
		logErrorMsg(e, "Throwable异常");
		return ApiResult.fail(e.getMessage());
	}

	private void logErrorMsg(Throwable e, String msg) {
		String url = "";
		try {
			url = SpringContextUtils.getHttpServletRequest().getRequestURI();
		} catch (Exception ignored) {
		}
		log.error(url + ":" + (msg == null ? e.getMessage() : msg), e);
	}
}
