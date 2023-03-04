package com.polyPool.helper;


public class ApiUrlConstant {
	/** 根据CODE获取token */
	public static final String GET_TOKENS_URL = "/openWeb/auth/accessToken";
	/** 刷新token */
	public static final String REFRESH_TOKENS_URL = "/openWeb/auth/refreshToken";
	/** 店铺商品资料查询 */
	public static final String SKUMAP_QUERY_URL= "/open/sku/query";
	/** 订单查询（非淘系订单查询） */
	public static final String ORDERS_QUERY_URL= "/open/orders/single/query";
	/** 订单上传(商家自有商城、跨境线下) */
	public static final String ORDERS_UPLOAD_URL= "/open/jushuitan/orders/upload";
	/** 发货信息查询 */
	public static final String LOGISTIC_QUERY_URL= "/open/logistic/query";
	/** 商品上传 */
	public static final String ITEM_SKU_UPLOAD="/open/jushuitan/itemsku/upload";
}
