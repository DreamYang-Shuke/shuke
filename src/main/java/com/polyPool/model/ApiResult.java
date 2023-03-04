package com.polyPool.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口返回数据格式
 */
@Data
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回处理消息
     */
    private String msg = "操作成功！";
    
    /**
     * 返回代码
     */
    private Integer code = 0;

    /**
     * 返回数据对象 data
     */
    private T data;

    private static <T> ApiResult<T> create(boolean success, Integer code, String msg, T data) {
        ApiResult<T> r = new ApiResult<T>();
        r.code = code;
        r.msg = msg;
        r.data = data;
        return r;
    }

    public static <T> ApiResult<T> ok() {
        return create(true, ApiResponeCode.OK.getCode(), ApiResponeCode.OK.getMsg(), null);
    }

    public static <T> ApiResult<T> ok(T data) {
        return create(true, ApiResponeCode.OK.getCode(), ApiResponeCode.OK.getMsg(), data);
    }

    public static <T> ApiResult<T> ok(String msg, T data) {
        return create(true, ApiResponeCode.OK.getCode(), msg, data);
    }

    public static <T> ApiResult<T> fail() {
        return create(false, ApiResponeCode.FAIL.getCode(), ApiResponeCode.FAIL.getMsg(), null);
    }

    public static <T> ApiResult<T> fail(ApiResponeCode apiResponeCode) {
        return create(false, apiResponeCode.getCode(), apiResponeCode.getMsg(), null);
    }

    public static <T> ApiResult<T> fail(String msg) {
        return create(false, ApiResponeCode.FAIL.getCode(), msg, null);
    }

    public static ApiResult<Object> fail(Integer code, String msg) {
        return create(false, code, msg, null);
    }

    public static <T> ApiResult<T> fail(Integer code, String msg, T data) {
        return create(false, code, msg, data);
    }

    public static boolean isSuccess(ApiResult<?> apiResult) {
        return ApiResponeCode.OK.getCode() == apiResult.code; 
    }
}