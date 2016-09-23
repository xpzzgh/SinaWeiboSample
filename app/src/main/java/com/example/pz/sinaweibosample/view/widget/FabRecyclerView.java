package com.example.pz.sinaweibosample.view.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.example.pz.sinaweibosample.util.MyLog;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * 到下面可以加载更多的recyclerView
 * Created by pz on 2016/9/7.
 */

public class FabRecyclerView extends RecyclerView {

    FloatingActionsMenu fabMenu;
    boolean isScrollToBottom;   //翻到下面
    ObjectAnimator objectAnimator;
    float width;
    Context context;
//    OnLoadMoreListener listener;

    public FabRecyclerView(Context context) {
        super(context);
    }

    public FabRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FabRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        objectAnimator = new ObjectAnimator();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    //    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
//        this.listener = loadMoreListener;
//    }

    public void bindFloatButton(FloatingActionsMenu fabMenu) {
        this.fabMenu = fabMenu;
        width = fabMenu.getMeasuredWidth();
    }

    @Override
    public void onScrolled(int dx, int dy) {
        if(fabMenu.isExpanded()) {
            fabMenu.toggle();
        }
        if(dy == 0) {
            return;
        }
//        fabMenu.animate().cancel();
//        fabMenu.animate().setListener(null);
        if(dy < 0) {
            //往上翻的时候显示
            if(fabMenu != null && fabMenu.getVisibility() == GONE) {
                MyLog.v(MyLog.WIDGET_TAG, "fab没有显示，该显示了！！！");
                AnimatorSet set = new AnimatorSet();
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fabMenu, "scaleX", 0f, 1f);
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(fabMenu, "scaleY", 0f, 1f);
                set.play(objectAnimator).with(objectAnimator1);
                set.setDuration(100);
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        fabMenu.setVisibility(VISIBLE);
                    }
                });
                set.start();
//                fabMenu.animate()
//                        .alpha(1f)
//                        .setDuration(2000)
//                        .setListener(new AnimatorListenerAdapter() {
//                            private boolean mCancelled;
//                            @Override
//                            public void onAnimationStart(Animator animation) {
//                                fabMenu.setVisibility(VISIBLE);
//                                fabMenu.setAlpha(0f);
//                                MyLog.v(MyLog.WIDGET_TAG, "fab动画开始，设置为VISIBLE");
//                                mCancelled = false;
//                            }
//
//                            @Override
//                            public void onAnimationCancel(Animator animation) {
//                                mCancelled = true;
//                            }
//                        })
//                        .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animation) {
//                                MyLog.v(MyLog.WIDGET_TAG, "fab动画透明度：" + animation.getAnimatedValue() );
//                            }
//                        });
//                    fabMenu.setVisibility(VISIBLE);
            }
        }else {
            //往下翻的时候隐藏
            isScrollToBottom = true;

            if(fabMenu != null && fabMenu.getVisibility() == VISIBLE) {
                AnimatorSet set = new AnimatorSet();
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fabMenu, "scaleX", 1f, 0f);
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(fabMenu, "scaleY", 1f, 0f);
                set.play(objectAnimator).with(objectAnimator1);
                set.setDuration(100);
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(fabMenu.getScaleX() < 0.1) {
                            fabMenu.setVisibility(GONE);
                        }
                    }
                });
                set.start();
//                fabMenu.setAlpha(1f);
//                fabMenu.animate()
//                        .alpha(0f)
//                        .setDuration(2000)
//                        .setListener(new AnimatorListenerAdapter() {
//                            private boolean mCancelled;
//
//                            @Override
//                            public void onAnimationStart(Animator animation) {
//                                fabMenu.setVisibility(VISIBLE);
//                                mCancelled = false;
//                            }
//
//                            @Override
//                            public void onAnimationCancel(Animator animation) {
//                                mCancelled = true;
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                if(!mCancelled) {
//                                    fabMenu.setVisibility(GONE);
//                                    MyLog.v(MyLog.WIDGET_TAG, "fab动画结束，设置为GONE");
//
//                                }
//                            }
//                        });
            }
        }
    }
//
//    @Override
//    public void onScrollStateChanged(int state) {
////        getAdapter();
//        super.onScrollStateChanged(state);
//        if(isScrollToBottom && state == RecyclerView.SCROLL_STATE_IDLE) {
//            LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
//            int lastItem = layoutManager.findLastVisibleItemPosition();
//            int itemCount = layoutManager.getItemCount();
//            if(itemCount>0 && lastItem == (itemCount-1)) {
//                if(listener != null) {
//                    listener.loadMore();
//                }
//            }
//        }
//    }
//
//    public interface OnLoadMoreListener {
//        void loadMore();
//    }
}
