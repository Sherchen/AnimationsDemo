package com.sherchen.wanjiedemo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hirondelle.date4j.DateTime;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/27
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class CommonUtils {

    public static String getDisplayTime(long postTime) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String nowS = sdf.format(Calendar.getInstance().getTime());
        if(!DateTime.isParseable(nowS)) return "";
        DateTime now = new DateTime(nowS);
        String postS = sdf.format(postTime);
        if(!DateTime.isParseable(postS)) return "";
        DateTime post = new DateTime(postS);
        if(now.getYear().intValue() == post.getYear().intValue()) {//当年
            if(post.isSameDayAs(now)){//当天
                int extraHour = now.getHour() - post.getHour();
                if(extraHour >= 1){
                    return extraHour + "小时前";
                }

                int extraMinute = now.getMinute() - post.getMinute();
                if(extraMinute >= 1){
                    return extraMinute + "分钟前";
                }
                return "刚刚";
            }else if(post.numDaysFrom(now) == 1){//昨天
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                return "昨天" + sdf1.format(new Date(postTime));
            }else {
                SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日 HH:mm");
                return sdf2.format(new Date(postTime));
            }
        }else{
            return postS;
        }
    }

}
