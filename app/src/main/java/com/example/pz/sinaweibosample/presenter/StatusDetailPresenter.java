package com.example.pz.sinaweibosample.presenter;

import android.content.Context;

import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.view.iview.IStatusDetailView;

/**
 * Created by pz on 2016/9/13.
 */

public class StatusDetailPresenter extends BasePresenter<IStatusDetailView> {

    public StatusDetailPresenter(Context context, IStatusDetailView iView) {
        super(context, iView);
    }

    @Override
    public void destroy() {

    }
}
