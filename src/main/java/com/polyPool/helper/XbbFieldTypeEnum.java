package com.polyPool.helper;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 字段类型枚举类 参考
 * @author wufeng
 * @date 2018/8/10 9:54
 */
@Deprecated
@Getter
public enum XbbFieldTypeEnum {
	//id,
	DATAID(0, "num"),
	//formId
	FORM_ID(0, "num"),

	ID(13, "num"),
	// 单行文本
	TEXT(1, "text"),
	//数字
	NUM(2, "num"),
	// 长整形留作业务使用
	LONG(5, "long"),
	// 长整形留作业务使用 -- 与data平级的系统级别预留字段，非自定义字段，对应SysAliasEnum枚举内占位的字段类型
	SYS_LONG(50, "long"),
	// 下拉框:combo
	COMBO(3, "text"),
	//逻辑下拉（如客户是否归档）
	COMBO_NUM(19, "num"),
	// DateTimeEnum
	DATETIME(4, "date"),
	// 图片image
	IMAGE(6, "file"),
	// 多行文本
	TEXTAREA(7, "text"),
	// 附件 upload
	UPLOAD(8, "file"),
	// 复选框组：checkboxgroup
	CHECKBOX_GROUP(9, "array"),
	//星级
	STAR(10, "num"),
	// address 地址
	ADDRESS(12, "address"),
	// 音频
	VIDEO(14, "file"),
	//开关
	SWITCH(15, "num"),
	// 员工选择
	SELECT_DEPARTMENT(16, "text"),
	//flowStatus
	FLOW_STATUS(17, "num"),
	//计数（用于统计）
	COUNT(18, "num"),
	//计算字段（用于统计）
	COMPUTED_FIELD(20, "num"),
	//生日
	BIRTHDAY(37, "text"),
	// radiogroup 单选按钮
	RADIO_GROUP(10000, "text"),
	// 下拉框复选：combocheck
	COMBO_CHECK(10001, "array"),
	// 分割线：separator
	SEPARATOR(10002, "other"),
	// location定位
	LOCATION(10003, "geo"),
	// 描述文本
	MEMO(10004, "text"),
	// 超链接
	HREF(10005, "file"),
	// 子表单 subform
	SUB_FORM(10006, "subForm"),
	// 关联查询 linkquery
	LINK_QUERY(10007, "text"),
	// 关联数据 linkdata
	LINK_DATA(10008, "text"),
	// 成员单选 user
	USER(10009, "text"),
	// 成员多选 usergroup
	USER_GROUP(10010, "array"),
	// dept 部门单选
	DEPT(10011, "text"),
	// deptgroup 部门多选
	DEPT_GROUP(10012, "array"),
	// 创建人
	CREATORID(10013, "text"),
	// 创建时间
	ADDTIME(10014, "date"),
	// 修改时间
	UPDATETIME(10015, "date"),
	// 所属部门
	DEPARTMENTID(10016, "num"),
	// 拥有者
	OWNERID(10017, "text"),
	// Synergetic协同人
	COUSERID(10018, "text"),
	// serial number 流水号
	SERIALNO(10019, "text"),
	//仓库
	WAREHOUSE_ID(10020, "num"),
	//预警类型
	WARINING_TYPE(10021, "num"),
	//保质状况
	GUARANTEE_STATUS(10022, "num"),
	//分类id 库存查询也用到了这个
	CATEGORY_ID(10023, "num"),

	TEAM_USER(10024, "text"),

	TEAM_USER_TYPE(10025, "text"),
	// 完成时间
	ENDTIME(10026, "date"),
	// 关闭时间
	CLOSETIME(10027, "date"),
	//抄送人
	COPY_USER(10028, "text"),
	// 其他费用
	SURCHARGE(10029, "num"),
	//阶段推进器字段
	STAGE_THRUSTER(10030, "text"),
	//TODO haibin.zhang 重了

	// =========================Saas特有控件==============================
	// 关联业务单选，比如选择关联客户（关联ID）
	LINK_BUSINESS_SINGLE(20001, "text"),
	// 关联业务多选，比如选择关联客户（多选）
	LINK_BUSINESS_MULTI(20002, "text"),
	// 联系电话
	CONTACT_NUMBER(20003, "subForm"),
	// 选择产品
	SELECT_PRODUCT(20004, "array"),
	// 勾选+下拉选择，比如访客计划中的执行人
	CHECK_COMBO(20005, "text"),
	// 下拉+关联业务选择，比如跟进记录的跟进业务
	COMBO_LINK_BUSINESS(20006, "text"),
	// 开关+文本显示，比如今日计划
	SWITCH_TEXT(20007, "subForm"),
	// 产品规格
	SPECIFICATION(20008, "text"),
	// 产品库存
	PRODUCT_STOCK(20009, "num"),
	// 文本后带勾选框
	TEXT_CHECK(20010, "text"),
	// 富文本编辑器
	RICH_TEXT_EDITOR(20011, "text"),
	// 关联业务+下拉，比如跟进记录的关联客户
	LINK_BUSINESS_COMBO(20012, "text"),
	// 关联新建
	LINK_FORM(20013, "array"),
	// 孤独子表单
	SINGLE_SUB_FORM(20014, "subForm"),
	// 工单超时
	WORK_ORDER_TIME_OUT(20015, "text"),
	WORK_ORDER_ROB(20016, "num"),
	//工单超时，么的办法了，曾经我也想做个好人
	WORK_ORDER_DATA_TIME_OUT(20017, "text"),
	WORK_ORDER_DATA_TIME_NOT_OUT(20018, "text"),
	//工单状态
	WORK_ORDER_STATUS(20021, "num"),
	//工单阶段
	WORK_ORDER_STAGE(20022, "num"),
	WORK_ORDER_ALL_OUTBOUND(20023, "num"),
	//多模板选择模板字段
	TEMPLATE(20024, "text"),
	//工单阶段状态
	WORK_ORDER_NODE_STATUS(20025, "num"),
	//最后跟进时间
	// LAST_CONNECT_TIME(20023, "date"),
	//跟进记录日志类型
	LOG_TYPE(20026, "num"),
	//工单阶段名称
	WORK_ORDER_STAGE_NAME(20027, "text"),
	PRE_OWNER_ID(20028, "text"),
	// 公海分组-省特殊选择
	PUBLIC_GROUP_PROVINCE(20029, "array"),
	// 级联选择
	CASCADER(20030, "array"),
	BATCH(20031, "array"),

	//saas列表操作filedType
	OPTION(30000, "option"),
	FOCUS(30001, "option"),
	REMIND(30002, "option"),
	CALL(30003, "option"),
	FLOW(30004, "option"),
	CONTRACT(30005, "option"),
	PRODUCT(30006, "option"),
	STOCK_SEARCH(30007, "option"),
	WAIT_INSTOCK_CONTRACT(30008, "option"),
	WAIT_OUTSTOCK_PRODUCT(30009, "option"),
	HEAD_RADIO_GROUP(30010, "option"),
	HEAD_CHECKBOX_GROUP(30011, "option"),
	WARNING_SEARCH(30012, "option"),
	RANGE_SCREEN(30013, "rangeScreen"),
	SEQ_TRACK(30014, "seqTrack"),
	SEQ(30015, "seq"),

	//库存 新的层级，面子够大
	//仓库
	STOCK_WAREHOUSE_ID(40001, "num"),
	WAREHOUSE_CHECKED(40002, "num"),
	WAREHOUSE_IS_NOTIFY(40003, "num"),
	ALL_WAREHOUSE_ALL_LIMIT(40004, "num"),
	ALL_WAREHOUSE_UPPER_LIMIT(40005, "num"),
	ALL_WAREHOUSE_LOWER_LIMIT(40006, "num"),
	CHECKED_WAREHOUSE_ALL_LIMIT(40007, "num"),
	CHECKED_WAREHOUSE_UPPER_LIMIT(40008, "num"),
	CHECKED_WAREHOUSE_LOWER_LIMIT(40009, "num"),
	BATCH_WAREHOUSE_ID(40010, "num"),
	BATCH_WAREHOUSE_CHECKED(40011, "num"),
	BATCH_PRODUCE_DATE(40012, "num"),
	BUSINESS_ID(40013, "num"),
	REF_ID(40014, "num"),
	STOCK_FLOW_SUB_FORM(40015, "subForm"),
	STOCK_FLOW_SHEET_DATE(40016, "date"),
	//业务类别筛选字段
	BUSINESS_TYPE(40017, "num"),

	// 流程用，我也不想加的
	CREATOR_DEPT(50001, "array"),

	//回款单内新增的两个字段类型
	SHEET_ALONE(60001, "num"),
	SHEET_UUID(60002, "text"),

	LINK_FORM_BUTTON(700001, "button"),

	LABEL(800000, "array"),
	//评分区间筛选字段
	SCORE_RANGE(800010, "num"),

	//应收账期
	ACCOUNT_PERIOD(700002, "text"),
	//系统字段sys_long_10，生成规则 1:手动创建应收 2:按开票产生应收 3:按出库产生应收
	SYS_LONG_10(700003, "num"),

	SOURCE(800001, "num"),

	//营销适用地区
	APPLICATION_AREA(850000, "array"),

	// 经销商
	//所在地区
	AREA(900001, "text"),
	//可采购商品
	PURCHASEABLE_GOODS(900002, "array"),
	//订货单选择收货人
	LINK_RECEIVER(900003, "text"),

	//对账日期
	CHECK_DATE(900010, "subForm"),

	//阶段比例特殊类型
	STAGE_RATIO(10031, "long"),

	//成员、部门筛选范围字段,),
	USER_NAME(900050, "array"),
	DEPT_NAME(900051, "array"),
	ROLE_NAME(900052, "array"),
	REGISTER_USER(900053, "text"),
	REGISTER_DEPT(900054, "array"),

	//悬浮 点击时会出现一个小窗口
	FLOAT(1000000, "text"),

	//工作报告阅读状态
	WORKREPORT_READ_STATUS(910000, "text"),
	;

	/**
	 * 字段类型编码
	 */
	private final Integer code;
	/**
	 * 字段实际类型
	 */
	private final String type;

	XbbFieldTypeEnum(Integer code, String type) {
		this.code = code;
		this.type = type;
	}

	public static XbbFieldTypeEnum getFieldTypeEnum(Integer type) {
		for (XbbFieldTypeEnum e : values()) {
			if (e.code.equals(type)) {
				return e;
			}
		}
		return null;
	}

	//	/**
	//	 * 根据类型进行分类返回
	//	 *
	//	 * @return 根据搜索和统计分为三种类型
	//	 */
	//	public static FieldTypeClassifyClass esaliasTypeList() {
	//		FieldTypeClassifyClass fieldTypeClassifyClass = new FieldTypeClassifyClass();
	//		//数字型的code集合
	//		List<Integer> numIdList = new ArrayList<>();
	//		List<FieldTypeEnum> numList = new ArrayList<>();
	//		//非搜索型的code集合（文件，连接）
	//		List<Integer> noSearchIdList = new ArrayList<>();
	//		List<FieldTypeEnum> noSearchList = new ArrayList<>();
	//		//可筛选的集合
	//		List<Integer> selectIdList = new ArrayList<>();
	//		List<FieldTypeEnum> selectList = new ArrayList<>();
	//		//人员归属的集合
	//		List<Integer> belongIdList = new ArrayList<>();
	//		List<FieldTypeEnum> belongList = new ArrayList<>();
	//		//时间类型的集合
	//		List<Integer> timeIdList = new ArrayList<>();
	//		List<FieldTypeEnum> timeList = new ArrayList<>();
	//
	//		for (FieldTypeEnum e : values()) {
	//			String esalias = e.getEsalias();
	//			Integer type = e.getCode();
	//			if ("other".equals(esalias) || "geo".equals(esalias)) {
	//				noSearchIdList.add(type);
	//				noSearchList.add(e);
	//			} else {
	//				//阶段比例本质也为数字字段
	//				boolean flag = numField(e, esalias);
	//				if (flag) {
	//					numIdList.add(type);
	//					numList.add(e);
	//				} else if ("date".equals(esalias)) {
	//					timeIdList.add(type);
	//					timeList.add(e);
	//				}
	//				selectIdList.add(type);
	//				selectList.add(e);
	//			}
	//		}
	//		belongList.add(CREATORID);
	//		belongList.add(OWNERID);
	//		belongList.add(COUSERID);
	//		belongList.add(USER);
	//		belongList.add(USER_GROUP);
	//		for (FieldTypeEnum fieldTypeEnum : belongList) {
	//			belongIdList.add(fieldTypeEnum.getCode());
	//		}
	//		fieldTypeClassifyClass.setNoSearchIdList(noSearchIdList);
	//		fieldTypeClassifyClass.setNoSearchList(noSearchList);
	//		fieldTypeClassifyClass.setSelectIdList(selectIdList);
	//		fieldTypeClassifyClass.setSelectList(selectList);
	//		fieldTypeClassifyClass.setNumIdList(numIdList);
	//		fieldTypeClassifyClass.setNumList(numList);
	//		fieldTypeClassifyClass.setTimeIdList(timeIdList);
	//		fieldTypeClassifyClass.setTimeList(timeList);
	//		fieldTypeClassifyClass.setBelongIdList(belongIdList);
	//		fieldTypeClassifyClass.setBelongList(belongList);
	//		return fieldTypeClassifyClass;
	//	}

	/**
	 * 需要二次解析的枚举
	 * @return
	 */
	public static List<Integer> getExplainFieldTypeEnumType() {
		List<Integer> list = new ArrayList<>();
		list.add(RADIO_GROUP.getCode());
		list.add(COMBO.getCode());
		list.add(COMBO_CHECK.getCode());
		return list;
	}

	/**
	 * 判断是否子表单类型
	 * @param type 类型参数
	 * @return 是否为子表单
	 */
	public static boolean isSubFormType(Integer type) {
		XbbFieldTypeEnum xbbFieldTypeEnum = getFieldTypeEnum(type);
		String esAlias = "";
		if (xbbFieldTypeEnum != null) {
			esAlias = xbbFieldTypeEnum.type;
		}
		return Objects.equals(esAlias, SUB_FORM.type);
	}

	/**
	 * 判断是否数组类型
	 * @param type 类型参数
	 * @return 是否为子表单
	 */
	public static boolean isArrayType(Integer type) {
		XbbFieldTypeEnum xbbFieldTypeEnum = getFieldTypeEnum(type);
		String esAlias = "";
		if (xbbFieldTypeEnum != null) {
			esAlias = xbbFieldTypeEnum.type;
		}
		return Objects.equals(esAlias, CHECKBOX_GROUP.type);
	}

	/**
	 * 判断是否文本类型
	 * @param type 类型参数
	 * @return 是否为文本
	 */
	public static boolean isTextType(Integer type) {
		XbbFieldTypeEnum xbbFieldTypeEnum = getFieldTypeEnum(type);
		String esAlias = "";
		if (xbbFieldTypeEnum != null) {
			esAlias = xbbFieldTypeEnum.type;
		}
		return Objects.equals(esAlias, TEXT.type);
	}

	public static boolean isArray(Integer type) {
		return isArrayType(type) || Objects.equals(XbbFieldTypeEnum.IMAGE.getCode(), type);
	}

	public static boolean isJsonObject(Integer type) {
		return Objects.equals(XbbFieldTypeEnum.ADDRESS.getCode(), type) || Objects.equals(XbbFieldTypeEnum.LOCATION.getCode(), type);
	}

	/**
	 * 需要跳过的字段（记录日志时，某些字段的变更无法记录，如图片、附件等）
	 * @param fieldType 被判断的字段的类型
	 * @return boolean
	 * @author zcp
	 * @date 2019/7/2 21:39
	 * @version v1.0
	 * @since v1.0
	 */
	public static boolean filterType(Integer fieldType) {
		return fieldType == null || Objects.equals(DATAID.code, fieldType) || Objects.equals(IMAGE.code, fieldType) || Objects.equals(UPLOAD.code, fieldType)
				|| Objects.equals(VIDEO.code, fieldType) || Objects.equals(COUNT.code, fieldType) || Objects.equals(SEPARATOR.code, fieldType) || Objects.equals(HREF.code, fieldType)
				|| Objects.equals(LINK_QUERY.code, fieldType) || Objects.equals(CREATORID.code, fieldType) || Objects.equals(ADDTIME.code, fieldType)
				|| Objects.equals(TEAM_USER.code, fieldType) || Objects.equals(TEAM_USER_TYPE.code, fieldType) || Objects.equals(SELECT_PRODUCT.code, fieldType) || Objects.equals(CHECK_COMBO.code, fieldType)
				|| Objects.equals(LINK_FORM.code, fieldType) || Objects.equals(WORK_ORDER_TIME_OUT.code, fieldType) || Objects.equals(WORK_ORDER_ROB.code, fieldType)
				|| Objects.equals(WORK_ORDER_DATA_TIME_OUT.code, fieldType) || Objects.equals(WORK_ORDER_DATA_TIME_NOT_OUT.code, fieldType) || Objects.equals(WORK_ORDER_STATUS.code, fieldType) || Objects.equals(WORK_ORDER_STAGE.code, fieldType)
				|| Objects.equals(WORK_ORDER_ALL_OUTBOUND.code, fieldType) || Objects.equals(WORK_ORDER_NODE_STATUS.code, fieldType) || Objects.equals(TEMPLATE.code, fieldType)
				|| Objects.equals(OPTION.code, fieldType) || Objects.equals(FOCUS.code, fieldType) || Objects.equals(REMIND.code, fieldType) || Objects.equals(CALL.code, fieldType) || Objects.equals(FLOW.code, fieldType)
				|| Objects.equals(CONTRACT.code, fieldType) || Objects.equals(PRODUCT.code, fieldType) || Objects.equals(STOCK_SEARCH.code, fieldType) || Objects.equals(WAIT_INSTOCK_CONTRACT.code, fieldType)
				|| Objects.equals(WAIT_OUTSTOCK_PRODUCT.code, fieldType) || Objects.equals(HEAD_RADIO_GROUP.code, fieldType) || Objects.equals(HEAD_CHECKBOX_GROUP.code, fieldType) || Objects.equals(STOCK_WAREHOUSE_ID.code, fieldType)
				|| Objects.equals(WAREHOUSE_CHECKED.code, fieldType) || Objects.equals(WAREHOUSE_IS_NOTIFY.code, fieldType) || Objects.equals(ALL_WAREHOUSE_ALL_LIMIT.code, fieldType) || Objects.equals(ALL_WAREHOUSE_UPPER_LIMIT.code, fieldType)
				|| Objects.equals(ALL_WAREHOUSE_LOWER_LIMIT.code, fieldType) || Objects.equals(CHECKED_WAREHOUSE_ALL_LIMIT.code, fieldType) || Objects.equals(CHECKED_WAREHOUSE_UPPER_LIMIT.code, fieldType) || Objects.equals(CHECKED_WAREHOUSE_LOWER_LIMIT.code, fieldType)
				|| Objects.equals(BATCH_WAREHOUSE_ID.code, fieldType) || Objects.equals(BATCH_WAREHOUSE_CHECKED.code, fieldType);
	}

	//	public String getDescription() {
	//		return I18nMessageUtil.getMessage(description);
	//	}

	/**
	 * 高级版可用字段
	 * @return
	 */
	public static List<Integer> getComplexList() {
		return Arrays.asList(XbbFieldTypeEnum.SUB_FORM.getCode(), XbbFieldTypeEnum.LINK_DATA.getCode(), XbbFieldTypeEnum.USER.getCode(), XbbFieldTypeEnum.USER_GROUP.getCode(), XbbFieldTypeEnum.DEPT.getCode(),
				XbbFieldTypeEnum.DEPT_GROUP.getCode());
	}

	/**
	 * 旗舰版所有可用字段，包含高级版
	 * @return
	 */
	public static List<Integer> getUltimateList() {
		List<Integer> list = new ArrayList<>(getComplexList());
		List<Integer> ultimateList = new ArrayList<>(getUltimatePrivateList());
		list.addAll(ultimateList);
		return list;
	}

	/**
	 * 旗舰版可用字段，不含用高级版的字段
	 * @return
	 */
	public static List<Integer> getUltimatePrivateList() {
		return Collections.singletonList(XbbFieldTypeEnum.STAGE_THRUSTER.getCode());
	}

	public static List<Integer> getComputedType() {
		return Arrays.asList(XbbFieldTypeEnum.ADDTIME.getCode(),
				XbbFieldTypeEnum.UPDATETIME.getCode(),
				XbbFieldTypeEnum.CLOSETIME.getCode(),
				XbbFieldTypeEnum.ENDTIME.getCode(),
				XbbFieldTypeEnum.DATETIME.getCode(),
				XbbFieldTypeEnum.STOCK_FLOW_SHEET_DATE.getCode(),
				XbbFieldTypeEnum.NUM.getCode());
	}

	//	public static List<String> getDateSpecialAlias(){
	//		return Arrays.asList(FieldTypeEnum.ADDTIME.getAlias(),
	//				FieldTypeEnum.UPDATETIME.getAlias(),
	//				FieldTypeEnum.CLOSETIME.getAlias(),
	//				FieldTypeEnum.ENDTIME.getAlias(),
	//				FieldTypeEnum.STOCK_FLOW_SHEET_DATE.getAlias());
	//	}

	public static List<Integer> getUserList() {
		return Arrays.asList(XbbFieldTypeEnum.USER.getCode(), XbbFieldTypeEnum.USER_GROUP.getCode(), XbbFieldTypeEnum.OWNERID.getCode(), XbbFieldTypeEnum.COUSERID.getCode(), XbbFieldTypeEnum.CREATORID.getCode());
	}

	public static List<Integer> getDeptList() {
		return Arrays.asList(XbbFieldTypeEnum.DEPT.getCode(), XbbFieldTypeEnum.DEPT_GROUP.getCode(), XbbFieldTypeEnum.DEPARTMENTID.getCode(), XbbFieldTypeEnum.CREATOR_DEPT.getCode());
	}

	/**
	 * 判断字段类似是否是数字类型
	 * num、阶段比例类型都为数字
	 * @param fieldType
	 * @return boolean
	 * @author zcp
	 * @version v1.0
	 * @since v1.0
	 */
	public static boolean numFieldType(Integer fieldType) {
		return Objects.equals(NUM.code, fieldType) || Objects.equals(STAGE_RATIO.code, fieldType);
	}

	/**
	 * 判断字段类似是否是数字类型
	 * num、阶段比例类型都为数字
	 * @param xbbFieldTypeEnum
	 * @param esalias
	 * @return boolean
	 * @author zcp
	 * @version v1.0
	 * @since v1.0
	 */
	private static boolean numField(XbbFieldTypeEnum xbbFieldTypeEnum, String esalias) {
		return ("num".equals(esalias) && Objects.equals(NUM, xbbFieldTypeEnum)) || ("long".equals(esalias) && Objects.equals(STAGE_RATIO, xbbFieldTypeEnum));
	}
}


