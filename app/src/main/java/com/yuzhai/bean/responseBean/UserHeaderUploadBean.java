package com.yuzhai.bean.responseBean;

/**
 * Created by Administrator on 2017/1/10.
 */

public class UserHeaderUploadBean {
    private String Avatar;

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    @Override
    public String toString() {
        return "UserHeaderUploadBean{" +
                "Avatar='" + Avatar + '\'' +
                '}';
    }
}
