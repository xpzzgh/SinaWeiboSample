package com.example.pz.sinaweibosample.presenter;

import android.content.Context;

import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.model.entity.MyKeyValue;
import com.example.pz.sinaweibosample.model.entity.User;
import com.example.pz.sinaweibosample.util.SimpleUtil;
import com.example.pz.sinaweibosample.view.iview.IUserView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.util.ObserverSubscriber;
import rx.schedulers.Schedulers;

/**
 * Created by pz on 2016/9/2.
 */

public class UserPresenter extends BasePresenter<IUserView> {

    public UserPresenter(Context context, IUserView iView) {
        super(context, iView);
    }

    public void fillUserInfo(User user) {
        subscription = Observable.just(user).map(new Func1<User, List<MyKeyValue>>() {
            @Override
            public List<MyKeyValue> call(User user) {
                return getKeyValueList(user);
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<MyKeyValue>>() {
            @Override
            public void call(List<MyKeyValue> myKeyValues) {
                iView.fillUserSimpleInfo();
                iView.fillListInfo(myKeyValues);
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

    }
}
