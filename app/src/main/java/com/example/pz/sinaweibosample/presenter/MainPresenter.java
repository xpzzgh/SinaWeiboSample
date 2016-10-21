package com.example.pz.sinaweibosample.presenter;

import android.content.Context;

import com.example.pz.sinaweibosample.base.BaseObject;
import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.exception.ApiException;
import com.example.pz.sinaweibosample.exception.RetrofitError;
import com.example.pz.sinaweibosample.http.user.UserParamsHelper;
import com.example.pz.sinaweibosample.http.user.UserRetrofitClient;
import com.example.pz.sinaweibosample.model.entity.User;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.util.PrefUtil;
import com.example.pz.sinaweibosample.view.iview.IMainView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by pz on 2016/8/31.
 */

public class MainPresenter extends BasePresenter<IMainView> {

    public MainPresenter(Context context, IMainView iView) {
        super(context, iView);
    }

    public void getUserInfo() {
        Observable<User> observable = UserRetrofitClient.instanceOf().getUserInfo(UserParamsHelper.getUserInfoParams());
        subscription = observable.timeout(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map(new Func1<User, User>() {
                    @Override
                    public User call(User user) {
                        if(user != null) {
                            return user;
                        }
                        throw new ApiException(user);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable.compose(handleRetryWhen());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<User>() {
                    @Override
                    public void onNext(User user) {
                        PrefUtil.setUserInfo(user);
                        MyLog.v(MyLog.USER_TAG, user.getName());
                        iView.fillUserInfo(user);
                        iView.hideLoginButton();
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
