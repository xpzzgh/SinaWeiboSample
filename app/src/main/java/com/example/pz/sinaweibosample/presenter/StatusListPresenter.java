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

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pz on 2016/9/9.
 */

public class StatusListPresenter extends BasePresenter<IStatusListView> {

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
        subscription = observable.timeout(10, TimeUnit.SECONDS)
                .map(new Func1<StatusList, List<Status>>() {
                    @Override
                    public List<Status> call(StatusList statusList) {
                        if(statusList != null) {
                            List<Status> statuses = statusList.getStatuses();
                            return statuses;
                        }
                        BaseObject errorObject = new BaseObject();
                        errorObject.setError("请求结果为空");
                        errorObject.setError_code(0);
                        throw new ApiException(errorObject);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Status>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            BaseObject errorObject = RetrofitError.parse(e);
                            iView.showSnackInfo(errorObject.getError(), Constant.ERROR_CODE);
                        }catch (Exception e1) {
                            MyLog.v(MyLog.STATUS_TAG, e1.toString());
                        }
                    }

                    @Override
                    public void onNext(List<Status> statuses) {
                        if(statuses == null || statuses.size() == 0) {
                            iView.showSnackInfo("没有更多数据了", Constant.NO_MORE_CODE);
                        }
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
