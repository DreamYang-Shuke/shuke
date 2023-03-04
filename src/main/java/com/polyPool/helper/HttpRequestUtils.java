package com.polyPool.helper;

import com.alibaba.fastjson.JSONObject;
import com.polyPool.core.exception.ApiException;
import com.polyPool.model.ApiResponeCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * http请求工具类
 */
@Slf4j
public class HttpRequestUtils {

	public static JSONObject post(String url, String param) throws IOException {
		return execute(url, "POST", param, null, null, null);
	}
	public static JSONObject get(String url, String param) throws IOException {
		return execute(url, "GET", param, null, null, null);
	}
	public static JSONObject delete(String url) throws IOException {
		return execute(url, "DELETE", null, null, null, null);
	}
	
	/**
	 * 发起GET请求
	 * @param url
	 * @param param
	 * @param sign
	 * @return
	 * @throws IOException
	 */
	public static JSONObject get(String url, String param, String sign) throws IOException {
		return execute(url, "GET", param, sign, null, null);
	}
	
	/**
	 * 发起POST请求
	 * @param url   请求url
	 * @param param 请求参数(JSON格式)
	 * @param sign  签名
	 * @return 请求回参
	 */
	public static JSONObject post(String url, String param, String sign) throws IOException {
		return execute(url, "POST", param, sign, null, null);
	}

	/**
	 * 发起DELETE请求
	 * @param url
	 * @param param
	 * @param sign
	 * @return
	 * @throws IOException
	 */
	public static JSONObject delete(String url, String param, String sign) throws IOException {
		return execute(url, "DELETE", param, sign, null, null);
	}

	private static JSONObject execute(String url, String method, String param, String sign, String authorization, String token) throws IOException {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		try {
			// 创建默认的httpClient实例.
			httpclient = HttpClients.createDefault();

			HttpRequestBase httpRequestBase = null;
			switch (method.toUpperCase()) {
				case "GET":
					httpRequestBase = new HttpGet(url);
					break;
				case "POST":
					httpRequestBase = new HttpPost(url);
					//注意：请求格式为JSON,请求内容以UTF-8编码
					if (Objects.nonNull(param)) {
						StringEntity uefEntity = new StringEntity(param, "UTF-8");
						uefEntity.setContentType("application/json");
						((HttpPost) httpRequestBase).setEntity(uefEntity);
					}
					break;
				case "DELETE":
					httpRequestBase = new HttpDeleteWithBody(url);
					if (Objects.nonNull(param)) {
						StringEntity uefEntity = new StringEntity(param, "UTF-8");
						uefEntity.setContentType("application/json");
						((HttpDeleteWithBody) httpRequestBase).setEntity(uefEntity);
					}
					break;
				default:
					throw new RuntimeException("不支持的请求方法");
			}
			// 创建http post
			addHeader(httpRequestBase, sign, authorization, token);

			//参数设置
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
			httpRequestBase.setConfig(requestConfig);

			response = httpclient.execute(httpRequestBase);
			int statusCode = -1;
			try {
				statusCode = response.getStatusLine().getStatusCode();
			} catch (Exception ignored) {
			}

			HttpEntity entity = response.getEntity();
			String body = null;
			if (entity != null) {
				body = EntityUtils.toString(entity, "UTF-8");
				log.info("--------------------------------------");
				log.info("Response content: " + body);
				log.info("--------------------------------------");
			}
			Assert.isTrue(statusCode == 200, "请求失败，接口响应异常" + statusCode);
			if (!StringUtils.isEmpty(body)) {
				return JSONObject.parseObject(body);
			} else {
				return new JSONObject();
			}
		} finally {
			// 关闭连接,释放资源
			close(response, httpclient);
		}
	}

	/**
	 * 设置请求头
	 * @param httpRequestBase
	 * @param sign
	 * @param authorization
	 * @param token
	 */
	private static void addHeader(HttpRequestBase httpRequestBase, String sign, String authorization, String token) {
		if (!StringUtils.isEmpty(sign)) {
			//注意：sign放在请求头里
			httpRequestBase.addHeader("sign", sign);
		}
		if (!StringUtils.isEmpty(authorization)) {
			httpRequestBase.addHeader("authorization", "Basic " + authorization);
		}
		if (!StringUtils.isEmpty(token)) {
			httpRequestBase.addHeader("X-ACCESS-TOKEN", token);
		}
	}
	
	private static void close(AutoCloseable... autoCloseables) {
		if (autoCloseables == null) {
			return;
		}
		for (AutoCloseable autoCloseable : autoCloseables) {
			try {
				if (autoCloseable != null) {
					autoCloseable.close();
				}
			} catch (Exception ignored) {
			}
		}
	}
}