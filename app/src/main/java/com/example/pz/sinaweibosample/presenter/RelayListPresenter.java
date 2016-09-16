package com.example.pz.sinaweibosample.presenter;

import android.content.Context;

import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.exception.ApiException;
import com.example.pz.sinaweibosample.http.status.StatusParamsHelper;
import com.example.pz.sinaweibosample.http.status.StatusRetrofitClient;
import com.example.pz.sinaweibosample.model.entity.CommentList;
import com.example.pz.sinaweibosample.model.entity.RelayList;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.view.iview.IRelayListView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pz on 2016/9/15.
 */

public class RelayListPresenter extends BasePresenter<IRelayListView> {

    public RelayListPresenter(Context context, IRelayListView iView) {
        super(context, iView);
    }

    public void fillRelayList(String statusId, int page) {
        subscription = StatusRetrofitClient.instanceOf().getRalays(StatusParamsHelper.getRelaysParams(statusId, page))
                .timeout(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<RelayList, RelayList>() {
                    @Override
                    public RelayList call(RelayList relayList) {
                        if(relayList != null && relayList.getTotal_number() != 0) {
                            iView.updateTabTitle(relayList.getTotal_number());
                        }
                        return relayList;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<RelayList, List<Status>>() {
                    @Override
                    public List<Status> call(RelayList relayList) {
                        if(relayList == null || relayList.getReposts() == null || relayList.getReposts().size() == 0) {
                            throw new ApiException(relayList);
                        }else {
                            return relayList.getReposts();
                        }
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
//                        iView.showProgress();
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
                    public void onNext(List<Status> statusList) {
                        iView.fillData(statusList);
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
