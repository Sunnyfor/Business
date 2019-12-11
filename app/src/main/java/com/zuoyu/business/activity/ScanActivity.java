package com.zuoyu.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.google.zxing.Result;
import com.jaeger.library.StatusBarUtil;
import com.zuoyu.business.R;
import com.zuoyu.business.application.Constant;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.application.UrlManage;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.entity.SearchEntity;
import com.zuoyu.business.utils.IntentUtil;
import com.zuoyu.business.utils.LogUtil;
import com.zuoyu.business.utils.SharedUtil;
import com.zuoyu.business.utils.ToastUtil;
import com.zuoyu.business.utils.http.HttpResult;
import com.zuoyu.business.zxing.ScanListener;
import com.zuoyu.business.zxing.ScanManager;
import com.zuoyu.business.zxing.decode.DecodeThread;

import java.util.Map;
import java.util.TreeMap;

/**
 * <pre>
 * Function：条码扫描器
 *
 * Created by JoannChen on 2017/4/17 11:34
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class ScanActivity extends BaseActivity implements ScanListener {

    private SurfaceView scanPreview = null;// 提供一个可见区域

    private ScanManager scanManager;
    private String TicketId;


    @Override
    public int setLayout() {
        return R.layout.scan_main;
    }

    @Override
    public void initTitle() {
        titleManage.setTitleText("扫小票");
    }

    @Override
    public void initView() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        ButterKnife.bind(this);

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);

        View scanContainer = findViewById(R.id.capture_container);
        View scanCropView = findViewById(R.id.capture_crop_layout);
        ImageView scanLine = (ImageView) findViewById(R.id.capture_scan_line);

        CheckBox lightImg = (CheckBox) findViewById(R.id.iv_light);
        lightImg.setOnClickListener(this);


        //构造出扫描管理器
        scanManager = new ScanManager(this, scanPreview, scanContainer, scanCropView, scanLine, DecodeThread.ALL_MODE, this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.iv_light:
                scanManager.switchLight();
                break;
            default:
                break;
        }
    }

    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.orange), 0);
    }

    @Override
    public void scanResult(Result rawResult, Bundle bundle) {

        //扫描成功后，扫描器不会再连续扫描，如需连续扫描，调用reScan()方法。
//        scanManager.reScan();

        TicketId = rawResult.getText();

        if (TicketId.contains("ticketid=")) {
            TicketId = TicketId.split("ticketid=")[1].split("&")[0];
//            TicketId = TicketId.split("ticketid=")[1].substring(0, 13);
        }

//       else {
//            if (TicketId.length() != 13) {
//                // 非无忧停车小票
//                IntentUtil.start(activity, ResultActivity.class, Constant.SCAN_RESULT, 1, false);
//                return;
//            }
//        }
        LogUtil.e("结果：" + rawResult.getText());
        LogUtil.e("截取后结果：" + TicketId);

        parseScanTicket();

    }

    @Override
    public void scanError(Exception e) {
        ToastUtil.show(e.getMessage());
        //相机扫描出错时
        if (e.getMessage() != null && e.getMessage().startsWith("相机")) {
            scanPreview.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scanManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        scanManager.onPause();
    }


    /**
     * 【解析】查找车牌号
     */
    public void parseScanTicket() {

        Map<String, String> params = new TreeMap<>();
        params.put("parkid", SharedUtil.getString(SharedUtil.PARK_ID));// 停车场id
        params.put("merchant_id", SharedUtil.getString(SharedUtil.USER_ID));//商户id
        params.put("ticketno", TicketId.trim());// 小票号
        params.put("page", "1");
        params.put("per_page", "1");


        httpUtil.post(params, UrlManage.SEARCH_CAR_URL, new HttpResult<SearchEntity>() {
            @Override
            public void onSuccess(SearchEntity result) {

                if (result.getData().size() == 0) {
                    // 未找到符合条件的小票
                    IntentUtil.start(activity, ResultActivity.class, Constant.SCAN_RESULT, 3, false);
                } else {
                    //1:在场车辆   4：异常车辆
                    if (result.getData().get(0).getService_status() == 1 || result.getData().get(0).getService_status() == 4) {
                        // 车辆在场
                        Intent intent = new Intent(activity, ParkActivity.class);
                        intent.putExtra(Constant.PARK_INFO, result.getData().get(0));
                        startActivity(intent);
                        finish();
                    } else {
                        // 车辆不在场
                        IntentUtil.start(activity, ResultActivity.class, Constant.SCAN_RESULT, 2, false);
                    }
                }
            }

            @Override
            public void onFailed(int errCord, String errMsg) {

            }
        });

    }

}
