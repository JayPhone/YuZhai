package com.yuzhai.util;

/**
 * Created by Administrator on 2016/6/6.
 */
public class CheckData {
    //校验数据是否为空
    public static boolean isEmpty(String data) {
        if (data.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    //校验数据长度是否匹配
    public static boolean matchLength(String data, int length) {
        if (data.length() == length) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean lessLength(String data, int length) {
        if (data.length() < length) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean matchString(String data1, String data2) {
        if (data1.equals(data2)) {
            return true;
        } else {
            return false;
        }
    }
}
