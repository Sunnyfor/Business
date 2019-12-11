package com.zuoyu.business.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.zuoyu.business.R;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.application.UrlManage;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.entity.LoginEntity;
import com.zuoyu.business.entity.LongEntity;
import com.zuoyu.business.utils.SharedUtil;
import com.zuoyu.business.utils.ToolUtil;
import com.zuoyu.business.utils.http.HttpResult;

import java.util.Map;
import java.util.TreeMap;

/**
 * <pre>
 * Function：个人中心界面
 *
 * Created by JoannChen on 2017/6/29 10:22
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * </pre>
 */
public class MineActivity extends BaseActivity {

    private TextView longText;
    private LoginEntity.UserInfo mUserInfo;

    @Override
    public int setLayout() {
        return R.layout.mine_main;
    }

    @Override
    public void initTitle() {
        titleManage.hideTitle();
    }

    @Override
    public void initView() {

        try {
            mUserInfo = (LoginEntity.UserInfo) SharedUtil.getObject(SharedUtil.USER_INFO);
        } catch (Exception e) {
            MyApplication.exitApplication(activity);
        }

        ImageView backImg = (ImageView) findViewById(R.id.iv_back);
        backImg.setOnClickListener(this);

        // 商家名称
        TextView nameText = (TextView) findViewById(R.id.tv_name);
        nameText.setText(mUserInfo.getMerchantname());
        nameText.setSingleLine();

        // 商家地址
        TextView addressText = (TextView) findViewById(R.id.tv_address);
        addressText.setText(mUserInfo.getParkname());
        addressText.setSingleLine();

        // 账户余额
        longText = (TextView) findViewById(R.id.tv_long);

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
        }
    }

    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.bg_theme), 0);
        parseGetLong();
    }


    /**
     * 【解析】获取账户余额时长
     */
    private void parseGetLong() {
        Map<String, String> params = new TreeMap<>();
        params.put("merchant_id", SharedUtil.getString(SharedUtil.USER_ID));

        httpUtil.post(params, UrlManage.REMAINING_URL, new HttpResult<LongEntity>() {
            @Override
            public void onSuccess(LongEntity result) {

                longText.setText((ToolUtil.isEmpty(result.getData().getRemaining()) ? "未充值" : result.getData().getRemaining()));
            }

            @Override
            public void onFailed(int errCord, String errMsg) {

            }
        });
    }

}
