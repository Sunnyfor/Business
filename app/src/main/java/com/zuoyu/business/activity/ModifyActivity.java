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
import com.zuoyu.business.base.BaseEntity;
import com.zuoyu.business.utils.EncryptUtil;
import com.zuoyu.business.utils.SharedUtil;
import com.zuoyu.business.utils.ToastUtil;
import com.zuoyu.business.utils.ToolUtil;
import com.zuoyu.business.utils.http.HttpResult;

import java.util.Map;
import java.util.TreeMap;

/**
 * <pre>
 * Function：修改密码界面
 *
 * Created by JoannChen on 2017/4/18 14:11
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class ModifyActivity extends BaseActivity {

    EditText oldPwdEdit, newPwdEdit;
    Button sureBtn;

    @Override
    public int setLayout() {
        return R.layout.modify_main;
    }

    @Override
    public void initTitle() {
        titleManage.setTitleText(getString(R.string.modify_password));
    }

    @Override
    public void initView() {

        oldPwdEdit = (EditText) findViewById(R.id.et_old_password);
        oldPwdEdit.addTextChangedListener(watcher);
        newPwdEdit = (EditText) findViewById(R.id.et_new_password);
        newPwdEdit.addTextChangedListener(watcher);


        CheckBox oldEyeCBox = (CheckBox) findViewById(R.id.iv_oldEye);
        CheckBox newEyeCBox = (CheckBox) findViewById(R.id.iv_newEye);
        ToolUtil.isShowPassword(oldEyeCBox, oldPwdEdit);
        ToolUtil.isShowPassword(newEyeCBox, newPwdEdit);

        sureBtn = (Button) findViewById(R.id.btn_sure);
        sureBtn.setOnClickListener(this);


    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                parseModify();
                break;
        }
    }

    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.orange), 0);
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
            if (oldPwdEdit.getText().length() < 6 || newPwdEdit.getText().length() < 6) {
                ToolUtil.setBtnClick(sureBtn, false);
            } else {
                ToolUtil.setBtnClick(sureBtn, true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    /**
     * 【解析】修改密码
     */
    private void parseModify() {

        String oldPwd = oldPwdEdit.getText().toString();
        String newPwd = newPwdEdit.getText().toString();

        if (oldPwd.length() < 6 || newPwd.length() < 6) {
            ToastUtil.show("请输入6位-20位密码");
            return;
        }


        Map<String, String> params = new TreeMap<>();
        params.put("merchant_id", SharedUtil.getString(SharedUtil.USER_ID));
        params.put("oldpwd", EncryptUtil.getMD5(oldPwd));// 账户
        params.put("newpwd", EncryptUtil.getMD5(newPwd));// 密码

        httpUtil.post(params, UrlManage.MODIFY_PW_URL, new HttpResult<BaseEntity>() {

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
