package com.example.pz.sinaweibosample.model.entity;

import com.example.pz.sinaweibosample.base.BaseObject;

/**
 * Created by pz on 2016/10/21.
 */

public class PushExtra extends BaseObject {

    private String id;
    private String title;

    public String toString() {
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
