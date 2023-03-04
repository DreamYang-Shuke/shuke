package com.polyPool.service;

import com.alibaba.fastjson.JSONObject;
import com.polyPool.model.XbbDetail;
import com.polyPool.model.XbbIPage;

/**
 * @date 2022-06-22 16:23
 */
public interface ConvertService {

	/**
	 * 销帮帮字段编码
	 * @param json
	 * @param formId
	 * @return
	 */
	JSONObject xbbFieldEncode(JSONObject json, long formId);

	/**
	 * 销帮帮字段解析
	 * @param iPage
	 * @param formId
	 */
	void xbbFieldDecode(XbbIPage iPage, long formId);

	/**
	 * 销帮帮字段解析
	 * @param xbbDetail
	 * @param formId
	 */
	void xbbFieldDecode(XbbDetail xbbDetail, long formId);

	/**
	 * 销帮帮转换下拉选项值，转成可保存的格式
	 * @param json
	 * @param formId
	 * @return
	 */
	JSONObject xbbConvertSelect(JSONObject json, long formId);
}
