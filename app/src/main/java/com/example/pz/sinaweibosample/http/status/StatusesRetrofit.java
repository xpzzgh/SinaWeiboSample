package com.example.pz.sinaweibosample.http.status;


import com.example.pz.sinaweibosample.model.entity.StatusList;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by pz on 2016/9/1.
 */

public interface StatusesRetrofit {

    @GET("public_timeline.json")
    Observable<StatusList> getPublicStatuses(@QueryMap Map<String, String> params);

    @GET("user_timeline.json")
    Observable<StatusList> getUserStatuses(@QueryMap Map<String, String> params);

    @GET("friends_timeline.json")
    Observable<StatusList> getFriendsStatuses(@QueryMap Map<String, String> params);
}
