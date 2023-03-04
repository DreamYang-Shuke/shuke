package com.polyPool.model;

import lombok.Getter;

/**
 * 服务端响应代码
 */
@Getter
public enum ApiResponeCode {
	/** 操作成功 */
	OK(0, "成功"),
	/** 操作失败 */
	FAIL(-1,"操作失败"),
	/** 请求方法不支持 */
	METHOD_NOT_SUPPORTED(405, "请求方法不支持"),
	;

	/** 错误代码 */
	private final int code;
	/** 错误消息 */
	private final String msg;

	ApiResponeCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
