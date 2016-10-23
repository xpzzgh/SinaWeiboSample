package com.example.pz.sinaweibosample.util;

/**
 * app 信息
 * Created by pz on 2016/8/30.
 */

public class AppInfo {

    /**
     * app key
     */
    public static final String APP_KEY = "1867364799";

    /**
     * app 秘钥
     */
    public static final String APP_SECRET = "758735665efa1c41afd61644ade44a75";

    /**
     * 项目包名
     */
    public static final String PACKAGE_NAME = "com.example.pz.sinaweibosample";

    /**
     * 项目 签名
     */
//    public static final String PACKAGE_SIGN = "382d8d7ff555f8c3f68ee10b73e11898";
    public static final String PACKAGE_SIGN = "5f060b949d670c08289485a7562031d7";

    /**
     * 权限请求的范围
     */
    public static final String SCOPE = "";

    /**
     * 登陆成功之后的默认跳转页面, 需与开放平台中的 “OAuth授权设置-授权回调页” 一致
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * 新浪微博开放平台 注册名
     */
    public static final String APP_NAME = "bigZ微博儿小助手";

    /**
     * 授权回调页和取消授权回调页, 需与跳转页一致
     */
    public static final String SIGN_CALLBACK = "https://api.weibo.com/oauth2/default.html";

}
