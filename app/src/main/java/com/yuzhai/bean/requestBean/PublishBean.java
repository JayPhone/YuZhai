package com.yuzhai.bean.requestBean;

/**
 * Created by Administrator on 2016/7/19.
 */
public class PublishBean {
    private String title;
    private String description;
    private String type;
    private String deadline;
    private String tel;
    private String reward;


    public PublishBean(String title, String description, String type, String deadline, String tel, String reward) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.deadline = deadline;
        this.tel = tel;
        this.reward = reward;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
