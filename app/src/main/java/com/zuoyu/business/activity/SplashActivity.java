package com.zuoyu.business.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.zuoyu.business.R;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.utils.IntentUtil;
import com.zuoyu.business.utils.LogUtil;
import com.zuoyu.business.utils.SharedUtil;

/**
 * <pre>
 * Function：启动页
 *
 * Created by JoannChen on 2017/4/14 10:28
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </
 */
public class SplashActivity extends BaseActivity {

    private Handler handler = new Handler();

    @Override
    public int setLayout() {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.splash_main;
    }

    @Override
    public void initTitle() {
        titleManage.hideTitle();
    }

    @Override
    public void initView() {
        ImageView imageView = (ImageView) findViewById(R.id.iv_icon);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_splash);
        imageView.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        LogUtil.e("【启动】mUserInfo:" + SharedUtil.getObject(SharedUtil.USER_INFO));
                        if (SharedUtil.getObject(SharedUtil.USER_INFO) == "" || SharedUtil.getObject(SharedUtil.USER_INFO) == null) {
                            IntentUtil.start(activity, LoginActivity.class, true);
                        } else {
                            IntentUtil.start(activity, HomeActivity.class, true);
                        }
                    }
                }, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.bg_theme), 0);
    }
}
