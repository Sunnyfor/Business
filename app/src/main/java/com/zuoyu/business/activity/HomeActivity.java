package com.zuoyu.business.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.zuoyu.business.R;
import com.zuoyu.business.application.Constant;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.application.UrlManage;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.entity.LoginEntity;
import com.zuoyu.business.entity.LongEntity;
import com.zuoyu.business.entity.UpdateEntity;
import com.zuoyu.business.utils.DialogUtil;
import com.zuoyu.business.utils.IntentUtil;
import com.zuoyu.business.utils.LogUtil;
import com.zuoyu.business.utils.PermissionUtil;
import com.zuoyu.business.utils.SharedUtil;
import com.zuoyu.business.utils.ToastUtil;
import com.zuoyu.business.utils.ToolUtil;
import com.zuoyu.business.utils.ViewUtil;
import com.zuoyu.business.utils.http.HttpResult;

import java.util.Map;
import java.util.TreeMap;

/**
 * <pre>
 * Function：首页
 *
 * Created by JoannChen on 2017/4/17 16:33
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class HomeActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private long mLastClickTime = 0;

    LoginEntity.UserInfo mUserInfo;

    DrawerLayout mDrawerLayout;
    RelativeLayout mDrawerContent;

    private TextView longText;
    TextView discountText, modifyText, exitText;

    TextView baselineText;

    ActionBarDrawerToggle mDrawerToggle;

    @Override
    public int setLayout() {
        return R.layout.home_main;
    }

    @Override
    public void initTitle() {
        titleManage.hideTitle();
    }

    @Override
    public void initView() {

        try {
            mUserInfo = (LoginEntity.UserInfo) SharedUtil.getObject(SharedUtil.USER_INFO);
        } catch (Exception e) {
            MyApplication.exitApplication(activity);
        }

        LogUtil.e("【首页】mUserInfo:" + SharedUtil.getObject(SharedUtil.USER_INFO));


        // DrawerLayout布局／DrawerLayout容器
        mDrawerLayout = id(R.id.drawer_layout);
        mDrawerContent = id(R.id.drawer_content);
        ViewUtil.setViewSize(mDrawerContent, 0, 550);

        //关闭手势滑动
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        // 个人中心
        ImageView mineImg = id(R.id.tv_mine);
        mineImg.setOnClickListener(this);


        /*
         * 布局
         */
        RelativeLayout searchRLayout = id(R.id.rl_search);
        RelativeLayout scanRLayout =  id(R.id.rl_scan);
        searchRLayout.setOnClickListener(this);
        scanRLayout.setOnClickListener(this);
        ViewUtil.setViewSize(searchRLayout, 196, 540);
        ViewUtil.setViewSize(scanRLayout, 196, 540);

        /*
         * 图标
         */
        ImageView icon1 =  id(R.id.icon_1);
        ImageView icon2 = id(R.id.icon_2);
        ViewUtil.setViewSize(icon1, 75, 75);
        ViewUtil.setViewSize(icon2, 75, 75);
        ViewUtil.setMarginLeft(icon1, 68, ViewUtil.RELATIVELAYOUT);
        ViewUtil.setMarginLeft(icon2, 68, ViewUtil.RELATIVELAYOUT);

        /*
         * 中文
         */
        TextView text1 =  id(R.id.text_1);
        TextView text2 =  id(R.id.text_2);
        ViewUtil.setTextSize(text1, 54);
        ViewUtil.setTextSize(text2, 54);
        ViewUtil.setMarginLeft(text1, 80, ViewUtil.RELATIVELAYOUT);
        ViewUtil.setMarginLeft(text2, 80, ViewUtil.RELATIVELAYOUT);

        /*
         * 英文
         */
        TextView text3 = findViewById(R.id.text_3);
        TextView text4 =  findViewById(R.id.text_4);
        ViewUtil.setTextSize(text3, 26);
        ViewUtil.setTextSize(text4, 26);
        ViewUtil.setMarginLeft(text3, 60, ViewUtil.RELATIVELAYOUT);
        ViewUtil.setMarginLeft(text4, 78, ViewUtil.RELATIVELAYOUT);


        // 个人中心
        LinearLayout mineLLayout = id(R.id.ll_mine);
        mineLLayout.setOnClickListener(this);


        // 商家名称
        TextView nameText = id(R.id.tv_name);
        nameText.setText(mUserInfo.getMerchantname());
        nameText.setSingleLine();

        // 商家地址
        TextView addressText = id(R.id.tv_address);
        addressText.setText(mUserInfo.getParkname());
        addressText.setSingleLine();

        // 账户余额
        longText = id(R.id.tv_long);


        discountText = id(R.id.tv_discount);
        discountText.setOnClickListener(this);

        modifyText = id(R.id.tv_coupons_record);
        modifyText.setOnClickListener(this);

        exitText = id(R.id.tv_setting);
        exitText.setOnClickListener(this);

        baselineText = id(R.id.tv_baseline);
        baselineText.setText(getString(R.string.web_site));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, null, 0, 0);
        mDrawerLayout.addDrawerListener(mDrawerToggle);


    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_mine:
                IntentUtil.start(activity, MineActivity.class, false);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.tv_mine:
                mDrawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.rl_search:
                IntentUtil.start(activity, SearchActivity.class, false);
                break;
            case R.id.rl_scan:
//                AndPermission.with(this)
//                        .requestCode(Constant.PERMISSION_REQUEST_CAMERA)
//                        .permission(Manifest.permission.CAMERA)
//                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；这样避免用户勾选不再提示，导致以后无法申请权限。
//                        .rationale(new RationaleListener() {
//                            @Override
//                            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
////                                if (requestCode == Constant.PERMISSION_REQUEST_CAMERA) {
//                                // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
//                                    rationale.resume();
////                                }
//                            }
//                        })
//                        .callback(new PermissionListener() {
//                            @Override
//                            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
//                                IntentUtil.start(activity, ScanActivity.class, false);
//                            }
//
//                            @Override
//                            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
//                                ToastUtil.show("授权失败");
//                            }
//                        })
//                        .start();
                PermissionUtil.requestPermission(this, PermissionUtil.CODE_CAMERA, mPermissionGrant);
                break;
            case R.id.tv_discount:
                IntentUtil.start(activity, MyCouponsActivity.class, false);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.tv_coupons_record:
                IntentUtil.start(activity, CouponsActivity.class, false);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.tv_setting:
                IntentUtil.start(activity, SettingActivity.class, false);
                mDrawerLayout.closeDrawers();
                break;
            default:
                break;
        }
    }

    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.bg_home), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawers();
        }
        showUpdate();
        parseGetLong();
    }


    /**
     * 当权限请求已完成时收到的回调
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(
            final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }


    /**
     * 权限授予后返回的操作
     */
    private PermissionUtil.PermissionGrant mPermissionGrant = new PermissionUtil.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            if (requestCode == PermissionUtil.CODE_CAMERA) {
                IntentUtil.start(activity, ScanActivity.class, false);
            }
        }
    };


    /**
     * 显示更新提示
     */
    private void showUpdate() {

        //如果 updateTime 为空，则 updateTime = currentTime
        long difference = 24 * 60 * 60 * 1000;
        long currentTime = System.currentTimeMillis();
        long updateTime = ToolUtil.isEmpty(SharedUtil.getString(SharedUtil.UPDATE_TIME)) ? currentTime : Long.parseLong(SharedUtil.getString(SharedUtil.UPDATE_TIME));


        /*
         * 首次进入应用提示更新
         * updateTime == 0 （第一次提示更新）
         * currentTime - updateTime >= difference  (24小时后再次更新)
         */
        if (SharedUtil.getBoolean(SharedUtil.IS_UPDATE, true) || (currentTime - updateTime >= difference)) {
            parseUpdate();
        }

    }

    /**
     * 【解析】自动更新提示
     */
    private void parseUpdate() {

        LogUtil.e("当前版本：" + ToolUtil.getVersionName());


        final String appId = Constant.APP_ID; // 应用id（APPID_AN）
        final String appVersion = "V" + ToolUtil.getVersionName();// 版本号

        Map<String, String> params = new TreeMap<>();
        params.put("merchant_id", SharedUtil.getString(SharedUtil.USER_ID));
        params.put("appid", appId);
        params.put("c_version", appVersion);

        httpUtil.post(params, UrlManage.IS_UPDATE_URL, new HttpResult<UpdateEntity>() {

            @Override
            public void onSuccess(final UpdateEntity result) {

                // 校验AppId是否和服务器一致
                if (appId.endsWith(result.getData().getAppid())) {

                    // 判断本地版本号和服务器最新的版本号是否一致 ? 不做更新提示 ： 更新提示
                    if (!appVersion.endsWith(result.getData().getN_version())) {

                        // 判断当前本地版本是否在线（0 下线 1 在线） ？ 提示更新 ： 强制更新
                        if (result.getData().getIs_online() == 1) {
                            LogUtil.e("当前版本在线");
                            final DialogUtil dialog = new DialogUtil(context);
                            dialog.setTitle("新版本上线了！");
                            dialog.setContent(result.getData().getIllustrate());
                            dialog.setLeftButton("忽略", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //开始计时，24 小时后再次提醒
                                    SharedUtil.setString(SharedUtil.UPDATE_TIME, System.currentTimeMillis() + "");
                                    SharedUtil.setBoolean(SharedUtil.IS_UPDATE, false);
                                    dialog.dismiss();
                                }
                            });
                            dialog.setRightButton("马上尝试", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // 将计时置空
                                    SharedUtil.setString(SharedUtil.UPDATE_TIME, "");
                                    SharedUtil.setBoolean(SharedUtil.IS_UPDATE, true);

                                    // 下载最新安卓包
                                    Uri uri = Uri.parse(result.getData().getUrl());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);

                                    dialog.dismiss();
                                }
                            });
                        } else {
                            LogUtil.e("当前版本已下线");
                            final DialogUtil dialog = new DialogUtil(context);
                            dialog.setTitle("发现新版本！");
                            dialog.setContent(result.getData().getIllustrate());
                            dialog.setCancelable(false);
                            dialog.setRightButton("体验惊喜", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    // 下载最新安卓包
                                    Uri uri = Uri.parse(result.getData().getUrl());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);

                                    dialog.dismiss();
                                }
                            });

                        }
                    } else {
                        LogUtil.e("当前版本已经是最新啦！");
                    }

                } else {
                    LogUtil.e("AppId验证失败");
                }


            }

            @Override
            public void onFailed(int errCord, String errMsg) {

            }
        });


    }


    /**
     * 【解析】获取账户余额时长
     */
    private void parseGetLong() {
        Map<String, String> params = new TreeMap<>();
        params.put("merchant_id", SharedUtil.getString(SharedUtil.USER_ID));

        httpUtil.post(params, UrlManage.REMAINING_URL, new HttpResult<LongEntity>() {
            @Override
            public void onSuccess(LongEntity result) {

                longText.setText(("账户时长：" + (ToolUtil.isEmpty(result.getData().getRemaining()) ? "未充值" : result.getData().getRemaining())));
            }

            @Override
            public void onFailed(int errCord, String errMsg) {

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - mLastClickTime) > 2000) {
                ToastUtil.show_("再按一次退出程序");
                mLastClickTime = System.currentTimeMillis();
            } else {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                MyApplication.removeActivity();
                System.exit(0);
                return false;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

