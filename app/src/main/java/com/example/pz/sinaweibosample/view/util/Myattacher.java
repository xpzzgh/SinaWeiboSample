package com.example.pz.sinaweibosample.view.util;

import android.view.View;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by pz on 2016/9/21.
 */

public class Myattacher extends PhotoViewAttacher {

    public Myattacher(ImageView imageView) {
        super(imageView);
    }

    public Myattacher(ImageView imageView, boolean zoomable) {
        super(imageView, zoomable);
    }

    @Override
    public void setOnLongClickListener(View.OnLongClickListener listener) {
        super.setOnLongClickListener(listener);
    }
}
