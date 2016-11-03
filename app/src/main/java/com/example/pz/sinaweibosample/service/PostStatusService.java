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
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.view.activity.MainActivity;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import okhttp3.RequestBody;
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
        statusType = intent.getIntExtra("type", Constant.TYPE_STATUS_WORD);
        Observable<Status> observable = null;
        try {
            switch (statusType) {
                case Constant.TYPE_STATUS_WORD:
                    observable = StatusRetrofitClient.uploadInstanceOf().postStatus(handleStatusParams(intent));
                    break;
                case Constant.TYPE_STATUS_IMAGE:
                    observable = StatusRetrofitClient.uploadInstanceOf().postImageStatus(handleImageStatusParams(intent));
                    break;
                case Constant.TYPE_STATUS_RELAY:
                    observable = StatusRetrofitClient.instanceOf().relayStatus(handleRelayStatusParams(intent));
                    break;
            }
            if(observable != null) {
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
            }

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

    private Map<String, RequestBody> handleImageStatusParams(Intent intent) throws UnsupportedEncodingException{
        Map<String, RequestBody> params;
        String text = intent.getStringExtra("text");
        int visible = intent.getIntExtra("visible", 0);
        Album selectedAlbum = (Album) intent.getSerializableExtra("pic");
        String annotations = intent.getStringExtra("annotations");
        params = StatusParamsHelper.getPostStatusParams(text, visible, selectedAlbum, annotations);
        return params;
    }

    private Map<String, Object> handleStatusParams(Intent intent) throws UnsupportedEncodingException{
        Map<String, Object> params;
        String text = intent.getStringExtra("text");
        int visible = intent.getIntExtra("visible", 0);
        String annotations = intent.getStringExtra("annotations");
        params = StatusParamsHelper.getPostStatusParams(text, visible, annotations);
        return params;
    }

    /**
     *  intent.putExtra("id", id);
        intent.putExtra("status", text);
        intent.putExtra("isComment", isComment);
        intent.putExtra("type", Constant.TYPE_STATUS_RELAY);
     * @param intent
     * @return
     */
    private Map<String, Object> handleRelayStatusParams(Intent intent) {
        Map<String, Object> params;
        String id = intent.getStringExtra("id");
        String text = intent.getStringExtra("status");
        int isComment = intent.getIntExtra("isComment", 0);
        params = StatusParamsHelper.getRelayStatusParams(id, text, isComment);
        return params;
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
