package com.yuzhai.bean.responseBean;

/**
 * Created by Administrator on 2016/8/27.
 */
public class LoginRespBean {
    //返回码
    private String code;
    //用户名
    private String user_name;
    //用户头像路径
    private String user_head_url;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_head_url() {
        return user_head_url;
    }

    public void setUser_head_url(String user_head_url) {
        this.user_head_url = user_head_url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
