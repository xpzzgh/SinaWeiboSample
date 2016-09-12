package com.example.pz.sinaweibosample.base;

/**
 * Created by pz on 2016/8/18.
 */
public interface IView {
    void showProgress();
    void hideProgress();
    void showSnackInfo(String errorString, int errorCode);
}
