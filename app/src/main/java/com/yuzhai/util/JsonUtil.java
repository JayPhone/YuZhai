package com.yuzhai.util;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 创建时间2016/5/14.
 * 作者 HJF
 * 主要功能：JSON数据的解析
 */

public class JsonUtil {

    public static String decodeJson(String jsonString, String key) {
        String data;
        try {
            if (jsonString != null) {
                JSONObject jsonObject = new JSONObject(jsonString);
                data = jsonObject.getString(key);
                return data;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String gsonToString(Object toString) {
        if (toString != null) {
            String jsonString;
            Gson gson = new Gson();
            jsonString = gson.toJson(toString);
            return jsonString;
        }
        return null;
    }
}
