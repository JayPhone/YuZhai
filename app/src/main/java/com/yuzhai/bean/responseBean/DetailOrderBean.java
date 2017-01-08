package com.yuzhai.bean.responseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/28.
 */
public class DetailOrderBean {
    private OrderInfoBean order;

    public OrderInfoBean getOrder() {
        return order;
    }

    public void setOrder(OrderInfoBean order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "DetailOrderBean{" +
                "order=" + order +
                '}';
    }

    public class OrderInfoBean {
        private PublisherBean Publisher;
        private String Description;
        private String Reward;
        private String Date;
        private String OrderID;
        private String Type;
        private String Deadline;
        private String Title;
        private String State;
        private String Tel;
        private List<PicturesBean> Picture;
        private List<ApplicantBean> Applicant;

        public PublisherBean getPublisher() {
            return Publisher;
        }

        public void setPublisher(PublisherBean publisher) {
            Publisher = publisher;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getTel() {
            return Tel;
        }

        public void setTel(String tel) {
            Tel = tel;
        }

        public String getReward() {
            return Reward;
        }

        public void setReward(String reward) {
            Reward = reward;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public String getOrderID() {
            return OrderID;
        }

        public void setOrderID(String orderID) {
            OrderID = orderID;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getDeadline() {
            return Deadline;
        }

        public void setDeadline(String deadline) {
            Deadline = deadline;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public List<PicturesBean> getPicture() {
            return Picture;
        }

        public void setPicture(List<PicturesBean> picture) {
            Picture = picture;
        }

        public List<ApplicantBean> getApplicant() {
            return Applicant;
        }

        public void setApplicant(List<ApplicantBean> applicant) {
            Applicant = applicant;
        }

        @Override
        public String toString() {
            return "OrderInfoBean{" +
                    "Publisher=" + Publisher +
                    ", Description='" + Description + '\'' +
                    ", Reward='" + Reward + '\'' +
                    ", Date='" + Date + '\'' +
                    ", OrderID='" + OrderID + '\'' +
                    ", Type='" + Type + '\'' +
                    ", Deadline='" + Deadline + '\'' +
                    ", Title='" + Title + '\'' +
                    ", State='" + State + '\'' +
                    ", Tel='" + Tel + '\'' +
                    ", Picture=" + Picture +
                    ", Applicant=" + Applicant +
                    '}';
        }

        //发布用户信息
        public class PublisherBean {
            private String Avatar;
            private String Name;

            public String getAvatar() {
                return Avatar;
            }

            public void setAvatar(String avatar) {
                Avatar = avatar;
            }

            public String getName() {
                return Name;
            }

            public void setName(String name) {
                Name = name;
            }

            @Override
            public String toString() {
                return "PublisherBean{" +
                        "Avatar='" + Avatar + '\'' +
                        ", Name='" + Name + '\'' +
                        '}';
            }

        }


        //图片信息
        public class PicturesBean {
            private String Image;

            public String getImage() {
                return Image;
            }

            public void setImage(String image) {
                this.Image = image;
            }

            @Override
            public String toString() {
                return "PicturesBean{" +
                        "Image='" + Image + '\'' +
                        '}';
            }
        }

        //申请人信息
        public class ApplicantBean {
            private String applicantAvatar;
            private String applicantId;

            public String getApplicantAvatar() {
                return applicantAvatar;
            }

            public void setApplicantAvatar(String applicantAvatar) {
                this.applicantAvatar = applicantAvatar;
            }

            public String getApplicantId() {
                return applicantId;
            }

            public void setApplicantId(String applicantId) {
                this.applicantId = applicantId;
            }

            @Override
            public String toString() {
                return "ApplicantBean{" +
                        "applicantAvatar='" + applicantAvatar + '\'' +
                        ", applicantId='" + applicantId + '\'' +
                        '}';
            }
        }
    }
}
