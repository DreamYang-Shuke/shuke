package com.polyPool.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.polyPool.api.XbbApi;
import com.polyPool.helper.XbbField;
import com.polyPool.helper.XbbForm;
import com.polyPool.model.XbbDetail;
import com.polyPool.model.XbbIPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * @date 2022-06-22 16:23
 */
@Service
public class ConvertServiceImpl implements ConvertService {

	@Autowired
	private XbbApi xbbApi;

	/**
	 * 销帮帮字段编码
	 * 只实现简单的转换
	 * @param json
	 * @param formId
	 * @return
	 */
	@Override
	public JSONObject xbbFieldEncode(JSONObject json, long formId) {
		XbbField[] xbbField = getFieldByFormId(formId);
		JSONObject result = new JSONObject();
		for (XbbField item : xbbField) {
			String type = item.getType();
			Object value = json.get(item.getName());
			if ("4".equals(type) && value != null) {
				if (value instanceof Number) {
					long l = ((Number) value).longValue();
					if (l > 9999999999L) {
						l = l / 1000;
					}
					value = l;
				} else if (value instanceof List) {
					List list = (List) value;
					int[] arr = new int[7];
					for (int i = 0; i < arr.length; i++) {
						Object o = i < list.size() ? list.get(i) : 0;
						arr[i] = (Integer) o;
					}
					LocalDateTime time = LocalDateTime.of(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6]);
					value = time.toEpochSecond(ZoneOffset.of("+8"));
				}
			}
			result.put(item.getAttr(), value);
		}
		return result;
	}

	/**
	 * 销帮帮字段解析
	 * @param iPage
	 * @param formId
	 */
	@Override
	public void xbbFieldDecode(XbbIPage iPage, long formId) {
		XbbField[] xbbField = getFieldByFormId(formId);
		List<XbbDetail> list = iPage.getList();
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (XbbDetail xbbDetail : list) {
			JSONObject data = xbbDetail.getData();
			if (data == null) {
				continue;
			}
			JSONObject jsonObject = JSONObject.parseObject(data.toString());
			xbbDetail.setSrcData(jsonObject);
			data = xbbFieldDecode(data, xbbField);
			xbbDetail.setData(data);
		}
	}

	/**
	 * 销帮帮字段解析
	 * @param xbbDetail
	 * @param formId
	 */
	@Override
	public void xbbFieldDecode(XbbDetail xbbDetail, long formId) {
		if (xbbDetail == null) {
			return;
		}
		JSONObject data = xbbDetail.getData();
		if (data == null) {
			return;
		}
		JSONObject jsonObject = JSONObject.parseObject(data.toString());
		xbbDetail.setSrcData(jsonObject);
		XbbField[] xbbField = getFieldByFormId(formId);
		data = xbbFieldDecode(data, xbbField);
		xbbDetail.setData(data);
	}

	@Override
	public JSONObject xbbConvertSelect(JSONObject json, long formId) {
		XbbField[] xbbField = getFieldByFormId(formId);
		JSONObject result = new JSONObject();
		for (XbbField field : xbbField) {
			String type = field.getType().split("\\.")[0];
			String attr = field.getAttr().split("\\.")[0];
			String[] typeArr = type.split("-");
			type = typeArr[0];
			if (StringUtils.isEmpty(attr)) {
				continue;
			}
			
			if ("3".equals(type) || "10000".equals(type) || "03".equals(type) || "010000".equals(type)) {
				try {
					Object value = json.get(attr);
					if (value instanceof JSONObject) {
						json.fluentPut(attr, ((JSONObject) value).getString("text"));
					}
				} catch (Exception ignored) {
				}
			}
			if ("10006".equals(type) || "010006".equals(type)) {
				JSONArray subForm = json.getJSONArray(attr);
				if (!CollectionUtils.isEmpty(subForm)) {
					long subFormId = Long.parseLong(typeArr[1]);
					for (int i = 0; i < subForm.size(); i++) {
						JSONObject form = subForm.getJSONObject(i);
						form = xbbConvertSelect(form, subFormId);
						subForm.set(i, form);
					}
				}
			}
			if ("10009".equals(type)) {
				JSONObject item = json.getJSONObject(attr);
				if (item != null) {
					json.put(attr, item.getString("id"));
				}
			}
			result.put(attr, json.get(attr));
		}
		return result;
	}

	/**
	 * 销帮帮字段解析
	 * @param json
	 * @param xbbField
	 * @return
	 */
	private JSONObject xbbFieldDecode(JSONObject json, XbbField[] xbbField) {
		JSONObject result = new JSONObject(true);
		for (XbbField item : xbbField) {
			String attr = item.getAttr();
			String type = item.getType();

			String[] attrArr = attr.split("\\.");
			String[] typeArr = type.split("\\.");

			Object value = json;
			for (int i = 0; i < attrArr.length; i++) {
				String attrSub = attrArr[i];
				String typeSub = typeArr[i];
				Object val = ((JSONObject) value).get(attrSub);
				value = xbbFieldDecode(val, (JSONObject) value, typeSub, item);
				if (val == null) {
					break;
				}
			}

			if (value == null) {
				continue;
			}
			
			String name = item.getName();
			String[] nameArr = name.split("\\.");
			JSONObject tmp = result;
			for (int i = 0; i < nameArr.length - 1; i++) {
				String str = nameArr[i];
				Object val = tmp.get(str);
				if (val == null) {
					val = new JSONObject(true);
					tmp.put(str, val);
				}
				tmp = (JSONObject) val;
			}

			String lastName = nameArr[nameArr.length - 1];
			boolean isArr = false;
			if (lastName.endsWith("[]")) {
				lastName = lastName.substring(0, lastName.length() - 2);
				isArr = true;
			}
			if (isArr) {
				JSONArray array = tmp.getJSONArray(lastName);
				if (array == null) {
					array = new JSONArray();
					tmp.put(lastName, array);
				}
				array.addAll((JSONArray) value);
			} else {
				tmp.put(lastName, value);
			}
		}
		return result;
	}

	/**
	 * 销帮帮字段解析
	 * @param o
	 * @param type
	 * @param item
	 * @return
	 */
	private Object xbbFieldDecode(Object o, JSONObject value, String type, XbbField item) {
		if ("-1".equals(type)) {
			//固定值
			return item.getAttr();
		}
		if (o == null) {
			return null;
		}
		String[] typeArr = type.split("-");
		type = typeArr[0];
		switch (type) {
			case "1"://文本
			case "2"://数字
			case "7"://多行文本
			case "10003"://定位
			case "100030"://经纬度
			case "10009"://成员单选
			case "10019"://serial number 流水号
				break;
			case "4"://时间
				o = ((Number) o).longValue() * 1000;
				break;
			case "3"://下拉选择
			case "10000"://单选按钮
				o = XbbField.Dict.getNum(item, (JSONObject) o);
				break;
			case "03"://下拉选择
				o = XbbField.Dict.getNum(item, new JSONObject().fluentPut("value", o));
				break;
//			case "010000"://单选按钮，boolean
//				Integer num = XbbField.Dict.getNum(item, (JSONObject) o);
//				if (num == null) {
//					o = null;
//				} else {
//					o = (num == 1);
//				}
//				break;
			case "6"://图片
				String format = XbbField.Dict.getValue(item, null);
				Assert.notNull(format, "字段配置错误：" + item.getAttr());
				JSONArray array = null;
				if (o instanceof JSONArray) {
					array = (JSONArray) o;
				} else {
					array = new JSONArray().fluentAdd(o);
				}
				JSONArray arr = new JSONArray();
				for (int i = 0; i < array.size(); i++) {
					String url = array.getString(i);
					int idx = url.lastIndexOf("?");
					if (idx > -1) {
						url = url.substring(0, idx);
					}
					arr.add(JSONObject.parse(String.format(format, url)));
				}
				o = arr;
				break;
			case "08"://附件
				Object obj = xbbFieldDecode(o, value, "8", item);
				o = xbbFieldDecode(obj, value, "6", item);
				break;
			case "8"://附件
				if (o instanceof JSONArray) {
					JSONArray array1 = (JSONArray) o;
					JSONArray arr1 = new JSONArray();
					for (int i = 0; i < array1.size(); i++) {
						JSONObject json = array1.getJSONObject(i);
						String url = json.getString("attachIndex");
						int idx = url.lastIndexOf("?");
						if (idx > -1) {
							url = url.substring(0, idx);
						}
						arr1.add(url);
					}
					o = arr1;
				}
				break;
			case "12"://地址
				JSONObject addr = (JSONObject) o;
				o = doNull(addr.getString("province"))
						+ "-" + doNull(addr.getString("city"))
						+ "-" + doNull(addr.getString("district"))
						+ "-" + doNull(addr.getString("address"));
				break;
			case "010006"://子表单 数组
			case "10006"://子表单 数组
				o = xbbFieldDecodeSubForm((JSONArray) o, typeArr);
				break;
			case "10008":
				//关联数据
				XbbDetail xbbDetail = xbbApi.get(toLong(o), false, false);
				o = xbbDetail == null ? null : xbbDetail.getData();
				break;
			case "010008":
				//关联工单数据
				XbbDetail workOrder = xbbApi.get(toLong(o), true, false);
				o = workOrder == null ? null : workOrder.getData();
				break;
		}
		return o;
	}

	/**
	 * 解析子表单
	 * @param array
	 * @param typeArr
	 * @return
	 */
	private JSONArray xbbFieldDecodeSubForm(JSONArray array, String[] typeArr) {
		JSONArray result = new JSONArray();
		long subFormId = Long.parseLong(typeArr[1]);
		XbbField[] xbbFields = getFieldByFormId(subFormId);
		for (int i = 0; i < array.size(); i++) {
			JSONObject json = array.getJSONObject(i);
			if (json == null) {
				continue;
			}
			result.add(this.xbbFieldDecode(json, xbbFields));
		}
		if ("010006".equals(typeArr[0])) {
			//安装信息的附件 处理
			JSONArray tmp = new JSONArray();
			for (int i = 0; i < result.size(); i++) {
				JSONObject attJson = result.getJSONObject(i);
				String key = "attachmentType";
				JSONArray arr = attJson.getJSONArray("item");
				Integer attachmentType = attJson.getInteger(key);
				if (attachmentType != null) {
					for (int j = 0; j < arr.size(); j++) {
						JSONObject item = arr.getJSONObject(j);
						item.put(key, attachmentType);
						tmp.add(item);
					}
				}
			}
			result = tmp;
		}
		return result;
	}
	
	/**
	 * 根据formId获取字段列表
	 * @param formId
	 * @return
	 */
	private XbbField[] getFieldByFormId(long formId) {
		XbbForm form = XbbForm.getByFormId(formId);
		Assert.notNull(form, "表单未配置");
		Class cls = form.getXbbField();
		return (XbbField[]) cls.getEnumConstants();
	}

	/**
	 * null转成空字符串
	 * @param str
	 * @return
	 */
	private String doNull(String str) {
		return str == null ? "" : str;
	}

	/**
	 * 转换值为Long类型
	 * @param o
	 * @return
	 */
	private Long toLong(Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof Long) {
			return (Long)o;
		}
		if (o instanceof Number) {
			return ((Number) o).longValue();
		}
		return Long.valueOf(o.toString());
	}
}
