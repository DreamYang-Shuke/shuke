package com.polyPool.helper;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

import java.util.Objects;

/**
 * @date 2022-07-01 13:04
 */
public interface XbbField {

	String getAttr();

	String getType();

	String getName();

	/**
	 * 阿维塔派单
	 */
	@Getter
	enum Product implements XbbField {
		;

		private final String attr;
		private final String type;
		private final String name;

		Product(String attr, String type, String name) {
			this.attr = attr;
			this.type = type;
			this.name = name;
		}
	}

	/**
	 */
	@Getter
	enum Contract implements XbbField {
        shop_buyer_id("shop_buyer_id","","shop_buyer_id"),
        CONTRACT_NO("serialNo", "010008.10008.1", "so_id"),
        NAME("serialNo", "4", "so_id"),
        SIGN_TIME("date_1", "1", "order_date"),
        //仓库
        wms_co_id("text_15","","wms_co_id"),
        //快递公司
        logistics_company("text_16", "3", "logistics_company"),
        //快递单号
        logistics_no("text_28", "3", "l_id"),
        //买家留言
        buyer_message("text_23", "1", "buyer_message"),
        //订单备注；卖家备注
        remark("text_24", "1", "remark"),
        //收件信息-收件人
        receiver_name("text_17", "1", "receiver_name"),
        //手机
        receiver_mobile("subForm_1", "1", "receiver_mobile"),
        //收货地址
        receiver_address("address_1", "1", "receiver_address"),
        //商品编码，对应普通商品资料页面商品编码，对应库存管理的SKU
        sku_id("array_4", "1", "sku_id"),
        //数量
        qty("num_26", "1", "qty"),
        //运费，保留两位小数，单位（元）
        freight("num_12", "1", "freight"),
        //应付金额，保留两位小数，单位（元）
        pay_amount("num_1", "1", "pay_amount"),
        //聚水潭订单状态
        // 待付款：WaitPay；
        // 发货中：Delivering；
        // 被合并：Merged；
        // 异常：Question；
        // 被拆分：Split；
        // 等供销商|外仓发货：WaitOuterSent；
        // 已付款待审核：WaitConfirm；
        // 已客审待财审：WaitFConfirm；
        // 已发货：Sent；
        // 取消：Cancelled
        status("text_6", "1", "status"),
        //实际支付金额
        paid_amount("num_6", "1", "paid_amount"),
        send_date("date_3","","send_date"),
        
        ;

		private final String attr;
		private final String type;
		private final String name;
        
        Contract(String attr, String type, String name) {
			this.attr = attr;
			this.type = type;
			this.name = name;
		}
	}
    /**
     */
    @Getter
    enum Customer implements XbbField {
        
        ;
        
        private final String attr;
        private final String type;
        private final String name;
        
        Customer(String attr, String type, String name) {
            this.attr = attr;
            this.type = type;
            this.name = name;
        }
    }
    
    /**
     * 下拉选择、单选 翻译
     */
    @Getter
    enum Dict {
        _1_1("text_15", "7ee00778-77f8-c382-298d-c599bfe31223", 10396153, "TW上海仓库"),
        _1_2("text_15", "7083025e-d754-ca43-245c-ada38092c696", 10396401, "TW北京仓库"),
        _1_3("text_15", "ccd68434-88b2-2b8b-08b6-55992bc0e44e", 11833602, "广州补膜仓"),
        _1_4("text_15", "3ce6bfd1-80d2-5464-62a3-5aeaed42d360", 10880594, "Teckwrap仓库"),
        _1_5("text_15", "9b65479d-06d5-54ba-bb62-724c41f640ea", 12280652, "成都区域仓"),
    
    
    
        _2_1("text_8","016247284335481266",12627316,"赖嘉毅"),
        _2_2("text_8","105852375224086513",10947455,"张博士"),
        _2_3("text_8","122161021726593131",13501053,"李金华"),
        _2_4("text_8","306834024420923513",11512595,"刘国达"),
        _2_5("text_8","143146066927458427",10947459,"沈小欢"),
        _2_6("text_8","2744575016756120",12723342,"孙科"),
        _2_7("text_8","133451274821782476",11510979,"吴英浩"),//TW-D
        _2_8("text_8","1431460668859374",10947453,"杨靖"),
        _2_9("text_8","01435955251221369495",13696604,"吴俊凭1"),
    
        _3_1("text_29","eace9df0-3504-b2f6-66e5-3ccc6f96e9cb",0,"中行秃鹰公帐"),
        _3_2("text_29","fc74ddf9-70b0-e2d7-58a7-e821c8d08d45",0,"秃鹰微信公账"),
        _3_3("text_29","b6b02d38-4812-11b3-7f93-4b427e509f28",0,"TW支付宝"),
        _3_4("text_29","36969a9c-dbe0-be55-2628-f6e9e5e5ad3c",0,"快速支付"),
        _3_5("text_29","bcc7964b-6a3e-351a-76f2-50416b66069f",0,"HSBC-HK (TECKWRAP)"),
        _3_6("text_29","be6a72ee-3e13-9af2-b383-4ce25df2b619",0,"PAYPAL TECKWRAP"),
        ;
        
        
        private final String type;
        private final String value;
        private final Integer num;
        private final String name;
        
        Dict(String type, String value, int num, String name) {
            this.type = type;
            this.value = value;
            this.num = num;
            this.name = name;
        }
        
        public static String getName(XbbField xbbField, Integer num) {
            String type = xbbField.getClass().getSimpleName() + "_" + xbbField.getAttr();
            for (Dict dict : values()) {
                if ( Objects.equals(dict.type, type) && (num == null || Objects.equals(dict.num, num))) {
                    return dict.name;
                }
            }
            return null;
        }
        
        public static String getValue(XbbField xbbField, Integer num) {
            String type = xbbField.getClass().getSimpleName() + "_" + xbbField.getAttr();
            for (Dict dict : values()) {
                if (Objects.equals(dict.type, type) && (num == null || Objects.equals(dict.num, num))) {
                    return dict.value;
                }
            }
            return null;
        }
    
        public static Integer getNum(String type, String value) {
            for (Dict dict : values()) {
                if (Objects.equals(dict.type, type) && (value == null || Objects.equals(dict.value, value))) {
                    return dict.num;
                }
            }
            return null;
        }
        
        public static Integer getNum(XbbField xbbField, JSONObject jsonObject) {
            if (jsonObject == null) {
                return null;
            }
            String type = xbbField.getClass().getSimpleName() + "_" + xbbField.getAttr();
            for (Dict dict : values()) {
                if (Objects.equals(dict.type, type) && Objects.equals(dict.value, jsonObject.getString("value"))) {
                    return dict.num;
                }
            }
            return null;
        }
    
        public static Integer getName(String name) {
            if (name == null) {
                return null;
            }
            for (Dict dict : values()) {
                if (Objects.equals(dict.name, name)) {
                    return dict.num;
                }
            }
            return null;
        }
    }
}
