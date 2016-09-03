package com.example.pz.sinaweibosample.exception;

import com.example.pz.sinaweibosample.base.BaseObject;
import com.example.pz.sinaweibosample.util.MyLog;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Func1;

/**
 * Created by pz on 2016/9/1.
 */

public class RetrofitError {

    BaseObject errorObject;
    static Gson gson;

    static {
        if(gson == null) {
            gson = new Gson();
        }
    }

    public static BaseObject parse(Throwable e) {
        if(e instanceof HttpException) {
            try {
                String errorInfo = ((HttpException)e).response().errorBody().source().readUtf8Line();
                BaseObject errorObject = gson.fromJson(errorInfo, BaseObject.class);
                return errorObject;
            } catch (IOException e1) {}
        }else if(e instanceof ApiException) {
            return ((ApiException)e).getErrorObject();
        }
        BaseObject errorObject = new BaseObject();
        errorObject.setError_code(111);
        errorObject.setError("其他错误");
        MyLog.e(MyLog.LOGIN_TAG, e.toString());
        return errorObject;
    }

    public static BaseObject getBaseObject(BaseObject baseObject) {
        if(baseObject != null && baseObject.getError_code() == 0) {
            return baseObject;
        }else if(baseObject != null){
            throw new ApiException(baseObject);
        }else {
            BaseObject errorObject = new BaseObject();
            errorObject.setError("空值错误");
            errorObject.setError_code(000);
            throw new ApiException(errorObject);
        }
    }
}
