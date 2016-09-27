package com.example.pz.sinaweibosample.base;

import android.app.Application;
import android.content.Context;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by pz on 2016/8/18.
 */
public class MyApplication extends Application{

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //报错时跳出错误处理页面
//        CustomActivityOnCrash.setLaunchErrorActivityWhenInBackground(true);  //错误处理，设为false表示不显示，同时系统错误也将被屏蔽
//        CustomActivityOnCrash.setShowErrorDetails(true); //是否显示报错详情和按钮
//        CustomActivityOnCrash.setEnableAppRestart(true); //重启还是关闭app  true为重启
//        CustomActivityOnCrash.install(this);
        context = getApplicationContext();
        //初始化joda时间库
        JodaTimeAndroid.init(this);
    }

    public static Context getContext() {
        return context;
    }

}
