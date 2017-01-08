package com.yuzhai.bean.responseBean;

/**
 * Created by Administrator on 2016/8/27.
 */
public class LoginRespBean {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像路径
     */
    private String userHeadUrl;

    /**
     * 返回码
     */
    private String code;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
