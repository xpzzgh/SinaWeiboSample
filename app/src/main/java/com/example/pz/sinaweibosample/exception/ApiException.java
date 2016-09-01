package com.example.pz.sinaweibosample.exception;

import com.example.pz.sinaweibosample.base.BaseObject;

/**
 * Created by pz on 2016/9/1.
 */

public class ApiException extends RuntimeException{

    BaseObject errorObject;

    public ApiException(BaseObject errorObject) {
        this.errorObject = errorObject;
    }

    public BaseObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(BaseObject errorObject) {
        this.errorObject = errorObject;
    }
}
