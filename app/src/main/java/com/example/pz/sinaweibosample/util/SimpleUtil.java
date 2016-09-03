package com.example.pz.sinaweibosample.util;

/**
 * Created by pz on 2016/9/2.
 */

public class SimpleUtil {

    public static String parseGender(String code) {
        if(code.equals("m")) {
            return "男";
        }else if(code.equals("f")) {
            return "女";
        }else {
            return "未知";
        }
    }
}
