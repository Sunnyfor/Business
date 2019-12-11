package com.zuoyu.business.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.zuoyu.business.utils.http.HttpUtil;

/**
 * <pre>
 * Function：Fragment基类
 *
 * Created by JoannChen on 2017/4/19 14:30
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private View view;
    protected SparseArray<Object> sparseArray;

    public Context context;
    public Activity activity;

    public HttpUtil httpUtil; // 初始化网络请求


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (null == view) {
            view = inflater.inflate(setLayout(), container, false);

            if (null == sparseArray)
                sparseArray = new SparseArray<>();

            getWidgetId(this.view);
        }

        context = getContext();
        activity = getActivity();


        httpUtil = new HttpUtil(context);

        return view;
    }


    /**
     * 赋值fragment布局ID
     *
     * @return 视图
     */
    public abstract int setLayout();

    /**
     * 组件点击事件
     */
    public abstract void onClickEvent(View v);

    /**
     * fragment销毁方法回调
     */
    public abstract void onDestroyCallback();


    /**
     * 此页面控件赋值的点击事件，如果需要点击事件，就重写此方法
     */
    @Override
    public void onClick(View v) {
        onClickEvent(v);
    }

    /**
     * 轮询找到View布局里面的控件
     *
     * @param view view
     */
    private void getWidgetId(View view) {
        if (!(view instanceof ViewGroup)) {
            setWidgetId(view, view.getId());
        } else {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            if (childCount > 0) {
                for (int j = 0; j < childCount; j++) {
                    View childAt = viewGroup.getChildAt(j);
                    int id = childAt.getId();
                    // 布局轮询获取控件
                    if (childAt instanceof ViewGroup) {
                        setWidgetId(childAt, id);
                        getWidgetId(childAt);
                    } else
                        setWidgetId(childAt, id);
                }
            } else
                setWidgetId(view, view.getId());
        }
    }

    /**
     * 判断控件是否赋值ID，赋值ID就存储进SparseArray集合里面并设置点击事件
     *
     * @param childAt 控件
     * @param id      控件ID
     */
    private void setWidgetId(View childAt, int id) {
        if (id > 0) {
            // 那些控件不能设置点击事件，把它排除掉
            if (!(childAt instanceof ListView)
                    && !(childAt instanceof GridView)
                    && !(childAt instanceof ViewPager)
                    && !(childAt instanceof HorizontalScrollView)
                    && !(childAt instanceof ScrollView)
                    && !(childAt instanceof WebView))
                if (childAt.isClickable())
                    childAt.setOnClickListener(this);

            sparseArray.put(id, childAt);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != view) {
            ViewGroup group = (ViewGroup) view.getParent();
            if (null != group) {
                group.removeView(view);
            }
        }
        if (null != sparseArray && sparseArray.size() > 0)
            sparseArray.clear();
        onDestroyCallback();
    }

}
