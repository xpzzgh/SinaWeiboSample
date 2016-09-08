package com.example.pz.sinaweibosample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.ActivityManager;
import com.example.pz.sinaweibosample.base.BaseActivity;
import com.example.pz.sinaweibosample.model.entity.MyKeyValue;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.model.entity.User;
import com.example.pz.sinaweibosample.presenter.UserPresenter;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.view.adapter.SimpleDivideAdapter;
import com.example.pz.sinaweibosample.view.adapter.UserStatusAdapter;
import com.example.pz.sinaweibosample.view.decoration.SimpleDecoration;
import com.example.pz.sinaweibosample.view.iview.IUserView;
import com.example.pz.sinaweibosample.view.widget.FabRecyclerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pz on 2016/9/2.
 */

public class UserActivity extends BaseActivity<UserPresenter> implements IUserView, AppBarLayout.OnOffsetChangedListener,
        SwipyRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnRefreshListener{

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
//    ProgressDialog userDialog;
    private List<Status> statusList;
    private UserStatusAdapter statusAdapter;
    private boolean isRefresh = true;
    int page = 1;

    @BindView(R.id.view_user_head_title)
    LinearLayout mTitleContainer;
    @BindView(R.id.text_user_username)
    TextView mUsername;
    @BindView(R.id.text_user_description)
    TextView mUserDescription;
    @BindView(R.id.view_user_appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.view_user_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.text_user_title)
    TextView mTitle;
    @BindView(R.id.image_user_head)
    SimpleDraweeView mHeadDrawee;
    @BindView(R.id.list_user_statuses)
    FabRecyclerView userStatusesRecycler;

    @BindView(R.id.refresh_user_bottom)
    SwipyRefreshLayout userRefreshBottom;
    @BindView(R.id.refresh_user_top)
    SwipeRefreshLayout userRefreshTop;
    @BindView(R.id.fab_plus_status)
    FloatingActionButton fab;



    SimpleDivideAdapter simpleDivideAdapter;
    List<MyKeyValue> keyValueList;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        user = (User) getIntent().getSerializableExtra(Constant.USER);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user;
    }

    @Override
    public void setTitle() {
    }

    @Override
    protected void initPresenter() {
        presenter = new UserPresenter(this, this, user);
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this);
        //处理用户基本信息列表
        keyValueList = new ArrayList<MyKeyValue>();
        simpleDivideAdapter = new SimpleDivideAdapter(this, keyValueList);
        //初始化用户界面 微博列表
        statusList = new ArrayList<Status>();
//        LinearLayoutManager linearLayoutManager = ;
//        linearLayoutManager.setAutoMeasureEnabled(true);
        userStatusesRecycler.setLayoutManager(new LinearLayoutManager(this));
        statusAdapter = new UserStatusAdapter(this, statusList);
        userStatusesRecycler.setAdapter(statusAdapter);
        userStatusesRecycler.addItemDecoration(new SimpleDecoration(this, R.drawable.divider_status, LinearLayout.VERTICAL));
        userStatusesRecycler.bindFloatButton(fab);
        fab.show();
        //这个方法可以屏蔽recyclerView的滚动方法，是recyclerView在nestedScrollView中平滑滚动
//        userStatusesRecycler.setNestedScrollingEnabled(false);


        userRefreshBottom.setOnRefreshListener(this);
        userRefreshTop.setOnRefreshListener(this);

        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        presenter.fillUserStatusInfo(page);
    }

    @Override
    public void fillUserSimpleInfo(User user) {
        mUsername.setText(user.getName());
        mUserDescription.setText(user.getDescription());
        mHeadDrawee.setImageURI(user.getAvatar_large());
        mTitle.setText(user.getName());
    }

    @Override
    public void fillStatusesInfo(List<Status> statuses) {
        if(isRefresh) {
            statusList.clear();
            statusList.addAll(statuses);
        }else {
            statusList.addAll(statuses);
        }
        statusAdapter.notifyDataSetChanged();
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
    public void showProgress() {

        if(!userRefreshTop.isRefreshing()) {
            userRefreshTop.setRefreshing(true);
        }
    }

    @Override
    public void hideProgress() {

        if(userRefreshTop.isRefreshing()) {
            userRefreshTop.setRefreshing(false);
        }
    }

    @Override
    public void showErrorInfo(String errorString) {
        hideProgress();
        Snackbar.make(mTitleContainer, errorString, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNoMoreStatus() {
        if(page == 1) {
            hideProgress();
        }else {
            hideLoadMoreProgress();
        }
        Snackbar.make(fab, "没有更多微博了！", Snackbar.LENGTH_LONG).show();
        page--;
    }

    @Override
    public void hideLoadMoreProgress() {
        if(userRefreshBottom.isRefreshing()) {
            userRefreshBottom.setRefreshing(false);
        }
    }

    @Override
    public void showLoadMoreProgress() {
        if(!userRefreshBottom.isRefreshing()) {
            userRefreshBottom.setRefreshing(true);
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if(direction == SwipyRefreshLayoutDirection.BOTTOM) {
            page++;
            isRefresh = false;
            presenter.fillUserStatusInfo(page);
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        showProgress();
        isRefresh = true;
        presenter.fillUserStatusInfo(page);
    }

    //    @Override
//    public void loadMore() {
//        MyLog.v(MyLog.USER_TAG, "加载更多！！");
//        page++;
//
//        presenter.fillUserStatusInfo(page);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public void onBackPressed() {
        ActivityManager.instanceOf().finishActivity(this);
    }
}