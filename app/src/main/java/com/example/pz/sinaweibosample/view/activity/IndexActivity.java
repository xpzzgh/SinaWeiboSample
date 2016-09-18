package com.example.pz.sinaweibosample.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.ActivityManager;
import com.example.pz.sinaweibosample.model.entity.Emotion;
import com.example.pz.sinaweibosample.util.PrefUtil;

import rx.Subscription;
import rx.functions.Action0;

/**
 * Created by pz on 2016/9/18.
 */

public class IndexActivity extends AppCompatActivity {

    Subscription subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.instanceOf().add(this);
        setContentView(R.layout.activity_index);
        if(Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int opinion = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(opinion);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }



    @Override
    protected void onResume() {
        super.onResume();

//        initData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e) {}
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(IndexActivity.this, MainActivity.class);
                        startActivity(intent);
                        //fade_in 表示下一个activity淡入，fade_out表示这个activity切换时淡出
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        ActivityManager.instanceOf().finishActivity(IndexActivity.this);
                    }
                });
            }
        }).start();


    }

    void initData() {
        subscription = Emotion.getEmotionList(new Action0() {
            @Override
            public void call() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(1000);
                        }catch (InterruptedException e) {}
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(IndexActivity.this, MainActivity.class);
                                startActivity(intent);
                                //fade_in 表示下一个activity淡入，fade_out表示这个activity切换时淡出
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                ActivityManager.instanceOf().finishActivity(IndexActivity.this);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }
}
