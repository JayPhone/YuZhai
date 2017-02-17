package com.yuzhai.dao;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/7/9.
 */
public class CookieOperate {

    private static String COOKIE = "cookie";
    private SharedPreferences.Editor cookieEdit;
    private SharedPreferences cookiePreferences;

    public CookieOperate(Context context) {
        cookiePreferences = context.getSharedPreferences("cookie_preferences", Context.MODE_PRIVATE);
    }

    //添加cookie到本地
    public void setCookie(String cookie) {
        cookieEdit = cookiePreferences.edit();
        cookieEdit.putString(COOKIE, cookie);
        cookieEdit.commit();
    }

    //获取cookie
    public String getCookie() {
        return cookiePreferences.getString(COOKIE, null);
    }

    //清除cookie
    public void clearCookie() {
        cookieEdit = cookiePreferences.edit();
        cookieEdit.remove(COOKIE);
        cookieEdit.commit();
    }
}
