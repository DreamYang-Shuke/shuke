package com.polyPool.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @date 2022-07-01 14:23
 */
@Data
public class XbbIPage implements Serializable {
	/** 返回数据对象 */
	private List<XbbDetail> list;
	/** 页码，默认为1 */
	private Integer page = 1;
	/** 每页数量，默认为20，最大值100 */
	private Integer pageSize = 1;
	/** 总数 */
	private Integer totalCount = 1;
	/** 总页数 */
	private Integer totalPage = 1;
}
