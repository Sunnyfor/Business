package com.zuoyu.business.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * <pre>
 * Function：自定义ListView，解决ListView中嵌套GridView显示不正常的问题（1行半）
 *
 * Created by JoannChen on 2017/12/6 15:41
 * E-mail:Q8622268@foxmail.com
 * QQ:411083907
 * </pre>
 */
public class MyListView extends ListView {

    public boolean isOnMeasure;

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        isOnMeasure = true;
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        isOnMeasure = false;
        super.onLayout(changed, l, t, r, b);
    }
}
