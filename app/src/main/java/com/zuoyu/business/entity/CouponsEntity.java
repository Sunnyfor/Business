package com.zuoyu.business.entity;

import com.zuoyu.business.base.BaseEntity;
import com.zuoyu.business.base.BaseModel;

import java.util.List;

/**
 * <pre>
 * Function：优惠记录实体类
 *
 * Created by JoannChen on 2017/4/19 14:12
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class CouponsEntity extends BaseEntity {

    public Coupons data;

    public Coupons getData() {
        return data;
    }

    public void setData(Coupons data) {
        this.data = data;
    }

    //    1.
    public class Coupons extends BaseModel {
        private String dayuse;//日已用
        private String daycount;//日上限
        private List<DiscountInfo> list;//详细结果集合

        public String getDayuse() {
            return dayuse;
        }

        public void setDayuse(String dayuse) {
            this.dayuse = dayuse;
        }

        public String getDaycount() {
            return daycount;
        }

        public void setDaycount(String daycount) {
            this.daycount = daycount;
        }

        public List<DiscountInfo> getList() {
            return list;
        }

        public void setList(List<DiscountInfo> list) {
            this.list = list;
        }

        @Override
        public String toString() {
            return "Coupons{" +
                    "dayuse=" + dayuse +
                    ", daycount=" + daycount +
                    ", list=" + list +
                    '}';
        }


        //        2.
        public class DiscountInfo extends BaseModel {

            private String discount_details;//优惠详情(5元、2小时、8折)
            private String start_time;//发放日期(2017.4.10 12:09:34)
            private String license_plate;//车牌号
            private String ticketno;// 小票id
            private String used_time;//使用日期(2017.4.10 12:09:34)
            private String coupons_nickname;//优惠券昵称


            public String getCoupons_nickname() {
                return coupons_nickname;
            }

            public void setCoupons_nickname(String coupons_nickname) {
                this.coupons_nickname = coupons_nickname;
            }

            public String getDiscount_details() {
                return discount_details;
            }

            public void setDiscount_details(String discount_details) {
                this.discount_details = discount_details;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
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

            public String getUsed_time() {
                return used_time;
            }

            public void setUsed_time(String used_time) {
                this.used_time = used_time;
            }

            @Override
            public String toString() {
                return "DiscountInfo{" +
                        "discount_details='" + discount_details + '\'' +
                        ", start_time='" + start_time + '\'' +
                        ", license_plate='" + license_plate + '\'' +
                        ", ticketno='" + ticketno + '\'' +
                        ", used_time='" + used_time + '\'' +
                        ", coupons_nickname='" + coupons_nickname + '\'' +
                        '}';
            }
        }
    }


}
