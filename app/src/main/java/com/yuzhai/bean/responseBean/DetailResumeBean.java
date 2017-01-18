package com.yuzhai.bean.responseBean;

/**
 * Created by Administrator on 2017/1/11.
 */

public class DetailResumeBean {
    private ResumeBean DetailResume;

    public ResumeBean getDetailResume() {
        return DetailResume;
    }

    public void setDetailResume(ResumeBean detailResume) {
        DetailResume = detailResume;
    }

    @Override
    public String toString() {
        return "DetailResumeBean{" +
                "DetailResume=" + DetailResume +
                '}';
    }

    public class ResumeBean {
        private String Avatar;
        private String UserName;
        private String ContactNumber;
        private String Name;
        private String Sex;
        private String Module;
        private String Education;
        private String EducationExperience;
        private String Skill;
        private String WorkExperience;
        private String SelfEvaluation;

        public String getAvatar() {
            return Avatar;
        }

        public void setAvatar(String avatar) {
            Avatar = avatar;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getContactNumber() {
            return ContactNumber;
        }

        public void setContactNumber(String contactNumber) {
            ContactNumber = contactNumber;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getSex() {
            return Sex;
        }

        public void setSex(String sex) {
            Sex = sex;
        }

        public String getModule() {
            return Module;
        }

        public void setModule(String module) {
            Module = module;
        }

        public String getEducation() {
            return Education;
        }

        public void setEducation(String education) {
            Education = education;
        }

        public String getEducationExperience() {
            return EducationExperience;
        }

        public void setEducationExperience(String educationExperience) {
            EducationExperience = educationExperience;
        }

        public String getSkill() {
            return Skill;
        }

        public void setSkill(String skill) {
            Skill = skill;
        }

        public String getWorkExperience() {
            return WorkExperience;
        }

        public void setWorkExperience(String workExperience) {
            WorkExperience = workExperience;
        }

        public String getSelfEvaluation() {
            return SelfEvaluation;
        }

        public void setSelfEvaluation(String selfEvaluation) {
            SelfEvaluation = selfEvaluation;
        }

        @Override
        public String toString() {
            return "ResumeBean{" +
                    "Avatar='" + Avatar + '\'' +
                    ", UserName='" + UserName + '\'' +
                    ", ContactNumber='" + ContactNumber + '\'' +
                    ", Name='" + Name + '\'' +
                    ", Sex='" + Sex + '\'' +
                    ", Module='" + Module + '\'' +
                    ", Education='" + Education + '\'' +
                    ", EducationExperience='" + EducationExperience + '\'' +
                    ", Skill='" + Skill + '\'' +
                    ", WorkExperience='" + WorkExperience + '\'' +
                    ", SelfEvaluation='" + SelfEvaluation + '\'' +
                    '}';
        }
    }
}
