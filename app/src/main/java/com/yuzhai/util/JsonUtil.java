package com.yuzhai.util;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建时间2016/5/14.
 * 作者 HJF
 * 主要功能：JSON数据的解析
 */

public class JsonUtil {

    public static Map<String, Object> decodeResponseForJobDetail(String jsonString) {
        JSONObject jsonObject = null;
        Map<String, Object> data = null;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONObject jsonObject1 = jsonObject.getJSONObject("publish");
            JSONArray jsonArray1 = jsonObject.getJSONArray("pictures");
            List<String> tempPicture = new ArrayList<>();
            for (int i = 0; i < jsonArray1.length(); i++) {
                tempPicture.add(jsonArray1.getJSONObject(i).getString("picturePath"));
            }
            data = new HashMap<>();
            data.put("date", jsonObject1.getString("date"));
            data.put("currentstatu", jsonObject1.getString("currentstatus"));
            data.put("price", jsonObject1.getString("money"));
            data.put("tel", jsonObject1.getString("tel"));
            data.put("name", jsonObject1.getString("title"));
            data.put("descript", jsonObject1.getString("descript"));
            data.put("limit", jsonObject1.getString("deadline"));
            data.put("publishId", jsonObject1.getString("publishId"));
            data.put("pictures", tempPicture);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static <T> T decodeByGson(String json, Class<T> cls) {
        Gson gson = new Gson();
        return gson.fromJson(json, cls);
    }

    public static String codeByGson(Object src) {
        Gson gson = new Gson();
        return gson.toJson(src);
    }
}
