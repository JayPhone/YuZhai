package com.yuzhai.bean.responseBean;

import java.util.List;

/**
 * Created by 35429 on 2017/2/20.
 */

public class OrderAppliedBean {
    private List<OrderAppliedBean.OrderBean> orders;

    public List<OrderAppliedBean.OrderBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderAppliedBean.OrderBean> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "OrderAppliedBean{" +
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

        public String getOrderId() {
            return order_id;
        }

        public void setOrderId(String order_id) {
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
