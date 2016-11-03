package com.example.pz.sinaweibosample.http.status;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.model.entity.Album;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.oauth.AccessTokenKeeper;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.Util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.http.Field;
import retrofit2.http.PartMap;

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

    public static Map<String, Object> getPostStatusParams(String text, int visible, String annotations) throws UnsupportedEncodingException {
        text = "这个人很懒，什么都没有填";
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("status", text);
        fieldMap.put("access_token", AccessTokenKeeper.readToken().getToken());
        fieldMap.put("visible", 0);

        Map<String, Double> locationMap;
        if((locationMap = Util.getLatAndLong()) != null) {
            fieldMap.put("lat", locationMap.get(Constant.LATITUDE));
            fieldMap.put("long", locationMap.get(Constant.LONGITUDE));
        }
        if(annotations != null) {
//            params.put("annotations", RequestBody.create(MediaType.parse("Text/plain"), annotations));
        }
        return fieldMap;
    }

    public static Map<String, RequestBody> getPostStatusParams(String text, int visible, final Album album, String annotations) throws UnsupportedEncodingException {
        final Map<String, RequestBody> params = new HashMap<String, RequestBody>();
        if(text == null || text.isEmpty()) {
            text = "分享图片";
        }
        params.put("access_token", RequestBody.create(MediaType.parse("text/plain"), AccessTokenKeeper.readToken().getToken()));
        params.put("status", RequestBody.create(MediaType.parse("text/plain"), URLEncoder.encode(text, "utf-8")));
        params.put("visible", RequestBody.create(MediaType.parse("Text/plain"), 0 + ""));
        if(album != null && album.getImageInfoList().size() > 0) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            Bitmap bitmap = BitmapFactory.decodeFile(album.getImageInfoList().get(0).getImageFile().getAbsolutePath(), options);
//            int bytes = bitmap.getByteCount();
//            ByteBuffer buffer = ByteBuffer.allocate(bytes);
//            bitmap.copyPixelsToBuffer(buffer);
//            params.put("pic", RequestBody.create(MediaType.parse("image/*"), buffer.array()));
            params.put("pic" + "\"; filename=\"file", RequestBody.create(MediaType.parse("application/octet-stream"), album.getImageInfoList().get(0).getImageFile()));
        }
        Map<String, Double> locationMap;
        if((locationMap = Util.getLatAndLong()) != null) {
            params.put("lat", RequestBody.create(MediaType.parse("Text/plain"), locationMap.get(Constant.LATITUDE) + ""));
            params.put("long", RequestBody.create(MediaType.parse("Text/plain"), locationMap.get(Constant.LONGITUDE) + ""));
        }
        if(annotations != null) {
//            params.put("annotations", RequestBody.create(MediaType.parse("Text/plain"), annotations));
        }
        return params;
    }

    /**
     *
     * @param statusId 要转发的微博ID。
     * @param text  添加的转发文本，必须做URLencode，内容不超过140个汉字，不填则默认为“转发微博”。
     * @param isComment  是否在转发的同时发表评论，0：否、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0 。
     * @return 转发微博所需的参数
     */
    public static Map<String, Object> getRelayStatusParams(String statusId, String text, int isComment) {
        Map<String, Object> fieldMap = new HashMap<String,  Object>();
        fieldMap.put("access_token", AccessTokenKeeper.readToken().getToken());
        fieldMap.put("id", statusId);
        fieldMap.put("status", Util.isEmpty(text) ? "来自XPZ客户端的转发微博" : text);
        fieldMap.put("is_comment", isComment);
        return fieldMap;
    }
}
