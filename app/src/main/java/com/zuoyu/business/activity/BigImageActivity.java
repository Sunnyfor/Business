package com.zuoyu.business.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.zuoyu.business.R;
import com.zuoyu.business.application.Constant;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.base.BaseActivity;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.zuoyu.business.R.id.imageView;

/**
 * <pre>
 * Function：查看大图
 *
 * Created by JoannChen on 2017/4/21 14:03
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class BigImageActivity extends BaseActivity {


    @Override
    public int setLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.big_image_main;
    }

    @Override
    public void initTitle() {
        titleManage.hideTitle();
    }

    @Override
    public void initView() {

        PhotoView photoView = (PhotoView) this.findViewById(imageView);

        String url = getIntent().getStringExtra(Constant.IMG_URL);
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.bg_park_default)
                .into(photoView);

        //单击退出
        photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                finish();
            }
        });

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
        StatusBarUtil.setTransparent(this);
    }

}
