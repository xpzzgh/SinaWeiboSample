package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.example.pz.sinaweibosample.util.MyLog;

/**
 * 到下面可以加载更多的recyclerView
 * Created by pz on 2016/9/7.
 */

public class LoadMoreRecyclerView extends RecyclerView {

    boolean isScrollToBottom;   //翻到下面
    OnLoadMoreListener listener;

    public LoadMoreRecyclerView(Context context) {
        super(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.listener = loadMoreListener;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        if(dy < 0) {
            //往上翻的时候显示
            isScrollToBottom = false;
        }else {
            //往下翻的时候隐藏
            isScrollToBottom = true;
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        MyLog.v(MyLog.WIDGET_TAG, "列表状态改变！！");
        super.onScrollStateChanged(state);
        if(isScrollToBottom && state == RecyclerView.SCROLL_STATE_IDLE) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
            int lastItem = layoutManager.findLastVisibleItemPosition();
            MyLog.v(MyLog.WIDGET_TAG, "当前界面最下面的item的位置是：" + lastItem + ", 第一个是：" + layoutManager.findFirstVisibleItemPosition());
            int itemCount = layoutManager.getItemCount();
            MyLog.v(MyLog.WIDGET_TAG, "当前列表item总数是：" + itemCount);
            if(itemCount>0 && lastItem == (itemCount-1)) {
                MyLog.v(MyLog.WIDGET_TAG, "列表要加载更多了！！！");
                listener.loadMore();
            }
        }
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }
}
