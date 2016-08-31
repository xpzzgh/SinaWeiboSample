package com.example.pz.sinaweibosample.presenter;

import android.content.Context;

import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.view.IMainView;

/**
 * Created by pz on 2016/8/31.
 */

public class MainPresenter extends BasePresenter<IMainView> {

    public MainPresenter(Context context, IMainView iView) {
        super(context, iView);
    }

    @Override
    public void destroy() {

    }
}
