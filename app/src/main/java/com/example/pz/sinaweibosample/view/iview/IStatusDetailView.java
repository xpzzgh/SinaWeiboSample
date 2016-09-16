package com.example.pz.sinaweibosample.view.iview;

import com.example.pz.sinaweibosample.base.IView;
import com.example.pz.sinaweibosample.model.entity.Status;

/**
 * Created by pz on 2016/9/13.
 */

public interface IStatusDetailView extends IView {
    void fillStatusData(Status status);
}
