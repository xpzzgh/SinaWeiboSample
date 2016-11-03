package com.example.pz.sinaweibosample.util;

/**
 * Created by pz on 2016/9/2.
 */

public class Constant {
    public static final String MAIN = "main";
    public static final String USER = "user";
    public static final String STATUS = "status";
    public static final String IMAGE = "image";

    public static final String SMALL_IMAGE = "small";
    public static final String MEDIUM_IMAGE = "medium";
    public static final String LARGE_IMAGE = "large";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    /**
     * http返回错误
     */
    public static final int CAPACITY_CODE = 1004;

    public static final int ERROR_CODE = 1001;
    public static final int NO_MORE_CODE = 1002;
    public static final int SUCCESS_CODE = 1003;
    public static final int NULL_CODE = 1004;


    public static final int FRIENDS_TYPE = 101;
    public static final int PUBLIC_TYPE = 102;

    /**
     * 网络请求失败之后，重试的次数
     */
    public static final int RETRY_TIME = 0;

    public static final int COMMENT_TYPE = 11;
    public static final int RELAY_TYPE = 12;

    /**
     * emoji键盘行列数
     */
    public static final int EMOJI_COUNT_COLUMN = 7;
    public static final int EMOJI_COUNT_ROW = 4;
    public static final int EMOJI_COUNT_PER_PAGER = EMOJI_COUNT_COLUMN * EMOJI_COUNT_ROW;

    public static final int EMOJI_VIEW_TAG = 10021;

    public static final int STATUS_MOST_LENGTH = 140;

    /**
     * 微博类型
     */
//    public static final String KEY_COMMENT_STATUS = "comment_status";
    public static final String KEY_COMMENT_RELAY_STATUS = "relay_status";
    public static final String KEY_TYPE_STATUS = "status_type";
    public static final int TYPE_STATUS_WORD = 55;
    public static final int TYPE_STATUS_IMAGE = 66;
    public static final int TYPE_STATUS_RELAY = 77;
    public static final int TYPE_STATUS_COMMENT = 88;
    public static final int TYPE_STATUS_COMMENT_ANSWER = 555;
    public static final int TYPE_STATUS_COMMENT_RELAY = 666;
}
