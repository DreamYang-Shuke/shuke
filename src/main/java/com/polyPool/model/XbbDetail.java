package com.polyPool.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @date 2022-07-01 14:23
 */
@Data
public class XbbDetail implements Serializable {
	/**数据对象-原始*/
	private JSONObject srcData;
	/**数据对象*/
	private JSONObject data;
	/** 数据添加时间 */
	private Long addTime;
	/** 数据ID */
	private Long dataId;
	/** 表单ID */
	private Long formId;
	/** 数据更新时间 */
	private Long updateTime;
}
