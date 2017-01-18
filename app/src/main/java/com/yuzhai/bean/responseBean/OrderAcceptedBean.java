package com.yuzhai.bean.responseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/10.
 */

public class OrderAcceptedBean {
    private List<OrderAcceptedBean.OrderBean> Orders;

    public List<OrderAcceptedBean.OrderBean> getOrders() {
        return Orders;
    }

    public void setOrders(List<OrderAcceptedBean.OrderBean> orders) {
        this.Orders = orders;
    }

    @Override
    public String toString() {
        return "OrderAcceptedBean{" +
                "Orders=" + Orders +
                '}';
    }

    public class OrderBean {
        private String Status;
        private String Reward;
        private String Date;
        private String OrderID;
        private String Type;
        private String Deadline;
        private String Title;

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
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

        @Override
        public String toString() {
            return "OrderBean{" +
                    "Status='" + Status + '\'' +
                    ", Reward='" + Reward + '\'' +
                    ", Date='" + Date + '\'' +
                    ", OrderID='" + OrderID + '\'' +
                    ", Type='" + Type + '\'' +
                    ", Deadline='" + Deadline + '\'' +
                    ", Title='" + Title + '\'' +
                    '}';
        }
    }
}
