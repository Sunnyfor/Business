package com.zuoyu.business.entity;

import com.zuoyu.business.base.BaseEntity;

/**
 * <pre>
 * Function：账户余额（停车时长）实体类
 *
 * Created by JoannChen on 2017/6/28 16:38
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * </pre>
 */
public class LongEntity extends BaseEntity {

    public LongInfo data;

    public LongInfo getData() {
        return data;
    }

    public void setData(LongInfo data) {
        this.data = data;
    }

    public class LongInfo {
        //账户余额（x小时x分钟）
        // x（不带单位）
        // xx（不带单位）
        private String remaining;

        private String hour;
        private String minute;

        public String getRemaining() {
            return remaining;
        }

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }

        public String getMinute() {
            return minute;
        }

        public void setMinute(String minute) {
            this.minute = minute;
        }

        public void setRemaining(String remaining) {
            this.remaining = remaining;
        }

        @Override
        public String toString() {
            return "LongInfo{" +
                    "remaining='" + remaining + '\'' +
                    ", hour='" + hour + '\'' +
                    ", minute='" + minute + '\'' +
                    '}';
        }
    }
}