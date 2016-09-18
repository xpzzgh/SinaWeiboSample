package com.example.pz.sinaweibosample.http.status;

import android.support.annotation.Nullable;

import com.example.pz.sinaweibosample.oauth.AccessTokenKeeper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pz on 2016/9/1.
 */

public class StatusParamsHelper {

    public static final int STATUS_COUNT_PER_REQUEST = 15;
    public static final int COMMENT_COUNT_PER_REQUEST = 30;
    public static final int BASE_APP = 0;

    public static Map<String, String> getPublicStatusParams(int page) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", AccessTokenKeeper.readToken().getToken());
        params.put("count", STATUS_COUNT_PER_REQUEST + "");
        params.put("page", page+"");
        params.put("base_app", BASE_APP+"");
        return params;
    }

    public static Map<String, String> getUserStatusParams(int page) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", AccessTokenKeeper.readToken().getToken());
        params.put("count", STATUS_COUNT_PER_REQUEST + "");
        params.put("page", page+"");
        params.put("uid", AccessTokenKeeper.readToken().getUid());
        return params;
    }

    public static Map<String, String> getFriendsStatusesParams(int page) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", AccessTokenKeeper.readToken().getToken());
        params.put("count", STATUS_COUNT_PER_REQUEST + "");
        params.put("page", page+"");
        return params;
    }

    public static Map<String, String> getStatusByIdParams(String statusId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", AccessTokenKeeper.readToken().getToken());
        params.put("id", statusId);
        return params;
    }

    public static Map<String, String> getCommentsParams(String statusId, int page) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", AccessTokenKeeper.readToken().getToken());
        params.put("id", statusId);
        params.put("count", COMMENT_COUNT_PER_REQUEST + "");
        params.put("page", page+"");
        return params;
    }

    public static Map<String, String> getRelaysParams(String statusId, int page) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", AccessTokenKeeper.readToken().getToken());
        params.put("id", statusId);
        params.put("count", COMMENT_COUNT_PER_REQUEST + "");
        params.put("page", page+"");
        return params;
    }

    public static Map<String, String> getEmotionParams(@Nullable String type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", AccessTokenKeeper.readToken().getToken());
        if(type != null) {
            params.put("type", type);
        }
        return params;

    }
}
