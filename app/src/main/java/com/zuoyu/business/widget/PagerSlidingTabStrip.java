package com.zuoyu.business.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zuoyu.business.R;
import com.zuoyu.business.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <pre>
 * Function：专为ViewPager定制的滑动选项卡
 *
 * Created by JoannChen on 2017/3/10 15:48
 * QQ:411083907
 * E-mail:q8622268@163.com
 * Version Information：V 1.0
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class PagerSlidingTabStrip extends HorizontalScrollView implements View.OnClickListener {


    private int currentPosition;// 当前位置
    private float currentPositionOffset;// 当前位置偏移量
    private View currentSelectedView;// 当前标题项

    private boolean disableViewPager;// 禁用ViewPager
    private boolean allowWidthFull; // 内容宽度无法充满时，允许自动调整Item的宽度以充满
    public boolean textSizeTag = false; // 用来做TextView文字是否变大，默认不会变大
    public boolean slidingTag = true;// 对象是否可以滑动,默认可以滑动

    private boolean start;
    private int lastOffset;
    private int lastScrollX = 0;


    // 滑块
    private Drawable slidingBlockDrawable;
    private ViewPager viewPager;

    // 标题项布局
    private ViewGroup tabsLayout;

    // 页面改变监听器
    private OnPageChangeListener onPageChangeListener;
    private OnClickTabListener onClickTabListener;
    private List<View> tabViews;


    /**
     * 返回标题布局里面的所有子布局（只是为了让标题上面显示红点）
     *
     * @return 子布局
     */
    public ViewGroup getTabsLayoutTest() {
        return tabsLayout;
    }

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 隐藏横向滑动提示条
        setHorizontalScrollBarEnabled(false);

        if (attrs != null) {
            TypedArray attrsTypedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
            if (attrsTypedArray != null) {
                allowWidthFull = attrsTypedArray.getBoolean(
                        R.styleable.PagerSlidingTabStrip_allowWidthFull, false);
                slidingBlockDrawable = attrsTypedArray
                        .getDrawable(R.styleable.PagerSlidingTabStrip_slidingBlock);
                disableViewPager = attrsTypedArray.getBoolean(
                        R.styleable.PagerSlidingTabStrip_disableViewPager,
                        false);
                attrsTypedArray.recycle();
            }
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!allowWidthFull) {
            return;
        }

        ViewGroup tabsLayout = getTabsLayout();
        ViewUtil.setViewSize(tabsLayout, 88, 0);


        if (tabsLayout == null || tabsLayout.getMeasuredWidth() >= getMeasuredWidth()) {
            return;
        }

        if (tabsLayout.getChildCount() <= 0) {
            return;
        }

        if (tabViews == null) {
            tabViews = new ArrayList<>();
        } else {
            tabViews.clear();
        }

        for (int w = 0; w < tabsLayout.getChildCount(); w++) {
            tabViews.add(tabsLayout.getChildAt(w));
            ViewUtil.setTextSize((TextView) tabsLayout.getChildAt(w), 34);
        }

        adjustChildWidthWithParent(
                tabViews,
                getMeasuredWidth() - tabsLayout.getPaddingLeft()
                        - tabsLayout.getPaddingRight(), widthMeasureSpec,
                heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 调整views集合中的View，让所有View的宽度加起来正好等于parentViewWidth
     *
     * @param views                   子View集合
     * @param parentViewWidth         父Vie的宽度
     * @param parentWidthMeasureSpec  父View的宽度规则
     * @param parentHeightMeasureSpec 父View的高度规则
     */
    private void adjustChildWidthWithParent(
            List<View> views, int parentViewWidth,
            int parentWidthMeasureSpec, int parentHeightMeasureSpec) {

        // 先去掉所有子View的外边距
        for (View view : views) {
            if (view.getLayoutParams() instanceof MarginLayoutParams) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view
                        .getLayoutParams();
                parentViewWidth -= lp.leftMargin + lp.rightMargin;
            }
        }

        // 去掉宽度大于平均宽度的View后再次计算平均宽度
        int averageWidth = parentViewWidth / views.size();
        int bigTabCount = views.size();
        while (true) {
            Iterator<View> iterator = views.iterator();
            while (iterator.hasNext()) {
                View view = iterator.next();
                if (view.getMeasuredWidth() > averageWidth) {
                    parentViewWidth -= view.getMeasuredWidth();
                    bigTabCount--;
                    iterator.remove();
                }
            }
            averageWidth = parentViewWidth / bigTabCount;
            boolean end = true;
            for (View view : views) {
                if (view.getMeasuredWidth() > averageWidth) {
                    end = false;
                }
            }
            if (end) {
                break;
            }
        }

        // 修改宽度小于新的平均宽度的View的宽度
        for (View view : views) {
            if (view.getMeasuredWidth() < averageWidth) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.width = averageWidth;
                view.setLayoutParams(layoutParams);
                // 再次测量让新宽度生效
                measureChildWithMargins(view, parentWidthMeasureSpec, 0,
                        parentHeightMeasureSpec, 0);

            }
        }
    }

    /**
     * 标题点击事件绑定监听
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        ViewGroup tabViewGroup = getTabsLayout();

        if (tabViewGroup != null) {

            // 初始化滑块位置以及选中状态
            currentPosition = viewPager != null ? viewPager.getCurrentItem() : 0;
            if (!disableViewPager) {
                scrollToChild(currentPosition, 0); // 移动滑块到指定位置
                selectedTab(currentPosition); // 选中指定位置的TAB
                viewPager.setCurrentItem(currentPosition, true);// 设置ViewPager当前显示那个页面
            }

            // 给每一个tab设置点击事件，当点击的时候切换Pager
            for (int w = 0; w < tabViewGroup.getChildCount(); w++) {
                View itemView = tabViewGroup.getChildAt(w);
                itemView.setTag(w);
                itemView.setOnClickListener(this);

            }
        }
    }

    @Override
    public void onClick(View v) {
        int index = (Integer) v.getTag();
        if (onClickTabListener != null) {
            onClickTabListener.onClickTab(v, index);
        }
        if (viewPager != null) {
            viewPager.setCurrentItem(index, true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (disableViewPager)
            return;
        /* 绘制滑块 */
        ViewGroup tabsLayout = getTabsLayout();
        if (tabsLayout != null && tabsLayout.getChildCount() > 0
                && slidingBlockDrawable != null) {
            View currentTab = tabsLayout.getChildAt(currentPosition);
            if (currentTab != null) {
                float slidingBlockLeft = currentTab.getLeft();
                float slidingBlockRight = currentTab.getRight();
                // 判断是否允许手势滑动
                if (slidingTag) {
                    // 设置字体变大就不允许手势滑动
                    if (!textSizeTag) {
                        if (currentPositionOffset > 0f
                                && currentPosition < tabsLayout.getChildCount() - 1) {
                            View nextTab = tabsLayout
                                    .getChildAt(currentPosition + 1);

                            if (nextTab != null) {
                                final float nextTabLeft = nextTab.getLeft();
                                final float nextTabRight = nextTab.getRight();
                                slidingBlockLeft = (currentPositionOffset
                                        * nextTabLeft + (1f - currentPositionOffset)
                                        * slidingBlockLeft);
                                slidingBlockRight = (currentPositionOffset
                                        * nextTabRight + (1f - currentPositionOffset)
                                        * slidingBlockRight);

                            }
                        }
                    }
                }
                slidingBlockDrawable.setBounds((int) slidingBlockLeft, 0,
                        (int) slidingBlockRight, getHeight());
                slidingBlockDrawable.draw(canvas);
            }
        }

    }

    /**
     * 获取布局
     */
    private ViewGroup getTabsLayout() {
        if (tabsLayout == null) {
            if (getChildCount() > 0) {
                tabsLayout = (ViewGroup) getChildAt(0);
            } else {
                removeAllViews();
                tabsLayout = new LinearLayout(getContext());
                addView(tabsLayout, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
        return tabsLayout;
    }


    /**
     * 滚动到指定的位置
     */
    private void scrollToChild(int position, int offset) {
        ViewGroup tabsLayout = getTabsLayout();
        if (tabsLayout != null && tabsLayout.getChildCount() > 0
                && position < tabsLayout.getChildCount()) {
            View view = tabsLayout.getChildAt(position);
            if (view != null) {
                // 计算新的X坐标
                int newScrollX = view.getLeft() + offset;
                if (position > 0 || offset > 0) {
                    newScrollX -= 240 - getOffset(view.getWidth()) / 2;
                }

                // 如果同上次X坐标不一样就执行滚动
                if (newScrollX != lastScrollX) {
                    lastScrollX = newScrollX;
                    scrollTo(newScrollX, 0);
                }
            }
        }
    }


    /**
     * 获取偏移量
     */
    private int getOffset(int newOffset) {
        if (lastOffset < newOffset) {
            if (start) {
                lastOffset += 1;
                return lastOffset;
            } else {
                start = true;
                lastOffset += 1;
                return lastOffset;
            }
        }
        if (lastOffset > newOffset) {
            if (start) {
                lastOffset -= 1;
                return lastOffset;
            } else {
                start = true;
                lastOffset -= 1;
                return lastOffset;
            }
        } else {
            start = true;
            lastOffset = newOffset;
            return lastOffset;
        }
    }


    /**
     * 选中指定位置的TAB
     */
    private void selectedTab(int currentSelectedTabPosition) {
        ViewGroup tabsLayout = getTabsLayout();
        if (currentSelectedTabPosition > -1 && tabsLayout != null
                && currentSelectedTabPosition < tabsLayout.getChildCount()) {

            if (currentSelectedView != null) {
                currentSelectedView.setSelected(false);
            }

            currentSelectedView = tabsLayout.getChildAt(currentSelectedTabPosition);

            if (currentSelectedView != null) {
                currentSelectedView.setSelected(true);

            }

            Log.e("PagerSlidingTabStrip", "选中指定位置的TAB方法执行了");
        }
    }


    /**
     * 添加Tab
     */
    public void addTab(View tabView, int index) {
        if (tabView != null) {
            getTabsLayout().addView(tabView, index);
            requestLayout();
        }
    }

    /**
     * 添加Tab
     */
    public void addTab(View tabView) {
        addTab(tabView, -1);
    }

    /**
     * 添加Tab
     *
     * @param tabViews 可以一次添加多个Tab
     */
    public void addTab(View... tabViews) {
        if (tabViews != null) {
            for (View view : tabViews) {
                getTabsLayout().addView(view);
            }
            requestLayout();
        }
    }

    /**
     * 添加Tab
     */
    public void addTab(List<View> tabViews) {
        if (tabViews != null) {
            for (View view : tabViews) {
                getTabsLayout().addView(view);
            }
            requestLayout();
        }
    }

    /**
     * 设置ViewPager
     *
     * @param viewPager ViewPager
     */
    public void setViewPager(ViewPager viewPager) {
        if (disableViewPager)
            return;
        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                selectedTab(position);

                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrolled(int nextPagePosition,
                                       float positionOffset, int positionOffsetPixels) {
                ViewGroup tabsLayout = getTabsLayout();
                if (nextPagePosition < tabsLayout.getChildCount()) {
                    View view = tabsLayout.getChildAt(nextPagePosition);
                    if (view != null) {
                        currentPosition = nextPagePosition;
                        currentPositionOffset = positionOffset;
                        scrollToChild(nextPagePosition,
                                (int) (positionOffset * view.getWidth()));
                        invalidate();
                    }
                }
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(nextPagePosition,
                            positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(arg0);
                }
            }
        });
        requestLayout();
    }


    /**
     * 设置Page切换监听器
     *
     * @param onPageChangeListener Page切换监听器
     */
    public void setOnPageChangeListener(
            OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }


    /**
     * 设置是否充满屏幕
     *
     * @param allowWidthFull true：当内容的宽度无法充满屏幕时，自动调整每一个Item的宽度以充满屏幕
     */
    public void setAllowWidthFull(boolean allowWidthFull) {
        this.allowWidthFull = allowWidthFull;
        requestLayout();
    }


    /**
     * 设置滑块图片
     */
    public void setSlidingBlockDrawable(Drawable slidingBlockDrawable) {
        this.slidingBlockDrawable = slidingBlockDrawable;
        requestLayout();
    }


    /**
     * 获取Tab总数
     */
    public int getTabCount() {
        ViewGroup tabsLayout = getTabsLayout();
        return tabsLayout != null ? tabsLayout.getChildCount() : 0;
    }


    /**
     * 设置Tab点击监听器
     *
     * @param onClickTabListener Tab点击监听器
     */
    public void setOnClickTabListener(OnClickTabListener onClickTabListener) {
        this.onClickTabListener = onClickTabListener;
    }


    /**
     * 设置不使用ViewPager
     *
     * @param disableViewPager 不使用ViewPager
     */
    public void setDisableViewPager(boolean disableViewPager) {
        this.disableViewPager = disableViewPager;
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(onPageChangeListener);
            viewPager = null;
        }
        requestLayout();
    }


    /**
     * Tab点击监听器
     *
     * @author xiaopan
     */
    public interface OnClickTabListener {
        void onClickTab(View tab, int index);
    }
}
