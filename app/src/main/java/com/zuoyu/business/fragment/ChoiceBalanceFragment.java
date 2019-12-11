package com.zuoyu.business.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zuoyu.business.R;
import com.zuoyu.business.application.Constant;
import com.zuoyu.business.application.UrlManage;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.entity.LongEntity;
import com.zuoyu.business.utils.SharedUtil;
import com.zuoyu.business.utils.ToastUtil;
import com.zuoyu.business.utils.ToolUtil;
import com.zuoyu.business.utils.http.HttpResult;
import com.zuoyu.business.widget.EasyPickerView;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


/**
 * <pre>
 * Function：选择余额优惠
 *
 * Created by JoannChen on 2017/7/417:24
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class ChoiceBalanceFragment extends Fragment {

    // 选择的时长
    private int choice_hour;
    private int choice_minute = 30;//默认显示30分钟

    // 解析后的小时数
    private float parse_totalHour;

    private TextView longText;
    private TextView hourText, minuteText;
    private LinearLayout longLLayout;

    private EasyPickerView epvHour, epvMinute;
    private TextView promptText;

    private Button sureBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_balance, container, false);

        // 未充值
        longText = (TextView) view.findViewById(R.id.tv_long);

        // 账户余额:小时／分钟
        hourText = (TextView) view.findViewById(R.id.tv_hour);
        minuteText = (TextView) view.findViewById(R.id.tv_minute);
        longLLayout = (LinearLayout) view.findViewById(R.id.ll_long);

        // 小时／分钟选择器
        epvHour = (EasyPickerView) view.findViewById(R.id.epv_hour);
        epvMinute = (EasyPickerView) view.findViewById(R.id.epv_minute);


        // 提示语
        promptText = (TextView) view.findViewById(R.id.tv_prompt);

        // 确定按钮
        sureBtn = (Button) view.findViewById(R.id.btn_sure);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (choice_hour == 0 && choice_minute == 0) {
                    ToastUtil.show("请选择优惠时长");
                    return;
                }

                String longStr = "";

                if (choice_hour != 0) {
                    longStr = choice_hour + getString(R.string.hour);
                }

                if (choice_minute != 0) {
                    longStr = longStr + choice_minute + getString(R.string.minute);
                }


                float choice_totalHour = choice_hour + ((float) choice_minute / 60);


                if (parse_totalHour < choice_totalHour) {
                    ToastUtil.show("您的账户时长不足");
                    return;
                }


                Intent it = getActivity().getIntent();
                it.putExtra(Constant.COUPONS_INFO, longStr);
                it.putExtra(Constant.COUPONS_HOUR, String.valueOf(choice_totalHour));
                getActivity().setResult(30, it);
                getActivity().finish();
            }
        });


        initHours();
        initMinutes();

        parseGetLong();




        return view;
    }

    /**
     * 初始化小时选择器
     */
    private void initHours() {
        final ArrayList<String> hDataList = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            hDataList.add("" + i);

        epvHour.setDataList(hDataList);
        epvHour.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {
                choice_hour = Integer.parseInt(hDataList.get(curIndex));
            }

            @Override
            public void onScrollFinished(int curIndex) {
                choice_hour = Integer.parseInt(hDataList.get(curIndex));
            }
        });
    }

    /**
     * 初始化分钟选择器
     */
    private void initMinutes() {
        final ArrayList<String> dataList2 = new ArrayList<>();
        dataList2.add("30");
        dataList2.add("45");
        dataList2.add("0");
        dataList2.add("15");


        epvMinute.setDataList(dataList2);
        epvMinute.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {
                choice_minute = Integer.parseInt(dataList2.get(curIndex));
            }

            @Override
            public void onScrollFinished(int curIndex) {
                choice_minute = Integer.parseInt(dataList2.get(curIndex));
            }
        });
    }

    /**
     * 【解析】获取账户余额时长
     */
    private void parseGetLong() {
        final Map<String, String> params = new TreeMap<>();
        params.put("merchant_id", SharedUtil.getString(SharedUtil.USER_ID));

        ((BaseActivity) getActivity()).httpUtil.post(params, UrlManage.REMAINING_URL, new HttpResult<LongEntity>() {

            @Override
            public void onSuccess(LongEntity result) {

                if (ToolUtil.isEmpty(result.getData().getRemaining())) {
                    longText.setVisibility(View.VISIBLE);
                    longLLayout.setVisibility(View.GONE);
                    promptText.setText(getString(R.string.discount_long_empty));
                    ToolUtil.setBtnClick(sureBtn, false);
                } else {

                    float parse_hour = Float.parseFloat(result.getData().getHour());
                    float parse_minute = Float.parseFloat(result.getData().getMinute());

                    parse_totalHour = parse_hour + (parse_minute / 60);

                    longText.setVisibility(View.GONE);
                    longLLayout.setVisibility(View.VISIBLE);
                    hourText.setText(result.getData().getHour());
                    minuteText.setText(result.getData().getMinute());
                    promptText.setText(getString(R.string.discount_long));
                    ToolUtil.setBtnClick(sureBtn, true);
                }


            }

            @Override
            public void onFailed(int errCord, String errMsg) {

            }
        });
    }


}
