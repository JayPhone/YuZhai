package com.yuzhai.entry.responseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/28.
 */
public class OrderPublishedBean {
    private List<OrderBean> order;

    public List<OrderBean> getOrder() {
        return order;
    }

    public void setOrder(List<OrderBean> order) {
        this.order = order;
    }

    //单个订单
    public class OrderBean {
        private PublishBean publish;
        private List<PicturesBean> pictures;
        private List<AppliersBean> appliers;

        public List<AppliersBean> getAppliers() {
            return appliers;
        }

        public void setAppliers(List<AppliersBean> appliers) {
            this.appliers = appliers;
        }

        public PublishBean getPublish() {
            return publish;
        }

        public void setPublish(PublishBean publish) {
            this.publish = publish;
        }

        public List<PicturesBean> getPictures() {
            return pictures;
        }

        public void setPictures(List<PicturesBean> pictures) {
            this.pictures = pictures;
        }

        @Override
        public String toString() {
            return "OrderBean{" +
                    "publish=" + publish +
                    ", pictures=" + pictures +
                    ", appliers=" + appliers +
                    '}';
        }

        //订单详情
        public class PublishBean {
            private String title;
            private String publishId;
            private String tel;
            private String money;
            private String descript;
            private String date;
            private String type;
            private String deadline;
            private String currentstatus;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPublishId() {
                return publishId;
            }

            public void setPublishId(String publishId) {
                this.publishId = publishId;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getDescript() {
                return descript;
            }

            public void setDescript(String descript) {
                this.descript = descript;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
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

            public String getCurrentstatus() {
                return currentstatus;
            }

            public void setCurrentstatus(String currentstatus) {
                this.currentstatus = currentstatus;
            }


            @Override
            public String toString() {
                return "PublishBean{" +
                        "title='" + title + '\'' +
                        ", publishId='" + publishId + '\'' +
                        ", tel='" + tel + '\'' +
                        ", money='" + money + '\'' +
                        ", descript='" + descript + '\'' +
                        ", date='" + date + '\'' +
                        ", type='" + type + '\'' +
                        ", deadline='" + deadline + '\'' +
                        ", currentstatus='" + currentstatus + '\'' +
                        '}';
            }
        }

        //图片信息
        public class PicturesBean {
            private String picturePath;

            public String getPicturePath() {
                return picturePath;
            }

            public void setPicturePath(String picturePath) {
                this.picturePath = picturePath;
            }

            @Override
            public String toString() {
                return "PicturesBean{" +
                        "picturePath='" + picturePath + '\'' +
                        '}';
            }
        }

        //申请用户信息
        public class AppliersBean {
            private String applier;

            public String getApplier() {
                return applier;
            }

            public void setApplier(String applier) {
                this.applier = applier;
            }

            @Override
            public String toString() {
                return "AppliersBean{" +
                        "applier='" + applier + '\'' +
                        '}';
            }
        }
    }
}
