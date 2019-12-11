package com.zuoyu.business.base;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.zuoyu.business.R;
import com.zuoyu.business.adapter.ViewPageAdapter;
import com.zuoyu.business.utils.LogUtil;
import com.zuoyu.business.widget.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * <pre>
 * Function：给ViewPager每个页面赋值
 *
 * Created by JoannChen on 2017/3/10 15:48
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class BaseViewPagerFragment extends BaseFragment {

    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageAdapter mTabsAdapter;
    protected ViewGroup tabsLayout;

    protected int index = -1;// 要展示的索引
    protected int cacheLimit = 0;// 设置viewpager缓存多少个


    protected int backgroundId, slidingBlock; // 默认滑块背景图
    protected int textColorSelect; // 设置title颜色选择器ID

    //PagerSlidingTabStrip对象是否可以滑动,默认可以滑动
    protected boolean slidingTag = true;
    //PagerSlidingTabStrip对象title文本内容字体选择效果是否需要放大,默认是不放大
    protected boolean textSizeTag = false;


    public final byte TYPE_ONE = 0x0;
    public final byte TYPE_TWO = 0x1;
    public final byte TYPE_THREE = 0x2;
    // 存储fragment对象的集合
    private ArrayList<ViewHolder> fragmentObjList;

    public static BaseViewPagerFragment newInstance() {
        return new BaseViewPagerFragment();
    }


    @Override
    public int setLayout() {
        return R.layout.base_viewpage_fragment;
    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void onDestroyCallback() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabstrip);
        mTabStrip.setBackgroundResource(backgroundId);
        mTabStrip.setSlidingBlockDrawable(getResources().getDrawable(
                slidingBlock));
        mTabStrip.slidingTag = this.slidingTag;
        mTabStrip.textSizeTag = this.textSizeTag;
        mTabsAdapter = new ViewPageAdapter(getChildFragmentManager(),
                mTabStrip, mViewPager);
        mTabsAdapter.textColorSelect = textColorSelect;
        mViewPager.setOffscreenPageLimit(cacheLimit);// 设置ViewPare一次性加载几个页面。
        // 回调方法给子类调用
        onSetupTabAdapter(mTabsAdapter);
        mTabsAdapter.notifyDataSetChanged();
        if (index != -1)
            // 设置要展示的页面索引值
            mViewPager.setCurrentItem(index == -1 ? 0 : index, true);
        tabsLayout = mTabStrip.getTabsLayoutTest();
        // 监听事件(点击之后隐藏红点)
        /*
         * mTabStrip.setOnClickTabListener(new OnClickTabListener() {
		 *
		 * @Override public void onClickTab(View tab, int index) { View
		 * view=data.getChildAt(3); TextView viewText =(TextView) view;
		 * viewText.setText("哈哈"); } });
		 */
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mViewPager.getCurrentItem());
    }


    public BaseViewPagerFragment() {
        if (null == fragmentObjList)
            fragmentObjList = new ArrayList<>();

    }

    /**
     * 设置当前那个页面显示
     *
     * @param index 页面索引值
     * @return this
     */
    public BaseViewPagerFragment setViewpagerIndxe(int index) {
        this.index = index;
        return this;
    }

    /**
     * 设置viewpager一次性加载几个对象
     *
     * @param cacheLimit
     * @return this
     */
    public BaseViewPagerFragment setViewpagerCacheLimit(int cacheLimit) {
        this.cacheLimit = cacheLimit;
        return this;
    }

    /**
     * 设置滑块默认图片和滑动时的图片
     *
     * @param backgroundId 默认图片
     * @param slidingBlock 滑动图片
     * @return this
     */
    public BaseViewPagerFragment setSlidingTabStripImage(int backgroundId,
                                                         int slidingBlock) {
        this.backgroundId = backgroundId;
        this.slidingBlock = slidingBlock;
        return this;
    }

    /**
     * 设置滑块字体颜色选择器
     *
     * @param color
     * @return this
     */
    public BaseViewPagerFragment setTextColorSelect(int color) {
        this.textColorSelect = color;
        return this;
    }

    /**
     * 设置标题底部是否需要跟着手势滑动效果
     *
     * @param slidingTag
     * @return this
     */
    public BaseViewPagerFragment setSlidingTag(boolean slidingTag) {
        this.slidingTag = slidingTag;
        return this;
    }

    /**
     * 设置是否title字体选中后是否需要变大效果
     *
     * @return this
     */
    public BaseViewPagerFragment setTextSizeTag(boolean textSizeTag) {
        this.textSizeTag = textSizeTag;
        return this;
    }

    /**
     * 此对象实例化之后就初始化ViewPager片段页面
     */
    protected void onSetupTabAdapter(ViewPageAdapter adapter) {
        if (!fragmentObjList.isEmpty()) {
            for (int i = 0; i < fragmentObjList.size(); i++) {
                ViewHolder fragmentObj = fragmentObjList.get(i);
                Bundle bundle = new Bundle();
                bundle.putInt(i + "", i);
                adapter.addTab(fragmentObj.titleName,
                        fragmentObj.clazz.getName(), fragmentObj.clazz, bundle);
            }
        } else
            LogUtil.e("BaseViewPagerFragment对象中fragmentObjList展示集合没有赋值对象,请调用setFragmentObjList()赋值");
    }

    /**
     * 根据需要的数据设置要展示fragment存储对象集合
     *
     * @param array     string[]数组
     * @param clazzList fragment.class对象集合
     * @return obj
     */
    public BaseViewPagerFragment setFragmentObjList(String[] array, ArrayList<Class<?>> clazzList) {
        if (null != clazzList && !clazzList.isEmpty()) {

            for (int i = 0; i < clazzList.size(); i++) {
                // 限制的fragment对象不能大于标题名字的数组
                if (i < array.length) {
                    ViewHolder holder = new ViewHolder();
                    holder.clazz = clazzList.get(i);
                    holder.titleName = array[i];
                    fragmentObjList.add(holder);
                }
            }
        }
        return this;
    }


    /**
     * fragment存储定义对象
     */
    private class ViewHolder {
        String titleName;
        Class<?> clazz;
    }
}
