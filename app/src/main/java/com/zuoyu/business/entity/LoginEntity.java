package com.zuoyu.business.entity;

import com.zuoyu.business.base.BaseEntity;

import java.io.Serializable;

/**
 * <pre>
 * Function：登录实体类
 *
 * Created by JoannChen on 2017/4/19 10:15
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class LoginEntity extends BaseEntity {

    public UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }

    public class UserInfo implements Serializable {
        String merchantid;// 商户id
        String merchantname;// 商户名
        String parkname;// 停车场名称
        String parkid;// 停车场id
        String token;//用户的token值
        int usecamera;//是否使用摄像头拍照功能1.不使用 2.使用

        public String getMerchantid() {
            return merchantid;
        }

        public String getMerchantname() {
            return merchantname;
        }

        public String getParkname() {
            return parkname;
        }

        public String getParkid() {
            return parkid;
        }

        public String getToken() {
            return token;
        }

        public void setMerchantid(String merchantid) {
            this.merchantid = merchantid;
        }

        public void setMerchantname(String merchantname) {
            this.merchantname = merchantname;
        }

        public void setParkname(String parkname) {
            this.parkname = parkname;
        }

        public void setParkid(String parkid) {
            this.parkid = parkid;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUsecamera() {
            return usecamera;
        }

        public void setUsecamera(int usecamera) {
            this.usecamera = usecamera;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "merchantid=" + merchantid +
                    ", merchantname='" + merchantname + '\'' +
                    ", parkname='" + parkname + '\'' +
                    ", parkid=" + parkid +
                    ", token='" + token + '\'' +
                    '}';
        }
    }


}
