package com.example.pz.sinaweibosample.view.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.transition.Slide;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.BaseActivity;
import com.example.pz.sinaweibosample.model.entity.User;
import com.example.pz.sinaweibosample.oauth.AccessTokenKeeper;
import com.example.pz.sinaweibosample.presenter.MainPresenter;
import com.example.pz.sinaweibosample.util.AppInfo;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.view.adapter.StatusListViewPagerAdapter;
import com.example.pz.sinaweibosample.view.fragment.StatusListFragment;
import com.example.pz.sinaweibosample.view.iview.IMainView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter>
        implements IMainView, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    Oauth2AccessToken mAccessToken;

    MenuItem preNavChecked;
    Button loginButton;
    View navigationHeaderView;
    TextView usernameText;
    TextView userDescription;
    SimpleDraweeView userHeadDrawee;
    User user;
    StatusListViewPagerAdapter statusListViewPagerAdapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab_plus_status)
    FloatingActionButton addStatusFab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.view_tab)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setupWindowAnimations();
    }

    void setupWindowAnimations() {
//        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.slide_activity);
//        getWindow().setExitTransition(slide);
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
        presenter = new MainPresenter(this, this);
    }

    @Override
    protected void initView() {
        //实例化token请求对象
        mAuthInfo = new AuthInfo(this, AppInfo.APP_KEY, AppInfo.REDIRECT_URL, AppInfo.SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        //获取侧边栏头部实例
        Menu navMenu = navigationView.getMenu();
        preNavChecked = navMenu.findItem(R.id.nav_weibo);
        navigationHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        usernameText = (TextView)navigationHeaderView.findViewById(R.id.text_username);
        userDescription = (TextView)navigationHeaderView.findViewById(R.id.text_description);
        userHeadDrawee = (SimpleDraweeView)navigationHeaderView.findViewById(R.id.image_head);
        userHeadDrawee.setOnClickListener(this);
        loginButton = (Button)navigationHeaderView.findViewById(R.id.login_button);
        handleLoginButton();
        //设置actionBar
        setSupportActionBar(toolbar);
        //设置侧边栏item点击监听
        navigationView.setNavigationItemSelectedListener(this);
        //浮动按钮点击监听
//        addWeiboFab.setOnClickListener(this);
        //抽屉增加开关
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
//        setImmerseBar();

        //设置viewPager
        setPagerAdapter();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setPagerAdapter() {
        statusListViewPagerAdapter = new StatusListViewPagerAdapter(getSupportFragmentManager());
        statusListViewPagerAdapter.addFragment(StatusListFragment.instanceOf(Constant.FRIENDS_TYPE), "相关微博");
        statusListViewPagerAdapter.addFragment(StatusListFragment.instanceOf(Constant.PUBLIC_TYPE), "最新微博");
//        statusListViewPagerAdapter.addFragment(new StatusListFragment(), "最新微博");
        viewPager.setAdapter(statusListViewPagerAdapter);
    }

    public FloatingActionButton getFab() {
        return addStatusFab;
    }

    @Override
    public void setTitle() {
        getSupportActionBar().setTitle("bigZ微博");
    }

    @Override
    public void fillUserInfo(User user) {
        this.user = user;
        usernameText.setText(user.getName());
        userDescription.setText(user.getDescription());
        userHeadDrawee.setImageURI(user.getAvatar_large());
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideLoginButton() {
        loginButton.setVisibility(View.GONE);
    }

    @Override
    public void showSnackInfo(String errorString, int errorCode) {
        hideProgress();
        Snackbar snackbar = Snackbar.make(addStatusFab, errorString, Snackbar.LENGTH_LONG);
        TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        if(errorCode == Constant.NO_MORE_CODE) {
            tv.setTextColor(getResources().getColor(R.color.colorWhite));
        }else if(errorCode == Constant.ERROR_CODE) {
            tv.setTextColor(getResources().getColor(R.color.colorRed));
        }
        snackbar.setAction("点击重试", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getUserInfo();
            }
        }).show();
    }

    private void handleLoginButton() {
        if(loginButton != null) {
            if(AccessTokenKeeper.isTokenValid()) {
                //显示用户名和信息、头像
                MyLog.v(MyLog.LOGIN_TAG, "token可用，已登录");
                presenter.getUserInfo();
//                loginButton.setVisibility(View.GONE);
            }else {
                //显示登陆按钮
                MyLog.v(MyLog.LOGIN_TAG, "token不可用，未登录");
                loginButton.setVisibility(View.VISIBLE);
                loginButton.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.login_button:
                MyLog.v(MyLog.LOGIN_TAG, "点击了登陆按钮。");
                mSsoHandler.authorize(new MyWeiboAuthlistener());
                break;
            case R.id.fab_plus_status:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.image_head:
                if(user != null && !user.getId().isEmpty()) {
                    Intent intent = new Intent(this, UserActivity.class);
                    intent.putExtra(Constant.USER, user);
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle();
//                    String transitionName = getString(R.string.transition_image_head);
//                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, userHeadDrawee, transitionName).toBundle();
                    startActivity(intent, bundle);
//                    overridePendingTransition(R.transition.fade_activity, R.transition.explode_activity);
                    //fade_in 表示下一个activity淡入，fade_out表示这个activity切换时淡出
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
        }
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
        presenter = null;
        mAuthInfo = null;
        mSsoHandler = null;
        mAccessToken = null;
        user = null;
        statusListViewPagerAdapter = null;
        viewPager = null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if(preNavChecked != null && preNavChecked != item) {
            preNavChecked.setChecked(false);
            item.setChecked(true);
            preNavChecked = item;
            MyLog.v(MyLog.MAIN_TAG, "点击了：" + item.getTitle());
            int checkedId = item.getItemId();

            if (checkedId == R.id.nav_weibo) {

            } else if (checkedId == R.id.nav_gallery) {

            } else if (checkedId == R.id.nav_slideshow) {

            } else if (checkedId == R.id.nav_manage) {

            } else if (checkedId == R.id.nav_share) {

            } else if (checkedId == R.id.nav_send) {

            }
        }
        drawer.closeDrawers();
        // Handle navigation view item clicks here.


        return true;
    }

    private class MyWeiboAuthlistener implements WeiboAuthListener {
        @Override
        public void onCancel() {
            MyLog.v(MyLog.LOGIN_TAG, "授权取消");
        }

        @Override
        public void onComplete(Bundle bundle) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
            if(mAccessToken.isSessionValid()) {
                AccessTokenKeeper.writeToken(mAccessToken);
                //登陆成功之后，隐藏登陆按钮
//                loginButton.setVisibility(View.GONE);
                MyLog.v(MyLog.LOGIN_TAG, "授权成功，token：" + mAccessToken.getToken());
                presenter.getUserInfo();
                //获取viewPager下面的所有fragment，并做一些操作。
                for(int i = 0; i<statusListViewPagerAdapter.getCount(); i++) {
                    ((StatusListFragment)(statusListViewPagerAdapter.getItem(i))).bindOperateAfterLogin();
                }
            }else {
                String code = bundle.getString("code");
                MyLog.v(MyLog.LOGIN_TAG, "授权失败，code：" + code);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            MyLog.e(MyLog.LOGIN_TAG, "授权出错，详情 " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            //获取当前显示的viewPager下的fragment，并做一些操作。
//            Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 0);
//            // based on the current position you can then cast the page to the correct
//            // class and call the method:
//            if (page != null) {
//                ((StatusListFragment)page).bindOperateAfterLogin();
//            }

        }
    }
}
