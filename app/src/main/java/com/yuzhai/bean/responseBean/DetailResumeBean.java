package com.yuzhai.bean.responseBean;

/**
 * Created by Administrator on 2017/1/11.
 */

public class DetailResumeBean {
    private ResumeBean detail_resume;

    public ResumeBean getDetailResume() {
        return detail_resume;
    }

    public void setDetailResume(ResumeBean detailResume) {
        detail_resume = detailResume;
    }

    @Override
    public String toString() {
        return "DetailResumeBean{" +
                "detail_resume=" + detail_resume +
                '}';
    }

    public class ResumeBean {
        private String avatar;
        private String user_name;
        private String contact_number;
        private String name;
        private String sex;
        private String module;
        private String education;
        private String education_experience;
        private String skill;
        private String work_experience;
        private String self_evaluation;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUserName() {
            return user_name;
        }

        public void setUserName(String userName) {
            user_name = userName;
        }

        public String getContactNumber() {
            return contact_number;
        }

        public void setContactNumber(String contactNumber) {
            contact_number = contactNumber;
        }

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

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getEducationExperience() {
            return education_experience;
        }

        public void setEducationExperience(String educationExperience) {
            education_experience = educationExperience;
        }

        public String getSkill() {
            return skill;
        }

        public void setSkill(String skill) {
            this.skill = skill;
        }

        public String getWorkExperience() {
            return work_experience;
        }

        public void setWorkExperience(String workExperience) {
            work_experience = workExperience;
        }

        public String getSelfEvaluation() {
            return self_evaluation;
        }

        public void setSelfEvaluation(String selfEvaluation) {
            self_evaluation = selfEvaluation;
        }

        @Override
        public String toString() {
            return "ResumeBean{" +
                    "avatar='" + avatar + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", contact_number='" + contact_number + '\'' +
                    ", name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    ", module='" + module + '\'' +
                    ", education='" + education + '\'' +
                    ", education_experience='" + education_experience + '\'' +
                    ", skill='" + skill + '\'' +
                    ", work_experience='" + work_experience + '\'' +
                    ", self_evaluation='" + self_evaluation + '\'' +
                    '}';
        }
    }
}
