package com.yuzhai.global;

/**
 * Created by Administrator on 2016/7/9.
 */
public class Login {
    private static String COOKIE = null;

    public static String getCOOKIE() {
        return COOKIE;
    }

    public static void setCOOKIE(String COOKIE) {
        Login.COOKIE = COOKIE;
    }
}
