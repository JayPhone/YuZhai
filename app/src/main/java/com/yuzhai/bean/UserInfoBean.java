package com.yuzhai.bean;

/**
 * Created by Administrator on 2016/11/23.
 */

public class UserInfoBean extends BaseUserInfoBean {
    private String userPhone;
    private String realName;

    public UserInfoBean(String userHeadUrl, String userName, String userPhone, String realName, boolean isLogin) {
        super(userHeadUrl, userName, isLogin);
        this.userPhone = userPhone;
        this.realName = realName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
