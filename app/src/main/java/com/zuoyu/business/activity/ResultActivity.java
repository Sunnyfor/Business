package com.zuoyu.business.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zuoyu.business.R;
import com.zuoyu.business.application.Constant;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.utils.IntentUtil;
import com.zuoyu.business.utils.ViewUtil;

/**
 * <pre>
 * Function：扫描结果界面
 *
 * Created by JoannChen on 2017/4/20 17:27
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class ResultActivity extends BaseActivity {

    int result;

    ImageView closeImg;

    TextView timeText;
    TextView backText;

    TimerCount timer;


    @Override
    public int setLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.result_main;
    }

    @Override
    public void initTitle() {
        titleManage.hideTitle();
    }

    @Override
    public void initView() {
        timer = new TimerCount();

        closeImg = (ImageView) findViewById(R.id.iv_close);
        closeImg.setOnClickListener(this);


        LinearLayout backLLayout = (LinearLayout) findViewById(R.id.ll_back);
        timeText = (TextView) findViewById(R.id.tv_time);
        backText = (TextView) findViewById(R.id.tv_back);
        /*
         * 返回物理键和返回首页按钮事件冲突
         */
//        backText.setOnClickListener(this);


        ImageView stateImg = (ImageView) findViewById(R.id.iv_state);

        result = getIntent().getIntExtra(Constant.SCAN_RESULT, 0);

        /*
         * ==========================================
         * 扫描结果
         * 1.非无忧停车小票
         * 2.该小票已失效！（车辆已经出场或重复取票）
         * 3.未找到符合条件的小票，请尝试查询车牌号
         * ==========================================
         * 优惠券发放结果
         * 609.该车辆已有优惠券
         * 611.该优惠券使用已达上限
         * 200.优惠券发放成功
         */
        switch (result) {
            case 1:
                stateImg.setImageResource(R.mipmap.bg_ticked_no_51park);
                ViewUtil.setViewSize(stateImg, ViewUtil.getHeight(710), 0);
                break;
            case 2:
                stateImg.setImageResource(R.mipmap.bg_ticked_invalid);
                break;
            case 3:
                stateImg.setImageResource(R.mipmap.bg_ticket_not_find);
                break;
            case 609:
                stateImg.setImageResource(R.mipmap.bg_coupons_already_send);
                break;
            case 611:
                stateImg.setImageResource(R.mipmap.bg_coupons_send_run_out);
                break;
            case 200:
                stateImg.setImageResource(R.mipmap.bg_coupons_send);
                backLLayout.setVisibility(View.VISIBLE);
                closeImg.setVisibility(View.GONE);
                timer.start();
                break;
            default:
                stateImg.setImageResource(R.mipmap.bg_ticked_no_51park);
                ViewUtil.setViewSize(stateImg, ViewUtil.getHeight(710), 0);
                break;
        }

        stateImg.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_back:
                timer.cancel();
                IntentUtil.start(activity, HomeActivity.class, true);
                break;
        }
    }

    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
    }


    /**
     * 发送验证码
     */
    private class TimerCount extends CountDownTimer {

        private TimerCount() {
            super(4 * 1000, 1000);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String timer = "（" + millisUntilFinished / 1000 + "s）";
            timeText.setText(timer);
        }

        @Override
        public void onFinish() {
            IntentUtil.start(activity, HomeActivity.class, true);
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            timer.cancel();
            IntentUtil.start(activity, HomeActivity.class, true);
        }
        return super.onKeyDown(keyCode, event);
    }
}
