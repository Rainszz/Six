package com.jay.six.utils;

import java.util.Date;

/**
 * Created by jayli on 2017/5/8 0008.
 */

public class TimeUtils {

    public static String getTime(){
        String time = String.valueOf(new Date().getTime());
        return time.substring(0,time.length()-3);
    }
}
