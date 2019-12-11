package com.zuoyu.business.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zuoyu.business.R;
import com.zuoyu.business.utils.LogUtil;
import com.zuoyu.business.utils.ToolUtil;


/**
 * <pre>
 * Function：自定义加载进度框
 *
 * Created by JoannChen on 2017/3/10 15:48
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class LoadingProgress extends ProgressDialog {

    private Context context;

    // 加载文字（传值）
    private String loadingContent;


    // 加载框
    private ImageView loadingImg;
    private TextView loadingText;

    // 加载动画
    private AnimationDrawable animation;
    private RotateAnimation mAnimation;

    /**
     * 自定义无忧停车动画加载进度的对话框
     *
     * @param context 上下文对象
     */
    public LoadingProgress(Context context) {
        super(context, R.style.Loading_Dialog_Style);
        this.context = context;
        this.loadingContent = context.getString(R.string.loading_in);
        setCanceledOnTouchOutside(true);
    }

    /**
     * 自定义无忧停车动画加载进度的对话框
     *
     * @param context 上下文对象
     * @param info    提示信息
     */
    public LoadingProgress(Context context, String info) {
        super(context, R.style.Loading_Dialog_Style);
        this.context = context;
        this.loadingContent = !ToolUtil.isEmpty(info) ? info : context.getString(R.string.loading_in);
        setCanceledOnTouchOutside(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initView();
//        initData();
    }

    private void initView() {

        setContentView(R.layout.layout_loading);


        // 加载动画展示
        loadingImg = (ImageView) findViewById(R.id.iv_icon);
        loadingImg.setFocusable(false);

        // 加载提示文字
        loadingText = (TextView) findViewById(R.id.tv_text);

        mAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.anim_rotating);
        loadingImg.setAnimation(mAnimation);
        loadingText.setText(loadingContent);

        LogUtil.i("执行动画:--------------");


    }

    private void initData() {


//        // 通过ImageView对象拿到背景显示的AnimationDrawable
//        animation = (AnimationDrawable) loadingImg.getBackground();


//        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
//        loadingImg.post(new Runnable() {
//            @Override
//            public void run() {
//                mAnimation.start();
//            }
//        });

//        设置文字

    }

    @Override
    public void show() {
        super.show();
        initView();
    }
}
