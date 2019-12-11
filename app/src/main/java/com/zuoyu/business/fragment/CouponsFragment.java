package com.zuoyu.business.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zuoyu.business.R;
import com.zuoyu.business.adapter.CouponsAdapter;
import com.zuoyu.business.application.UrlManage;
import com.zuoyu.business.entity.CouponsEntity;
import com.zuoyu.business.utils.PullUtil;
import com.zuoyu.business.utils.SharedUtil;
import com.zuoyu.business.utils.http.HttpResult;
import com.zuoyu.business.utils.http.HttpUtil;
import com.zuoyu.business.widget.pullable.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Function：优惠券通用类
 *
 * Created by JoannChen on 2017/4/17 16:49
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public abstract class CouponsFragment extends Fragment {

    /*private TextView usedText, totalText;*/

    private HttpUtil httpUtil;
    private PullUtil pullUtil;
    private CouponsAdapter adapter;

    private List<CouponsEntity.Coupons.DiscountInfo> list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getContext(), R.layout.fragment_coupons, null);

        httpUtil = new HttpUtil(getContext());

        /*
        usedText = (TextView) view.findViewById(R.id.tv_alreadyUsed);
        totalText = (TextView) view.findViewById(R.id.tv_total);
        */

        /*
         * 下拉刷新／上拉加载
         */
        PullToRefreshLayout refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refreshLayout);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setDividerHeight(33);

        pullUtil = new PullUtil(refreshLayout);
        adapter = new CouponsAdapter(getContext(), list, setCouponsType());
        listView.setAdapter(adapter);

        pullUtil.setOnLoadListener(new PullUtil.onLoadListener() {
            @Override
            public void onLoad(boolean flag, Map<String, String> params) {
                parseDiscount(params);
            }
        });

        return view;

    }


    /**
     * 优惠类型(1：全免优惠，4：时长优惠，5：金额优惠，6：折扣优惠，7：余额优惠)
     *
     * @return int
     */
    public abstract int setCouponsType();


    /**
     * 【解析】获取优惠记录
     */
    public void parseDiscount(Map<String, String> params) {

        params.put("merchant_id", SharedUtil.getString(SharedUtil.USER_ID));//商户id
        params.put("type", String.valueOf(setCouponsType()));// 优惠类型(1：全免优惠，4：时长优惠，5：金额优惠，6：折扣优惠)

        httpUtil.post(params, UrlManage.COUPONS_URL, new HttpResult<CouponsEntity>() {
            @Override
            public void onSuccess(CouponsEntity result) {

                /*
                usedText.setText(result.getData().getDayuse());
                totalText.setText(("/" + result.getData().getDaycount()));
                */

                pullUtil.setResult(adapter, list, result.getData().getList());
                pullUtil.setEmptyView(list, R.mipmap.icon_no_coupons, getString(R.string.sorry_no_send_coupons));

            }

            @Override
            public void onFailed(int errCord, String errMsg) {

            }
        });

    }

}
