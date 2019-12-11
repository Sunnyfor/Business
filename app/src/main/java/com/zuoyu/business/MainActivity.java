package com.zuoyu.business;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zuoyu.business.widget.EasyPickerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int hour;
    private int minute;
    private EasyPickerView epvHour,epvMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_balance);

        initHours();
        initMinutes();

    }

    private void initHours() {
        epvHour =  findViewById(R.id.epv_hour);
        final ArrayList<String> hDataList = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            hDataList.add("" + i);

        epvHour.setDataList(hDataList);
        epvHour.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {
                hour = Integer.parseInt(hDataList.get(curIndex));
            }

            @Override
            public void onScrollFinished(int curIndex) {
                hour = Integer.parseInt(hDataList.get(curIndex));
            }
        });
    }

    private void initMinutes() {
        epvMinute = (EasyPickerView) findViewById(R.id.epv_minute);
        final ArrayList<String> dataList2 = new ArrayList<>();
        dataList2.add("0");
        dataList2.add("15");
        dataList2.add("30");
        dataList2.add("45");

        epvMinute.setDataList(dataList2);
        epvMinute.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {
                minute = Integer.parseInt(dataList2.get(curIndex));
            }

            @Override
            public void onScrollFinished(int curIndex) {
                minute = Integer.parseInt(dataList2.get(curIndex));
            }
        });
    }
}
