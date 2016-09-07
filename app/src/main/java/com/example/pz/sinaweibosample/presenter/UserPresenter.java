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

        subscription = StatusRetrofitClient.instanceOf().getUserStatuses(StatusParamsHelper.getUserStatusParams(page))
                .timeout(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map(new Func1<StatusList, List<Status>>() {
                    @Override
                    public List<Status> call(StatusList statusList) {
                        if(statusList != null) {
                            List<Status> statuses = statusList.getStatuses();
                            if(statuses.size() > 0 && page == 1) {
                                User newUser = statuses.get(0).getUser();
                                MyLog.v(MyLog.USER_TAG, "1. 用户数据更新了，微博总数为：" + newUser.getStatuses_count());
                                if(newUser != null) {
                                    user = newUser;
                                    MyLog.v(MyLog.USER_TAG, "2. 用户数据更新了，微博总数为：" + user.getStatuses_count());
                                }
                            }
                            return statuses;
                        }
                        throw new ApiException(statusList);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Status>>() {
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
                    public void onNext(List<Status> statusList) {
                        if(statusList != null || statusList.size() != 0) {
                            int count = statusList.size();
                            for(int i = 0; i<count; i++) {
                                MyLog.v(MyLog.STATUS_TAG, statusList.get(i).toString());
                            }
                            iView.fillStatusesInfo(statusList);
                        }else {
                            iView.showNoMoreStatus();
                        }
                        if(page == 1) {
                            iView.fillUserSimpleInfo(user);
                            iView.fillListInfo(getKeyValueList(user));
                        }
                        iView.hideProgress();
                    }

                    @Override
                    public void onStart() {
                        iView.showProgress();
                    }
                });
    }

    private List<MyKeyValue> getKeyValueList(User user) {
        List<MyKeyValue> keyValueList = new ArrayList<MyKeyValue>();
        keyValueList.add(new MyKeyValue("性别", SimpleUtil.parseGender(user.getGender())));
        keyValueList.add(new MyKeyValue("粉丝数", user.getFollowers_count() + ""));
        keyValueList.add(new MyKeyValue("关注数", user.getFriends_count() + ""));
        keyValueList.add(new MyKeyValue("所在地", user.getLocation()));
        keyValueList.add(new MyKeyValue("简介", user.getDescription()));
        keyValueList.add(new MyKeyValue("微博数", user.getStatuses_count() + ""));
        keyValueList.add(new MyKeyValue("认证信息", user.isVerified()?"已认证" + user.getVerified_reason():"未认证"));
        return keyValueList;
    }

    @Override
    public void destroy() {
        if(subscription != null) {
            subscription.unsubscribe();
        }
    }
}
