package com.example.pz.sinaweibosample.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.ActivityManager;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.view.util.FitWidthScaleType;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pz on 2016/9/16.
 */

public class ImageFragment extends Fragment implements View.OnClickListener, View.OnTouchListener{

    View view;
    Unbinder unbinder;
    String imageUrl;
    String smallImageUrl;
    ControllerListener controllerListener;
    float dx = 0, dy = 0;

    @BindView(R.id.image_single_big)
    SimpleDraweeView image;

    public static ImageFragment instanceOf(String smallImageUrl, String imageUrl) {
        ImageFragment imageFragment = new ImageFragment();
        imageFragment.imageUrl = imageUrl;
        imageFragment.smallImageUrl = smallImageUrl;
        return imageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            imageUrl = savedInstanceState.getString("image_url");
            smallImageUrl = savedInstanceState.getString("small_image_url");
        }
        MyLog.v(MyLog.Image, "大图：createView！");
        view = inflater.inflate(R.layout.fragment_image_single_big, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    void init() {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy draweeHierarchy = builder
                .setFadeDuration(500)
                .setActualImageScaleType(new FitWidthScaleType())
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        controllerListener = new BaseControllerListener() {

        };
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setOldController(image.getController())
                .setControllerListener(controllerListener)
                .setLowResImageRequest(ImageRequest.fromUri(smallImageUrl))
                .setImageRequest(ImageRequest.fromUri(imageUrl))
                .build();
//        .setPlaceholderImage(R.mipmap.a6, ScalingUtils.ScaleType.FIT_CENTER)
//                .setFailureImage(R.mipmap.a5, ScalingUtils.ScaleType.FIT_CENTER)
        image.setHierarchy(draweeHierarchy);
        image.setController(draweeController);
        MyLog.v(MyLog.Image, "加载大图：" + imageUrl);
        image.setOnClickListener(this);
        image.setOnTouchListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_single_big:
                ActivityManager.instanceOf().finishActivity(getActivity());
        }
    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float currentX, currentY;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dx = motionEvent.getX();
                dy = motionEvent.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = motionEvent.getX();
                currentY = motionEvent.getY();
                image.scrollBy((int) (dx - currentX), (int) (dy - currentY));
                dx = currentX;
                dy = currentY;
                break;
            case MotionEvent.ACTION_UP:
                currentX = motionEvent.getX();
                currentY = motionEvent.getY();
                image.scrollBy((int) (dx - currentX), (int) (dy - currentY));
                break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("image_url", imageUrl);
        outState.putString("small_image_url", smallImageUrl);
    }

    @Override
    public void onDestroyView() {
        if(unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }
}
