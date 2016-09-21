package com.example.pz.sinaweibosample.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.BaseFragment;
import com.example.pz.sinaweibosample.http.status.StatusParamsHelper;
import com.example.pz.sinaweibosample.model.entity.Comment;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.presenter.CommentListPresenter;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.view.adapter.CommentListAdapter;
import com.example.pz.sinaweibosample.view.iview.ICommentListView;
import com.example.pz.sinaweibosample.view.widget.LoadMoreRecyclerView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pz on 2016/9/13.
 */

public class CommentListFragment extends BaseFragment<CommentListPresenter> implements ICommentListView,
        LoadMoreRecyclerView.LoadMoreListener, SwipeRefreshLayout.OnRefreshListener{

    View view;
    Context context;
    List<Comment> comments;
    Status status;
    OnDataFinishedListener dataFinishedListener;
    int noMoreTime = 0;

    CommentListAdapter commentListAdapter;

    int page = 1;
    boolean isRefresh = true;

    @BindView(R.id.recycler_comment_list)
    LoadMoreRecyclerView commentListRecycler;
    @BindView(R.id.refresh_list_comment)
    SwipeRefreshLayout commentListRefresh;
//    @BindView(R.id.text_no_content)
//    TextView noContentText;

    public static CommentListFragment instanceOf(Status status) {
        CommentListFragment commentListFragment = new CommentListFragment();
        commentListFragment.setStatus(status);
        return commentListFragment;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        initView();
        initPresenter();
        initData();
        return view;
    }

    @Override
    public void initView() {
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        setupCommentRecycler();
        commentListRefresh.setOnRefreshListener(this);
//        noContentText.setMovementMethod(new ScrollingMovementMethod());
    }

    private void setupCommentRecycler() {
        comments = new ArrayList<Comment>();
        commentListRecycler.setLayoutManager(new LinearLayoutManager(context));
        commentListAdapter = new CommentListAdapter(context, comments);
        commentListRecycler.setAdapter(commentListAdapter);
        commentListRecycler.setLoadMoreListener(this);
    }

    @Override
    public void loadMore() {
        //处理加载更多的逻辑
        isRefresh = false;
        page++;
        fragmentPresenter.fillCommentListData(status.getId(), page);
    }

    @Override
    public void onRefresh() {
        //处理刷新的逻辑
        isRefresh = true;
        page = 1;
        fragmentPresenter.fillCommentListData(status.getId(), page);
    }

    public void setOnDataFinishedListener(OnDataFinishedListener dataFinishedListener) {
        this.dataFinishedListener = dataFinishedListener;
    }

    @Override
    public void initPresenter() {
        fragmentPresenter = new CommentListPresenter(context, this);
    }

    public void initData() {
        isRefresh = true;
        fragmentPresenter.fillCommentListData(status.getId(), page);
    }

    @Override
    public void fillData(List<Comment> commentList) {
        if(isRefresh) {
            comments.clear();
            comments.addAll(commentList);
        }else {
            comments.addAll(commentList);
        }
        commentListAdapter.notifyDataSetChanged();
        //如果请求到的数目小于请求的数目10条，那么去掉加载更多的监听
        if(commentList.size() < StatusParamsHelper.COMMENT_COUNT_PER_REQUEST - 10) {
            commentListRecycler.setLoadMoreListener(null);
        }else {
            commentListRecycler.setLoadMoreListener(this);
        }
    }

    @Override
    public void updateTabTitle(int count) {
        if(dataFinishedListener != null) {
            dataFinishedListener.onFinished(count);
        }
    }

    @Override
    public void hideProgress() {
        if(commentListRefresh.isRefreshing()) {
            commentListRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showProgress() {
        if(!commentListRefresh.isRefreshing()) {
            commentListRefresh.setRefreshing(true);
        }
    }

    @Override
    public void showSnackInfo(String errorString, int errorCode) {
        hideProgress();
        Toast.makeText(context, errorString, Toast.LENGTH_LONG).show();
        if(page > 1) {
            page--;
        }
    }

    public interface OnDataFinishedListener {
        void onFinished(int count);
    }
}
