package com.example.pz.sinaweibosample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.BaseActivity;
import com.example.pz.sinaweibosample.oauth.AccessTokenKeeper;
import com.example.pz.sinaweibosample.presenter.MainPresenter;
import com.example.pz.sinaweibosample.util.AppInfo;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.view.IMainView;
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

    Button loginButton;
    View navigationHeaderView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab_add_weibo)
    FloatingActionButton addWeiboFab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        navigationHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        loginButton = (Button)navigationHeaderView.findViewById(R.id.login_button);
        handleLoginButton();
        //设置actionBar
        setSupportActionBar(toolbar);
        //设置侧边栏item点击监听
        navigationView.setNavigationItemSelectedListener(this);


        //浮动按钮点击监听
        addWeiboFab.setOnClickListener(this);
        //抽屉增加开关
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setImmerseBar();
    }

    private void handleLoginButton() {
        if(loginButton != null) {
            AccessTokenKeeper.clear(this);
            if(AccessTokenKeeper.isTokenValid(this)) {
                //显示用户名和信息、头像
                MyLog.v(MyLog.LOGIN_TAG, "token可用，已登录");
                loginButton.setVisibility(View.GONE);
            }else {
                //显示登陆按钮
                MyLog.v(MyLog.LOGIN_TAG, "token不可用，未登录");
                loginButton.setVisibility(View.VISIBLE);
                loginButton.setOnClickListener(this);
            }
//            loginButton.invalidate();
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
            case R.id.fab_add_weibo:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;

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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
                AccessTokenKeeper.writeToken(MainActivity.this, mAccessToken);
                //登陆成功之后，隐藏登陆按钮
                loginButton.setVisibility(View.GONE);
                MyLog.v(MyLog.LOGIN_TAG, "授权成功，token：" + mAccessToken.getToken());
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
        }
    }
}
