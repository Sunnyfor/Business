package com.zuoyu.business.application;

/**
 * <pre>
 * Function：常量类
 *
 * Created by Joann on 2017/2/28 14:23
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class Constant {

    /**
     * App配置参数
     */
    public static final String PROJECT_ID = "merchant";
    public static final String PROJECT_KEY = "7A1B429E73D6FB97730F7A5E5667DCDD";

    /**
     * App更新提供
     */
    public static final String APP_ID = "merchant_AN";


    /**
     * 相机权限
     */
    public static final int PERMISSION_REQUEST_CAMERA = 1;




    /**
     * 联系电话
     */
//    public static final String Company_Tel = "400-709-5151";


    //============================= Intent跳转传参数 ======================================

//    /**
//     * 判断是从《登录》还是《首页》进入选择停车场页面
//     */
//    public static final String IS_FROM_LOGIN = "isFormLoginPage";
//
//    /**
//     * 判断是《重置密码》还是《修改密码》
//     */
//    public static final String IS_RESET_PASSWORD = "isResetPassword";
//
//    /**
//     * 判断是从《订单记录》还是《在场记录》进入详情页面
//     */
//    public static final String IS_FROM_ORDER = "isFormOrderPage";
//
//    /**
//     * 判断是从《日报表》还是《月报表》进入
//     */
//    public static final String IS_FROM_DAY_REPORT = "isFromDayReport";
//
    /**
     * 查看大图时，路径
     */
    public static final String IMG_URL = "imgUrl";
//

    /**
     * 扫描结果
     * 1.非无忧停车小票
     * 2.该小票已失效！（车辆已经出场或重复取票）
     * 3.未找到符合条件的小票，请尝试查询车牌号
     * ==========================================
     * 优惠券发放结果
     * 11.优惠券发放失败
     * 12.优惠券发放成功
     */
    public static final String SCAN_RESULT = "scanResult";


    /**
     * 停车场信息，搜索页／扫描页传入停车页
     */
    public static final String PARK_INFO = "parkInfo";


    /**
     * 优惠券信息
     */
    public static final String COUPONS_INFO = "couponsInfo";

    /**
     * 优惠时长（小时为单位）
     */
    public static final String COUPONS_HOUR = "couponsLong";


}
