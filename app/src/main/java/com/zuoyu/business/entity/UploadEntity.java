package com.zuoyu.business.entity;

import com.zuoyu.business.base.BaseEntity;
import com.zuoyu.business.base.BaseModel;

/**
 * <pre>
 * Function：上传凭证
 *
 * Created by JoannChen on 2017/10/26 10:01
 * E-mail:Q8622268@foxmail.com
 * QQ:411083907
 * </pre>
 */
public class UploadEntity extends BaseEntity {

    public UploadInfo data;

    public UploadInfo getData() {
        return data;
    }

    public void setData(UploadInfo data) {
        this.data = data;
    }

    public class UploadInfo extends BaseModel {
        private String imageurl;

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        @Override
        public String toString() {
            return "UploadInfo{" +
                    "imageurl='" + imageurl + '\'' +
                    '}';
        }
    }


}
