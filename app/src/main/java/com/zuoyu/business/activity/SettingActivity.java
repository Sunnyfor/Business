package com.zuoyu.business.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.zuoyu.business.R;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.application.UrlManage;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.base.BaseEntity;
import com.zuoyu.business.utils.IntentUtil;
import com.zuoyu.business.utils.SharedUtil;
import com.zuoyu.business.utils.ToolUtil;
import com.zuoyu.business.utils.http.HttpResult;

import java.util.Map;
import java.util.TreeMap;

/**
 * <pre>
 * Function：设置页面
 *
 * Created by JoannChen on 2017/6/12 09:54
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class SettingActivity extends BaseActivity {

    private Dialog dialog;

    @Override
    public int setLayout() {
        return R.layout.setting_main;
    }

    @Override
    public void initTitle() {
        titleManage.setTitleText(getResources().getString(R.string.setting));
    }

    @Override
    public void initView() {

        RelativeLayout modifyRLayout = (RelativeLayout) findViewById(R.id.rl_modify);
        modifyRLayout.setOnClickListener(this);

        RelativeLayout feedbackRLayout = (RelativeLayout) findViewById(R.id.rl_feedback);
        feedbackRLayout.setOnClickListener(this);

        TextView versionText = (TextView) findViewById(R.id.tv_version);
        versionText.setText(("V" + ToolUtil.getVersionName()));

        Button exitBtn = (Button) findViewById(R.id.btn_exit);
        exitBtn.setEnabled(true);
        exitBtn.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {

        switch (v.getId()) {
            case R.id.rl_modify:
                IntentUtil.start(activity, ModifyActivity.class, false);
                break;
            case R.id.rl_feedback:
                IntentUtil.start(activity, FeedbackActivity.class, false);
                break;
            case R.id.btn_exit:
                showExitDialog();
                break;
            case R.id.btn_sure:
                dialog.dismiss();
                parseExit();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }

    }

    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.orange), 0);
    }


    /**
     * 显示退出登录对话框
     */
    private void showExitDialog() {

        dialog = new Dialog(context, R.style.exit_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.show();

        Button sureBtn = (Button) dialog.findViewById(R.id.btn_sure);
        Button cancelBtn = (Button) dialog.findViewById(R.id.btn_cancel);

        sureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

    }


    /**
     * 【解析】退出登录
     */
    private void parseExit() {

        Map<String, String> params = new TreeMap<>();
        params.put("merchant_id", SharedUtil.getString(SharedUtil.USER_ID));

        httpUtil.post(params, UrlManage.EXIT_URL, new HttpResult<BaseEntity>() {


            @Override
            public void onSuccess(BaseEntity result) {

                MyApplication.exitApplication(activity);

            }

            @Override
            public void onFailed(int errCord, String errMsg) {

            }
        });
    }


}
