package com.zuoyu.business.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import com.zuoyu.business.R;
import com.zuoyu.business.base.BaseAdapter;
import com.zuoyu.business.base.ViewHolder;
import com.zuoyu.business.entity.ChoiceCouponsEntity;
import com.zuoyu.business.utils.ViewUtil;

import java.util.List;

/**
 * <pre>
 * Function：优惠券适配器
 *
 * Created by JoannChen on 2017/4/24 14:19
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class ChoiceCouponsAdapter1 extends BaseAdapter {

    public ChoiceCouponsAdapter1(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public int setLayoutId() {
        return R.layout.choice_coupons_item;
    }

    @Override
    public void getView(ViewHolder holder) {

        final ChoiceCouponsEntity.ChoiceCoupons entity = (ChoiceCouponsEntity.ChoiceCoupons) list.get(holder.getPosition());


        // 发放张数
        // 优惠券(面额)

        ((TextView) holder.getView(R.id.tv_nickname)).setText(entity.getCoupons_nickname());
        ((TextView) holder.getView(R.id.tv_count)).setText(("（今日可用 " + entity.getCanuse() + " 张）"));

        TextView couponsText = holder.getView(R.id.tv_coupons);
//        TextView typeText = holder.getView(R.id.tv_couponsType);

        String coupons = entity.getCoupons_name();
        SpannableStringBuilder style = null;

        if (coupons.contains("优惠券")) {

            coupons = coupons.split("优惠券")[0];

            /*
             * 优惠类型(1：全免优惠，4：时长优惠，5：金额优惠，6：折扣优惠，7：余额，9：封顶)
             */
            if (entity.getType() == 9) {
                coupons = coupons.split("封顶")[0] + "(封顶)";
            }

            style = new SpannableStringBuilder(coupons);

            if (coupons.contains("元")) {
                style.setSpan(new AbsoluteSizeSpan(ViewUtil.getHeight(30)),
                        coupons.length() - 1, coupons.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

            if (coupons.contains("折")) {
                style.setSpan(new AbsoluteSizeSpan(ViewUtil.getHeight(30)),
                        coupons.length() - 1, coupons.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

            if (coupons.contains("小时")) {
                style.setSpan(new AbsoluteSizeSpan(ViewUtil.getHeight(30)),
                        coupons.length() - 2, coupons.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

            if (coupons.contains("全免")) {
                style.setSpan(new AbsoluteSizeSpan(ViewUtil.getHeight(60)),
                        0, coupons.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

            if (coupons.contains("封顶")) {
                style.setSpan(new AbsoluteSizeSpan(ViewUtil.getHeight(30)),
                        coupons.length() - 5, coupons.length() - 4,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                style.setSpan(new AbsoluteSizeSpan(ViewUtil.getHeight(24)),
                        coupons.length() - 4, coupons.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

        }

        couponsText.setText(style);


    }

}
