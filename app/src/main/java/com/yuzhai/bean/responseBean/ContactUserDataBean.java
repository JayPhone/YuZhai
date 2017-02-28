package com.yuzhai.bean.responseBean;

/**
 * Created by 35429 on 2017/2/23.
 */

public class ContactUserDataBean {
    private String user_id;
    private String name;
    private String avatar;

    public ContactUserDataBean(String user_id, String name, String avatar) {
        this.user_id = user_id;
        this.name = name;
        this.avatar = avatar;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "ContactUserDataBean{" +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
