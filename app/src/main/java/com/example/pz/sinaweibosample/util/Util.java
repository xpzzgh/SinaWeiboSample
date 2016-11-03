package com.example.pz.sinaweibosample.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.TypedValue;
import android.widget.Toast;

import com.example.pz.sinaweibosample.base.BaseObject;
import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.exception.ApiException;
import com.example.pz.sinaweibosample.model.entity.MyKeyValue;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.model.entity.ThumbNailPic;
import com.example.pz.sinaweibosample.model.entity.User;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by pz on 2016/9/4.
 */

public class Util {

    /**
     * dp值转换成pixel单位
     * @param dp dp值
     * @return pixel值
     */
    public static int dpToPx(int dp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static int spToPx(int sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    /**
     * 根据图片模式（大图、中图、小图），获取图片地址。
     * 优先选择匹配模式，如果没有，则找偏大的图，最后找偏小的图。
     * 都没有的情况下，返回空
     * @param status  微博
     * @param priorityImageSize 图片模式 （大、中、小）
     * @return
     */
    public static List<String> getPriorityImagesUris(Status status, String priorityImageSize) {
        List<String> imageUriList;
        if (status != null && priorityImageSize != null) {
            if ((imageUriList = getImageUris(status, priorityImageSize)) != null) {
                return imageUriList;
            } else {
                if (priorityImageSize.equals(Constant.LARGE_IMAGE)) {
                    if (((imageUriList = getImageUris(status, Constant.MEDIUM_IMAGE)) != null)) {
                        return imageUriList;
                    } else if (((imageUriList = getImageUris(status, Constant.SMALL_IMAGE)) != null)) {
                        return imageUriList;
                    }
                } else if (priorityImageSize.equals(Constant.MEDIUM_IMAGE)) {
                    if (((imageUriList = getImageUris(status, Constant.LARGE_IMAGE)) != null)) {
                        return imageUriList;
                    } else if (((imageUriList = getImageUris(status, Constant.SMALL_IMAGE)) != null)) {
                        return imageUriList;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 图片转换工具
     * @param status
     * @param imageSize
     * @return
     */
    private static List<String> getImageUris(Status status, String imageSize) {
        List<String> realUrls = new ArrayList<String>();
        List<String> smallImageList = new ArrayList<String>();
        if (!isEmpty(imageSize) && status.getPic_urls() != null && status.getPic_urls().size() != 0) {
            List<ThumbNailPic> picUrls = status.getPic_urls();

            for (int i = 0; i < picUrls.size(); i++) {
                String parsedUrl = picUrls.get(i).getThumbnail_pic();
                if (parsedUrl != null && !parsedUrl.isEmpty()) {
                    smallImageList.add(parsedUrl);
                }
            }

            if (imageSize.equals(Constant.SMALL_IMAGE)) {
                return smallImageList;
            } else if (imageSize.equals(Constant.MEDIUM_IMAGE) && status.getBmiddle_pic() != null) {

                return smallImageToBig(smallImageList, status.getBmiddle_pic());
            } else if (imageSize.equals(Constant.LARGE_IMAGE) && status.getOriginal_pic() != null) {
                return smallImageToBig(smallImageList, status.getOriginal_pic());
            }
        }
        return null;
    }

    private static List<String> smallImageToBig(List<String> smallImageList, String bigImage) {
        int count;
        if (bigImage == null || bigImage.isEmpty() || smallImageList == null || (count = smallImageList.size()) == 0) {
            return null;
        }

        String bigImagePrefix = bigImage.substring(0, bigImage.lastIndexOf("/") + 1);
//        bigImage.sub
        MyLog.v(MyLog.UTIL_TAG, "大图的前缀：" + bigImagePrefix);
        String smallImage;
        String convertBigImage;
        List<String> bigImageList = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            smallImage = smallImageList.get(i);
            convertBigImage = bigImagePrefix + smallImage.substring(smallImage.lastIndexOf("/") + 1);
            MyLog.v(MyLog.UTIL_TAG, convertBigImage);
            bigImageList.add(convertBigImage);
        }
        return bigImageList;
//        if(imageSize.equals(Constant.LARGE_IMAGE)) {
//
//        }else if(imageSize.equals(Constant.MEDIUM_IMAGE)) {
//            for(int i = 0; i<count; i++) {
//                smallImage = smallImageList.get(i);
//            }
//        }
    }

//    private static String parseSmallPic(String urlString) {
//        if(urlString == null || urlString.isEmpty()) {
//            throw new RuntimeException("图片地址为空");
//        }
//        if(urlString.contains("=")) {
//            int index = urlString.indexOf("=");
//            String url = urlString.substring(index, urlString.length());
//            if(url != null) {
//                return url;
//            }
//        }
//        return null;
//    }

    /**
     * 拼接转发微博的字符串
     * @param status
     * @return
     */
    public static String pieceRelayBody(Status status) {
        if (status != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("@");
            builder.append(status.getUser().getName().isEmpty() ? "空发布者" : status.getUser().getName());
            builder.append(" : ");
            builder.append(status.getText().isEmpty() ? "空微博" : status.getText());
            return builder.toString();
        }
        return "";
    }

    /**
     * 判断是否为空
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }

        if (o instanceof String) {
            String s = (String) o;
            if (s == null || s.isEmpty()) {
                return true;
            }
        } else if (o instanceof Integer) {
            if (o == Integer.valueOf(0)) {
                return true;
            }
        }else if(o instanceof Status && ((Status) o).isEmpty()) {
            return true;
        }
        return false;
    }


    /**
     * 获取UTC，格式为“EEE MMM dd HH:mm:ss Z yyyy” 的时间字符串距现在的时间
     * @param utcTime
     * @return
     */
    public static String getTimeDiffFromUTC(String utcTime) {
        if (utcTime == null || utcTime.isEmpty()) {
            throw new RuntimeException("时间字符串为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
        String timeDiff;
        try {
            Date date = sdf.parse(utcTime);
            timeDiff = getDimeDiff(date);
            return timeDiff;

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 获取某时间到现在的时间差
     * @param date
     * @return
     */
    public static String getDimeDiff(Date date) {
        if (date == null) {
            throw new ApiException(new BaseObject("时间参数为空", Constant.ERROR_CODE));
        }
        String timeDiff;
        DateTime dtStart = new DateTime(date);
        DateTime dtStop = new DateTime();
        int minuteDiff = Minutes.minutesBetween(dtStart, dtStop).getMinutes();
        if (minuteDiff <= 1) {
            timeDiff = "刚刚";
        } else if (minuteDiff < 60) {
            timeDiff = minuteDiff + "分钟前";
        } else if (minuteDiff < 60 * 24) {
            timeDiff = (int) (minuteDiff / 60f) + "小时前";
        } else if (minuteDiff < 60 * 24 * 30) {
            timeDiff = (int) (minuteDiff / (60 * 24f)) + "天前";
        } else if (minuteDiff < 60 * 24 * 30 * 12) {
            timeDiff = (int) (minuteDiff / (60f * 24 * 30)) + "月前";
        } else {
            timeDiff = (int) (minuteDiff / (60f * 24 * 30 * 12)) + "年前";
        }
        MyLog.v(MyLog.UTIL_TAG, timeDiff);
        return timeDiff;
    }

    public static String timeFormatFromUTC(String utcTime) {
        if (utcTime == null || utcTime.isEmpty()) {
            throw new RuntimeException("时间字符串为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
        String formattedTime;
        try {
            Date date = sdf.parse(utcTime);
            SimpleDateFormat newSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            formattedTime = newSdf.format(date);
            return formattedTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Map<String, String> getUserDataMap(User user) {
        Map<String, String> data = new LinkedHashMap<String, String>();
        data.put("性别", SimpleUtil.parseGender(user.getGender()));
        data.put("粉丝数", user.getFollowers_count() + "");
//        data.put("粉丝数", "235万");
        data.put("关注数", user.getFriends_count() + "");
        data.put("所在地", user.getLocation());
        data.put("简介", user.getDescription());
        data.put("微博数", user.getStatuses_count() + "");
        data.put("认证信息", user.isVerified() ? "已认证" + user.getVerified_reason() : "未认证");
//        data.put("认证信息", "已认证");
        return data;
    }

    public static int getRandomInt(int seed) {
        Random random = new Random();
        return random.nextInt(15);
    }

    /**
     * 数字超过1万时转换成**万
     * @return
     */
    public static String bigNumToStr(int num) {
        int bigNum;
        if (num / 10000 > 0) {
            bigNum = num / 10000;
            return bigNum + "万";
        } else {
            return num + "";
        }
    }

    public static Map<String, Double> getLatAndLong() {
        Map<String, Double> locationMap = new HashMap<String, Double>();

        if (ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MyApplication.getContext(), "没有获取位置的权限", Toast.LENGTH_SHORT).show();
            return null;
        }else {
            LocationManager lm = (LocationManager) MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            Location location = lm.getLastKnownLocation(lm.getBestProvider(criteria, false));
            if(location != null) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                if(latitude != 0d && longitude != 0d) {
                    locationMap.put(Constant.LATITUDE, latitude);
                    locationMap.put(Constant.LONGITUDE, longitude);
                    return locationMap;
                }
            }
        }
        return null;
    }
}
