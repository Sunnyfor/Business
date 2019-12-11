package com.zuoyu.business.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.zuoyu.business.R;
import com.zuoyu.business.base.BaseAdapter;
import com.zuoyu.business.base.ViewHolder;
import com.zuoyu.business.entity.ChoiceCouponsEntity;
import com.zuoyu.business.utils.LogUtil;
import com.zuoyu.business.utils.ViewUtil;

import java.util.List;

/**
 * <pre>
 * Function：我的优惠券适配器
 *
 * Created by JoannChen on 2017/6/12 11:16
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class MyCouponsAdapter extends BaseAdapter {

    public MyCouponsAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public int setLayoutId() {
        return R.layout.my_coupons_item;
    }

    @Override
    public void getView(ViewHolder holder) {

        final ChoiceCouponsEntity.ChoiceCoupons entity = (ChoiceCouponsEntity.ChoiceCoupons) list.get(holder.getPosition());

        // 今日优惠券总数
        // 剩余优惠券
        // 有效期
        // 优惠券(面额)

        ((TextView) holder.getView(R.id.tv_nickname)).setText(entity.getCoupons_nickname());
        ((TextView) holder.getView(R.id.tv_total)).setText((entity.getDaycount() + " 张"));
        ((TextView) holder.getView(R.id.tv_surplus)).setText((entity.getCanuse() + " 张"));
        ((TextView) holder.getView(R.id.tv_valid)).setText((context.getString(R.string.valid_colon) + entity.getEnddate()));

        TextView couponsText = holder.getView(R.id.tv_coupons);

        String coupons = entity.getCoupons_name();


        SpannableStringBuilder style = null;


        if (coupons.contains("优惠券")) {

            coupons = coupons.split("优惠券")[0];

            /*
             * 优惠类型(1：全免优惠，4：时长优惠，5：金额优惠，6：折扣优惠，7：余额，9：封顶)
             */
            if (entity.getType() == 9) {
                coupons = coupons.split("封顶")[0] + "（封顶）";
            }


            LogUtil.e("【优惠券】" + coupons);
            LogUtil.e("【优惠券】" + coupons.length());


            style = new SpannableStringBuilder(coupons);

            if (coupons.contains("元")) {
                style.setSpan(new AbsoluteSizeSpan(ViewUtil.getHeight(44)),
                        coupons.length() - 1, coupons.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

            if (coupons.contains("折")) {
                style.setSpan(new AbsoluteSizeSpan(ViewUtil.getHeight(44)),
                        coupons.length() - 1, coupons.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

            if (coupons.contains("小时")) {
                style.setSpan(new AbsoluteSizeSpan(ViewUtil.getHeight(44)),
                        coupons.length() - 2, coupons.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (coupons.contains("全免")) {
                style.setSpan(new AbsoluteSizeSpan(ViewUtil.getHeight(60)),
                        0, coupons.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

            if (coupons.contains("封顶")) {
                style.setSpan(new AbsoluteSizeSpan(ViewUtil.getHeight(44)),
                        coupons.length() - 5, coupons.length() - 4,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                style.setSpan(new AbsoluteSizeSpan(ViewUtil.getHeight(24)),
                        coupons.length() - 4, coupons.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                // 上面设置有问题
                style.setSpan(new AbsoluteSizeSpan(ViewUtil.getHeight(24)),
                        coupons.length() - 1, coupons.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.gray_font)),
                        coupons.length() - 4, coupons.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

        }

        couponsText.setText(style == null ? coupons : style);


        if (holder.getPosition() == 0) {
            ViewUtil.setMarginTop(holder.getView(R.id.rl_item), 30, ViewUtil.RELATIVELAYOUT);
        }


    }

}
