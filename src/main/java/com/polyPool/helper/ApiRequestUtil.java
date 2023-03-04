package com.polyPool.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.polyPool.core.exception.ApiException;
import com.polyPool.model.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;


/**
 *
 * @date 2022-08-16 14:10
 */
@Slf4j
public class ApiRequestUtil {
	
	private static final String SIGN_METHOD_MD5 = "md5";
	private static final String CHARSET_UTF8 = "UTF-8";
	private static final String CONTENT_ENCODING_GZIP = "gzip";
	
	/**
	 * 获取新的token
	 * @param code
	 * @return
	 */
	public static JSONObject getToken(URL url, String appKey, String appSecret, String code) {
		Map<String, String> params = new HashMap<>();
		// 公共参数
		params.put("app_key", appKey);
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		params.put("grant_type", "authorization_code");
		params.put("charset", "utf-8");
		params.put("code", code);
		// 签名参数
		params.put("sign", signParam(params, appSecret));
		// 调用API
		try {
			return callApi(url, params);
		} catch (Exception e) {
			throw new ApiException(e);
		}
	}
	
	/**
	 * 刷新token
	 * @param refreshToken
	 * @return
	 */
	public static JSONObject refreshToken(URL url, String appKey, String appSecret, String refreshToken) {
		Map<String, String> params = new HashMap<>();
		// 公共参数
		params.put("app_key", appKey);
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		params.put("grant_type", "refresh_token");
		params.put("charset", "utf-8");
		params.put("refresh_token", refreshToken);
		params.put("scope", "all");
		// 签名参数
		params.put("sign", signParam(params, appSecret));
		// 调用API
		try {
			return callApi(url, params);
		} catch (Exception e) {
			throw new ApiException(e);
		}
	}
    /**
     * 调用接口API
     * @param url
     * @param bizJson
     * @return
     */
    public static JSONObject callByBiz(URL url, String appKey, String appSecret, String accessToken, JSONArray bizJson) {
        Map<String, String> params = new HashMap<>();
        // 公共参数
        params.put("app_key", appKey);
        params.put("access_token", accessToken);
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("version", "2");
        params.put("charset", "utf-8");
        // 业务参数
        String bizStr = bizJson.toJSONString();
        params.put("biz", bizStr);
        // 签名参数
        params.put("sign", signParam(params, appSecret));
        // 调用API
        try {
            log.info("请求参数URL：{}, 参数：{}", url.toString(), bizStr);
            return callApi(url, params);
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }
    
    
	/**
	 * 调用接口API
	 * @param url
	 * @param bizJson
	 * @return
	 */
	public static JSONObject callByBiz(URL url, String appKey, String appSecret, String accessToken, JSONObject bizJson) {
		Map<String, String> params = new HashMap<>();
		// 公共参数
		params.put("app_key", appKey);
		params.put("access_token", accessToken);
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		params.put("version", "2");
		params.put("charset", "utf-8");
		// 业务参数
		String bizStr = bizJson.toJSONString();
		params.put("biz", bizStr);
		// 签名参数
		params.put("sign", signParam(params, appSecret));
		// 调用API
		try {
			log.info("请求参数URL：{}, 参数：{}", url.toString(), bizStr);
			return callApi(url, params);
		} catch (Exception e) {
			throw new ApiException(e);
		}
	}
	
	/**
	 * 对TOP请求进行签名。
	 */
	private static String signParam(Map<String, String> params,String secret) {
		// 第一步：检查参数是否已经排序
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		
		// 第二步：把所有参数名和参数值串在一起
		StringBuilder query = new StringBuilder();
		query.append(secret);
		for (String key : keys) {
			String value = params.get(key);
			if (isNotEmpty(key) && isNotEmpty(value)) {
				query.append(key).append(value);
			}
		}
		return createSign(query.toString());
	}
	
	/**
	 * 生成新sign
	 *
	 * @param str 字符串
	 * @return String
	 */
	private static String createSign(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		try {
			MessageDigest mdTemp = MessageDigest.getInstance(SIGN_METHOD_MD5);
			mdTemp.update(str.getBytes(StandardCharsets.UTF_8));
			
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] buf = new char[j * 2];
			int k = 0;
			int i = 0;
			while (i < j) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
				i++;
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 执行请求
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	private static JSONObject callApi(URL url, Map<String, String> params) throws IOException {
		String query = buildQuery(params);
		byte[] content = {};
		if (query != null) {
			content = query.getBytes(StandardCharsets.UTF_8);
		}
		
		HttpURLConnection conn = null;
		OutputStream out = null;
		ApiResult<JSONObject> result = null;
		try {
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Host", url.getHost());
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + CHARSET_UTF8);
			out = conn.getOutputStream();
			out.write(content);
			String rsp = getResponseAsString(conn);
			result = JSONObject.toJavaObject(JSONObject.parseObject(rsp), ApiResult.class);
			if (!ApiResult.isSuccess(result)) {
				log.warn("api请求失败，URL:{}, result:{}, param:{}", url, result, params);
				throw new ApiException(result.getMsg());
			}
		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return result.getData();
	}
	
	private static String buildQuery(Map<String, String> params) throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}
		
		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> entries = params.entrySet();
		boolean hasParam = false;
		
		for (Entry<String, String> entry : entries) {
			String name = entry.getKey();
			String value = entry.getValue();
			// 忽略参数名或参数值为空的参数
			if (isNotEmpty(name) && isNotEmpty(value)) {
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}
				query.append(name).append("=").append(URLEncoder.encode(value, ApiRequestUtil.CHARSET_UTF8));
			}
		}
		return query.toString();
	}
	
	private static String getResponseAsString(HttpURLConnection conn) throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		if (conn.getResponseCode() < 400) {
			String contentEncoding = conn.getContentEncoding();
			if (CONTENT_ENCODING_GZIP.equalsIgnoreCase(contentEncoding)) {
				return getStreamAsString(new GZIPInputStream(conn.getInputStream()), charset);
			} else {
				return getStreamAsString(conn.getInputStream(), charset);
			}
		} else {// Client Error 4xx and Server Error 5xx
			throw new IOException(conn.getResponseCode() + " " + conn.getResponseMessage());
		}
	}
	
	private static String getStreamAsString(InputStream stream, String charset) throws IOException {
		try {
			Reader reader = new InputStreamReader(stream, charset);
			StringBuilder response = new StringBuilder();
			
			final char[] buff = new char[1024];
			int read = 0;
			while ((read = reader.read(buff)) > 0) {
				response.append(buff, 0, read);
			}
			
			return response.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}
	
	private static String getResponseCharset(String ctype) {
		String charset = CHARSET_UTF8;
		if (isNotEmpty(ctype)) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (isNotEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}
		
		return charset;
	}
	
	private static boolean isNotEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return false;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(value.charAt(i))) {
				return true;
			}
		}
		return false;
	}
}
