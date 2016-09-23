package com.example.pz.sinaweibosample.presenter;

import android.content.Context;

import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.view.iview.IPostStatusView;

/**
 * Created by pz on 2016/9/23.
 */

public class PostStatusPresenter extends BasePresenter<IPostStatusView> {

    public PostStatusPresenter(Context context, IPostStatusView iView) {
        super(context, iView);
    }

    @Override
    public void destroy() {
        if(subscription != null) {
            subscription.unsubscribe();
        }
    }
}
