package com.yuzhai.bean.responseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class SimpleOrderByTypeBean {
    private List<SimpleOrderBean> orders;

    public List<SimpleOrderBean> getOrders() {
        return orders;
    }

    public void setOrders(List<SimpleOrderBean> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "SimpleOrderByTypeBean{" +
                "orders=" + orders +
                '}';
    }

    public class SimpleOrderBean {
        private String publisher;
        private String reward;
        private String date;
        private String order_id;
        private String type;
        private String deadline;
        private String title;
        private List<PicturesBean> picture;

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
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

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<PicturesBean> getPicture() {
            return picture;
        }

        public void setPicture(List<PicturesBean> picture) {
            this.picture = picture;
        }

        @Override
        public String toString() {
            return "OrderBean{" +
                    "publisher='" + publisher + '\'' +
                    ", reward='" + reward + '\'' +
                    ", date='" + date + '\'' +
                    ", order_id='" + order_id + '\'' +
                    ", type='" + type + '\'' +
                    ", deadline='" + deadline + '\'' +
                    ", title='" + title + '\'' +
                    ", picture=" + picture +
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
    }
}
