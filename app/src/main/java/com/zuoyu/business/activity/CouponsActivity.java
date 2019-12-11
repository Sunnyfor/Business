package com.zuoyu.business.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.zuoyu.business.R;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.base.BaseViewPagerFragment;
import com.zuoyu.business.fragment.BalanceCouponsFragment;
import com.zuoyu.business.fragment.CapCouponsFragment;
import com.zuoyu.business.fragment.DiscountCouponsFragment;
import com.zuoyu.business.fragment.FreeCouponsFragment;
import com.zuoyu.business.fragment.LongCouponsFragment;
import com.zuoyu.business.fragment.MoneyCouponsFragment;
import com.zuoyu.business.utils.SharedUtil;

import java.util.ArrayList;

/**
 * <pre>
 * Function：优惠记录界面
 *
 * Created by JoannChen on 2017/4/19 09:33
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class CouponsActivity extends BaseActivity {

    @Override
    public int setLayout() {
        return R.layout.coupons_main;
    }

    @Override
    public void initTitle() {
        titleManage.setTitleText(getString(R.string.coupons_record));

    }

    @Override
    public void initView() {
        showViewPager();

        if (SharedUtil.getBoolean(SharedUtil.IS_FIRST_COUPONS, true)) {
            showPromptDialog();
        }
    }

    @Override
    public void onClickEvent(View v) {
    }

    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.orange), 0);
    }

    /**
     * FragmentLayout界面赋值
     */
    private void showViewPager() {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ArrayList<Class<?>> list = new ArrayList<>();

        list.add(MoneyCouponsFragment.class);
        list.add(LongCouponsFragment.class);
        list.add(DiscountCouponsFragment.class);
        list.add(FreeCouponsFragment.class);
        list.add(BalanceCouponsFragment.class);
        list.add(CapCouponsFragment.class);


//        String[] array = new String[]{"金额", "时长", "折扣", "全免","余额"};
//        String[] array = new String[]{"金额优惠", "时长优惠", "折扣优惠", "全免优惠","余额优惠","aa","bb","Beautiful","ddd","EeEeEeEeEeEeEeEeEeEe"};
        String[] array = new String[]{"金额优惠", "时长优惠", "折扣优惠", "全免优惠", "余额优惠", "封顶优惠"};
        ft.replace(R.id.ll_container, BaseViewPagerFragment
                .newInstance()
                .setFragmentObjList(array, list)
                .setViewpagerIndxe(0)
                .setViewpagerCacheLimit(list.size())
                .setSlidingTabStripImage(
                        R.mipmap.bg_tab_unchecked,
                        R.mipmap.bg_tab_checked3)
                .setTextColorSelect(R.color.selector_slide_tab_font))
                .commit();
    }


    /**
     * 展示左右滑动的提示框
     */
    private void showPromptDialog() {
        final Dialog dialog = new Dialog(context, R.style.Dialog_Theme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_slide_prompt);
        dialog.show();

        ImageView iconImg = dialog.findViewById(R.id.iv_icon);
        iconImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        SharedUtil.setBoolean(SharedUtil.IS_FIRST_COUPONS, false);
    }


}
