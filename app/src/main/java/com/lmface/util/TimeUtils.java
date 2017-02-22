package com.lmface.util;

import java.text.SimpleDateFormat;

/**
 * Created by ice on 2015/12/17.
 */
public class TimeUtils {
    public static String getFormaData(long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        return format.format(time);
    }
    public static String getFormaDatass(long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(time);
    }
}
