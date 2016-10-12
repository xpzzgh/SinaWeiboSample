package com.example.pz.sinaweibosample.http.status;


import com.example.pz.sinaweibosample.model.entity.CommentList;
import com.example.pz.sinaweibosample.model.entity.Emotion;
import com.example.pz.sinaweibosample.model.entity.RelayList;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.model.entity.StatusList;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    @GET("emotions.json")
    Observable<List<Emotion>> getEmotions(@QueryMap Map<String, String> params);

    @Multipart
    @POST("statuses/upload.json")
    Observable<Status> postImageStatus(@PartMap Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST("statuses/update.json")
    Observable<Status> postStatus(@FieldMap Map<String, Object> params);
}
