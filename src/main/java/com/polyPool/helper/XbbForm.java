package com.polyPool.helper;

import lombok.Getter;

/**
 * 自定义表单
 * @date 2022-06-28 11:21
 */
@Getter
public enum XbbForm {
	/** 产品 */
	product(6547616, XbbField.Product.class, "产品"),
	/** 合同订单 */
	contract(1965920, XbbField.Contract.class, "合同订单"),
    customer(1965913, XbbField.class,"客户"),
	;

	private final long formId;
	private final Class xbbField;
	private final String text;

	XbbForm(long formId, Class xbbField, String text) {
		this.formId = formId;
		this.xbbField = xbbField;
		this.text = text;
	}

	public static XbbForm getByFormId(long formId) {
		for (XbbForm xbbForm : values()) {
			if (xbbForm.getFormId() == formId) {
				return xbbForm;
			}
		}
		return null;
	}
}
