package com.example.pz.sinaweibosample.presenter;

import android.content.Context;

import com.example.pz.sinaweibosample.base.BaseObject;
import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.exception.RetrofitError;
import com.example.pz.sinaweibosample.http.user.UserParamsHelper;
import com.example.pz.sinaweibosample.http.user.UserRetrofitClient;
import com.example.pz.sinaweibosample.model.entity.User;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.util.PrefUtil;
import com.example.pz.sinaweibosample.view.iview.IMainView;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pz on 2016/8/31.
 */

public class MainPresenter extends BasePresenter<IMainView> {

    public MainPresenter(Context context, IMainView iView) {
        super(context, iView);
    }

    public void getUserInfo() {
        subscription = UserRetrofitClient.instanceOf().getUserInfo(UserParamsHelper.getUserInfoParams())
                .timeout(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map(new Func1<User, BaseObject>() {
                    @Override
                    public BaseObject call(User user) {
                        BaseObject object = RetrofitError.getBaseObject(user);
                        if(object instanceof User) {
                            return object;
                        }else {
                            return object;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseObject>() {
                    @Override
                    public void onCompleted() {
                        MyLog.v(MyLog.STATUS_TAG, "complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseObject errorObject = RetrofitError.parse(e);
                        iView.showErrorInfo(errorObject.getError());
                    }

                    @Override
                    public void onNext(BaseObject object) {
                        if(object instanceof User) {
                            User user = (User)object;
                            PrefUtil.setUserInfo(user);
                            MyLog.v(MyLog.USER_TAG, user.getName());
                            iView.fillUserInfo(user);
                        }

                }
            });
    }

    @Override
    public void destroy() {
        if(subscription != null) {
            subscription.unsubscribe();
        }
    }
}
