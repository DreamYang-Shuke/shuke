package com.polyPool.core.exception;

/**
 * 接口异常
 */
public class ApiException extends RuntimeException {

	public ApiException(String errMsg) {
		super(errMsg);
	}
	
	public ApiException(Exception e) {
		super(e.getMessage(), e);
	}
}
