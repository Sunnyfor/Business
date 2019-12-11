package com.zuoyu.business.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zuoyu.business.R;
import com.zuoyu.business.adapter.SendCouponsAdapter;
import com.zuoyu.business.application.Constant;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.application.UrlManage;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.base.BaseEntity;
import com.zuoyu.business.entity.ChoiceCouponsEntity;
import com.zuoyu.business.entity.SearchEntity;
import com.zuoyu.business.entity.UploadEntity;
import com.zuoyu.business.utils.CalendarUtil;
import com.zuoyu.business.utils.ImageUtil;
import com.zuoyu.business.utils.IntentUtil;
import com.zuoyu.business.utils.LogUtil;
import com.zuoyu.business.utils.SharedUtil;
import com.zuoyu.business.utils.ToastUtil;
import com.zuoyu.business.utils.ToolUtil;
import com.zuoyu.business.utils.ViewUtil;
import com.zuoyu.business.utils.http.HttpResult;
import com.zuoyu.business.utils.http.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * <pre>
 * Function：车辆信息界面
 *
 * Created by JoannChen on 2017/4/18 16:09
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class ParkActivity extends BaseActivity {

    ImageView image;
    TextView inTimeText;
    TextView plateText, ticketText, longText, priceText;
    TextView couponsText, coupons2Text, useCouponsText;
    TextView uploadText, promptText;
    private ImageView uploadImg, deleteImg;

    private Dialog dialog;


    /**
     * 判断
     * true：使用优惠券
     * false：使用余额
     */
    private boolean flag = true;

    private SearchEntity.CarInfo entity;

    private ChoiceCouponsEntity.ChoiceCoupons couponsInfo;//优惠券
    private String parkHour;//优惠时长(小时为单位)，7.5小时，网络请求时使用


    /**
     * 上传凭证
     */
    private String mFilePath;
    private String mNewFilePath;
    private File mFileUpload;

    private String imageUrl;//服务器返回的图片地址

    @Override
    public int setLayout() {
        return R.layout.park_main;
    }

    @Override
    public void initTitle() {
        titleManage.setTitleText(getString(R.string.park_info));
    }

    @Override
    public void initView() {


        ScrollView scrollView = id(R.id.scrollView);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);

        entity = (SearchEntity.CarInfo) getIntent().getSerializableExtra(Constant.PARK_INFO);


        image = findViewById(R.id.iv_icon);
        image.setOnClickListener(this);
        ImageUtil.load(context, image, entity.getImageurl(), R.mipmap.bg_park_default);

        inTimeText = findViewById(R.id.tv_inTime);
        inTimeText.setText(CalendarUtil.getDateTime(entity.getEntertime()));

        plateText = findViewById(R.id.tv_plate);
        plateText.setText(setColor(getString(R.string.plate_number_colon) + entity.getLicense_plate()));

        ticketText = findViewById(R.id.tv_ticket);
        ticketText.setText(setColor(getString(R.string.park_ticket_colon) + entity.getTicketno()));

        longText = findViewById(R.id.tv_long);
        longText.setText(setColor(getString(R.string.park_long_colon) + entity.getParkingtime()));

        priceText = findViewById(R.id.tv_price);
        priceText.setText(setColor(getString(R.string.park_fee_colon) + "￥" + entity.getTotalprice()));


        useCouponsText = findViewById(R.id.tv_use_coupons);
        useCouponsText.setOnClickListener(this);
        useCouponsText.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_font));
        useCouponsText.setEnabled(false);

//        LogUtil.e("集合：" + entity.getCouponlist().size());
//        LogUtil.e("优惠：" + (entity.getPermissions() == 0 ? "0-单项" : "1-多项"));
//        LogUtil.e("上限：" + (entity.getDiscount_used() == 1 ? "1-已使用" : "2-未使用"));

        // 优惠券使用达上限时提示语
        String couponsUsedPrompt = null;

        /*
         *  判断是单项优惠还是多项优惠
         *  0：单项 1：多项
         */
        LinearLayout sendLLayout = id(R.id.ll_send_coupons);

        if (entity.getPermissions() == 0) {
            couponsUsedPrompt = getString(R.string.already_use_coupons);
            sendLLayout.setVisibility(View.GONE);
        } else if (entity.getPermissions() == 1) {

            couponsUsedPrompt = getString(R.string.limited_coupons);

            if (entity.getCouponlist().size() > 0) {

                sendLLayout.setVisibility(View.VISIBLE);

                ListView sendLstv = id(R.id.listView);
                SendCouponsAdapter adapter = new SendCouponsAdapter(context, entity.getCouponlist());
                sendLstv.setAdapter(adapter);
            }
        }


        /*
         * 上传凭证
         * 是否使用摄像头拍照功能1.不使用 2.使用
         */
        RelativeLayout uploadRLayout = id(R.id.rl_upload);
        if (SharedUtil.getInteger(SharedUtil.USE_CAMERA) == 2) {

            uploadRLayout.setVisibility(View.VISIBLE);
            LogUtil.e("可以使用相机权限");

            findViewById(R.id.line_2).setVisibility(View.VISIBLE);
            findViewById(R.id.line_3).setVisibility(View.VISIBLE);

            uploadText = id(R.id.tv_upload);
            promptText = id(R.id.tv_prompt);


            //上传后的缩略图//删除按钮
            uploadImg = id(R.id.iv_upload);
            ImageUtil.load(context, uploadImg, entity.getImage());

            deleteImg = id(R.id.iv_delete);
            deleteImg.setOnClickListener(this);

        } else {
            uploadRLayout.setVisibility(View.GONE);
            findViewById(R.id.line_2).setVisibility(View.GONE);
            findViewById(R.id.line_3).setVisibility(View.GONE);
        }

        /*
         * 是否使用优惠(1:已使用2:未使用)
         */
        if (entity.getDiscount_used() == 1) {
            coupons2Text = findViewById(R.id.tv_coupons2);
            coupons2Text.setVisibility(View.VISIBLE);
            coupons2Text.setText(couponsUsedPrompt);
        } else {
            couponsText = findViewById(R.id.tv_coupons);
            LinearLayout couponsLLayout = findViewById(R.id.ll_coupons);
            couponsLLayout.setVisibility(View.VISIBLE);
            couponsLLayout.setOnClickListener(this);

            // 优惠券未使用可以拍照,(多项优惠只可以拍一次照)
            if (entity.getCouponlist().size() == 0) {
                uploadRLayout.setOnClickListener(this);
            }

        }
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.iv_icon:
                IntentUtil.start((Activity) context, BigImageActivity.class, Constant.IMG_URL, entity.getImageurl(), false);
                break;
            case R.id.ll_coupons:
                Intent it = new Intent(activity, ChoiceCouponsActivity.class);
                startActivityForResult(it, 20);
                ViewUtil.setMarginLeft(couponsText, 10, ViewUtil.LINEARLAYOUT);
                break;
            case R.id.rl_upload:
                showDialog();
                break;
            case R.id.tv_photo:
                AndPermission.with(this)
                        .requestCode(Constant.PERMISSION_REQUEST_CAMERA)
                        .permission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .rationale(new RationaleListener() {
                            @Override
                            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
//                                if (requestCode == Constant.PERMISSION_REQUEST_CAMERA) {
//                                    rationale.resume();
//                                }
                                AndPermission.rationaleDialog(context, rationale).show();
                            }
                        })
                        .callback(new PermissionListener() {
                            @Override
                            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                                Uri imageUri;
                                //获取SD卡路径
                                String SDCardRoot = Environment.getExternalStorageDirectory().toString() + "/51Park/image/";
                                String fileName = "51Park" + DateFormat.format("yyyyMMddhhmmss", Calendar.getInstance(Locale.CHINA));
                                mFilePath = SDCardRoot + fileName + ".jpg";
                                mNewFilePath = SDCardRoot + fileName + "_.jpg";
                                mFileUpload = new File(mFilePath);
                                LogUtil.e("【mFilePath】" + mFilePath);


                                File file = new File(SDCardRoot);
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                try {
                                    if (mFileUpload.createNewFile()) {
                                        Intent intent = new Intent();

                                        //判断是否为7.0以上设备
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                            imageUri = FileProvider.getUriForFile(context, "com.zuoyu.business.fileprovider", mFileUpload);
                                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                        } else {
                                            imageUri = Uri.fromFile(mFileUpload);//加载路径
                                        }

                                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                        startActivityForResult(intent, 40);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                                ToastUtil.show("授权失败");
                            }
                        })
                        .start();
                dialog.dismiss();
                break;
            case R.id.tv_cancel:
                dialog.dismiss();
                break;
            case R.id.iv_delete:
                uploadImg.setImageResource(0);
                deleteImg.setImageResource(0);
                break;
            case R.id.tv_use_coupons:
                parseUseCoupons(flag);
                break;
        }

    }


    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.orange), 0);

    }


    /**
     * 点击上传凭证后弹出对话框
     */
    private void showDialog() {
        dialog = new Dialog(context, R.style.Dialog_Theme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.layout_dialog_upload);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);

        dialog.show();


        dialog.findViewById(R.id.tv_photo).setOnClickListener(this);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {


        LogUtil.e("【requestCode】:" + requestCode + "\n【resultCode】:" + resultCode);

        switch (resultCode) {
            case 20:// 选择停车券
                couponsInfo = (ChoiceCouponsEntity.ChoiceCoupons) intent.getSerializableExtra(Constant.COUPONS_INFO);
                LogUtil.e("couponsInfo:" + couponsInfo);
                couponsText.setText(couponsInfo.getCoupons_nickname());
                useCouponsText.setBackgroundColor(ContextCompat.getColor(context, R.color.red_font));
                useCouponsText.setEnabled(true);
                flag = true;
                break;
            case 30:// 选择停车时长
                String parkLong = intent.getStringExtra(Constant.COUPONS_INFO);
                parkHour = intent.getStringExtra(Constant.COUPONS_HOUR);
                LogUtil.e("parkLong:" + parkLong);
                LogUtil.e("parkHour:" + parkHour);
                couponsText.setText(parkLong);
                useCouponsText.setBackgroundColor(ContextCompat.getColor(context, R.color.red_font));
                useCouponsText.setEnabled(true);
                flag = false;
                break;
            case Activity.RESULT_CANCELED:
                LogUtil.e("取消上传");
                break;
        }

        switch (requestCode) {
            case 40:// // 上传凭证
                if (resultCode == -1) {
                    LogUtil.e("拍照成功~");
                    parseUploadVoucher();
                    ImageUtil.load(context, uploadImg, mFilePath);
                    deleteImg.setImageResource(R.mipmap.icon_delete_orange);
                } else if (resultCode == 0) {
                    LogUtil.e("取消上传~");

                }

                break;
        }


    }

    /**
     * 动态改变TextView字体颜色
     *
     * @param str 字符串
     * @return SpannableStringBuilder
     */

    private SpannableStringBuilder setColor(String str) {
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(
                new ForegroundColorSpan(Color.parseColor("#999999")),
                5, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }


    /**
     * 【解析】使用优惠
     *
     * @param flag true 使用优惠券
     *             false 使用账户余额
     */
    private void parseUseCoupons(boolean flag) {

        Map<String, String> params = new TreeMap<>();
        params.put("merchant_id", SharedUtil.getString(SharedUtil.USER_ID));//  商户id
        params.put("parkid", SharedUtil.getString(SharedUtil.PARK_ID));//  停车场id

        String url;
        if (flag) {
            params.put("coupon_id", couponsInfo.getCoupon_id());//  优惠类型id
            url = UrlManage.USE_COUPONS_URL;
        } else {
            params.put("disc_time", parkHour);//  余额抵扣时长（单位：小时）
            url = UrlManage.USETIME_URL;
        }

        params.put("ordernum", entity.getOrdernum());//  订单号


        //  图片的路径为空不上传
        if (!ToolUtil.isEmpty(imageUrl)) {
            params.put("imageurl", imageUrl);
        }

        // 某些停车场不需要小票id
        if (!ToolUtil.isEmpty(entity.getTicketno())) {
            params.put("ticket_id", entity.getTicketno());//  停车小票
        }


        httpUtil.post(params, url, new HttpResult<BaseEntity>() {

            @Override
            public void onSuccess(BaseEntity result) {
                IntentUtil.start(activity, ResultActivity.class, Constant.SCAN_RESULT, 200, true);
            }

            @Override
            public void onFailed(int errCord, String errMsg) {

            }
        });

    }


    /**
     * 【解析】上传凭证
     */
    private void parseUploadVoucher() {


        Map<String, String> params = new TreeMap<>();
        params.put("merchant_id", SharedUtil.getString(SharedUtil.USER_ID));//  商户id
        params.put("image", ImageUtil.getImageStr(ImageUtil.saveBitmapToFile(mFileUpload, mNewFilePath)));//  Base64图片流


        httpUtil = new HttpUtil(context, "正在上传…");
        httpUtil.post(params, UrlManage.UPLOAD_IMAGE_URL, new HttpResult<UploadEntity>() {

            @Override
            public void onSuccess(UploadEntity result) {
                // 点击使用优惠券传给服务器，已便确认最终是哪一张显示（上传给服务器的图片可能有多张）
                imageUrl = result.getData().getImageurl();
                ToastUtil.show("上传成功");
                LogUtil.e("【本地返回图片路径】" + mFilePath);
                LogUtil.e("【服务器返回图片路径】" + imageUrl);
            }

            @Override
            public void onFailed(int errCord, String errMsg) {

            }
        });

    }


}
