package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.util.Util;

/**
 * Created by pz on 2016/9/20.
 */

public class ImageViewWithTag extends ImageView {

    boolean tagEnable;

    int tagWidth;

    int tagHeight;

    String tagText;

    int tagTextSize;

    int tagTextColor;

    int tagColor;

    Paint mPaint;

    Path mPath;

    TextPaint mTextPaint;

    Rect mTextBound;

    Context context;

    public ImageViewWithTag(Context context) {
        this(context, null, 0);
    }

    public ImageViewWithTag(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewWithTag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageViewWithTag, 0, 0);
        try{
            tagEnable = a.getBoolean(R.styleable.ImageViewWithTag_tagEnable, false);
            tagWidth = a.getDimensionPixelSize(R.styleable.ImageViewWithTag_tagWidth, Util.dpToPx(35, context));
            tagHeight = a.getDimensionPixelSize(R.styleable.ImageViewWithTag_tagHeight, Util.dpToPx(20, context));
            tagText = a.getString(R.styleable.ImageViewWithTag_tagText);
            if(tagText == null) {
                tagText = "";
            }
            tagTextSize = a.getDimensionPixelSize(R.styleable.ImageViewWithTag_tagTextSize, Util.spToPx(15, context));
            tagTextColor = a.getColor(R.styleable.ImageViewWithTag_tagTextColor, getResources().getColor(R.color.colorAccent));
            tagColor = a.getColor(R.styleable.ImageViewWithTag_tagColor, getResources().getColor(R.color.colorPrimary));
        }finally {
            a.recycle();
        }
        mPaint = new Paint();
        mPath = new Path();
        mTextPaint = new TextPaint();
        mTextBound = new Rect();
    }

    public void setTagEnable(boolean tagEnable) {
        this.tagEnable = tagEnable;
        if(tagText != null && !tagText.isEmpty()) {
            invalidate();
        }
    }

    public void setTagHeight(int tagHeightDp) {
        int tagHeightPx = Util.dpToPx(tagHeightDp, context);
        if(tagHeightPx != 0 && tagHeightPx != tagHeight && tagEnable) {
            tagHeight = tagHeightPx;
            invalidate();
        }
    }

    public void setTagText(String tagText) {
        if(tagText != null && !tagText.isEmpty() && !tagText.equals(this.tagText) && tagEnable) {
            this.tagText = tagText;
            invalidate();
        }
    }

    public void setTagWidth(int tagWidthDp) {
        int tagWidthPx = Util.dpToPx(tagWidthDp, context);
        if(tagWidthPx != 0 && tagWidth != tagWidthPx && tagEnable) {
            tagWidth = tagWidthPx;
            invalidate();
        }
    }

    public void setTagColor(int tagColor) {
        if(tagColor != 0 && tagColor != this.tagColor && tagEnable) {
            this.tagColor = tagColor;
            invalidate();
        }
    }

    public void setTagTextColor(int tagTextColor) {
        if(tagTextColor != 0 && tagTextColor != this.tagTextColor && tagEnable) {
            this.tagTextColor = tagTextColor;
            invalidate();
        }
    }

    public void setTagTextSize(int tagTextSize) {
        if(tagTextSize != 0 && tagTextSize != this.tagTextSize && tagEnable) {
            this.tagTextSize = tagTextSize;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(tagEnable) {
            drawTag(canvas);
        }
    }

    /**
     * 根据条件画tag
     * @param canvas
     */
    public void drawTag(Canvas canvas) {
        //初始化mPaint
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(tagColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(tagHeight);
        //初始化path
        mPath.reset();
        mPath.moveTo(getWidth() - tagWidth, getHeight() - tagHeight/2);
        mPath.lineTo(getWidth(), getHeight() - tagHeight/2);
        canvas.drawPath(mPath, mPaint);

        mTextPaint.setColor(tagTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(tagTextSize);
        mTextPaint.getTextBounds(tagText, 0, tagText.length(), mTextBound);
        canvas.drawTextOnPath(tagText, mPath, tagWidth/2 - mTextBound.width()/2, mTextBound.height()/2, mTextPaint);
    }
}
