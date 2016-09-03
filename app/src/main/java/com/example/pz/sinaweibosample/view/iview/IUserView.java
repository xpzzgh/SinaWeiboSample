package com.example.pz.sinaweibosample.view.iview;

import com.example.pz.sinaweibosample.base.IView;
import com.example.pz.sinaweibosample.model.entity.MyKeyValue;

import java.util.List;

/**
 * Created by pz on 2016/9/2.
 */

public interface IUserView extends IView {

    public void fillUserSimpleInfo();

    public void fillListInfo(List<MyKeyValue> data);

}
