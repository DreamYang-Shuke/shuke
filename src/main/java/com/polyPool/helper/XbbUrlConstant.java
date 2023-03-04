package com.polyPool.helper;

/**
 * 销帮帮接口全局配置与常量
 */
public class XbbUrlConstant {

	/* ---------------------------------------------表单模块接口地址------------------------------------------------*/
	/** 表单模板列表接口 */
	public static final String FORM_LIST = "/pro/v2/api/form/list";

	/** 表单模板字段解释接口 */
	public static final String FORM_GET = "/pro/v2/api/form/get";
    
    /* ---------------------------------------------PAAS表单接口地址------------------------------------------------*/
    /** 新建自定义表单数据接口 */
    public static final String PAAS_ADD = "/pro/v2/api/paas/add";
    /** 编辑自定义表单数据接口 */
    public static final String PAAS_EDIT = "/pro/v2/api/paas/edit";
    /** 自定义表单数据列表接口 */
    public static final String PAAS_GET_LIST = "/pro/v2/api/paas/list";
    /** 自定义表单数据详情接口 */
    public static final String PAAS_GET_DETAIL = "/pro/v2/api/paas/detail";
    /*** 删除自定义表单数据接口 */
    public static final String PAAS_DEL = "/pro/v2/api/paas/del";

	/* ---------------------------------------------工单接口地址------------------------------------------------*/
	/*** 工单列表接口 */
	public static final String WORJ_ORDER_LIST = "/pro/v2/api/workOrder/list";
	/*** 工单详情接口 */
	public static final String WORJ_ORDER_GET_DETAIL = "/pro/v2/api/workOrder/detail";
	/*** 工单配件接口 */
	public static final String WORJ_ORDER_PRODUCT_LIST = "/pro/v2/api/workOrder/productList";
	/*** 工单模板列表接口 */
	public static final String WORJ_ORDER_TEMPLATE_LIST = "/pro/v2/api/workOrder/templateList";
	/*** 工单模板详情接口 */
	public static final String WORJ_ORDER_TEMPLATE_GET_DETAIL = "/pro/v2/api/workOrder/templateDetail";
	
	
	/* ---------------------------------产品表单-----------------------*/
    public static final String PRODUCT_ADD = "/pro/v2/api/product/add";
    
    public static final String PRODUCT_EDIT = "/pro/v2/api/product/edit";
    
    public static final String PRODUCT_DETAIL_LIST = "/pro/v2/api/product/list";
    
    public static final String PRODUCT_DETAIL_GET = "/pro/v2/api/product/detail";
    
    /* ---------------------------------合同表单-----------------------*/
    public static final String CONTRACT_ADD = "/pro/v2/api/contract/add";
    
    public static final String CONTRACT_EDIT = "/pro/v2/api/contract/edit";
    
    public static final String CONTRACT_DETAIL_LIST = "/pro/v2/api/contract/list";
    
    public static final String CONTRACT_DETAIL_GET = "/pro/v2/api/contract/detail";
    
    public static final String CUSTOMER_DETAIL_GET = "/pro/v2/api/customer/detail";
    
}
