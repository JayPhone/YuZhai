package com.yuzhai.bean.responseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */

public class SimpleResumeByTypeBean {
    private List<SimpleResumeBean> resumes;

    public List<SimpleResumeBean> getResumes() {
        return resumes;
    }

    public void setResumes(List<SimpleResumeBean> Resumes) {
        this.resumes = Resumes;
    }

    @Override
    public String toString() {
        return "SimpleResumeByTypeBean{" +
                "resumes=" + resumes +
                '}';
    }

    public class SimpleResumeBean {
        private String avatar;
        private String user_phone;
        private String name;
        private String module;
        private String sex;
        private String education;
        private String contact_number;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUserPhone() {
            return user_phone;
        }

        public void setUserPhone(String userPhone) {
            user_phone = userPhone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getContactNumber() {
            return contact_number;
        }

        public void setContactNumber(String contactNumber) {
            contact_number = contactNumber;
        }

        @Override
        public String toString() {
            return "SimpleResumeBean{" +
                    "avatar='" + avatar + '\'' +
                    ", user_phone='" + user_phone + '\'' +
                    ", name='" + name + '\'' +
                    ", module='" + module + '\'' +
                    ", sex='" + sex + '\'' +
                    ", education='" + education + '\'' +
                    ", contact_number='" + contact_number + '\'' +
                    '}';
        }
    }
}
