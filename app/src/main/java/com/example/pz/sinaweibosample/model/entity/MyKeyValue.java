package com.example.pz.sinaweibosample.model.entity;

/**
 * Created by pz on 2016/9/2.
 */

public class MyKeyValue {

    String key;
    String value;

    public MyKeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
