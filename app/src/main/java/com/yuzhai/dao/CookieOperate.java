package com.yuzhai.dao;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/7/9.
 */
public class CookieOperate {

    public static String COOKIE = "cookie";
    private SharedPreferences.Editor cookieEdit = null;
    private SharedPreferences cookiePreferences = null;

    public CookieOperate(Context context) {
        cookiePreferences = context.getSharedPreferences("cookie_preferences", Context.MODE_PRIVATE);
    }

    //添加cookie到本地
    public void addCookie(String cookie) {
        cookieEdit = cookiePreferences.edit();
        cookieEdit.putString(COOKIE, cookie);
        cookieEdit.commit();
    }

    //获取cookie
    public String getCookie() {
        String cookie = cookiePreferences.getString(COOKIE, null);
        return cookie;
    }

    //清除cookie
    public void clearCookie() {
        cookieEdit = cookiePreferences.edit();
        cookieEdit.remove(COOKIE);
        cookieEdit.commit();
    }
}
