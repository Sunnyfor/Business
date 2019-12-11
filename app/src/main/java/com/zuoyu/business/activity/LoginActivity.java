package com.zuoyu.business.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jaeger.library.StatusBarUtil;
import com.zuoyu.business.R;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.application.UrlManage;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.entity.LoginEntity;
import com.zuoyu.business.utils.EncryptUtil;
import com.zuoyu.business.utils.IntentUtil;
import com.zuoyu.business.utils.LogUtil;
import com.zuoyu.business.utils.SharedUtil;
import com.zuoyu.business.utils.ToastUtil;
import com.zuoyu.business.utils.ToolUtil;
import com.zuoyu.business.utils.http.HttpResult;
import com.zuoyu.business.widget.ClearEditText;

import java.util.Map;
import java.util.TreeMap;

/**
 * <pre>
 * Function：登录界面
 *
 * Created by JoannChen on 2017/4/14 13:43
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class LoginActivity extends BaseActivity {

    private EditText usernameEdit, passwordEdit;
    private Button loginBtn;

    @Override
    public int setLayout() {
        return R.layout.login_main;
    }

    @Override
    public void initTitle() {
        titleManage.hideTitle();
    }

    @Override
    public void initView() {

        // 账号输入框
        usernameEdit = (ClearEditText) findViewById(R.id.et_username);
        usernameEdit.addTextChangedListener(watcher);

        // 密码输入框
        passwordEdit = (ClearEditText) findViewById(R.id.et_password);
        passwordEdit.addTextChangedListener(watcher);

        loginBtn = (Button) findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);

        // 小眼睛
        CheckBox eyeCBox = (CheckBox) findViewById(R.id.cb_eye);
        ToolUtil.isShowPassword(eyeCBox, passwordEdit);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                parseLogin();
                break;
            default:
                break;
        }
    }

    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.bg_theme), 0);
    }

    /**
     * 监听文本变化
     */
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            // 交互：当手机号和验证码均不为空时，且手机号长度为11位，登录按钮高亮显示
            if (usernameEdit.getText().length() < 3
                    || passwordEdit.getText().length() < 6) {
                ToolUtil.setBtnClick(loginBtn, false);
            } else {
                ToolUtil.setBtnClick(loginBtn, true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    /**
     * 【解析】登录
     */
    private void parseLogin() {

        Map<String, String> params = new TreeMap<>();
        params.put("username", usernameEdit.getText().toString());// 账户
        params.put("password", EncryptUtil.getMD5(passwordEdit.getText().toString()));// 密码


        httpUtil.post(params, UrlManage.LOGIN_URL, new HttpResult<LoginEntity>() {

            @Override
            public void onSuccess(LoginEntity result) {

                // 保存用户信息/Token
                SharedUtil.setObject(SharedUtil.USER_INFO, result.getData());
                LogUtil.e("【登录】mUserInfo:" + SharedUtil.getObject(SharedUtil.USER_INFO));

                SharedUtil.setString(SharedUtil.TOKEN, result.getData().getToken());
                SharedUtil.setString(SharedUtil.USER_ID, result.getData().getMerchantid());
                SharedUtil.setString(SharedUtil.PARK_ID, result.getData().getParkid());
                SharedUtil.setInteger(SharedUtil.USE_CAMERA, result.getData().getUsecamera());

                IntentUtil.start(activity, HomeActivity.class, true);

            }

            @Override
            public void onFailed(int errCord, String errMsg) {
                ToastUtil.show(errMsg);
            }
        });
    }

}
