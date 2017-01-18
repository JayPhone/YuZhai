package com.yuzhai.bean.requestBean;

/**
 * Created by Administrator on 2017/1/11.
 */

public class SendResumeBean {
    private String name;
    private String sex;
    private String type;
    private String education;
    private String tel;
    private String educationalExperience;
    private String skill;
    private String workExperience;
    private String selfEvaluation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEducationalExperience() {
        return educationalExperience;
    }

    public void setEducationalExperience(String educationalExperience) {
        this.educationalExperience = educationalExperience;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public String getSelfEvaluation() {
        return selfEvaluation;
    }

    public void setSelfEvaluation(String selfEvaluation) {
        this.selfEvaluation = selfEvaluation;
    }

    @Override
    public String toString() {
        return "SendResumeBean{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", type='" + type + '\'' +
                ", education='" + education + '\'' +
                ", tel='" + tel + '\'' +
                ", educationalExperience='" + educationalExperience + '\'' +
                ", skill='" + skill + '\'' +
                ", workExperience='" + workExperience + '\'' +
                ", selfEvaluation='" + selfEvaluation + '\'' +
                '}';
    }
}
