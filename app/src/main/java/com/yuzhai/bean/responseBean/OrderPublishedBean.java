package com.yuzhai.bean.responseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/28.
 */
public class OrderPublishedBean {
    private List<OrderPublishedBean.OrderBean> orders;

    public List<OrderPublishedBean.OrderBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderPublishedBean.OrderBean> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "OrderPublishedBean{" +
                "Orders=" + orders +
                '}';
    }

    public class OrderBean {
        private String status;
        private String reward;
        private String date;
        private String order_id;
        private String type;
        private String deadline;
        private String title;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        @Override
        public String toString() {
            return "OrderBean{" +
                    "status='" + status + '\'' +
                    ", reward='" + reward + '\'' +
                    ", date='" + date + '\'' +
                    ", order_id='" + order_id + '\'' +
                    ", type='" + type + '\'' +
                    ", deadline='" + deadline + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}
