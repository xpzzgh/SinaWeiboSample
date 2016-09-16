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
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.presenter.RelayListPresenter;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.view.adapter.RelayListAdapter;
import com.example.pz.sinaweibosample.view.iview.IRelayListView;
import com.example.pz.sinaweibosample.view.widget.LoadMoreRecyclerView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pz on 2016/9/15.
 */

public class RelayListFragment extends BaseFragment<RelayListPresenter> implements IRelayListView,
        SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView.LoadMoreListener{

    List<Status> relays;
    View view;
    Context context;
    RelayListAdapter relayListAdapter;
    Status status;
    OnDataFinishedListener dataFinishedListener;

    int page = 1;
    boolean isRefresh;

    @BindView(R.id.recycler_comment_list)
    LoadMoreRecyclerView relayListRecycler;
    @BindView(R.id.refresh_list_comment)
    SwipeRefreshLayout relayListRefresh;
//    @BindView(R.id.text_no_content)
//    TextView noContentText;

    public static RelayListFragment instanceOf(Status status) {
        RelayListFragment relayListFragment = new RelayListFragment();
        relayListFragment.setStatus(status);
        return relayListFragment;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        initPresenter();
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        setupRelayRecycler();
        relayListRefresh.setOnRefreshListener(this);
//        noContentText.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onRefresh() {
        //处理刷新的逻辑
        isRefresh = true;
        page = 1;
        fragmentPresenter.fillRelayList(status.getId(), page);
    }

    @Override
    public void loadMore() {
        //处理加载更多的逻辑
        isRefresh = false;
        page++;
        fragmentPresenter.fillRelayList(status.getId(), page);
    }

    public void initData() {
        isRefresh = true;
        fragmentPresenter.fillRelayList(status.getId(), page);
    }


    private void setupRelayRecycler() {
        relays = new ArrayList<Status>();
        relayListRecycler.setLayoutManager(new LinearLayoutManager(context));
        relayListAdapter = new RelayListAdapter(context, relays);
        relayListRecycler.setAdapter(relayListAdapter);
        relayListRecycler.setLoadMoreListener(this);
    }


    @Override
    public void initPresenter() {
        fragmentPresenter = new RelayListPresenter(context, this);
    }

    @Override
    public void fillData(List<Status> relayList) {
        relayListRecycler.setVisibility(View.VISIBLE);
        if(isRefresh) {
            relays.clear();
            relays.addAll(relayList);
        }else {
            relays.addAll(relayList);
        }
        relayListAdapter.notifyDataSetChanged();
    }

    public void setOnDataFinishedListener(RelayListFragment.OnDataFinishedListener dataFinishedListener) {
        this.dataFinishedListener = dataFinishedListener;
    }

    @Override
    public void updateTabTitle(int count) {
        if(dataFinishedListener != null) {
            dataFinishedListener.onFinished(count);
        }
    }

    @Override
    public void showProgress() {
        if(!relayListRefresh.isRefreshing() && !isRefresh) {
            relayListRefresh.setRefreshing(true);
        }
    }

    @Override
    public void hideProgress() {
        if(relayListRefresh.isRefreshing()) {
            relayListRefresh.setRefreshing(false);
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
