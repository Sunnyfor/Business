package com.zuoyu.business.entity;

import com.zuoyu.business.base.BaseEntity;
import com.zuoyu.business.base.BaseModel;

import java.util.List;

/**
 * <pre>
 * Function：
 *
 * Created by JoannChen on 2017/4/24 11:22
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class ChoiceCouponsEntity extends BaseEntity {

    private List<ChoiceCoupons> data;

    public List<ChoiceCoupons> getData() {
        return data;
    }

    public void setData(List<ChoiceCoupons> data) {
        this.data = data;
    }

    public class ChoiceCoupons extends BaseModel {

        private String coupon_id;//优惠类型id
        private String canuse;//可使用数
        private String coupons_name;// 优惠名称
        private int type;// 优惠类型(1：全免优惠，4：时长优惠，5：金额优惠，6：折扣优惠)
        private String coupons_nickname;// 优惠券昵称


        // 有效期截至日期
        // 今日优惠券总数

        private String enddate;
        private String daycount;

        public String getCoupons_nickname() {
            return coupons_nickname;
        }

        public void setCoupons_nickname(String coupons_nickname) {
            this.coupons_nickname = coupons_nickname;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(String coupon_id) {
            this.coupon_id = coupon_id;
        }

        public String getCanuse() {
            return canuse;
        }

        public void setCanuse(String canuse) {
            this.canuse = canuse;
        }

        public String getCoupons_name() {
            return coupons_name;
        }

        public void setCoupons_name(String coupons_name) {
            this.coupons_name = coupons_name;
        }

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }

        public String getDaycount() {
            return daycount;
        }

        public void setDaycount(String daycount) {
            this.daycount = daycount;
        }

        @Override
        public String toString() {
            return "ChoiceCoupons{" +
                    "coupon_id='" + coupon_id + '\'' +
                    ", canuse='" + canuse + '\'' +
                    ", coupons_name='" + coupons_name + '\'' +
                    ", type=" + type +
                    ", coupons_nickname='" + coupons_nickname + '\'' +
                    ", enddate='" + enddate + '\'' +
                    ", daycount='" + daycount + '\'' +
                    '}';
        }
    }

}
