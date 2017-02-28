package com.yuzhai.bean.innerBean;

/**
 * Created by 35429 on 2017/2/19.
 */

public class NotificationBean {
    private String type;
    private String order_id;
    private String date;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
