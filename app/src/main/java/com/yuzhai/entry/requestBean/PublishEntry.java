package com.yuzhai.entry.requestBean;

/**
 * Created by Administrator on 2016/7/19.
 */
public class PublishEntry {
    private String title;
    private String descript;
    private String type;
    private String deadline;
    private String tel;
    private String money;

    public PublishEntry() {

    }

    public PublishEntry(String title, String descript, String type, String deadline, String tel, String money) {
        this.title = title;
        this.descript = descript;
        this.type = type;
        this.deadline = deadline;
        this.tel = tel;
        this.money = money;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
