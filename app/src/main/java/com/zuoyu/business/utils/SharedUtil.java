package com.zuoyu.business.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.zuoyu.business.application.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * <pre>
 * Function：SharedPreferences保存数据工具类
 *
 * Created by JoannChen on 2017/3/10 15:48
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class SharedUtil {

    /**
     * 文件名
     */
    private static final String FILE_NAME = "51Manager";

    // string token
    public static final String TOKEN = "token";


    // string token
    public static final String USER_INFO = "userInfo";

    // string 用户id
    public static final String USER_ID = "userId";

    // string 车场id
    public static final String PARK_ID = "parkId";

    // int 是否使用摄像头拍照功能1.不使用 2.使用
    public static final String USE_CAMERA = "use_camera";

    // boolean 不提示更新吗？
    // string 点击忽略按钮时的时间，用来计算下次提示更新的时间
    public static final String IS_UPDATE = "noUpdate";
    public static final String UPDATE_TIME = "updateTime";

    //保存意见反馈的内容
    public static final String FEEDBACK_CONTENT = "feedbackContent";

    /**
     * 是否首次进入优惠记录界面
     */
    public static final String IS_FIRST_COUPONS = "isFirstCoupons";


    /**
     * 将所有本地数据置空
     */
    public static void setEmptyAllData() {
        setObject(USER_INFO, null);

        setString(TOKEN, "");
        setString(USER_ID, "");
        setString(PARK_ID, "");
        setString(UPDATE_TIME, "");
        setString(FEEDBACK_CONTENT, "");

        setBoolean(IS_UPDATE, false);
        setBoolean(IS_FIRST_COUPONS, true);

        setInteger(USE_CAMERA, 0);
    }

    /**
     * 获取SharedPreferences对象
     *
     * @return SharedPreferences
     */
    private static SharedPreferences getSharedPreferences() {

        return MyApplication.getInstance()
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }


    /**
     * 保存String信息
     *
     * @param key     键名
     * @param content 默认值
     */
    public static void setString(String key, String content) {
        getSharedPreferences().edit().putString(key, content).apply();
    }

    public static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }


    /**
     * 保存Boolean类型的信息
     *
     * @param key  键名
     * @param flag 默认值
     */
    public static void setBoolean(String key, boolean flag) {
        getSharedPreferences().edit().putBoolean(key, flag).apply();
    }

    public static boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean flag) {
        return getSharedPreferences().getBoolean(key, flag);
    }

    /**
     * 保存Integer信息
     *
     * @param key     键名
     * @param content 默认值
     */
    public static void setInteger(String key, int content) {
        getSharedPreferences().edit().putInt(key, content).apply();
    }

    public static int getInteger(String key) {
        return getSharedPreferences().getInt(key, 0);
    }


    /**
     * 使用SharedPreference保存对象
     *
     * @param key 储存对象的key
     * @param obj 储存的对象
     */
    public static void setObject(String key, Object obj) {
        getSharedPreferences().edit().putString(key, Object2String(obj)).apply();
    }

    /**
     * 获取SharedPreference保存的对象
     *
     * @param key 储存对象的key
     * @return object 返回根据key得到的对象
     */
    public static Object getObject(String key) {
        String string = getSharedPreferences().getString(key, null);
        return string != null ? String2Object(string) : null;
    }

    /* ========================================================================== */

    /**
     * writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
     * 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
     *
     * @param object 待加密的转换为String的对象
     * @return String   加密后的String
     */
    private static String Object2String(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            objectOutputStream.close();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用Base64解密String，返回Object对象
     *
     * @param objectString 待解密的String
     * @return object      解密后的object
     */
    private static Object String2Object(String objectString) {
        byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}
