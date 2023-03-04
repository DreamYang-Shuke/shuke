package com.polyPool.helper;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 配置工具类
 * @date 2022-08-23 13:30
 */
public class CommonUtil {
	/** 配置文件名 */
	private static final String FILE_NAME = "config.json";
	/** 时间格式 */
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/** 默认时间段长度：7天 */
	public static final long SEGMENT_LENGIT_DEFAULT = 7 * 24 * 60 * 60 * 1000L;
	
	/**
	 * 获取配置项
	 * @param name
	 * @param cls
	 * @param <T>
	 * @return
	 * @throws IOException
	 */
	public static <T> T getConfigItem(String name, Class<T> cls, T defaultValue) throws IOException {
		File file = new File(FILE_NAME);
		synchronized (CommonUtil.class) {
			String str = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			JSONObject json = JSONObject.parseObject(str);
            T value = null;
            if ( Objects.nonNull(json) ) {
                value = json.getObject(name, cls);
            }
			if (value == null) {
				value = defaultValue;
			}
			return value;
		}
	}
	
	/**
	 * 保存配置项
	 * @param name
	 * @param value
	 * @throws IOException
	 */
	public static void setConfigItem(String name, Object value) throws IOException {
		File file = new File(FILE_NAME);
		synchronized (CommonUtil.class) {
			String str = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			JSONObject json = JSONObject.parseObject(str);
			if ( Objects.isNull(json) ) {
			    json = new JSONObject();
            }
			json.put(name, value);
			String outStr = json.toJSONString();
			FileUtils.writeStringToFile(file, outStr, StandardCharsets.UTF_8);
		}
	}
	
	/**
	 * 分割时间段
	 * @param beg           开始时间
	 * @param end           结束时间
	 * @param segmentLengit 每段时间长度，单位毫秒
	 * @return
	 */
	public static List<Date[]> splitTime(Date beg, Date end, long segmentLengit) {
		long begTime = beg.getTime();
		long endTime = end.getTime();
		List<Date[]> list = new ArrayList<>();
		do {
			long endTmp = begTime + segmentLengit;
			endTmp = Math.min(endTmp, endTime);
			Date[] arr = new Date[2];
			arr[0] = new Date(begTime);
			arr[1] = new Date(endTmp);
			list.add(arr);
			begTime = endTmp;
		} while (begTime < endTime);
		return list;
	}
}
