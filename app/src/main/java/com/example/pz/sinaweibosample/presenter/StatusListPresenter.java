package com.example.pz.sinaweibosample.presenter;

import android.content.Context;

import com.example.pz.sinaweibosample.base.BaseObject;
import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.exception.ApiException;
import com.example.pz.sinaweibosample.exception.RetrofitError;
import com.example.pz.sinaweibosample.http.status.StatusParamsHelper;
import com.example.pz.sinaweibosample.http.status.StatusRetrofitClient;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.model.entity.StatusList;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.view.iview.IStatusListView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by pz on 2016/9/9.
 */

public class StatusListPresenter extends BasePresenter<IStatusListView> {

    private static final int RETRY_TIME = 2;

    public StatusListPresenter(Context context, IStatusListView iView) {
        super(context, iView);
    }

    public void fillStatusList(int page, int type) {

        Observable<StatusList> observable;

        if(type == Constant.FRIENDS_TYPE) {
            observable = StatusRetrofitClient.instanceOf().
                    getFriendsStatuses(StatusParamsHelper.getFriendsStatusesParams(page));
        }else {
            observable = StatusRetrofitClient.instanceOf().
                    getPublicStatuses(StatusParamsHelper.getPublicStatusParams(page));
        }

        subscription = observable
                .timeout(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map(new Func1<StatusList, List<Status>>() {
                    @Override
                    public List<Status> call(StatusList statusList) {
                        if(statusList != null) {
                            List<Status> statuses = statusList.getStatuses();
                            if(statuses != null && statuses.size() != 0) {
                                return statuses;
                            }
                        }
                        throw new ApiException(statusList);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        iView.showProgress();
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
                        iView.fillDataList(statuses);
                        iView.hideProgress();
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
