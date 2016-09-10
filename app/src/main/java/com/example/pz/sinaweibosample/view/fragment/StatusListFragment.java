package com.example.pz.sinaweibosample.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.BaseFragment;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.presenter.StatusListPresenter;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.view.adapter.StatusListAdapter;
import com.example.pz.sinaweibosample.view.decoration.SimpleDecoration;
import com.example.pz.sinaweibosample.view.iview.IStatusListView;
import com.example.pz.sinaweibosample.view.widget.FabRecyclerView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pz on 2016/9/9.
 */

public class StatusListFragment extends BaseFragment<StatusListPresenter> implements IStatusListView,
        SwipyRefreshLayout.OnRefreshListener{

    View view;
    StatusListAdapter statusListAdapter;
    List<Status> statusList;
    Context context;
    int page;
    boolean isRefresh;
    int type;

    @BindView(R.id.refresh_status_list)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.recycler_status_list)
    FabRecyclerView recyclerView;

    FloatingActionButton fab;

    public static StatusListFragment instanceOf(int fragmentType) {
        StatusListFragment statusListFragment = new StatusListFragment();
        statusListFragment.setType(fragmentType);
        return statusListFragment;
    }

    private void setType(int type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_status_list, container, false);
        initPresenter();
        init();
        return view;
    }

    @Override
    public void init() {
        page = 1;
        context = getActivity();
        statusList = new ArrayList<Status>();
        unbinder = ButterKnife.bind(this, view);
        //recycler的初始化
        statusListAdapter = new StatusListAdapter(context, statusList);
        recyclerView.setAdapter(statusListAdapter);
        recyclerView.addItemDecoration(new SimpleDecoration(context, R.drawable.divider_status, SimpleDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        fab = ((com.example.pz.sinaweibosample.view.activity.MainActivity)context).getFab();
        recyclerView.bindFloatButton(fab);
        //设置刷新操作
        refreshLayout.setOnRefreshListener(this);
        fragmentPresenter.fillStatusList(1, type);
    }

    @Override
    public void initPresenter() {
        fragmentPresenter = new StatusListPresenter(getActivity(), this);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if(direction == SwipyRefreshLayoutDirection.BOTTOM) {
            page++;
            isRefresh = false;
        }else {
            page = 1;
            isRefresh = true;
        }
        fragmentPresenter.fillStatusList(page, type);
    }

    @Override
    public void fillDataList(List<Status> newStatusList) {
        if(isRefresh) {
            statusList.clear();
            statusList.addAll(newStatusList);
        }else {
            statusList.addAll(newStatusList);
        }
        statusListAdapter.notifyDataSetChanged();
    }

    @Override
    public void hideProgress() {
        if(refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showSnackInfo(String infoString, final int code) {
        hideProgress();
        page--;
        Snackbar snackbar = Snackbar.make(fab, infoString, Snackbar.LENGTH_LONG);
        TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        if(code == Constant.NO_MORE_CODE) {
            tv.setTextColor(getResources().getColor(R.color.colorWhite));
        }else if(code == Constant.ERROR_CODE) {
            tv.setTextColor(getResources().getColor(R.color.colorRed));
        }
        snackbar.setAction("点击重试", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page++;
                fragmentPresenter.fillStatusList(page, type);
            }
        }).show();
    }

}
