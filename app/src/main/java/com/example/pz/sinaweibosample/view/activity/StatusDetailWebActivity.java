package com.example.pz.sinaweibosample.view.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.ActivityManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pz on 2016/10/23.
 */

public class StatusDetailWebActivity extends AppCompatActivity {

    @BindView(R.id.layout_web_status_detail)
    WebView statusDetailWebLayout;

    Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail_web);
        unbinder = ButterKnife.bind(this);
        ActivityManager.instanceOf().add(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        WebSettings webSettings = statusDetailWebLayout.getSettings();
        webSettings.setJavaScriptEnabled(true);
        statusDetailWebLayout.loadUrl("http://jayfeng.com/2015/11/07/Android%E6%89%93%E5%8C%85%E7%9A%84%E9%82%A3%E4%BA%9B%E4%BA%8B/");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        ActivityManager.instanceOf().finishActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(unbinder != null) {
            unbinder.unbind();
        }

        super.onDestroy();
    }
}
