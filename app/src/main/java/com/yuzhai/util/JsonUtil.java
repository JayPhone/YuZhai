package com.yuzhai.util;

import com.google.gson.Gson;
import com.yuzhai.yuzhaiwork.R;

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

    public static JSONObject decodeToJsonObject(String jsonString, String key) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        jsonObject = jsonObject.getJSONObject(key);
        return jsonObject;
    }

    public static JSONArray decodeToJsonArray(String jsonString, String key) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray(key);
        return jsonArray;
    }

    public static List<Map<String, Object>> decodeResponseForJob(String jsonString, int type) {
        int[] typeImages = new int[]{R.drawable.it, R.drawable.music, R.drawable.design, R.drawable.movie, R.drawable.game, R.drawable.write, R.drawable.calculate};
        List<Map<String, Object>> datas = new ArrayList<>();
        try {
            JSONArray jsonArray = decodeToJsonArray(jsonString, "order");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                JSONObject jsonObject2 = jsonObject1.getJSONObject("publish");
                JSONArray jsonArray1 = jsonObject1.getJSONArray("pictures");
                List<String> tempPicture = new ArrayList<>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    tempPicture.add(jsonArray1.getJSONObject(j).getString("picturePath"));
                }
                Map<String, Object> data = new HashMap<>();
                data.put("image", typeImages[type]);
                data.put("date", jsonObject2.getString("date"));
                data.put("currentstatu", jsonObject2.getString("currentstatus"));
                data.put("price", jsonObject2.getString("money"));
                data.put("tel", jsonObject2.getString("tel"));
                data.put("name", jsonObject2.getString("title"));
                data.put("descript", jsonObject2.getString("descript"));
                data.put("limit", jsonObject2.getString("deadline"));
                data.put("publishId", jsonObject2.getString("publishId"));
                data.put("pictures", tempPicture);
                datas.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }

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

    public static List<Map<String, Object>> decodeResponseForPublished(String jsonString) {
        List<String> typeIndex = new ArrayList<>();
        typeIndex.add("软件IT");
        typeIndex.add("音乐制作");
        typeIndex.add("平面设计");
        typeIndex.add("视频拍摄");
        typeIndex.add("游戏研发");
        typeIndex.add("文案撰写");
        typeIndex.add("金融会计");
        int[] typeImages = new int[]{R.drawable.it, R.drawable.music, R.drawable.design, R.drawable.movie, R.drawable.game, R.drawable.write, R.drawable.calculate};
        List<Map<String, Object>> datas = new ArrayList<>();
        try {
            JSONArray jsonArray = decodeToJsonArray(jsonString, "order");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                JSONObject jsonObject2 = jsonObject1.getJSONObject("publish");
                JSONArray jsonArray1 = jsonObject1.getJSONArray("pictures");
                List<String> tempPicture = new ArrayList<>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    tempPicture.add(jsonArray1.getJSONObject(j).getString("picturePath"));
                }
                Map<String, Object> data = new HashMap<>();
                data.put("image", typeImages[typeIndex.indexOf(jsonObject2.getString("type"))]);
                data.put("date", jsonObject2.getString("date"));
                data.put("currentstatu", jsonObject2.getString("currentstatus"));
                data.put("price", jsonObject2.getString("money"));
                data.put("tel", jsonObject2.getString("tel"));
                data.put("name", jsonObject2.getString("title"));
                data.put("descript", jsonObject2.getString("descript"));
                data.put("limit", jsonObject2.getString("deadline"));
                data.put("publishId", jsonObject2.getString("publishId"));
                data.put("pictures", tempPicture);
                datas.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }
}
