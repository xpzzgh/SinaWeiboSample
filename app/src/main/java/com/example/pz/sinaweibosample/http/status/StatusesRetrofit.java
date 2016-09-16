package com.example.pz.sinaweibosample.http.status;


import com.example.pz.sinaweibosample.model.entity.CommentList;
import com.example.pz.sinaweibosample.model.entity.RelayList;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.model.entity.StatusList;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by pz on 2016/9/1.
 */

public interface StatusesRetrofit {

    @GET("statuses/public_timeline.json")
    Observable<StatusList> getPublicStatuses(@QueryMap Map<String, String> params);

    @GET("statuses/user_timeline.json")
    Observable<StatusList> getUserStatuses(@QueryMap Map<String, String> params);

    @GET("statuses/friends_timeline.json")
    Observable<StatusList> getFriendsStatuses(@QueryMap Map<String, String> params);

    @GET("statuses/show.json")
    Observable<Status> getStatusById(@QueryMap Map<String, String> params);

    @GET("comments/show.json")
    Observable<CommentList> getComments(@QueryMap Map<String, String> params);

    @GET("statuses/repost_timeline.json")
    Observable<RelayList> getRalays(@QueryMap Map<String ,String> params);
}
