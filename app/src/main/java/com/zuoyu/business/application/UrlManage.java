package com.zuoyu.business.application;

/**
 * <pre>
 * Function：网络请求Url管理类
 *
 * Created by JoannChen on 2017/3/7 16:01
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class UrlManage {

    /*
     * 正式环境
     */
    private static final String HOST = "http://cloudcenter.51park.cn/CloudCenter/";

    /*
     * beta测试环境
     */
//    private static final String HOST = "http://betacloudcenter.51park.cn/CloudCenter/";

    /*
     * alpha测试环境
     */
//    private static final String HOST = "http://alphacloudcenter.51park.cn/CloudCenter/";
//

    /**
     * 1．登录
     */
    public static final String LOGIN_URL = HOST + "mtlogin";


    /**
     * 2．修改密码
     */
    public static final String MODIFY_PW_URL = HOST + "mtupdatepwd";


    /**
     * 3．退出登录
     */
    public static final String EXIT_URL = HOST + "mtexit";


    /**
     * 4．优惠记录
     */
    public static final String COUPONS_URL = HOST + "mtcoupons";


    /**
     * 5．获取优惠券
     */
    public static final String GET_COUPONS_URL = HOST + "getcoupons";


    /**
     * 6．使用优惠券
     */
    public static final String USE_COUPONS_URL = HOST + "usecoupons";


    /**
     * 7．查找车辆
     */
    public static final String SEARCH_CAR_URL = HOST + "selectcar";


    /**
     * 8．意见反馈
     */
    public static final String FEEDBACK_URL = HOST + "feedback";


    /**
     * 9．获取账户余额
     */
    public static final String REMAINING_URL = HOST + "remaining";


    /**
     * 10．使用优惠券—余额优惠
     */
    public static final String USETIME_URL = HOST + "usetime";

    /**
     * 11. 上传凭证
     */
    public static final String UPLOAD_IMAGE_URL = HOST + "uploadimage";

    /**
     * 100．是否升级询问
     */
    public static final String IS_UPDATE_URL = HOST + "isupdate";

}
