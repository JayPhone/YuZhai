package com.yuzhai.bean.responseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */

public class SimpleResumeByTypeBean {
    private List<SimpleResumeBean> Resumes;

    public List<SimpleResumeBean> getResumes() {
        return Resumes;
    }

    public void setResumes(List<SimpleResumeBean> Resumes) {
        this.Resumes = Resumes;
    }

    @Override
    public String toString() {
        return "SimpleResumeByTypeBean{" +
                "resumes=" + Resumes +
                '}';
    }

    public class SimpleResumeBean {
        private String Avatar;
        private String UserPhone;
        private String Name;
        private String Module;
        private String Sex;
        private String Education;
        private String ContactNumber;

        public String getAvatar() {
            return Avatar;
        }

        public void setAvatar(String avatar) {
            Avatar = avatar;
        }

        public String getUserPhone() {
            return UserPhone;
        }

        public void setUserPhone(String userPhone) {
            UserPhone = userPhone;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getModule() {
            return Module;
        }

        public void setModule(String module) {
            Module = module;
        }

        public String getSex() {
            return Sex;
        }

        public void setSex(String sex) {
            Sex = sex;
        }

        public String getEducation() {
            return Education;
        }

        public void setEducation(String education) {
            Education = education;
        }

        public String getContactNumber() {
            return ContactNumber;
        }

        public void setContactNumber(String contactNumber) {
            ContactNumber = contactNumber;
        }

        @Override
        public String toString() {
            return "SimpleResumeBean{" +
                    "Avatar='" + Avatar + '\'' +
                    ", UserPhone='" + UserPhone + '\'' +
                    ", Name='" + Name + '\'' +
                    ", Module='" + Module + '\'' +
                    ", Sex='" + Sex + '\'' +
                    ", Education='" + Education + '\'' +
                    ", ContactNumber='" + ContactNumber + '\'' +
                    '}';
        }
    }
}
