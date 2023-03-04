package com.polyPool.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.polyPool.api.XbbApi;
import com.polyPool.core.util.DateUtil;
import com.polyPool.helper.CommonUtil;
import com.polyPool.helper.XbbField;
import com.polyPool.helper.XbbOperate;
import com.polyPool.helper.XbbForm;
import com.polyPool.helper.XbbUrlConstant;
import com.polyPool.model.ApiIPage;
import com.polyPool.model.XbbBack;
import com.polyPool.model.XbbDetail;
import com.polyPool.model.XbbIPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Service
public class XbbServiceImpl implements XbbService {
    
    /**
     * 同步周期
     */
    @Value("${period}")
    private Integer period;
    
    @Autowired
    private XbbApi xbbApi;
    @Autowired
    private ApiService apiService;
    @Autowired
    private ConvertService convertService;
    
    @Value("${retry.count}")
    private Integer retryCount;
    
    //<editor-fold desc="销帮帮回调处理">
    @Override
    public void accept(XbbBack xbbBack, HttpServletRequest request, String sign) {
        //验证签名
        xbbApi.checkParams(xbbBack, sign);
        Long formId = xbbBack.getFormId();
        XbbOperate operate = XbbOperate.getByAttr(xbbBack.getOperate());
        Assert.notNull(operate, "不支持的operate");
        
        XbbForm xbbForm = XbbForm.getByFormId(formId);
        if ( xbbForm == null ) {
            log.info("不支持的formId");
            return;
        }
        
        Long dataId = xbbBack.getDataId();
        log.info("接收处理 {}，dataId：{}，operate：{}，formId：{}", xbbForm.name(), dataId, operate, xbbForm.getFormId());
        retryCount = retryCount == null ? 3 : retryCount;
        
        handle(xbbForm, dataId, operate, 0, xbbBack);
    }
    
    /**
     * 销帮帮回调的处理，失败后重试
     *
     * @param xbbForm
     * @param dataId
     * @param operate
     * @param retry
     * @param xbbBack
     */
    private void handle(XbbForm xbbForm, Long dataId, XbbOperate operate, int retry, XbbBack xbbBack) {
        try {
            switch (xbbForm) {
                default:
                    break;
            }
        } catch (Exception e) {
            retry++;
            log.warn("第{}次处理异常", retry);
            if ( retry > retryCount ) {
                log.warn("{}次处理异常，记录异常到销帮帮失败表", retry);
//				xbbApi.logFail(xbbBack, e);
            } else {
                int finalRetry = retry;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handle(xbbForm, dataId, operate, finalRetry, xbbBack);
                        timer.cancel();
                    }
                }, retry * 5000L);
            }
        }
    }
    //</editor-fold>
    
    /**
     * 启动定时获取产品
     */
    @Override
    public void startSyncProduct() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for ( int i = 0; i < 3; i++ ) {
                    try {
                        if ( i > 0 ) {
                            Thread.sleep(3000L);
                        }
                        syncProduct();
                        break;
                    } catch (Exception e) {
                        log.error("同步产品失败", e);
                    }
                }
            }
        }, 0, 60000L);
        Runtime.getRuntime().addShutdownHook(new Thread(timer::cancel));
    }
    
    @Override
    public void syncProduct() {
		/*
		1. 取上次同步时间，若为空 则取当天0时
		2. 判断 上次同步时间 与当前时间差值是否大于7天，若大于7天 分段同步
		3. 循环查询聚水潭商品，写入销帮帮中
		4. 更新上次同步时间
		 */
        String configName = "lastSyncProductTime";
        Date nowDate = new Date();
        try {
            Date lastTime = CommonUtil.getConfigItem(configName, Date.class, DateUtils.truncate(nowDate, Calendar.DAY_OF_MONTH));
            lastTime = new Date(lastTime.getTime() - 60000L);
            List<Date[]> dateList = CommonUtil.splitTime(lastTime, nowDate, CommonUtil.SEGMENT_LENGIT_DEFAULT);
            for ( Date[] dates : dateList ) {
                Date begDate = dates[0];
                Date endDate = dates[1];
                Integer pageNo = 1;
                Integer pageSize = 50;
                while (true) {
                    ApiIPage iPage = apiService.skumapQuery(pageNo, pageSize, begDate, endDate);
                    log.info("pageNo:{},begDate:{},endDate:{},pageCount:{}", pageNo, begDate, endDate, iPage.getPage_count());
                    List<JSONObject> result = iPage.getResult();
                    for ( JSONObject json : result ) {
                        try {
                            String skuId = json.getString("sku_id");
                            //获取本产品是否有相同的
                            JSONArray conditions = new JSONArray()
                                    .fluentAdd(new JSONObject()
                                            .fluentPut("attr", "serialNo")
                                            .fluentPut("symbol", "equal")
                                            .fluentPut("value", Arrays.asList(skuId)))
                                    .fluentAdd(new JSONObject()
                                            .fluentPut("attr", "num_3")
                                            .fluentPut("symbol", "equal")
                                            .fluentPut("value", Arrays.asList(0)));
                            XbbDetail xbbDetail = xbbApi.productGet(XbbForm.product.getFormId(), 1, 1, conditions);
                            //TODO 转成销帮帮数据
                            JSONObject xbbJson = new JSONObject();
                            //产品编号
                            xbbJson.put("serialNo", json.getString("sku_id"));
                            //产品名称
                            xbbJson.put("text_1", json.getString("name"));
                            //款式图片
                            if ( Objects.nonNull(json.getString("pic")) ) {
                                JSONArray jsonArray = new JSONArray();
                                jsonArray.add(json.getString("pic"));
                                xbbJson.put("file_1", jsonArray);
                            }
                            //分类
                            xbbJson.put("num_13", json.getIntValue("c_id"));
                            //价格
                            xbbJson.put("num_1", json.getString("sale_price"));
                            //成本
                            xbbJson.put("num_2", json.getString("cost_price"));
                            xbbJson.put("text_5", json.getString("properties_value"));
                            xbbJson.put("text_4", json.getString("short_name"));
                            if ( Objects.isNull(xbbDetail) ) {
                                xbbApi.productAdd(XbbForm.product.getFormId(), xbbJson);
                            } else {
                                xbbApi.productEdit(xbbDetail.getDataId(), xbbJson);
                            }
                            Thread.sleep(350L);
                        } catch (Exception e) {
                            log.error("数据保存错误", e);
                        }
                    }
                    if ( !iPage.hasNext() ) {
                        break;
                    }
                    pageNo++;
                }
            }
        } catch (Exception e) {
            log.error("同步产品数据失败 " + e.getMessage(), e);
        } finally {
            try {
                CommonUtil.setConfigItem(configName, nowDate);
            } catch (Exception e) {
                log.error("同步产品数据保存时间失败 " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * 启动定时获取产品
     */
    @Override
    public void startContract() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for ( int i = 0; i < 3; i++ ) {
                    try {
                        if ( i > 0 ) {
                            Thread.sleep(3000L);
                        }
                        syncContract();
                        break;
                    } catch (Exception e) {
                        log.error("同步合同失败", e);
                    }
                }
            }
        }, 0, 60000L);
        Runtime.getRuntime().addShutdownHook(new Thread(timer::cancel));
    }
    
    /**
     * 启动定时同步物流
     */
    @Override
    public void startLogistic() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for ( int i = 0; i < 3; i++ ) {
                    try {
                        if ( i > 0 ) {
                            Thread.sleep(3000L);
                        }
                        syncLogistic();
                        break;
                    } catch (Exception e) {
                        log.error("同步物流失败", e);
                    }
                }
            }
        }, 0, 60000L);
        Runtime.getRuntime().addShutdownHook(new Thread(timer::cancel));
    }
    
    @Override
    public void syncContract() {
		/*
		1 定时查询 销帮帮-合同订单列表 条件：同步聚水潭时间 为空
		2 调用 聚水潭-订单查询 接口，判断是否已经上传
		3 未上传 调用 聚水潭-订单上传 接口
		4 调用 销帮帮-编辑合同订单接口，更新 同步聚水潭时间 字段
		 */
        Integer pageNo = 1;
        Integer pageSize = 100;
        String configName = "lastSyncContractTime";
        Date nowDate = new Date();
        try {
            Date lastTime = CommonUtil.getConfigItem(configName, Date.class, DateUtils.truncate(nowDate, Calendar.DAY_OF_MONTH));
            lastTime = new Date(lastTime.getTime() - 60000L);
            JSONArray conditions = new JSONArray().fluentAdd(range("updateTime", lastTime.getTime() / 1000L, nowDate.getTime() / 1000L));
            while (true) {
                //固定查第一页，直到所有数据都处理完
                XbbIPage iPage = xbbApi.contractList(XbbForm.contract.getFormId(), pageNo, pageSize, conditions);
                if ( iPage == null ) {
                    break;
                }
                List<XbbDetail> list = iPage.getList();
                if ( CollectionUtils.isEmpty(iPage.getList()) ) {
                    break;
                }
                List<String> idList = new ArrayList<>();
                for ( XbbDetail xbbDetail : list ) {
                    JSONObject data = xbbDetail.getData();
                    //TODO 字段名
                    idList.add(data.getString("serialNo"));
                }
                
                ApiIPage apiIPage = apiService.ordersQuery(idList);
                List<JSONObject> apiList = apiIPage.getResult();
                Map<String, JSONObject> apiMap = new HashMap<>();
                apiList.forEach(api -> {
                    apiMap.put(api.getString("so_id"), api);
                });
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                
                for ( XbbDetail xbbDetail : list ) {
                    Long contractId = xbbDetail.getDataId();
                    XbbDetail contractDetail = xbbApi.getContract(contractId);
                    JSONObject data = contractDetail.getData();
                    //上传订单数据
                    JSONArray orderArray = new JSONArray();
                    JSONObject orderUpload = new JSONObject();
                    JSONObject signerObj = data.getJSONObject("text_8");
                    Integer shop_id = 11510979;
                    if ( Objects.nonNull(signerObj) ) {
                        String signerStr = signerObj.getString("id");
                        if ( Objects.nonNull(signerStr) ) {
                            shop_id = XbbField.Dict.getNum(XbbField.Dict._2_1.getType(), signerStr);
                        }
                        if ( Objects.isNull(shop_id) ) {
                            shop_id = 11510979;
                        }
                    }
                    orderUpload.put("shop_id", shop_id);
                    JSONObject shopBuyerIdObj = data.getJSONObject("text_2");
                    String shopBuyerId = data.getString("text_17");
                    if ( Objects.nonNull(shopBuyerIdObj) ) {
                        shopBuyerId = shopBuyerIdObj.getString("name");
                        Long customerId = shopBuyerIdObj.getLongValue("id");
                        if ( Objects.nonNull(customerId) || Objects.equals(customerId, 0L) ) {
                            XbbDetail customerDetail = xbbApi.getCustomer(customerId);
                            if ( Objects.nonNull(customerDetail) ) {
                                JSONObject customerObj = customerDetail.getData();
                                if ( Objects.nonNull(customerObj) ) {
                                    //获取客户表中的数据
                                    shopBuyerId = customerObj.getString("text_24");
                                }
                            }
                        }
                    }
                    JSONObject logisticsCompanyObj = data.getJSONObject("text_16");
                    String logisticsCompany = null;
                    if ( Objects.nonNull(logisticsCompanyObj) ) {
                        logisticsCompany = logisticsCompanyObj.getString("text");
                    }
                    
                    JSONObject wmsCoIdObj = data.getJSONObject("text_15");
                    if ( Objects.nonNull(wmsCoIdObj) ) {
                        Integer wmsCoId = XbbField.Dict.getName(wmsCoIdObj.getString("text"));
                        orderUpload.put("wms_co_id", wmsCoId);
                        log.info("wms_co_id:{}", wmsCoId);
                    }
                    log.info("shop_buyer_id:{},logistics_company:{}", shopBuyerId, logisticsCompany);
                    orderUpload.put("shop_buyer_id", shopBuyerId);
                    orderUpload.put("pay_amount", data.getIntValue("num_1"));
                    orderUpload.put("freight", data.getIntValue("num_12"));
                    orderUpload.put("so_id", data.getString("serialNo"));
                    orderUpload.put("order_date", dateFormat.format(xbbDetail.getAddTime() * 1000L));
                    orderUpload.put("buyer_message", data.getString("text_23"));
                    orderUpload.put("remark", data.getString("text_24"));
                    orderUpload.put("receiver_name", data.getString("text_17"));
                    orderUpload.put("logistics_company", logisticsCompany);
                    JSONObject address_1_Object = data.getJSONObject("address_1");
                    if ( Objects.nonNull(address_1_Object) ) {
                        orderUpload.put("receiver_state", address_1_Object.getString("province"));
                        orderUpload.put("receiver_city", address_1_Object.getString("city"));
                        orderUpload.put("receiver_district", address_1_Object.getString("district"));
                        orderUpload.put("receiver_address", address_1_Object.getString("address"));
                    }
                    JSONArray subFormPhoneArray = data.getJSONArray("subForm_1");
                    if ( !CollectionUtils.isEmpty(subFormPhoneArray) ) {
                        JSONObject subFormObject = subFormPhoneArray.getJSONObject(0);
                        if ( Objects.nonNull(subFormObject) ) {
                            orderUpload.put("receiver_phone", subFormObject.getString("text_1"));
                            orderUpload.put("receiver_mobile", subFormObject.getString("text_1"));
                        }
                    }
                    JSONArray productArray = data.getJSONArray("array_4");
                    if ( !CollectionUtils.isEmpty(productArray) ) {
                        JSONArray jsonArray = new JSONArray();
                        int i = 1;
                        for ( Object detailObject : productArray ) {
                            JSONObject detail = (JSONObject) detailObject;
                            XbbDetail productDetail = xbbApi.getProduct(((JSONObject) detailObject).getLongValue("text_1"));
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("sku_id", productDetail.getData().getString("serialNo"));
                            jsonObject.put("qty", detail.getIntValue("num_3"));
                            jsonObject.put("shop_sku_id", "123");
                            jsonObject.put("base_price", detail.getIntValue("num_6"));
                            jsonObject.put("amount", detail.getIntValue("num_5"));
                            jsonObject.put("name", productDetail.getData().getString("text_1"));
                            jsonObject.put("outer_oi_id", data.getString("serialNo") + i);
                            jsonArray.add(jsonObject);
                            i++;
                        }
                        orderUpload.put("items", jsonArray);
                    }
                    
                    //订单：
                    // 等待买家付款=WAIT_BUYER_PAY，
                    // 等待卖家发货=WAIT_SELLER_SEND_GOODS（传此状态时实际支付金额=应付金额ERP才会显示已付款待审核）,
                    // 等待买家确认收货=WAIT_BUYER_CONFIRM_GOODS,
                    // 交易成功=TRADE_FINISHED,
                    // 付款后交易关闭=TRADE_CLOSED,
                    // 付款前交易关闭=TRADE_CLOSED_BY_TAOBAO；发货前可更新
                    orderUpload.put("shop_status", "WAIT_SELLER_SEND_GOODS");
                    JSONObject payObject = new JSONObject();
                    //支付账号
                    payObject.put("outer_pay_id", data.getString("text_25"));
                    payObject.put("pay_date", dateFormat.format(xbbDetail.getAddTime() * 1000L));
                    JSONObject paymentObj = data.getJSONObject("text_29");
                    String payment = "";
                    if ( Objects.nonNull(paymentObj) ){
                        payment = paymentObj.getString("text");
                    }
                    payObject.put("payment", payment);
                    //收款账号
                    JSONObject sellerAccount = data.getJSONObject("text_29");
                    if ( Objects.nonNull(sellerAccount) ) {
                        payObject.put("seller_account", sellerAccount.getString("text"));
                    }
                    payObject.put("buyer_account", shopBuyerId);
                    payObject.put("amount", data.getIntValue("num_1"));
                    orderUpload.put("pay", payObject);
                    orderArray.add(orderUpload);
                    JSONObject jsonObject1 = apiService.ordersUpload(orderArray);
                    log.info(jsonObject1 + "");
                }
                Thread.sleep(350L);
                if ( list.size() < pageSize ) {
                    break;
                }
                pageNo++;
            }
        } catch (Exception e) {
            log.error("同步合同数据失败 " + e.getMessage(), e);
        } finally {
            try {
                CommonUtil.setConfigItem(configName, nowDate);
            } catch (IOException e) {
                log.error("塞入同步合同时间失败 " + e.getMessage(), e);
            }
            
        }
    }
    
    @Override
    public void syncLogistic() {
        /*
         1 根据状态为Send,PageIndex来查找订单list，优化的话就可以在更新poly的订单更新时间，每次只获取大于更新时间的列表，这个后面可以加上
         2 根据poly订单数据，更新XBB合同订单的发送时间
         3 上述操作循环，直到最大page_count
         */
//        ApiIPage apiIPage1 = apiService.wmsCoId();
        Integer pageNo = 1;
        Integer pageSize = 100;
        String configName = "lastSyncLogisticTime";
        Date nowDate = new Date();
        try {
            Date lastTime = CommonUtil.getConfigItem(configName, Date.class, DateUtils.truncate(nowDate, Calendar.DAY_OF_MONTH));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (true) {
                ApiIPage apiIPage = apiService.ordersQueryByStatus("Sent", pageNo, dateFormat.format(lastTime), dateFormat.format(nowDate));
                List<JSONObject> apiList = apiIPage.getResult();
                //poly条件查询数据为空，跳出循环
                if ( CollectionUtils.isEmpty(apiList) ) {
                    break;
                }
                List<String> serialNoList = new ArrayList<>();
                Map<String, JSONObject> sendDateMap = new HashMap<>();
                for ( JSONObject jsonObject : apiList ) {
                    String soId = jsonObject.getString("so_id");
                    serialNoList.add(soId);
                    sendDateMap.put(soId, jsonObject);
                }
                //将serialNo放在一起去xbb列表查询
                JSONArray conditions = new JSONArray().fluentAdd(getEqConditions(XbbField.Contract.CONTRACT_NO, serialNoList));
                XbbIPage iPage = xbbApi.contractList(XbbForm.contract.getFormId(), pageNo, pageSize, conditions);
                if ( iPage == null ) {
                    break;
                }
                List<XbbDetail> list = iPage.getList();
                if ( CollectionUtils.isEmpty(iPage.getList()) ) {
                    break;
                }
                for ( XbbDetail xbbDetail : list ) {
                    try {
                        JSONObject data = xbbDetail.getData();
                        String serialNo = data.getString("serialNo");
                        JSONObject polyData = sendDateMap.get(serialNo);
                        if ( polyData == null ) {
                            continue;
                        }
                        String sendTime = polyData.getString(XbbField.Contract.send_date.getName());
                        long sendTimeLong = DateUtil.getInt(sendTime);
                        JSONObject newData = new JSONObject();
                        newData.put(XbbField.Contract.logistics_company.getAttr(), polyData.getString(XbbField.Contract.logistics_company.getName()));
                        newData.put(XbbField.Contract.logistics_no.getAttr(), polyData.getString(XbbField.Contract.logistics_no.getName()));
                        newData.put(XbbField.Contract.send_date.getAttr(), sendTimeLong);
                        //更新合同订单发货时间
                        xbbApi.contractEdit(xbbDetail.getDataId(), newData);
                    } catch (Exception e) {
                        log.error("保存数据，防止多条数据更新失败影响数据准确性问题");
                    }
                }
                Integer page_count = apiIPage.getPage_count();
                if ( pageNo < page_count ) {
                    pageNo = pageNo + 1;
                } else {
                    break;
                }
                Thread.sleep(350L);
            }
        } catch (Exception e) {
            log.error("同步合同发货时间数据失败 " + e.getMessage(), e);
        } finally {
            try {
                CommonUtil.setConfigItem(configName, nowDate);
            } catch (IOException e) {
                log.error("更新发货时间失败重新保存轮训时间数据");
            }
            
        }
    }
    
    
    /**
     * 获取equal条件
     *
     * @param field
     * @param value
     */
    private JSONObject getEqConditions(XbbField field, Object value) {
        return new JSONObject()
                .fluentPut("attr", field.getAttr())
                .fluentPut("symbol", "in")
                .fluentPut("fieldType", "1")
                .fluentPut("value", value);
    }
    
    /**
     * 获取isNull条件
     *
     * @param field
     */
    private JSONObject getIsNullConditions(XbbField field) {
        return new JSONObject()
                .fluentPut("attr", field.getAttr())
                .fluentPut("symbol", "empty")
                .fluentPut("value", "[]");
    }
    
    /**
     * 获取大于等于
     *
     * @param attr
     * @param value
     */
    private JSONObject greaterequal(String attr, Object value) {
        return new JSONObject()
                .fluentPut("attr", attr)
                .fluentPut("fieldType", 4)
                .fluentPut("symbol", "greaterequal")
                .fluentPut("value", new JSONArray().fluentAdd(value));
    }
    
    /**
     * 获取小于等于
     *
     * @param attr
     * @param value
     */
    private JSONObject lessequal(String attr, Object value) {
        return new JSONObject()
                .fluentPut("attr", attr)
                .fluentPut("fieldType", 4)
                .fluentPut("symbol", "lessequal")
                .fluentPut("value", new JSONArray().fluentAdd(value));
    }
    
    /**
     * 获取选择范围
     *
     * @param attr
     * @param value1
     * @param value2
     */
    private JSONObject range(String attr, Object value1, Object value2) {
        return new JSONObject()
                .fluentPut("attr", attr)
                .fluentPut("fieldType", 4)
                .fluentPut("symbol", "range")
                .fluentPut("value", Arrays.asList(value1, value2));
    }
}
