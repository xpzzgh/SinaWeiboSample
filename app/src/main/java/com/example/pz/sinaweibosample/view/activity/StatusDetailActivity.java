package com.example.pz.sinaweibosample.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.ActivityManager;
import com.example.pz.sinaweibosample.base.BaseActivity;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.presenter.StatusDetailPresenter;
import com.example.pz.sinaweibosample.util.Util;
import com.example.pz.sinaweibosample.view.adapter.StatusDetailViewPagerAdapter;
import com.example.pz.sinaweibosample.view.fragment.CommentListFragment;
import com.example.pz.sinaweibosample.view.fragment.RelayListFragment;
import com.example.pz.sinaweibosample.view.iview.IStatusDetailView;
import com.example.pz.sinaweibosample.view.widget.BottomOperateTabView;
import com.example.pz.sinaweibosample.view.widget.StatusView;

import butterknife.BindView;

/**
 * Created by pz on 2016/9/13.
 */

public class StatusDetailActivity extends BaseActivity<StatusDetailPresenter> implements IStatusDetailView{

    Status status;
    StatusDetailViewPagerAdapter viewPagerAdapter;
    ActionBar supportToolbar;
    CommentListFragment commentListFragment;
    RelayListFragment relayListFragment;


    @BindView(R.id.view_status_detail)
    StatusView statusView;
    @BindView(R.id.view_relay_comment_like_status_detail)
    BottomOperateTabView bottomOperateTabView;
    @BindView(R.id.view_pager_status_detail)
    ViewPager statusDetailViewPager;
    @BindView(R.id.view_tab_status_detail)
    TabLayout statusDetailTabView;
//    @BindView(R.id.refresh_status_detail)
//    SwipeRefreshLayout statusDetailRefresh;
    @BindView(R.id.view_toolbar_status_detail)
    Toolbar statusDetailToolbar;
    @BindView(R.id.view_appbar_status_detail)
    AppBarLayout statusDetailAppBar;
    @BindView(R.id.collapsing_status_detail)
    CollapsingToolbarLayout statusDetailCollapsing;
    @BindView(R.id.text_like_status_detail)
    TextView statusDetailLikeText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        status = (Status)getIntent().getSerializableExtra("status");
        super.onCreate(savedInstanceState);
//        initData();
    }

    @Override
    public void setTitle() {
//        supportToolbar.setTitle("微博正文");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_status_detail;
    }

    @Override
    protected void initPresenter() {
        presenter = new StatusDetailPresenter(this, this);
    }

    @Override
    public void fillStatusData(Status status) {
        this.status = status;
        setTabTitle();
        relayListFragment.initData();
        commentListFragment.initData();
//        viewPagerAdapter.notifyDataChanged();
    }

    @Override
    protected void initView() {
        initFragment();
        setupToolbar();
        statusView.setRelayCommentLikeViewGone();
        statusView.setData(status);
        initBottomOperateTabView();
        setupViewPager();
        statusDetailTabView.setupWithViewPager(statusDetailViewPager);
        setupTitleAppear();
        statusDetailLikeText.setText("点赞 " + (status.getAttitudes_count() == 0 ? "" : Util.bigNumToStr(status.getAttitudes_count())));
        initUpdateTitles();
//        statusDetailRefresh.setOnRefreshListener(this);
    }

    void initFragment() {
        commentListFragment = CommentListFragment.instanceOf(status);

        relayListFragment = RelayListFragment.instanceOf(status);
    }

    void initData() {
        presenter.fillStatusDetail(status.getId());
    }

    void setupToolbar() {
        //取消collapsingToolbar标题功能
        statusDetailCollapsing.setTitleEnabled(false);
        setSupportActionBar(statusDetailToolbar);
        supportToolbar = getSupportActionBar();
        supportToolbar.setDisplayHomeAsUpEnabled(false);
        supportToolbar.setDisplayShowHomeEnabled(false);
        supportToolbar.setTitle("");

    }

    private void initBottomOperateTabView() {
        bottomOperateTabView.setData(status);
        bottomOperateTabView.undoData();
    }

    private void setupViewPager() {
        viewPagerAdapter = new StatusDetailViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(relayListFragment, commentListFragment);
        viewPagerAdapter.setTitles("转发 " + (status.getReposts_count() == 0 ? "" : Util.bigNumToStr(status.getReposts_count())),
                "评论 " + (status.getComments_count() == 0 ? "" : Util.bigNumToStr(status.getComments_count())));
        statusDetailViewPager.setAdapter(viewPagerAdapter);
        statusDetailViewPager.setCurrentItem(1);
    }

    private void initUpdateTitles() {
        commentListFragment.setOnDataFinishedListener(new CommentListFragment.OnDataFinishedListener() {
            @Override
            public void onFinished(int count) {
                viewPagerAdapter.setCommentTitle("评论 " + (count == 0 ? "" : Util.bigNumToStr(count)));
            }
        });
        relayListFragment.setOnDataFinishedListener(new RelayListFragment.OnDataFinishedListener() {
            @Override
            public void onFinished(int count) {
                viewPagerAdapter.setRelayTitle("转发 " + (count == 0 ? "" : Util.bigNumToStr(count)));
            }
        });
    }

    /**
     * 更新tabLayout的title，即微博的评论、转发、更新数目
     */
    private void setTabTitle() {
        if(viewPagerAdapter != null) {
            String relayCount = "转发 " + (status.getReposts_count() == 0 ? "" : Util.bigNumToStr(status.getReposts_count()));
            String commentCount = "评论 " + (status.getComments_count() == 0 ? "" : Util.bigNumToStr(status.getComments_count()));
            viewPagerAdapter.setTitles(relayCount, commentCount);
        }
        statusDetailLikeText.setText("点赞 " + (status.getAttitudes_count() == 0 ? "" : Util.bigNumToStr(status.getAttitudes_count())));
    }

    /**
     * 设置页面title显示的情况
     */
    void setupTitleAppear() {
//        statusDetailCollapsing.setTitle("");
        statusDetailAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = false;
            //appbar总共要滚动的高度，是appbar的高度
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (scrollRange + verticalOffset <= 20) {
                    supportToolbar.setDisplayHomeAsUpEnabled(true);
                    supportToolbar.setDisplayShowHomeEnabled(true);
//                    statusDetailCollapsing.setTitle("微博正文");
                    supportToolbar.setTitle("微博正文");
                    isShow = true;
                } else if(isShow) {
//                    statusDetailCollapsing.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                    supportToolbar.setTitle("");
                    supportToolbar.setDisplayHomeAsUpEnabled(false);
                    supportToolbar.setDisplayShowHomeEnabled(false);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        ActivityManager.instanceOf().finishActivity(this);
        return true;
    }

    @Override
    public void hideProgress() {
//        if(statusDetailRefresh.isRefreshing()) {
//            statusDetailRefresh.setRefreshing(false);
//        }
    }

    @Override
    public void showProgress() {
//        if(!statusDetailRefresh.isRefreshing()) {
//            statusDetailRefresh.setRefreshing(true);
//        }
    }



    @Override
    public void showSnackInfo(String errorString, int errorCode) {

    }

    @Override
    protected void saveCurrentState(Bundle bundle) {

    }

    @Override
    protected void pauseOperation() {

    }

    @Override
    protected void stopOperation() {

    }

    @Override
    protected void destroyOperation() {

    }

    @Override
    public void onBackPressed() {
        ActivityManager.instanceOf().finishActivity(this);
    }
}
