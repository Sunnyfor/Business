package com.zuoyu.business.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zuoyu.business.R;
import com.zuoyu.business.adapter.ChoiceCouponsAdapter1;
import com.zuoyu.business.application.Constant;
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
 * Function：选择优惠券
 *
 * Created by JoannChen on 2017/4/24 11:05
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class ChoiceCouponsFragment extends Fragment {

    private PullUtil pullUtil;
    private ChoiceCouponsAdapter1 adapter;
    private List<ChoiceCouponsEntity.ChoiceCoupons> list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_pull, container, false);

        // 下拉刷新／上拉加载
        PullToRefreshLayout refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refreshLayout);
        final PullListView listView = (PullListView) view.findViewById(R.id.listView);
        listView.setDivider(ContextCompat.getDrawable(getContext(), R.color.white));
        listView.setDividerHeight(24);

        pullUtil = new PullUtil(refreshLayout);
        pullUtil.setPerPageCount(15);
        adapter = new ChoiceCouponsAdapter1(getContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent it = getActivity().getIntent();
                it.putExtra(Constant.COUPONS_INFO, list.get(position));
                getActivity().setResult(20, it);
                getActivity().finish();
            }
        });


        pullUtil.setOnLoadListener(new PullUtil.onLoadListener() {
            @Override
            public void onLoad(boolean flag, Map<String, String> params) {
                parseChoiceCoupons(params);
            }
        });

        return view;
    }


    /**
     * 【解析】选择优惠券
     */
    private void parseChoiceCoupons(Map<String, String> params) {


        params.put("merchant_id", SharedUtil.getString(SharedUtil.USER_ID));


        ((BaseActivity) getActivity()).httpUtil.post(params, UrlManage.GET_COUPONS_URL, new HttpResult<ChoiceCouponsEntity>() {

            @Override
            public void onSuccess(ChoiceCouponsEntity result) {

                pullUtil.setResult(adapter, list, result.getData());
                pullUtil.setEmptyView(list, 0, "暂无优惠券");
            }

            @Override
            public void onFailed(int errCord, String errMsg) {

            }
        });

    }
}
