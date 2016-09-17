package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.util.Util;

/**
 * Created by pz on 2016/9/16.
 */

public class PointProgressBar extends ViewGroup{

    int pointWidth;
    int pointGapWidth;
    Context context;
    int lastSelected;

    public PointProgressBar(Context context) {
        this(context, null, 0);
    }

    public PointProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PointProgressBar, 0, 0);
        try{
            pointWidth = a.getDimensionPixelSize(R.styleable.PointProgressBar_pointWidth, Util.dpToPx(1, context));
            pointGapWidth = a.getDimensionPixelSize(R.styleable.PointProgressBar_pointGapWidth, Util.dpToPx(2, context));
        }finally {
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        int maxWidth = pointWidth * count + pointGapWidth * (count - 1);
        int maxHeight = pointWidth;
//        for(int i = 0; i<count; i++) {
//            View child = getChildAt(i);
//            maxWidth = maxWidth +
//        }
//        int childState = 0;

        setMeasuredDimension(maxWidth, maxHeight);
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        int count = getChildCount();
        int currentLeft = 0;
        for(int i = 0; i<count; i++) {
            getChildAt(i).layout(currentLeft, 0, currentLeft + pointWidth, pointWidth);
            currentLeft = currentLeft + pointWidth + pointGapWidth;
        }
    }

    public void setData(int count, int selectedItem) {
        if(getChildCount() != 0) {
            removeAllViews();
        }
        for(int i = 0; i<count; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_point, null);
            ImageView point = (ImageView) view.findViewById(R.id.image_point);
            addView(point);
        }
        setPointSelected(selectedItem);
    }

    public void setPointSelected(int selectedItem) {
        getChildAt(lastSelected).setSelected(false);
        getChildAt(selectedItem).setSelected(true);
        lastSelected = selectedItem;
    }
}
