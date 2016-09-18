package com.example.pz.sinaweibosample.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.model.entity.Emotion;
import com.example.pz.sinaweibosample.model.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pz on 2016/9/1.
 */

public class PrefUtil {

    private static final String USER_FLAG = "user";
    public static final String EMOTION_FLAG = "emotion";

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

    public static List<Emotion> getEmotions() {
        SharedPreferences pref = MyApplication.getContext().getSharedPreferences(EMOTION_FLAG, Context.MODE_PRIVATE);
        String emotionList = pref.getString(EMOTION_FLAG, "");
        if(emotionList == null || emotionList.isEmpty()) {
            return null;
        }
        List<Emotion> emotions = gson.fromJson(emotionList, new TypeToken<List<Emotion>>(){}.getType());
        return emotions;
    }

    public static void setEmotions(List<Emotion> emotions) {
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences(EMOTION_FLAG, Context.MODE_PRIVATE).edit();
        String emotionList = gson.toJson(emotions);
        editor.putString(EMOTION_FLAG, emotionList);
        editor.commit();
    }
}
