package com.polyPool.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.polyPool.core.exception.ApiException;
import com.polyPool.helper.ApiRequestUtil;
import com.polyPool.helper.ApiUrlConstant;
import com.polyPool.helper.CommonUtil;
import com.polyPool.model.ApiIPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Slf4j
@Service
public class ApiServiceImpl implements ApiService {
    
    /**
     * 激活环境
     */
    @Value("${spring.profiles.active}")
    private String active;
    
    /**
     * polyPool接口根域名
     */
    @Value("${polyPool.SERVER_URL}")
    private String serverUrl;
    /**
     * 应用KEY
     */
    @Value("${polyPool.APP_KEY}")
    private String appKey;
    /**
     * 应用秘钥
     */
    @Value("${polyPool.APP_SECRET}")
    private String appSecret;
    /**
     * 应用授权码
     */
    @Value("${polyPool.CODE}")
    private String code;
    /**
     * 同步周期
     */
    @Value("${period}")
    private Integer period;
    
    /**
     * 访问token
     */
    @Value("${polyPool.ACCESS_TOKEN}")
    private String accessToken;
    /**
     * 刷新token
     */
    @Value("${polyPool.REFRESH_ACCESS_TOKEN}")
    private String refreshToken;
    /**
     * token刷新时间
     */
    @Value("${polyPool.FLUSH_TOKEN_TIME}")
    private Long flushTokenTime;
    /**
     * TOKEN有效期，单位毫秒
     */
    @Value("${polyPool.FLUSH_TOKEN_TIME}")
    private Long tokenExpires;
    /**
     * charset
     */
    private static final String CHARSET_UTF8 = "utf-8";
    /**
     * 版本号
     */
    private static final String VERSION = "2";
    
    /**
     * 启动定时刷新token
     */
    @Override
    public void startFlushToken() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for ( int i = 0; i < 3; i++ ) {
                    try {
                        if ( i > 0 ) {
                            Thread.sleep(3000L);
                        }
                        if ( flushTokenTime != null && System.currentTimeMillis() - flushTokenTime < tokenExpires ) {
                            return;
                        }
                        flushToken();
                        break;
                    } catch (Exception e) {
                        log.error("刷新token异常", e);
                    }
                }
            }
        }, 0, 60000L);
        Runtime.getRuntime().addShutdownHook(new Thread(timer::cancel));
    }
    
    /**
     * 接口：服务商登录获取 token(阿维塔提供) (basic auth)
     * • 地址：/v1/tokens
     * • 类型：POST
     *
     * @throws IOException
     */
    @Override
    public void flushToken() {
        log.info("刷新token");
        if ( "dev".equals(active) ) {
            return;
        }
        try {
            JSONObject json = null;
            String tokenStr = "tokenStr";
            String refreshTokenStr = "refreshTokenStr";
            String flushTokenTimeStr = "flushTokenTimeStr";
            String accessTokenStr = CommonUtil.getConfigItem(tokenStr, String.class,accessToken);
            String refreshTokenSt = CommonUtil.getConfigItem(refreshTokenStr, String.class,refreshToken);
            Long flushTokenTimeLong = CommonUtil.getConfigItem(flushTokenTimeStr, Long.class,System.currentTimeMillis()/1000L);
            if ( StringUtils.isEmpty(accessTokenStr) || flushTokenTimeLong<=System.currentTimeMillis()/1000L ) {
                //根据 refreshToken 刷新 accessToken
                json = ApiRequestUtil.refreshToken(getURL(ApiUrlConstant.REFRESH_TOKENS_URL), appKey, appSecret, refreshTokenSt);
            }else {
                return;
            }
            accessToken = json.getString("access_token");
            refreshToken = json.getString("refresh_token");
            Long expiresIn = json.getLong("expires_in");
            if ( expiresIn != null ) {
                //刷新时间提前1分钟
                tokenExpires = expiresIn * 1000 - 60 * 1000;
            }
            long now = System.currentTimeMillis()/1000L;
            flushTokenTime = now+expiresIn;
            CommonUtil.setConfigItem(tokenStr, accessToken);
            CommonUtil.setConfigItem(refreshTokenStr, refreshToken);
            CommonUtil.setConfigItem(flushTokenTimeStr, flushTokenTime);
        } catch (IOException e) {
            log.error("获取token失败", e);
        }
        
    }
    
    @Override
    public ApiIPage skumapQuery(Integer pageNo, Integer pageSize, Date begDate, Date endDate) {
        Assert.notNull(begDate, "开始时间不能为空");
        Assert.notNull(endDate, "结束时间不能为空");
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 50 : pageSize;
        JSONObject data = new JSONObject();
        data.fluentPut("page_index", pageNo)//第几页，从第一页开始，默认1
                .fluentPut("page_size", pageSize)//每页多少条，默认30，最大50
                .fluentPut("modified_begin", DateFormatUtils.format(begDate, CommonUtil.TIME_FORMAT))//修改起始时间，和结束时间必须同时存在，时间间隔不能超过七天，与商品编码不能同时为空
                .fluentPut("modified_end", DateFormatUtils.format(endDate, CommonUtil.TIME_FORMAT));//修改结束时间，和起始时间必须同时存在，时间间隔不能超过七天，与商品编码不能同时为空
        JSONObject result = this.callByBiz(ApiUrlConstant.SKUMAP_QUERY_URL, data);
        return toIPage(result, "datas");
    }
    
    @Override
    public ApiIPage ordersQuery(List<String> soIds) {
        Assert.notEmpty(soIds, "合同编号不能为空");
        Assert.isTrue(soIds.size() <= 20, "结束时间不能为空");
        JSONObject data = new JSONObject();
        data.fluentPut("page_index", 1)//第几页，从第一页开始，默认1
                .fluentPut("page_size", 50)//每页多少条，默认30，最大50
                .fluentPut("so_ids", soIds);//线上单号，最大限制20条
        JSONObject result = this.callByBiz(ApiUrlConstant.ORDERS_QUERY_URL, data);
        return toIPage(result, "orders");
    }
    
    @Override
    public ApiIPage ordersQueryByStatus(String status, Integer pageIndex, String modified_begin, String modified_end) {
        Assert.notNull(status, "状态不能为空");
        JSONObject data = new JSONObject();
        data.fluentPut("page_index", pageIndex)//第几页，从第一页开始，默认
//                .fluentPut("shop_id",11510979)//默认就是这家店铺作为线下对接店铺
                .fluentPut("page_size", 50)//每页多少条，默认30，最大50
//                .fluentPut("status", status)//线上单号，最大限制20条
                .fluentPut("modified_begin", modified_begin)//修改起始时间，ex:2021-12-02 10:26:25
                .fluentPut("modified_end", modified_end);//修改结束时间,ex: 2021-12-02 11:26:25
        JSONObject result = this.callByBiz(ApiUrlConstant.ORDERS_QUERY_URL, data);
        return toIPage(result, "orders");
    }
    
    @Override
    public ApiIPage wmsCoId() {
        JSONObject data = new JSONObject();
        data.fluentPut("page_index", 1)//第几页，从第一页开始，默认
                .fluentPut("page_size", 50);//每页多少条，默认30，最大50
        JSONObject result = this.callByBiz("/open/wms/partner/query", data);
        return toIPage(result, "datas");
    }
    
    @Override
    public ApiIPage shopId() {
        JSONObject data = new JSONObject();
        data.fluentPut("page_index", 1)//第几页，从第一页开始，默认
                .fluentPut("page_size", 50);//每页多少条，默认30，最大50
        JSONObject result = this.callByBiz("/open/shops/query", data);
        return toIPage(result, "datas");
    }
    
    @Override
    public JSONObject ordersUpload(JSONArray data) {
        return this.callByBiz(ApiUrlConstant.ORDERS_UPLOAD_URL, data);
    }
    
    @Override
    public ApiIPage logisticQuery(Integer pageNo, Integer pageSize, List<String> soIdList) {
        Assert.notEmpty(soIdList, "订单编号不能为空");
        pageSize = pageSize == null ? 50 : pageSize;
        pageNo = pageNo == null ? 1 : pageNo;
        JSONObject data = new JSONObject();
        data.fluentPut("page_index", pageNo)//第几页，从第一页开始，默认1
                .fluentPut("page_size", pageSize)//每页多少条，默认30，最大50
                .fluentPut("so_ids", soIdList);//平台订单编号,最多20
        JSONObject result = this.callByBiz(ApiUrlConstant.LOGISTIC_QUERY_URL, data);
        return toIPage(result, "orders");
    }
    
    /**
     * 调用业务接口
     *
     * @param api
     * @param paramJson
     * @return
     */
    private JSONObject callByBiz(String api, JSONObject paramJson) {
        return ApiRequestUtil.callByBiz(getURL(api), appKey, appSecret, accessToken, paramJson);
    }
    
    /**
     * 调用业务接口
     *
     * @param api
     * @param paramJson
     * @return
     */
    private JSONObject callByBiz(String api, JSONArray paramJson) {
        return ApiRequestUtil.callByBiz(getURL(api), appKey, appSecret, accessToken, paramJson);
    }
    
    /**
     * 将JSON数据封装成分页类型
     *
     * @param data
     * @param name
     * @return
     */
    private ApiIPage toIPage(JSONObject data, String name) {
        ApiIPage iPage = data.toJavaObject(ApiIPage.class);
        JSONArray jsonArray = data.getJSONArray(name);
        iPage.setResult(jsonArray);
        return iPage;
    }
    
    /**
     * 获取真实的URL
     *
     * @param api
     * @return
     */
    private URL getURL(String api) {
        String u = serverUrl;
        if ( !api.startsWith("/") ) {
            u += "/";
        }
        u += api;
        try {
            return new URL(u);
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }
    
}
