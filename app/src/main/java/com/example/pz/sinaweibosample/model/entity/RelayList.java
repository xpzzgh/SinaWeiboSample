package com.example.pz.sinaweibosample.model.entity;

import com.example.pz.sinaweibosample.base.BaseObject;

import java.util.ArrayList;

/**
 * Created by pz on 2016/9/15.
 */

public class RelayList extends BaseObject {

    /** 微博列表 */
    public ArrayList<Status> reposts;
    public String previous_cursor;
    public String next_cursor;
    public int total_number;

    public String getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(String next_cursor) {
        this.next_cursor = next_cursor;
    }

    public String getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(String previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public ArrayList<Status> getReposts() {
        return reposts;
    }

    public void setReposts(ArrayList<Status> reposts) {
        this.reposts = reposts;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }
}
