package com.example.pz.sinaweibosample.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.model.entity.User;
import com.google.gson.Gson;

/**
 * Created by pz on 2016/9/1.
 */

public class PrefUtil {

    public static final String USER_FLAG = "user";

    static Gson gson;

    static {
        if(gson == null) {
            gson = new Gson();
        }
    }

    public static void setUserInfo(User user) {
        if(user != null && !user.getId().isEmpty()) {
            SharedPreferences.Editor prefsEditor = MyApplication.getContext().getSharedPreferences(USER_FLAG, Context.MODE_PRIVATE).edit();
            String userInfo = gson.toJson(user);
            prefsEditor.putString(USER_FLAG, userInfo);
            prefsEditor.commit();
        }
    }

    public static User getUserInfo() {
        SharedPreferences pref = MyApplication.getContext().getSharedPreferences(USER_FLAG, Context.MODE_PRIVATE);
        String userInfo = pref.getString(USER_FLAG, "");
        if(!userInfo.isEmpty()) {
            User user = gson.fromJson(userInfo, User.class);
            return user;
        }
        return null;
    }
}
