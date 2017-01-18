package com.yuzhai.bean.responseBean;

/**
 * Created by Administrator on 2017/1/11.
 */

public class SendResumeRespBean {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "SendResumeRespBean{" +
                "code='" + code + '\'' +
                '}';
    }
}
