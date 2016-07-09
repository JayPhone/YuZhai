package com.yuzhai.global;

/**
 * Created by Administrator on 2016/7/9.
 */
public class Login {
    private static boolean LOGIN = false;
    private static String COOKIE = null;

    public static boolean getLOGIN() {
        return LOGIN;
    }

    public static void setLOGIN(boolean isLogin) {
        LOGIN = isLogin;
    }

    public static String getCOOKIE() {
        return COOKIE;
    }

    public static void setCOOKIE(String COOKIE) {
        Login.COOKIE = COOKIE;
    }
}
