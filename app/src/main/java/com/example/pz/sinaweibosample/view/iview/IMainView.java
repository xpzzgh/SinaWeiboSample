package com.example.pz.sinaweibosample.view.iview;

import com.example.pz.sinaweibosample.base.IView;
import com.example.pz.sinaweibosample.model.entity.User;

/**
 * Created by pz on 2016/8/31.
 */

public interface IMainView extends IView{

    void fillUserInfo(User user);
    void hideLoginButton();
    void openDrawer();
}
