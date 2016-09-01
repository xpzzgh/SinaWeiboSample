package com.example.pz.sinaweibosample.base;

import java.io.Serializable;

/**
 * Created by pz on 2016/9/1.
 */

public class BaseObject implements Serializable {
    public String request;
    public int error_code;
    public String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
