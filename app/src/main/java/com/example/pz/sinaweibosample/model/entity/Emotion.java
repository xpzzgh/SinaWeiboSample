package com.example.pz.sinaweibosample.model.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.pz.sinaweibosample.base.BaseObject;
import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.exception.ApiException;
import com.example.pz.sinaweibosample.exception.RetrofitError;
import com.example.pz.sinaweibosample.http.status.StatusParamsHelper;
import com.example.pz.sinaweibosample.http.status.StatusRetrofitClient;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.util.PrefUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pz on 2016/9/18.
 */

public class Emotion extends BaseObject {

//    "category": "休闲",
//            "common": true,
//            "hot": false,
//            "icon": "http://img.t.sinajs.cn/t35/style/images/common/face/ext/normal/eb/smile.gif",
//            "phrase": "[呵呵]",
//            "picid": null,
//            "type": "face",
//            "url": "http://img.t.sinajs.cn/t35/style/images/common/face/ext/normal/eb/smile.gif",
//            "value": "[呵呵]"

    public Emotion() {}

    String category;
    boolean common;
    boolean hot;
    String icon;
    String phrase;
    String picid;
    String type;
    String url;
    String value;

    public static Subscription getEmotionList(final Action0 actionAfterComplete) {
//        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences(PrefUtil.EMOTION_FLAG, Context.MODE_PRIVATE).edit();
//        editor.clear();
//        editor.commit();
        if(PrefUtil.getEmotions() == null) {
            Subscription subscription = StatusRetrofitClient.instanceOf().
                    getEmotions(StatusParamsHelper.getEmotionParams(null))
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<List<Emotion>, List<Emotion>>() {
                        @Override
                        public List<Emotion> call(List<Emotion> emotions) {
                            if(emotions == null || emotions.size() == 0) {
                                throw new ApiException(new BaseObject("没请求到数据", Constant.ERROR_CODE));
                            }
                            return emotions;
                        }
                    })
                    .doOnCompleted(actionAfterComplete)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            try{
                                Toast.makeText(MyApplication.getContext(), "emotion请求出错：" + RetrofitError.parse(throwable).getError(), Toast.LENGTH_SHORT).show();
                                actionAfterComplete.call();
                            }catch(Exception e) {MyLog.e(MyLog.UTIL_TAG, e.toString());}
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<Emotion>>() {
                        @Override
                        public void call(List<Emotion> emotions) {
                            PrefUtil.setEmotions(emotions);
                            MyLog.v(MyLog.UTIL_TAG, "请求到emotion个数为：" + emotions.size());
                        }
                    });
            return subscription;
        }
        actionAfterComplete.call();
        return null;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getPicid() {
        return picid;
    }

    public void setPicid(String picid) {
        this.picid = picid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
