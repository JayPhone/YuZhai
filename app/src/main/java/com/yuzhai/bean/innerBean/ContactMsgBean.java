package com.yuzhai.bean.innerBean;

/**
 * Created by 35429 on 2017/2/9.
 */

public class ContactMsgBean {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;

    private String content;
    private int type;

    public ContactMsgBean(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
