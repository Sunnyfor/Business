package com.zuoyu.business.adapter;

import android.content.Context;
import android.widget.TextView;

import com.zuoyu.business.R;
import com.zuoyu.business.base.BaseAdapter;
import com.zuoyu.business.base.ViewHolder;
import com.zuoyu.business.entity.SearchEntity;

import java.util.List;

/**
 * <pre>
 * Function：已发优惠券
 *
 * Created by JoannChen on 2017/12/06 11:16
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class SendCouponsAdapter extends BaseAdapter {

    public SendCouponsAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_send_coupons;
    }

    @Override
    public void getView(ViewHolder holder) {

        final SearchEntity.CarInfo.CouponsInfo entity = (SearchEntity.CarInfo.CouponsInfo) list.get(holder.getPosition());

        ((TextView) holder.getView(R.id.text_1)).setText(entity.getCoupons_nickname());

    }

}
