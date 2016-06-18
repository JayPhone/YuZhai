package com.yuzhai.util;

/**
 * Created by Administrator on 2016/6/6.
 */
public class JsonToParams {
    public static String replaceToParams(String jsonString) {
        jsonString = jsonString.replace(":", "=");
        jsonString = jsonString.replace(",", "&");
        jsonString = jsonString.replace("\"", "");
        jsonString = jsonString.replace("{", "");
        jsonString = jsonString.replace("}", "");
        return jsonString;
    }
}
