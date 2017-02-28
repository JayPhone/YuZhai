package com.yuzhai.bean.responseBean;

/**
 * Created by Administrator on 2017/1/10.
 */

public class UserAvatarUploadBean {
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "UserAvatarUploadBean{" +
                "avatar='" + avatar + '\'' +
                '}';
    }
}
