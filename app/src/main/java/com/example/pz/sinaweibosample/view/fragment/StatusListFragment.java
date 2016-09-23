package com.example.pz.sinaweibosample.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.BaseFragment;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.oauth.AccessTokenKeeper;
import com.example.pz.sinaweibosample.presenter.StatusListPresenter;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.view.activity.StatusDetailActivity;
import com.example.pz.sinaweibosample.view.adapter.StatusListAdapter;
import com.example.pz.sinaweibosample.view.decoration.SimpleDecoration;
import com.example.pz.sinaweibosample.view.iview.IStatusListView;
import com.example.pz.sinaweibosample.view.widget.FabRecyclerView;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
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
        SwipyRefreshLayout.OnRefreshListener, StatusListAdapter.IViewHolderClick{

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

    FloatingActionsMenu fabMenu;

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
        initView();
        return view;
    }

    @Override
    public void initView() {
        page = 1;
        context = getActivity();
        statusList = new ArrayList<Status>();
        unbinder = ButterKnife.bind(this, view);
        //recycler的初始化
        statusListAdapter = new StatusListAdapter(context, statusList);
        statusListAdapter.setViewHolderClick(this);
        recyclerView.setAdapter(statusListAdapter);
        recyclerView.addItemDecoration(new SimpleDecoration(context, R.drawable.divider_status, SimpleDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        fabMenu = ((com.example.pz.sinaweibosample.view.activity.MainActivity)context).getFab();
        recyclerView.bindFloatButton(fabMenu);
        //设置刷新操作
        if(AccessTokenKeeper.isTokenValid()) {
            refreshLayout.setOnRefreshListener(this);
            fragmentPresenter.fillStatusList(1, type);
        }
    }

    public void bindOperateAfterLogin() {
        refreshLayout.setOnRefreshListener(this);
        fragmentPresenter.fillStatusList(1, type);
    }

    @Override
    public void initPresenter() {
        fragmentPresenter = new StatusListPresenter(getActivity(), this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(type != 0) {
            outState.putInt("type", type);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            type = savedInstanceState.getInt("type");
        }
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
    public void showProgress() {
        if(!refreshLayout.isRefreshing()) {
//            SwipyRefreshLayoutDirection direction = refreshLayout.getDirection();
//            refreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
            refreshLayout.setRefreshing(true);
//            refreshLayout.setDirection(direction);
        }
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
        Snackbar snackbar = Snackbar.make(fabMenu, infoString, Snackbar.LENGTH_LONG);
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

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getActivity(), "单击了第" + position + "条微博！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), StatusDetailActivity.class);
        intent.putExtra("status", statusList.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Toast.makeText(getActivity(), "长按了第" + position + "条微博！", Toast.LENGTH_SHORT).show();
    }
}
