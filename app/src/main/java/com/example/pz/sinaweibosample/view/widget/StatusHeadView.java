package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.example.pz.sinaweibosample.model.entity.Status;

/**
 * Created by pz on 2016/9/12.
 */

public class StatusHeadView extends ViewGroup {

    Context context;

    public StatusHeadView(Context context) {
        this(context, null, 0);
    }

    public StatusHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }



    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    public void setData(Status status) {

    }
}
