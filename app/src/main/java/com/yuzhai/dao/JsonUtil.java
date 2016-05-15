package com.yuzhai.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 创建时间2016/5/14.
 * 作者 HJF
 * 主要功能：JSON数据的解析
 */
public class JsonUtil {

    public static <T> T getPerson(String jsonString, Class<T> cls) {
        T t = null;
        Gson gson = new Gson();
        t = gson.fromJson(jsonString, cls);
        return t;
    }

    public static <T> List<T> getPersons(String jsonString) {
        List<T> list = new ArrayList<T>();
        Gson gson = new Gson();
        list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
        }.getType());
        return list;
    }

    public static <T> List<T> getCitys(String jsonString) {
        List<T> list = new ArrayList<T>();
        Gson gson = new Gson();
        list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
        }.getType());
        return list;
    }

    public static <T> List<Map<String, T>> getListMap(String jsonString) {
        List<Map<String, T>> list = new ArrayList<Map<String, T>>();
        Gson gson = new Gson();
        list = gson.fromJson(jsonString, new TypeToken<List<Map<String, T>>>() {
        }.getType());
        return list;
    }
}
