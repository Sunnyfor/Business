package com.zuoyu.business.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.zuoyu.business.R;
import com.zuoyu.business.base.BaseAdapter;
import com.zuoyu.business.base.ViewHolder;
import com.zuoyu.business.entity.CouponsEntity;
import com.zuoyu.business.utils.LogUtil;
import com.zuoyu.business.utils.ToolUtil;
import com.zuoyu.business.utils.ViewUtil;

import java.util.List;

/**
 * <pre>
 * Function：优惠券适配器
 *
 * Created by JoannChen on 2017/4/20 14:19
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class CouponsAdapter extends BaseAdapter {

    private int type;

    public CouponsAdapter(Context context, List<?> list, int type) {
        super(context, list);
        this.type = type;
    }

    @Override
    public int setLayoutId() {
        return R.layout.coupons_item;
    }

    @Override
    public void getView(ViewHolder holder) {

        final CouponsEntity.Coupons.DiscountInfo entity = (CouponsEntity.Coupons.DiscountInfo) list.get(holder.getPosition());

        // 优惠券名称
        // 车牌号
        // 小票
        // 发放时间
        // 使用时间
        // 优惠券(面额)


        ((TextView) holder.getView(R.id.tv_plate)).setText(entity.getLicense_plate());
        ((TextView) holder.getView(R.id.tv_ticket)).setText((context.getString(R.string.ticket_colon) + entity.getTicketno()));
        ((TextView) holder.getView(R.id.tv_sendTime)).setText((context.getString(R.string.hand_out_colon) + (ToolUtil.isEmpty(entity.getStart_time()) ? "1970-01-01 00:00:00" : entity.getStart_time())));
        ((TextView) holder.getView(R.id.tv_useTime)).setText((context.getString(R.string.use_colon) + entity.getUsed_time()));

        TextView nicknameText = holder.getView(R.id.tv_nickname);
        TextView couponsText = holder.getView(R.id.tv_coupons);



        /*
         * 优惠类型(1：全免优惠，4：时长优惠，5：金额优惠，6：折扣优惠，7：余额，9：封顶)
         * 显示优惠时长
         */
        if (type == 7) {
            nicknameText.setVisibility(View.INVISIBLE);
            couponsText.setText(entity.getDiscount_details());
        } else {
            nicknameText.setVisibility(View.VISIBLE);
            nicknameText.setText(entity.getCoupons_nickname());

            String coupons = entity.getDiscount_details();
            SpannableStringBuilder style = null;

            LogUtil.e("【优惠券】" + coupons);
            if (coupons.contains("优惠券")) {

                coupons = coupons.split("优惠券")[0];

                if (type == 9) {
                    coupons = coupons.split("封顶")[0] + "（封顶）";
                }

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

                    style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.gray_font)),
                            coupons.length() - 4, coupons.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }

            }

            LogUtil.e("【优惠券剪切后】" + style);
            couponsText.setText(style);
        }


    }

}
