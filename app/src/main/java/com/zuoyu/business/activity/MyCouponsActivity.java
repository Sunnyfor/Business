package com.zuoyu.business.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.zuoyu.business.R;
import com.zuoyu.business.adapter.MyCouponsAdapter;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.application.UrlManage;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.entity.ChoiceCouponsEntity;
import com.zuoyu.business.utils.PullUtil;
import com.zuoyu.business.utils.SharedUtil;
import com.zuoyu.business.utils.http.HttpResult;
import com.zuoyu.business.widget.pullable.PullListView;
import com.zuoyu.business.widget.pullable.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Function：我的优惠券
 *
 * Created by JoannChen on 2017/6/12 11:16
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class MyCouponsActivity extends BaseActivity {

    private PullUtil pullUtil;
    private MyCouponsAdapter adapter;
    private List<ChoiceCouponsEntity.ChoiceCoupons> list = new ArrayList<>();

    @Override
    public int setLayout() {
        return R.layout.layout_pull;
    }

    @Override
    public void initTitle() {
        titleManage.setTitleText(getResources().getString(R.string.my_coupons));
    }

    @Override
    public void initView() {

        // 下拉刷新／上拉加载
        PullToRefreshLayout refreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshLayout);
        final PullListView listView = (PullListView) findViewById(R.id.listView);
//        listView.setDividerHeight(24);

        pullUtil = new PullUtil(refreshLayout);
        pullUtil.setPerPageCount(15);
        adapter = new MyCouponsAdapter(context, list);
        listView.setAdapter(adapter);


        pullUtil.setOnLoadListener(new PullUtil.onLoadListener() {
            @Override
            public void onLoad(boolean flag, Map<String, String> params) {
                parseChoiceCoupons(params);
            }
        });
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
     * 【解析】选择优惠券
     */
    private void parseChoiceCoupons(Map<String, String> params) {


        params.put("merchant_id", SharedUtil.getString(SharedUtil.USER_ID));


        httpUtil.post(params, UrlManage.GET_COUPONS_URL, new HttpResult<ChoiceCouponsEntity>() {

            @Override
            public void onSuccess(ChoiceCouponsEntity result) {

                pullUtil.setResult(adapter, list, result.getData());
                pullUtil.setEmptyView(list, R.mipmap.icon_no_coupons, getString(R.string.sorry_no_coupons));
            }

            @Override
            public void onFailed(int errCord, String errMsg) {

            }
        });

    }


}
