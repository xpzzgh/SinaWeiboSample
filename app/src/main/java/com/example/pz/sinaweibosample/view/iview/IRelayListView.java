package com.example.pz.sinaweibosample.view.iview;

import com.example.pz.sinaweibosample.base.IView;
import com.example.pz.sinaweibosample.model.entity.Status;

import java.util.List;

/**
 * Created by pz on 2016/9/15.
 */

public interface IRelayListView extends IView {
    void fillData(List<Status> relayList);
    void updateTabTitle(int count);
}
