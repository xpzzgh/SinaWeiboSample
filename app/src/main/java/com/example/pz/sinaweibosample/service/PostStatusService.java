package com.example.pz.sinaweibosample.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.http.status.StatusParamsHelper;
import com.example.pz.sinaweibosample.http.status.StatusRetrofitClient;
import com.example.pz.sinaweibosample.model.entity.Album;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.presenter.PostStatusPresenter;
import com.example.pz.sinaweibosample.view.activity.MainActivity;

import java.io.UnsupportedEncodingException;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by pz on 2016/10/11.
 */

public class PostStatusService extends IntentService {

    private static final int ID = 12301;

    private Subscription subscription;
    private Notification notification;
    Intent notificationIntent;
    PendingIntent pendingIntent;
    NotificationManager notificationManager;
    int statusType;

    public PostStatusService() {
        super("PostStatusService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationIntent = new Intent(this, MainActivity.class);
        notificationManager = (NotificationManager) getSystemService(MyApplication.getContext().NOTIFICATION_SERVICE);
        pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification = getNotification("正在发送", "图文微博", R.drawable.ic_circle, pendingIntent);
        startForeground(ID, notification);
    }

    @Override
    protected void onHandleIntent(Intent intent){
        statusType = intent.getIntExtra("type", PostStatusPresenter.TYPE_TEXT_POST_STATUS);

        String text = intent.getStringExtra("text");
        int visible = intent.getIntExtra("visible", 0);
        Album selectedAlbum = (Album) intent.getSerializableExtra("pic");
        String annotations = intent.getStringExtra("annotations");
        Observable<Status> observable;
        try {
            if(statusType == PostStatusPresenter.TYPE_IMAGE_POST_STATUS) {
                observable = StatusRetrofitClient.uploadInstanceOf().
                        postImageStatus(StatusParamsHelper.getPostStatusParams(text, visible, selectedAlbum, annotations));
            }else {
                observable = StatusRetrofitClient.uploadInstanceOf().
                        postStatus(StatusParamsHelper.getPostStatusParams(text, visible, annotations));
            }
            subscription = observable
                    .subscribe(new Subscriber<Status>() {
                        @Override
                        public void onCompleted() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            notification = getNotification("发送失败", "微博发送失败", R.drawable.ic_close, pendingIntent);
                            notificationManager.notify(ID, notification);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }

                        @Override
                        public void onNext(Status status) {
                            notification = getNotification("发送成功", "微博发送成功", R.drawable.ic_tick, pendingIntent);
                            notificationManager.notify(ID, notification);
                            LocalBroadcastManager.getInstance(MyApplication.getContext()).sendBroadcast(new Intent("com.example.pz.sinaweibosample.view.activity.PostStatusBroadcastReceiver"));
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Notification getNotification(String contentTitle, String contentText, int icon, PendingIntent pi) {

        return new NotificationCompat.Builder(PostStatusService.this)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), icon))
                .setContentIntent(pi)
                .build();
    }

    @Override
    public void onDestroy() {
        if(subscription != null) {
            subscription.unsubscribe();
        }
        notificationManager = null;
        super.onDestroy();
    }
}
