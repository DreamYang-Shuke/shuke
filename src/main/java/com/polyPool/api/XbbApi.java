package com.polyPool.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.polyPool.core.exception.XbbException;
import com.polyPool.core.util.XbbDigestUtil;
import com.polyPool.helper.HttpRequestUtils;
import com.polyPool.helper.XbbOperate;
import com.polyPool.helper.XbbUrlConstant;
import com.polyPool.helper.XbbField;
import com.polyPool.helper.XbbForm;
import com.polyPool.model.XbbBack;
import com.polyPool.model.XbbDetail;
import com.polyPool.model.XbbIPage;
import com.polyPool.service.ConvertService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @date 2022-06-23 17:38
 */
@Slf4j
@Component
public class XbbApi {
    
    /**
     * 销帮帮接口根域名
     */
    @Value("${xbb.XBB_API_ROOT}")
    private String XBB_API_ROOT;
    /**
     * 本公司访问接口的token,该值相当于密钥，请妥善保管，不要泄露，可以下列url中重置token
     * 管理员账号登录销帮帮WEB版后台后，访问https://pfweb.xbongbong.com/#/apiToken/index查看
     */
    @Value("${xbb.TOKEN}")
    private String TOKEN;
    /**
     * 本公司corpid。接口基础参数，接口请求必传
     * 管理员账号登录销帮帮WEB版后台后，访问https://pfweb.xbongbong.com/#/apiToken/index查看
     */
    @Value("${xbb.CORP_ID}")
    private String CORP_ID;
    /**
     * 接口操作人userId,接口基础参数。不传默认取超管角色
     */
    @Value("${xbb.USER_ID}")
    private String USER_ID;
    /**
     * 回调TOKEN
     */
    @Value("${xbb.BACK_TOKEN}")
    private String BACK_TOKEN;
    
    @Autowired
    private ConvertService convertService;
    
    
    /**
     * 检查参数是否合法
     *
     * @param xbbBack
     */
    public void checkParams(XbbBack xbbBack, String sign) {
        if ( !this.checkSign(xbbBack.getSignStr(), sign) ) {
            throw new XbbException(401, "签名错误");
        }
        String type = xbbBack.getType();
        if ( type != null ) {
            throw new XbbException(501, "只处理自定义类型");
        }
        Assert.isTrue(Objects.equals(CORP_ID, xbbBack.getCorpid()), "corpid错误");
        Assert.notNull(xbbBack.getFormId(), "formId错误");
        Assert.notNull(xbbBack.getDataId(), "dataId错误");
        Assert.notNull(XbbOperate.getByAttr(xbbBack.getOperate()), "operate错误");
    }
    
    /**
     * 获取 表单模板列表
     *
     * @return
     */
    public JSONArray getFormList() {
        JSONObject data = new JSONObject();
        data.put("saasMark", 2);
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        
        return this.xbbApi(XbbUrlConstant.FORM_LIST, data, "formList", JSONArray.class);
    }
    
    /**
     * 获取 表单模板字段解释
     *
     * @param formId
     * @return
     */
    public JSONArray getFormGet(Long formId) {
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        return this.xbbApi(XbbUrlConstant.FORM_GET, data, "explainList", JSONArray.class);
    }
    
    
    /**
     * 新建表单数据接口
     *
     * @param formId
     * @param dataList
     * @return 表单数据ID
     */
    public Long productAdd(Long formId, JSONObject dataList) {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        data.put("dataList", dataList);
        return this.xbbApi(XbbUrlConstant.PRODUCT_ADD, data, "formDataId", Long.class);
    }
    
    /**
     * 编辑表单数据接口 不编码
     *
     * @param dataId
     * @param dataList
     * @return 表单数据ID
     */
    public Long productEdit(Long dataId, JSONObject dataList) {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("dataId", dataId);
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        data.put("dataList", dataList);
        return this.xbbApi(XbbUrlConstant.PRODUCT_EDIT, data, "formDataId", Long.class);
    }
    
    /**
     * 产品表单数据列表接口
     *
     * @param formId
     * @param pageNo
     * @param pageSize
     * @param conditions
     * @return 数据列表
     */
    public XbbDetail productGet(Long formId, Integer pageNo, Integer pageSize, JSONArray conditions) {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("conditions", conditions);
        data.put("page", pageNo);
        data.put("pageSize", pageSize);
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        XbbIPage iPage = this.xbbApi(XbbUrlConstant.PRODUCT_DETAIL_LIST, data, "", XbbIPage.class);
        List<XbbDetail> list = iPage.getList();
        if ( CollectionUtils.isEmpty(list) ) {
            return null;
        }
        return list.get(0);
    }
    
    
    /**
     * 产品表单数据列表接口
     *
     * @param formId
     * @param pageNo
     * @param pageSize
     * @param conditions
     * @return 数据列表
     */
    public XbbIPage productList(Long formId, Integer pageNo, Integer pageSize, JSONArray conditions) {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("conditions", conditions);
        data.put("page", pageNo);
        data.put("pageSize", pageSize);
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        XbbIPage iPage = this.xbbApi(XbbUrlConstant.PRODUCT_DETAIL_LIST, data, "", XbbIPage.class);
        if ( iPage != null ) {
//			if (Objects.equals(formId, XbbForm.explorationsInfo.getFormId())
//					|| Objects.equals(formId, XbbForm.installationsInfo.getFormId())) {
//				//勘探、安装 重新get查询，避免数据格式不一致
//				List<XbbDetail> list = iPage.getList();
//				if (CollectionUtils.isEmpty(list)) {
//					return iPage;
//				}
//				List<XbbDetail> dataList = new ArrayList<>();
//				for (XbbDetail xbbDetail : list) {
//					dataList.add(this.get(xbbDetail.getDataId()));
//				}
//				iPage.setList(dataList);
//			}
        } else {
            iPage = new XbbIPage();
        }
        return iPage;
    }
    /**
     * 自定义表单数据列表接口
     *
     * @param formId
     * @param pageNo
     * @param pageSize
     * @param conditions
     * @return 数据列表
     */
    public XbbIPage contractList(Long formId, Integer pageNo, Integer pageSize, JSONArray conditions) {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("conditions", conditions);
        data.put("page", pageNo);
        data.put("pageSize", pageSize);
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        XbbIPage iPage = this.xbbApi(XbbUrlConstant.CONTRACT_DETAIL_LIST, data, "", XbbIPage.class);
        if ( iPage != null ) {
//			if (Objects.equals(formId, XbbForm.explorationsInfo.getFormId())
//					|| Objects.equals(formId, XbbForm.installationsInfo.getFormId())) {
//				//勘探、安装 重新get查询，避免数据格式不一致
//				List<XbbDetail> list = iPage.getList();
//				if (CollectionUtils.isEmpty(list)) {
//					return iPage;
//				}
//				List<XbbDetail> dataList = new ArrayList<>();
//				for (XbbDetail xbbDetail : list) {
//					dataList.add(this.get(xbbDetail.getDataId()));
//				}
//				iPage.setList(dataList);
//			}
        } else {
            iPage = new XbbIPage();
        }
        return iPage;
    }

    /**
     * 合同订单编辑
     * @param dataId
     * @param dataList
     * @return 数据列表
     */
    public Long contractEdit(Long dataId, JSONObject dataList) {
        //todo
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("dataId", dataId);
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        data.put("dataList", dataList);
        return this.xbbApi(XbbUrlConstant.CONTRACT_EDIT, data, "formDataId", Long.class);
    }
    
    /**
     * 新建自定义表单数据接口
     *
     * @param formId
     * @param dataList
     * @return 表单数据ID
     */
    public Long add(Long formId, JSONObject dataList) {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        data.put("dataList", dataList);
        return this.xbbApi(XbbUrlConstant.PAAS_ADD, data, "formDataId", Long.class);
    }
    
    /**
     * 编辑自定义表单数据接口 不编码
     *
     * @param dataId
     * @param dataList
     * @return 表单数据ID
     */
    public Long edit(Long dataId, JSONObject dataList) {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("dataId", dataId);
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        data.put("dataList", dataList);
        return this.xbbApi(XbbUrlConstant.PAAS_EDIT, data, "formDataId", Long.class);
    }
    
    /**
     * 自定义表单数据列表接口
     *
     * @param formId
     * @param pageNo
     * @param pageSize
     * @param conditions
     * @return 数据列表
     */
    public XbbIPage list(Long formId, Integer pageNo, Integer pageSize, JSONArray conditions) {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("conditions", conditions);
        data.put("page", pageNo);
        data.put("pageSize", pageSize);
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        XbbIPage iPage = this.xbbApi(XbbUrlConstant.PAAS_GET_LIST, data, "", XbbIPage.class);
        if ( iPage != null ) {
//			if (Objects.equals(formId, XbbForm.explorationsInfo.getFormId())
//					|| Objects.equals(formId, XbbForm.installationsInfo.getFormId())) {
//				//勘探、安装 重新get查询，避免数据格式不一致
//				List<XbbDetail> list = iPage.getList();
//				if (CollectionUtils.isEmpty(list)) {
//					return iPage;
//				}
//				List<XbbDetail> dataList = new ArrayList<>();
//				for (XbbDetail xbbDetail : list) {
//					dataList.add(this.get(xbbDetail.getDataId()));
//				}
//				iPage.setList(dataList);
//			}
        } else {
            iPage = new XbbIPage();
        }
        return iPage;
    }
    
    /**
     * 自定义表单数据列表接口 查询唯一数据
     *
     * @param formId
     * @param conditions
     * @return 数据对象
     */
    public XbbDetail listOne(Long formId, JSONArray conditions) {
        XbbIPage iPage = this.list(formId, 1, 1, conditions);
        if ( iPage == null ) {
            return null;
        }
        List<XbbDetail> list = iPage.getList();
        if ( CollectionUtils.isEmpty(list) ) {
            return null;
        }
        return list.get(0);
    }
    
    /**
     * 自定义表单数据详情接口
     *
     * @param dataId
     * @return 数据对象
     */
    public XbbDetail get(Long dataId) {
        return this.get(dataId, false, true);
    }
    
    /**
     * 删除自定义表单数据接口
     *
     * @param dataId
     */
    public void del(Long dataId) {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("dataId", dataId);
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        this.xbbApi(XbbUrlConstant.PAAS_DEL, data, null, null);
    }
    
    /**
     * 自定义表单数据详情接口
     *
     * @param dataId
     * @return 数据对象
     */
    public XbbDetail getProduct(Long dataId) {
        return this.get(dataId, true, false);
    }
    
    public XbbDetail getContract(Long dataId) {
        return this.get(dataId, false, false);
    }
    
    /**
     * 查询自定义表单 或 工单
     *
     * @param dataId      数据ID
     * @return
     */
    public XbbDetail getCustomer(Long dataId) {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("dataId", dataId);
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        XbbDetail xbbDetail = this.xbbApi(XbbUrlConstant.CUSTOMER_DETAIL_GET, data, "", XbbDetail.class);
        return xbbDetail;
    }
    
    /**
     * 查询自定义表单 或 工单
     *
     * @param dataId      数据ID
     * @param isProduct   是否产品类型
     * @param isDecode    是否解析字段
     * @return
     */
    public XbbDetail get(Long dataId, boolean isProduct, boolean isDecode) {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("dataId", dataId);
        data.put("corpid", CORP_ID);
        data.put("userId", USER_ID);
        XbbDetail xbbDetail = this.xbbApi(isProduct ? XbbUrlConstant.PRODUCT_DETAIL_GET : XbbUrlConstant.CONTRACT_DETAIL_GET, data, "", XbbDetail.class);
        if ( xbbDetail != null && isDecode ) {
            convertService.xbbFieldDecode(xbbDetail, xbbDetail.getFormId());
        }
        return xbbDetail;
    }
    
    /**
     * 调用销帮帮API
     *
     * @param url
     * @param data
     * @param key
     * @param resultClass
     * @param <T>
     * @return
     */
    private <T> T xbbApi(String url, JSON data, String key, Class<T> resultClass) {
        //调用xbbApi方法，发起API请求
        String absoluteUrl = this.getApiUrl(url);
        //签名规则:将访问接口所需的参数集data + token字符串拼接后进行SHA256运算得到最后的签名,然后将签名参数sign(参数名为sign)放入http header中;
        // 			将访问接口所需的参数集data(参数名为data)放入http body。
        // 			算法为 sha-256 ( data+token ),使用utf-8编码
        String sign = this.getDataSign(data, TOKEN);
        
        log.debug("====以下输出用于调试，正式上线时请删除com.xbongbong.api.demo.helper.ConfigConstant.xbbApi()======");
        log.debug("xbbapi url=" + url);
        log.debug("xbbapi data=" + data);
        log.debug("xbbapi sign=" + sign);
        
        JSONObject responseJson = null;
        try {
            //发起post请求，data作为 request body，sign在 http-header中传输
            responseJson = HttpRequestUtils.post(absoluteUrl, data.toJSONString(), sign);
        } catch (Exception e) {
            log.error("销帮帮接口请求异常", e);
            throw new XbbException(-1, "http post访问出错");
        }
        
        T result = null;
        Integer code = responseJson.getInteger("code");
        if ( Objects.equals(1, code) ) {
            if ( resultClass == null ) {
                return null;
            }
            if ( "".equals(key) ) {
                result = responseJson.getObject("result", resultClass);
            } else {
                JSONObject resultJson = responseJson.getJSONObject("result");
                if ( resultJson != null ) {
                    result = resultJson.getObject(key, resultClass);
                }
            }
            return result;
        } else {
            log.warn("请求参数：{}", data.toJSONString());
            throw new XbbException(code, responseJson.getString("msg"));
        }
    }
    
    /**
     * 获取接口地址
     *
     * @param restApiUrl 请求url
     * @return 请求回参
     */
    private String getApiUrl(String restApiUrl) {
        return XBB_API_ROOT + restApiUrl;
    }
    
    /**
     * 获取签名
     *
     * @param data  请求参数(JSON格式)
     * @param token 令牌
     * @return 接口回参
     */
    private String getDataSign(JSON data, String token) {
        return XbbDigestUtil.encrypt(JSON.toJSONString(data) + token, "SHA-256");
    }
    
    /**
     * sign校验，如果需要对回调请求进行合法性校验，需要对传过来的sign做验证
     *
     * @param params 有序的接收到的参数
     * @param orSign 请求传过来的sign
     * @return
     */
    private boolean checkSign(String params, String orSign) {
        // 将请求参数与token进行SHA256运算，得到校验签名sign
        String sign = XbbDigestUtil.encrypt(params + BACK_TOKEN, "SHA-256");
        // 将请求过来的checkSign与生成的sign进行匹配，匹配成功则证明到达该接口的请求是由销帮帮发出的安全的请求
        return Objects.equals(orSign, sign);
    }
    
    /**
     * 获取默认的排序条件
     *
     * @return
     */
    public static JSONObject getDefualtSort() {
        return new JSONObject()
                .fluentPut("sortMap", new JSONObject()
                        .fluentPut("field", "updateTime")
                        .fluentPut("sort", "acs"));
    }
    
    /**
     * 当 销帮帮回调数据处理失败 时，记录异常数据信息
     *
     * @param xbbBack
     * @param e
     */
    public void logFail(XbbBack xbbBack, Exception e) {
//		Long formId = xbbBack.getFormId();
//		Long dataId = xbbBack.getDataId();
//		XbbOperate operate = XbbOperate.getByAttr(xbbBack.getOperate());
//		Assert.notNull(operate, "不支持的operate");
//		XbbForm xbbForm = XbbForm.getByFormId(formId);
//		Assert.notNull(xbbForm, "不支持的formId");
//		XbbDetail xbbDetail = get(dataId, false, false);
//		if (xbbDetail == null || xbbDetail.getData() == null) {
//			//没有数据 不处理
//			return;
//		}
//
//		log.info("接收处理 {}，dataId：{}，operate：{}，formId：{}", xbbForm.name(), dataId, operate, xbbForm.getFormId());
//		// 数据表单 数据serialNo operate  异常堆栈 时间
//		JSONObject data = new JSONObject();
//		JSONObject json = xbbDetail.getData();
//		String serialNo = null;
//		if (xbbForm == XbbForm.assignments) {
//			serialNo = json.getString("text_1");
//		} else {
//			serialNo = json.getString("serialNo");
//		}
//		data.fluentPut(XbbField.Fail.form.getAttr(), xbbForm.getText())
//				.fluentPut(XbbField.Fail.data.getAttr(), serialNo)
//				.fluentPut(XbbField.Fail.content.getAttr(), operate.getText())
//				.fluentPut(XbbField.Fail.time.getAttr(), DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"))
//				.fluentPut(XbbField.Fail.execute.getAttr(), e.getMessage());
//		this.add(XbbForm.fail.getFormId(), data);
    }
}
