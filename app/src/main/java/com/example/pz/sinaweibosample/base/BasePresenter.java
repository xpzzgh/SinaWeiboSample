package com.example.pz.sinaweibosample.base;

import android.content.Context;

import com.example.pz.sinaweibosample.exception.RetrofitError;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.MyLog;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by pz on 2016/8/18.
 */
public abstract class BasePresenter<T extends IView>{

    protected T iView;
    protected Context context;
    protected Subscription subscription;

    public BasePresenter(Context context, T iView) {
        this.iView = iView;
        this.context = context;
    }

    public Observable.Transformer<Throwable, Long> handleRetryWhen() {
        return new Observable.Transformer<Throwable, Long>() {
            @Override
            public Observable<Long> call(Observable<Throwable> throwableObservable) {
                return throwableObservable.zipWith(Observable.range(1, Constant.RETRY_TIME + 1), new Func2<Throwable, Integer, Integer>() {
                    @Override
                    public Integer call(Throwable throwable, Integer integer) {
                        if(integer == Constant.RETRY_TIME + 1 && throwable instanceof RuntimeException) {
                            RuntimeException runtimeException = (RuntimeException)throwable;
                            throw runtimeException;
                        }
                        return integer;
                    }
                }).flatMap(new Func1<Integer, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Integer integer) {
                        MyLog.e(MyLog.BASE_TAG, integer + "次!!");
                        return Observable.timer(2, TimeUnit.SECONDS);
                    }
                });
            }
        };
    }



    public abstract class BaseSubscriber<T> extends Subscriber<T> {
        @Override
        public void onCompleted() {
            iView.hideProgress();
        }

        @Override
        public void onError(Throwable e) {
            try {
                MyLog.e(MyLog.BASE_TAG, "onError!!!");
                BaseObject errorObject = RetrofitError.parse(e);
                iView.showSnackInfo(errorObject.getError(), Constant.ERROR_CODE);
            }catch (Exception e1) {
                MyLog.e(MyLog.BASE_TAG, e1.toString());
            }
        }

        public abstract void onNext(T t);
    }

    public abstract void destroy();
}
