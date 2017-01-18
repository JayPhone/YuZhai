package com.yuzhai.bean.responseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/10.
 */

public class OrderAcceptedDetailBean {
    private OrderAcceptedDetailBean.OrderInfoBean DetailedOrder;

    public OrderAcceptedDetailBean.OrderInfoBean getDetailedOrder() {
        return DetailedOrder;
    }

    public void setDetailedOrder(OrderAcceptedDetailBean.OrderInfoBean detailedOrder) {
        this.DetailedOrder = detailedOrder;
    }

    @Override
    public String toString() {
        return "DetailOrderBean{" +
                "DetailedOrder=" + DetailedOrder +
                '}';
    }

    public class OrderInfoBean {
        private String Publisher;
        private String Description;
        private String Reward;
        private String Date;
        private String OrderID;
        private String PublisherAvatar;
        private String Deadline;
        private String Title;
        private String Status;
        private String Tel;
        private List<OrderAcceptedDetailBean.OrderInfoBean.PicturesBean> Pictures;
        private List<OrderAcceptedDetailBean.OrderInfoBean.ApplicantAvatars> applicantAvatars;

        public String getPublisher() {
            return Publisher;
        }

        public void setPublisher(String publisher) {
            Publisher = publisher;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
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

        public List<OrderAcceptedDetailBean.OrderInfoBean.PicturesBean> getPictures() {
            return Pictures;
        }

        public void setPictures(List<OrderAcceptedDetailBean.OrderInfoBean.PicturesBean> pictures) {
            Pictures = pictures;
        }

        public String getPublisherAvatar() {
            return PublisherAvatar;
        }

        public void setPublisherAvatar(String publisherAvatar) {
            PublisherAvatar = publisherAvatar;
        }

        public List<OrderAcceptedDetailBean.OrderInfoBean.ApplicantAvatars> getApplicantAvatars() {
            return applicantAvatars;
        }

        public void setApplicantAvatars(List<OrderAcceptedDetailBean.OrderInfoBean.ApplicantAvatars> applicantAvatars) {
            this.applicantAvatars = applicantAvatars;
        }

        @Override
        public String toString() {
            return "OrderInfoBean{" +
                    "Publisher='" + Publisher + '\'' +
                    ", Description='" + Description + '\'' +
                    ", Reward='" + Reward + '\'' +
                    ", Date='" + Date + '\'' +
                    ", OrderID='" + OrderID + '\'' +
                    ", PublisherAvatar='" + PublisherAvatar + '\'' +
                    ", Deadline='" + Deadline + '\'' +
                    ", Title='" + Title + '\'' +
                    ", Status='" + Status + '\'' +
                    ", Tel='" + Tel + '\'' +
                    ", Pictures=" + Pictures +
                    ", applicantAvatars=" + applicantAvatars +
                    '}';
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
        public class ApplicantAvatars {
            private String ApplicantAvatar;

            public String getApplicantAvatar() {
                return ApplicantAvatar;
            }

            public void setApplicantAvatar(String applicantAvatar) {
                ApplicantAvatar = applicantAvatar;
            }

            @Override
            public String toString() {
                return "ApplicantAvatars{" +
                        "ApplicantAvatar='" + ApplicantAvatar + '\'' +
                        '}';
            }
        }
    }
}
