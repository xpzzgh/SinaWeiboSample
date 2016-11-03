package com.example.pz.sinaweibosample.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.ActivityManager;

/**
 * Created by pz on 2016/10/24.
 */

public class WidgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        ActivityManager.instanceOf().add(this);
    }

    @Override
    public void onBackPressed() {
        ActivityManager.instanceOf().finishActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
