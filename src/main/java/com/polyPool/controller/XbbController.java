package com.polyPool.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.polyPool.model.XbbBack;
import com.polyPool.service.XbbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

/**
 * 销帮帮的接口
 * @date 2022-06-22 16:07
 */
@Slf4j
@RestController
public class XbbController {

	@Autowired
	private XbbService xbbService;

	/**
	 * 获取参数
	 * @param request
	 * @return JSONObject 将请求参数有序接收后格式示例：{"corpid":"XXX","dataId":123456,"formId":714275,"operate":"EDIT","saasMark":1,"type":"CUSTOMER"}
	 * @throws IOException
	 */
	public static XbbBack getParams(HttpServletRequest request) throws IOException {
		ServletInputStream in = request.getInputStream();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		int len = 0;
		byte[] buff = new byte[1024 * 10];
		while ((len = in.read(buff)) != -1) {
			output.write(buff, 0, len);
		}
		String paramStr = new String(output.toByteArray(), StandardCharsets.UTF_8);
		LinkedHashMap<?, ?> paramMap = JSON.parseObject(paramStr, LinkedHashMap.class, Feature.OrderedField);
		String str = JSONObject.toJSONString(paramMap);
		XbbBack xbbBack = JSON.parseObject(paramStr, XbbBack.class);
		xbbBack.setSignStr(str);
		return xbbBack;
	}
}
