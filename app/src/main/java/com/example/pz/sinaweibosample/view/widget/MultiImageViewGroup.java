package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.ActivityManager;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.util.Util;
import com.example.pz.sinaweibosample.view.activity.ImageActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pz on 2016/9/3.
 */

public class MultiImageViewGroup extends ViewGroup {

    /**
     * 多图模式下，每张小图的宽度，pixel
     */
    private int widthPerImage;
    /**
     * 多图模式下，小图之间间隔距离，pixel
     */
    private int widthImageGap;
    /**
     * 单图模式下，自定义的最大宽度，当宽度超过该宽度时，图片宽度将设置为此值
     */
    private int widthMax;

    /**
     * 定义的固定高度，单图模式下显示的图片高度
     */
    private int fixedHeight;

    Context context;

    ControllerListener listener;

    Status status;

    List<String> imageList;
    ArrayList<String> bigImageList;

    public MultiImageViewGroup(Context context) {
        this(context, null, 0);
    }

    public MultiImageViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiImageViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MultiImageViewGroup, 0, 0);
        try {
            //获取定义的值，单位是pixel
            widthPerImage = a.getDimensionPixelSize(R.styleable.MultiImageViewGroup_width_per_image, Util.dpToPx(80, context));
            widthImageGap = a.getDimensionPixelSize(R.styleable.MultiImageViewGroup_width_image_gap, Util.dpToPx(10, context));
            widthMax = a.getDimensionPixelSize(R.styleable.MultiImageViewGroup_width_max, Util.dpToPx(230, context));
            fixedHeight = a.getDimensionPixelSize(R.styleable.MultiImageViewGroup_height_image_fixed, Util.dpToPx(150, context));
        }finally {
            //回收TypedArray
            a.recycle();
        }
    }

    private void updateViewSize(@Nullable Object imageInfo, SimpleDraweeView singleImage) {
        if (imageInfo != null && singleImage != null) {
            if(imageInfo instanceof ImageInfo) {
                ImageInfo info = (ImageInfo)imageInfo;
                singleImage.getLayoutParams().height = fixedHeight;
                singleImage.setAspectRatio((float) info.getWidth() / info.getHeight());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        MyLog.v(MyLog.STATUS_VIEW_TAG, "图片控件测量！！");
        int count = getChildCount(); //所有子view数目
        int maxWidth = getPaddingLeft() + getPaddingRight();  //初始宽度为左右两个padding
        int maxHeight = getPaddingTop() + getPaddingBottom();
        List<View> realViewList = new ArrayList<View>();  //真实显示的子view
        int childState = 0;

        //显示的子view数量
        int realViewCount = 0;

        for(int i = 0; i<count; i++) {
            final View view = getChildAt(i);
            if(view.getVisibility() == GONE) {
                continue;
            }
            realViewCount++;
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            realViewList.add(view);
        }

        /**
         * 当只有一个字view时，显示大图
         */
        if(realViewCount == 0) {
            setMeasuredDimension(0,0);
            return;
        }else if(realViewCount == 1) {
            final View view = realViewList.get(0);
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = fixedHeight;
            //当宽度大于定义的最大宽度时，将宽度设为定义的最大宽度
            if(widthMax < measuredWidth) {
                measuredWidth = widthMax;
            }
            maxWidth += measuredWidth;
            maxHeight += measuredHeight;
            childState = combineMeasuredStates(childState, view.getMeasuredState());
            maxWidth = Math.max(maxWidth, getSuggestedMinimumHeight());
            maxHeight = Math.max(maxHeight, getSuggestedMinimumWidth());
            //设置测量结果
            setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                    resolveSizeAndState(maxHeight, heightMeasureSpec, childState));
        }else{  //多图时，宽高按照多图模式定义的小图宽高直接计算
            int rowCount;
            if(realViewCount % 3 == 0) {
                rowCount = realViewCount / 3;
            }else {
                rowCount = realViewCount / 3 + 1;
            }

            maxHeight += widthPerImage * rowCount + widthImageGap * (rowCount - 1);
            if(realViewCount == 2 || realViewCount == 4) {
                maxWidth += widthPerImage * 2 + widthImageGap;
            }else {
                maxWidth += widthPerImage * 3 + widthImageGap * 2;
            }
            setMeasuredDimension(maxWidth, maxHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        MyLog.v(MyLog.STATUS_VIEW_TAG, "图片控件布局！！" + "一共有" + getChildCount() + "张图片");
        int count = getChildCount();
        List<View> realViewList = new ArrayList<View>();
        //占位子的子view数量
        int realViewCount = 0;

        //获取真实显示的子view
        for(int i = 0; i<count; i++) {
            final View view = getChildAt(i);
            if(view.getVisibility() == GONE) {
                continue;
            }
            realViewList.add(view);
            realViewCount++;
        }

        //把子view的范围圈出来
        final int scopeLeft = getPaddingLeft();
        final int scopeTop = getPaddingTop();
        final int scopeRight = getMeasuredWidth() - getPaddingRight();
        final int scopeBottom = getMeasuredHeight() - getPaddingBottom();

        if (realViewCount == 1) {
            realViewList.get(0).layout(scopeLeft, scopeTop, scopeRight, scopeBottom);
        }else if(realViewCount == 2 || realViewCount == 4) {
            int currentLeft = scopeLeft;
            int currentTop = scopeTop;
            for(int i = 0; i<realViewCount; i++) {
                realViewList.get(i).layout(currentLeft, currentTop, currentLeft+widthPerImage, currentTop+widthPerImage);
                if(i == 1) {
                    currentLeft = scopeLeft;
                    currentTop = scopeTop + widthPerImage + widthImageGap;
                }else {
                    currentLeft = currentLeft + widthPerImage + widthImageGap;
                }
            }
        }else {
            int currentLeft = scopeLeft;
            int currentTop = scopeTop;
            for(int i = 0; i < realViewCount; i++) {
                realViewList.get(i).layout(currentLeft, currentTop, currentLeft+widthPerImage, currentTop+widthPerImage);
                if(i%3 == 2) {
                    currentLeft = scopeLeft;
                    currentTop = currentTop + widthPerImage + widthImageGap;
                }else {
                    currentLeft = currentLeft + widthPerImage + widthImageGap;
                }
            }
        }
    }

    public void setData(Status status) {
        this.status = status;
        imageList = Util.getPriorityImagesUris(this.status, Constant.SMALL_IMAGE);
        bigImageList = (ArrayList<String>) Util.getPriorityImagesUris(this.status, Constant.MEDIUM_IMAGE);
        if(imageList.size() == 1) {
            imageList = Util.getPriorityImagesUris(this.status, Constant.MEDIUM_IMAGE);
        }
//        List<String> imageList
//        MyLog.v(MyLog.STATUS_VIEW_TAG, "为图片控件设置数据，一共有" + imageList.size() + "张图片！");
        if(getChildCount() != 0) {
            removeAllViews();
        }
        final int imageSize = imageList.size();

        for(int i = 0; i<imageSize; i++) {
            String imageUri = imageList.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.image_single, null, false);
            final SimpleDraweeView singleImage = (SimpleDraweeView)view.findViewById(R.id.imageView_simple);

            listener = new BaseControllerListener() {

                @Override
                public void onIntermediateImageSet(String id, Object imageInfo) {
                    super.onIntermediateImageSet(id, imageInfo);
                }

                @Override
                public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                    if(imageSize > 1) {
                        updateViewSize(imageInfo, singleImage);
                    }
                }
            };

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(singleImage.getController())
                    .setUri(imageUri)
                    .setTapToRetryEnabled(true)
                    .setControllerListener(listener).build();

            singleImage.setController(controller);
            singleImage.setMinimumWidth(Util.dpToPx((int)(getResources().getDimension(R.dimen.width_image_place_little)), context));
            singleImage.setTag(i);
            singleImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ImageActivity.class);
                    intent.putExtra("current_item", (int)view.getTag());
                    intent.putStringArrayListExtra("url_list", bigImageList);
                    intent.putStringArrayListExtra("url_small_list", (ArrayList<String>) imageList);
                    String transitionName = context.getString(R.string.transition_image_big);
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(ActivityManager.instanceOf().getCurrentActivity())
                            .toBundle();
//                    .makeSceneTransitionAnimation(ActivityManager.instanceOf().getCurrentActivity(), view, transitionName)
                    context.startActivity(intent, bundle);
                    Toast.makeText(context, "点击了大图地址为" + view.getTag() + "的图片！", Toast.LENGTH_LONG).show();
                }
            });
            addView(singleImage);
        }

//        int childCount = getChildCount();
//        if(childCount > 0) {
//            for(int i = 0; i<childCount; i++) {
//                SimpleDraweeView image = (SimpleDraweeView) getChildAt(i);
//                image.setOnClickListener(new ImageOnClickListener(i));
//            }
//        }

        MyLog.v(MyLog.STATUS_VIEW_TAG, "图片控件一共有：" + getChildCount() + "张图片");
    }

    public class ImageOnClickListener implements OnClickListener {

        int i;

        public ImageOnClickListener(int i) {
            this.i = i;
        }

        @Override
        public void onClick(View view) {
            status.getAttitudes_count();
            Toast.makeText(context, "点击了第" + i + "张图片！", Toast.LENGTH_LONG).show();

        }
    }
}






























