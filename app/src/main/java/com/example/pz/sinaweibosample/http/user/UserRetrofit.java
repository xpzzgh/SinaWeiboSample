package com.example.pz.sinaweibosample.http.user;


import com.example.pz.sinaweibosample.model.entity.StatusList;
import com.example.pz.sinaweibosample.model.entity.User;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by pz on 2016/9/1.
 */

public interface UserRetrofit {

    @GET("show.json")
    Observable<User> getUserInfo(@QueryMap Map<String, String> params);

}
