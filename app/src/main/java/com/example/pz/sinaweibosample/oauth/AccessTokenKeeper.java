package com.example.pz.sinaweibosample.oauth;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pz.sinaweibosample.util.MyLog;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by pz on 2016/8/31.
 */

public class AccessTokenKeeper {

    private static final String PREFERENCES_NAME = "com_example_pz_sample_oAuth";

    private static final String KEY_UID           = "uid";
    private static final String KEY_ACCESS_TOKEN  = "access_token";
    private static final String KEY_EXPIRES_IN    = "expires_in";

    public static void writeToken(Context context, Oauth2AccessToken mAccessToken) {
        if(context == null || mAccessToken == null) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_UID, mAccessToken.getUid());
        editor.putString(KEY_ACCESS_TOKEN, mAccessToken.getToken());
        editor.putLong(KEY_EXPIRES_IN, mAccessToken.getExpiresTime());
        editor.commit();
    }

    public static Oauth2AccessToken readToken(Context context) {
        if(context == null) {
            return null;
        }

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Oauth2AccessToken mAccessToken = new Oauth2AccessToken();
        mAccessToken.setUid(pref.getString(KEY_UID, ""));
        mAccessToken.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));
        mAccessToken.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
        return mAccessToken;
    }

    public static void clear(Context context) {
        if(context == null) {
            return ;
        }

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static boolean isTokenValid(Context context) {
        Oauth2AccessToken mAccessToken = readToken(context);
        if(mAccessToken.isSessionValid()) {
            long expireTime = mAccessToken.getExpiresTime();
            long currentTimeMillis = System.currentTimeMillis();
            MyLog.v(MyLog.LOGIN_TAG, "当前时间戳：" + currentTimeMillis + "; 有效时间戳：" + expireTime);
            if(currentTimeMillis <= expireTime) {
                return true;
            }
        }
        return false;
    }
}
