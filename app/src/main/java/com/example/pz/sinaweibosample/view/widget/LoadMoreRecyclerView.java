package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.example.pz.sinaweibosample.util.MyLog;

/**
 * Created by pz on 2016/9/15.
 */

public class LoadMoreRecyclerView extends RecyclerView {

    FloatingActionButton actionButton;
    boolean isScrollToBottom;   //翻到下面
    LoadMoreListener listener;

    public LoadMoreRecyclerView(Context context) {
        this(context, null, 0);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.listener = loadMoreListener;
    }

    public void bindFloatButton(FloatingActionButton actionButton) {
        this.actionButton = actionButton;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        if(dy < 0) {
            //往上翻的时候显示
            isScrollToBottom = false;
            if(actionButton != null && !actionButton.isShown()) {
                actionButton.show();
            }
        }else {
            //往下翻的时候隐藏
            isScrollToBottom = true;
            if(actionButton != null && actionButton.isShown()) {
                actionButton.hide();
            }
        }
    }



    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if(isScrollToBottom && state == RecyclerView.SCROLL_STATE_IDLE) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
            int lastItem = layoutManager.findLastVisibleItemPosition();
            int itemCount = layoutManager.getItemCount();
            if(lastItem == (itemCount-1) && listener != null) {
                MyLog.v(MyLog.WIDGET_TAG, "要加载更多了！！！！");
                listener.loadMore();
            }
        }
    }

    public interface LoadMoreListener {
        public void loadMore();
    }
}
