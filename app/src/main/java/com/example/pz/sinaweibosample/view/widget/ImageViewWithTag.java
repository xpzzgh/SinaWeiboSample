package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by pz on 2016/9/20.
 */

public class ImageViewWithTag extends ImageView {

    public ImageViewWithTag(Context context) {
        this(context, null, 0);
    }

    public ImageViewWithTag(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewWithTag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void drawTag(Canvas canvas) {

    }
}
