package com.example.pz.sinaweibosample.presenter;

import android.content.Context;

import com.example.pz.sinaweibosample.base.BasePresenter;
import com.example.pz.sinaweibosample.view.iview.ICommentListView;

/**
 * Created by pz on 2016/9/13.
 */

public class CommentListPresenter extends BasePresenter<ICommentListView> {

    public CommentListPresenter(Context context, ICommentListView iView) {
        super(context, iView);
    }

    @Override
    public void destroy() {

    }
}
