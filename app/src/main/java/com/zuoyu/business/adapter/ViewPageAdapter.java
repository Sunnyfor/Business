package com.zuoyu.business.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.zuoyu.business.R;
import com.zuoyu.business.entity.ViewPageInfo;
import com.zuoyu.business.widget.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * ViewPager页面切换赋值适配器
 *
 * @author Liqi
 */
public class ViewPageAdapter extends FragmentPagerAdapter implements
        ViewPager.OnPageChangeListener {

    private final Context mContext;
    private PagerSlidingTabStrip mPagerStrip;

    // 设置title颜色选择器ID
    public int textColorSelect;

    private final ArrayList<ViewPageInfo> mTabs = new ArrayList<>();


//    static class DummyTabFactory implements TabHost.TabContentFactory {
//        private final Context mContext;
//
//        public DummyTabFactory(Context context) {
//            mContext = context;
//        }
//
//        @Override
//        public View createTabContent(String tag) {
//            View v = new View(mContext);
//            v.setMinimumWidth(0);
//            v.setMinimumHeight(0);
//            return v;
//        }
//    }

    public ViewPageAdapter(FragmentManager fm,
                           PagerSlidingTabStrip pageStrip, ViewPager pager) {
        super(fm);
        mContext = pager.getContext();
        mPagerStrip = pageStrip;
        ViewPager mViewPager = pager;
        mViewPager.setAdapter(this);
        mViewPager.addOnPageChangeListener(this);
        mPagerStrip.setViewPager(mViewPager);
    }

    public void addTab(String title, String tag, Class<?> clss, Bundle args) {
        ViewPageInfo info = new ViewPageInfo(title, tag, clss, args);
        mTabs.add(info);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for (ViewPageInfo viewPageInfo : mTabs) {
            TextView v = (TextView) LayoutInflater.from(mContext).inflate(
                    R.layout.sliding_tab_item, null);
            v.setText(viewPageInfo.title);
            v.setTextColor(mContext.getResources().getColorStateList(textColorSelect));

            // 需要在此处动态添加标题图片
            mPagerStrip.addTab(v);
        }
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        ViewPageInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.clazz.getName(), info.args);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).title;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
         Log.e("ViewPager切换", "ViewPager切换"+state);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }
}
