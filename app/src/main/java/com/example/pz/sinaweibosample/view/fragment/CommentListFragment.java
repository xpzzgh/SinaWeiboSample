package com.example.pz.sinaweibosample.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.BaseFragment;
import com.example.pz.sinaweibosample.model.entity.MyKeyValue;
import com.example.pz.sinaweibosample.presenter.CommentListPresenter;
import com.example.pz.sinaweibosample.view.adapter.CommentListAdapter;
import com.example.pz.sinaweibosample.view.iview.ICommentListView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pz on 2016/9/13.
 */

public class CommentListFragment extends BaseFragment<CommentListPresenter> implements ICommentListView{

    View view;
    Context context;
    List<MyKeyValue> datas;

    @BindView(R.id.recycler_comment_list)
    RecyclerView commentListRecycler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        initPresenter();
        init();
        return view;
    }

    @Override
    public void init() {
        initData();
        context = (Context) getActivity();
        unbinder = ButterKnife.bind(this, view);
        commentListRecycler.setLayoutManager(new LinearLayoutManager(context));
        commentListRecycler.setAdapter(new CommentListAdapter(context, datas));
    }

    @Override
    public void initPresenter() {
        fragmentPresenter = new CommentListPresenter(context, this);
    }

    private void initData() {
        datas = new ArrayList<MyKeyValue>();
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));
        datas.add(new MyKeyValue("asd", "asdasfd"));

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void showSnackInfo(String errorString, int errorCode) {

    }

    public boolean canScrollVertically(int direction) {
        return view != null && view.canScrollVertically(direction);
    }
}
