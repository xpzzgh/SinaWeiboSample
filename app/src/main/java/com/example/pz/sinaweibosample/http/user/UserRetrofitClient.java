package com.example.pz.sinaweibosample.http.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pz on 2016/9/1.
 */

public class UserRetrofitClient {

    private static Retrofit retrofit;

    static {
        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weibo.com/2/users/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static UserRetrofit instanceOf() {
        return RetrofitHolder.userRetrofit;
    }

    private static class RetrofitHolder {
        static final UserRetrofit userRetrofit = retrofit.create(UserRetrofit.class);
    }
}
