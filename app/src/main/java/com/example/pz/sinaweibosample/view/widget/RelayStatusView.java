package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.util.Util;

import java.util.List;


/**
 * Created by pz on 2016/9/10.
 */

public class RelayStatusView extends LinearLayout {

    Context context;

    private TextView textView;
    private MultiImageViewGroup imageViewGroup;

    public RelayStatusView(Context context) {
        this(context, null, 0);
    }

    public RelayStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelayStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        textView = (TextView) getChildAt(0);
        imageViewGroup = (MultiImageViewGroup) getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        MyLog.v(MyLog.STATUS_VIEW_TAG, "转发控件测量！");
//        if(textView.getVisibility() != GONE) {
//            measureChild(textView, widthMeasureSpec, heightMeasureSpec);
//        }
//        if(imageViewGroup.getVisibility() != GONE) {
//            MyLog.v(MyLog.STATUS_VIEW_TAG, "转发图片控件测量！");
//            measureChild(imageViewGroup, widthMeasureSpec, heightMeasureSpec);
//        }
////        int width = Math.max(textView.getMeasuredWidth(), imageViewGroup.getMeasuredWidth());
//        MyLog.v(MyLog.STATUS_VIEW_TAG, "转发图片控件被测出高度为：" + imageViewGroup.getMeasuredHeight());
//        int maxWidth = getPaddingLeft() + getPaddingRight() + width;  //初始宽度为左右两个padding
//        int maxHeight = getPaddingTop() + getPaddingBottom() + textView.getMeasuredHeight() + imageViewGroup.getMeasuredHeight();
//
        int width = 0;
        int height = 0;
        for(int i = 0; i<getChildCount(); i++) {
            View view = getChildAt(i);
            if(view.getVisibility() != GONE) {
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                width = Math.max(width, view.getMeasuredWidth());
                height = height + view.getMeasuredHeight();
            }
        }
        width = width + getPaddingLeft() + getPaddingRight();
        height = height + getPaddingTop() + getPaddingBottom();
        MyLog.v(MyLog.STATUS_VIEW_TAG, "转发控件测出来的宽高分别为：" + width + ", " + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        final int scopeLeft = getPaddingLeft();
        final int scopeTop = getPaddingTop();
        final int scopeRight = getMeasuredWidth() - getPaddingRight();
        final int scopeBottom = getMeasuredHeight() - getPaddingBottom();
        int currentTop = scopeTop;
        MyLog.v(MyLog.STATUS_VIEW_TAG, "转发控件布局");

        for(int l = 0; l < getChildCount(); l++) {
            View view = getChildAt(l);
            if(view.getVisibility() != GONE) {
                MyLog.v(MyLog.STATUS_VIEW_TAG, "转发控件布局左上右下分别为：" + scopeLeft + ", " + currentTop + ", " + scopeRight + ", " + currentTop + view.getMeasuredHeight());
                view.layout(scopeLeft, currentTop, scopeRight, currentTop + view.getMeasuredHeight());
                currentTop = currentTop + view.getMeasuredHeight();
            }
        }
//        if(textView.getVisibility() != GONE) {
//            textView.layout(scopeLeft, scopeTop, scopeRight, scopeTop + textView.getMeasuredHeight());
//        }
//        if()
//        imageViewGroup.layout(scopeLeft, scopeTop + textView.getMeasuredHeight(),
//                scopeLeft + imageViewGroup.getMeasuredWidth(), textView.getMeasuredHeight() + imageViewGroup.getMeasuredHeight());
    }

    public void setData(Status status) {
        if(status != null) {
//            MyLog.v(MyLog.STATUS_VIEW_TAG, Util.pieceRelayBody(status));
            if(status.getDeleted() != 1 && status.getUser() != null) {
                textView.setText(Util.pieceRelayBody(status));
                fillRelayMultiImagesUri(status);
            }else {
                textView.setText(status.getText());
                imageViewGroup.setVisibility(GONE);
            }
        }
    }

    /**
     * 填充微博转发图片信息
     * @param status
     */
    private void fillRelayMultiImagesUri(Status status) {

        List<String> imageUris = Util.getPriorityImagesUris(status, Constant.SMALL_IMAGE);
//        MyLog.v(MyLog.STATUS_VIEW_TAG, "设置转发消息的图片控件可见，图片有" + imageUris.size() + "张！");
        if(imageUris == null || imageUris.size() == 0) {
            imageViewGroup.setVisibility(GONE);
            MyLog.v(MyLog.STATUS_VIEW_TAG, "设置转发消息的图片控件不可见，图片数据为空！");
            return;
        }
//        MyLog.v(MyLog.STATUS_VIEW_TAG, "设置转发消息的图片控件可见，图片有" + imageUris.size() + "张！");
        imageViewGroup.setVisibility(VISIBLE);
        //填充地址
        imageViewGroup.setData(status);
    }
}
