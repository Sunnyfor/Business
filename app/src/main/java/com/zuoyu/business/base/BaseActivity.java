package com.zuoyu.business.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.utils.AutoUtils;
import com.zuoyu.business.R;
import com.zuoyu.business.utils.ToolUtil;
import com.zuoyu.business.utils.http.HttpUtil;
import com.zuoyu.business.widget.LoadingProgress;


/**
 * <pre>
 * Function：Activity 基类
 *
 * Created by Joann on 17/1/22 16:35
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public abstract class BaseActivity extends AutoLayoutActivity implements View.OnClickListener {

    public Context context;
    public Activity activity;

    public HttpUtil httpUtil;// 初始化网络请求
    public LoadingProgress loadingProgress;// 加载进度条
    public TitleManage titleManage;// 标题栏管理类


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //强制屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = this;
        activity = this;


        httpUtil = new HttpUtil(this);
        loadingProgress = new LoadingProgress(this);


        // 配置标题栏管理
        titleManage = new TitleManage(this);

        // 初始化Root布局（最外层父布局）
        LinearLayout rootView = new LinearLayout(this);
        rootView.setBackgroundResource(R.color.bg_theme);
        rootView.setOrientation(LinearLayout.VERTICAL);

        // 初始化子类布局（除去标题栏以外的布局）
        View childView = View.inflate(this, setLayout(), null);
        childView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        AutoUtils.autoSize(childView);
        rootView.addView(childView);

        // 添加标题栏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.px90));

        rootView.addView(titleManage.getTitleView(), 0, params);
        AutoUtils.autoSize(titleManage.getTitleView());

        setContentView(rootView);


        // 子类需要重写的方法
        initTitle();
        initView();
        initAfterLayout(savedInstanceState);


    }

    /**
     * 设置布局文件
     */
    public abstract int setLayout();

    /**
     * 初始化标题栏
     */
    public abstract void initTitle();

    /**
     * 初始化View组件
     */
    public abstract void initView();

    /**
     * 组件点击事件
     */
    public abstract void onClickEvent(View v);

    /**
     * 在加载布局后初始化数据
     */
    public abstract void initAfterLayout(Bundle savedInstanceState);

    /**
     * 简化findViewById 调用：img = id(R.id.img);
     * @param id id名称
     * @param <T> 泛型
     * @return 对应的控件
     */
    @SuppressWarnings("unchecked")
    protected final <T extends View> T id(int id) {
        return (T) findViewById(id);
    }


    @Override
    public void onClick(View v) {
        if (ToolUtil.isFastClick()) {
            onClickEvent(v);
        }
    }


    /**
     * 以下为生命周期
     */


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        httpUtil.cancel();
        super.onDestroy();

    }

}

