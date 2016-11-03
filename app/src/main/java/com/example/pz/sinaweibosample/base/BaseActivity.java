package com.example.pz.sinaweibosample.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.example.pz.sinaweibosample.util.MyLog;

import java.util.Stack;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by pz on 2016/8/18.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    public T presenter;
    public Unbinder unbinder;
    public ActivityManager activityManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManager = ActivityManager.instanceOf();  //初始化ActivityManager对象
        activityManager.add(this); //默认将继承baseActivity的activity压入堆栈
        setContentView(getLayoutResId());
        //setImmerseBar();
        unbinder = ButterKnife.bind(this); //初始化butterknife
        initPresenter();
        initView();
        showActivityStack();
        MyLog.v(MyLog.BASE_TAG, this.getLocalClassName() + ": OnCreate");
    }

    /**
     * 设置app页面顶端沉浸
     */
    protected void setImmerseBar() {
        if(Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int opinion = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(opinion);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void showActivityStack() {
        Stack<Activity> activityStack =  ActivityManager.instanceOf().getActivityStack();
        String allActivity = "当前所有activity：";
        for(Activity activity:activityStack) {
            allActivity  = allActivity + activity.getLocalClassName() + "；";
        }
        MyLog.v(MyLog.BASE_TAG, allActivity);
    }

    public abstract void initTitle();

    public T getPresenter() {
        return presenter;
    }

    protected abstract int getLayoutResId();

    protected abstract void initPresenter();

    protected abstract void initView();

    @Override
    protected void onDestroy() {
        destroyOperation();
        if(unbinder != null) {
            unbinder.unbind();  //butterKnife 解绑
        }
        MyLog.v(MyLog.BASE_TAG, this.getLocalClassName() + ": OnDestroy");
        presenter = null;
        activityManager.finishActivity(this); //将要destroy的activity移出堆栈
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initTitle();
        MyLog.v(MyLog.BASE_TAG, this.getLocalClassName() + ": OnStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MyLog.v(MyLog.BASE_TAG, this.getLocalClassName() + ": OnRestart");
    }

    @Override
    protected void onPause() {
        pauseOperation();
        JPushInterface.onPause(this);
        super.onPause();
        MyLog.v(MyLog.BASE_TAG, this.getLocalClassName() + ": OnPause");
    }

    @Override
    protected void onStop() {
        presenter.destroy(); //释放presenter资源
        stopOperation();
        MyLog.v(MyLog.BASE_TAG, this.getLocalClassName() + ": OnStop");
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        MyLog.v(MyLog.BASE_TAG, this.getLocalClassName() + ": OnResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveCurrentState(outState);
        super.onSaveInstanceState(outState);
    }
    //保存临时数据，进度等信息
    protected abstract void saveCurrentState(Bundle bundle);

    //停止动画、提交永久性保存、释放系统资源
    protected abstract void pauseOperation();

    //释放泄露内存、占用cpu和时长较长的操作
    protected abstract void stopOperation();

    //destroy之前，执行一些，释放控件的操作,如正在显示的progress
    protected abstract void destroyOperation();

    @Override
    public void onBackPressed() {
        ActivityManager.instanceOf().finishActivity(this);
    }
}
