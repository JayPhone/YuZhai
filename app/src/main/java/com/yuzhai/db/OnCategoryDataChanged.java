package com.yuzhai.db;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/1.
 */
public interface OnCategoryDataChanged {
    void onCategoryDataInserted(List<Map<String,Object>> categoryData);

    void OnCategoryDataRemove(List<Map<String, String>> categoryData, int removeIndex);
}
