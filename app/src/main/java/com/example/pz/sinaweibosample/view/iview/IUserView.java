package com.example.pz.sinaweibosample.view.iview;

import com.example.pz.sinaweibosample.base.IView;
import com.example.pz.sinaweibosample.model.entity.MyKeyValue;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.model.entity.User;

import java.util.List;

/**
 * Created by pz on 2016/9/2.
 */

public interface IUserView extends IView {

    void fillUserSimpleInfo(User user);

    void fillListInfo(List<MyKeyValue> data);

    void fillStatusesInfo(List<Status> statuses);

    void showProgress();

    void hideProgress();

    void showErrorInfo(String errorString);

    void showNoMoreStatus();
}
