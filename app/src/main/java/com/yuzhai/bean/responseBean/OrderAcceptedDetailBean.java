package com.yuzhai.bean.responseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/10.
 */

public class OrderAcceptedDetailBean {
    private OrderAcceptedDetailBean.OrderInfoBean detailed_order;

    public OrderAcceptedDetailBean.OrderInfoBean getDetailedOrder() {
        return detailed_order;
    }

    public void setDetailedOrder(OrderAcceptedDetailBean.OrderInfoBean detailedOrder) {
        this.detailed_order = detailedOrder;
    }

    @Override
    public String toString() {
        return "DetailOrderBean{" +
                "detailed_order=" + detailed_order +
                '}';
    }

    public class OrderInfoBean {
        private String publisher;
        private String description;
        private String reward;
        private String date;
        private String order_id;
        private String publisher_avatar;
        private String deadline;
        private String title;
        private String status;
        private String tel;
        private String bidder;

        private List<OrderAcceptedDetailBean.OrderInfoBean.PicturesBean> pictures;
        private List<OrderAcceptedDetailBean.OrderInfoBean.ApplicantAvatars> applicant_avatars;

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getOrderID() {
            return order_id;
        }

        public void setOrderID(String orderID) {
            order_id = orderID;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<OrderAcceptedDetailBean.OrderInfoBean.PicturesBean> getPictures() {
            return pictures;
        }

        public void setPictures(List<OrderAcceptedDetailBean.OrderInfoBean.PicturesBean> pictures) {
            this.pictures = pictures;
        }

        public String getPublisherAvatar() {
            return publisher_avatar;
        }

        public void setPublisherAvatar(String publisherAvatar) {
            publisher_avatar = publisherAvatar;
        }

        public List<OrderAcceptedDetailBean.OrderInfoBean.ApplicantAvatars> getApplicantAvatars() {
            return applicant_avatars;
        }

        public void setApplicantAvatars(List<OrderAcceptedDetailBean.OrderInfoBean.ApplicantAvatars> applicantAvatars) {
            this.applicant_avatars = applicantAvatars;
        }

        public String getBidder() {
            return bidder;
        }

        public void setBidder(String bidder) {
            this.bidder = bidder;
        }

        @Override
        public String toString() {
            return "OrderInfoBean{" +
                    "publisher='" + publisher + '\'' +
                    ", description='" + description + '\'' +
                    ", reward='" + reward + '\'' +
                    ", date='" + date + '\'' +
                    ", order_id='" + order_id + '\'' +
                    ", publisher_avatar='" + publisher_avatar + '\'' +
                    ", deadline='" + deadline + '\'' +
                    ", title='" + title + '\'' +
                    ", status='" + status + '\'' +
                    ", tel='" + tel + '\'' +
                    ", bidder='" + bidder + '\'' +
                    ", pictures=" + pictures +
                    ", applicant_avatars=" + applicant_avatars +
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
