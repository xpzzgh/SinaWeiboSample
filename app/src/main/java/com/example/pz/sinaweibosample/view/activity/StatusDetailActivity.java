package com.example.pz.sinaweibosample.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.BaseActivity;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.presenter.StatusDetailPresenter;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.view.adapter.StatusDetailViewPagerAdapter;
import com.example.pz.sinaweibosample.view.fragment.CommentListFragment;
import com.example.pz.sinaweibosample.view.iview.IStatusDetailView;
import com.example.pz.sinaweibosample.view.widget.BottomOperateTabView;
import com.example.pz.sinaweibosample.view.widget.StatusView;

import butterknife.BindView;
import ru.noties.scrollable.CanScrollVerticallyDelegate;
import ru.noties.scrollable.OnScrollChangedListener;
import ru.noties.scrollable.ScrollableLayout;

/**
 * Created by pz on 2016/9/13.
 */

public class StatusDetailActivity extends BaseActivity<StatusDetailPresenter> implements IStatusDetailView {

    Status status;
    StatusDetailViewPagerAdapter viewPagerAdapter;
    ActionBar supportToolbar;


    @BindView(R.id.view_status_detail)
    StatusView statusView;
    @BindView(R.id.view_relay_comment_like_status_detail)
    BottomOperateTabView bottomOperateTabView;
    @BindView(R.id.view_pager_status_detail)
    ViewPager statusDetailViewPager;
    @BindView(R.id.view_tab_status_detail)
    TabLayout statusDetailTabView;
//    @BindView(R.id.scrollable_status_detail)
//    ScrollableLayout statusDetailScrollable;
    @BindView(R.id.refresh_status_detail)
    SwipeRefreshLayout statusDetailRefresh;
    @BindView(R.id.view_toolbar_status_detail)
    Toolbar statusDetailToolbar;
    @BindView(R.id.view_appbar_status_detail)
    AppBarLayout statusDetailAppBar;
    @BindView(R.id.collapsing_status_detail)
    CollapsingToolbarLayout statusDetailCollapsing;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        status = (Status)getIntent().getSerializableExtra("status");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setTitle() {
//        supportToolbar.setTitle(status.getUser().getName() + "的微博");
        supportToolbar.setTitle("微博正文");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_status_detail1;
    }

    @Override
    protected void initPresenter() {
        presenter = new StatusDetailPresenter(this, this);
    }

    @Override
    protected void initView() {
        setupToolbar();
        statusView.setRelayCommentLikeViewGone();
        statusView.setData(status);
        initBottomOperateTabView();
        setupViewPager();
        statusDetailTabView.setupWithViewPager(statusDetailViewPager);
//        setupScrollable();
        setupTitleAppear();
//        statusDetailRefresh.setEnabled(false);

        statusDetailRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(10000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(statusDetailRefresh.isRefreshing()) {
                                        statusDetailRefresh.setRefreshing(false);
                                    }
                                }
                            });
                        }catch (Exception e) {

                        }
                        MyLog.v(MyLog.STATUS_DETAIL, "刷新完成！！");
                    }
                }).start();
            }
        });
    }

    void setupToolbar() {
        setSupportActionBar(statusDetailToolbar);
        supportToolbar = getSupportActionBar();
        supportToolbar.setDisplayHomeAsUpEnabled(true);
        supportToolbar.setDisplayShowHomeEnabled(true);
    }

    private void initBottomOperateTabView() {
        bottomOperateTabView.setData(status);
        bottomOperateTabView.undoData();
    }

    private void setupViewPager() {
        viewPagerAdapter = new StatusDetailViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new CommentListFragment(), "转发");
        viewPagerAdapter.addFragment(new CommentListFragment(), "评论");
        viewPagerAdapter.addFragment(new CommentListFragment(), "点赞");
        statusDetailViewPager.setAdapter(viewPagerAdapter);
    }

    void setupTitleAppear() {
        statusDetailCollapsing.setTitle(" ");
        statusDetailAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = false;
            //appbar总共要滚动的高度，是appbar的高度
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                MyLog.v(MyLog.STATUS_DETAIL, "scrollRange: " + scrollRange + "; verticalOffset: " + verticalOffset );
                if(verticalOffset == 0 || verticalOffset == -1) {
                    statusDetailRefresh.setEnabled(true);
                }else {
                    statusDetailRefresh.setEnabled(false);
                }
                if (scrollRange + verticalOffset == 0) {
                    statusDetailCollapsing.setTitle("微博正文");
                    isShow = true;
                } else if(isShow) {
                    statusDetailCollapsing.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }



    /**
     * 处理可以粘顶的view滚动
     */
//    private void setupScrollable() {
//        //设置粘性的可以滚动的view
//        statusDetailScrollable.setDraggableView(statusDetailTabView);
//        statusDetailScrollable.setCanScrollVerticallyDelegate(new CanScrollVerticallyDelegate() {
//            @Override
//            public boolean canScrollVertically(int direction) {
//                return viewPagerAdapter.canScrollVertically(statusDetailViewPager.getCurrentItem(), direction);
//            }
//        });
//
//        statusDetailScrollable.setOnScrollChangedListener(new OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged(int y, int oldY, int maxY) {
//
//                // Sticky behavior
//                final float tabsTranslationY;
//                if (y < maxY) {
//                    tabsTranslationY = .0F;
//                } else {
//                    tabsTranslationY = y - maxY;
//                }
//
//                statusDetailTabView.setTranslationY(tabsTranslationY);
////                statusView.setTranslationY(y / 100);
//            }
//        });
//
//        statusView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @Override
//            public void onGlobalLayout() {
//                // Ensure you call it only once :
//                statusView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//
//                // Here you can get the size :)
//                statusDetailScrollable.setMaxScrollY(statusView.getMeasuredHeight());
//            }
//        });
//    }

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
}
