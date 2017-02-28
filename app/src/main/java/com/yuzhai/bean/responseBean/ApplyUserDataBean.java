package com.yuzhai.bean.responseBean;

/**
 * Created by 35429 on 2017/2/20.
 */

public class ApplyUserDataBean {
    private String avatar;
    private String user_phone;
    private String user_name;
    private String authen;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserPhone() {
        return user_phone;
    }

    public void setUserPhone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getAuthen() {
        return authen;
    }

    public void setAuthen(String authen) {
        this.authen = authen;
    }

    @Override
    public String toString() {
        return "ApplyUserDataBean{" +
                "avatar='" + avatar + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_name='" + user_name + '\'' +
                ", authen='" + authen + '\'' +
                '}';
    }
}
