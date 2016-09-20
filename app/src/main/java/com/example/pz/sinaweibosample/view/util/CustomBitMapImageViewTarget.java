package com.example.pz.sinaweibosample.view.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.pz.sinaweibosample.util.MyLog;

/**
 * Created by pz on 2016/9/20.
 */

public class CustomBitMapImageViewTarget extends BitmapImageViewTarget {

    ImageView targetView;
    int maxWidth;
    int maxHeight;

    public CustomBitMapImageViewTarget(ImageView view) {
        this(view, 200, 200);
    }

    public CustomBitMapImageViewTarget(ImageView view, int maxWidth, int maxHeight) {
        super(view);
        targetView = view;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
    }

    @Override
    protected void setResource(Bitmap bitmap) {
        MyLog.v(MyLog.WIDGET_TAG, targetView.getLayoutParams().height + "");
        float radio = (float)bitmap.getWidth() / (float)bitmap.getHeight();
        MyLog.v(MyLog.WIDGET_TAG, "宽高比为：" + radio);

        if((targetView.getLayoutParams().width = (int)(maxHeight * radio)) > maxWidth) {
            MyLog.v(MyLog.WIDGET_TAG, "算出来的宽度和最大宽度分别为：" + targetView.getLayoutParams().width + ", " + maxWidth);
            targetView.getLayoutParams().width = maxWidth;
        }
        targetView.requestLayout();
        super.setResource(bitmap);
    }
}
