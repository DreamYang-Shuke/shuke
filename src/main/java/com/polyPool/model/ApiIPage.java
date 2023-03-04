package com.polyPool.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.polyPool.api.XbbApi;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 标准分页返回
 */
@Data
public class ApiIPage implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 返回数据对象 */
    private List<Object> result;
    /** 页码 */
    private Integer page_index = 1;
    /** 页大小 */
    private Integer page_size = 50;
    /** 总条数 */
    private Integer data_count;
    /** 总页数 */
    private Integer page_count;
    /** 是否有下一页 */
    private Boolean has_next;

    public static ApiIPage xbbToJst(XbbIPage data, Integer pageNo, Integer pageSize) {
        ApiIPage iPage = new ApiIPage();
        iPage.page_index = pageNo;
        iPage.page_size = pageSize;
        if (data != null) {
            List<XbbDetail> list = data.getList();
            JSONArray array = new JSONArray();
            if (!CollectionUtils.isEmpty(list)) {
                for (XbbDetail xbbDetail : list) {
                    array.add(xbbDetail.getData());
                }
            }
            iPage.result = array;
            iPage.data_count = data.getTotalCount();
        }
        return iPage;
    }
    
    public List<JSONObject> getResult() {
        List<JSONObject> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object o : result) {
                list.add((JSONObject)o);
            }
        }
        return list;
    }
    
    public boolean hasNext() {
        return Boolean.TRUE.equals(this.has_next);
    }
}