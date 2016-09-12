package com.example.pz.sinaweibosample.view.iview;

import com.example.pz.sinaweibosample.base.IView;
import com.example.pz.sinaweibosample.model.entity.Status;

import java.util.List;

/**
 * Created by pz on 2016/9/9.
 */

public interface IStatusListView extends IView {

    void hideProgress();

    void fillDataList(List<Status> statusList);

    void showSnackInfo(String infoString, final int code);

    void showProgress();
}
