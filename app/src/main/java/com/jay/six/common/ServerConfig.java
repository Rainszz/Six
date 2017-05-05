package com.jay.six.common;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class ServerConfig {
    public static final String TX_BASE_URL = "https://api.tianapi.com/";

    public static String getUrl(String type){
        return TX_BASE_URL+type+"/";
    }
}
