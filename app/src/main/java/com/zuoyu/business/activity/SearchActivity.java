package com.zuoyu.business.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.jaeger.library.StatusBarUtil;
import com.zuoyu.business.R;
import com.zuoyu.business.adapter.SearchAdapter;
import com.zuoyu.business.application.Constant;
import com.zuoyu.business.application.MyApplication;
import com.zuoyu.business.application.UrlManage;
import com.zuoyu.business.base.BaseActivity;
import com.zuoyu.business.entity.LoginEntity;
import com.zuoyu.business.entity.SearchEntity;
import com.zuoyu.business.utils.CaseSwitchUtil;
import com.zuoyu.business.utils.PullUtil;
import com.zuoyu.business.utils.SharedUtil;
import com.zuoyu.business.utils.ToastUtil;
import com.zuoyu.business.utils.ToolUtil;
import com.zuoyu.business.utils.http.HttpResult;
import com.zuoyu.business.widget.pullable.PullListView;
import com.zuoyu.business.widget.pullable.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Function：搜索界面
 *
 * Created by JoannChen on 2017/4/17 17:32
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class SearchActivity extends BaseActivity {

    private boolean isFirstIn = true;

    LoginEntity.UserInfo mUserInfo;


    EditText searchEdit;
    TextView cancelText;
    ViewSwitcher viewSwitcher;

    private List<SearchEntity.CarInfo> list = new ArrayList<>();
    private SearchAdapter adapter;
    private PullUtil pullUtil;
    PullListView listView;

    @Override
    public int setLayout() {
        return R.layout.search_main;
    }

    @Override
    public void initTitle() {
        titleManage.setTitleText("搜车牌");
    }

    @Override
    public void initView() {

        mUserInfo = (LoginEntity.UserInfo) SharedUtil.getObject(SharedUtil.USER_INFO);

        // 搜索按钮
        searchEdit =  findViewById(R.id.et_search);
        searchEdit.addTextChangedListener(watcher);
        searchEdit.setSingleLine();
        searchEdit.setTransformationMethod(new CaseSwitchUtil());

        // 取消按钮
        cancelText =  findViewById(R.id.tv_cancel);
        cancelText.setOnClickListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEdit, 0);
            }
        }, 100);


        viewSwitcher = findViewById(R.id.viewSwitcher);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (searchEdit.getText().length() >= 3) {
                        isFirstIn = true;
                        list.clear();
                        initListView();
                        return true;
                    } else {
                        ToastUtil.show(getString(R.string.please_input_plate));
                        return false;
                    }
                }
                return false;
            }
        });


        listView = findViewById(R.id.listView);
        listView.setDividerHeight(2);


    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
//                finish();
                if (searchEdit.getText().length() >= 3) {
                    isFirstIn = true;
                    list.clear();
                    initListView();
                } else {
                    ToastUtil.show(getString(R.string.please_input_plate));
                }
                break;
        }
    }


    @Override
    public void initAfterLayout(Bundle savedInstanceState) {
        MyApplication.addActivity(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.orange), 0);
    }


    /**
     * 初始化ListView
     */
    private void initListView() {

        // 下拉刷新／上拉加载
        PullToRefreshLayout refreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshLayout);
        pullUtil = new PullUtil(refreshLayout);
        pullUtil.setPerPageCount(5);
        adapter = new SearchAdapter(context, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(activity, ParkActivity.class);
                intent.putExtra(Constant.PARK_INFO, list.get(position));
                startActivity(intent);
            }
        });

        pullUtil.setOnLoadListener(new PullUtil.onLoadListener() {
            @Override
            public void onLoad(boolean flag, Map<String, String> params) {
                if (searchEdit.getText().length() > 0) {
                    parseSearch(params);
                }
            }
        });
    }


    /**
     * 监听文本变化
     */
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            if (searchEdit.getText().length() == 0 && !isFirstIn) {
                viewSwitcher.setDisplayedChild(0);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    /**
     * 【解析】搜索车牌号
     */
    public void parseSearch(Map<String, String> params) {

        params.put("parkid", mUserInfo.getParkid());// 停车场id
        params.put("merchant_id", mUserInfo.getMerchantid());//商户id
        params.put("license_plate", searchEdit.getText().toString().trim());// 车牌号

        httpUtil.post(params, UrlManage.SEARCH_CAR_URL, new HttpResult<SearchEntity>() {
            @Override
            public void onSuccess(SearchEntity result) {

                if (isFirstIn) {
                    viewSwitcher.setDisplayedChild(1);
                }

                ToolUtil.hideKeyBoard(context, searchEdit);
                pullUtil.setResult(adapter, list, result.getData());
                pullUtil.setEmptyView(list, R.mipmap.icon_search_sorry, getString(R.string.sorry_no_plate));
                isFirstIn = false;
            }

            @Override
            public void onFailed(int errCord, String errMsg) {

            }
        });

    }

}
