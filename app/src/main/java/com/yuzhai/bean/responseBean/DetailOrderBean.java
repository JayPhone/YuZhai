package com.yuzhai.bean.responseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/28.
 */
public class DetailOrderBean {
    private OrderInfoBean detailed_order;

    public OrderInfoBean getDetailedOrder() {
        return detailed_order;
    }

    public void setDetailedOrder(OrderInfoBean detailed_order) {
        this.detailed_order = detailed_order;
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
        private List<PicturesBean> pictures;
        private List<ApplicantAvatars> applicant_avatars;

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

        public List<PicturesBean> getPictures() {
            return pictures;
        }

        public void setPictures(List<PicturesBean> pictures) {
            this.pictures = pictures;
        }

        public String getPublisherAvatar() {
            return publisher_avatar;
        }

        public void setPublisherAvatar(String publisherAvatar) {
            publisher_avatar = publisherAvatar;
        }

        public List<ApplicantAvatars> getApplicantAvatars() {
            return applicant_avatars;
        }

        public void setApplicantAvatars(List<ApplicantAvatars> applicantAvatars) {
            this.applicant_avatars = applicantAvatars;
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
                    ", pictures=" + pictures +
                    ", applicantAvatars=" + applicant_avatars +
                    '}';
        }

        //图片信息
        public class PicturesBean {
            private String image;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            @Override
            public String toString() {
                return "PicturesBean{" +
                        "image='" + image + '\'' +
                        '}';
            }
        }

        //申请人信息
        public class ApplicantAvatars {
            private String applicant_avatar;

            public String getApplicantAvatar() {
                return applicant_avatar;
            }

            public void setApplicantAvatar(String applicantAvatar) {
                applicant_avatar = applicantAvatar;
            }

            @Override
            public String toString() {
                return "applicant_avatars{" +
                        "applicant_avatar='" + applicant_avatar + '\'' +
                        '}';
            }
        }
    }
}
