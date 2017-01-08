package com.yuzhai.util;

import com.google.gson.Gson;

/**
 * 创建时间2016/5/14.
 * 作者 HJF
 * 主要功能：JSON数据的解析
 */

public class JsonUtil {
    public static <T> T decodeByGson(String json, Class<T> cls) {
        Gson gson = new Gson();
        return gson.fromJson(json, cls);
    }

    public static String codeByGson(Object src) {
        Gson gson = new Gson();
        return gson.toJson(src);
    }
}
