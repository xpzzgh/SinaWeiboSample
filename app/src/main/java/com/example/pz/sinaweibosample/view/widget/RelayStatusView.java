package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.util.AttributeSet;
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
        measureChild(textView, widthMeasureSpec, heightMeasureSpec);
        measureChild(imageViewGroup, widthMeasureSpec, heightMeasureSpec);
        int width = Math.max(textView.getMeasuredWidth(), imageViewGroup.getMeasuredWidth());

        int maxWidth = getPaddingLeft() + getPaddingRight() + width;  //初始宽度为左右两个padding
        int maxHeight = getPaddingTop() + getPaddingBottom() + textView.getMeasuredHeight() + imageViewGroup.getMeasuredHeight();
//        int childState = 0;
//        childState = combineMeasuredStates(childState, view.getMeasuredState());
        setMeasuredDimension(maxWidth, maxHeight);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        final int scopeLeft = getPaddingLeft();
        final int scopeTop = getPaddingTop();
        final int scopeRight = getMeasuredWidth() - getPaddingRight();
        final int scopeBottom = getMeasuredHeight() - getPaddingBottom();

        textView.layout(scopeLeft, scopeTop, scopeRight, scopeTop + textView.getMeasuredHeight());
        imageViewGroup.layout(scopeLeft, scopeTop + textView.getMeasuredHeight(), scopeLeft + imageViewGroup.getMeasuredWidth(), scopeBottom);
    }

    public void setData(Status status) {
        if(status != null) {
//            MyLog.v(MyLog.STATUS_VIEW_TAG, Util.pieceRelayBody(status));
            textView.setText(Util.pieceRelayBody(status));
            fillRelayMultiImagesUri(status);
        }
    }

    /**
     * 填充微博转发图片信息
     * @param status
     */
    private void fillRelayMultiImagesUri(Status status) {
        List<String> imageUris = Util.getPriorityImagesUris(status, Constant.SMALL_IMAGE);
        if(imageUris == null) {
            imageViewGroup.setVisibility(GONE);
            return;
        }else if(imageUris.size() == 1) {
            imageUris = Util.getPriorityImagesUris(status, Constant.MEDIUM_IMAGE);
        }

        //填充地址
        imageViewGroup.setData(imageUris);
    }
}
