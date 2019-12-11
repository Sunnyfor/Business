package com.zuoyu.business.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zuoyu.business.R;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.fragment.ChoiceBalanceFragment;
import com.zuoyu.business.fragment.ChoiceCouponsFragment;
import com.zuoyu.business.utils.ViewUtil;

/**
 * <pre>
 * Function：选择优惠界面
 *
 * Created by JoannChen on 2017/4/24 11:05
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class ChoiceCouponsActivity extends BaseActivity {

    private ChoiceCouponsFragment couponsFragment;
    private ChoiceBalanceFragment balanceFragment;

    @Override
    public int setLayout() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.choice_coupons_main;
    }

    @Override
    public void initTitle() {
        titleManage.hideTitle();
    }

    @Override
    public void initView() {

        // 关闭按钮
        ImageView closeImg = (ImageView) findViewById(R.id.iv_close);
        closeImg.setOnClickListener(this);


        couponsFragment = new ChoiceCouponsFragment();
        balanceFragment = new ChoiceBalanceFragment();


        showFragment(couponsFragment);


        // 默认选中第一项
        RadioButton couponsRBtn = (RadioButton) findViewById(R.id.rb_coupons);
        ViewUtil.setTextSize(couponsRBtn, 28);
        couponsRBtn.setChecked(true);

        RadioButton balanceRBtn = (RadioButton) findViewById(R.id.rb_balance);
        ViewUtil.setTextSize(balanceRBtn, 28);

        // 选项卡切换
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ViewUtil.setViewSize(radioGroup, 60, 306);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_coupons:
                        showFragment(couponsFragment);
                        break;
                    case R.id.rb_balance:
                        showFragment(balanceFragment);
                        break;
                }
            }
        });


    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
        }
    }


    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
    }

    /**
     * Fragment切换
     *
     * @param fragment fragment
     */
    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_container, fragment).commit();
    }

}
