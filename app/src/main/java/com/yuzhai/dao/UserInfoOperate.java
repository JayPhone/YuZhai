package com.yuzhai.dao;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/7/11.
 */
public class UserInfoOperate {

    public static String USERPHONE = "userphone";
    public static String PASSWORD = "password";
    private SharedPreferences userInfoPreferences = null;
    private SharedPreferences.Editor userInfoEdit = null;

    public UserInfoOperate(Context context) {
        userInfoPreferences = context.getSharedPreferences("user_info_preferences", Context.MODE_PRIVATE);
    }

    public void addUserInfo(String userPhone, String userPswd) {
        userInfoEdit = userInfoPreferences.edit();
        userInfoEdit.putString(USERPHONE, userPhone);
        userInfoEdit.putString(PASSWORD, userPswd);
        userInfoEdit.commit();
    }

    public String getUserPhone() {
        return userInfoPreferences.getString(USERPHONE, null);
    }

    public String getPassword() {
        return userInfoPreferences.getString(PASSWORD, null);
    }

    public void clearUserInfo() {
        userInfoEdit = userInfoPreferences.edit();
        userInfoEdit.remove(USERPHONE);
        userInfoEdit.remove(PASSWORD);
        userInfoEdit.commit();
    }
}
