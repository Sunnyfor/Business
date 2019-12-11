package com.zuoyu.business.utils.http;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoyu.business.R;
import com.zuoyu.business.activity.ResultActivity;
import com.zuoyu.business.application.Constant;
import com.zuoyu.business.utils.IntentUtil;
import com.zuoyu.business.utils.LogUtil;
import com.zuoyu.business.utils.SharedUtil;
import com.zuoyu.business.utils.ToastUtil;
import com.zuoyu.business.utils.ToolUtil;
import com.zuoyu.business.widget.ExitLoginDialog;
import com.zuoyu.business.widget.LoadingProgress;

import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * <pre>
 * Function：封装网络请求
 *
 * Created by JoannChen on 2017/3/7 14:46
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class HttpUtil {
    private Context context;
    private boolean isCache = false;

    // 加载进度条
    private LoadingProgress loadingProgress;

    /**
     * 外层结构是对象
     */
    static final int OBJECT_TYPE = 0x1;

    /**
     * 外层结构是集合
     */
    static final int ARRAY_TYPE = 0x2;

    /**
     * 构造方法
     *
     * @param context 请求标记（用于销毁请求）
     */
    public HttpUtil(Context context) {
        this.context = context;
        loadingProgress = new LoadingProgress(context);
    }

    /**
     * 构造方法
     *
     * @param context 请求标记（用于销毁请求）
     * @param info    加载框提示语
     */
    public HttpUtil(Context context, String info) {
        this.context = context;
        loadingProgress = new LoadingProgress(context, info);
    }


    /**
     * 生成头信息
     *
     * @param urlParams 访问的url
     * @return 生成头信息
     */
    private Map<String, String> getHeader(String urlParams) {

        Map<String, String> header = new HashMap<>();

        String token = SharedUtil.getString(SharedUtil.TOKEN);

        LogUtil.i("【token】" + token);

        // 要加密数据
        StringBuilder builder = new StringBuilder();

        // 时间戳
        final String timestampStr = String.valueOf(System.currentTimeMillis() / 1000);


        try {
            builder.append(timestampStr)
                    .append(URLDecoder.decode(urlParams, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String builderAppend = builder.toString().trim();

        try {

            //加密
            byte[] data = HMAC.encodeHmacSHA256(
                    builderAppend.getBytes(), (ToolUtil.isEmpty(token) ? Constant.PROJECT_KEY.getBytes() : token.getBytes()));

            final String encryptContent = new String(Hex.encode(data));

            // 创建加解密对象，放入头信息
            header.put("Timestamp", timestampStr);
            header.put("Authorization", encryptContent); // 加密串
            header.put("Projectid", Constant.PROJECT_ID);


            LogUtil.e("【头信息加密前】：" + builderAppend);
            LogUtil.e("【头信息加密后】：" + encryptContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return header;
    }

    /**
     * POST 加密请求
     *
     * @param params 请求参数，需要一个有序的集合，所以用TreeMap
     * @param url    请求URL
     * @param result 结果回调
     */
    public <T> void post(Map<String, String> params, final String url, final HttpResult<T> result) {

        if (!ToolUtil.isNetConnection()) {
            result.onFailed(0, context.getString(R.string.network_anomaly));
            ToastUtil.showNetWorkError();
            return;
        }

        if (!loadingProgress.isShowing()) {
            loadingProgress.show();
        }


        OkHttpUtils.post()
                .headers(getHeader(buildUrlParams(url, params)))
                .params(getJsonParams(params))
                .url(url)
                .tag(context)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                if (loadingProgress.isShowing()) {
                    loadingProgress.dismiss();
                }

                LogUtil.e("URL:" + url + " 状态码：" + id + " 错误信息：" + e.getMessage());
                result.onFailed(id, e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {

                if (loadingProgress.isShowing()) {
                    loadingProgress.dismiss();
                }

                LogUtil.e("【当前版本】V" + ToolUtil.getVersionName());
                LogUtil.e("【Post请求JSON】" + response);


                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(response);

                    switch (jsonObject.optInt("code")) {
                        case 200:
                            result.onSuccess((T) parseJson(response, result.getObj(), result.getType()));
                            break;
                        case 401:
//                            MyApplication.exitApplication((Activity) context, true);
//                            ToastUtil.show("401");
                            ExitLoginDialog.show_(context);
//                            ExitLoginDialog.show(context);
//                            MyApplication.getDialog(context).show();
                            break;
                        case 409:
                            // App版本不存在
                            break;
                        case 609:
                            // 该车辆已有优惠券
                            IntentUtil.start((Activity) context, ResultActivity.class, Constant.SCAN_RESULT, 609, false);
                            break;
                        case 611:
                            // 该优惠券使用已达上限
                            IntentUtil.start((Activity) context, ResultActivity.class, Constant.SCAN_RESULT, 611, false);
                            break;
                        default:
                            ToastUtil.show(ToolUtil.isEmpty(jsonObject.optString("msg")) ?
                                    context.getString(R.string.network_anomaly) :
                                    jsonObject.optString("msg"));
                            break;

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * GET 加密请求
     *
     * @param params 请求参数
     * @param url    请求URL
     * @param result 结果回调
     */
    public <T> void get(Map<String, String> params, final String url, final HttpResult<T> result) {

        if (!ToolUtil.isNetConnection()) {
            return;
        }

        if (!loadingProgress.isShowing()) {
            loadingProgress.show();
        }

        OkHttpUtils.get()
                .headers(getHeader(buildUrlParams(url, params)))
                .params(params)
                .url(url)
                .tag(context)
                .build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {

                if (loadingProgress.isShowing()) {
                    loadingProgress.dismiss();
                }

                result.onFailed(id, e.getMessage());
                LogUtil.i("URL:" + url + " 状态码：" + id + " 错误信息：" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {

                if (loadingProgress.isShowing()) {
                    loadingProgress.dismiss();
                }

                LogUtil.i("Get请求JSON：" + response);
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(response);
                    int code = jsonObject.optInt("code");

                    if (code == 200) {
                        result.onSuccess((T) parseJson(response, result.getObj(), result.getType()));
                    } else {
                        ToastUtil.show(jsonObject.optString("msg"));
                    }

                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 解析Json
     *
     * @param json  json串
     * @param clazz 当前类
     * @param type  json数据格式：OBJECT 对象；LIST 集合
     * @param <T>   泛型
     * @return 当前实体类
     */
    private <T> T parseJson(String json, Class<?> clazz, int type) {
        if (json != null && !json.equals("")) {

            // 是否存储JSON
            try {
                if (isCache) {
                    //存储JSON
                }

                switch (type) {
                    case OBJECT_TYPE:
                        return (T) JSON.parseObject(json, clazz);
                    case ARRAY_TYPE:
                        return (T) JSON.parseArray(json, clazz);
                    default:
                        return (T) json;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 销毁请求
     */
    public void cancel() {
        OkHttpUtils.getInstance().cancelTag(context);
    }


    /**
     * 参数转换JSON格式
     *
     * @param params 参数
     * @return map集合
     */
    private Map<String, String> getJsonParams(Map<String, String> params) {

        Map<String, String> jsonParams = new HashMap<>();

        if (params != null) {
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry set : params.entrySet()) {
                try {
                    jsonObject.put(set.getKey().toString(), set.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            jsonParams.put("params", jsonObject.toString());
        }

        return jsonParams;
    }


    /**
     * 追加参数到URL
     *
     * @param params 请求参数
     * @return 带参数的URL
     */
    private String buildUrlParams(String url, Map<String, String> params) {
        String urlParams = "?";
        try {
            for (Map.Entry entry : params.entrySet()) {

                if (urlParams.length() > 1) {
                    urlParams = urlParams + "&";
                }

                urlParams = urlParams
                        + entry.getKey().toString()
                        + "="
                        + URLEncoder.encode(entry.getValue().toString(),
                        "utf-8");

            }


            LogUtil.e("【请求URL】" + url + urlParams);

        } catch (Exception e) {
            LogUtil.e("【HttpUtil中请求参数为空】");
        }

        return url + urlParams;

    }
}
