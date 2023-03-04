package com.polyPool.model;

import lombok.Data;

/**
 * 销帮帮回调参数对象
 * @date 2022-06-24 11:24
 */
@Data
public class XbbBack {
	/** 公司id */
	String corpid;
	/** 业务主键id */
	Long dataId;
	/** 回调业务 */
	String type;
	/** 操作类型（说明见操作类型枚举） */
	String operate;
	/** 1:销帮帮系统模板数据，2:自定义表单数据 */
	Integer saasMark;
	/** 对应数据的表单模板id */
	Long formId;

	/**用于签名校验的参数字符串*/
	String signStr;
}
