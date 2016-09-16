package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by pz on 2016/9/16.
 */

public class PointProgressView extends ViewGroup{

    public PointProgressView(Context context) {
        this(context, null, 0);
    }

    public PointProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {

    }
}
