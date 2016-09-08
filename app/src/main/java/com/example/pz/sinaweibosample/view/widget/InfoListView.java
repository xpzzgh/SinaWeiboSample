package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.support.percent.PercentFrameLayout;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.util.MyLog;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pz on 2016/9/8.
 */

public class InfoListView extends CardView {

    Map<String, String> data;
    Context context;

    public InfoListView(Context context) {
        this(context, null, 0);
    }

    public InfoListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InfoListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxWidth = getPaddingLeft() + getPaddingRight();
        int maxHeight = getPaddingTop() + getPaddingBottom();
        int childState = 0;

        for(int i = 0; i<count; i++) {
            View child = getChildAt(i);

            measureChild(child, widthMeasureSpec, heightMeasureSpec);

//            MyLog.v(MyLog.STATUS_VIEW_TAG, "子view的bottom是：" + child.getBottom());
            MyLog.v(MyLog.STATUS_VIEW_TAG, "第" + i + "个子view的高度为：" + child.getMeasuredHeight());
            maxHeight += child.getMeasuredHeight();
        }
        if(count > 0) {
            maxWidth += getChildAt(0).getMeasuredWidth();
            childState = combineMeasuredStates(childState, getChildAt(0).getMeasuredState());
        }

        maxWidth = Math.max(maxWidth, getSuggestedMinimumHeight());
        maxHeight = Math.max(maxHeight, getSuggestedMinimumWidth());
        //设置测量结果
        MyLog.v(MyLog.STATUS_VIEW_TAG, "信息控件测完之后的宽度和高度分别为：" + maxWidth + ", " + maxHeight);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        //把子view的范围圈出来
        final int scopeLeft = getPaddingLeft();
        final int scopeTop = getPaddingTop();
        final int scopeRight = getMeasuredWidth() - getPaddingRight();
        int currentTop = scopeTop;

        for(int i = 0; i<count; i++) {
            View view = getChildAt(i);
            MyLog.v(MyLog.WIDGET_TAG, "用户信息列表的左右上下分别为：" + scopeLeft + ", " + currentTop + ", " + scopeRight + ", " +  (currentTop + view.getMeasuredHeight()));
            view.layout(scopeLeft, currentTop, scopeRight, currentTop + view.getMeasuredHeight());
            currentTop += view.getMeasuredHeight();
        }
    }

    public void setData(Map<String, String> data) {
        this.data = data;
        removeAllViews();
        handleData();
    }

    /**
     * 处理数据，根据数据的数目来新增view
     */
    private void handleData() {
        Set<String> keySet = data.keySet();

        for(String key:keySet) {
            String value = data.get(key);
            View view = LayoutInflater.from(context).inflate(R.layout.textview_simple, null, false);
            PercentFrameLayout frameLayout = (PercentFrameLayout)view.findViewById(R.id.view_textview_simple);

            TextView keyText = (TextView)frameLayout.findViewById(R.id.text_simple_key);
            TextView valueText = (TextView)frameLayout.findViewById(R.id.text_simple_value);
            MyLog.v(MyLog.WIDGET_TAG, "用户基本信息键值对：" + key + "：" + value);
            keyText.setText(key);
            if(value != null) {
                valueText.setText(value);
            }
            addView(frameLayout);
        }
    }
}
