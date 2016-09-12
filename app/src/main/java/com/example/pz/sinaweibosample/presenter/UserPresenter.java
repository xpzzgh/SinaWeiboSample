package com.example.pz.sinaweibosample.presenter;

import android.content.Context;

import com.example.pz.sinaweibosample.base.BaseObject;
import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.exception.ApiException;
import com.example.pz.sinaweibosample.exception.RetrofitError;
import com.example.pz.sinaweibosample.http.status.StatusParamsHelper;
import com.example.pz.sinaweibosample.http.status.StatusRetrofitClient;
import com.example.pz.sinaweibosample.model.entity.MyKeyValue;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.model.entity.StatusList;
import com.example.pz.sinaweibosample.model.entity.User;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.util.SimpleUtil;
import com.example.pz.sinaweibosample.view.iview.IUserView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pz on 2016/9/2.
 */

public class UserPresenter extends BasePresenter<IUserView> {

    User user;

    public UserPresenter(Context context, IUserView iView, User user) {
        super(context, iView);
        this.user = user;
    }

    public void fillUserStatusInfo(final int page) {

        Observable<StatusList> observable = StatusRetrofitClient.instanceOf().getUserStatuses(StatusParamsHelper.getUserStatusParams(page));
        subscription = observable
                .subscribeOn(Schedulers.io())
                .timeout(10, TimeUnit.SECONDS)
                .map(new Func1<BaseObject, List<Status>>() {
                    @Override
                    public List<Status> call(BaseObject statusList) {
                        if(statusList != null && statusList instanceof StatusList) {
                            List<Status> statuses = ((StatusList)statusList).getStatuses();
                            if(statuses.size() > 0) {
                                if(page == 1) {
                                    User newUser = statuses.get(0).getUser();
                                    if(newUser != null) {
                                        user = newUser;
                                    }
                                }
                                return statuses;
                            }
                        }
                        throw new ApiException(statusList);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if(page == 1) {
                            iView.showProgress();
                        }else {
                            iView.showLoadMoreProgress();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable.compose(handleRetryWhen());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<Status>>() {
                    @Override
                    public void onNext(List<Status> statuses) {
                        iView.fillStatusesInfo(statuses);
                        if(page == 1) {
                            iView.fillUserSimpleInfo(user);
                            iView.hideProgress();
                        }else {
                            iView.hideLoadMoreProgress();
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
