package com.zuoyu.business.entity;

import com.zuoyu.business.base.BaseEntity;
import com.zuoyu.business.base.BaseModel;

import java.util.List;

/**
 * <pre>
 * Function：搜索车牌号实体类
 *
 * Created by JoannChen on 2017/4/19 13:39
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class SearchEntity extends BaseEntity {

    private List<CarInfo> data;

    public List<CarInfo> getData() {
        return data;
    }

    public void setData(List<CarInfo> data) {
        this.data = data;
    }

    public class CarInfo extends BaseModel {


//        车牌号
//        条形码
//        停车时长(xx天xx时xx分)
//        总应收金额
//        入场图片(可能为空)
//        订单号

//        入场时间秒数
//        是否使用优惠(1:已使用2:未使用)
//        出场状态（1：在场，4，异常车辆，其他数字：出场）


        private String license_plate;
        private String ticketno;
        private String parkingtime;
        private String totalprice;
        private String imageurl;
        private String ordernum;
        private String image;//拍照照片

        private int entertime;
        private int discount_used;
        private int service_status;

        //发放权限 0：单项 1：多项
        private int permissions;
        private List<CouponsInfo> couponlist;

        public int getPermissions() {
            return permissions;
        }

        public void setPermissions(int permissions) {
            this.permissions = permissions;
        }

        public List<CouponsInfo> getCouponlist() {
            return couponlist;
        }

        public void setCouponlist(List<CouponsInfo> couponlist) {
            this.couponlist = couponlist;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(String ordernum) {
            this.ordernum = ordernum;
        }

        public int getEntertime() {
            return entertime;
        }

        public void setEntertime(int entertime) {
            this.entertime = entertime;
        }

        public String getLicense_plate() {
            return license_plate;
        }

        public void setLicense_plate(String license_plate) {
            this.license_plate = license_plate;
        }

        public String getTicketno() {
            return ticketno;
        }

        public void setTicketno(String ticketno) {
            this.ticketno = ticketno;
        }

        public String getParkingtime() {
            return parkingtime;
        }

        public void setParkingtime(String parkingtime) {
            this.parkingtime = parkingtime;
        }

        public String getTotalprice() {
            return totalprice;
        }

        public void setTotalprice(String totalprice) {
            this.totalprice = totalprice;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public int getDiscount_used() {
            return discount_used;
        }

        public void setDiscount_used(int discount_used) {
            this.discount_used = discount_used;
        }

        public int getService_status() {
            return service_status;
        }

        public void setService_status(int service_status) {
            this.service_status = service_status;
        }

        @Override
        public String toString() {
            return "CarInfo{" +
                    "license_plate='" + license_plate + '\'' +
                    ", ticketno='" + ticketno + '\'' +
                    ", parkingtime='" + parkingtime + '\'' +
                    ", totalprice='" + totalprice + '\'' +
                    ", imageurl='" + imageurl + '\'' +
                    ", ordernum='" + ordernum + '\'' +
                    ", image='" + image + '\'' +
                    ", entertime=" + entertime +
                    ", discount_used=" + discount_used +
                    ", service_status=" + service_status +
                    ", permissions=" + permissions +
                    ", couponlist=" + couponlist +
                    '}';
        }

        public class CouponsInfo extends BaseModel {

            // 优惠昵称
            private String coupons_nickname;

            public String getCoupons_nickname() {
                return coupons_nickname;
            }

            public void setCoupons_nickname(String coupons_nickname) {
                this.coupons_nickname = coupons_nickname;
            }

            @Override
            public String toString() {
                return "CouponsInfo{" +
                        "coupons_nickname='" + coupons_nickname + '\'' +
                        '}';
            }
        }
    }


}
