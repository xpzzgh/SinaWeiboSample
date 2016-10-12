package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.model.entity.ImageInfo;
import com.example.pz.sinaweibosample.util.Util;

/**
 * Created by pz on 2016/9/27.
 */

public class ImageViewWithClose extends ViewGroup {

    int imageWidth;
    int closeImageWidth;
    int count;
    Context context;
    ImageView imageView;
    ImageView closeImageView;
    OnViewClickListener onViewClickListener;

    public ImageViewWithClose(Context context) {
        this(context, null, 0);
    }

    public ImageViewWithClose(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewWithClose(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageViewWithClose, 0, 0);
        try{
            imageWidth = typedArray.getDimensionPixelSize(R.styleable.ImageViewWithClose_imageWidth, Util.dpToPx(50, context));
            closeImageWidth = typedArray.getDimensionPixelSize(R.styleable.ImageViewWithClose_closeImageWidth, Util.dpToPx(8, context));
        }finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        count = getChildCount();
        if(count != 2) {
            throw new RuntimeException("ImageViewWithClose只能有两个子view，包含image和close image");
        }

        imageView= (ImageView) getChildAt(0);
        closeImageView = (ImageView) getChildAt(1);
        closeImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onViewClickListener != null) {
                    onViewClickListener.onClick(v);
                }
            }
        });
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        measureChild(imageView, widthMeasureSpec, heightMeasureSpec);
//        measureChild(closeImageView, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(imageWidth, imageWidth);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //把子view的范围圈出来
        final int scopeLeft = getPaddingLeft();
        final int scopeTop = getPaddingTop();
        final int scopeRight = getMeasuredWidth() - getPaddingRight();
        final int scopeBottom = getMeasuredHeight() - getPaddingBottom();
        getChildAt(0).layout(scopeLeft, scopeTop, scopeRight, scopeBottom);
        getChildAt(1).layout(scopeRight - closeImageWidth - closeImageView.getPaddingRight(), scopeTop + closeImageView.getPaddingTop(),
                scopeRight - closeImageView.getPaddingRight(), closeImageWidth + closeImageView.getPaddingTop());
//        imageView.layout(l, t, r, b);
//        closeImageView.layout(r - closeImageWidth, l, r, closeImageWidth);
    }

    public void setData(ImageInfo imageInfo, boolean isCloseVisible) {

        if(imageView != null && closeImageView != null) {

            if(isCloseVisible) {
                if(closeImageView.getVisibility() == GONE) {
                    closeImageView.setVisibility(VISIBLE);
                }
                Glide.with(context)
                        .load(imageInfo.getImageFile())
                        .asBitmap()
                        .override(500, 500)
                        .centerCrop()
                        .into(imageView);
            }else {
                closeImageView.setVisibility(GONE);
                imageView.setImageResource(R.drawable.ic_plus);
                imageView.setBackground(getResources().getDrawable(R.color.colorVeryLightGray));
            }
        }
    }

    public interface OnViewClickListener {
        void onClick(View v);
    }

}
