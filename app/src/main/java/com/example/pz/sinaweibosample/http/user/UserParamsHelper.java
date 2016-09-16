package com.example.pz.sinaweibosample.http.user;

import com.example.pz.sinaweibosample.oauth.AccessTokenKeeper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pz on 2016/9/1.
 */

public class UserParamsHelper {

    public static final int COUNT_PER_REQUEST = 20;
    public static final int BASE_APP = 0;

    public static Map<String, String> getUserInfoParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", AccessTokenKeeper.readToken().getToken());
        params.put("uid", AccessTokenKeeper.readToken().getUid());
        return params;
    }

    public static Map<String, String> getOtherUserInfoParams(String uid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", AccessTokenKeeper.readToken().getToken());
        params.put("uid", uid);
        return params;
    }

}
