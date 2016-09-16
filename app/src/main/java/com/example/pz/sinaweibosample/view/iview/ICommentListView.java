package com.example.pz.sinaweibosample.view.iview;

import com.example.pz.sinaweibosample.base.IView;
import com.example.pz.sinaweibosample.model.entity.Comment;
import com.example.pz.sinaweibosample.model.entity.CommentList;
import com.example.pz.sinaweibosample.model.entity.RelayList;
import com.example.pz.sinaweibosample.model.entity.Status;

import java.util.List;

/**
 * Created by pz on 2016/9/13.
 */

public interface ICommentListView extends IView {
    void fillData(List<Comment> commentList);
    void updateTabTitle(int count);
}
