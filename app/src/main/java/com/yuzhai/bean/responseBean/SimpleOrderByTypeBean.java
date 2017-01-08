package com.yuzhai.bean.responseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class SimpleOrderByTypeBean {
    private List<SimpleOrderBean> Orders;

    public List<SimpleOrderBean> getOrders() {
        return Orders;
    }

    public void setOrders(List<SimpleOrderBean> orders) {
        this.Orders = orders;
    }

    @Override
    public String toString() {
        return "SimpleOrderByTypeBean{" +
                "Orders=" + Orders +
                '}';
    }

    public class SimpleOrderBean {
        private String Publisher;
        private String Reward;
        private String Date;
        private String OrderID;
        private String Type;
        private String Deadline;
        private String Title;
        private List<PicturesBean> Picture;

        public String getPublisher() {
            return Publisher;
        }

        public void setPublisher(String publisher) {
            Publisher = publisher;
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
            this.Picture = picture;
        }

        @Override
        public String toString() {
            return "SimpleOrderBean{" +
                    "Publisher='" + Publisher + '\'' +
                    ", Reward='" + Reward + '\'' +
                    ", Date='" + Date + '\'' +
                    ", OrderID='" + OrderID + '\'' +
                    ", Type='" + Type + '\'' +
                    ", Deadline='" + Deadline + '\'' +
                    ", Title='" + Title + '\'' +
                    ", Picture=" + Picture +
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
    }
}
