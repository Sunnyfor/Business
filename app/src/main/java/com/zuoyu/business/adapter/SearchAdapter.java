package com.zuoyu.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zuoyu.business.R;
import com.zuoyu.business.activity.BigImageActivity;
import com.zuoyu.business.application.Constant;
import com.zuoyu.business.base.BaseAdapter;
import com.zuoyu.business.base.ViewHolder;
import com.zuoyu.business.entity.SearchEntity;
import com.zuoyu.business.utils.CalendarUtil;
import com.zuoyu.business.utils.ImageUtil;
import com.zuoyu.business.utils.IntentUtil;

import java.util.List;

/**
 * <pre>
 * Function：搜索车牌号适配器
 *
 * Created by JoannChen on 2017/4/20 14:19
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class SearchAdapter extends BaseAdapter {

    public SearchAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public int setLayoutId() {
        return R.layout.search_item;
    }

    @Override
    public void getView(ViewHolder holder) {

        final SearchEntity.CarInfo entity = (SearchEntity.CarInfo) list.get(holder.getPosition());

        // 图片地址
        // 车牌号
        // 入场时间

        ImageView image = holder.getView(R.id.iv_icon);
        ImageUtil.load(context, image, entity.getImageurl(),R.mipmap.bg_park_default);

        ((TextView) holder.getView(R.id.tv_plate)).setText(entity.getLicense_plate());
        ((TextView) holder.getView(R.id.tv_inTime)).setText(CalendarUtil.getDateTime(entity.getEntertime()));

        TextView seeBigImg = holder.getView(R.id.tv_seeBigImg);
        seeBigImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.start((Activity) context, BigImageActivity.class, Constant.IMG_URL, entity.getImageurl(), false);
            }
        });


    }

}
