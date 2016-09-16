package com.example.pz.sinaweibosample.presenter;

import android.content.Context;

import com.example.pz.sinaweibosample.base.BaseObject;
import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.exception.ApiException;
import com.example.pz.sinaweibosample.http.status.StatusParamsHelper;
import com.example.pz.sinaweibosample.http.status.StatusRetrofitClient;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.view.iview.IStatusDetailView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pz on 2016/9/13.
 */

public class StatusDetailPresenter extends BasePresenter<IStatusDetailView> {

    public StatusDetailPresenter(Context context, IStatusDetailView iView) {
        super(context, iView);
    }

    public void fillStatusDetail(String statusId) {
        if(statusId == null || statusId.isEmpty()) {
            throw new ApiException(new BaseObject("微博Id为空", Constant.ERROR_CODE));
        }
        subscription = StatusRetrofitClient.instanceOf().getStatusById(StatusParamsHelper.getStatusByIdParams(statusId))
                .timeout(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Status, Status>() {
                    @Override
                    public Status call(Status status) {
                        if(status != null) {
                            return status;
                        }
                        throw new ApiException(status);
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
                .subscribe(new BaseSubscriber<Status>() {
                    @Override
                    public void onNext(Status status) {
                        iView.fillStatusData(status);
                        iView.hideProgress();
                    }
                });
    }

    @Override
    public void destroy() {

    }
}
