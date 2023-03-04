package com.polyPool.service;

import com.polyPool.model.XbbBack;

import javax.servlet.http.HttpServletRequest;

/**
 * @date 2022-06-22 16:23
 */
public interface XbbService {

	/**
	 * 接收处理销帮帮的回调
	 * @param xbbBack
	 * @param request
	 * @param sign
	 */
	void accept(XbbBack xbbBack, HttpServletRequest request, String sign) throws Exception;
	
	void startSyncProduct();
	/**
	 * 同步产品数据
	 */
	void syncProduct();
    /**
     * 同步合同数据
     */
    void startContract();
	
	/**
	 * 同步合同订单
	 */
	void syncContract();
	/**
	 * 同步物流
	 */
	void syncLogistic();
	/**
	 * 同步物流
	 */
	void startLogistic();
}
