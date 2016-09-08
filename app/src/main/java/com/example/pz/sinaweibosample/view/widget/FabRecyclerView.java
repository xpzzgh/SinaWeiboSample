package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.example.pz.sinaweibosample.util.MyLog;

/**
 * 到下面可以加载更多的recyclerView
 * Created by pz on 2016/9/7.
 */

public class FabRecyclerView extends RecyclerView {

    FloatingActionButton fab;
    boolean isScrollToBottom;   //翻到下面
//    OnLoadMoreListener listener;

    public FabRecyclerView(Context context) {
        super(context);
    }

    public FabRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FabRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
//        this.listener = loadMoreListener;
//    }

    public void bindFloatButton(FloatingActionButton fab) {
        this.fab = fab;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        if(dy < 0) {
            //往上翻的时候显示
            isScrollToBottom = false;
            if(fab != null && !fab.isShown()) {
                fab.show();
            }
        }else {
            //往下翻的时候隐藏
            isScrollToBottom = true;
            if(fab != null && fab.isShown()) {
                fab.hide();
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
