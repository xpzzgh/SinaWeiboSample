package com.example.pz.sinaweibosample.view.util;

import android.graphics.Matrix;
import android.graphics.Rect;

import com.facebook.drawee.drawable.ScalingUtils;

/**
 * 自定义缩放类型，适配屏幕宽度，高度不够时居中显示，足够时从上往下显示
 * Created by pz on 2016/9/16.
 */

public class FitWidthScaleType implements ScalingUtils.ScaleType {

    @Override
    public Matrix getTransform(Matrix outTransform, Rect parentBounds, int childWidth, int childHeight, float focusX, float focusY) {
        float scaleX = (float) parentBounds.width() / (float) childWidth;
//        float scaleY = (float) parentBounds.height() / (float) childHeight;
//        float dx = parentBounds.left + (parentBounds.width() - childWidth * scaleX)
        float offsetY = (parentBounds.height() - childHeight * scaleX) * 0.5f;
        float dy;
        float dx = parentBounds.left;
        if(offsetY > 0) {
            dy = parentBounds.top + offsetY;
        }else {
            dy = parentBounds.top;
        }

        outTransform.setScale(scaleX, scaleX);
        outTransform.postTranslate(dx, dy);
        return outTransform;
    }
}
