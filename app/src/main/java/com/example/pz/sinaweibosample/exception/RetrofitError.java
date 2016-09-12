package com.example.pz.sinaweibosample.exception;

import com.example.pz.sinaweibosample.base.BaseObject;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.MyLog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Func1;

/**
 * Created by pz on 2016/9/1.
 */

public class RetrofitError {

    static Gson gson;

    static {
        if(gson == null) {
            gson = new Gson();
        }
    }

    public static BaseObject parse(Throwable e) {
        if(e instanceof HttpException) {
            try {
                MyLog.e(MyLog.BASE_TAG, "HTTP错误！");
                String errorInfo = ((HttpException)e).response().errorBody().source().readUtf8Line();
                BaseObject errorObject;
                try{
                    errorObject = gson.fromJson(errorInfo, BaseObject.class);
                }catch (IllegalStateException | JsonSyntaxException exception) {
                    errorObject = new BaseObject();
                    errorObject.setError("http返回错误");
                    errorObject.setError_code(Constant.CAPACITY_CODE);
                }
                return errorObject;
            } catch (IOException e1) {}
        }else if(e instanceof ApiException) {
            BaseObject errorObject = ((ApiException)e).getErrorObject();
            if(errorObject == null) {
                errorObject.setError("请求结果为空");
                errorObject.setError_code(Constant.NULL_CODE);
            }else if(errorObject.getError_code() == 0) {
                errorObject.setError_code(Constant.NO_MORE_CODE);
                errorObject.setError("没有更多数据了！");
            }
            return errorObject;
        }
        BaseObject errorObject = new BaseObject();
        errorObject.setError_code(111);
        errorObject.setError("其他错误");
        MyLog.e(MyLog.LOGIN_TAG, e.toString());
        return errorObject;
    }
//
//    public static BaseObject getBaseObject(BaseObject baseObject) {
//        if(baseObject != null && baseObject.getError_code() == 0) {
//            return baseObject;
//        }else if(baseObject != null){
//            throw new ApiException(baseObject);
//        }else {
//            BaseObject errorObject = new BaseObject();
//            errorObject.setError("空值错误");
//            errorObject.setError_code(000);
//            throw new ApiException(errorObject);
//        }
//    }
}
