package com.zuoyu.business.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.zuoyu.business.R;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.application.UrlManage;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.base.BaseEntity;
import com.zuoyu.business.utils.SharedUtil;
import com.zuoyu.business.utils.ToastUtil;
import com.zuoyu.business.utils.ToolUtil;
import com.zuoyu.business.utils.http.HttpResult;

import java.util.Map;
import java.util.TreeMap;

import static com.zuoyu.business.R.id.et_input;


/**
 * <pre>
 * Function：意见反馈
 *
 * Created by JoannChen on 2017/6/9 11:03
 * QQ:411083907
 * E-mail:chenyongzuo@51park.com.cn
 * Version Information：V 1.0
 * Copyright Notice：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class FeedbackActivity extends BaseActivity {

    private TextView numText;
    private EditText inputEdit;
    private Button submitBtn;


    @Override
    public int setLayout() {
        return R.layout.feedback_main;
    }


    @Override
    public void initTitle() {
        titleManage.setTitleText(getString(R.string.feedback));
    }

    @Override
    public void initView() {
//        字数
        numText = id(R.id.tv_num);

        submitBtn = id(R.id.btn_sure);
        submitBtn.setOnClickListener(this);
        submitBtn.setEnabled(false);


//        输入框
        inputEdit = id(et_input);

//        保留意见反馈的内容，光标置后
        if (!ToolUtil.isEmpty(SharedUtil.getString(SharedUtil.FEEDBACK_CONTENT))) {
            inputEdit.setText(SharedUtil.getString(SharedUtil.FEEDBACK_CONTENT));
            inputEdit.setSelection(inputEdit.getText().length());
            ToolUtil.setBtnClick(submitBtn, true);
            numText.setText(((200 - inputEdit.getText().length()) + "字"));

        }

        inputEdit.addTextChangedListener(watcher);


    }


    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                parseSubmitFeedback();
//                提交后将本地意见反馈置空
                SharedUtil.setString(SharedUtil.FEEDBACK_CONTENT, "");
                break;
        }
    }


    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.orange), 0);
    }


    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            if (inputEdit.getText().length() == 0) {
                ToolUtil.setBtnClick(submitBtn, false);
            } else {
                ToolUtil.setBtnClick(submitBtn, true);
            }

//            String num = (200 - charSequence.length()) + "字";
            numText.setText(((200 - charSequence.length()) + "字"));

        }

        @Override
        public void afterTextChanged(Editable editable) {
            SharedUtil.setString(SharedUtil.FEEDBACK_CONTENT, inputEdit.getText().toString());
        }
    };


    /**
     * 【解析】提交意见反馈
     */
    private void parseSubmitFeedback() {

        Map<String, String> params = new TreeMap<>();
        params.put("merchant_id", SharedUtil.getString(SharedUtil.USER_ID));//用户id
        params.put("content", inputEdit.getText().toString());//意见反馈内容
        params.put("type", "1");//意见反馈类型（1：商户端）

        httpUtil.post(params, UrlManage.FEEDBACK_URL, new HttpResult<BaseEntity>() {

            @Override
            public void onSuccess(BaseEntity result) {
                ToastUtil.show("感谢您的反馈！");
                finish();
            }

            @Override
            public void onFailed(int errCord, String errMsg) {

            }
        });

    }
}
