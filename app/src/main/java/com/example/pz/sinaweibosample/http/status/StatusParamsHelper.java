package com.example.pz.sinaweibosample.http.status;

import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.oauth.AccessTokenKeeper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pz on 2016/9/1.
 */

public class StatusParamsHelper {

    public static final int COUNT_PER_REQUEST = 5;
    public static final int BASE_APP = 0;

    public static Map<String, String> getPublicStatusParams(int page) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", AccessTokenKeeper.readToken().getToken());
        params.put("count", COUNT_PER_REQUEST + "");
        params.put("page", page+"");
        params.put("base_app", BASE_APP+"");
        return params;
    }

    public static Map<String, String> getUserStatusParams(int page) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", AccessTokenKeeper.readToken().getToken());
        params.put("count", COUNT_PER_REQUEST + "");
        params.put("page", page+"");
        params.put("uid", AccessTokenKeeper.readToken().getUid());
        return params;
    }
}
