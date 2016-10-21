package com.example.pz.sinaweibosample.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.model.entity.PushExtra;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.view.activity.StatusDetailActivity;
import com.google.gson.Gson;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by pz on 2016/10/20.
 */

public class StatusDetailReceiver extends BroadcastReceiver {

    /**
     * Intent 参数

     JPushInterface.EXTRA_TITLE
     保存服务器推送下来的消息的标题。
     对应 API 消息内容的 title 字段。
     Portal 推送消息界上不作展示
     Bundle bundle = intent.getExtras();
     String title = bundle.getString(JPushInterface.EXTRA_TITLE);
     JPushInterface.EXTRA_MESSAGE
     保存服务器推送下来的消息内容。
     对应 API 消息内容的 message 字段。
     对应 Portal 推送消息界面上的"自定义消息内容”字段。
     Bundle bundle = intent.getExtras();
     String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
     JPushInterface.EXTRA_EXTRA
     保存服务器推送下来的附加字段。这是个 JSON 字符串。
     对应 API 消息内容的 extras 字段。
     对应 Portal 推送消息界面上的“可选设置”里的附加字段。
     Bundle bundle = intent.getExtras();
     String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
     JPushInterface.EXTRA_CONTENT_TYPE
     保存服务器推送下来的内容类型。
     对应 API 消息内容的 content_type 字段。
     Bundle bundle = intent.getExtras();
     String type = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
     JPushInterface.EXTRA_RICHPUSH_FILE_PATH
     SDK 1.4.0 以上版本支持。
     富媒体通消息推送下载后的文件路径和文件名。
     Bundle bundle = intent.getExtras();
     String file = bundle.getString(JPushInterface.EXTRA_RICHPUSH_FILE_PATH);
     JPushInterface.EXTRA_MSG_ID
     SDK 1.6.1 以上版本支持。
     唯一标识消息的 ID, 可用于上报统计等。
     Bundle bundle = intent.getExtras();
     String file = bundle.getString(JPushInterface.EXTRA_MSG_ID);*/

    public static final String ACTION_STATUS_DETAIL = "status_detail_action";
    private static final int NOTIFY_CODE = 2001;

    String title;
    String message;
    String extra;
    String extraContentType;
    NotificationCompat.Builder notificationBuilder;
    PendingIntent pi;
    String id;

    @Override
    public void onReceive(Context context, Intent intent) {

        MyLog.v(MyLog.PUSH_FLAG, "收到自定义的推送消息");
        /**
         * 这三个方法顺序不能搞混
         */
        initValues(intent);
        initPendingIntent();
        initNotification();
        if(extra.isEmpty() || id == null || message == null) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_CODE, notificationBuilder.build());
    }

    /**
     * 从推送信息中初始化相关内容
     * @param intent
     */
    private void initValues(Intent intent) {
        Bundle bundle = intent.getExtras();
//        title = bundle.getString(JPushInterface.EXTRA_TITLE, "微博小助手儿");
        title = "微博小助手儿";
        message = bundle.getString(JPushInterface.EXTRA_MESSAGE, "微博Id内容");
        extra = bundle.getString(JPushInterface.EXTRA_EXTRA, "");
        extraContentType = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
    }

    private void initNotification() {
        notificationBuilder = new NotificationCompat.Builder(MyApplication.getContext())
                .setLargeIcon(BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.ic_circle))
                .setSmallIcon(R.drawable.ic_circle)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);
    }

    private void initPendingIntent() {
        Intent intent = new Intent(ACTION_STATUS_DETAIL);
        Gson gson = new Gson();
        id = gson.fromJson(extra, PushExtra.class).getId();
        if(id == null || id.isEmpty()) {
            intent.putExtra(StatusDetailActivity.STATUS_ID_FLAG, message);
        }else {
            intent.putExtra(StatusDetailActivity.STATUS_ID_FLAG, id);
        }
        pi = PendingIntent.getActivities(MyApplication.getContext(), NOTIFY_CODE, new Intent[] {intent}, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
