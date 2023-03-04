package com.polyPool.helper;

import lombok.Getter;

import java.util.Objects;

/**
 * 销帮帮WebHook操作类型枚举
 * @date 2022-06-24 10:47
 */
@Getter
public enum XbbOperate {
	/**新增*/
	NEW("new", "新增"),
	/**编辑*/
	EDIT("edit", "编辑"),
	/**删除*/
	DELETE("delete", "删除"),
	/**归档*/
	ARCHIVED("archived", "归档"),
	/**取消归档*/
	CANCELARCHIVED("cancelArchived", "取消归档"),
	;
	private final String attr;
	private final String text;

	XbbOperate(String attr, String text) {
		this.attr = attr;
		this.text = text;
	}

	public static XbbOperate getByAttr(String attr) {
        for (XbbOperate operate : values()) {
            if (Objects.equals(attr, operate.attr)) {
                return operate;
            }
        }
        return null;
    }
}
