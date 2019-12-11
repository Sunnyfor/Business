package com.zuoyu.business.application;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;
import com.zuoyu.business.activity.LoginActivity;
import com.zuoyu.business.utils.LogUtil;
import com.zuoyu.business.utils.SharedUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * <pre>
 * Function：
 *
 * Created by Joann on 17/1/22 17:15
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class MyApplication extends Application {

    /**
     * 全局Context
     */
    private static Context applicationContext;

    /**
     * 存放所有的Activity
     */
    public static List<Activity> activityList;


    @Override
    public void onCreate() {
        super.onCreate();


        // 关闭Log
        LogUtil.closeLog();

        applicationContext = getApplicationContext();

        /*
         * 初始化HttpClient
         */
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("网络请求"))
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);

        // android 7.0 调用系统相机崩溃的解决方案
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    /**
     * @return 全局上下文
     */
    public static Context getInstance() {
        return applicationContext;
    }


    /**
     * 将Activity添加到集合
     *
     * @param activity 当前操作的Activity
     */
    public static void addActivity(Activity activity) {

        if (activityList == null) {
            activityList = new ArrayList<>();
        }

        activityList.add(activity);
    }


    /**
     * 将Activity从集合移除
     */
    public static void removeActivity() {

        for (Activity activity : activityList) {
            if (null != activity) {
                activity.finish();
            }
        }

        activityList = null;

    }

    /**
     * 退出应用程序
     */
    public static void exitApplication(Activity activity) {

        SharedUtil.setEmptyAllData();
//        IntentUtil.start(activity, LoginActivity.class, true);
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();

        removeActivity();


    }


}
