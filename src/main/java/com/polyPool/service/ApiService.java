package com.polyPool.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.polyPool.model.ApiIPage;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @date 2022-06-22 16:23
 */
public interface ApiService {

	/**
	 * 启动定时刷新token
	 */
	void startFlushToken();

	/**
	 * 刷新token
	 * @throws IOException
	 */
	void flushToken() throws IOException;
	
	/**
	 * 店铺商品资料查询
	 * @param pageNo 起始页
	 * @param pageSize 每页显示数据量
	 * @param begDate 起始时间
	 * @param endDate 结束时间
	 * @return 店铺商品资料
	 */
	ApiIPage skumapQuery(Integer pageNo, Integer pageSize, Date begDate, Date endDate);
	
	/**
	 * 订单查询（非淘系订单查询）
	 * @param soIds 订单号(合同编号)，不超过20条
	 * @return 订单信息
	 */
	ApiIPage ordersQuery(List<String> soIds);

	/**
	 * 订单查询（非淘系订单查询）
	 * @param status 物流状态
	 * @param pageIndex 页码
	 * @return 订单信息
	 */
	ApiIPage ordersQueryByStatus(String status, Integer pageIndex, String modified_begin, String modified_end);
	
	/**
	 * 订单上传(商家自有商城、跨境线下)
	 *
	 * @param data 参数
	 * @return 执行成功/失败
	 */
	JSONObject ordersUpload(JSONArray data);
	
	/**
	 * 发货信息查询
	 * @param pageNo 第几页
	 * @param pageSize 默认30，最大不超过50
	 * @param soIdList 平台订单编号
	 * @return 发货信息
	 */
	ApiIPage logisticQuery(Integer pageNo, Integer pageSize, List<String> soIdList);
 
	ApiIPage wmsCoId();
    
    ApiIPage shopId();
}
