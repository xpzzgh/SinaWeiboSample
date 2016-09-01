package com.example.pz.sinaweibosample.presenter;

import android.content.Context;
import android.widget.Toast;

import com.example.pz.sinaweibosample.base.BaseObject;
import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.exception.ApiException;
import com.example.pz.sinaweibosample.exception.RetrofitError;
import com.example.pz.sinaweibosample.http.user.UserParamsHelper;
import com.example.pz.sinaweibosample.http.user.UserRetrofitClient;
import com.example.pz.sinaweibosample.model.entity.User;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.util.PrefUtil;
import com.example.pz.sinaweibosample.view.IMainView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit2.adapter.rxjava.HttpException;
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
                .map(new Func1<User, User>() {
                    @Override
                    public User call(User user) {
                        if(user != null && user.getError_code() == 0) {
                            return user;
                        }else if(user != null){
                            throw new ApiException(user);
                        }else {
                            BaseObject errorObject = new BaseObject();
                            errorObject.setError("空值错误");
                            errorObject.setError_code(000);
                            throw new ApiException(errorObject);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        MyLog.v(MyLog.STATUS_TAG, "complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseObject errorObject = RetrofitError.parse(e);
                        iView.showErrorInfo(errorObject.getError());
//                        if(e instanceof HttpException) {
//                            try {
//                                String errorInfo = ((HttpException)e).response().errorBody().source().readUtf8Line();
//                                new Gson().fromJson(errorInfo, BaseObject.class);
//                                iView.showErrorInfo(errorObject.getError());
//                            } catch (IOException e1) {
//                                e1.printStackTrace();
//                            }
//                        }
                    }

                    @Override
                    public void onNext(User user) {
                        PrefUtil.setUserInfo(user);
                        MyLog.v(MyLog.USER_TAG, user.getName());
                        iView.fillUserInfo(user);
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
