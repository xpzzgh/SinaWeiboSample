package com.example.pz.sinaweibosample.presenter;

import android.content.Context;

import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.exception.ApiException;
import com.example.pz.sinaweibosample.http.status.StatusParamsHelper;
import com.example.pz.sinaweibosample.http.status.StatusRetrofitClient;
import com.example.pz.sinaweibosample.model.entity.Comment;
import com.example.pz.sinaweibosample.model.entity.CommentList;
import com.example.pz.sinaweibosample.view.iview.ICommentListView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pz on 2016/9/13.
 */

public class CommentListPresenter extends BasePresenter<ICommentListView> {

    public CommentListPresenter(Context context, ICommentListView iView) {
        super(context, iView);
    }

    public void fillCommentListData(String statusId, int page) {
        subscription = StatusRetrofitClient.instanceOf().getComments(StatusParamsHelper.getCommentsParams(statusId, page))
                .timeout(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CommentList, CommentList>() {
                    @Override
                    public CommentList call(CommentList commentList) {
                        if(commentList != null && commentList.getTotal_number() != 0) {
                            iView.updateTabTitle(commentList.getTotal_number());
                        }
                        return commentList;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<CommentList, List<Comment>>() {
                    @Override
                    public List<Comment> call(CommentList commentList) {
                        if (commentList != null) {
                            if (commentList.getComments() != null && commentList.getComments().size() != 0) {
                                return commentList.getComments();
                            }
                        }
                        throw new ApiException(commentList);
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
                .subscribe(new BaseSubscriber<List<Comment>>() {
                    @Override
                    public void onNext(List<Comment> commentList) {
                        iView.fillData(commentList);
                        iView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

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
