package com.example.pz.sinaweibosample.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.http.status.StatusParamsHelper;
import com.example.pz.sinaweibosample.http.status.StatusRetrofitClient;
import com.example.pz.sinaweibosample.model.entity.Album;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.service.PostStatusService;
import com.example.pz.sinaweibosample.view.iview.IPostStatusView;

import java.io.UnsupportedEncodingException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pz on 2016/9/23.
 */

public class PostStatusPresenter extends BasePresenter<IPostStatusView> {

    public static final int TYPE_TEXT_POST_STATUS = 1001;
    public static final int TYPE_IMAGE_POST_STATUS = 1002;

    public PostStatusPresenter(Context context, IPostStatusView iView) {
        super(context, iView);
    }

    public boolean sendStatus(String text, int visible, Album selectedAlbum, String annotations) {
        Intent intent = new Intent(context, PostStatusService.class);
        intent.putExtra("text", text);
        intent.putExtra("visible", visible);
        if(selectedAlbum != null && selectedAlbum.getImageInfoList().size() > 0) {
            intent.putExtra("pic", selectedAlbum);
            intent.putExtra("type", TYPE_IMAGE_POST_STATUS);
        }else {
            intent.putExtra("type", TYPE_TEXT_POST_STATUS);
        }
        intent.putExtra("annotations", annotations);
        context.startService(intent);
        return true;
    }

    @Override
    public void destroy() {
        if(subscription != null) {
            subscription.unsubscribe();
        }
    }
}
